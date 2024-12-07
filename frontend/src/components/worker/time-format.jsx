
// Формирует дату и время с вариантом указанным в лабе.
const dateFormat = (dateString) => {
    const date = new Date(dateString);

    // Получаем компоненты даты и времени.
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    // const seconds = String(date.getSeconds()).padStart(2, '0');

    // Форматируем в нужный вид
    return `${year}-${month}-${day}T${hours}:${minutes}`;
};

export default dateFormat;