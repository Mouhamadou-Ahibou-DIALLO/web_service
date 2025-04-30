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
    document.getElementById("nomUtilisateur").innerText = user.prenom + " " + user.nom;

    const bandeau = document.createElement("div");
    bandeau.innerText = "Dernière connexion : " + (localStorage.getItem("dernierConnexion") || "inconnue");
    bandeau.style.cssText = "background: #dff0d8; padding: 10px; text-align:center;";
    document.body.insertBefore(bandeau, document.body.firstChild);

    setTimeout(() => bandeau.remove(), 5000);

    document.getElementById("user-nom").innerText = `${user.prenom} ${user.nom}`;
    document.getElementById("user-avatar").src = user.avatar;

    await afficherUtilisateursConnectes();

    const postsRes = await fetch("/posts");
    allPosts = await postsRes.json();
    afficherPagePosts();

    document.getElementById("prev-page").onclick = () => changerPage(-1);
    document.getElementById("next-page").onclick = () => changerPage(1);
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
                <button class="like-btn">👍 ${post.likes} Like(s)</button>
                <button class="comment-btn">💬 ${post.comments?.length || 0} Commentaires</button>
                <button class="share-btn">🔁 Partager</button>
            </div>
        `;

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
