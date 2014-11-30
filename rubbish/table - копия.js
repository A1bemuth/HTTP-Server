function addAlbumInit(index) {
  var table = document.getElementById("table");
  var rowCount = table.rows.length;
  var row = table.insertRow(index);

  var cellAuthor = row.insertCell(0);
  var elem1 = document.createElement("input");
  elem1.type = "text";
  elem1.value = rowCount;
  elem1.id = "author"+rowCount;
  cellAuthor.appendChild(elem1);

  var cellAlbum = row.insertCell(1);
  var elem2 = document.createElement("input");
  elem2.type = "text";
  elem2.id = "album"+rowCount;
  cellAlbum.appendChild(elem2);

  var cellYear = row.insertCell(2);
  var elem3 = document.createElement("input");
  elem3.type = "text";
  elem3.id = "year"+rowCount;
  cellYear.appendChild(elem3);

  var cellButton = row.insertCell(3);
  var elem4 = document.createElement("button");
  elem4.innerHTML = "Confirm";
  elem4.id = "confirmButton"+rowCount;
  cellButton.appendChild(elem4);

  document.getElementById("confirmButton"+rowCount).onclick = function(){
    addAlbum(rowCount);
    sendData(elem1.value, elem2.value, elem3.value);
    document.getElementById("addButton").disabled = false;
  }
  document.getElementById("addButton").disabled = true;
}

function addAlbum(rowID) {
  var table = document.getElementById("table");

  var elem1 = document.createElement("P");
  elem1.type = "text";
  elem1.innerHTML = document.getElementById("author"+rowID).value+" "+rowID;
  elem1.id = "author"+rowID;

  var elem2 = document.createElement("P");
  elem2.type = "text";
  elem2.innerHTML = document.getElementById("album"+rowID).value;
  elem2.id = "album"+rowID;

  var elem3 = document.createElement("P");
  elem3.type = "text";
  elem3.innerHTML = document.getElementById("year"+rowID).value;
  elem3.id = "year"+rowID;

  var elem4 = document.createElement("button");
  elem4.innerHTML = "Delete";
  elem4.id = "deleteButton"+rowID;

  var elem5 = document.createElement("button");
  elem5.innerHTML = "Edit...";
  elem5.id = "editButton"+rowID;

  table.deleteRow(rowID);
  var row = table.insertRow(rowID);

  var freeId = row.rowIndex;
  while(document.getElementById(freeId)!=null){
    freeId++;
  }
  row.id = "row" + freeId;

  elem1.innerHTML += " "+row.rowIndex;

  var cellAuthor = row.insertCell(0);
  cellAuthor.appendChild(elem1);
  var cellAlbum = row.insertCell(1);
  cellAlbum.appendChild(elem2);
  var cellYear = row.insertCell(2);
  cellYear.appendChild(elem3);
  var cellButton = row.insertCell(3);
  cellButton.appendChild(elem4);
  cellButton.appendChild(elem5);

  document.getElementById("deleteButton"+rowID).onclick = function(){
    table.deleteRow(row.rowIndex);
  }

  document.getElementById("editButton"+rowID).onclick = function(){
    var index = row.rowIndex;
    table.deleteRow(index);
    table.addAlbumInit(index);
  }

  document.getElementById("addButton").disabled = true;
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