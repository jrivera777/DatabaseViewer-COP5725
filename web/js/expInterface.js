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
        if(selectedIndex > 0)
        {
            var cube = $(this).val();
            var measures = getMeasuresFromCube(cube);
            $.each(measures, function(i, item){
                var measureName = "<option>"  + item.label + "</option>";
                $('#measureSelect').append(measureName);
            });
        }
    });
});


//get cube measures from cube name
function getMeasuresFromCube(name)
{
    var measures = [];
    $.each(dbCubes.children, function (i ,item){
        if(item.label == name)
        {
            $.each(item.children[1].children, function(i, meas) {
                measures.push(meas);
            });
        }
    });
    return measures;
}
