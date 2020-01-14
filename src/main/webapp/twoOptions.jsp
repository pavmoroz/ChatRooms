<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Choice</title>
</head>

<body>
<h1 id="sh1"></h1>
<h1 id="sh2">Please, choose your option.</h1>

<div>
<button id="create" onclick="window.location.href='createChatRoom.jsp'" class="button">Create a new Chat Room!</button>
</div>
<div>
    <b>OR</b>
</div>
<div>
    <button id="join" onclick="window.location.href='joinChatRoom.jsp'" class="button">Join an existing Chat Room! </button>
</div>
</body>
<link href="style.css" rel="stylesheet">
<script>
    document.getElementById("sh1").innerHTML = "We are happy to see you, " + sessionStorage.getItem("username") + "!";
</script>
</html>