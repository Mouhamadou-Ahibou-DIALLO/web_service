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

    const avatarElement = document.getElementById("avatar");
    if (!user.avatar) {
        const avatar = generateAvatar(user.nom, user.prenom);
        avatarElement.src = avatar;
    } else {
        avatarElement.src = user.avatar;
    }

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
            <button class="btn-modifier">Modifier</button>
            <button class="btn-supprimer">Supprimer</button>
            <hr>
        `;

        div.querySelector(".btn-modifier").onclick = () => ouvrirFormulaireModification(post);
        div.querySelector(".btn-supprimer").onclick = () => confirmerSuppression(post._id);

        listePostsDiv.appendChild(div);
    });
}

function confirmerSuppression(postId) {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce post ?")) {
        fetch(`/posts/${postId}`, { method: "DELETE" })
            .then(() => {
                alert("Post supprimé !");
                showNotification("Post supprimé avec succès !");
                afficherProfil();
            })
            .catch(err => console.error(err));
    }
}

function ouvrirFormulaireModification(post) {
    const popup = document.createElement("div");
    popup.classList.add("popup");
    popup.innerHTML = `
        <div class="popup-content">
            <h3>Modifier le post</h3>
            <label>Contenu :</label><br>
            <textarea id="edit-body">${post.body}</textarea><br>

            <label>Hashtags (séparés par des virgules) :</label><br>
            <input id="edit-hashtags" value="${post.hashtags?.join(',') || ''}"><br>

            <label>Titre de l'image :</label><br>
            <input id="edit-image-title" value="${post.image?.title || ''}"><br>

            <label>Image :</label><br>
            <input type="file" id="edit-image-file"><br><br>

            <button id="btn-enregistrer">Enregistrer</button>
            <button id="btn-annuler">Annuler</button>
        </div>
    `;

    document.body.appendChild(popup);

    document.getElementById("btn-annuler").onclick = () => popup.remove();

    document.getElementById("btn-enregistrer").onclick = async () => {
        const body = document.getElementById("edit-body").value;
        const hashtags = document.getElementById("edit-hashtags").value
            .split(",")
            .map(h => h.trim())
            .filter(h => h);
        const title = document.getElementById("edit-image-title").value;
        const file = document.getElementById("edit-image-file").files[0];

        let image = null;
        if (file) {
            const url = await fileToDataURL(file);
            image = { title, url };
        } else if (post.image) {
            image = { title, url: post.image.url };
        }

        const now = new Date();
        const updatedPost = {
            body,
            hashtags,
            date: now.toLocaleDateString("fr-FR"),
            hour: now.toLocaleTimeString("fr-FR"),
            image
        };

        await fetch(`/posts/${post._id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedPost)
        });

        alert("Post modifié !");
        popup.remove();
        showNotification("Post modifié avec succès !");
        await afficherProfil();
    };
}

function fileToDataURL(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result);
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
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

function generateAvatar(nom, prenom) {
    const initials = (prenom[0] + nom[0]).toUpperCase();
    const color = getRandomColor();

    const svgAvatar = `
        <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg">
            <circle cx="50" cy="50" r="50" fill="${color}" />
            <text x="50%" y="50%" font-size="30" fill="white" text-anchor="middle" alignment-baseline="central">${initials}</text>
        </svg>`;

    return 'data:image/svg+xml;base64,' + btoa(svgAvatar);
}

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

window.addEventListener("DOMContentLoaded", afficherProfil);
