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
                    var btn = $(this)
                    btn.button('loading')
                    var exec = $('#query-input').val();
                    $.ajax({
                        url:"RunQuery",
                        data: {query: exec},
                        success: function(data){
                            $('#results').empty();
                            $('#results').append(data);
                            btn.button('reset')
                        }
                    });
                });
            });
        </script>
        <script>
            $("document").ready(function(){
                $('#clearResults').click(function(){
                    $('#results').empty();
                });
            });
        </script>
    </head>
    <body>
        <!-- container for the existing markup tabs -->
        <div class="row-fluid">
            <div class="span12">
                <form method="get" action="UserLogout" target="_top">
                    <p class="logout"><button class="btn btn-small btn-info" type="submit"><i class="icon-off icon-white"></i> Log Out</button></p>
                </form>
                <div id="tabs">
                    <ul>
                        <li><a href="#construct">Construct Cubes</a></li>
                        <li><a href="#explore">Explore Cubes</a></li>
                    </ul>
                    <div id="construct">
                        <div class="row-fluid">
                            <div class="span12">
                                <button id="execute" type="button" class="btn btn-primary" data-loading-text="Executing...">Execute Query</button>
                                <button id="clearResults" type="submit" class="btn">Clear Results</button>
                                <textarea name="query" id="query-input" class="boxsizingBorder" rows="6" style="resize: none;"></textarea>
                                <div id="results" class="table-container">
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid">
                            <div class="span12 form-inline center-text well well-small">
                                <label class="control-label" for="cubeName">Cube Name:</label>
                                <input id="cubeName" class="input-xxlarge" type="text" placeholder="New Cube"></input>
                            </div>
                        </div>
                        <div class="row-fluid">
                            <div class="span4 form-inline well">
                                <label class="control-label" for="dimensionName">Dimension Name:</label>
                                <input id="cubeName" class="input-medium" type="text" placeholder="Dimension_1"></input>
                            </div>
                            <div class="span4 form-inline well">

                            </div>
                            <div class="span4 form-inline well">

                            </div>
                        </div>
                    </div>
                    <div id="explore">
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
