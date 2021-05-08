<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div id="wrapper">
    <div id="header">
        <h2>Admin</h2>
    </div>
</div>
<div id="container">
    <div id="content">
        <table border="1">
            <tr>
                <th>id</th>
                <th>User information</th>
                <th>Date of ordering</th>
                <th>Items</th>
                <th>Order price</th>
                <th>Status</th>
            </tr>
            <c:forEach var="order" items="${ORDERS_LIST}">
                <c:set var="orderPrice" value="${0}"/>
                <tr>
                    <th>${order.id}</th>
                    <th>
                        <table>
                            <tr>
                                <td>First Name</td>
                                <td> ${order.user.firstName} </td>
                            </tr>
                            <tr>
                                <td>Last Name</td>
                                <td>${order.user.lastName}</td>
                            </tr>
                            <tr>
                                <td>Username</td>
                                <td>${order.user.userName}</td>
                            </tr>
                            <tr>
                                <td>Email</td>
                                <td>${order.user.email}</td>
                            </tr>
                            <tr>
                                <td>Address</td>
                                <td>${order.user.address}</td>
                            </tr>
                            <tr>
                                <td>Phone Number</td>
                                <td>${order.user.phoneNumber}</td>
                            </tr>
                        </table>
                    </th>
                    <th>${order.orderDate}</th>
                    <th>
                        <table>
                            <tr>
                                <th>Name</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Sub Total</th>
                            </tr>


                            <c:forEach var="item" items="${order.items}">
                                <c:set var="orderPrice" value="${orderPrice+item.foodItem.price*item.quantity}"/>
                                <tr>
                                    <td> ${item.foodItem.name} </td>
                                    <td> ${item.foodItem.price}</td>
                                    <td>${item.quantity}</td>
                                    <td>${item.foodItem.price*item.quantity}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </th>
                    <th>${orderPrice}</th>
                    <th>
                            ${order.status}
                        <br/>
                        <a href="/AdminController?command=CHANGE_STATUS&orderId=${order.id}">Change status</a>
                    </th>
                    <th><a href="/AdminController?command=DELETE&orderId=${order.id}">Delete order</a></th>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
