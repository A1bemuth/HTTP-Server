<!--<form>-->

<script src="convert.js" type="text/javascript"></script>
<table cellpadding="5" cellspacing="0" border="0" class="text">
    <tr>
        <td>
            <p>Число для<br> конвертации</p>
        </td>
        <td>
          <input type="text" id="number" required pattern="^[0-9a-zA-Z]+$" required>
        </td>
        <td rowspan=4>
          <output id="result"></output>
        </td>
    </tr>
    <tr>
        <td>
            <p>Система<br> счисления</p>
        </td>
        <td>
            <input type="text" id="base1" required pattern="^[0-9]+$" required>
        </td>
    </tr>
    <tr>
        <td>
            <p>Итоговая система<br> счисления</p>
        </td>
        <td>
            <input type="text" id="base2" required pattern="^[0-9]+$" required>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <button width="100%" onclick="makeRequest()">Вычислить ответ</button>
        </td>
    </tr>
</table>
<!--</form>-->
