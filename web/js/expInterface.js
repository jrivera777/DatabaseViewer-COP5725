var dbCubes;
var defltCube = "<option>Select Cube...</option>";
var defltMeasure = "<option>Select Measure...</option>";


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
        var cName = $('#cubeSelect').val();
        $('#dimensionCollection').empty();
        if(selectedIndex > 0)
        {
            $.ajax({
                url:"ExploreCube",
                data:
                {
                    cubeName: cName
                },
                success: function()
                {
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

//set up dimension table
$("document").ready(function()  {
    $("#dimensionTable").treeTable();
});