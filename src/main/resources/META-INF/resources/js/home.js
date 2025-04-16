document.addEventListener("DOMContentLoaded", () => {
    fetch("/utilisateur/session", {
        method: "GET",
        credentials: "include"
    })
        .then(response => {
            if (!response.ok) {
                window.location.href = "../index.html";
            } else {
                return response.json();
            }
        })
        .then(utilisateur => {
            if (utilisateur && utilisateur.pseudo) {
                document.getElementById("nomUtilisateur").textContent = utilisateur.pseudo;
            }
        })
        .catch(err => {
            console.error("Erreur vérification session :", err);
            window.location.href = "../index.html";
        });
});

function goProfil() {
    window.location.href = "profil.html";
}

function logout() {
    fetch("/utilisateur/logout", {
        method: "POST",
        credentials: "include"
    })
        .then(response => {
            if (response.ok) {
                window.location.href = "login.html";
            } else {
                alert("Échec de la déconnexion.");
            }
        })
        .catch(err => {
            console.error("Erreur lors de la déconnexion :", err);
        });
}
