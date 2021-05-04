console.log("JS called")
/* barra de busca */
function myFilterFunction() {
  var input, filter, table, tr, td, i, txtValue;
  
  input = document.getElementById("myInput0");
  input1 = document.getElementById("myInput1");
  input2 = document.getElementById("myInput2");
  input3 = document.getElementById("myInput3");

  filter = input.value.toUpperCase();
  filter1 = input1.value.toUpperCase();
  filter2 = input2.value.toUpperCase();
  filter3 = input3.value.toUpperCase();

  table = document.getElementById("table");
  console.log(table)
  tr = table.getElementsByTagName("tr");
  
  console.log(tr)
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[0];
    td1 = tr[i].getElementsByTagName("td")[1];
    td2 = tr[i].getElementsByTagName("td")[2];
    td3 = tr[i].getElementsByTagName("td")[3];
    console.log(td)
    if (td ||td1 || td2 ||td3) {
      txtValue = td.textContent || td.innerText;
      txtValue1 = td1.textContent || td1.innerText;
      txtValue2 = td2.textContent || td2.innerText;
      txtValue3 = td3.textContent || td3.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1 && txtValue1.toUpperCase().indexOf(filter1) > -1 && txtValue2.toUpperCase().indexOf(filter2) > -1 && txtValue3.toUpperCase().indexOf(filter3) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }       
  }
}      


let loader =  `<div class="loader"></div>`;
if(document.getElementById('tbload')){
  document.getElementById('tbload').innerHTML = loader;
}
$(function () {

  const url = '/getdata';
  fetch(url, {method: 'GET'})
      .then(function(response) {
        var resp = response.text();
        console.log(resp);
        return resp;})
      .then(function(data) {

        const obj = JSON.parse(data);
        var final_data = obj['data']; 
        if(document.getElementById('tbload')){
          var tagLoad = document.getElementById('tbload');
          tagLoad.remove();
        }
        
        $('#table').bootstrapTable({
    
          data: final_data
      });

      });
  
});