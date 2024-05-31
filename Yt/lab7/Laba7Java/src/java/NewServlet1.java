/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 
 */
@WebServlet(name = "NewServlet1", urlPatterns = {"/NewServlet1"})
public class NewServlet1 extends HttpServlet {
 // Поле для хранения состояния триггера
    private boolean trigger = false;
    private int textSize = 16;
    private static final int MIN_TEXT_SIZE = 8;
    private static final int DEFAULT_TEXT_SIZE = 16;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
// Меняем состояние триггера при каждом запросе
        trigger = !trigger;
        
        String numbersStr = request.getParameter("numbers");
        String resetSize = request.getParameter("reset");
        if (resetSize != null) {
            try {
                textSize = DEFAULT_TEXT_SIZE;
                if (textSize < MIN_TEXT_SIZE) {
                    textSize = MIN_TEXT_SIZE;
                }
            } catch (NumberFormatException e) {
                textSize = 16; // Reset to default if parsing fails
            }
        }

        int[] ints = numbersStr != null ? Arrays.stream(numbersStr.split(","))
                                                .map(String::trim)
                                                .mapToInt(Integer::parseInt)
                                                .toArray() : new int[]{};

        Map<Integer, Integer> map = new HashMap<>();

        int maxCurr = Integer.MIN_VALUE;
        for (int i : ints) {
            Integer curr = map.get(i);
            if (curr == null)
                curr = 0;
            map.put(i, ++curr);
            if (curr > maxCurr) {
                maxCurr = curr;
            }
        }
        int minMaxRate = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == maxCurr) {
                if (entry.getKey() < minMaxRate) {
                    minMaxRate = entry.getKey();
                }
            }
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Лаба7</title></head><body>");
              out.println("<p><h1>Работу выполнил Уткин Амир Витальевич гр.4310 </h1> </p>");

      out.println("<p>Текущее состояние триггера: " + trigger + "</p>");

        if (textSize > MIN_TEXT_SIZE) {
            textSize -= 1;
        } else {
            out.println("<p>Дальнейшее уменьшение текста невозможно</p>");
        }
out.println("<table style='font-size:" + textSize + "px; border-collapse: collapse;'>");
out.println("<tr><td style='border: 1px solid black;'>Числа:</td><td style='border: 1px solid black;'>" + Arrays.toString(ints) + "</td></tr>");
out.println("<tr><td style='border: 1px solid black;'>oddNegativeCount</td><td style='border: 1px solid black;'>" + minMaxRate + "</td></tr>");
out.println("</table>");



        out.println("</body></html>");
    }
}
