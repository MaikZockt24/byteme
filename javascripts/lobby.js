let rooms = [];

function showCreateGameModal() {
    document.getElementById("createGameModal").style.display = "block";
}

function createNewGame() {
    const gameName = document.getElementById("gameName").value;
    const joinCode = document.getElementById("joinCode").value.toUpperCase();

    if (!gameName || !joinCode || joinCode.length < 6) {
        alert("Bitte gib einen gültigen Spielnamen und einen Join-Code (mind. 6 Zeichen) ein.");
        return;
    }

    if (rooms.some(room => room.code === joinCode)) {
        alert("Dieser Join-Code ist bereits vergeben.");
        return;
    }

    showLoadingAnimation();
    setTimeout(() => {
        rooms.push({ name: gameName, code: joinCode, host: "You", players: 1, maxPlayers: 2 });
        updateRoomList();
        document.getElementById("createGameModal").style.display = "none";
        document.getElementById("gameName").value = "";
        document.getElementById("joinCode").value = "";
        hideLoadingAnimation();
        // Platzhalter für Weiterleitung zur Spielseite (dein Partner macht das)
        // window.location.href = "game.html";
        alert("Spiel erstellt! Teile den Join-Code: " + joinCode + ". Weiterleitung zur Spielseite folgt (Platzhalter).");
    }, 3000); // Simuliert Ladezeit
}

function showJoinGameModal(element) {
    document.getElementById("joinGameModal").style.display = "block";
    document.getElementById("joinGameModal").dataset.selectedRoomCode = element.dataset.code;
}

function joinGame() {
    const joinCodeInput = document.getElementById("joinCodeInput").value.toUpperCase();
    const selectedRoomCode = document.getElementById("joinGameModal").dataset.selectedRoomCode;

    if (!joinCodeInput) {
        alert("Bitte gib einen Join-Code ein.");
        return;
    }

    showLoadingAnimation();
    setTimeout(() => {
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
                // Platzhalter für Weiterleitung zur Spielseite
                // window.location.href = "game.html";
            } else {
                hideLoadingAnimation();
                alert("Das Spiel ist bereits voll (max. 2 Spieler).");
            }
        } else {
            hideLoadingAnimation();
            alert("Ungültiger Join-Code.");
        }
    }, 3000); // Simuliert Ladezeit
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
    // Simulierte API-Abfrage für Räume anderer Nutzer
    fetchRoomsFromServer();
}

function showLoadingAnimation() {
    const overlay = document.getElementById("loadingOverlay");
    overlay.style.display = "flex";
}

function hideLoadingAnimation() {
    const overlay = document.getElementById("loadingOverlay");
    overlay.style.display = "none";
}

// Simulierte Funktion für API-Abfrage
function fetchRoomsFromServer() {
    // Platzhalter für echte API (z. B. fetch("/api/rooms"))
    // Hier simulieren wir Räume von anderen Nutzern
    const simulatedRooms = [
        { name: "Room A", code: "CODE001", host: "UserX", players: 1, maxPlayers: 2 },
        { name: "Room B", code: "CODE002", host: "UserY", players: 0, maxPlayers: 2 }
    ];
    rooms = [...rooms, ...simulatedRooms.filter(r => !rooms.some(room => room.code === r.code))];
    updateRoomList();
}

// Schließe das Modal, wenn man außerhalb klickt
window.onclick = function(event) {
    const modals = document.getElementsByClassName("modal");
    for (let modal of modals) {
        if (event.target == modal) {
            modal.style.display = "none";
            document.getElementById("gameName").value = "";
            document.getElementById("joinCode").value = "";
            document.getElementById("joinCodeInput").value = "";
        }
    }
}

// Initiale Raumliste laden
fetchRoomsFromServer();