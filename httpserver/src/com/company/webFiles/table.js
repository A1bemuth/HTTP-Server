function onEntriesFound(data) {
    if (data == "no entries") { return; }
    var entries = JSON.parse(data);
    for (var i=0; i < entries.length; i++) {
      addFilledTableRow(-1, entries[i].author, entries[i].album, entries[i].year );
    }
}

// Добавляет строку с полями для ввода данных
function addTableRow(index, author, album, year) {
    var table = document.getElementById("table");
    var row = table.insertRow(index); // Добавляем строку

    addCell(row, "input", "text", author); // Заполняем строку
    addCell(row, "input", "text", album);
    addCell(row, "input", "text", year);
    addCell(row, "button");

    var confButton = row.childNodes[3].childNodes[0];
    confButton.innerHTML = "Confirm";
    confButton.onclick = function(){
        confirmAction(this.parentNode.parentNode);
    }
}

// Добавляет строку, заполненную указанными данными
function addFilledTableRow(index, author, album, year) {
    var table = document.getElementById("table");
    var row = table.insertRow(index); // Добавляем строку

    addCell(row, "p", null, author); // Заполняем строку
    addCell(row, "p", null, album);
    addCell(row, "p", null, year);
    addCell(row, "button");
    editButton = document.createElement("button");
    row.childNodes[3].appendChild(editButton);

    var delButton = row.childNodes[3].childNodes[0];
    delButton.innerHTML = "Delete";
    delButton.onclick = function(){
        delEntry(author, album, year);
        table.deleteRow(this.parentNode.parentNode.rowIndex);           // Удаляем запись из таблицы
    }

    editButton.innerHTML = "Edit...";
    editButton.onclick = function(){
        var author = row.childNodes[0].childNodes[0].innerHTML;
        var album = row.childNodes[1].childNodes[0].innerHTML;
        var year = row.childNodes[2].childNodes[0].innerHTML;
        delEntry(author, album, year);  // Отправляем запрос на удаление с сервера
        var index = this.parentNode.parentNode.rowIndex;
        table.deleteRow(index);
        addTableRow(index, author, album, year);
    }
}

// Добавляет в конец строки ячейку и создаёт в ней указанный элемент
function addCell(row, element, type, text) {
    var cell = row.insertCell(-1);
    var elem = document.createElement(element);
    elem.type = type || null;
    elem.value = text || "";
    elem.innerHTML = text || "";
    cell.appendChild(elem);
}

// Заменяет поля ввода готовыми строками <p>
function confirmAction(row) {
    var table = document.getElementById("table");
    var elems = [];        // Массив новых элементов для строки
    var cellChildren = []; // Массив новых элементов для строки
    for(var i=0; i<4; i++){
        cellChildren[i] = row.childNodes[i].childNodes[0]; // Получаем ссылки на элементы строки
    }
    for(var i=0; i<3; i++){
        elems[i] = document.createElement("P"); // Создаём текстовые строки для вставки
        elems[i].innerHTML = cellChildren[i].value;
    }
    for(i=3; i<5; i++){
        elems[i] = document.createElement("button"); // Создаём кнопки для вставки
    }

    elems[3].innerHTML = "Delete";
    elems[4].innerHTML = "Edit...";

    for(i=0; i<4; i++){
      cellChildren[i].parentNode.replaceChild(elems[i], cellChildren[i]); // Заменяем старые эл-ты строки
    }
    row.childNodes[3].appendChild(elems[4]); // Добавляем кнопку "edit"

    sendData(elems[0].innerHTML, elems[1].innerHTML, elems[2].innerHTML); // Отправляем данные о записи на сервер

    // Добавляем функционал кнопок
    elems[3].onclick = function() {
        delEntry(elems[0].innerHTML, elems[1].innerHTML, elems[2].innerHTML); // Отправляем серверу запрос на удаление
        table.deleteRow(this.parentNode.parentNode.rowIndex);
    }
    elems[4].onclick = function(){
        delEntry(elems[0].innerHTML, elems[1].innerHTML, elems[2].innerHTML); // Отправляем серверу запрос на удаление
        var index = this.parentNode.parentNode.rowIndex;
        table.deleteRow(index);
        addTableRow(index, elems[0].innerHTML, elems[1].innerHTML, elems[2].innerHTML);
    }
}

$(document).ready(function (){
  $.ajax({
    url: 'table.html/getEntries',
    type: "POST",
    data: "getEntries",
    dataType: 'html',
    success: onEntriesFound
    });
});

function sendData(authorVal, albumVal, yearVal) {
    $.ajax({
        url: '/table.html/addEntry',
        type: 'POST',
        data: { json: JSON.stringify({
            author: authorVal,
            album: albumVal,
            year: yearVal
        })},
        dataType: 'HTML'
    });
}
function delEntry(authorVal, albumVal, yearVal) {
    $.ajax({
        url: '/table.html/deleteEntry',
        type: 'POST',
        data: { json: JSON.stringify({
            author: authorVal,
            album: albumVal,
            year: yearVal
        })},
        dataType: 'HTML'
    });
}