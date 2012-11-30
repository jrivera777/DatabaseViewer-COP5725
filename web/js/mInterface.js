//object holds table data
var dbData;
var dimensions = [];
var measures = [];
//setup tabs
$("document").ready(function() {
    $(function() {
        $( "#tabs" ).tabs();
    });
});

//load table selection dropdown
$("document").ready(function(){
    var deflt = "<option>Select Table or View...</option>";
    $('#tableSelect').append(deflt);
    $.get('DBInfo', function(data){
        dbData = data[0];
        var tables = dbData.children
        $.each(tables, function(i, vals){
            $.each(vals.children, function(i, item){
                var tableName = "<option>" + item.label + "</option>";
                $('#tableSelect').append(tableName);
            });
        });
    });
});

//clear button
$("document").ready(function(){
    $('#clearResults').click(function(){
        $('#results').empty();
    });
});

//execute query button
$("document").ready(function() {
    $('#execute').click(function() {
        var btn = $(this)
        btn.button('loading')
        var exec = $('#query-input').val();
        $.ajax({
            url:"RunQuery",
            data:
            {
                query: exec
            },
            success: function(data)
            {
                $('#results').empty();
                $('#results').append(data);
                $('#results').css("margin-bottom", "10px");
                
                var succ = data.indexOf("alert-error") < 0 ? 'btn-success' : 'btn-danger';

                btn.button('reset');
                btn.removeClass('btn-primary');
                btn.addClass(succ).delay(1000).queue(function(next){
                    btn.removeClass(succ)
                    btn.addClass('btn-primary');
                    next();
                });
            }
        });
    });
});


//Save Cube button
$("document").ready(function() {
    $('#saveCube').click(function() {
        var btn = $(this)
        var cubeName = $('#cubeName').val();
        var table = $('#tableSelect').val();
        if(cubeName == "")
        {
            $('#results').empty();
            $('#results').append("<div class=\"alert alert-error\">\n\
              <button type=\"button\" class=\"close\"data-dismiss=\"alert\">\n\
              ×</button>Cube must have a name.</div>")
            return;
        }
        if($('#tableSelect').prop('selectedIndex') <= 0)
        {
            $('#results').empty();
            $('#results').append("<div class=\"alert alert-error\">\n\
              <button type=\"button\" class=\"close\"data-dismiss=\"alert\">\n\
              ×</button>Cube must refer to a table.</div>");
            return;
        }
        if(dimensions.length < 1 && measures.length < 1)
        {
            $('#results').empty();
            $('#results').append("<div class=\"alert alert-error\">\n\
              <button type=\"button\" class=\"close\"data-dismiss=\"alert\">\n\
              ×</button>Cube must have at least one dimension or measure.</div>");
            return;
        }
        for(var i = 0; i < dimensions.length; i++)
        {
            if(jQuery.isEmptyObject(dimensions[i].data))
            {
                $('#results').empty();
                $('#results').append("<div class=\"alert alert-error\">\n\
              <button type=\"button\" class=\"close\"data-dismiss=\"alert\">\n\
              ×</button>A dimension must have at least one granularity.</div>");
                return;
            }
        }

        btn.button('loading');
        //printDimensions();
        $.ajax({
            url:"SaveCube",
            traditional: true,
            data:
            {
                name: cubeName,
                tableName: table,
                dimes: JSON.stringify(dimensions),
                msrs: JSON.stringify(measures)
            },
            success: function(data)
            {
                var succ = data.indexOf("alert-error") < 0 ? 'btn-success' : 'btn-danger';
                $('#results').empty();
                $('#results').append(data);

                btn.button('reset');
                btn.removeClass('btn-primary');
                btn.addClass(succ).delay(500).queue(function(next){
                    btn.removeClass(succ)
                    btn.addClass('btn-primary');
                    next();
                });
                if(succ.indexOf("success") > 0)
                    parent.location.reload();
            }
        });
        btn.button('reset');
    });
});

//Clear Cube button
$("document").ready(function() {
    $('#clearCube').click(function() {
        dimensions.length = 0;
        measures.length = 0;
        $('#tableSelect').removeAttr('disabled');
        $('#dimensions').empty();
        $('#granularity').empty();
        $('#cubeName').val('');
        $('#tableSelect').find("option").attr("selected", false);
        $('#measureCols').empty();
        $('#columns').empty();
        $('#measures').empty();
        $('#aggrs').find("option").attr("selected", false);
        disableDimensionSelection(true);
        disableMeasureSelection(true);

    });
});

