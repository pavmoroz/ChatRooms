<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Chat Room</title>
</head>

<body onload="simpleConnect(); createAChatRoom();" onbeforeunload="disconnect();">


<%--<button onclick="window.location.href='index.jsp'" class="button">Leave</button>--%>

<%--<textarea readonly="true" rows="30" cols="45" id="chat"></textarea>--%>

<%--<textarea readonly="true" rows="30" cols="20" id="userList">List of active users:</textarea>--%>

<%--<input type="text" size="40" id="msg" placeholder="Type your message here"/>--%>
<%--<button class="button" onclick="send();" id="send">Send</button>--%>

<div>
    <div id="namesLines1">
        <b id="namesLines"></b>
    </div>
    <div>
        <button id="leave" onclick="window.location.href='index.jsp'" class="button">Leave</button>
    </div>
</div>

<div>
    <textarea id="chat" readonly="true" rows="18" cols="29" style="font-size: 18pt"></textarea>
    <textarea id="userList" rows="18" cols="15" style="font-size: 18pt"> Active users:</textarea>
</div>

<div>
    <div>
        <input type="text" size="40" id="msg" placeholder="Message"/>
    </div>
    <div id="send1">
        <button id="send" class="button" onclick="send();" >Send</button>
    </div>
</div>



</body>
<link href="style4.css" rel="stylesheet">
<script src="file.js"></script>
<script>
    document.getElementById("chat").scrollTop = document.getElementById("chat").scrollHeight;
    document.getElementById("userList").scrollTop = document.getElementById("userList").scrollHeight
    document.getElementById("namesLines").innerHTML = "Username: " + sessionStorage.getItem("username") + ", Chat Room name: " + sessionStorage.getItem("chatRoomName");
</script>
<script>
    var input = document.getElementById("msg");
    input.addEventListener("keyup", function (event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            document.getElementById("send").click();
        }
    });
</script>

</html>