var id=window.location.href.split('=')[1];
var xhr = new XMLHttpRequest();
var xhr2 = new XMLHttpRequest();
var mass;
var counter=0;
var massOfTasks;
var massOfAnswers=[];
var testName;
//alert( document.cookie );
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

xhr.open("GET", hostName+'/getStudentById?id='+id, false);
xhr.send();


window.onload=function innerTxt () {
    document.getElementById('footerOfTestCard').style.visibility='hidden';
    document.getElementById('form_name').value=mass[0].name;
    document.getElementById('form_surname').value=mass[0].surname;
    document.getElementById('form_lastname').value=mass[0].patronymic;
    document.getElementById('form_email').value=mass[0].email;
    document.getElementById('form_group').value=mass[0].group;

    if(getCookie("testName")!=undefined){
        xhr2.open("GET", hostName+'/getTest?testName='+getCookie("testName"), false);
        xhr2.send();
    }

    var answersFromCookie=JSON.parse(getCookie("answers"));
    alert(answersFromCookie.length);
    if(answersFromCookie!=undefined){
        for(var i=0; i<answersFromCookie.length;i++){
            massOfAnswers[i]=answersFromCookie[i];
        }
    }
};

function startTest() {
    testName=document.getElementById('chooseTestBtn').innerText;
    xhr2.open("GET", hostName+'/getTest?testName='+testName, false);
    document.cookie = "testName="+testName.toString();
    xhr2.send();
}

function nextQuestion() {
    if(counter<massOfTasks.length-1) {
        massOfAnswers[counter]=document.getElementById('testInput').value;
        document.getElementById('testQuestionsPlace').innerText = massOfTasks[++counter];
        var json_string = JSON.stringify(massOfAnswers);
        document.cookie = "answers="+json_string;
        alert(getCookie("answers"));
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
        alert(getCookie("answers"));
        if(massOfAnswers[counter]!=undefined) {
            document.getElementById('testInput').value = massOfAnswers[counter];
        }else {
            document.getElementById('testInput').value ="";
        }
    }
}

function finishTest() {
    xhr=new XMLHttpRequest();
    xhr.open("POST", hostName+'/saveTestResults', false);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    var answers="";
    for(var i=0;i<massOfTasks.length-1;i++){
        answers+=massOfAnswers[i]+"___";
    }
    answers+=massOfAnswers[massOfTasks.length-1];
    var body='answers=' + answers+'&id='+id+'&testName='+testName;
    xhr.send(body);
}



function getCookie(name) {
    var matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}