<%-- 
    Document   : MainInterface
    Created on : Oct 22, 2012, 5:56:03 PM
    Author     : Joseph
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Data Explorer</title>
        <link href="css/bootstrap.css" type="text/css" rel="stylesheet"></link>
        <link href="css/style.css" type="text/css" rel="stylesheet"></link>
        <link href="css/jqtree.css" type="text/css" rel="stylesheet"></link>
        <script src="http://code.jquery.com/jquery-1.8.2.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/tree.jquery.js"></script>
    </head>
    <frameset cols="17%, *">
        <frame src="dbinfo.jsp" />
        <frame src="InterfaceOptions.jsp" />
    </frameset>
</html>
