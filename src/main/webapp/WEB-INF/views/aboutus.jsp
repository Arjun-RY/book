<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Header</title>
<style>
body {
	 font-family: sans-serif, Arial;
            background-color: pink;
            margin: 0;
            padding: 0;
}
header {
    background-color: pink; /* Light gray background */
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 20px;
    box-sizing: border-box;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Add shadow for depth */
}
/*
.left-section {
    display: flex;
    gap: 10px;
}

.center-section {
    text-align: center;
    font-size: 24px; 
    color: #333;
}

.right-section {
    display: flex;
    gap: 10px;
}
*/
button[type="submit"],
button[type="button"] {
    padding: 10px 20px; /* Increase padding for buttons */
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease; /* Smooth transition for hover effect */
}

button[type="submit"] {
    background-color: #28a745; /* Green color for submit button */
    color: white;
}

button[type="submit"]:hover {
    background-color: #218838; /* Darker green color on hover */
}

button[type="button"] {
    background-color: #ffc107; /* Yellow color for register button */
    color: white;
}

button[type="button"]:hover {
    background-color: #e0a800; /* Darker yellow color on hover */
}



</style>
</head>
<body>

<header>
    <div class="left-section">
        <a href="popup.jsp"><button type="button">About Us</button></a>
    </div>
    <div class="center-section">
        Book Store
    </div>
    <div class="right-section">
        <a href="login.jsp"><button type="button">Login</button></a>
        <a href="register.jsp"><button type="button">Register</button></a>
    </div>
</header>


</body>
</html>

