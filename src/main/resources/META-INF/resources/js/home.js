function goProfil() {
    window.location.href = "profil.html";
}

function logout() {
    const sessionId = localStorage.getItem("sessionId");
    if (sessionId) {
        fetch(`/utilisateur/logout/${sessionId}`, { method: "POST" })
            .finally(() => {
                localStorage.removeItem("sessionId");
                window.location.href = "index.html";
            });
    } else {
        window.location.href = "index.html";
    }
}

let allPosts = [];
let currentPage = 1;
const postsPerPage = 4;
let currentUserId = null;

window.onload = async function () {
    const sessionId = localStorage.getItem("sessionId");
    if (!sessionId) {
        window.location.href = "index.html";
        return;
    }

    const userRes = await fetch(`/utilisateur/session/${sessionId}`);
    if (!userRes.ok) {
        window.location.href = "index.html";
        return;
    }
    const user = await userRes.json();
    currentUserId = user.id;
    document.getElementById("nomUtilisateur").innerText = user.prenom + " " + user.nom;
    document.getElementById("user-nom").innerText = `${user.prenom} ${user.nom}`;
    document.getElementById("user-avatar").src = user.avatar;

    const bandeau = document.createElement("div");
    bandeau.id = "bandeau-message";
    bandeau.style.cssText = "display: none; position: fixed; top: 10px; left: 50%; transform: translateX(-50%); background-color: #004080; color: white; padding: 10px 20px; border-radius: 5px; z-index: 9999; font-weight: bold;";
    document.body.appendChild(bandeau);

    const connectInfo = document.createElement("div");
    connectInfo.innerText = "Dernière connexion : " + (localStorage.getItem("dernierConnexion") || "inconnue");
    connectInfo.style.cssText = "background: #dff0d8; padding: 10px; text-align:center;";
    document.body.insertBefore(connectInfo, document.body.firstChild);
    setTimeout(() => connectInfo.remove(), 5000);

    await afficherUtilisateursConnectes();
    await chargerPosts("recent");

    document.getElementById("prev-page").onclick = () => changerPage(-1);
    document.getElementById("next-page").onclick = () => changerPage(1);
    document.getElementById("sort-select").onchange = (e) => chargerPosts(e.target.value);
    document.getElementById("search-input").oninput = debounce(() => lancerRecherche(), 400);

    const modal = document.getElementById("likes-modal");
    const closeBtn = document.querySelector(".close-btn");
    closeBtn.onclick = () => modal.style.display = "none";
    window.onclick = (event) => {
        if (event.target === modal) modal.style.display = "none";
    };
};

function afficherBandeau(message, couleur = "#004080") {
    const bandeau = document.getElementById("bandeau-message");
    bandeau.style.backgroundColor = couleur;
    bandeau.textContent = message;
    bandeau.style.display = "block";
    setTimeout(() => {
        bandeau.style.display = "none";
    }, 3000);
}

function debounce(func, delay) {
    let timeout;
    return function () {
        clearTimeout(timeout);
        timeout = setTimeout(func, delay);
    };
}

async function chargerPosts(triType = "recent") {
    currentPage = 1;
    let url;
    switch (triType) {
        case "likes": url = "/tri/liked"; break;
        case "comments": url = "/tri/commented"; break;
        default: url = "/tri/recent";
    }
    const res = await fetch(url);
    allPosts = await res.json();
    afficherPagePosts();
}

async function lancerRecherche() {
    const keyword = document.getElementById("search-input").value.trim();
    if (!keyword) {
        await chargerPosts(document.getElementById("sort-select").value);
        return;
    }
    const res = await fetch(`/tri/search?keyword=${encodeURIComponent(keyword)}`);
    allPosts = await res.json();
    currentPage = 1;
    afficherPagePosts();
}

function afficherModalLikes(userAvatarsHtml) {
    const modal = document.getElementById("likes-modal");
    const likesList = document.getElementById("likes-list");
    likesList.innerHTML = userAvatarsHtml.join("");
    modal.style.display = "flex";
}

