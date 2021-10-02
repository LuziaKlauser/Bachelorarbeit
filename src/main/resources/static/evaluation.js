const DATA = "data";

$(document).ready(function () {

    $("#deleteButton").click(function (event) {
        event.preventDefault();
        deleteSurveyAnswers();
    });

});


function deleteSurveyAnswers() {
    var form = $('#deleteForm')[0];
    var data = new FormData(form);
    var surveyId=$("#deleteSurvey").val();

    //$("#deleteButton").prop("disabled", true);

    $.ajax({
        type: "DELETE",
        enctype: 'multipart/form-data',
        url: "/data/answer/" + surveyId,

        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function (data, textStatus, jqXHR) {
            console.log("SUCCESS :", data);
            //$("#deleteButton").prop("disabled", false);
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
        url: '/data/answer/check',
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
