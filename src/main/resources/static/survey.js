
function functionData(data) {
    var myBooks = data;
    // EXTRACT VALUE FOR HTML HEADER.
    // ('Book ID', 'Book Name', 'Category' and 'Price')
    var col = [];
    for (var i = 0; i < myBooks.length; i++) {
        for (var key in myBooks[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }
    // CREATE DYNAMIC TABLE.
    var table = document.createElement("table");

    // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

    var tr = table.insertRow(-1);                   // TABLE ROW.

    for (var i = 0; i < col.length; i++) {
        var th = document.createElement("th");      // TABLE HEADER.
        th.innerHTML = col[i];
        tr.appendChild(th);
    }

    // ADD JSON DATA TO THE TABLE AS ROWS.
    for (var i = 0; i < myBooks.length; i++) {

        tr = table.insertRow(-1);

        for (var j = 0; j < col.length; j++) {
            var tabCell = tr.insertCell(-1);
            tabCell.innerHTML = myBooks[i][col[j]];
        }
    }

    // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
    var divContainer = document.getElementById("showSurvey");
    console.log(divContainer);
    divContainer.innerHTML = "";
    divContainer.appendChild(table);
}
function createTable(){
    $.ajax({
        type: "GET",
        url: '/data/indicator',
        success: function (data) {
            // Call this function on success
            functionData(data);
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}

function getData (tableName, callback){
    $.ajax({
        url: "/data/enabler",
        method: "GET",
        dataType: "json",
        error: function (){
            alert('Error occured');
        },
    })
        .done(callback);
}

function createSurvey() {
    $.ajax({
        type: "GET",
        url: '/survey/2',
        success: function (data) {
            // Call this function on success
            createForm(data);
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
    form.setAttribute("action", "submit.php");

    var card = document.createElement("div");
    card.setAttribute("class", "card");

    for( var i =0; i<questions.length; i++){
        //Create card-header
        var cardHeader = document.createElement("div");
        cardHeader.setAttribute("class", "card-header");
        var id=questions[i].INDICATOR_ID;
        var question=questions[i].QUESTION
        cardHeader.setAttribute("id", id);
        cardHeader.setAttribute("value", question);
        cardHeader.innerHTML=(i+1)+". "+question;
        card.appendChild(cardHeader);

        //Create card-body
        var cardBody= document.createElement("div");
        cardBody.setAttribute("class", "card-body");
        //Input Yes
        var inputYes = document.createElement("input");
        inputYes.setAttribute("type", "radio")
        inputYes.setAttribute("name", id)
        inputYes.setAttribute("id", "yes"+id);
        var labelYes =document.createElement("label");
        labelYes.setAttribute("for", "yes"+id);
        labelYes.innerHTML="YES  ";

        //ADD Input, LAbel Yes to card-body
        cardBody.appendChild(inputYes);
        cardBody.appendChild(labelYes);

        //Input No
        var inputNo = document.createElement("input");
        inputNo.setAttribute("type", "radio");
        inputNo.setAttribute("name", id);
        inputNo.setAttribute("id", "no"+id);
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
    form.appendChild(card);
    form.appendChild(s);

    var divContainer = document.getElementById("survey");
    divContainer.appendChild(form);
    var button= divContainer.getElementsByTagName("surveyButton");
    button.setAttribute("display", "none");


}

