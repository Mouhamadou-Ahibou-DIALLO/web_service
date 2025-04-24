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

    const postsRes = await fetch("http://localhost:4000/posts");
    const posts = await postsRes.json();
    const container = document.getElementById("posts-container");

    posts.forEach(post => {
        const postElement = document.createElement("div");
        postElement.classList.add("post");

        const hashtagsHTML = post.hashtags?.length
            ? `<p><strong>Hashtags :</strong> ${post.hashtags.map(tag => `#${tag}`).join(' ')}</p>` : '';
        const imageHTML = post.image
            ? `<img src="${post.image.url}" alt="${post.image.title}" class="post-image">` : '';
        const commentsHTML = post.comments?.length
            ? `<div><strong>Commentaires :</strong><ul>` +
            post.comments.map(c =>
                `<li><em>${c.date} ${c.hour}</em> - Utilisateur ${c.commentedBy} : ${c.text}</li>`
            ).join('') + `</ul></div>` : '';
        const likedByHTML = post.likedBy?.length
            ? `<p><strong>Likes (${post.likes}) :</strong> ${post.likedBy.map(id => `Utilisateur ${id}`).join(', ')}</p>` : '';
        const sharedByHTML = post.sharedBy?.length
            ? `<p><strong>Partagé par :</strong> ${post.sharedBy.map(id => `Utilisateur ${id}`).join(', ')}</p>` : '';

        postElement.innerHTML = `
            <h3>${post.body || "(Pas de contenu)"}</h3>
            <p><strong>Créé par :</strong> Utilisateur ${post.createdBy}</p>
            <p><strong>Date :</strong> ${post.date} à ${post.hour}</p>
            ${hashtagsHTML}
            ${imageHTML}
            ${likedByHTML}
            ${sharedByHTML}
            ${commentsHTML}
        `;

        container.appendChild(postElement);
    });
};
