var xhr = new XMLHttpRequest();
var xhr2 = new XMLHttpRequest();

var hostName = window.location.hostname;
if (hostName == 'localhost') {
    hostName += ":8080";
    hostName = "http://" + hostName;
} else {
    hostName = "https://" + hostName;
}
console.log(hostName.toString());

function sendRegisterInformation() {
    //if(document.getElementById('contact_form').checkValidity()===true) {

    name = document.getElementById('form_name').value;
    lastname = document.getElementById('form_lastname').value;
    patronymic = document.getElementById('form_patronymic').value;
    email = document.getElementById('form_email').value;
    numberOfGroup = document.getElementById('form_num').value;
    password = document.getElementById('form_password').value;
    passwordRepeat = document.getElementById('form_passwordR').value;
    var checker = true;

    if (name.length <= 0) {
        $('#form_name').addClass('is-invalid');
        checker = false;
    }
    if (lastname.length <= 0) {
        $('#form_lastname').addClass('is-invalid');
        checker = false;
    }
    if (patronymic.length <= 0) {
        $('#form_patronymic').addClass('is-invalid');
        checker = false;
    }
    if (numberOfGroup.length <= 0) {
        $('#form_num').addClass('is-invalid');
        checker = false;
    }
    if (password.length <= 5) {
        $('#form_password').addClass('is-invalid');
        checker = false;
    }

    if (email.length <= 0) {
        $('#form_email').addClass('is-invalid');
        checker = false;
    }
    if (passwordRepeat != password) {
        checker = false;
    }
    // if(email.length<=0) {
    //     $('#form_num').addClass('is-invalid');
    // }
    if (checker) {
        var body = 'name=' + name +
            '&lastname=' + lastname +
            '&patronymic=' + patronymic +
            '&email=' + email +
            '&group=' + numberOfGroup +
            '&password=' + password;
        xhr.open("POST", hostName + '/addNewStudent', true);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.send(body);
    }
}

xhr.onload = function () {
    // document.getElementById('contact_form').classList.add('needs-validation');
    // document.getElementById('contact_form').classList.remove('was-validated');
    // document.getElementById('form_email').style.border="2px solid red";
    // document.getElementById('form_email').classList.add('error')
    //$('#form_email').parent().addClass('is-invalid');
    if (xhr.responseText == "false") {
        document.getElementById('emailFeedBack').innerText = 'SOMEBODY USE THIS EMAIL! YOU MUST ENTER ANOTHER EMAIL!';
        $('#form_email').addClass('is-invalid');
        $('#form_email').removeClass('is-valid');
    } else {
        var student = JSON.parse(xhr.responseText);
        // alert(student.email);

        var body = "j_username=" + student.email + "&j_password=" + student.password;
        xhr2.open("POST", hostName + "/j_spring_security_check", true);
        xhr2.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr2.send(body);
        xhr2.onload = function () {
            document.location.href = hostName + "/testPage?id=" + student.id;
        }
        //
    }
};

function validation(txt) {
    if (txt.value.length > 0) {
        $('#' + txt.id).addClass('is-valid');
        $('#' + txt.id).removeClass('is-invalid');
    } else {
        $('#' + txt.id).addClass('is-invalid');
        $('#' + txt.id).removeClass('is-valid');
    }
}


function checkRepeatPassword(txt) {
    //alert(txt.value+" "+$('#form_password').value)
    if (txt.value != document.getElementById('form_password').value)
        $('#' + txt.id).addClass('is-invalid');
    else {
        $('#' + txt.id).addClass('is-valid');
        $('#' + txt.id).removeClass('is-invalid');
    }
}

function checkPassword(txt) {
    //alert(txt.value+" "+$('#form_password').value)
    if (txt.value != document.getElementById('form_passwordR').value)
        $('#form_passwordR').addClass('is-invalid');
    else {
        $('#form_passwordR').addClass('is-valid');
        $('#form_passwordR').removeClass('is-invalid');
    }
    if (txt.value.length >= 6) {
        $('#form_password').addClass('is-valid');
        $('#form_password').removeClass('is-invalid');
    } else {
        $('#form_password').addClass('is-invalid');
        $('#form_password').removeClass('is-valid');
    }
}

function checkEmail(txt) {
    document.getElementById('emailFeedBack').innerText = 'You must write your email';
    if (!txt.validity.patternMismatch) {
        $('#form_email').addClass('is-valid');
        $('#form_email').removeClass('is-invalid');
    }
    else {
        $('#form_email').addClass('is-invalid');
        $('#form_email').removeClass('is-valid');
    }
}