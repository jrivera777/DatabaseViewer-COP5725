<%-- 
    Document   : Error
    Created on : Nov 7, 2012, 4:45:28 PM
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
        <title>Oops!</title>
    </head>
    <body>

        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span10 offset1 well">
                    <p>Sorry for the inconvenience, but something has gone wrong.
                        <a href="index.jsp">Click Here</a> to go back to the Login Page.
                    </p>
                    <div id="results">
                        <%
                            String err = (String) request.getAttribute("errorMessage");
                           out.println(err);
                        %>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
