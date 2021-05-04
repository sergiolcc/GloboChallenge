console.log("CHEGAMOS AQUI")
let loader =  `<div class="loader"></div>`;
document.getElementById('tbload').innerHTML = loader;
$(function () {

  const url = '/getdata';
  fetch(url, {method: 'GET'})
      .then(function(response) {
        var resp = response.text();
        console.log(resp);
        return resp;})
      .then(function(data) {
        console.log(data)

        const obj = JSON.parse(data);
        var final_data = obj['data']; 
        
        $('#table').bootstrapTable({
    
          data: final_data
      });

      });
  
});