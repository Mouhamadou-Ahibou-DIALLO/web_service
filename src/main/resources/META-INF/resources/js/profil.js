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

    //Afficher les posts de l'utilisateur
    const listePostsDiv = document.getElementById("liste-posts");
    const postsRes = await fetch(`/posts/createdBy/${user.id}`);
    const posts = await postsRes.json();

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
            <button>Modifier</button>
            <button>Supprimer</button>
            <hr>
        `;

        listePostsDiv.appendChild(div);
    });
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
