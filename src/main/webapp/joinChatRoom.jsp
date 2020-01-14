<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Join</title>
</head>
<h1 id="instr"></h1>
<h1>you want to join.</h1>
<body onload="simpleConnect()">
<input type="text" id="chatRoomName"><br/>
<button class="button" onclick="checkChatRoomNameJoin();" id="btn">Confirm</button><br/>
</body>

<link href="style.css" rel="stylesheet">
<script src="file.js"></script>
<script>
    document.getElementById("instr").innerHTML = sessionStorage.getItem("username") + ", please, enter the Chat Room name,";
</script>
<script>
    var input = document.getElementById("chatRoomName");
    input.addEventListener("keyup", function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            document.getElementById("btn").click();
        }
    });
</script>

</html>