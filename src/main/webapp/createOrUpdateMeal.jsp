<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <title>Meals</title>
    <style type="text/css">
        label, .field {
            display: inline-block;
            width: 200px;
            text-align: left;
            margin: 5px;
            border: solid silver;
            border-radius: 2px;
        }
        .button-block {
            margin-left: 5px;
        }
    </style>
</head>
<body>
<h3>
    <a href="index.html">Home</a>
</h3>
<hr>
<h2>Edit meal</h2>

<form method="POST" action='meals' name="createOrUpdateForm">
    <label for="dateTime">DateTime:</label>
    <input id="dateTime" name="dateTime" class="field" type="datetime-local" required
           value="${meal.dateTime}"/>
    <br/>
    <label for="description">Description:</label>
    <input id="description" name="description" class="field" type="text" required
        value="<c:out value="${meal.description}"/>"/>
    <br/>
    <label for="calories">Calories:</label>
    <input id="calories" name="calories" class="field" type="number" required
           value="<c:out value="${meal.calories}"/>"/>
    <br/>
    <input type="hidden" name="id" value="<c:out value="${meal.id}"/>"/>
    <span class="button-block">
        <input type="submit" value="Save"/>
        <a href="meals"><input type="button" value="Cancel"></a>
    </span>
</form>
</body>
</html>