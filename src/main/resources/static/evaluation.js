const DATA = "data";
const SURVEY_IDS =[];

$(document).ready(function () {

    $("#deleteButton").click(function (event) {
        event.preventDefault();
        deleteSurveyAnswers();
    });
    $("#sendSurveyButton").click(function (event) {
        event.preventDefault();
        sendMail();
    });
    $("#getTimeButton").click(function (event) {
        event.preventDefault();
        getTime();
    });


});

function start(){
    getStatus();
    addSurveySend();

}

function deleteSurveyAnswers() {
    var form = $('#deleteForm')[0];
    var data = new FormData(form);
    var surveyId=$("#deleteSurvey").val();

    //$("#deleteButton").prop("disabled", true);

    $.ajax({
        type: "DELETE",
        enctype: 'multipart/form-data',
        url: "/data/indicator_value/" + surveyId,

        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function (data, textStatus, jqXHR) {
            alert("Deletion successful");
            $('#deleteEntryForm')[0].reset();
            getStatus();
        }, error: function () {
            window.write("nope");
        }

    });
}
function getStatus(){
    $.ajax({
        type: "GET",
        url: '/data/indicator/check',
        success: function (data) {
            // Call this function on success
            var status=data.allanswers;
            var progress = document.getElementById("progress");
            progress.style.width = status + '%';
            progress.innerHTML=status+"%";
            var button = document.getElementById("calculateMaturity");
            if(status==100){
                button.disabled = false;
            }else{
                button.disabled=true;
            }
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}
function addSurveySend(){
    var form = document.getElementById("surveyToSend");
    $.ajax({
        type: "GET",
        url: '/data/survey/surveyIds',
        success: function (data) {
            // Call this function on success
            var br = document.createElement("br");
            for(var i=0; i<data.length;i++){
                var surveyId=(data[i].SURVEY_ID);
                SURVEY_IDS.push(surveyId);
                var inputCheckbox = document.createElement("input");
                inputCheckbox.setAttribute("type", "checkbox")
                inputCheckbox.setAttribute("id", "survey"+surveyId);
                inputCheckbox.setAttribute("value", surveyId);
                var labelCheckbox =document.createElement("label");
                labelCheckbox.setAttribute("for", "survey"+surveyId);
                labelCheckbox.innerHTML="Survey "+surveyId;
                form.appendChild(inputCheckbox);
                form.appendChild(labelCheckbox);
                var br = document.createElement("br");
                form.appendChild(br);
            }
            form.appendChild(br);
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}
function sendMail(){
    const send=[];
    for(var i=0;i<SURVEY_IDS.length; i++){
        var surveyId=SURVEY_IDS[i];
        var checkbox= document.getElementById("survey"+surveyId);
        if(checkbox.checked){
            send.push(surveyId);
            $.ajax({
                type: "POST",
                enctype: 'multipart/form-data',
                url: "/survey/"+surveyId+"/send",

                processData: false,
                contentType: false,
                cache: false,
                timeout: 1000000,
                success: function (data, textStatus, jqXHR) {
                }, error: function () {

                }
            });
        }
    }
    if(send.length>0){
        var message= "Successfully send";
        for(var i=0; i<send.length;i++){
            message+=" Survey "+send[i];
        }
        alert(message);
    }
}

function getTime(){
    $.ajax({
        type: "GET",
        url: '/data/indicator_value/time',
        success: function (data) {
            // Call this function on success
            createTable(data);
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}
function createTable(data){
    var div=document.getElementById("lastUpdate");
    div.innerHTML="";
    var table=document.createElement("table");
    table.setAttribute("class", "table");
    var thead=document.createElement("thead");
    var tr=document.createElement("tr");
    var th=document.createElement("th");
    th.innerHTML="#";
    tr.appendChild(th)
    for(var j in data[0]){
        var th=document.createElement("th");
        th.innerHTML=j;
        tr.appendChild(th);
    }
    table.appendChild(thead);
    table.appendChild(tr);
    var tbody= document.createElement("tbody");
    for(var i=0; i<data.length;i++){
        var tr=document.createElement("tr");
        var th=document.createElement("th");
        th.innerHTML=i+1;
        tr.appendChild(th);
        for(var key in data[i]){
            var th=document.createElement("th");
            th.innerHTML= data[i][key];
            tr.appendChild(th);
        }
        tbody.appendChild(tr);
    }
    table.appendChild(tbody);
    div.appendChild(table);
}