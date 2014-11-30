

function addTableRow() {
    var table = document.getElementById("table");
    var row = table.insertRow(-1); // Добавляем строку

    addCell(row, "input", "text"); // Заполняем строку
    addCell(row, "input", "text");
    addCell(row, "input", "text");
    addCell(row, "button");

    var confButton = row.childNodes[3].childNodes[0];
    confButton.innerHTML = "Confirm";
    confButton.onclick = function(){
        confirmAction(this.parentNode.parentNode);
    }
}

// Добавляет в конец строки ячейку и создаёт в ней указанный элемент
function addCell(row, elemtype, type) {
    var cell = row.insertCell(-1);
    var elem = document.createElement(elemtype);
    elem.type = type || null;
    cell.appendChild(elem);
}

// Заменяет поля ввода готовыми строками <p>
function confirmAction(row) {
    var table = document.getElementById("table");
    var elems = [];        // Массив новых элементов для строки
    var cellChildren = []; // Массив прежних элементов строки
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

    elems[3].onclick = function(){
        table.deleteRow(this.parentNode.parentNode.rowIndex);
    }
    elems[4].onclick = function(){

    }
}

function sendData(authorVal, albumVal, yearVal) {
    $.ajax({
        url: '/table.html',
        type: 'POST',
        data: { json: JSON.stringify({
            author: authorVal,
            album: albumVal,
            year: yearVal
        })},
        dataType: 'json'
    });
}