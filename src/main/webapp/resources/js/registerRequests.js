var xhr = new XMLHttpRequest();

function sendRegisterInformation() {
    name = document.getElementById('form_name').value;
    lastname = document.getElementById('form_lastname').value;
    patronymic = document.getElementById('form_patronymic').value;
    email = document.getElementById('form_email').value;
    numberOfGroup = document.getElementById('form_num').value;
    password=document.getElementById('form_password').value;
    passwordRepeat=document.getElementById('form_passwordR').value;

    var body = 'name=' + name +
        '&lastname=' + lastname+
         '&patronymic='+patronymic+
        '&email='+email+
         '&group='+numberOfGroup+
    '&password='+password;
    xhr.open("POST", 'http://localhost:8080/addNewStudent', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(body);
    // xhr.open("GET", 'http://localhost:8080/testPage', true);
    // xhr.send();
}