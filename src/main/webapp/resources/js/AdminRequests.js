var xhr = new XMLHttpRequest();
var xhr2 = new XMLHttpRequest();
var xhr3 = new XMLHttpRequest();
var xhr4 = new XMLHttpRequest();
var xhr5 = new XMLHttpRequest();
var xhr6 = new XMLHttpRequest();
var xhr7 = new XMLHttpRequest();
var mass;
var massOfTestResults;
var currentId_helperField = -1;

var hostName = window.location.hostname;
if(hostName=='localhost'){
    hostName+=":8080";
    hostName="http://"+hostName;
}else {
    hostName="https://"+hostName;
}

console.log(hostName.toString());

xhr.onload = function () {
    console.log(xhr.responseText);
    mass=xhr.responseText;
    mass=JSON.parse(mass);
    console.log(mass[0]);
}
xhr.open("GET", hostName+'/getallstudents', false);
xhr.send();


function showAllStudents() {
    for(var i=0;i<mass.length;i++) {
        // document.getElementById('students').innerText += "name:=" + mass[i].name;
        // document.getElementById('students').innerText += " surname:=" + mass[i].surname;
        // document.getElementById('students').innerText += " lastname:=" + mass[i].patronymic;
        // document.getElementById('students').innerText += " group:=" + mass[i].group;
        // document.getElementById('students').innerText += " email:=" + mass[i].email;
        // document.getElementById('students').innerHTML+="<br>";

        document.getElementById('tableOfStudents').innerHTML += "<tr class=\"bg-warning\" id=" + mass[i].id + ">\n" +
            "           <td><button type=\"button\" onclick='getStudentTests(this.parentNode.parentNode.id)' class=\"btn btn-default btn-lg\"><span class=\"glyphicon glyphicon-star\"></span> + </button></td>\n" +
            "           <td><input class=\"form-control\" value=" + mass[i].name + "></td>\n" +
            "           <td><input class=\"form-control\" value=" + mass[i].surname + "></td>\n" +
            "           <td><input class=\"form-control\" value=" + mass[i].patronymic + "></td>\n" +
            "           <td><input class=\"form-control\" value=" + mass[i].group + "></td>\n" +
            "           <td><input class=\"form-control\" value=" + mass[i].email + "></td>\n" +
            "<td><button class=\"btn btn-lg btn-success\" onclick='updateStudent(this.parentNode.parentNode.id)' name=" + mass[i].id + ">Save</button></td>" +
            "           <td scope=\"row\">" + "<button onclick=\"deleteStudent(this.parentNode.parentNode.id)\" type=\"button\" class=\"btn btn-danger btn-circle\"><i class=\"fas fa-map\">-</i></button>" + "</td>\n" +
            "       </tr>";
    }

}

