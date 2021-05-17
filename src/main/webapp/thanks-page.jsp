<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<%@ page session="true" %>


<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.lang}">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.1.0/css/flag-icon.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <title>Thanks page</title>
    <style><%@include file="/WEB-INF/css/styles.css"%></style>
</head>
<body>
<div class="bs-example">
    <nav class="navbar navbar-expand-md navbar-light bg-light">
        <a href="#" class="navbar-brand">
            <img width="70" height="70"
                 src="https://image.similarpng.com/thumbnail/2020/06/Restaurant-logo-with-chef-drawing-template-on-transparent-background-PNG.png"
                 height="28" alt="5 Min Cafe">
        </a>
        <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarCollapse">
            <div class="navbar-nav">
                <a href="FoodItemController" class="nav-item nav-link"><fmt:message key="thanks.menu"/></a>
                <a href="cart.jsp" class="nav-item nav-link"><fmt:message key="thanks.cart"/></a>
                <a href="my-orders.jsp" class="nav-item nav-link"><fmt:message key="thanks.my_orders"/></a>
            </div>
            <div class="nav-item dropdown ml-auto">
                <a class="nav-link dropdown-toggle" href="" id="dropdown09" data-toggle="dropdown" aria-haspopup="true"
                   aria-expanded="false">
                    <span class="flag-icon flag-icon-${sessionScope.lang}"> </span> <fmt:message key="thanks.language"/></a>
                <div class="dropdown-menu" aria-labelledby="dropdown09">
                    <a class="dropdown-item" href="/CartController?sessionLocale=ua"><span
                            class="flag-icon flag-icon-ua"> </span> <fmt:message key="thanks.ukrainian"/></a>
                    <a class="dropdown-item" href="/CartController?sessionLocale=en"><span
                            class="flag-icon flag-icon-us"> </span> <fmt:message key="thanks.english"/></a>
                </div>
            </div>
            <a href="cart.jsp">
            <span class="fa-stack fa-2x has-badge" data-count="${sessionScope.cart.size()}">
                    <i class="fa fa-circle fa-stack-2x fa-inverse"></i>
                    <i style="" class="fa fa-shopping-cart fa-stack-2x red-cart"></i>
                </span>
            </a>
        </div>
    </nav>
</div>
<div id="header">
    <h2>Gratitude</h2>
</div>
<div id="container">
    <h4><fmt:message key="thanks.message.first_line"/></h4>
    <h4><fmt:message key="thanks.message.contact"/></h4>
    <h4><fmt:message key="thanks.message.track"/></h4>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
