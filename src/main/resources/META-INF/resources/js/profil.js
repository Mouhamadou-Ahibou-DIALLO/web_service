function afficherProfil() {
    const user = JSON.parse(sessionStorage.getItem("utilisateur"));
    if (!user) {
        window.location.href = "../index.html";
        return;
    }

    document.getElementById("nom").innerText = user.prenom + " " + user.nom;
    document.getElementById("email").innerText = user.mail;
    document.getElementById("pseudo").innerText = user.pseudo;
    document.getElementById("avatar").src = user.avatar;
}

afficherProfil();