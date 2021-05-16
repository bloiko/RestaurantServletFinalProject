<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<%@ page session="true" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<html lang="${sessionScope.lang}">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.1.0/css/flag-icon.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <title>Cart</title>
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
        button {
            position: relative;
            font-weight: bold;
            height: 50px;
            width: 150px;
            top: 100%;
            left: 90%;
            margin-top: -50px;
            margin-left: -100px;
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
                <a href="FoodItemController" class="nav-item nav-link">Home</a>
                <a href="cart.jsp" class="nav-item nav-link active">Cart</a>
            </div>
            <div class="nav-item dropdown ml-auto">
                <a class="nav-link dropdown-toggle" href="" id="dropdown09" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span class="flag-icon flag-icon-${sessionScope.lang}"> </span> Language</a>
                <div class="dropdown-menu" aria-labelledby="dropdown09">
                    <a class="dropdown-item" href="/CartController?sessionLocale=ua"><span class="flag-icon flag-icon-ua"> </span>  Ukrainian</a>
                    <a class="dropdown-item" href="/CartController?sessionLocale=us"><span class="flag-icon flag-icon-us"> </span>  English</a>
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
    <h2>Cart</h2>
</div>
<div id="container">
    <%--<a href="FoodItemController"><button type="button" class="btn btn-light">Continue Shopping</button></a>--%>
    <div id="content">
        <table class="table">
            <thead></thead>
            <tr>
                <th scope="col">Image</th>
                <th scope="col">Name</th>
                <th scope="col">Price</th>
                <th scope="col">Quantity</th>
                <th scope="col">Option</th>
                <th scope="col">Sub Total</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${sessionScope.cart}">
                <c:set var="sum" value="${sum+item.foodItem.price*item.quantity}"/>
                <tr>
                    <td><img src="${item.foodItem.image}" width="80" height="80"></td>
                    <td> ${item.foodItem.name} </td>
                    <td> ${item.foodItem.price}</td>
                    <td> ${item.quantity} </td>
                    <td align="center"><a href="/CartController?command=DELETE&itemId=${item.foodItem.id}"
                                          onclick="return confirm('Are you sure?')">Delete</a></td>
                    >
                    <td>${item.foodItem.price*item.quantity}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<div class="footer">
    <h2>Sum: ${sum}</h2>
    <a href="/RegistrationController">
        <button type="button" class="btn btn-danger"> Order</button>
    </a>
</div>
</body>
</html>
