<%-- 
    Document   : main
    Created on : 28 мая 2024 г., 13:01:19
    Author     : username
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java8.BeanComponent" %>
<jsp:useBean id="BeanComponent" scope="session" class="java8.BeanComponent" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Два</title>
</head>
<body>
    <h1>Главная страница</h1>
    <%
        // Создаем экземпляр класса BeanComponent
        BeanComponent bean = new BeanComponent();

        // Получаем текущее значение триггера
        int trigger = bean.getTrigger();
    %>
    <!--<p>Текущее значение триггера: <%= trigger %></p>-->
    <!--<p>Триггер 4 <jsp:setProperty name="BeanComponent" property="trigger" value="100" /></p>-->
    <p>Триггер <jsp:getProperty name="BeanComponent" property="trigger" /></p>
    <form action="finish.jsp" method="post">
        Введите число M: <input type="text" name="M"><br>
        Введите последовательность чисел (через запятую): <input type="text" name="numbers"><br>
        <input type="submit" value="Вычислить">
    </form>
</body>
</html>
