var ws;

function simpleConnect() {
    ws = new WebSocket("ws://" + document.location.host + "/ChatRooms/");
}

function disconnect() {
    sessionStorage["timeOfUserDisconnection"] = new Date().getTime();
    askServerToDisconnectUser(sessionStorage.getItem("username"), sessionStorage.getItem("chatRoomName"));
}

function askServerToDisconnectUser(username, chatRoomName) {
    var jsonToSend = JSON.stringify({
        "from": username,
        "messageType": "USER_DISCONNECT",
        "chatRoomName": chatRoomName
    });
    ws.send(jsonToSend);
}

function askServerToCancelDisconnection(username, chatRoomName) {
    var jsonToSend = JSON.stringify({
        "from": username,
        "messageType": "USER_DISCONNECT_CANCELLATION",
        "chatRoomName": chatRoomName

    });
    ws.send(jsonToSend);
}

function checkUsername() {
    var nameToCheck = document.getElementById("username").value;
    if(inputFieldCheck(nameToCheck)){
        var jsonToSend = JSON.stringify({
            "content": nameToCheck,
            "messageType": "USERNAME_CHECK"
        });

        ws.send(jsonToSend);

        ws.onmessage = function (event) {
            var message = JSON.parse(event.data);

            if (message.messageType === "USERNAME_CHECK") {
                if (message.content === "YES") {
                    alert("This username is already taken, please think of another one!");
                    document.getElementById("username").value = "";
                } else if (message.content === "NO") {
                    sessionStorage.setItem("username", document.getElementById("username").value);
                    window.location.href = "twoOptions.jsp";
                }
            }
        };
    }
}

function checkChatRoomNameCreate() {
    var nameToCheck = document.getElementById("chatRoomName").value;

    if(inputFieldCheck(nameToCheck)) {

        var jsonToSend = JSON.stringify({
            "content": nameToCheck,
            "messageType": "CHAT_ROOM_NAME_CREATE"
        });

        ws.send(jsonToSend);

        ws.onmessage = function (event) {
            var message = JSON.parse(event.data);

            if (message.messageType === "CHAT_ROOM_NAME_CREATE") {
                if (message.content === "YES") {
                    alert("This Chat Room name is already taken, please think of another one!");
                    document.getElementById("chatRoomName").value = "";
                } else if (message.content === "NO") {
                    sessionStorage.setItem("chatRoomName",
                        document.getElementById("chatRoomName").value);
                    window.location.href = "chatRoom.jsp";
                }
            }
        };
    }
}

function checkChatRoomNameJoin() {
    var nameToCheck = document.getElementById("chatRoomName").value;

    if(inputFieldCheck(nameToCheck)) {

        var jsonToSend = JSON.stringify({
            "content": nameToCheck,
            "messageType": "CHAT_ROOM_NAME_JOIN"
        });
        ws.send(jsonToSend);

        ws.onmessage = function (event) {
            var message = JSON.parse(event.data);

            if (message.messageType === "CHAT_ROOM_NAME_JOIN") {
                if (message.content === "NO") {
                    alert("There is no Chat Room with this name, please type a name of an existing room or create a new one.");
                    document.getElementById("chatRoomName").value = "";
                } else if (message.content === "YES") {
                    sessionStorage.setItem("chatRoomName", document.getElementById("chatRoomName").value);
                    window.location.href = "chatRoom.jsp";
                }
            }
        }
    }
}

function createAChatRoom() {

    ws.onopen = function () {

        if (sessionStorage["timeOfUserDisconnection"]) {
            var timeOfUserDisconnection = sessionStorage["timeOfUserDisconnection"];
            var timeOfUserConnection = new Date().getTime();
            var durationOfDisconnection = timeOfUserConnection - timeOfUserDisconnection;
            if (durationOfDisconnection < (5 * 1000)) {
                askServerToCancelDisconnection(sessionStorage.getItem("username"), sessionStorage.getItem("chatRoomName"));
            }
        }

        var jsonToSend = JSON.stringify({
            "content": sessionStorage.getItem("username") + "/" + sessionStorage.getItem("chatRoomName"),
            "messageType": "JOIN_THE_ROOM",
            "chatRoomName": sessionStorage.getItem("chatRoomName")
        });

        ws.send(jsonToSend);
    };

    ws.onmessage = function (event) {
        var message = JSON.parse(event.data);
        var tmpChatStory = document.getElementById("chat");

        switch (message.messageType) {
            case "GENERAL":
                tmpChatStory.innerHTML += message.from + ": " + message.content + "\n";
                break;
            case "JOIN_THE_ROOM":
            case "QUIT":
                if (message.content) {
                    tmpChatStory.innerHTML += message.content + "\n";
                }
                if (message.activeUsers) {
                    var i;
                    var tmpList = "Active users:";
                    for (i = 0; i < message.activeUsers.length; i++) {
                        tmpList += "\n> " + message.activeUsers[i];
                    }
                    document.getElementById("userList").value = tmpList;
                }
                break;
            case "JOIN_WITH_STORY":
                if (message.chatStory) {
                    var tmp = "";
                    for (i = message.chatStory.length - 1; i >= 0; i--) {
                        tmp += message.chatStory[i];
                    }
                    tmpChatStory.innerHTML += tmp;
                }
                break;
        }
    };
}

function send() {
    var content = document.getElementById("msg").value;
    if (content && content.match(/[^ ]+/g)) {
        var jsonToSend = JSON.stringify({
            "content": content,
            "messageType": "GENERAL",
            "from": sessionStorage.getItem("username"),
            "chatRoomName": sessionStorage.getItem("chatRoomName")
        });
        ws.send(jsonToSend);
        document.getElementById("msg").value = "";
    }
}

function inputFieldCheck(inputField){
    var res1 = true;
    var res2 = true;
    if(!(inputField.match(/[^ ]+/))){
        alert("You can't have no name.");
        res1=false;
    }
    else if(inputField.match(/[\/]+/)){
        alert("You can't have '/' in your name.");
        res2 = false;
    }
    return res1 && res2;
}