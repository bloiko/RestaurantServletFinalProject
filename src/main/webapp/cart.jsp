<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Loiko
  Date: 01.05.2021
  Time: 12:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div id="wrapper">
    <div id="header">
        <h2>Cart</h2>
    </div>
</div>
<div id="container">
    <a href="FoodItemController">Continue Shopping</a>
    <div id="content">
        <table border="1">
            <tr>
                <th>Image</th>
                <th>Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Category</th>
                <th>Option</th>
                <th>Sub Total</th>
            </tr>
            <c:forEach var="item" items="${sessionScope.cart}">
                <c:set var="sum" value="${sum+item.foodItem.price*item.quantity}"/>
                <tr>
                    <td><img src="${item.foodItem.image}" width="100" height="100"></td>
                    <td> ${item.foodItem.name} </td>
                    <td> ${item.foodItem.price}</td>
                    <td> ${item.quantity} </td>
                    <td> ${item.foodItem.itemCategory} </td>
                    <td align="center"><a href="/CartController?command=DELETE&itemId=${item.foodItem.id}" onclick="return confirm('Are you sure?')">Delete</a></td>
                    >
                    <td>${item.foodItem.price*item.quantity}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
<div class="footer">
    <h2>Sum: ${sum}</h2>
    <a href="OrderController">Order</a>
</div>
</body>
</html>
