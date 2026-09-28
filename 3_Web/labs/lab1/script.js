// Ключ, под которым храним историю в LocalStorage
const STORAGE_KEY = 'lab1_results';

// === Инициализация после загрузки страницы ===
window.onload = function () {
    const form = document.getElementById('coordinates-form');
    if (form) {
        form.addEventListener('submit', handleFormSubmit);
    }

    const clearBtn = document.getElementById('clear-history');
    if (clearBtn) {
        clearBtn.addEventListener('click', clearHistory);
    }

    // Восстанавливаем историю из LocalStorage
    loadHistory();

    // Начальная отрисовка (значения по умолчанию из HTML)
    draw(0, 2, 3);
};

// === Обработчик отправки формы ===
function handleFormSubmit(event) {
    event.preventDefault();

    const errorDiv = document.getElementById("error-message");
    errorDiv.innerText = "";

    // 1. Получаем X из радиокнопок
    const xRadio = document.querySelector('input[name="X"]:checked');
    if (!xRadio) {
        errorDiv.innerText = "Ошибка: Выберите значение X!";
        return;
    }
    const X = Number(xRadio.value);

    // 2. Получаем Y и R из текстовых полей
    const rawY = document.getElementById("Y").value.trim().replace(',', '.');
    const rawR = document.getElementById("R").value.trim().replace(',', '.');

    if (rawY === "" || rawR === "") {
        errorDiv.innerText = "Ошибка: Все поля должны быть заполнены!";
        return;
    }

    const Y = Number(rawY);
    const R = Number(rawR);

    // 3. Валидация
    if (isNaN(Y) || Y < -5 || Y > 5) {
        errorDiv.innerText = "Ошибка: Y должен быть числом в диапазоне от -5 до 5!";
        return;
    }
    if (isNaN(R) || R < 1 || R > 4) {
        errorDiv.innerText = "Ошибка: R должен быть числом в диапазоне от 1 до 4!";
        return;
    }

    // 4. Проверяем попадание
    const hit = checkHit(X, Y, R);

    // 5. Формируем запись. Время сохраняем в UTC (ISO) — при смене
    //    часового пояса устройства отображение пересчитается корректно.
    const record = {
        x: X,
        y: Y,
        r: R,
        hit: hit,
        time: new Date().toISOString()
    };

    // 6. Сохраняем в LocalStorage и вставляем строку в начало таблицы
    saveResult(record);
    const tbody = document.getElementById('results-body');
    tbody.prepend(createRowElement(record));

    // 7. Перерисовываем Canvas
    draw(X, Y, R);
}

// === Проверка попадания в область ===
function checkHit(x, y, r) {
    // 1. Четверть круга в I четверти
    if (x >= 0 && y >= 0) {
        return (x * x + y * y) <= (r * r);
    }
    // 2. Прямоугольник в III четверти: [-R/2, 0] × [-R, 0]
    if (x <= 0 && y <= 0) {
        return (x >= -r / 2) && (y >= -r);
    }
    // 3. Треугольник в IV четверти: ниже прямой y = x - R
    if (x >= 0 && y <= 0) {
        return y >= x - r;
    }
    return false;
}

// === LocalStorage: сохранение и загрузка ===
function saveResult(record) {
    const results = JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];
    results.push(record);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(results));
}

function loadHistory() {
    const tbody = document.getElementById('results-body');
    if (!tbody) return;

    tbody.innerHTML = '';
    const results = JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];

    // Новые записи — сверху, поэтому идём с конца массива
    [...results].reverse().forEach(record => {
        tbody.appendChild(createRowElement(record));
    });
}

function clearHistory() {
    if (!confirm('Очистить всю историю проверок?')) return;
    localStorage.removeItem(STORAGE_KEY);
    document.getElementById('results-body').innerHTML = '';
}

