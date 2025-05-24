const currentGameId = 1;
const chatUrl = "https://iu-tomcat.servicecluster.de/byteme/api/game/chat";

async function handleLogin() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if (!email || !password) {
    updateStatus("Bitte E-Mail und Passwort eingeben.", "error");
    return;
    }

    updateStatus("Anmelden...", "loading");

    try {
    const response = await fetch("https://iu-tomcat.servicecluster.de/byteme/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
        throw new Error("Anmeldung fehlgeschlagen");
    }

    document.getElementById("loginForm").classList.add("hidden");
    document.getElementById("chatArea").classList.remove("hidden");
    updateStatus("Anmeldung erfolgreich");
    updateConnectionStatus(true);
    fetchMessages();
    setInterval(fetchMessages, 3000);
    } catch (error) {
    updateStatus("Anmeldung fehlgeschlagen: " + error.message, "error");
    updateConnectionStatus(false);
    }
}

async function sendMessage() {
    const text = document.getElementById("messageInput").value;

    if (!text) {
    updateStatus("Bitte eine Nachricht eingeben.", "error");
    return;
    }

    updateStatus("Nachricht wird gesendet...", "loading");

    try {
    const response = await fetch(chatUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ gameId: currentGameId, text })
    });

    if (!response.ok) {
        throw new Error("Fehler beim Senden der Nachricht");
    }

    document.getElementById("messageInput").value = "";
    updateStatus("Nachricht gesendet");
    fetchMessages();
    updateConnectionStatus(true);
    } catch (error) {
    updateStatus("Fehler beim Senden: " + error.message, "error");
    updateConnectionStatus(false);
    }
}

async function fetchMessages() {
    updateStatus("Nachrichten werden geladen...", "loading");

    try {
    const response = await fetch(chatUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ gameId: currentGameId })
    });

    if (!response.ok) {
        throw new Error("Fehler beim Laden der Nachrichten");
    }

    const messages = await response.json();
    updateChatWindow(messages);
    updateStatus("Nachrichten geladen");
    updateConnectionStatus(true);
    } catch (error) {
    updateStatus("Fehler beim Laden: " + error.message, "error");
    updateConnectionStatus(false);
    }
}

function updateChatWindow(messages) {
    const chatWindow = document.getElementById("chatWindow");
    chatWindow.innerHTML = "";
    messages.forEach(message => {
    const div = document.createElement("div");
    const isMe = !message.author || !message.author.id || message.author.email === "test@test.com";
    div.className = "message " + (isMe ? "me" : "other");
    const label = isMe ? "Du" : (message.author?.name || message.author?.email || "User");
    div.textContent = `${label}: ${message.message}`;
    chatWindow.appendChild(div);
    });
    chatWindow.scrollTop = chatWindow.scrollHeight;
}

function updateStatus(message, type = "") {
    const statusEl = document.getElementById("status");
    statusEl.className = type;
    const connectionEl = document.getElementById("connectionStatus");
    if (type === "loading") {
    statusEl.innerHTML = `<span id="spinner"></span>${message}<span id="connectionStatus" class="${connectionEl.className}">${connectionEl.textContent}</span>`;
    } else {
    statusEl.innerHTML = `${message}<span id="connectionStatus" class="${connectionEl.className}">${connectionEl.textContent}</span>`;
    }
}

function updateConnectionStatus(isConnected) {
    const connectionEl = document.getElementById("connectionStatus");
    if (isConnected) {
    connectionEl.textContent = "Verbunden";
    connectionEl.className = "connected";
    } else {
    connectionEl.textContent = "Getrennt";
    connectionEl.className = "disconnected";
    }
}

function logout() {
    fetch("https://iu-tomcat.servicecluster.de/byteme/logout", {
    method: "POST"
    }).finally(() => {
    window.location.href = "../login.html"; // Passt zu deiner Struktur
    });
}