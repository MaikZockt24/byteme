let rooms = [];

function showCreateGameModal() {
    document.getElementById("createGameModal").style.display = "block";
}

async function createNewGame() {
    const gameName = document.getElementById("gameName").value;
    const joinCode = document.getElementById("joinCode").value.toUpperCase();

    if (!gameName || !joinCode || joinCode.length < 6) {
        alert("Bitte gib einen gültigen Spielnamen und einen Join-Code (mind. 6 Zeichen) ein.");
        return;
    }

    const token = localStorage.getItem("jwtToken");
    if (!token) {
        alert("Bitte melde dich zuerst an.");
        window.location.href = "login.html";
        return;
    }

    showLoadingAnimation();
    try {
        const response = await fetch('https://iu-tomcat.servicecluster.de/byteme/api/game/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ name: gameName, code: joinCode, maxPlayers: 2 })
        });

        if (!response.ok) {
            if (response.status === 401) {
                alert("Sitzung abgelaufen. Bitte melde dich erneut an.");
                localStorage.removeItem("jwtToken");
                window.location.href = "login.html";
                return;
            }
            throw new Error("Fehler beim Erstellen des Spiels: " + response.statusText);
        }

        const newRoom = await response.json();
        // Annahme: Die API gibt eine gameId zurück, z. B. { gameId: 123, ... }
        if (newRoom.gameId) {
            localStorage.setItem("gameId", newRoom.gameId); // gameId speichern
        }
        rooms.push({ name: gameName, code: joinCode, players: 1, maxPlayers: 2 });
        updateRoomList();
        document.getElementById("createGameModal").style.display = "none";
        document.getElementById("gameName").value = "";
        document.getElementById("joinCode").value = "";
        hideLoadingAnimation();
        window.location.href = "gameRoom.html";
    } catch (error) {
        hideLoadingAnimation();
        alert(error.message);
    }
}

function showJoinGameModal(element) {
    document.getElementById("joinGameModal").style.display = "block";
    document.getElementById("joinGameModal").dataset.selectedRoomCode = element.dataset.code;
}

async function joinGame() {
    const joinCodeInput = document.getElementById("joinCodeInput").value.toUpperCase();
    const selectedRoomCode = document.getElementById("joinGameModal").dataset.selectedRoomCode;

    if (!joinCodeInput) {
        alert("Bitte gib einen Join-Code ein.");
        return;
    }

    const token = localStorage.getItem("jwtToken");
    if (!token) {
        alert("Bitte melde dich zuerst an.");
        window.location.href = "login.html";
        return;
    }

    showLoadingAnimation();
    try {
        const response = await fetch('https://iu-tomcat.servicecluster.de/byteme/api/game/join', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ code: joinCodeInput })
        });

        if (!response.ok) {
            if (response.status === 401) {
                alert("Sitzung abgelaufen. Bitte melde dich erneut an.");
                localStorage.removeItem("jwtToken");
                window.location.href = "login.html";
                return;
            }
            throw new Error("Fehler beim Beitreten: " + response.statusText);
        }

        const joinResponse = await response.json();
        // Annahme: Die API gibt eine gameId zurück, z. B. { gameId: 123, ... }
        if (joinResponse.gameId) {
            localStorage.setItem("gameId", joinResponse.gameId); // gameId speichern
        }

        const room = rooms.find(r => r.code === selectedRoomCode);
        if (room && room.code === joinCodeInput) {
            if (room.players < room.maxPlayers) {
                room.players++;
                document.getElementById("joinGameModal").style.display = "none";
                document.getElementById("joinCodeInput").value = "";
                hideLoadingAnimation();
                if (room.players === 2) {
                    alert("Das Spiel kann beginnen! (Host: " + room.host + ")");
                } else {
                    alert("Du bist dem Spiel beigetreten! Spieler: " + room.players + "/2");
                }
                window.location.href = "gameRoom.html";
            } else {
                hideLoadingAnimation();
                alert("Das Spiel ist bereits voll (max. 2 Spieler).");
            }
        } else {
            hideLoadingAnimation();
            alert("Ungültiger Join-Code.");
        }
    } catch (error) {
        hideLoadingAnimation();
        alert(error.message);
    }
}

function updateRoomList() {
    const roomList = document.getElementById("roomList");
    roomList.innerHTML = "";
    rooms.forEach(room => {
        const li = document.createElement("li");
        li.textContent = `${room.name} (by ${room.host}) - ${room.players}/2`;
        li.dataset.code = room.code;
        li.onclick = () => showJoinGameModal(li);
        roomList.appendChild(li);
    });
}

function showLoadingAnimation() {
    const overlay = document.getElementById("loadingOverlay");
    overlay.style.display = "flex";
}

function hideLoadingAnimation() {
    const overlay = document.getElementById("loadingOverlay");
    overlay.style.display = "none";
}

async function loadOpenGames() {
    showLoadingAnimation();
    try {
        const token = localStorage.getItem("jwtToken");
        if (!token) {
            window.location.href = "login.html";
            return;
        }

        const resp = await fetch('https://iu-tomcat.servicecluster.de/byteme/api/lobby', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        if (!resp.ok) throw new Error(`Fehler beim Laden der Lobby: ${resp.statusText}`);
        const games = await resp.json();
        
        const roomList = document.getElementById("roomList");
        roomList.innerHTML = '';

        rooms = games.map(game => ({
            name: game.name || `Raum ${game.gameId}`,
            code: game.code,
            host: game.host || "Unknown",
            players: game.playerCount,
            maxPlayers: game.maxPlayers || 2
        }));
        updateRoomList();
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        hideLoadingAnimation();
    }
}

window.addEventListener('load', () => {
    loadOpenGames();
    setInterval(loadOpenGames, 50000000);
});

function toggleMenu() {
    const dropdown = document.getElementById("menuDropdown");
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
}

async function logout(event) {
    event.preventDefault();
    const token = localStorage.getItem("jwtToken");
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    try {
        const response = await fetch('https://iu-tomcat.servicecluster.de/byteme/api/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Fehler beim Logout: " + response.statusText);
        }

        localStorage.removeItem("jwtToken");
        localStorage.removeItem("gameId"); // gameId entfernen
        window.location.href = "login.html";
    } catch (error) {
        alert(error.message);
        localStorage.removeItem("jwtToken");
        localStorage.removeItem("gameId"); // gameId entfernen
        window.location.href = "login.html";
    }
}

async function deleteAccount(event) {
    event.preventDefault();
    const token = localStorage.getItem("jwtToken");
    if (!token) {
        alert("Bitte melde dich zuerst an.");
        window.location.href = "login.html";
        return;
    }

    if (confirm("Möchtest du dein Konto wirklich löschen? Dies kann nicht rückgängig gemacht werden.")) {
        showLoadingAnimation();
        try {
            const response = await fetch('https://iu-tomcat.servicecluster.de/byteme/api/delete', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                if (response.status === 401) {
                    alert("Sitzung abgelaufen. Bitte melde dich erneut an.");
                    localStorage.removeItem("jwtToken");
                    localStorage.removeItem("gameId"); // gameId entfernen
                    window.location.href = "login.html";
                    return;
                }
                throw new Error("Fehler beim Löschen des Kontos: " + response.statusText);
            }

            localStorage.removeItem("jwtToken");
            localStorage.removeItem("gameId"); // gameId entfernen
            hideLoadingAnimation();
            window.location.href = "login.html";
        } catch (error) {
            hideLoadingAnimation();
            alert(error.message);
        }
    }
}