<%--
  Created by IntelliJ IDEA.
  User: Тарасов Александр
  Date: 18.04.2023
  Time: 11:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Три</title>
</head>
<body>
<header>
    <nav>
        <li><a href="StartPage.jsp">Раз</a></li>
        <li><a href="MainPage.jsp">Два</a></li>
        <li><a href="FinishPage.jsp">Три</a></li>
    </nav>
</header>
<main>
    <jsp:useBean id="mybean" scope="session" class="com.example.beans_jsp.MainBean"/>
    <jsp:setProperty name="mybean" property="*"/>
    <h3>Результат: </h3>
    <%= mybean.getAl()%>
</main>
</body>
</html>
