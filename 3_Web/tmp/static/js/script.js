document.addEventListener('DOMContentLoaded', () => {
    const button = document.getElementById('action-btn');
    const responseText = document.getElementById('js-response');

    button.addEventListener('click', () => {
        // Переключаем класс видимости текста
        if (responseText.classList.contains('hidden')) {
            responseText.classList.remove('hidden');
            responseText.classList.add('visible');
            button.innerText = 'Скрыть';
        } else {
            responseText.classList.remove('visible');
            responseText.classList.add('hidden');
            button.innerText = 'Нажми на меня';
        }
    });
});
