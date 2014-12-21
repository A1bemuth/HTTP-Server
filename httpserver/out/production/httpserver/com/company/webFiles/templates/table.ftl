<button onclick="addTableRow(-1)" id="addButton" class="text">Add</button>

<script type="text/javascript" src="jquery-1.11.1.min.js"></script>
<script src="table.js" type="text/javascript"></script>

<!--<script type="text/javascript">
  $(document).ready(function (){
    $.ajax({
      url: 'table.html/getEntries',
      type: "POST",
      data: "getEntries",
      dataType: 'html',
      success: onEntriesFound(data)
      });
  });
  </script>-->

<TABLE cellpadding="5" cellspacing="0" border="1" class="text" id="table">
<TR>
  <TD><p>Author:</p></TD>
  <TD><p>Album:</p></TD>
  <TD><p>Year:</p></TD>
  <TD></TD>
</TR>
</TABLE>
