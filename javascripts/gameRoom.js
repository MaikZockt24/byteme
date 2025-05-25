// Symbole-Animation wie bei Login/Register
const NUM_SYMBOLS = 40;
const symbols = [];
const symbolFiles = ["../images/kreuz.png", "../images/kringel.png"];

for (let i = 0; i < NUM_SYMBOLS; i++) {
    const img = document.createElement("img");
    img.src = symbolFiles[Math.floor(Math.random() * 2)];
    img.className = "symbol";
    img.style.top = `${Math.random() * 100}vh`;
    img.style.left = `${Math.random() * 100}vw`;
    img.style.animationDelay = `${Math.random() * 5}s`;
    document.body.appendChild(img);
    symbols.push(img);
}

// Symbole-Bewegung wie bei Login/Impressum
function swapPositions() {
    const indexA = Math.floor(Math.random() * symbols.length);
    let indexB = Math.floor(Math.random() * symbols.length);
    while (indexA === indexB) {
        indexB = Math.floor(Math.random() * symbols.length);
    }
    const a = symbols[indexA];
    const b = symbols[indexB];
    a.classList.add("teleport");
    b.classList.add("teleport");
    setTimeout(() => {
        a.classList.remove("teleport");
        b.classList.remove("teleport");
    }, 300);
    const tempTop = a.style.top;
    const tempLeft = a.style.left;
    a.style.top = b.style.top;
    a.style.left = b.style.left;
    b.style.top = tempTop;
    b.style.left = tempLeft;
}

setInterval(swapPositions, 500);

setInterval(() => {
    const randomSymbol = symbols[Math.floor(Math.random() * symbols.length)];
    randomSymbol.classList.add("teleport");
    setTimeout(() => {
        randomSymbol.classList.remove("teleport");
    }, 300);
    randomSymbol.style.top = `${Math.random() * 100}vh`;
    randomSymbol.style.left = `${Math.random() * 100}vw`;
}, 1500);

// URL-Parameter auslesen
const params = new URLSearchParams(window.location.search);
const gameId = parseInt(params.get('gameId'), 10);
const token = localStorage.getItem("jwtToken");

if (!token) {
    alert("Bitte melde dich zuerst an.");
    window.location.href = "login.html";
}

// Menü umschalten (aus Lobby.js)
function toggleMenu() {
    const dropdown = document.getElementById("menuDropdown");
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
}

// Spiel verlassen (aus Lobby.js)
async function leaveGame(event) {
    event.preventDefault();
    try {
        const response = await fetch('https://iu-tomcat.servicecluster.de/byteme/api/game/leave', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ gameId })
        });
        if (!response.ok) throw new Error("Fehler beim Verlassen des Spiels: " + response.statusText);
        window.location.href = "lobby.html";
    } catch (error) {
        alert(error.message);
    }
}

// Brett initialisieren
const boardEl = document.getElementById("board");
for (let r = 0; r < 3; r++) {
    for (let c = 0; c < 3; c++) {
        const cell = document.createElement("div");
        cell.className = "cell";
        cell.dataset.row = r;
        cell.dataset.col = c;
        cell.addEventListener("click", async () => {
            if (cell.textContent) return; // schon besetzt
            try {
                const resp = await fetch('https://iu-tomcat.servicecluster.de/byteme/api/game/move', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify({ gameId, row: r, column: c })
                });
                if (!resp.ok) throw new Error("Zug nicht möglich: " + resp.statusText);
                cell.textContent = 'X';
            } catch (e) {
                alert(e.message);
            }
        });
        boardEl.appendChild(cell);
    }
}

// Chat-Logik
const chatMessagesEl = document.getElementById("chatMessages");

async function sendChat() {
    const text = document.getElementById("chatInput").value.trim();
    if (!text) return;
    try {
        const resp = await fetch('https://iu-tomcat.servicecluster.de/byteme/api/game/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ gameId, text })
        });
        if (!resp.ok) throw new Error("Nachricht konnte nicht gesendet werden: " + resp.statusText);
        const data = await resp.json();
        appendChatMessage("Du", data.text);
        document.getElementById("chatInput").value = "";
    } catch (e) {
        alert(e.message);
    }
}

function appendChatMessage(user, text) {
    const msgEl = document.createElement("div");
    msgEl.className = "message";
    msgEl.innerHTML = `<strong>${user}:</strong> ${text}`;
    chatMessagesEl.appendChild(msgEl);
    chatMessagesEl.scrollTop = chatMessagesEl.scrollHeight;
}