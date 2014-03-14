<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!doctype html>
<html lang="en">

    <head>
        <link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css" rel="stylesheet">
        <script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>
        <style>
            <%@ include file="apidoc.css" %>
        </style>
    </head>

    <body>

        <div class="container">

            <div class="row">
                <div class="span4">
                    <%@ include file="_menu.jsp" %>
                </div>
                <div class="span8">
                    <%@ include file="_body.jsp" %>
                </div>
            </div>

        </div>
    </body>

</html>