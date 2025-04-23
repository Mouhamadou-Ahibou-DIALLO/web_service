function goProfil() {
    window.location.href = "profil.html";
}

function logout() {
    window.location.href = "index.html";
}

window.onload = function () {
    fetch("http://localhost:4000/posts")
        .then(response => response.json())
        .then(posts => {
            const container = document.getElementById("posts-container");

            posts.forEach(post => {
                const postElement = document.createElement("div");
                postElement.classList.add("post");

                // Hashtags
                const hashtagsHTML = post.hashtags && post.hashtags.length > 0
                    ? `<p><strong>Hashtags :</strong> ${post.hashtags.map(tag => `#${tag}`).join(' ')}</p>`
                    : '';

                // Image
                const imageHTML = post.image
                    ? `<img src="${post.image.url}" alt="${post.image.title}" class="post-image">`
                    : '';

                // Commentaires
                const commentsHTML = post.comments && post.comments.length > 0
                    ? `<div><strong>Commentaires :</strong><ul>` +
                    post.comments.map(c =>
                        `<li><em>${c.date} ${c.hour}</em> - Utilisateur ${c.commentedBy} : ${c.text}</li>`
                    ).join('') + `</ul></div>`
                    : '';

                // Likes
                const likedByHTML = post.likedBy && post.likedBy.length > 0
                    ? `<p><strong>Likes (${post.likes}) :</strong> ${post.likedBy.map(id => `Utilisateur ${id}`).join(', ')}</p>`
                    : `<p><strong>Likes :</strong> 0</p>`;

                // Partages
                const sharedByHTML = post.sharedBy && post.sharedBy.length > 0
                    ? `<p><strong>Partagé par :</strong> ${post.sharedBy.map(id => `Utilisateur ${id}`).join(', ')}</p>`
                    : '';

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
        })
        .catch(error => {
            console.error("Erreur lors de la récupération des posts :", error);
        });
}
