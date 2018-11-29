var xhr = new XMLHttpRequest();
var mass;

var hostName = window.location.hostname;
if(hostName=='localhost'){
    hostName+=":8080";
    hostName="http://"+hostName;
}else {
    hostName="https://"+hostName;
}

console.log(hostName.toString());

xhr.onreadystatechange=function () {
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

        document.getElementById('tableOfStudents').innerHTML+="<tr class=\"table-success\">\n" +
            "           <th scope=\"row\">"+"<button id="+mass[i].id+" onclick=\"deleteStudent(this.id)\" type=\"button\" class=\"btn btn-danger btn-circle\"><i class=\"fas fa-map\">-</i></button>"+"</th>\n" +
            "           <td>"+mass[i].name+"</td>\n" +
            "           <td>"+mass[i].surname+"</td>\n" +
            "           <td>"+mass[i].patronymic+"</td>\n" +
            "           <td>"+mass[i].group+"</td>\n" +
            "           <td>"+mass[i].email+"</td>\n" +
            "       </tr>";
    }

}

function deleteStudent(id) {
    var body = 'id=' + id;
    xhr.open("POST", hostName+'/deleteStudent', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(body);
    //refresh of the page
    location.reload();
}

function saveTestResults() {
    xhr.open("POST", hostName+'/saveAllTestResultsInFile', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send();
}


