package com.example.myservlet;

import java.io.*;
import java.util.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private static List<Map.Entry<String, String>> storage;
    private static final byte min_size = 5;
    private static final byte default_size = 1;
    public static byte size;
    public static long counter;
    public static boolean b;
    public static String ast;

    public HelloServlet() {
        HelloServlet.ast = "a static var c=";
        storage = new ArrayList<>();
        size = default_size;
        HelloServlet.counter = 0;
        HelloServlet.b = false;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Пищулин Максим Сергеевич 4312 </title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>ServletAppl" + request.getServletPath() + "</h1>");
            String fio = request.getParameter("fio");
            String group = request.getParameter("group");
            out.println(fio + " + " + group);

            if (HelloServlet.b) HelloServlet.b = false;
            else HelloServlet.b = true;
            HelloServlet.counter++;
            out.println("<h3> Servlet1.ast + Servlet1.b :" + HelloServlet.ast + HelloServlet.b + "</h3>");
            out.println("<h3> Servlet1.counter : " + HelloServlet.counter + "</h3>");
            String toSize = request.getParameter("size");
            if (toSize != null && Integer.parseInt(toSize) > 0 && Integer.parseInt(toSize) < 6) {
                size = Byte.parseByte(toSize);
            }
            out.println("<h3>Результаты:</h3>");
            out.println("<h" + HelloServlet.size + ">");
            String args = request.getParameter("args");
            if (args != null) {
                storage.add(new AbstractMap.SimpleEntry<>(args, sort_func(args.split(" "))));
            }

            for (Map.Entry<String, String> elem : storage) {
                out.println(elem.getKey() + " " + elem.getValue());
                out.println("<p></p>");
            }

            if (size == min_size) {
                out.println("<h3> Достигунт лимит шрифта.</h3>");
            } else {
                size++;
            }
            out.println("</body>");
            out.println("</html>");
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private String sort_func(String[] args) {
        ArrayList<Integer> al = new ArrayList<>();
        for (String x : args) {
            al.add((Integer.parseInt(x)));
        }
        Collections.sort(al, Collections.reverseOrder());
        return String.valueOf(al);
    }
}
