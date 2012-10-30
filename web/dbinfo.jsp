<%-- 
    Document   : dbinfo
    Created on : Oct 21, 2012, 5:44:07 PM
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
        <script src="http://code.jquery.com/jquery-1.8.2.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/tree.jquery.js"></script>
        <title>Database Information</title>

        <script>
            $(document).ready(function(){
                $('#body').tree();
            });
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row-fluid">
                <div id="body" class="span10 offset1" data-url="DBInfo"></div>
            </div>
        </div>
    </div>
</body>
</html>
