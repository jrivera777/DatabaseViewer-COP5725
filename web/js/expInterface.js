var dbCubes;
var defltCube = "<option>Select Cube...</option>";
var defltMeasure = "<option>Select Measure...</option>";
var options;
 
//Load cubeSelect
$("document").ready(function(){
    $('#cubeSelect').append(defltCube);
    $('#measureSelect').append(defltMeasure);
    $.get('DBInfo', function(data){
        dbCubes = data[0].children[2];
        //alert(dbCubes.label);
        $.each(dbCubes.children, function(i, item){
            var cubeName = "<option>" + item.label + "</option>";
            $('#cubeSelect').append(cubeName);
        });
    });
});

//load measure from cube
$("document").ready(function(){
    $('#cubeSelect').change(function(){
        var selectedIndex = $(this).prop('selectedIndex');
        $('#measureSelect').empty();
        $('#measureSelect').append(defltMeasure);
        $('#dimensionCollection').empty();
        $('#graphCollection').empty();
        if(selectedIndex > 0)
        {
            var cube = $(this).val();
            var measures = getMeasuresFromCube(getCubeByName(cube));
            $.each(measures, function(i, item){
                var measureName = "<option>"  + item.label + "</option>";
                $('#measureSelect').append(measureName);
            });
        }
    });
});



//load dimension tables on measure change
$("document").ready(function(){
    $('#measureSelect').change(function(){
        var selectedIndex = $(this).prop('selectedIndex');
        var selectedMeasure = $(this).val();
        var cName = $('#cubeSelect').val();

        $('#graphCollection').empty();
        $('#dimensionCollection').empty();
        if(selectedIndex > 0)
        {
            $.ajax({
                url:"ExploreCube",
                data:
                {
                    cubeName: cName,
                    measure: selectedMeasure
                },
                success: function(data)
                {
                    $('#dimensionCollection').append(data);
                    $.each($('#dimensionCollection table'), function(i, item){
                        $('#' + item.id).treeTable();
                        $('#' + item.id + ' tbody tr').on('click', function() {
                            var val = $(this).find("td:eq(0)").text();
                            var table = item.id.substring(0,item.id.indexOf('-'));
                            var cube = getCubeByName(cName);
                            var dimes = cube.children[0];
                            $.each(dimes.children, function(i, curr){
                                if(curr.label.indexOf(table.toUpperCase()) >= 0)
                                {
                                    if(val.indexOf(curr.children[0].label) >= 0)
                                    {
                                        $.ajax({
                                            url:"GenerateGraph",
                                            data:
                                            {
                                                cubeName: cName,
                                                dimension: table,
                                                measure: selectedMeasure,
                                                lookup: val.substring(val.indexOf("=") + 1)
                                            },
                                            success: function(results)
                                            {
                                                var x = [];
                                                var y = [];
                                                for(var i = 0; i < results[0].length; i++)
                                                {
                                                    y.push(parseFloat(results[1][i]));
                                                }
                                                var chart = new Highcharts.Chart({
                                                    chart: {
                                                        renderTo: 'graphCollection',
                                                        defaultSeriesType: 'column'
                                                    },
                                                    title: {
                                                        text: val
                                                    },
                                                    plotOptions: {
                                                        column: {
                                                            pointPadding: 0.2,
                                                            borderWidth: 0
                                                        }
                                                    },
                                                    xAxis: {
                                                        categories: results[0]
                                                    },
                                                    yAxis: {
                                                        min: 0,
                                                        title: {
                                                            text: selectedMeasure
                                                        }
                                                    },
                                                    series: [{
                                                        name: curr.children[0].children[0].label,
                                                        data: y
                                                    }]
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                            
                        });
                    });
                },
                error: function(xhr)
                {
                    alert("WOMP");
                    alert(xhr.statusText);
                }
            });
        }
    });
});

//get cube data based on name given
function getCubeByName(name)
{
    var cube;
    $.each(dbCubes.children, function (i ,item){
        if(item.label == name)
            cube = item;
    });
    return cube;
}

//get cube measures from cube name
function getMeasuresFromCube(cube)
{
    var measures = [];
    $.each(cube.children[1].children, function(i, meas) {
        measures.push(meas);
    });
    return measures;
}
