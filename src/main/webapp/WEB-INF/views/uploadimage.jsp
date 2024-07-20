<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.Base64" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Product Page</title>
<style>
    .container {
        height:100%;
        background-color: pink;
        display: flex;
        justify-content: space-around;
        align-items: center; /* Center items vertically */
        margin-top: 20px;
    }

    .box {
        margin-left: 7%;
        margin-right: 7%;
        width: 25%;
        overflow: hidden;
        border: 2px solid transparent; /* Invisible border */
        border-radius: 10px; /* Curved border */
    }

    .box img {
        width: 100%;
        height: auto;
    }

    marquee{
        padding: 10px;
        background-color: #fff;
    }
</style>
</head>
<body>

<%
try {
    Connection connection = DatabaseConnection.getConnection();
    String sql = "SELECT book_name, author_name FROM book " +
            " ORDER BY last_updated DESC" +
            " LIMIT 5;";
    StringBuilder book = new StringBuilder();
    PreparedStatement statement = connection.prepareStatement(sql);
    ResultSet resultSet = statement.executeQuery();
    while(resultSet.next()) {
        book.append(resultSet.getString("book_name") + " - " +  resultSet.getString("author_name") + "&emsp;&emsp;");
    }
%>
    <marquee behavior="scroll" direction="left">New arrivals: &emsp;&emsp;&emsp;<%= book %></marquee>

<div class="container">
<%
    String query = "SELECT image FROM image WHERE image_id < 4 AND date >= CURDATE();";
    PreparedStatement pstmt = connection.prepareStatement(query);
    ResultSet rs = pstmt.executeQuery();
    int i = 1;
    boolean resultSetEmpty = true;
    while (rs.next()) {
        resultSetEmpty = false;
        // Convert image to Base64 encoding for embedding in HTML
        Blob blob = rs.getBlob("image");
        InputStream inputStream = blob.getBinaryStream();
        byte[] imageBytes = new byte[(int) blob.length()];
        inputStream.read(imageBytes);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        // Generate data URL for embedding in HTML
        String dataURL = "data:image/jpg;base64," + base64Image;
%>
    <div class="box">
        <img src="<%= dataURL %>" alt="Product <%= i %>">
    </div>
<%
       i++;
    }
    if (resultSetEmpty) {
%>
    <div class="box">
        <img src="1.jpg" alt="Fallback Product 1">
    </div>
    <div class="box">
        <img src="2.jpg" alt="Fallback Product 2">
    </div>
    <div class="box">
        <img src="3.jpg" alt="Fallback Product 3">
    </div>
<%
    }
} catch (SQLException e) {
    e.printStackTrace();
}
%>
</div>
</body>
</html>
