<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
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
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Thanks page</title>
    <style><%@include file="/WEB-INF/css/styles.css"%></style>
    <style>
        * {
            margin: 0;
            padding: 0;
        }
        body{
            background: white;

        }
        body::before{
            opacity: .4;
        }

        .container1 {
            display: block;
            margin: 50px auto 100px;

            min-height: 200px;
            min-width: 500px;
            width: 800px;
            height: 400px;
            align-items: center;
            position: relative;
            font-size: 30px;

            font-family: "Book Antiqua";

        }

        .row1 {
            margin-top: 50px;
            padding: 30px;
            text-align: center;
            border: 3px solid crimson;
            margin-right: 100px;
            margin-left: 100px;
        }

        .item {
            padding: 3px;
            font-size: 20px;
        }

        .link {
            color: crimson;
            text-decoration: none;
        }

        .title-gratitude {
            color: midnightblue;
            padding-bottom: 10px;
        }
    </style>
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
                <a href="/MyOrdersController" class="nav-item nav-link"><fmt:message key="thanks.my_orders"/></a>
            </div>
            <div class="nav-item dropdown ml-auto">
                <a class="nav-link dropdown-toggle" href="" id="dropdown09" data-toggle="dropdown" aria-haspopup="true"
                   aria-expanded="false" style="color:black;">
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
<div class="container1">
    <div class="row1">
        <h3 class="title-gratitude">
            <fmt:message key="thanks.message.gratitude"/>
        </h3>
        <div class="item">
            <span><fmt:message key="thanks.your_order_number_is"/> <a class="link" href="#">${orderId}</a> </span>
        </div>
        <div style="padding-bottom: 20px;" class="item">
            <span><fmt:message key="thanks.will_be_delivered"/> </span>
        </div>
        <div class="item">
            <span><fmt:message key="thanks.email_us"/> <a class="link" href="#">contact@dessertcafe.com.ua</a> <fmt:message key="thanks.for_help"/></span>
        </div>
        <div class="item">
            <span><fmt:message key="thanks.message.contact"/></span>
        </div>
        <div class="item">
            <span><fmt:message key="thanks.message.track"/></span>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
