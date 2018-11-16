var xhr = new XMLHttpRequest();

xhr.onload=function () {
     mass=JSON.parse(xhr.responseText);
     if(mass.length!=0){
         window.location.href ="http://localhost:8080/testPage?id="+mass[0].id;
     }else {
         alert("exception");
     }
}

function Show() {
    $('#myModal').modal('show');
}

function sendAutoInformationToServer() {

    email = document.getElementById('email').value;
    password = document.getElementById('password').value;

    var body = 'email=' + email +
        '&password=' + password;
    xhr.open("POST", 'http://localhost:8080/autoCheck', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(body);
}