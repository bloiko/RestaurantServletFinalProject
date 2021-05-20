<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="mypredefinedtaglibrary" prefix="my" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<%@ page session="true" %>
<!DOCTYPE html>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.lang}">
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.1.0/css/flag-icon.min.css" rel="stylesheet">
    <%--<link rel="stylesheet" type="text/css" href="/WEB-INF/css/styles.css"/>--%>
    <title>Menu</title>
    <style>
        <%@include file="/WEB-INF/css/styles.css" %>
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
                <a href="FoodItemController" class="nav-item nav-link active"><fmt:message key="list_food.menu"/></a>
                <c:if test="${sessionScope.get('username')!=null}">
                    <a href="cart.jsp" class="nav-item nav-link"><fmt:message key="list_food.cart"/></a>
                    <a href="/MyOrdersController" class="nav-item nav-link"><fmt:message key="list_food.my_orders"/></a>
                </c:if>
            </div>
            <div class="nav-item dropdown ml-auto">
                <a class="nav-link dropdown-toggle" href="" id="dropdown09" style="color:black;" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    <span class="flag-icon flag-icon-${sessionScope.lang}"> </span> <fmt:message
                        key="list_food.language"/></a>
                <div class="dropdown-menu" aria-labelledby="dropdown09">
                    <a class="dropdown-item" href="/FoodItemController?sessionLocale=ua"><span
                            class="flag-icon flag-icon-ua"> </span> <fmt:message key="list_food.ukrainian"/></a>
                    <a class="dropdown-item" href="/FoodItemController?sessionLocale=en"><span
                            class="flag-icon flag-icon-us"> </span><fmt:message key="list_food.english"/></a>
                </div>
            </div>
            <c:if test="${sessionScope.get('username')==null}">
                <div class="navbar-nav">
                    <a href="/RegistrationController" class="nav-item nav-link">Registration</a>
                    <a href="login-main.jsp" class="nav-item nav-link">Login</a>
                </div>
            </c:if>
            <c:if test="${sessionScope.get('username')!=null}">
                <div class="navbar-nav">
                    <a href="/LogoutController" class="nav-item nav-link">Logout</a>
                </div>
            <a href="cart.jsp">
            <span class="fa-stack fa-2x has-badge" data-count="${sessionScope.cart.size()}">
                    <i class="fa fa-circle fa-stack-2x fa-inverse"></i>
                    <i style="" class="fa fa-shopping-cart fa-stack-2x red-cart"></i>
                </span>
            </a>
            </c:if>
        </div>
    </nav>
</div>
<div id="wrapper">
    <div id="header">
        <h2><fmt:message key="list_food.menu"/></h2>
    </div>
</div>
<div id="container">
    <div id="content">

        <a href="/FoodItemController?filter=all_categories">
            <button type="button" class="btn btn-light">All categories</button>
        </a>
        <c:forEach var="category" items="${categories}">
            <a href="/FoodItemController?filter=${category.name}">
                <button type="button" class="btn btn-light">${category.name}</button>
            </a>
        </c:forEach>

        <table class="table">
            <thead>
            <tr>
                <th scope="col"><fmt:message key="list_food.image"/></th>
                <th scope="col"><fmt:message key="list_food.name"/><a href="/FoodItemController?sort=name"
                                                                      style="color:red;">&#8597;</a></th>
                <th scope="col"><fmt:message key="list_food.price"/><a href="/FoodItemController?sort=price"
                                                                       style="color:red;">&#8597;</a></th>
                <th scope="col"><fmt:message key="list_food.category"/><a href="/FoodItemController?sort=category"
                                                                          style="color:red;">&#8597;</a></th>
                <c:if test="${sessionScope.get('username')!=null}">
                    <th scope="col"></th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="foodItem" items="${FOOD_LIST}">
                <tr>
                    <td><img src="${foodItem.image}" width="100" height="100"></td>
                    <td> ${foodItem.name} </td>
                    <td> ${foodItem.price}$</td>
                    <td>${foodItem.category.name}</td>
                    <c:if test="${sessionScope.get('username')!=null}">
                        <td><a href="/FoodItemController?command=ORDER&foodId=${foodItem.id}" style="color:black;">
                            <button type="button" class="btn btn-dark"><fmt:message
                                    key="list_food.add_to_cart"/></button>
                        </a>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination_section">
            <c:set var="counter" value="1"/>
            <c:set var="end" value="${requestScope.numberOfPages}"/>
            <my:looping start="1" end="${end}">
                <a href="FoodItemController?page=${counter}">${counter}</a>
                <c:set var="counter" value="${counter+1}"/>
            </my:looping>
        </div>

    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>








