function Show() {
    document.getElementById('regReference').setAttribute('href', '/register');
    $('#myModal').modal('show');
}

// function sendAutoInformationToServer() {
//
//     email = document.getElementById('email').value;
//     password = document.getElementById('password').value;
//
//     var body = 'email=' + email +
//         '&password=' + password;
//     xhr.open("POST", hostName + '/autoCheck', true);
//     xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
//     xhr.send(body);
// }