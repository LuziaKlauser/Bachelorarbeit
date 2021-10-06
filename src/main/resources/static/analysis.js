$(document).ready(function () {

    $("#moreInfo").click(function (event) {
        event.preventDefault();
        getEnablerInformation();
    });
    $("#checkIndicators").click(function (event) {
        event.preventDefault();
        getAnswers();
    });

});

function fill(xValues, yValues){
    var barColors = ["Aquamarine", "LightGreen","YellowGreen","green", "PaleTurquoise","LightSeaGreen","SteelBlue"];

    var myChart = document.getElementById("myChart");
    myChart.height=200;
    var ctx = myChart.getContext("2d");

    new Chart(ctx, {
        type: "bar",
        scaleStartValue: 0,
        data: {
            labels: xValues,
            datasets: [{
                backgroundColor: barColors,
                data: yValues
            }]
        },
        options: {
            legend: {display: false},
            title: {
                display: true,
                text: "Capability Level of Enablers:"
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        stepSize: 1
                    }
                }]
            },

        }
    });
}
function start(){
    getEnabler();
    getMaturityLevel();
}

function getMaturityLevel(){
    $.ajax({
        type: "GET",
        url: '/data/calculate',
        success: function (data) {
            var level=document.getElementById("achievedLevel");
            level.innerHTML=data;
            var maturityLevel=data;
            MaturityLevelInformation(maturityLevel);
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}

function getEnabler(){
    var xValues = [];
    var yValues = [];
    $.ajax({
        type: "GET",
        url: '/data/enabler/calculate',
        success: function (data) {
            for(var i =0; i<data.length;i++){
                xValues.push(data[i]["name"]);
                yValues.push(data[i]["capabilityLevel"])
            }
            fill(xValues,yValues);
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}
function getEnablerInformation(){
    $.ajax({
        type: "GET",
        url: '/data/capability_level',
        success: function (data) {
            createTable(data);
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}
function createTable(data) {
    var div = document.getElementById("information");
    div.innerHTML = "";
    var table=document.createElement("table");
    table.setAttribute("class", "table-light table-bordered table-hover");
    var thead=document.createElement("thead");
    var tr=document.createElement("tr");

    var th1=document.createElement("th");
    th1.innerHTML="Level";
    var th2=document.createElement("th");
    th2.innerHTML="Description";
    tr.appendChild(th1);
    tr.appendChild(th2);

    table.appendChild(thead);
    table.appendChild(tr);

    var tbody= document.createElement("tbody");
    for(var i=0; i<data.length;i++){
        var tr=document.createElement("tr");
        var th1=document.createElement("th");
        th1.innerHTML= data[i]["LEVEL"];
        tr.appendChild(th1);
        var th2=document.createElement("th");
        th2.innerHTML= data[i]["DESCRIPTION"];
        tr.appendChild(th2);

        tbody.appendChild(tr);
    }
    table.appendChild(tbody);
    div.appendChild(table);
}
function MaturityLevelInformation(maturityLevel){
    $.ajax({
        type: "GET",
        url: '/data/maturity_level',
        success: function (data) {
            createMaturityLevelInformation(data, maturityLevel);
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}
function createMaturityLevelInformation(data, maturityLevel){
    for(var i=0; i<data.length;i++){
        var id=i+1;
        var a =document.getElementById("navLevel"+id);
        var div =document.getElementById("level"+id);
        if(id==maturityLevel){
            a.setAttribute("class", "nav-link text-dark active");
            div.setAttribute("class", "container tab-pane active")
        }
        if(id==data[i]["LEVEL"]){
            var ul =document.createElement("ul");
            var description= data[i]["DESCRIPTION"];
            var descriptions= description.split("/");
            for(var j=0; j<descriptions.length;j++){
                var li=document.createElement("li");
                li.innerHTML=descriptions[j];
                ul.appendChild(li);
            }
            div.appendChild(ul);
        }
        if(i==data.length-1){
            $(".nav-tabs a").click(function(){
                $(this).tab('show');
            });
        }
    }
}
function getAnswers(){
    $.ajax({
        type: "GET",
        url: '/data/answers',
        success: function (data) {
            createAnswersTables(data);
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}
function createAnswersTables(data){
    var divNo = document.getElementById("tableNo");
    var divYes = document.getElementById("tableYes");
    divYes.innerHTML = "";
    divNo.innerHTML = "";
    var jumbotronYes=document.createElement("div");
    jumbotronYes.setAttribute("class", "jumbotron");
    var jumbotronNo=document.createElement("div");
    jumbotronNo.setAttribute("class", "jumbotron");
    var tableYes=document.createElement("table");
    tableYes.setAttribute("class", "table-light table-bordered table-hover");
    var tableNo=document.createElement("table");
    tableNo.setAttribute("class", "table-light table-bordered table-hover");

    //data is sorted by type, if there is type "no", it is always on the top
    if(!data[0]["TYPE"]===("no")){
        createYesTable(data, divYes, tableYes, jumbotronYes);
    }else{
        createYesTable(data, divYes, tableYes, jumbotronYes);
        createNoTable(data, divNo, tableNo, jumbotronNo);
    }
}
function createYesTable(data, divYes, tableYes, jumbotronYes){
    var tr=document.createElement("tr");
    var th1=document.createElement("th");
    th1.innerHTML="Enabler";
    var th2=document.createElement("th");
    th2.innerHTML="Description";
    var th3=document.createElement("th");
    th3.innerHTML="Type";
    tr.appendChild(th1);
    tr.appendChild(th2);
    tr.appendChild(th3);
    tableYes.appendChild(tr);

    var tbody= document.createElement("tbody");
    for (var i=0; i<data.length;i++){
        console.log(i);
        if(data[i]["TYPE"]===("yes")){
            var tr=document.createElement("tr");
            var th1= createColorsForEnabler(data[i]["ENABLER_NAME"]);
            tr.appendChild(th1);
            var th2=document.createElement("th");
            th2.innerHTML= data[i]["DESCRIPTION"];
            tr.appendChild(th2);
            var th3=document.createElement("th");
            th3.innerHTML= data[i]["TYPE"];
            tr.appendChild(th3);
            tbody.appendChild(tr);
            tableYes.appendChild(tbody);
        }
    }
    var tableTitle=document.createElement("h3");
    tableTitle.innerHTML="Fulfilled indicators";
    jumbotronYes.appendChild(tableTitle);
    jumbotronYes.appendChild(tableYes);
    divYes.appendChild(jumbotronYes);
}
function createNoTable(data, divNo, tableNo, jumbotronNo){
    var tr=document.createElement("tr");
    var th1=document.createElement("th");
    th1.innerHTML="Enabler";
    var th2=document.createElement("th");
    th2.innerHTML="Description";
    var th3=document.createElement("th");
    th3.innerHTML="Type";
    tr.appendChild(th1);
    tr.appendChild(th2);
    tr.appendChild(th3);
    tableNo.appendChild(tr);
    var tbody= document.createElement("tbody");
    for (var i=0; i<data.length;i++){
        if(data[i]["TYPE"]===("no")){
            var tr=document.createElement("tr");
            var th1= createColorsForEnabler(data[i]["ENABLER_NAME"]);
            tr.appendChild(th1);
            var th2=document.createElement("th");
            th2.innerHTML= data[i]["DESCRIPTION"];
            tr.appendChild(th2);
            var th3=document.createElement("th");
            th3.innerHTML= data[i]["TYPE"];
            tr.appendChild(th3);
            tbody.appendChild(tr);
            tableNo.appendChild(tbody);
        }
    }
    var tableTitle=document.createElement("h3");
    var br=document.createElement("br");
    tableTitle.innerHTML="Unfulfilled indicators"
    jumbotronNo.appendChild(br);
    jumbotronNo.appendChild(tableTitle);
    jumbotronNo.appendChild(tableNo);
    divNo.appendChild(jumbotronNo);
}
function createColorsForEnabler(name){
    var th=document.createElement("th");
    th.innerHTML= name;
    var backgroundcolor="background-color:";
    if(name==="Principles, Policies and Frameworks"){
        backgroundcolor+="Aquamarine";
    }else if(name==="Processes") {
        backgroundcolor+="LightGreen";
    }else if(name==="Organizational structures") {
        backgroundcolor+="YellowGreen";
    }else if(name==="Information") {
        backgroundcolor+="green";
    }else if(name==="Culture, ethics and behaviour") {
        backgroundcolor+="PaleTurquoise";
    }else if(name==="Peoples, skills and competences") {
        backgroundcolor+="LightSeaGreen";
    }else if(name==="Services, infrastructure and applications") {
        backgroundcolor+="SteelBlue";
    }
    th.setAttribute("style",backgroundcolor);
    console.log(th);
    return th;
}
