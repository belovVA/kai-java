<%-- 
    Document   : finish
    Created on : 28 мая 2024 г., 13:01:45
    Author     : username
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java8.BeanComponent" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Три</title>
</head>
<body>
    <h1>Финишная страница</h1>
    <%
        // Получаем введенные данные из запроса
        int M = Integer.parseInt(request.getParameter("M"));
        String[] numbersStr = request.getParameter("numbers").split(",");
        int[] numbers = new int[numbersStr.length];
        for (int i = 0; i < numbersStr.length; i++) {
            numbers[i] = Integer.parseInt(numbersStr[i].trim());
        }

        // Создаем экземпляр класса BeanComponent
        BeanComponent bean = new BeanComponent();

        // Вычисляем результат
        String result = bean.calculateProduct(numbers, M);
    %>
    <%-- Если результат - строка "Нет чисел больше M", выводим сообщение --%>
    <% if ("Нет чисел больше M".equals(result)) { %>
        <table border="1">
        <tr>
            <th>Результат вычислений</th>
        </tr>
        <tr>
            <td>Нет чисел больших M</td>
        </tr>
    </table>
    <% } else { %>
    <%-- В противном случае выводим результат вычислений --%>
    <table border="1">
        <tr>
            <th>Результат вычислений</th>
        </tr>
        <tr>
            <td><%= result %></td>
        </tr>
    </table>
    <% } %>
    <a href="main.jsp">Возврат на главную страницу</a>
</body>
</html>
