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
}

async function afficherPostsUtilisateur(userId) {
    const listePostsDiv = document.getElementById("liste-posts");
    const postsRes = await fetch(`/posts/createdBy/${userId}`);
    const posts = await postsRes.json();

    listePostsDiv.innerHTML = "";

    if (posts.length === 0) {
        listePostsDiv.innerHTML = "<p>Vous n'avez encore publié aucun post.</p>";
        return;
    }

    posts.forEach(post => {
        const div = document.createElement("div");
        div.classList.add("post");
        div.dataset.postId = post.id;

        const postId = post.id;

        div.dataset.title = post.title || "";
        div.dataset.body = post.body || "";
        div.dataset.hashtags = post.hashtags ? JSON.stringify(post.hashtags) : "[]";
        div.dataset.imageTitle = post.image ? post.image.title : "";
        div.dataset.imageUrl = post.image ? post.image.url : "";

        div.innerHTML = `
            <h2>${post.title}</h2>
            <h3>${post.body || "(Contenu vide)"}</h3>
            <p><strong>Date :</strong> ${post.date} à ${post.hour}</p>
            <p><strong>ID du post :</strong> ${postId}</p> <!-- pour debug -->
            ${post.image ? `<img src="${post.image.url}" alt="${post.image.title}" width="150">` : ""}
            <br>
            <button class="btn-modifier">Modifier</button>
            <button class="btn-supprimer">Supprimer</button>
            <hr>
        `;

        div.querySelector(".btn-modifier").onclick = () => ouvrirFormulaireModification(postId);
        div.querySelector(".btn-supprimer").onclick = () => confirmerSuppression(postId);

        listePostsDiv.appendChild(div);
    });
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
        if (postId) {
            const response = await fetch(`/posts/${postId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(postData)
            });
            if (response.ok) {
                showNotification("Post modifié avec succès !");
            } else {
                showNotification("Erreur modification !");
            }
        } else {
            const response = await fetch(`/posts/${userId}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(postData)
            });
            if (response.ok) {
                showNotification("Post créé avec succès !");
            } else {
                showNotification("Erreur création !");
            }
        }
        closePostPopup();
        afficherPostsUtilisateur(userId);
    } catch (err) {
        console.error("Erreur lors de l'envoi du post", err);
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

const closePopupButton = document.getElementById("close-popup");
if (closePopupButton) {
    closePopupButton.addEventListener("click", closePostPopup);
}

window.addEventListener("DOMContentLoaded", afficherProfil);