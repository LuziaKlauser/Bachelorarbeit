$(document).ready(function () {

    $("#moreInfo").click(function (event) {
        event.preventDefault();
        getEnablerInformation();
    });
    $(".nav-tabs a").click(function(){
        $(this).tab('show');
    });
});

function fill(xValues, yValues){
    var barColors = ["red", "green","blue","orange", "green","blue","orange"];

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
    MaturityLevelInformation();
}

function getMaturityLevel(){
    $.ajax({
        type: "GET",
        url: '/data/calculate',
        success: function (data) {
            var level=document.getElementById("achievedLevel");
            level.innerHTML=data;
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
        console.log(data[i]);
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
function MaturityLevelInformation(){
    $.ajax({
        type: "GET",
        url: '/data/maturity_level',
        success: function (data) {
            createMaturityLevelInformation(data);
            return data;
        },
        error: function () {
            alert('Error occured');
        }
    });
}
function createMaturityLevelInformation(data){
    var allLevels=document.getElementById("allLevels");
    //allLevels.innerHTML="";
    var show=document.getElementById("showLevel");
    //show.innerHTML="";
    console.log(data);
    for(var i=0; i<data.length;i++){
        var li =document.createElement("li");
        li.setAttribute("class", "nav-item")
        var a =document.createElement("a");
        a.setAttribute("class", "nav-link active");
        a.setAttribute("data-toggle", "tab")
        var name= data[i]["LEVEL"];
        a.setAttribute("href","#"+name);
        a.innerHTML="Level "+name;
        li.appendChild(a);
        allLevels.appendChild(li);

        var div =document.createElement("div");
        div.setAttribute("class", "container tab-pane fade")
        div.setAttribute("id",name);
        div.innerHTML="hheee";
        show.appendChild(div);

    }
}
