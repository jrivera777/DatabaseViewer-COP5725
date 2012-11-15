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
                btn.addClass(succ).delay(2000).queue(function(next){
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
                "data": null
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
        if(index <= 0)
            $('#columns').empty();
        else
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
        var index = $(this).prop('selectedIndex');
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
        var selected = $('#columns').prop('selectedIndex');
        if(selected < 0)
        {
            $('#col-arr').addClass('icon-white')
            btn.addClass('btn-danger ').delay(2000).queue(function(next){
                btn.removeClass('btn-danger')
                $('#col-arr').removeClass('icon-white')
                next();
            });
        }
        else
        {
            
        }
    });
});

//handle disabling some selection controls
function disableSelection(val)
{
    if (val)
    {
        $('#tableSelect').find("option").attr("selected", false);
        $('#columns').empty();
        $('#tableSelect').attr('disabled', 'disabled');
        $('#columns').attr('disabled', 'disabled');
        $('#addColumn').attr('disabled', 'disabled');
    }
    else
    {
        $('#tableSelect').removeAttr('disabled');
        $('#columns').removeAttr('disabled');
        $('#addColumn').removeAttr('disabled');
    }
}