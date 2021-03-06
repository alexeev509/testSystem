var massOfAnswers = [];
var massOfQuestions = [];
var size = 0;
var xhr = new XMLHttpRequest();
var testName;

var hostName = window.location.hostname;
if (hostName == 'localhost') {
    hostName += ":8080";
    hostName = "http://" + hostName;
} else {
    hostName = "https://" + hostName;
}


function nextQuestion() {
    massOfQuestions[size] = document.getElementById("question").value;
    massOfAnswers[size] = document.getElementById("test_answer").value;
    ++size;
    if (massOfQuestions[size] == undefined) {
        document.getElementById("question").value = "";
    }
    else {
        document.getElementById("question").value = massOfQuestions[size];
    }

    if (massOfAnswers[size] == undefined) {
        document.getElementById("test_answer").value = "";
    }
    else {
        document.getElementById("test_answer").value = massOfAnswers[size];
    }
}

function prevQuestion() {
    massOfQuestions[size] = document.getElementById("question").value;
    massOfAnswers[size] = document.getElementById("test_answer").value;
    if (size > 0)
        --size;
    if (massOfQuestions[size] == undefined) {
        document.getElementById("question").value = "";
    }
    else {
        document.getElementById("question").value = massOfQuestions[size];
    }

    if (massOfAnswers[size] == undefined) {
        document.getElementById("test_answer").value = "";
    }
    else {
        document.getElementById("test_answer").value = massOfAnswers[size];
    }
}

function finish() {
    massOfAnswers[size] = document.getElementById("test_answer").value;
    massOfQuestions[size] = document.getElementById("question").value;

    testName = document.getElementById("test_name").value;
    xhr.open("POST", hostName + '/saveNewTest', false);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
    console.log(massOfQuestions);
    //prepare questions and answers
    var questions = "";
    for (var i = 0; i < massOfQuestions.length - 1; i++)
        questions += massOfQuestions[i] + "\n" + "_____" + "\n";
    questions += massOfQuestions[massOfQuestions.length - 1];
    //
    var answers = "";
    for (var i = 0; i < massOfAnswers.length - 1; i++)
        answers += massOfAnswers[i].trim() + "\n";
    answers += massOfAnswers[massOfAnswers.length - 1];
    //
    var body = 'questions=' + questions + '&answers=' + answers + '&testName=' + testName;
    xhr.send(body);
    window.location = hostName + "/admin";
}