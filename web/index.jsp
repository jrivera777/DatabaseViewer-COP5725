<%-- 
    Document   : index
    Created on : Oct 17, 2012, 9:35:17 PM
    Author     : Joseph
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" type="text/css" rel="stylesheet"></link>
        <link href="css/style.css" type="text/css" rel="stylesheet"></link>
        <script src="http://code.jquery.com/jquery-1.8.2.js"></script>
        <script src="js/bootstrap.js"></script>
        <title>COP5725 Database Viewer</title>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span8 offset2 well">
                    <form class="form-horizontal" method="get" action="UserLogin">
                        <div class="control-group">
                            <label class="control-label" for="dbName">Database Name</label>
                            <div class="controls">
                                <input id="dbName" type="text" name="dbName" placeholder="Database Name">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="dbAddress">Database Address</label>
                            <div class="controls">
                                <input id="dbAddress" type="text" name="dbAddress" placeholder="Address">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="dbUser">User</label>
                            <div class="controls">
                                <input id="dbUser" type="text" name="dbUser" placeholder="username">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="dbPW">Password</label>
                            <div class="controls">
                                <input id="dbPW" type="password" name="dbPW" placeholder="password">
                            </div>
                        </div>
                        <div class="control-group">
                            <div class="controls">
                                <button id="login" type="submit" class="btn btn-primary">Log in</button>
                            </div>
                        </div>
                    </form>
                </div>
                <div id="results">
                </div>
            </div>
        </div>
    </body>
</html> 

