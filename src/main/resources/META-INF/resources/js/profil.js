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

        div.innerHTML = `
            <h3>${post.body || "(Contenu vide)"}</h3>
            <p><strong>Date :</strong> ${post.date} à ${post.hour}</p>
            ${post.image ? `<img src="${post.image.url}" alt="${post.image.title}" width="150">` : ""}
            <br>
            <button class="btn-modifier">Modifier</button>
            <button class="btn-supprimer">Supprimer</button>
            <hr>
        `;

        div.querySelector(".btn-modifier").onclick = () => ouvrirFormulaireModification(post, userId);
        div.querySelector(".btn-supprimer").onclick = () => confirmerSuppression(userId, post);

        listePostsDiv.appendChild(div);
    });
}

function ouvrirFormulaireModification(post, userId) {
    document.getElementById("popup-title").innerText = "Modifier un post";
    document.getElementById("post-id").value = JSON.stringify(post);
    document.getElementById("post-body").value = post.body || "";
    document.getElementById("post-hashtags").value = post.hashtags?.join(",") || "";
    document.getElementById("post-image-title").value = post.image?.title || "";
    document.getElementById("post-image-file").value = "";

    document.getElementById("post-form").onsubmit = async (e) => {
        e.preventDefault();
        const postOrigin = JSON.parse(document.getElementById("post-id").value);
        await sauvegarderModification(postOrigin, userId);
    };

    document.getElementById("edit-popup").style.display = "block";
}

function ouvrirFormulaireCreation() {
    document.getElementById("popup-title").innerText = "Créer un post";
    document.getElementById("post-id").value = "";
    document.getElementById("post-body").value = "";
    document.getElementById("post-hashtags").value = "";
    document.getElementById("post-image-title").value = "";
    document.getElementById("post-image-file").value = "";

    document.getElementById("post-form").onsubmit = async (e) => {
        e.preventDefault();
        await creerNouveauPost();
    };

    document.getElementById("edit-popup").style.display = "block";
}

async function sauvegarderModification(postOrigin, userId) {
    const updatedPost = await construirePost(postOrigin);
    await fetch(`/posts/${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ postOrigin, updatedPost })
    });

    showNotification("Post modifié avec succès !");
    fermerPopup();
    afficherProfil();
}

async function creerNouveauPost() {
    const sessionId = localStorage.getItem("sessionId");
    const userRes = await fetch(`/utilisateur/session/${sessionId}`);
    const user = await userRes.json();

    const post = await construirePost();
    post.createdBy = user.id;

    await fetch(`/posts/${user.id}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(post)
    });

    showNotification("Post créé avec succès !");
    fermerPopup();
    afficherProfil();
}

async function construirePost(basePost = {}) {
    const body = document.getElementById("post-body").value;
    const hashtags = document.getElementById("post-hashtags").value
        .split(",")
        .map(h => h.trim())
        .filter(h => h);
    const title = document.getElementById("post-image-title").value;
    const file = document.getElementById("post-image-file").files[0];

    let image = basePost.image || null;
    if (file) {
        const url = await fileToDataURL(file);
        image = { title, url };
    } else if (title && image) {
        image.title = title;
    }

    const now = new Date();
    return {
        ...basePost,
        body,
        hashtags,
        date: now.toLocaleDateString("fr-FR"),
        hour: now.toLocaleTimeString("fr-FR"),
        image
    };
}

function confirmerSuppression(userId, post) {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce post ?")) {
        fetch(`/posts/${userId}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(post)
        })
            .then(() => {
                showNotification("Post supprimé avec succès !");
                afficherProfil();
            })
            .catch(err => console.error(err));
    }
}

function fermerPopup() {
    document.getElementById("edit-popup").style.display = "none";
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