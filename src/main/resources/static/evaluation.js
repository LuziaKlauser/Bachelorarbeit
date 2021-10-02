
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