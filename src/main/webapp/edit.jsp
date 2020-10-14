<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Meal</title>
</head>
<body>
<h2>${action == "add" ? "Add" : "Edit"} Meal</h2>
<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="id" value="${meal.id}">
    <p><label>Date</label>
        <input type="datetime-local" name="dateTime" value="${meal.dateTime}" required></p>
    <p><label>Description</label>
        <input type="text" name="description" value="${meal.description}" required></p>
    <p><label>Calories</label>
        <input type="number" name="calories" value="${meal.calories}" required></p>
    <input type="submit" id="save" value="Save">
    <input type="button" id="cancel" value="Cancel" onclick="window.history.back()">
</form>
</body>
</html>
