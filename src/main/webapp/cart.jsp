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
        <table>
            <tr>
                <th>Image</th>
                <th>Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Category</th>
                <th>Option</th>
                <th>Sub Total</th>
            </tr>
            <c:forEach var="foodItem" items="${sessionScope.cart}">
                <c:set var="sum" value="${sum+foodItem.price*foodItem.quantity}"/>
                <tr>
                    <td><img src="${foodItem.image}" width="100" height="100"></td>
                    <td> ${foodItem.name} </td>
                    <td> ${foodItem.price} </td>
                    <td> ${foodItem.quantity} </td>
                    <td> ${foodItem.itemCategory} </td>
                    <td align="center"><a href="">Delete</a></td>
                    >
                    <td>${foodItem.price*foodItem.quantity} </td>
                </tr>
            </c:forEach>


        </table>
    </div>
</div>
<div class="footer">
    <h2>Sum: ${sum}</h2>
</div>
</body>
</html>