//create dimension button
$("document").ready(function() {
    $('#createDime').click(function() {
        $('#dimensions').find("option").attr("selected", false);
        disableDimensionSelection(true);
        var disableTableSelection = false;
        var btn = $(this)
        btn.button('loading')
        var name = $('#dimeName').val();
        var exists = false;
        $('#dimensions option').each(function(){
            if (this.value == name)
            {
                exists = true;
                return false;
            }
        });
        if(!exists && name !== "")
        {
            dimensions.push({
                "name": name,
                "data": {}
            });
            if(dimensions.length > 0)
                disableTableSelection = true;
            $('#dimensions').append("<option>" + name + "</option>")
            $('#dimeName').val('');
            $('#dimension_add_cntrls').removeClass('error')
        }
        else
            $('#dimension_add_cntrls').addClass('error')
        if(disableTableSelection)
            $('#tableSelect').attr('disabled', 'disabled');
        else
            $('#tableSelect').removeAttr('disabled');
        btn.button('reset');
    });
});

//textbox error clearing
$("document").ready(function() {
    $('input').focus(function() {
        $('#dimension_add_cntrls').removeClass('error')
        $('#granularity').empty();
        $('#dimensions').find("option").attr("selected", false);
        disableDimensionSelection(true);
        $('#aggrs').find("option").attr("selected", false);
    });
});

//Table selection and column loading
$("document").ready(function(){
    $('#tableSelect').change(function(){
        var index = $(this).prop('selectedIndex');
        var chosen = $(this).val();
        var selector = $(this).attr("id");
        $('#dimensions').find("option").attr("selected", false);
        disableMeasureSelection(true);
        disableDimensionSelection(true);
        $('#columns').empty();
        $('#granularity').empty();
        $('#measureCols').empty();
        if(index > 0)
        {
            disableMeasureSelection(false);
            var tables = dbData.children;
            //load column options
            $.each(tables, function(i, vals){
                $.each(vals.children, function(i, item){
                    if(item.dataName === chosen)
                    {
                        var colName;
                        $.each(item.children, function(i, cols){
                            colName = "<option>" + cols.dataName + "</option>";
                            $('#columns').append(colName);
                            $('#measureCols').append(colName);
                        });
                    }
                });
            });
        }
    });
});

//dimension selection
$("document").ready(function() {
    $('#dimensions').change(function(){
        $('#columns').empty();
        $('#granularity').empty();
        var selected = $(this).val();
        var selectedDimensionIndex = $('#dimensions').prop('selectedIndex');
        if(selected !== null)
        {
            var chosen = $('#tableSelect').val();
            disableDimensionSelection(false);
            $('#tableSelect').val(chosen);

            //load previously selected granularity options
            if(!jQuery.isEmptyObject(dimensions[selectedDimensionIndex].data))
                for(var i = 0; i < dimensions[selectedDimensionIndex].data.data.length; i++)
                    $('#granularity').append("<option>" + dimensions[selectedDimensionIndex].data.data[i] + "</option>");

            //load column options
            var tables = dbData.children;
            $.each(tables, function(i, vals){
                $.each(vals.children, function(i, item){
                    if(item.dataName === chosen)
                    {
                        var colName;
                        $.each(item.children, function(i, cols){
                            colName = "<option>" + cols.dataName + "</option>";
                            $('#columns').append(colName);
                        });
                    }
                });
            });
        }
        else
            disableDimensionSelection(true);
    });
});

//aggregate selection
$("document").ready(function() {
    $('#aggrs').change(function(){
        var selected = $(this).val();
        var selectedDimensionIndex = $('#aggrs').prop('selectedIndex');
        if(selected !== null)
            disableMeasureSelection(false);
        else
            disableMeasureSelection(true);
    });
});

//add granularity button
$("document").ready(function() {
    $('#addColumn').click(function() {
        var btn = $(this)
        var selectedDimensionIndex = $('#dimensions').prop('selectedIndex');
        var selectedTable = $('#tableSelect').val();
        var selecteColIndex = $('#columns').prop('selectedIndex');
        var selectedVal = $('#columns').val();
        var exists = false;
        if(selecteColIndex < 0)
            applySuccFail(btn, 'col-arr', false);
        else
        {
            $('#granularity option').each(function(){
                if (this.value == selectedVal)
                {
                    exists = true;
                    return false;
                }
            });
            if(!exists && selectedVal !== "")
            {
                if(jQuery.isEmptyObject(dimensions[selectedDimensionIndex].data) || dimensions[selectedDimensionIndex].data.name != selectedTable)
                {
                    dimensions[selectedDimensionIndex].data = {
                        "name": selectedTable,
                        "data": [ selectedVal ]
                    };
                }
                else
                    dimensions[selectedDimensionIndex].data.data.push(selectedVal);
                $('#granularity').append("<option>" + selectedVal + "</option>")
                applySuccFail(btn, 'col-arr', true);
            }
            else
                applySuccFail(btn, 'col-arr', false);
        }
    });
});

