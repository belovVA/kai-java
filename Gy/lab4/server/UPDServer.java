
import java.io.*;
import java.net.*;
import java.util.*;

public class UPDServer {
    private static final ArrayList<String> IDClients = new ArrayList<>();
    private static final int LENGTH_PACKET = 1024;
    private static int PORT;
    private static String JOURNAL_PATH;

    public static void main(String[] args) {
        DatagramSocket servSocket = null;
        DatagramPacket datagram;
        InetAddress clientAddr;
        int clientPort;
        byte[] data;

        if (get_settings(args) == 1) {
            return;
        }
        FileEditor FEditor = new FileEditor(JOURNAL_PATH);

        try {
            servSocket = new DatagramSocket(PORT);
            System.out.println("Ожидание пакетов от клиента...");

            while (true) {
                // Получение пакета
                byte[] operationData = new byte[LENGTH_PACKET];
                DatagramPacket operationPacket = new DatagramPacket(operationData, operationData.length);
                servSocket.receive(operationPacket);
                // Формирование строки из пакета
                String operation = new String(operationPacket.getData(), 0, operationPacket.getLength()).replaceAll(" ", "");
                String[] operation_split = operation.split(",");

                // Работа со строкой
                String toThrow = "";
                if (operation_split.length == 1) {
                    // Добавление id клиента
                    System.out.printf("Принято от клиента: %s\n", operation);
                    FEditor.save(String.format("Принято от клиента: %s\n", operation));
                    toThrow = check_ID_client(operation_split[0]);
                } else if (Integer.parseInt(operation_split[1]) == 0){
                    // закрытие id
                    toThrow = remove_ID_client(operation_split[0]);

                } else{
                    System.out.printf("Принято от клиента (id = %s): %s\n", operation_split[0], operation);
                    FEditor.save(String.format("Принято от клиента (id = %s): %s", operation_split[0], operation));
                    operation = get_arithmetic_expression(operation);
                    toThrow = request_processing(operation);
                }

                // Получение адреса и порта клиента из полученного пакета
                clientAddr = operationPacket.getAddress();
                clientPort = operationPacket.getPort();

                // Отправка и вывод результата
                operationPacket.setData(((toThrow).trim()).getBytes());
                data = operationPacket.getData();
                datagram = new DatagramPacket(data, data.length, clientAddr, clientPort);
                servSocket.send(datagram);
                System.out.printf("Отправлено клиенту (id = %s):", operation_split[0]);
                FEditor.save(String.format("Отправлено клиенту (id = %s):", operation_split[0]));
                getSendData(data, FEditor);
            }
        } catch (SocketException e) {
            System.err.println("Ошибка при создании сокета: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        } finally {
            if (servSocket != null) {
                servSocket.close();
            }
        }
    }

    // Проверка ID на валидность
    private static String check_ID_client(String request) {
        if (IDClients.contains(request)) {
            return String.valueOf(false);
        } else {
            IDClients.add(request);
            return String.valueOf(true);
        }
    }

    // Работа со строкой запроса и получение результата
    private static String request_processing(String request) {
        String toThrow = "";
        ParserStr parser = new ParserStr(request);
        try {
            parser.parse(); // Парсим строку
            List<Object> tokens = parser.getTokens(); // Получаем список токенов

            Arithmetic arithmetic = new Arithmetic(tokens);
            arithmetic.evaluate(); // Выполняем вычисления

            double result = arithmetic.getResult(); // Получаем результат
            toThrow =  "Результат вычисления: " + request  + " = " + result;
        } catch (IllegalArgumentException e) {
            toThrow = "Ошибка: " + e.getMessage();
        } catch (ArithmeticException e) {
            toThrow =  "Ошибка арифметики: " + e.getMessage();
        }
        return toThrow;
    }

    // Функция вывода содержимого отправленного пакета данных
    private static void getSendData(byte[] b, FileEditor FEditor) {
        System.out.println(new String(b));
        FEditor.save(new String(b));
        byte[] result = new byte[b.length];
        System.arraycopy(b, 0, result, 0, b.length);
    }

    // Получение настроек при запуске сервера
    private static int get_settings(String[] args) {
        Scanner in = new Scanner(System.in);
        int flag;
        try {
            System.out.print("Введите Порт сервера: ");
            PORT = Integer.parseInt(in.nextLine());

            JOURNAL_PATH = args[0];

            System.out.printf("Сервер запущен. Порт сервера %d\n", PORT);
            flag =  0;
        } catch (NumberFormatException e) {
            System.err.println("Неверный формат ввода Порта: " + e.getMessage());
            flag =  1;
        } catch (Exception e) {
            System.err.println("Количество аргументов меньше 1го");
            flag =  1;
        } finally {
            in.close();

        }
        return flag;
    }

    // удаление ID клиента из списка при закрытии сокета
    private static String remove_ID_client(String id){
        String toThrow = "";
        if (IDClients.remove(id)){
            toThrow = String.format("Соединение с клиентом (id = %s) завершено", id);
        } else {
            toThrow = String.format(" клиент (id = %s) не найден из работающих ID", id);
        }
        return toThrow;
    }

    // Переделка запроса в арифм выржение путем отсечения начальной части строки
    private static String get_arithmetic_expression(String request){
        int firstCommaIndex = request.indexOf(',');
        String result = "";
        if (firstCommaIndex != -1) {
            // Находим позицию второго вхождения ',' начиная с позиции после первого вхождения
            int secondCommaIndex = request.indexOf(',', firstCommaIndex + 1);

            if (secondCommaIndex != -1) {
                // Обрезаем строку правее второго вхождения ','
                 result = request.substring(secondCommaIndex + 1);
            } else {
                System.err.println("Второго вхождения ',' не найдено");
            }
        } else {
            System.err.println("Первого вхождения ',' не найдено");
        }
        return result;
    }
}

