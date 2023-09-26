///Listeners for the table headers
const sortID_label = document.getElementById("sortID");
const sortNome_label = document.getElementById("sortNome");
const sortPreco_label = document.getElementById("sortPreco");
/*
sortID_label.addEventListener('click', function () {
    fetch('/produtos/list/1', {
        method: "POST"
    })
        .then(response => response.text())
        .then(data => {
            document.getElementById("SparkUpdateArea").innerHTML = data;
        })
});
sortNome_label.addEventListener('click', function () {
    fetch('/produtos/list/2', {
        method: "POST"
    })
        .then(response => response.text())
        .then(data => {
            document.getElementById("SparkUpdateArea").innerHTML = data;
        })
});
sortPreco_label.addEventListener('click', function () {
    fetch('/produtos/list/3', {
        method: "POST"
    })
        .then(response => response.text())
        .then(data => {
            document.getElementById("SparkUpdateArea").innerHTML = data;
        })
});
*/
//---------------------------------------------------------------
///Listener for insert form submission
const insertForm = document.getElementById("insertForm");

insertForm.addEventListener('submit', function (event) {
	event.preventDefault(); 
    fetch('/product/insert', {
        method: "POST",
        body: new FormData(insertForm)
    })
        .then(response => response.json())
        .then(data => {
			if (!data.status){
				data.errors.forEach(error => {
					alert(error);
				});
			} else {
				alert("sucess");
				document.getElementById("SparkUpdateArea").innerHTML = data.data;
			}
            
        })
});