<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<p><a href="meals?action=add">Add Meal</a></p>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="mealTo" items="${mealsTo}">
        <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr style="color: ${mealTo.excess ? "red" : "green"}">
            <td>${mealTo.date}&nbsp;${mealTo.time}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?id=${mealTo.id}&action=edit">Update</a></td>
            <td><a href="meals?id=${mealTo.id}&action=delete">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>