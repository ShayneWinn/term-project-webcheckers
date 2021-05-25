<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>Web Checkers | Sign In</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

    <h1>Web Checkers | Sign In</h1>

    <div class="body">
        <#include "message.ftl" />

        <form action="./signin" method="POST">
            Please enter your username:
            <br>
            <input name="currentUser" required>
            <br>
            <button type="submit">Sign In</button>
        </form>
    </div>
</div>

</body>

</html>