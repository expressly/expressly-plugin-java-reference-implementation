<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MyShop Homepage</title>
</head>
<body>
<h1>Welcome To MyShop</h1>

<p>
    This is meant to representative of your shop's homepage or any page on which you would be redirecting new users from
    the Expressly network.
</p>

<p>
    In the event that an Expressly user has clicked a promotion for MyShop they will be forwarded to the
    /expressly/api/[campaign-customer-uuid] endpoint which will retrieve the expressky confirmation html and
    add it to the user's session under the attibute name "popupContent".
</p>

<p>
    Following this it will redirect them to a
    regular page on your site, typically your home page or cart. You should remove the html from the session attribute
    space and render it at the bottom of your page as in this example.
</p>

<%--Render the expressly confirmation dialog if present in the attribute space--%>
<c:if test="${popupContent}">
    ${popupContent}
</c:if>


</body>
</html>
