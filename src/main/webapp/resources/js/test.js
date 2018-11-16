var id=window.location.href.split('=')[1];
var xhr = new XMLHttpRequest();
var xhr2 = new XMLHttpRequest();
var mass;
var counter=0;
var massOfTasks;
var massOfAnswers=[];
var testName;

function changeTxtOnButton(event){
    document.getElementById('chooseTestBtn').innerText=event.target.innerText;
}


xhr.onload=function () {
    mass=JSON.parse(xhr.responseText);
    console.log(mass);
};

xhr2.onload=function () {
    //console.log(xhr2.responseText);
    massOfTasks=xhr2.responseText.split('_____');
    document.getElementById('testQuestionsPlace').innerText=massOfTasks[counter];
    document.getElementById('footerOfTestCard').innerHTML="<div class='row'>" +
        "<div class='col-lg-4'> "+
        "<input id=\"testInput\" type=\"text\" class=\"form-control\"> " +
        "</div>"+
        "<div class='col-lg-8'>"+
        "<button type=\"button\" class=\"btn btn-success\" onclick='prevQuestion()'> Previous </button>" +
        "<button type=\"button\" class=\"btn btn-success\" onclick='nextQuestion()'> Next </button>" +
        "<button type=\"button\" class=\"btn btn-success\" onclick='finishTest()'> Finish </button>" +
        "</div>"+
        "</div>";
};

xhr.open("GET", 'http://localhost:8080/getStudentById?id='+id, false);
xhr.send();

window.onload=function innerTxt () {
    document.getElementById('form_name').value=mass[0].name;
    document.getElementById('form_surname').value=mass[0].surname;
    document.getElementById('form_lastname').value=mass[0].patronymic;
    document.getElementById('form_email').value=mass[0].email;
    document.getElementById('form_group').value=mass[0].group;
};

function startTest() {
    testName=document.getElementById('chooseTestBtn').innerText;
    xhr2.open("GET", 'http://localhost:8080/getTest?testName='+testName, false);
    xhr2.send();
}

function nextQuestion() {
    if(counter<massOfTasks.length-1) {
        massOfAnswers[counter]=document.getElementById('testInput').value;
        document.getElementById('testQuestionsPlace').innerText = massOfTasks[++counter];
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
        if(massOfAnswers[counter]!=undefined) {
            document.getElementById('testInput').value = massOfAnswers[counter];
        }else {
            document.getElementById('testInput').value ="";
        }
    }
}

function finishTest() {
     xhr=new XMLHttpRequest();
    xhr.open("POST", 'http://localhost:8080/saveTestResults', false);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    var answers="";
    for(var i=0;i<massOfTasks.length-1;i++){
        answers+=massOfAnswers[i]+"___";
    }
    answers+=massOfAnswers[massOfTasks.length-1];
    var body='answers=' + answers+'&id='+id+'&testName='+testName;
    xhr.send(body);
}


