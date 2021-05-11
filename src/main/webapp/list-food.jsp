<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <style>
        .bs-example {
            margin: 20px;
        }
    </style>
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
                <a href="#" class="nav-item nav-link active">Home</a>
                <a href="#" class="nav-item nav-link">Profile</a>
                <a href="#" class="nav-item nav-link">Messages</a>
                <a href="#" class="nav-item nav-link disabled" tabindex="-1">Reports</a>
            </div>
            <div class="navbar-nav ml-auto">
                <a href="#" class="nav-item nav-link">Login</a>
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
                    <td> ${foodItem.price}$</td>
                    <td>${foodItem.category.name}</td>
                    <td><a href="/FoodItemController?command=ORDER&foodId=${foodItem.id}">Add to Cart</a></td>
                </tr>
            </c:forEach>
            </tbody>
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








