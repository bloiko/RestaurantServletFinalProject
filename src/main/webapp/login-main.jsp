<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%--<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>--%>
<!DOCTYPE html <%--PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"--%>>
<html>
<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <!------ Include the above in your HEAD tag ---------->

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.8/css/all.css">
    <title>Login</title>
    <style>
        <%@include file="/WEB-INF/css/styles.css" %>
    </style>
</head>
<body>
<article class="card-body mx-auto" style="max-width: 400px;">
    <h4 class="card-title mt-3 text-center"><fmt:message key="registration.write_information"/></h4>
    <form action="/LoginMainController" method="post">
        <label class="errorMessage">${message}</label>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input name="username" class="form-control" placeholder="<fmt:message key="login.username"/>" type="username" value="${username}">
        </div>
        <label class="errorMessage">${password_error_message}</label>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input name="password" class="form-control" placeholder="<fmt:message key="login.password"/>" type="password" value="${password}">
        </div>
        <div class="form-group">
            <button style="height: 50px" type="submit" class="btn btn-primary btn-block">Login</button>
        </div>
    </form>
</article>
</body>
</html>
