<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Student Tracker App</title>
</head>
<style>
    .fa-stack[data-count]:after{
        position:absolute;
        right:0%;
        top:0%;
        content: attr(data-count);
        font-size:40%;
        padding:.6em;
        border-radius:999px;
        line-height:.75em;
        color: white;
        color:#DF0000;
        text-align:center;
        min-width:1em;
        font-weight:bold;
        background: white;
        border-style:solid;
    }
    .fa-circle {
        color:#DF0000;
    }

    .red-cart {
        color: #DF0000; background:white;
    }
</style>
<body>
<span class="fa-stack fa-2x has-badge" data-count="${sessionScope.cart.size()}" >
  <i class="fa fa-circle fa-stack-2x fa-inverse" ></i>
  <i style="" class="fa fa-shopping-cart fa-stack-2x red-cart" ></i>
</span>
<h1>Cart: ${sessionScope.cart.size()} items</h1>
<div id="wrapper">
    <div id="header">
        <h2>Menu</h2>
    </div>
</div>
<div id="container">
    <a href="cart.jsp">Cart</a>
    <div id="content">
        <table border="1">
            <tr>
                <th>Image</th>
                <th>Name<a href="/FoodItemController?sort=NAME">+</a></th>
                <th>Price<a href="/FoodItemController?sort=PRICE">+</a></th>
                <th>Category<a href="/FoodItemController?sort=CATEGORY">+</a></th>
                <th></th>
            </tr>
            <c:forEach var="foodItem" items="${FOOD_LIST}">
                <%--<c:url var="tempLink" value="FoodItemController">
                    <c:param name="command" value="LOAD" />
                    <c:param name="studentId" value="${tempStudent.id}" />
                </c:url>--%>
                <%--<c:url var="deleteLink" value="FoodItemController">
                    <c:param name="command" value="ADD" />
                    <c:param name="foodId" value="${foodItem.id}" />
                </c:url>--%>
                <tr>
                    <td><img src="${foodItem.image}" width="100" height="100"></td>
                    <td> ${foodItem.name} </td>
                    <td> ${foodItem.price} </td>
                    <td>${foodItem.category.name}</td>
                    <td><a href="/FoodItemController?command=ORDER&foodId=${foodItem.id}">Add to Cart</a></td>
                </tr>
            </c:forEach>
        </table>
        <div>
            <a href="FoodItemController?page=1">1</a>
            <a href="FoodItemController?page=2">2</a>
            <a href="FoodItemController?page=3">3</a>
        </div>
    </div>
</div>
</body>


</html>








