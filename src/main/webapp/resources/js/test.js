var id = window.location.href.split('=')[1].replace('#', '');
var xhr = new XMLHttpRequest();
var xhr2 = new XMLHttpRequest();
var xhr3 = new XMLHttpRequest();
var xhr4 = new XMLHttpRequest();
var xhr5 = new XMLHttpRequest();
var mass;
var counter=0;
var massOfTasks;
var massOfAnswers=[];
var testName;
var nameOfTheTests = [];

var hostName = window.location.hostname;
if(hostName=='localhost'){
    hostName+=":8080";
    hostName="http://"+hostName;
}else {
    hostName="https://"+hostName;
}

function changeTxtOnButton(event){
    document.getElementById('chooseTestBtn').innerText=event.target.innerText;
}


xhr.onload=function () {
    mass=JSON.parse(xhr.responseText);
    console.log(mass);
};

xhr2.onload=function () {
    massOfTasks=xhr2.responseText.split('_____');
    document.getElementById('testQuestionsPlace').innerText=massOfTasks[counter];
    document.getElementById('footerOfTestCard').style.visibility='visible';
};

xhr3.onload = function () {
    nameOfTheTests = xhr3.responseText.split(' ');
};

xhr3.open("GET", hostName + "/getAllTestNames?id=" + id, false);
xhr3.send();

xhr.open("GET", hostName+'/getStudentById?id='+id, false);
xhr.send();


window.onload=function innerTxt () {
    document.getElementById('footerOfTestCard').style.visibility='hidden';
    document.getElementById('form_name').value=mass[0].name;
    document.getElementById('form_surname').value=mass[0].surname;
    document.getElementById('form_lastname').value=mass[0].patronymic;
    document.getElementById('form_email').value=mass[0].email;
    document.getElementById('form_group').value=mass[0].group;

    if (getCookie("testName") != undefined && getCookie("testName") != '') {
        alert(getCookie("testName"));
        xhr2.open("GET", hostName+'/getTest?testName='+getCookie("testName"), false);
        xhr2.send();
    }

    // if(getCookie("answers")!=undefined) {
    //     for (var i = 0; i <JSON.parse(getCookie("answers")).length;i++)
    //         massOfAnswers[i]=JSON.parse(getCookie("answers"))[i];
    //        // alert(JSON.parse(getCookie("answers"))[0]);
    // }

    for (var i = 0; i < nameOfTheTests.length; i++)
        document.getElementById("nameOfTests").innerHTML += "<a class='dropdown-item' href='#'>" + nameOfTheTests[i] + "</a>"
    // var answersFromCookie=JSON.parse(getCookie("answers"));
    // alert(answersFromCookie.length);
    // if(answersFromCookie!=undefined){
    //     for(var i=0; i<answersFromCookie.length;i++){
    //         massOfAnswers[i]=answersFromCookie[i];
    //     }
    // }
};

function startTest() {
    testName=document.getElementById('chooseTestBtn').innerText;
    xhr2.open("GET", hostName+'/getTest?testName='+testName, false);
    document.cookie = "testName="+testName.toString();
    document.cookie = "answers=" + "";
    massOfAnswers = [];
    xhr2.send();
}

function nextQuestion() {
    if(counter<massOfTasks.length-1) {
        massOfAnswers[counter]=document.getElementById('testInput').value;
        document.getElementById('testQuestionsPlace').innerText = massOfTasks[++counter];
        var json_string = JSON.stringify(massOfAnswers);
        document.cookie = "answers="+json_string;
        if(massOfAnswers[counter]!=undefined) {
            document.getElementById('testInput').value = massOfAnswers[counter];
        }else {
            document.getElementById('testInput').value ="";
        }
    }
}

function prevQuestion() {
    if(counter>0) {
        massOfAnswers[counter]=document.getElementById('testInput').value;
        document.getElementById('testQuestionsPlace').innerText = massOfTasks[--counter];
        var json_string = JSON.stringify(massOfAnswers);
        document.cookie = "answers="+json_string;
        if(massOfAnswers[counter]!=undefined) {
            document.getElementById('testInput').value = massOfAnswers[counter];
        }else {
            document.getElementById('testInput').value ="";
        }
    }
}

function finishTest() {
    //??
    massOfAnswers[counter] = document.getElementById('testInput').value;
    //
    xhr4 = new XMLHttpRequest();
    xhr4.open("POST", hostName + '/saveTestResults', false);
    xhr4.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    var answers="";
    for(var i=0;i<massOfTasks.length-1;i++){
        answers+=massOfAnswers[i]+"___";
    }
    answers+=massOfAnswers[massOfTasks.length-1];
    var body='answers=' + answers+'&id='+id+'&testName='+testName;
    xhr4.send(body);

    document.cookie = "answers=;";
    document.cookie = "testName=;";
    document.getElementById('footerOfTestCard').style.visibility = 'hidden';
    location.reload();
}

// xhr4.onload=function () {
//    console(xhr4.responseText);
// };

function viewTestResults() {
    xhr5.open("GET", hostName + '/getStudentTests?id=' + id, true)
    xhr5.send();
}

xhr5.onload = function () {
    console.log(xhr2.responseText);
    var massOfTestResults = xhr5.responseText;
    massOfTestResults = JSON.parse(massOfTestResults);
    console.log(massOfTestResults[0]);


    var name = mass[0].name;
    var surname = mass[0].surname;
    var lastname = mass[0].patronymic;
    var group = mass[0].group;

    $('#myModal').modal('show');
    document.getElementById('studentFio').innerText = name + " " + surname + " " + lastname + " " + "\n" + group;
    document.getElementById('tableOfTestResults').innerHTML = "";
    for (var i = 0; i < massOfTestResults.length; i++)
        document.getElementById('tableOfTestResults').innerHTML +=
            "<tr>" +
            "<td><input class=\"form-control\" value=" + massOfTestResults[i].test_name + " disabled></td>" +
            "<td><input class=\"form-control\" value=" + massOfTestResults[i].result + " disabled></td>" +
            "</tr>";

};

function showUserInfo() {
    $('#modalUserInfo').modal('show');
}

function getCookie(name) {
    var matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}
