<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib uri="mypredefinedtaglibrary" prefix="my" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<%@ page session="true" %>
<!DOCTYPE html>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

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

    <title>Menu</title>
</head>
<style>
    .fa-stack[data-count]:after {
        position: absolute;
        right: 0%;
        top: 0%;
        content: attr(data-count);
        font-size: 40%;
        padding: .6em;
        border-radius: 999px;
        line-height: .75em;
        color: whitesmoke;
        color: #DF0000;
        text-align: center;
        min-width: 1em;
        font-weight: bold;
        background: whitesmoke;
        border-style: solid;
    }

    .fa-circle {
        color: #DF0000;
    }

    .red-cart {
        color: #DF0000;
        background: whitesmoke;
    }
</style>
<style>

    /* header styling */
    h1 {
        color: green;
    }

    /* pagination position styling */
    .pagination_section {
        position: fixed;
        top: 100%;
        left: 50%;
        margin-top: -50px;
        margin-left: -100px;
    }

    /* pagination styling */
    .pagination_section a {
        background-color: lightgray;
        color: black;
        padding: 10px 18px;
        text-decoration: none;
    }

    /* pagination hover effect on non-active */
    .pagination_section a:hover:not(.active) {
        background-color: red;
        color: black;
    }

</style>
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
                <a href="FoodItemController" class="nav-item nav-link active">Home</a>
                <a href="cart.jsp" class="nav-item nav-link">Cart</a>
            </div>
            <div class="nav-item dropdown ml-auto">
                <a class="nav-link dropdown-toggle" href="" id="dropdown09" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                   <span class="flag-icon flag-icon-${sessionScope.lang}"> </span> Language</a>
                <div class="dropdown-menu" aria-labelledby="dropdown09">
                    <a class="dropdown-item" href="/FoodItemController?sessionLocale=ua"><span class="flag-icon flag-icon-ua"> </span>  Ukrainian</a>
                    <a class="dropdown-item" href="/FoodItemController?sessionLocale=us"><span class="flag-icon flag-icon-us"> </span>  English</a>
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
<div id="wrapper">
    <div id="header">
        <h2>Menu</h2>
    </div>
</div>
<div id="container">
    <div id="content">

        <%--TO DO--%>
        <a href="/FoodItemController?filter=allCategories">
            <button type="button" class="btn btn-light">All categories</button>
        </a>
        <c:forEach var="category" items="${categories}">
            <a href="/FoodItemController?filter=${category.name}">
                <button type="button" class="btn btn-light">${category.name}</button>
            </a>
        </c:forEach>
        <%--TO DO--%>

        <table class="table">
            <thead>
            <tr>
                <th scope="col">Image</th>
                <th scope="col">Name<a href="/FoodItemController?sort=NAME">&#8597;</a></th>
                <th scope="col">Price<a href="/FoodItemController?sort=PRICE">&#8597;</a></th>
                <th scope="col">Category<a href="/FoodItemController?sort=CATEGORY">&#8597;</a></th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="foodItem" items="${FOOD_LIST}">
                <tr>
                    <td><img src="${foodItem.image}" width="100" height="100"></td>
                    <td> ${foodItem.name} </td>
                    <td> ${foodItem.price}$</td>
                    <td>${foodItem.category.name}</td>
                    <td><a href="/FoodItemController?command=ORDER&foodId=${foodItem.id}">Add to Cart</a></td>
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
</body>
</html>








