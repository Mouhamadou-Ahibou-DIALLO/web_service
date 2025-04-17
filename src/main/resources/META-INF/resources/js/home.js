// document.addEventListener("DOMContentLoaded", () => {
//     const sessionId = sessionStorage.getItem("sessionId");
//
//     fetch("/utilisateur/mySession", {
//         method: "GET",
//         headers: {
//             "Authorization": sessionId
//         }
//     })
//         .then(response => {
//             if (!response.ok) {
//                 window.location.href = "../index.html";
//             } else {
//                 return response.json();
//             }
//         })
//         .then(utilisateur => {
//             if (utilisateur && utilisateur.pseudo) {
//                 document.getElementById("nomUtilisateur").textContent = utilisateur.pseudo;
//             }
//         })
//         .catch(err => {
//             console.error("Erreur vérification session :", err);
//             window.location.href = "../index.html";
//         });
// });

function goProfil() {
    window.location.href = "profil.html";
}

function logout() {
    // const sessionId = sessionStorage.getItem("sessionId");
    //
    // fetch("/utilisateur/logout", {
    //     method: "POST",
    //     headers: {
    //         "Authorization": sessionId
    //     }
    // })
    //     .then(response => {
    //         if (response.ok) {
    //             sessionStorage.clear();
    //             window.location.href = "login.html";
    //         } else {
    //             alert("Échec de la déconnexion.");
    //         }
    //     })
    //     .catch(err => {
    //         console.error("Erreur lors de la déconnexion :", err);
    //     });

    window.location.href = "index.html";
}
