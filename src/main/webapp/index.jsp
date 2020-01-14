<html>

<head>
    <title>Home</title>
</head>

<body onload="simpleConnect();">
<h1>Welcome to our Chat Rooms!</h1>
<h1>Please, enter your nickname:</h1>
<input type="text" id="username"><br/>
<button id = "btn" class="button" onclick="checkUsername();">Let's go!</button><br/>
</body>

<link href="style.css" rel="stylesheet">
<script src="file.js"></script>
<script>
    var input = document.getElementById("username");
    input.addEventListener("keyup", function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            document.getElementById("btn").click();
        }
    });
</script>

</html>