function deleteStudent(id) {
    var body = 'id=' + id;
    xhr.open("POST", hostName+'/deleteStudent', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(body);
    //refresh of the page
    //WE MUST REFRESH WHEN WE GET ANSWER FROM SERVER
    location.reload();
}

function updateStudent(id) {
    var userInfoFromMass = document.getElementById(id).getElementsByTagName('td');
    var name = userInfoFromMass[1].getElementsByTagName('input')[0].value;
    var surname = userInfoFromMass[2].getElementsByTagName('input')[0].value;
    var lastname = userInfoFromMass[3].getElementsByTagName('input')[0].value;
    var group = userInfoFromMass[4].getElementsByTagName('input')[0].value;
    var email = userInfoFromMass[5].getElementsByTagName('input')[0].value;
    var body = 'id=' + id + '&name=' + name + '&surname=' + surname +
        '&lastname=' + lastname + '&group=' + group
        + '&email=' + email;
    xhr.open("POST", hostName + '/updateStudent', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(body);
}

function saveTestResults() {
    xhr.open("POST", hostName+'/saveAllTestResultsInFile', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send();
}

function getStudentTests(id) {
    currentId_helperField = id;
    xhr2.open("GET", hostName + '/getStudentTests?id=' + id, true)
    xhr2.send();
}

xhr2.onload = function () {
    console.log(xhr2.responseText);
    massOfTestResults = xhr2.responseText;
    massOfTestResults = JSON.parse(massOfTestResults);
    console.log(massOfTestResults[0]);


    var userInfoFromMass = document.getElementById(currentId_helperField).getElementsByTagName('td');
    var name = userInfoFromMass[1].getElementsByTagName('input')[0].value;
    var surname = userInfoFromMass[2].getElementsByTagName('input')[0].value;
    var lastname = userInfoFromMass[3].getElementsByTagName('input')[0].value;
    var group = userInfoFromMass[4].getElementsByTagName('input')[0].value;

    $('#myModal').modal('show');
    document.getElementById('studentFio').innerText = name + " " + surname + " " + lastname + " " + "\n" + group;
    document.getElementById('tableOfTestResults').innerHTML = "";
    for (var i = 0; i < massOfTestResults.length; i++)
        document.getElementById('tableOfTestResults').innerHTML +=
            "<tr>" +
            "<td><input class=\"form-control\" value=" + massOfTestResults[i].test_name + " disabled></td>" +
            "<td><input class=\"form-control\" value=" + massOfTestResults[i].result + "></td>" +
            "<td><button class='btn btn-lg btn-success' onclick='updateTest(this)'>Save</button></td>" +
            "<td><button onclick='deleteTest(this)' type=\"button\" class=\"btn btn-danger btn-circle\"><i class=\"fas fa-map\">-</i></button></td>" +
            "</tr>";

};

function deleteTest(obj) {
    //alert(obj.parentNode.parentNode.childNodes[0].getElementsByTagName('input')[0].value);
    var massOfCurrentTestValues = obj.parentNode.parentNode.childNodes;
    var testName = massOfCurrentTestValues[0].getElementsByTagName('input')[0].value;
    var body = 'id=' + currentId_helperField + '&test_name=' + testName;
    xhr4.open("POST", hostName + '/deleteStudentsTest', true);
    xhr4.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr4.send(body);
    obj.parentNode.parentNode.innerHTML = "";
    document.getElementById('tableOfTestResults').refresh();
}

function updateTest(obj) {
    var massOfCurrentTestValues = obj.parentNode.parentNode.childNodes;
    var testName = massOfCurrentTestValues[0].getElementsByTagName('input')[0].value;
    var result = massOfCurrentTestValues[1].getElementsByTagName('input')[0].value;
    //Change % because % in url has code %25
    var body = 'id=' + currentId_helperField + '&test_name=' + testName + '&result=' + result.replace("%", "%25");
    xhr3.open("POST", hostName + '/updateStudentTest', true);
    xhr3.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr3.send(body);
}

// function addNewTest() {
//     document.getElementById("studentTable").style.visibility="hidden";
//     document.getElementById("addNewTestForm").style.visibility="visible";
// }
//
// Window.onload=function () {
//     document.getElementById("addNewTestForm").style.visibility="hidden";
// }

function viewTests() {
    xhr5.open("GET", hostName + "/getTestNames", false);
    xhr5.send();
}

xhr5.onload = function () {
    var massOfTestNames = xhr5.responseText.split(' ');
    $('#modalOfTests').modal('show');
    document.getElementById('tableOfTests').innerHTML = "";
    for (var i = 0; i < massOfTestNames.length; i++)
        document.getElementById('tableOfTests').innerHTML +=
            "<tr>" +
            "<td><input class=\"form-control\" value=" + massOfTestNames[i] + " disabled></td>" +
            "<td><button onclick='deleteTestName(this)' type=\"button\" class=\"btn btn-danger btn-circle\"><i class=\"fas fa-map\">-</i></button></td>" +
            "</tr>";
};

function deleteTestName(btn) {
    var body = "testName=" + btn.parentNode.parentNode.firstChild.firstChild.value;
    xhr6.open("POST", hostName + "/deleteTestByName", false);
    xhr6.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr6.send(body);
    raw = btn.parentNode.parentNode;
    raw.parentNode.removeChild(raw);
}


function showAdminPanel() {
    $('#adminRegister').modal('show');
}

function addNewAdmin() {
    if (document.getElementById('form_password').value == document.getElementById('form_passwordR').value
        && document.getElementById('form_password').value.length != 0) {
        var body = "email=" + document.getElementById("form_email_admin").value + "&password=" + document.getElementById("form_password").value;
        xhr7.open("POST", "/addNewAdmin", false);
        xhr7.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr7.send(body);
    }
}

xhr7.onload = function () {
    console.log(xhr7.responseText);
    if (xhr7.responseText == "false") {
        document.getElementById('emailFeedBack').innerText = 'SOMEBODY USE THIS EMAIL! YOU MUST ENTER ANOTHER EMAIL!';
        $('#form_email_admin').addClass('is-invalid');
        $('#form_email_admin').removeClass('is-valid');
    }
    else {
        $('#form_email_admin').addClass('is-valid');
        $('#form_email_admin').removeClass('is-invalid');
    }
};


function checkEmail(txt) {
    document.getElementById('emailFeedBack').innerText = 'You must write admin email';
    if (!txt.validity.patternMismatch) {
        $('#form_email_admin').addClass('is-valid');
        $('#form_email_admin').removeClass('is-invalid');
    }
    else {
        $('#form_email_admin').addClass('is-invalid');
        $('#form_email_admin').removeClass('is-valid');
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