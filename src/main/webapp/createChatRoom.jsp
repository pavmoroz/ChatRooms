<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Creation</title>
</head>
<h1 id="inst"></h1>
<h1>you want to create.</h1>
<body onload="simpleConnect()">
<input type="text" id="chatRoomName"><br/>
<button class="button "onclick="checkChatRoomNameCreate();" id="btn">Confirm</button><br/>
</body>

<link href="style.css" rel="stylesheet">
<script src="file.js"></script>
<script>
    document.getElementById("inst").innerHTML =
        sessionStorage.getItem("username") + ", please, enter the Chat Room Name,";
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