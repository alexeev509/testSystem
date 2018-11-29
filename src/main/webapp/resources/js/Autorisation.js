var xhr = new XMLHttpRequest();
var hostName = window.location.hostname;
if(hostName=='localhost'){
    hostName+=":8080";
    hostName="http://"+hostName;
}else {
    hostName="https://"+hostName;
}
console.log(hostName.toString());


xhr.onload=function () {
     mass=JSON.parse(xhr.responseText);
     if(mass.length!=0){
         window.location.href =hostName+"/testPage?id="+mass[0].id;
     }else {
         alert("exception");
     }
}

function Show() {
    document.getElementById('regReference').setAttribute('href','/register');
    $('#myModal').modal('show');
}

function sendAutoInformationToServer() {

    email = document.getElementById('email').value;
    password = document.getElementById('password').value;

    var body = 'email=' + email +
        '&password=' + password;
    xhr.open("POST", hostName+'/autoCheck', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(body);
}