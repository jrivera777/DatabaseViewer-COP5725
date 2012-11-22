//object holds table data
var dbData;
var dimensions = [];
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
                
                var succ = data.indexOf("alert") < 0 ? 'btn-success' : 'btn-danger';

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

//create dimension button
$("document").ready(function() {
    $('#createDime').click(function() {
        $('#dimensions').find("option").attr("selected", false);
        disableSelection(true);

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
                "data": []
            });

            $('#dimensions').append("<option>" + name + "</option>")
            $('#dimeName').val('');
            $('#dimension_add_cntrls').removeClass('error')
        }
        else
            $('#dimension_add_cntrls').addClass('error')
        btn.button('reset');
    });
});

//textbox error clearing
$("document").ready(function() {
    $('input').focus(function() {
        $('#dimension_add_cntrls').removeClass('error')
        disableSelection(true);
        $('#dimensions').find("option").attr("selected", false);
    });
});

//Table selection and column loading
$("document").ready(function(){
    $('#tableSelect').change(function(){
        var index = $(this).prop('selectedIndex');
        var chosen = $(this).val();
        $('#columns').empty();
        if(index > 0)
        {
            var tables = dbData.children
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
    });
});

//dimension selection
$("document").ready(function() {
    $('#dimensions').change(function(){
        var selected = $(this).val();
        if(selected !== null)
            disableSelection(false);
        else
            disableSelection(true);
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
                dimensions[selectedDimensionIndex].data.push({
                    "tableName": selectedTable,
                    "data": [ selectedVal ]
                });

                $('#granularity').append("<option>" + selectedVal + "</option>")
                applySuccFail(btn, 'col-arr', true);
            }
            else
            {
                applySuccFail(btn, 'col-arr', false);
            }
        }
    });
});

//Order granularity buttons
$("document").ready(function() {
    $('.up-down').click(function() {
        var id = this.id;
        var btn = $(this);
        moveItem(btn, btn.children()[0].id);
    });
});

function moveItem(btn, direction)
{
    var selected = $('#granularity option:selected');
    var failed = selected.length == 0;
    if(direction.indexOf("up") > 0)
    {
        if(selected.prev().length == 0)
            failed = true;
        else
        {
            selected.each(function(){
                $(this).insertBefore($(this).prev());
                failed = false;
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
                $(this).insertAfter($(this).next());
            });
        }
    }
    applySuccFail(btn, direction, !failed);
}

// Parameters: string  - button to manipulate
//             boolean - true to apply success color.  Failure color otherwise.
function applySuccFail(btn, arr, succ)
{
    var css = succ ? 'btn-success' : 'btn-danger';
    $('#' + arr).addClass('icon-white')
    btn.addClass(css).delay(1000).queue(function(next){
        btn.removeClass(css)
        $('#' + arr).removeClass('icon-white')
        next();
    });
}

function findIndexByKeyValue(obj, key, value)
{
    for (var i = 0; i < obj.length; i++) {
        if (obj[i][key] == value) {
            return i;
        }
    }
    return -1;
}

//handle disabling some selection controls
function disableSelection(val)
{
    if (val)
    {
        $('#tableSelect').find("option").attr("selected", false);
        $('#columns').empty();
        $('#granularity').empty();
        $('#tableSelect').attr('disabled', 'disabled');
        $('#columns').attr('disabled', 'disabled');
        $('#addColumn').attr('disabled', 'disabled');
        $('#granularity').attr('disabled', 'disabled');
        $('#moveGranUp').attr('disabled', 'disabled');
        $('#moveGranDown').attr('disabled', 'disabled');
    }
    else
    {
        $('#tableSelect').removeAttr('disabled');
        $('#columns').removeAttr('disabled');
        $('#addColumn').removeAttr('disabled');
        $('#granularity').removeAttr('disabled');
        $('#moveGranUp').removeAttr('disabled');
        $('#moveGranDown').removeAttr('disabled');
    }
}