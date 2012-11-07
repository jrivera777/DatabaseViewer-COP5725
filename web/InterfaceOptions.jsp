<%-- 
    Document   : InterfaceOptions
    Created on : Oct 22, 2012, 5:58:26 PM
    Author     : Joseph
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" type="text/css" rel="stylesheet"></link>
        <link href="css/style.css" type="text/css" rel="stylesheet"></link>
        <link href="css/jqtree.css" type="text/css" rel="stylesheet"></link>
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" />
        <script src="http://code.jquery.com/jquery-1.8.2.js"></script>
        <script src="http://code.jquery.com/ui/1.9.1/jquery-ui.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/tree.jquery.js"></script>
        <script>
            $(function() {
                $( "#tabs" ).tabs();
            });
        </script>
        <script>
            $("document").ready(function() {
                $('#execute').click(function() {
                    var exec = $('#query-input').val();
                    $.ajax({
                        url:"RunQuery",
                        data: {query: exec},
                        success: function(data){
                            $('#results').empty();
                            $('#results').append(data);
                        }
                    });
                });
            });
        </script>
    </head>
    <body>
        <!-- container for the existing markup tabs -->
        <div id="tabs">
            <ul>
                <li><a href="#construct">Construct Cubes</a></li>
                <li><a href="#explore">Explore Cubes</a></li>
            </ul>
            <div id="construct">
                <div class="controls">
                    <button id="execute" type="submit" class="btn btn-primary">Execute Query</button>
                    <textarea name="query" id="query-input"rows="6"></textarea>
                </div>
                <div id="results" class="table-container">
                </div>
            </div>
            <div id="explore">
            </div>
        </div>
    </body>
</html>