async function afficherPagePosts() {
    const container = document.getElementById("posts-container");
    container.innerHTML = "";

    const start = (currentPage - 1) * postsPerPage;
    const end = start + postsPerPage;
    const pagePosts = allPosts.slice(start, end);

    for (const post of pagePosts) {
        const postElement = document.createElement("div");
        postElement.classList.add("post-card");

        const hashtagsHTML = post.hashtags?.length
            ? `<div class="hashtags">${post.hashtags.map(tag => `#${tag}`).join(' ')}</div>` : '';
        const imageHTML = post.image
            ? `<img src="${post.image.url}" alt="${post.image.title}" class="post-img">` : '';

        let auteurNom = `Utilisateur ${post.createdBy}`;
        let auteurAvatar = "img/default-avatar.png";

        try {
            const auteurRes = await fetch(`/utilisateur/${post.createdBy}`);
            if (auteurRes.ok) {
                const auteur = await auteurRes.json();
                auteurNom = `${auteur.prenom} ${auteur.nom}`;
                auteurAvatar = auteur.avatar;
            }
        } catch (e) {
            console.warn("Impossible de charger l'auteur du post", post.createdBy);
        }

        const userAlreadyLiked = post.likedBy?.includes(currentUserId);
        const userAlreadyShared = post.sharedBy?.includes(currentUserId);

        const commentsSection = document.createElement("div");
        commentsSection.classList.add("comments-section");
        commentsSection.style.display = "none";

        const postId = post.id;

        postElement.innerHTML = `
            <div class="post-header">
                <div class="post-user">
                    <img src="${auteurAvatar}" class="avatar-small" alt="avatar">
                    <span><strong>${auteurNom}</strong><br>${post.date} à ${post.hour}</span>
                </div>
            </div>
            <div class="post-body">
                <p>${post.body || "(Pas de contenu)"}</p>
                ${imageHTML}
                ${hashtagsHTML}
            </div>
            <div class="post-footer">
                <button class="like-btn ${userAlreadyLiked ? 'liked' : ''}" data-post-id="${postId}">👍 ${post.likes} Like(s)</button>
                <button class="comment-btn">💬 ${post.comments?.length || 0} Commentaires</button>
                <button class="share-btn ${userAlreadyShared ? 'shared' : ''}" data-post-id="${postId}">🔁 Partager</button>
                <button class="view-likes-btn" data-post-id="${postId}">👥 Voir les likes</button>
            </div>
        `;

        postElement.querySelector(".like-btn").onclick = async (e) => {
            const liked = e.target.classList.contains("liked");
            const url = liked ? `/likes/unlike/${postId}/${currentUserId}` : `/likes/like/${postId}/${currentUserId}`;
            const res = await fetch(url, { method: "POST" });
            if (res.ok) afficherBandeau(liked ? "Like retiré." : "Post liké !");
            await rafraichirPost(postId);
        };

        postElement.querySelector(".share-btn").onclick = async () => {
            const res = await fetch(`/share/${postId}/${currentUserId}`, { method: "POST" });
            if (res.ok) afficherBandeau("Post partagé avec succès !");
            await rafraichirPost(postId);
        };

        postElement.querySelector(".view-likes-btn").onclick = async () => {
            const res = await fetch(`/likes/who-liked/${postId}`);
            if (!res.ok) return;

            const userIds = await res.json();
            if (userIds.length === 0) {
                afficherModalLikes(["<p>Aucun utilisateur n’a liké ce post.</p>"]);
                return;
            }

            const avatars = await Promise.all(userIds.map(async (id) => {
                try {
                    const ures = await fetch(`/utilisateur/${id}`);
                    if (ures.ok) {
                        const u = await ures.json();
                        return `<div style="margin: 5px 0;"><img src="${u.avatar}" title="${u.prenom} ${u.nom}" class="avatar-small" style="margin-right:10px;">${u.prenom} ${u.nom}</div>`;
                    }
                } catch (_) {}
                return `<div>Utilisateur ${id}</div>`;
            }));

            afficherModalLikes(avatars);
        };

        const commentBtn = postElement.querySelector(".comment-btn");
        commentBtn.addEventListener("click", () => {
            if (commentsSection.style.display === "none") {
                afficherCommentaires(post, commentsSection);
                commentsSection.style.display = "block";
            } else {
                commentsSection.style.display = "none";
            }
        });

        postElement.appendChild(commentsSection);
        container.appendChild(postElement);
    }

    document.getElementById("page-number").innerText = `Page ${currentPage}`;
    document.getElementById("prev-page").disabled = currentPage === 1;
    document.getElementById("next-page").disabled = end >= allPosts.length;
}

