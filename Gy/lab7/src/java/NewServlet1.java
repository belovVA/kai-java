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


    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // Меняем состояние триггера при каждом запросе
        trigger = !trigger;


        String numbersStr = request.getParameter("numbers");
        String resetTextSizeStr = request.getParameter("resetTextSize");

        if (resetTextSizeStr != null) {
            try {
                textSize = Integer.parseInt(resetTextSizeStr);
                if (textSize < MIN_TEXT_SIZE) {
                    textSize = MIN_TEXT_SIZE;
                }
            } catch (NumberFormatException e) {
//                textSize = 16; // Reset to default if parsing fails
            }
        }

        int[] numbers = numbersStr != null ? Arrays.stream(numbersStr.split(","))
                                                .map(String::trim)
                                                .mapToInt(Integer::parseInt)
                                                .toArray() : new int[]{};

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Лаб7</title></head><body>");
        out.println("<h1>Гумеров Б.Р. гр. 4310</h1>");
        out.println("<p>Текущее состояние триггера: " + trigger + "</p>");

        if (textSize > MIN_TEXT_SIZE) {
            textSize -= 1;
        } else {
            out.println("<p>Дальнейшее уменьшение текста невозможно</p>");
        }
        
        out.println("<table style='font-size:" + textSize + "px'>");
        out.println("<tr><td>Числа до сортировки: " + Arrays.toString(numbers) + "</td></tr>");

        Arrays.sort(numbers);
        out.println("<tr><td>Числа после сортировки: " + Arrays.toString(numbers) + "</td></tr>");
        out.println("</table>");


        out.println("</body></html>");
    }
}
