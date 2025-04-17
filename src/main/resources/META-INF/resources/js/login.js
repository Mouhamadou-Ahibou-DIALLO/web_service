document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const mail = document.getElementById("email").value;
    const motPasse = document.getElementById("password").value;

    console.log("mail : " + mail + " motPasse : " + motPasse);

    const response = await fetch("/utilisateur/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ mail, motPasse })
    });

    console.log("Réponse du backend :", response.status);

    if (response.ok) {
        const user = await response.json();
        // sessionStorage.setItem("utilisateur", JSON.stringify(user));
        // sessionStorage.setItem("sessionId", user.sessionId);

        window.location.href = "home.html";
    } else {
        document.getElementById("error").innerText = "Identifiants invalides.";
    }
});
