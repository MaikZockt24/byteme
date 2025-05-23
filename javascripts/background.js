/*const NUM_SYMBOLS = 40; // Anzahl der Symbole
const symbols = [];
const symbolFiles = ["../../assets/images/kreuz.png", "../../assets/images/kringel.png"]; // Deine transparenten Symbole

// Symbole erstellen und zufällig platzieren
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

// Funktion zum Tauschen der Positionen von zwei Symbolen
function swapPositions() {
  // Wähle zwei unterschiedliche Symbole
  const indexA = Math.floor(Math.random() * symbols.length);
  let indexB = Math.floor(Math.random() * symbols.length);
  while (indexA === indexB) {
    indexB = Math.floor(Math.random() * symbols.length); // Sicherstellen, dass indexA !== indexB
  }

  const a = symbols[indexA];
  const b = symbols[indexB];

  // Tausche die Positionen (top, left)
  const tempTop = a.style.top;
  const tempLeft = a.style.left;
  a.style.top = b.style.top;
  a.style.left = b.style.left;
  b.style.top = tempTop;
  b.style.left = tempLeft;
}

// Tausche Positionen alle 2 Sekunden (kürzerer Intervall für dynamischeren Effekt)
setInterval(swapPositions, 2000);

// Optional: Zusätzliche zufällige Bewegungen für Teleport-Effekt
setInterval(() => {
  const randomSymbol = symbols[Math.floor(Math.random() * symbols.length)];
  randomSymbol.style.top = `${Math.random() * 100}vh`;
  randomSymbol.style.left = `${Math.random() * 100}vw`;
}, 3000); */

const NUM_SYMBOLS = 40; // Anzahl der Symbole
const symbols = [];
const symbolFiles = ["../../assets/images/kreuz.png", "../../assets/images/kringel.png"]; // Deine transparenten Symbole

// Symbole erstellen und zufällig platzieren
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

// Funktion zum Tauschen der Positionen von zwei Symbolen
function swapPositions() {
  // Wähle zwei unterschiedliche Symbole
  const indexA = Math.floor(Math.random() * symbols.length);
  let indexB = Math.floor(Math.random() * symbols.length);
  while (indexA === indexB) {
    indexB = Math.floor(Math.random() * symbols.length); // Sicherstellen, dass indexA !== indexB
  }

  const a = symbols[indexA];
  const b = symbols[indexB];

  // Füge Teleport-Effekt hinzu
  a.classList.add("teleport");
  b.classList.add("teleport");
  setTimeout(() => {
    a.classList.remove("teleport");
    b.classList.remove("teleport");
  }, 300); // Entferne nach 300ms (Dauer der Animation)

  // Tausche die Positionen (top, left)
  const tempTop = a.style.top;
  const tempLeft = a.style.left;
  a.style.top = b.style.top;
  a.style.left = b.style.left;
  b.style.top = tempTop;
  b.style.left = tempLeft;
}

// Tausche Positionen alle 500 ms für häufigeren Effekt
setInterval(swapPositions, 500);

// Zusätzliche zufällige Bewegungen für Teleport-Effekt, häufiger auf 1500 ms
setInterval(() => {
  const randomSymbol = symbols[Math.floor(Math.random() * symbols.length)];
  randomSymbol.classList.add("teleport");
  setTimeout(() => {
    randomSymbol.classList.remove("teleport");
  }, 300); // Entferne nach 300ms
  randomSymbol.style.top = `${Math.random() * 100}vh`;
  randomSymbol.style.left = `${Math.random() * 100}vw`;
}, 1500);
