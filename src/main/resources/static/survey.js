
function createSurvey() {
    $.ajax({
        type: "GET",
        url: '/data/survey/'+getSurveyId(),
        success: function (data) {
            // Call this function on success
            createForm(data);
            var button = document.getElementById("surveyButton");
            button.style.display = 'none';
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}
function createForm(data){
    var questions = data;
    var form = document.createElement("form");
    form.setAttribute("method", "post");
    form.setAttribute("id", "surveyForm");
    form.setAttribute("enctype", "multipart/form-data");
    //API to upload the formdata
    form.setAttribute("action", "/survey/upload");

    var card = document.createElement("div");
    card.setAttribute("class", "card");

    for( var i =0; i<questions.length; i++){
        //Create card-header
        var cardHeader = document.createElement("div");
        cardHeader.setAttribute("class", "card-header");
        var id=questions[i].INDICATOR_ID;
        var question=questions[i].QUESTION;
        cardHeader.setAttribute("id", id);
        cardHeader.setAttribute("value", question);
        cardHeader.innerHTML=(i+1)+". "+question;
        card.appendChild(cardHeader);

        //Create card-body
        var cardBody= document.createElement("div");
        cardBody.setAttribute("class", "card-body");
        //Input Yes
        var inputYes = document.createElement("input");
        inputYes.required=true;
        inputYes.setAttribute("type", "radio")
        inputYes.setAttribute("name", id)
        inputYes.setAttribute("id", "yes"+id);
        inputYes.setAttribute("value", "yes");
        var labelYes =document.createElement("label");
        labelYes.setAttribute("for", "yes"+id);
        labelYes.innerHTML="YES";

        //ADD Input, LAbel Yes to card-body
        cardBody.appendChild(inputYes);
        cardBody.appendChild(labelYes);

        //Input No
        var inputNo = document.createElement("input");
        inputNo.setAttribute("type", "radio");
        inputNo.setAttribute("name", id);
        inputNo.setAttribute("id", "no"+id);
        inputNo.setAttribute("value", "no");
        var labelNo =document.createElement("label");
        labelNo.setAttribute("for", "no"+id);
        labelNo.innerHTML="NO";

        //ADD Input, label No to card-body
        cardBody.appendChild(inputNo);
        cardBody.appendChild(labelNo);

        card.appendChild(cardBody);
    }
    // create a submit button
    var s = document.createElement("input");
    s.setAttribute("type", "submit");
    s.setAttribute("value", "Submit");
    s.setAttribute("id", "submitUpload");
    form.appendChild(card);
    form.appendChild(s);

    var divContainer = document.getElementById("survey");
    divContainer.appendChild(form);
}

function getSurveyId() {
    var pathArray = window.location.pathname.split('/');
    var surveyId=pathArray[2];
    return surveyId;
}


