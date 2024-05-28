import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet; 

import java.util.Arrays;
import java.util.stream.Collectors;

/**
*
* @author username
*/
@WebServlet(name = "NewServlet1", urlPatterns = {"/newServlet1"})
public class NewServlet1 extends HttpServlet {
    private int visitCounter = 0;
    private int textSize = 16;
    private static final int MIN_TEXT_SIZE = 8;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        visitCounter++;
        String numbersStr = request.getParameter("numbers");
        String resetTextSizeStr = request.getParameter("resetTextSize");

        if (resetTextSizeStr != null) {
            try {
                textSize = Integer.parseInt(resetTextSizeStr);
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
        out.println("<html><head><title>Лаб7</title></head><body>");
        out.println("<h1>Белов Владимир Алексеевич, группа 4310</h1>");
        out.println("<p>Счетчик посещений: " + visitCounter + "</p>");

        if (textSize > MIN_TEXT_SIZE) {
            textSize -= 1;
        } else {
            out.println("<p>Дальнейшее уменьшение текста невозможно</p>");
        }


        int sumEvenNegative = 0;
        int sumOddPositive = 0;
        for (int number : numbers) {
            if (number % 2 == 0 && number < 0) {
          sumEvenNegative += number;
        } else if (number % 2 != 0 && number > 0) {
          sumOddPositive += number;
        }
      }
          out.println("<table style='font-size:" + textSize + "px'>");
        out.println("<tr><th>Числа</th><th>Список чисел</th></tr>");
        out.println("<tr><td>Числа</td><td>" + Arrays.toString(numbers) + "</td></tr>");
                out.println("</table>");

                out.println("<table style='font-size:" + textSize + "px'>");
        out.println("<tr><th>Число</th><th>Результат</th></tr>");
        out.println("<tr><td>sumEvenNegative</td><td>" + sumEvenNegative + "</td></tr>");
        out.println("<tr><td>sumOddPositive</td><td>" + sumOddPositive + "</td></tr>");
        out.println("</table>");


        out.println("</body></html>");
    }
}