//add measure button
$("document").ready(function() {
    $('#addMeasure').click(function() {
        var btn = $(this);
        var table = $('#tableSelect').val();
        var col = $('#measureCols').val();
        var exists = false;
        var disableTable = false;
        if(col == null)
        {
            applySuccFail(btn, 'm-arr', false);
            return;
        }
        var measureVal = [ $('#aggrs').val(),"(", col,")"].join("");
        $('#measures option').each(function(){
            if (this.value.indexOf(measureVal) >= 0 && this.value.indexOf(table) >= 0)
            {
                exists = true;
                return false;
            }
        });
        if(exists)
            applySuccFail(btn, 'm-arr', false);
        else
        {
            var aggrType = $('#aggrs').val();
            var measureVal = [ aggrType,"(", col,")"].join("");
            measures.push(
            {
                type: aggrType,
                columnname: col
            });
            if(measures.length > 0)
                disableTable = true;
            if(disableTable)
                $('#tableSelect').attr('disabled', 'disabled');
            else
                $('#tableSelect').removeAttr('disabled');
            $('#measures').append("<option>" + measureVal + " on table &lt;" + table + "&gt;</option>");
            applySuccFail(btn, 'm-arr', true);
        }
    });
});

//Order granularity buttons
$("document").ready(function() {
    $('.up-down').click(function() {
        var btn = $(this);
        moveItem(btn, btn.children()[0].id);
    });
});

function moveItem(btn, direction)
{
    var selectedDimensionIndex = $('#dimensions').prop('selectedIndex');
    var selectedCol = $('#tableSelect').val();
    var selected = $('#granularity option:selected');
    var selectedIndex = $('#granularity').prop('selectedIndex');
    var failed = selected.length == 0;
    //var index = findIndexByKeyValue($(dimensions, key, selected.val()));
    if(direction.indexOf("up") > 0)
    {
        if(selected.prev().length == 0)
            failed = true;
        else
        {
            selected.each(function(){
                var toSwap = dimensions[selectedDimensionIndex].data.data.splice(selectedIndex, 1);
                dimensions[selectedDimensionIndex].data.data.splice(selectedIndex-1, 0, toSwap[0]);
                $(this).insertBefore($(this).prev());
            });
        }
    }
    else
    {
        if(selected.next().length == 0)
            failed = true;
        else
        {
            selected.each(function(){
                var toSwap = dimensions[selectedDimensionIndex].data.data.splice(selectedIndex, 1);
                dimensions[selectedDimensionIndex].data.data.splice(selectedIndex+1, 0, toSwap[0]);
                $(this).insertAfter($(this).next());
            });
        }
    }
    applySuccFail(btn, direction, !failed);
}

// Parameters: string  - button to manipulate
//             string  - name of img to manipulate
//             boolean - true to apply success color.  Failure color otherwise.
function applySuccFail(btn, arr, succ)
{
    var css = succ ? 'btn-success' : 'btn-danger';
    $('#' + arr).addClass('icon-white');
    btn.addClass(css);
    window.setTimeout(function(){
        btn.removeClass(css)
        $('#' + arr).removeClass('icon-white')
    }, 500);
}

function findIndexByKeyValue(obj, key, value)
{
    for (var i = 0; i < obj.length; i++) {
        if(key == "")
        {
            if (obj[i] == value) {
                return i;
            }
        }
        else
        {
            if (obj[i][key] == value) {
                return i;
            }
        }
    }
    return -1;
}

//handle disabling some selection controls
function disableDimensionSelection(val)
{
    if (val)
    {
        $('#columns').attr('disabled', 'disabled');
        $('#addColumn').attr('disabled', 'disabled');
        $('#granularity').attr('disabled', 'disabled');
        $('#moveGranUp').attr('disabled', 'disabled');
        $('#moveGranDown').attr('disabled', 'disabled');
    }
    else
    {
        $('#columns').removeAttr('disabled');
        $('#addColumn').removeAttr('disabled');
        $('#granularity').removeAttr('disabled');
        $('#moveGranUp').removeAttr('disabled');
        $('#moveGranDown').removeAttr('disabled');
    }
}
function disableMeasureSelection(val)
{
    if (val)
    {
        $('#addMeasure').attr('disabled', 'disabled');
    }
    else
    {
        $('#addMeasure').removeAttr('disabled');
    }
}

function printDimensions()
{
    var message = [];
    message.push("Cube: " + $('#cubeName').val() + "\n")
    for(var i = 0; i < dimensions.length; i++)
    {
        var table = dimensions[i].data;
        if(!jQuery.isEmptyObject(table))
        {
            message.push("\tDimension: " + dimensions[i].name + " on Table: " + dimensions[i].data.name + "\n");
            for(var k = 0; k < dimensions[i].data.data.length; k++)
                message.push("\t\t\tGranularity: " + dimensions[i].data.data[k] + "\n");
        }
    }
    alert(message.join(""));
}