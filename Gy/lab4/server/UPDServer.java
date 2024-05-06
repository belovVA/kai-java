
import java.io.*;
import java.net.*;
import java.util.*;

public class UPDServer {
    private static final ArrayList<String> IDClients = new ArrayList<>();
    private static final int LENGTH_PACKET = 1024;
    private static int PORT;
    private static String JOURNAL_PATH;

    public static void main(String[] args) {
        if (!Arrays.asList(args).isEmpty())
            JOURNAL_PATH = args[0];

        FileEditor FEditor = new FileEditor(JOURNAL_PATH);

        DatagramSocket servSocket = null;
        DatagramPacket datagram;
        InetAddress clientAddr;
        int clientPort;
        byte[] data;

        if (get_settings(args) == 1) {
            return;
        }
        try {
            servSocket = new DatagramSocket(PORT);
            System.out.println("Ожидание пакетов от клиента...");

            while (true) {
                byte[] operationData = new byte[LENGTH_PACKET];
                DatagramPacket operationPacket = new DatagramPacket(operationData, operationData.length);
                servSocket.receive(operationPacket);
                String operation = new String(operationPacket.getData(), 0, operationPacket.getLength()).replaceAll(" ", "");
                String[] operation_split = operation.split(",");

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
                    System.out.printf("Принято от клиента (id = %s):\n %s\n", operation_split[0], operation);
                    FEditor.save(String.format("Принято от клиента (id = %s):\n %s\n", operation_split[0], operation));
                    toThrow = request_processing(operation);
                }

                clientAddr = operationPacket.getAddress();
                clientPort = operationPacket.getPort();

                operationPacket.setData(((toThrow).trim()).getBytes());
                data = operationPacket.getData();
                datagram = new DatagramPacket(data, data.length, clientAddr, clientPort);
                servSocket.send(datagram);
                System.out.printf("Отправлено клиенту (id = %s):\n", operation_split[0]);
                FEditor.save(String.format("Отправлено клиенту (id = %s):\n", operation_split[0]));
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

    private static String check_ID_client(String request) {
        if (IDClients.contains(request)) {
            return String.valueOf(false);
        } else {
            IDClients.add(request);
            return String.valueOf(true);
        }
    }

    private static String request_processing(String request) {
        String toThrow = "";
        ParserStr parser = new ParserStr(request);
        try {
            parser.parse(); // Парсим строку
            List<Object> tokens = parser.getTokens(); // Получаем список токенов
            System.out.println("Токены после парсинга:");
            for (Object token : tokens) {
                System.out.println(token);
            }

            Arithmetic arithmetic = new Arithmetic(tokens);
            arithmetic.evaluate(); // Выполняем вычисления
            double result = arithmetic.getResult(); // Получаем результат
            toThrow =  "Результат вычисления: " + result;
        } catch (IllegalArgumentException e) {
            toThrow = "Ошибка: " + e.getMessage();
        } catch (ArithmeticException e) {
            toThrow =  "Ошибка арифметики: " + e.getMessage();
        }
        return toThrow;
    }

    private static void getSendData(byte[] b, FileEditor FEditor) {
        System.out.println(new String(b));
        FEditor.save(new String(b));
        byte[] result = new byte[b.length];
        System.arraycopy(b, 0, result, 0, b.length);
    }
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

    private static String remove_ID_client(String id){
        String toThrow = "";
        if (IDClients.remove(id)){
            toThrow = String.format("Соединение с клиентом (id = %s) завершено", id);
        } else {
            toThrow = String.format(" клиент (id = %s) не найден из работающих ID", id);
        }
        return toThrow;
    }
}

