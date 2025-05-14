async function afficherProfil() {
    const sessionId = localStorage.getItem("sessionId");
    if (!sessionId) {
        window.location.href = "index.html";
        return;
    }

    const res = await fetch(`/utilisateur/session/${sessionId}`);
    if (!res.ok) {
        window.location.href = "index.html";
        return;
    }

    const user = await res.json();
    document.getElementById("nom").innerText = user.nom + " " + user.prenom;
    document.getElementById("email").innerText = user.mail;
    document.getElementById("pseudo").innerText = user.pseudo;
    document.getElementById("avatar").src = user.avatar;

    localStorage.setItem("userId", user.id);
    afficherPostsUtilisateur(user.id);
    afficherPostsLikes(user.id);
    afficherPostsCommentaires(user.id);
    afficherPostsPartages(user.id);
}

async function afficherPostsUtilisateur(userId) {
    const container = document.getElementById("user-posts");
    const res = await fetch(`/posts/createdBy/${userId}`);
    const posts = await res.json();
    container.innerHTML = posts.map(p => rendrePostGestion(p)).join("");
}

function rendrePostGestion(post) {
    return `
        <div class="post" data-post-id="${post.id}" data-title="${post.title || ''}" data-body="${post.body || ''}"
             data-hashtags='${JSON.stringify(post.hashtags || [])}' data-image-title="${post.image?.title || ''}"
             data-image-url="${post.image?.url || ''}">
            <h3>${post.title}</h3>
            <p>${post.body}</p>
            ${post.image ? `<img src="${post.image.url}" width="150">` : ""}
            <p><strong>Date :</strong> ${post.date} à ${post.hour}</p>
            <button class="btn-modifier" onclick="ouvrirFormulaireModification('${post.id}')">Modifier</button>
            <button class="btn-supprimer" onclick="confirmerSuppression('${post.id}')">Supprimer</button>
        </div>
    `;
}

async function afficherPostsLikes(userId) {
    const container = document.getElementById("liked-posts");
    const res = await fetch(`/likes/liked/${userId}`);
    const posts = await res.json();
    container.innerHTML = posts.map(p => rendrePostSimple(p)).join("");
}

async function afficherPostsCommentaires(userId) {
    const container = document.getElementById("commented-posts");
    const res = await fetch(`/comments/${userId}`);
    const posts = await res.json();
    container.innerHTML = posts.map(p => rendrePostSimple(p)).join("");
}

async function afficherPostsPartages(userId) {
    const container = document.getElementById("shared-posts");
    const res = await fetch(`/share?userId=${userId}`);
    const posts = await res.json();
    container.innerHTML = posts.map(p => rendrePostSimple(p, true)).join("");
}

function rendrePostSimple(post, avecBoutonSuppression = false) {
    return `
        <div class="post">
            <h3>${post.title}</h3>
            <p>${post.body}</p>
            ${post.image ? `<img src="${post.image.url}" width="150">` : ""}
            ${avecBoutonSuppression ? `<button onclick="supprimerPartage('${post.id}')">Supprimer le partage</button>` : ""}
        </div>
    `;
}

async function supprimerPartage(postId) {
    const userId = localStorage.getItem("userId");
    if (!confirm("Confirmer la suppression du partage ?")) return;
    const res = await fetch(`/share/${postId}/${userId}`, { method: "DELETE" });
    if (res.ok) {
        showNotification("Partage supprimé.");
        afficherPostsPartages(userId);
    } else {
        showNotification("Erreur suppression partage.");
    }
}

function ouvrirFormulaireCreation() {
    document.getElementById("popup-title").innerText = "Créer un post";
    document.getElementById("post-form").reset();
    document.getElementById("post-id").value = "";
    document.getElementById("post-popup").style.display = "block";
}

function ouvrirFormulaireModification(postId) {
    const postDiv = document.querySelector(`.post[data-post-id="${postId}"]`);

    if (postDiv) {
        const title = postDiv.dataset.title;
        const body = postDiv.dataset.body;
        const hashtags = JSON.parse(postDiv.dataset.hashtags);
        const imageTitle = postDiv.dataset.imageTitle;

        document.getElementById("popup-title").innerText = "Modifier le post";
        document.getElementById("post-title").value = title;
        document.getElementById("post-body").value = body;
        document.getElementById("post-hashtags").value = hashtags.join(", ");
        document.getElementById("post-image-title").value = imageTitle;
        document.getElementById("post-id").value = postId;
        document.getElementById("post-popup").style.display = "block";
    } else {
        console.error("Post non trouvé:", postId);
        showNotification("Erreur: Post introuvable");
    }
}

async function confirmerSuppression(postId) {
    if (!confirm("Voulez-vous vraiment supprimer ce post ?")) return;

    try {
        const res = await fetch(`/posts/${postId}`, { method: "DELETE" });
        if (res.ok) {
            showNotification("Post supprimé !");
            const userId = localStorage.getItem("userId");
            afficherPostsUtilisateur(userId);
        } else {
            showNotification("Erreur lors de la suppression !");
        }
    } catch (error) {
        console.error("Erreur suppression:", error);
        showNotification("Erreur réseau !");
    }
}

document.getElementById("btn-new-post").addEventListener("click", () => {
    ouvrirFormulaireCreation();
});

document.getElementById("post-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const userId = localStorage.getItem("userId");
    const title = document.getElementById("post-title").value;
    const body = document.getElementById("post-body").value;
    const hashtags = document.getElementById("post-hashtags").value.split(",").map(tag => tag.trim());
    const imageFile = document.getElementById("post-image-file").files[0];
    const imageTitle = document.getElementById("post-image-title").value;
    const postId = document.getElementById("post-id").value;

    let image = null;
    if (imageFile) {
        const imageUrl = await fileToDataURL(imageFile);
        image = { url: imageUrl, title: imageTitle };
    } else if (postId) {
        const postDiv = document.querySelector(`.post[data-post-id="${postId}"]`);
        if (postDiv && postDiv.dataset.imageUrl) {
            image = {
                url: postDiv.dataset.imageUrl,
                title: imageTitle || postDiv.dataset.imageTitle
            };
        }
    }

    const postData = {
        title,
        date: new Date().toISOString().split("T")[0],
        hour: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        body,
        hashtags,
        image
    };

    try {
        const response = await fetch(postId ? `/posts/${postId}` : `/posts/${userId}`, {
            method: postId ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(postData)
        });
        if (response.ok) {
            showNotification(postId ? "Post modifié avec succès !" : "Post créé avec succès !");
            afficherPostsUtilisateur(userId);
            closePostPopup();
        } else {
            showNotification("Erreur lors de l'envoi du post.");
        }
    } catch (err) {
        console.error("Erreur réseau:", err);
        showNotification("Erreur réseau");
    }
});

function closePostPopup() {
    document.getElementById("post-popup").style.display = "none";
}

function showNotification(message, duration = 3000) {
    const notif = document.getElementById("notification");
    notif.textContent = message;
    notif.style.display = "block";
    setTimeout(() => {
        notif.style.opacity = "0";
        setTimeout(() => {
            notif.style.display = "none";
            notif.style.opacity = "0.95";
        }, 500);
    }, duration);
}

function fileToDataURL(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result);
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}

window.addEventListener("DOMContentLoaded", afficherProfil);