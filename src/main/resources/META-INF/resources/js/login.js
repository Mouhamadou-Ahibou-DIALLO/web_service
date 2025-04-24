document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const mail = document.getElementById("email").value;
    const motPasse = document.getElementById("password").value;

    const response = await fetch("/utilisateur/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ mail, motPasse })
    });

    if (response.ok) {
        const session = await response.json();
        localStorage.setItem("sessionId", session.sessionId);
        localStorage.setItem("dernierConnexion", new Date().toLocaleString());
        window.location.href = "home.html";
    } else {
        document.getElementById("error").innerText = "Identifiants invalides.";
    }
});
