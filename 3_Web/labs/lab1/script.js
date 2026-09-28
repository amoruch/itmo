// Функция-обработчик отправки формы
function handleFormSubmit(event) {
    event.preventDefault();

    const errorDiv = document.getElementById("error-message");
    errorDiv.innerText = "";

    const rawX = document.getElementById("X").value.trim().replace(',', '.');
    const rawY = document.getElementById("Y").value.trim().replace(',', '.');
    const rawR = document.getElementById("R").value.trim().replace(',', '.');

    if (rawX === "" || rawY === "" || rawR === "") {
        errorDiv.innerText = "Ошибка: Все поля должны быть заполнены!";
        return;
    }

    const X = Number(rawX);
    const Y = Number(rawY);
    const R = Number(rawR);

    if (isNaN(X) || X < -5 || X > 3) {
        errorDiv.innerText = "Ошибка: X должен быть числом в диапазоне от -5 до 3!";
        return;
    }

    if (isNaN(Y) || Y < -5 || Y > 5) {
        errorDiv.innerText = "Ошибка: Y должен быть числом в диапазоне от -5 до 5!";
        return;
    }

    if (isNaN(R) || R < 1 || R > 4) {
        errorDiv.innerText = "Ошибка: R должен быть числом в диапазоне от 1 до 4!";
        return;
    }

    draw();
}

function draw() {
    const canvas = document.getElementById("canvas");
    if (!canvas) return;
    const ctx = canvas.getContext("2d");

    const inputX = document.getElementById("X").value;
    const inputY = document.getElementById("Y").value;
    const inputR = document.getElementById("R").value;

    const X = inputX !== "" ? parseFloat(inputX) : 1;
    const Y = inputY !== "" ? parseFloat(inputY) : 2;
    const R = inputR !== "" ? parseFloat(inputR) : 3; 

    const width = canvas.width;
    const height = canvas.height;
    const originX = width / 2;
    const originY = height / 2;
    const scale = width / (2 * R + 2); 

    ctx.clearRect(0, 0, width, height);
    ctx.save();
    ctx.translate(originX, originY);

    ctx.fillStyle = "rgb(44, 143, 255)"; 

    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.lineTo(0, -R * scale);
    ctx.arc(0, 0, R * scale, -Math.PI / 2, 0, false);
    ctx.lineTo(0, R * scale);
    ctx.lineTo(- (R / 2) * scale, R * scale);
    ctx.lineTo(- (R / 2) * scale, 0);
    ctx.closePath();
    ctx.fill();

    const pixelX = X * scale;
    const pixelY = -Y * scale;

    ctx.beginPath();
    ctx.arc(pixelX, pixelY, 5, 0, Math.PI * 2);
    ctx.fillStyle = "#ff3333"; // Ярко-красная точка
    ctx.strokeStyle = "#ffffff";
    ctx.lineWidth = 1.5;
    ctx.fill();
    ctx.stroke();

    ctx.restore();

    drawAxes(ctx, width, height, originX, originY, scale, R);
}

// Функция отрисовки осей
function drawAxes(ctx, width, height, originX, originY, scale, R) {
    ctx.strokeStyle = '#333333';
    ctx.lineWidth = 2;
    
    ctx.beginPath(); ctx.moveTo(0, originY); ctx.lineTo(width, originY); ctx.stroke();
    ctx.beginPath(); ctx.moveTo(originX, 0); ctx.lineTo(originX, height); ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(width, originY); ctx.lineTo(width - 8, originY - 4); ctx.lineTo(width - 8, originY + 4);
    ctx.fillStyle = '#333333'; ctx.fill();

    ctx.beginPath();
    ctx.moveTo(originX, 0); ctx.lineTo(originX - 4, 8); ctx.lineTo(originX + 4, 8);
    ctx.fill();

    ctx.font = 'bold 16px sans-serif'; 
    ctx.fillStyle = '#333333';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';

    ctx.fillText('x', width - 15, originY - 15);
    ctx.fillText('y', originX + 15, 15);

    const tickSize = 4; 

    const ticks = [
        { val: R, label: 'R' },
        { val: R / 2, label: 'R/2' },
        { val: -R / 2, label: '-R/2' },
        { val: -R, label: '-R' }
    ];

    ctx.lineWidth = 1.5;

    ticks.forEach(tick => {
        // --- МЕТКИ НА ОСИ X ---
        let posX = originX + tick.val * scale;
        
        // Рисуем вертикальную черточку (засечку) на оси X
        ctx.beginPath();
        ctx.moveTo(posX, originY - tickSize);
        ctx.lineTo(posX, originY + tickSize);
        ctx.stroke();
        
        // Пишем текст под засечкой (сдвиг вниз на 15 пикселей)
        ctx.fillText(tick.label, posX, originY + 18);

        // --- МЕТКИ НА ОСИ Y ---
        // Минус перед tick.val, так как в canvas верх — это отрицательный Y
        let posY = originY - tick.val * scale;
        
        // Рисуем горизонтальную черточку (засечку) на оси Y
        ctx.beginPath();
        ctx.moveTo(originX - tickSize, posY);
        ctx.lineTo(originX + tickSize, posY);
        ctx.stroke();
        
        // Пишем текст справа от засечки (сдвиг вправо на 20 пикселей)
        // Для оси Y выравнивание лучше сделать по левому краю, чтобы текст не налезал на ось
        ctx.save();
        ctx.textAlign = 'left';
        ctx.fillText(tick.label, originX + 10, posY);
        ctx.restore();
    });
}
