function addAlbumInit(index) {
  var table = document.getElementById("table");
  var rowCount = table.rows.length;
  var row = table.insertRow(index);

  var freeId = row.rowIndex;
  while(document.getElementById(freeId)!=null){
    freeId++;
  }
  row.id = "row" + freeId;

  var cellAuthor = row.insertCell(0);
  var elem1 = document.createElement("input");
  elem1.type = "text";
  elem1.value = freeId;
  elem1.id = "author"+freeId;
  cellAuthor.appendChild(elem1);

  var cellAlbum = row.insertCell(1);
  var elem2 = document.createElement("input");
  elem2.type = "text";
  elem2.id = "album"+freeId;
  cellAlbum.appendChild(elem2);

  var cellYear = row.insertCell(2);
  var elem3 = document.createElement("input");
  elem3.type = "text";
  elem3.id = "year"+freeId;
  cellYear.appendChild(elem3);

  var cellButton = row.insertCell(3);
  var elem4 = document.createElement("button");
  elem4.innerHTML = "Confirm";
  elem4.id = "confirmButton"+freeId;
  cellButton.appendChild(elem4);

  document.getElementById("confirmButton"+freeId).onclick = function(){
    addAlbum(rowCount, freeId);
    sendData(elem1.value, elem2.value, elem3.value);
    document.getElementById("addButton").disabled = false;
  }
  document.getElementById("addButton").disabled = true;
}

function replace(tableRow, rowInd){

    var elem1 = document.createElement("input");
    elem1.type = "text";
    var elem2 = document.createElement("input");
    elem2.type = "text";
    var elem3 = document.createElement("input");
    elem3.type = "text";
    var elem4 = document.createElement("button");
    elem4.innerHTML = "Confirm";
    elem4.id = "confirmButton"+rowInd;

    tableRow.childNodes[0].replaceChild(elem1, document.getElementById("author"+rowInd));
    tableRow.childNodes[1].replaceChild(elem2, document.getElementById("album"+rowInd));
    tableRow.childNodes[2].replaceChild(elem3, document.getElementById("year"+rowInd));
    tableRow.childNodes[3].replaceChild(elem4, document.getElementById("deleteButton"+rowInd));
    tableRow.childNodes[3].removeChild(document.getElementById("editButton"+rowInd));

    document.getElementById("confirmButton"+rowInd).onclick = function(){
        addAlbum(rowCount, rowInd);
        sendData(elem1.value, elem2.value, elem3.value);
        document.getElementById("addButton").disabled = false;
    }
}

function addAlbum(rowID, rowInd) {
  var table = document.getElementById("table");

  var elem1 = document.createElement("P");
  elem1.type = "text";
  elem1.innerHTML = document.getElementById("author"+rowID).value+" "+rowID;

  var elem2 = document.createElement("P");
  elem2.type = "text";
  elem2.innerHTML = document.getElementById("album"+rowID).value;

  var elem3 = document.createElement("P");
  elem3.type = "text";
  elem3.innerHTML = document.getElementById("year"+rowID).value;

  var elem4 = document.createElement("button");
  elem4.innerHTML = "Delete";

  var elem5 = document.createElement("button");
  elem5.innerHTML = "Edit...";
  elem5.id = "editButton"+rowInd;

    //table.deleteRow(rowID);
  //var row = table.insertRow(rowID);



  //elem1.innerHTML += " "+row.rowIndex;

  var row = document.getElementById("row"+rowInd);

  row.childNodes[0].replaceChild(elem1, document.getElementById("author"+rowInd));
  row.childNodes[1].replaceChild(elem2, document.getElementById("album"+rowInd));
  row.childNodes[2].replaceChild(elem3, document.getElementById("year"+rowInd));
  row.childNodes[3].replaceChild(elem4, document.getElementById("confirmButton"+rowInd));
  row.childNodes[3].appendChild(elem5);

  elem1.id = "author"+rowInd;
  elem2.id = "album"+rowInd;
  elem3.id = "year"+rowInd;
  elem4.id = "deleteButton"+rowInd;

//  var cellAuthor = row.insertCell(0);
//  cellAuthor.appendChild(elem1);
//  var cellAlbum = row.insertCell(1);
//  cellAlbum.appendChild(elem2);
//  var cellYear = row.insertCell(2);
//  cellYear.appendChild(elem3);
//  var cellButton = row.insertCell(3);
//  cellButton.appendChild(elem4);
//  cellButton.appendChild(elem5);

  document.getElementById("deleteButton"+rowInd).onclick = function(){
    table.deleteRow(row.rowIndex);
  }

  document.getElementById("editButton"+rowInd).onclick = function(){
//    var index = row.rowIndex;
//    table.deleteRow(index);
//    table.addAlbumInit(index);
    document.getElementById("addButton").disabled = true;
    replace(row, rowInd);
  }

  document.getElementById("addButton").disabled = false;
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