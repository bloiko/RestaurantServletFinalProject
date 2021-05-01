<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Tracker App</title>
</head>
<body>
<h1>Cart: ${sessionScope.cart.size()} items</h1>
<div id="wrapper">
    <div id="header">
        <h2>Menu</h2>
    </div>
</div>
<div id="container">
    <form action="/FoodItemController">
        <select name="sort">
            <option value="NAME">By name</option>
            <option value="PRICE">By price</option>
            <option value="CATEGORY">By category</option>
        </select>
        <input type="submit" value="Sort">
    </form>
    <div id="content">
        <table border="1">
            <tr>
                <th>Image</th>
                <th>Name</th>
                <th>Price</th>
                <th>Category</th>
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
                    <td>${foodItem.itemCategory}</td>
                    <td><a href="/CartController?command=ORDER&foodId=${foodItem.id}">Add to Cart</a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>


</html>








