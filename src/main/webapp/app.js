function addEmployee(event) {
  //event.preventDefault();
  var $form = $("#employee-form");
  var json = toJson($form);

  $.ajax({
    url: "./api",
    type: "POST",
    data: json,
    contentType: "application/json",
    success: function (response) {
      console.log("Employee created");
      getEmployeeList();
    },
    error: function () {
      alert("An error has occurred");
    },
  });

  return false;
}

function getEmployeeList() {
  $.ajax({
    url: "./api",
    type: "GET",
    success: function (data) {
      console.log("Employee data fetched");
      console.log(data);
      displayEmployeeList(data);
    },
    error: function () {
      alert("An error has occurred");
    },
  });
}

function deleteEmployee(id) {
  $.ajax({
    url: "./api?id=" + id,
    type: "DELETE",
    success: function (data) {
      console.log("Employee deleted");
    },
    error: function () {
      alert("An error has occurred");
    },
  });
}

function displayEmployeeList(data) {
  console.log("Printing employee data");
  var $tbody = $("#employee-table").find("tbody");
  $tbody.empty();
  for (var i in data) {
    var e = data[i];
    var buttonHtml =
      '<button onclick="deleteEmployee(' + e.id + ')">delete</button>';
    var row =
      "<tr>" +
      "<td>" +
      e.id +
      "</td>" +
      "<td>" +
      e.name +
      "</td>" +
      "<td>" +
      e.age +
      "</td>" +
      "<td>" +
      buttonHtml +
      "</td>" +
      "</tr>";
    $tbody.append(row);
  }
}

function toJson($form) {
  var serialized = $form.serializeArray();
  console.log(serialized);
  var s = "";
  var data = {};
  for (s in serialized) {
    data[serialized[s]["name"]] = serialized[s]["value"];
  }
  var json = JSON.stringify(data);
  console.log(json);
  return json;
}

function init() {
  //console.log("initialized function called");
  //alert("initialized");
  $("#add-employee").click(addEmployee);
  $("#refresh-data").click(getEmployeeList);
}

//if (typeof $ !== "undefined") {
$(document).ready(init);
$(document).ready(getEmployeeList);
//} else {
//window.addEventListener("DOMContentLoaded", init);
//}

/* alert("Hello World");
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
  xhttp.open("GET", "./api", true);
  xhttp.send();
}
function ProcessRequest() {
  if (this.status == 200) {
    console.log(xhttp.responseText);
  } else {
    console.log("A network error has happened");
  }
} */
