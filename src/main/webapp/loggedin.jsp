<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome Page</title>
</head>

<body>
<h1>Welcome <c:if test="${login.length() > 0}">${login}</c:if> !</h1>

<p>
    This is meant to be representative of your shop's login landing page.
</p>

<p>
    Once a user accepts a migration, he will be logged in and land here.
    The cart will include the coupon code and product id (if there is one).
</p>

</body>
</html>
