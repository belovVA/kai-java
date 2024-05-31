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
import java.util.Collections;


/**
 *
 * @author 
 */
@WebServlet(name = "NewServlet1", urlPatterns = {"/NewServlet1"})
public class NewServlet1 extends HttpServlet {
    private int visitCounter = 0;
    private int textSize = 16;
    private static final int MIN_TEXT_SIZE = 8;
    private static final int DEFAULT_TEXT_SIZE = 16;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        visitCounter++;
        String numbersStr = request.getParameter("numbers");
        String resetSize = request.getParameter("resetSize");
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

        int[] numbers = numbersStr != null ? Arrays.stream(numbersStr.split(","))
                                                .map(String::trim)
                                                .mapToInt(Integer::parseInt)
                
                                                .toArray() : new int[]{};

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Лаба 7</title></head><body>");
        out.println("<p><h1>Работу выполнил Григорьев Иван Алексеевич гр.4310 </h1> </p>");

        out.println("<p>Счетчик посещений: " + visitCounter + "</p>");

        if (textSize > MIN_TEXT_SIZE) {
            textSize -= 1;
        } else {
            out.println("<p>Дальнейшее уменьшение текста невозможно</p>");
        }



          out.println("<table style='font-size:" + textSize + "px'>");
                  out.println("<tr><td>Числа До сортировки: " + Arrays.toString(numbers) + "</td></tr>");

        Arrays.sort(numbers);
        int left = 0;
        int right = numbers.length - 1;
        while (left < right) {
            int temp = numbers[left];
            numbers[left] = numbers[right];
            numbers[right] = temp;
            left++;
            right--;
        }
        
        out.println("<tr><td>Числа После сортировки по убыванию: " + Arrays.toString(numbers) + "</td></tr>");
        out.println("</table>");


        out.println("</body></html>");
    }
}
