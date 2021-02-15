<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <style type="text/css">
        select {
            display: inline-block;
            width: 160px;
            height: 34px;
            line-height: 30px;
            position: relative;
        }
        button {
            margin-left: 6px;
            cursor: pointer;
            padding: 5px 16px;
            text-align: center;
            font-size: 14px;
            border-radius: 4px;
        }
    </style>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<form method="post" action="users">
    <label for="user"><b>Choose a user:</b></label>
    <select name="userId" id="user" required>
        <option value="1">User 1</option>
        <option value="2">User 2</option>
    </select>
    <button type="submit">Log in</button>
    <br>
</form>
</body>
</html>