// === Создание <tr> из записи ===
function createRowElement(record) {
    const row = document.createElement('tr');

    // Дата и время отображаются в локали клиента (ru-RU).
    // Хранится ISO-строка в UTC, поэтому при смене часового пояса
    // тот же самый момент времени отобразится в новом поясе.
    const timeString = new Date(record.time).toLocaleString('ru-RU');

    row.innerHTML = `
        <td>${record.x}</td>
        <td>${record.y}</td>
        <td>${record.r}</td>
        <td style="color: ${record.hit ? '#2e7d32' : '#d9534f'}; font-weight: bold;">
            ${record.hit ? 'Попадание' : 'Промах'}
        </td>
        <td>${timeString}</td>
    `;

    return row;
}

// === Отрисовка Canvas (без изменений) ===
function draw(x, y, r) {
    const canvas = document.getElementById("canvas");
    if (!canvas) return;
    const ctx = canvas.getContext("2d");

    const width = canvas.width;
    const height = canvas.height;
    const originX = width / 2;
    const originY = height / 2;

    // Фиксированное смещение фигуры от центра — размер фигуры визуально
    // не зависит от R, а штрихи R, R/2, -R/2, -R стоят на месте.
    const maxExtent = Math.min(width, height) / 2 - 30;
    const scale = maxExtent / r;

    ctx.clearRect(0, 0, width, height);
    ctx.save();
    ctx.translate(originX, originY);

    // Рисуем область
    ctx.fillStyle = "rgba(44, 143, 255, 0.8)";
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.lineTo(0, -r * scale);
    ctx.arc(0, 0, r * scale, -Math.PI / 2, 0, false);
    ctx.lineTo(0, r * scale);
    ctx.lineTo(-(r / 2) * scale, r * scale);
    ctx.lineTo(-(r / 2) * scale, 0);
    ctx.closePath();
    ctx.fill();

    // Рисуем точку
    const pixelX = x * scale;
    const pixelY = -y * scale;

    ctx.beginPath();
    ctx.arc(pixelX, pixelY, 5, 0, Math.PI * 2);
    ctx.fillStyle = "#ff3333";
    ctx.strokeStyle = "#ffffff";
    ctx.lineWidth = 1.5;
    ctx.fill();
    ctx.stroke();

    ctx.restore();

    drawAxes(ctx, width, height, originX, originY, scale, r);
}

function drawAxes(ctx, width, height, originX, originY, scale, r) {
    ctx.strokeStyle = '#333333';
    ctx.lineWidth = 2;

    ctx.beginPath(); ctx.moveTo(0, originY); ctx.lineTo(width, originY); ctx.stroke();
    ctx.beginPath(); ctx.moveTo(originX, 0); ctx.lineTo(originX, height); ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(width, originY); ctx.lineTo(width - 10, originY - 5); ctx.lineTo(width - 10, originY + 5);
    ctx.fillStyle = '#333333'; ctx.fill();

    ctx.beginPath();
    ctx.moveTo(originX, 0); ctx.lineTo(originX - 5, 10); ctx.lineTo(originX + 5, 10);
    ctx.fill();

    ctx.font = 'bold 14px sans-serif';
    ctx.fillStyle = '#333333';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';

    ctx.fillText('x', width - 15, originY - 15);
    ctx.fillText('y', originX + 15, 15);

    const tickSize = 5;
    const ticks = [
        { val: r, label: 'R' },
        { val: r / 2, label: 'R/2' },
        { val: -r / 2, label: '-R/2' },
        { val: -r, label: '-R' }
    ];

    ctx.lineWidth = 1.5;

    ticks.forEach(tick => {
        let posX = originX + tick.val * scale;
        ctx.beginPath();
        ctx.moveTo(posX, originY - tickSize);
        ctx.lineTo(posX, originY + tickSize);
        ctx.stroke();
        ctx.fillText(tick.label, posX, originY + 18);

        let posY = originY - tick.val * scale;
        ctx.beginPath();
        ctx.moveTo(originX - tickSize, posY);
        ctx.lineTo(originX + tickSize, posY);
        ctx.stroke();

        ctx.save();
        ctx.textAlign = 'left';
        ctx.fillText(tick.label, originX + 10, posY);
        ctx.restore();
    });
}