function changerPage(direction) {
    currentPage += direction;
    afficherPagePosts();
}

async function afficherUtilisateursConnectes() {
    const res = await fetch("/utilisateur/getAllconnected");
    const utilisateurs = await res.json();
    const ul = document.getElementById("connected-users");
    ul.innerHTML = "";

    utilisateurs.forEach(utilisateur => {
        const li = document.createElement("li");
        li.innerHTML = `<img src="${utilisateur.avatar}" class="avatar-small"> ${utilisateur.prenom} ${utilisateur.nom}`;
        ul.appendChild(li);
    });

    document.querySelector(".connected-list p").innerHTML = `<strong>Utilisateurs connectés (${utilisateurs.length})</strong>`;
}

async function afficherCommentaires(post, container) {
    container.innerHTML = "";

    for (const comment of post.comments) {
        const commentDiv = document.createElement("div");
        commentDiv.classList.add("comment-item");

        let commentUser = { prenom: "Inconnu", nom: "", avatar: "img/default-avatar.png" };
        try {
            const res = await fetch(`/utilisateur/${comment.commentedBy}`);
            if (res.ok) commentUser = await res.json();
        } catch (_) {}

        commentDiv.innerHTML = `
            <div class="comment-header">
                <img src="${commentUser.avatar}" class="avatar-small">
                <span><strong>${commentUser.prenom} ${commentUser.nom}</strong><br>${comment.date} ${comment.hour}</span>
            </div>
            <div class="comment-text">${comment.text}</div>
            ${comment.commentedBy === currentUserId ? `<button class="delete-comment" data-post-id="${post.id}" data-comment-id="${comment._id}">🗑 Supprimer</button>` : ""}
        `;

        if (comment.commentedBy === currentUserId) {
            commentDiv.querySelector(".delete-comment").onclick = async () => {
                const res = await fetch(`/comments/delete/${post.id}/${comment._id}`, { method: "DELETE" });
                if (res.ok) afficherBandeau("Commentaire supprimé");
                await rafraichirPost(post.id);
            };
        }

        container.appendChild(commentDiv);
    }

    const form = document.createElement("form");
    form.classList.add("comment-form");
    form.innerHTML = `
        <textarea required placeholder="Écrire un commentaire..."></textarea>
        <button type="submit">Envoyer</button>
    `;
    form.onsubmit = async (e) => {
        e.preventDefault();
        const text = form.querySelector("textarea").value.trim();
        if (!text) return;

        const res = await fetch(`/comments/add/${post.id}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                text,
                commentedBy: currentUserId,
                date: new Date().toISOString().split("T")[0],
                hour: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
            })
        });
        if (res.ok) afficherBandeau("Commentaire ajouté");
        await rafraichirPost(post.id);
    };
    container.appendChild(form);
}

async function rafraichirPost(postId) {
    const res = await fetch(`/posts/${postId}`);
    if (!res.ok) return;
    const post = await res.json();

    const index = allPosts.findIndex(p => p.id === postId);
    if (index !== -1) {
        allPosts[index] = post;
        afficherPagePosts();
    }
}