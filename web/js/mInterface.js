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
        var tables = data[0].children
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

//exectue query button
$("document").ready(function() {
    $('#execute').click(function() {
        var btn = $(this)
        btn.button('loading')
        var exec = $('#query-input').val();
        $.ajax({
            url:"RunQuery",
            data: {
                query: exec
            },
            success: function(data){
                $('#results').empty();
                $('#results').append(data);
                $('#results').css("margin-bottom", "10px");
                
                var succ;
                if(data.indexOf("alert") < 0)
                    succ = 'btn-success';
                else
                    succ = 'btn-danger';

                btn.button('reset');
                btn.removeClass('btn-primary');
                btn.addClass(succ).delay(3000).queue(function(next){
                    $(this).removeClass(succ)
                    $(this).addClass('btn-primary');
                    next();
                });
            }
        });
    });
});