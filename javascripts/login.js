// Symbole Animation
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

// Login-Handling
async function handleLogin() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if (!email || !password) {
    alert("Bitte E-Mail und Passwort eingeben.");
    return;
    }

    const loginData = {
    email: email,
    password: password
    };

    try {
    const response = await fetch("https://iu-tomcat.servicecluster.de/byteme/api/login", {
        method: "POST",
        headers: {
        "Content-Type": "application/json"
        },
        body: JSON.stringify(loginData)
    });

    if (response.ok) {
        window.location.href = "next-room.html"; // Direkte Weiterleitung zum nächsten Raum
    } else {
        const error = await response.json();
        alert("Anmeldung fehlgeschlagen: " + (error.message || "Unbekannter Fehler"));
    }
    } catch (error) {
    alert("Fehler bei der Anmeldung: " + error.message);
    }
}