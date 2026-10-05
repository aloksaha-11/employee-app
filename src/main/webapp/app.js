alert("Hello World");
function sayHello() {
  var name = document.getElementById("name").value;
  alert("Hello! " + name);

  calculateSalary(printSalary);
}
function calculateSalary(xyzfunction) {
  var age = document.getElementById("age").value;
  var salary = 2 * age;
  xyzfunction(salary);
}

function printSalary(salary) {
  console.info("The salary is " + salary);
}

function getEmployee() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = processRequest;
  xhttp.open("GET", "http://localhost:8080/employee-app/rest", true);
  xhttp.send();
}
function ProcessRequest() {
  if (this.status == 200) {
    console.log(xhttp.responseText);
  } else {
    console.log("A network error has happened");
  }
}
