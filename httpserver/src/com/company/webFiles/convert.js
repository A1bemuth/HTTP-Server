function getXmlHttp() {
    var xmlhttp;
    try {
      xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
    try {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    } catch (E) {
      xmlhttp = false;
    }
    }
    if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
      xmlhttp = new XMLHttpRequest();
    }
    return xmlhttp;
  }
function makeRequest() {
  var number = document.getElementById("number").value; // Считываем значение числа
  var base1 = document.getElementById("base1").value; // Считываем значение базы 1
  var base2 = document.getElementById("base2").value; // Считываем значение базы 2
  var xmlhttp = getXmlHttp(); // Создаём объект XMLHTTP
  xmlhttp.open('POST', 'converter.html', true); // Открываем асинхронное соединение
  //xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded'); // Отправляем кодировку
  xmlhttp.send("?number=" + encodeURIComponent(number) + "&base1=" + encodeURIComponent(base1)+ "&base2=" + encodeURIComponent(base2)); // Отправляем POST-запрос
  xmlhttp.onreadystatechange = function() // Ждём ответа от сервера
  {
    if (xmlhttp.readyState == 4) { // Ответ пришёл
      if(xmlhttp.status == 200) { // Сервер вернул код 200
        document.getElementById("result").value = xmlhttp.responseText; // Выводим ответ сервера
      }
    }
  };
}