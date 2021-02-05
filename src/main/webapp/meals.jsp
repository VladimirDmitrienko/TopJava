<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
    <head>
        <title>Meals</title>
    </head>
    <body>
        <h3>
            <a href="index.html">Home</a>
        </h3>
        <hr>
        <h2>Meals</h2>
        <table border="1" cellpadding="5">
            <tr>
                <th>Description</th>
                <th>Calories</th>
                <th>Date</th>
            </tr>
            <c:forEach var="meal" items="${meals}">
                <tr style="${meal.excess ? 'color: red' : 'color: green'}">
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td>${meal.date}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </body>
</html>