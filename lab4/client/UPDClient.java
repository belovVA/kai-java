package lab4.client;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.ArrayList;

public class UPDClient {
    private static String ID;
    private static final String PropertiesPath = "C:/Users/Vladimir/Documents/user/3course/2sem/java/lab4/client/config.properties";
    private static String HOST = "";
    private static int PORT;
    private static String JOURNAL_PATH = "";

    public static void main (String[] args) {
        Scanner in = new Scanner(System.in);

        if (get_settings(args) == 1){
            return;
        }
        System.out.println(JOURNAL_PATH);

        FileEditorClient FEditor = new FileEditorClient(JOURNAL_PATH);

        getID(in, FEditor);
        try {
            InetAddress addr = InetAddress.getByName(HOST);
            DatagramSocket socket = new DatagramSocket();
            Scanner get = new Scanner(System.in);

            int operation = 1;
            while (true){

                operation = getOperation(get);
                if (operation == 0){
                    break;
                }
                // Создание запроса
                String reqValues = "";
                reqValues =  getRequest(operation);
                String request = (ID + "," +  operation + ","  + reqValues);
                StringBuilder sb = new StringBuilder(request);

                // При необходимости удаление последней запятой
                if (sb.length() > 0 & (sb.lastIndexOf(",") == sb.length() - 1) ) {
                    sb.deleteCharAt(sb.length() - 1); // Удаляем последний символ
                }
                request = sb.toString();

                // Формирование и отправка пакета
                byte[] data = (request).getBytes();
                DatagramPacket packet2 = new DatagramPacket(data, data.length, addr, PORT);
                socket.send(packet2);

                FEditor.save("Отправлено на сервер:"+(new String(packet2.getData())).trim());
                System.out.println("Отправлено на сервер:"+(new String(packet2.getData())).trim());

                // Получаем ответ от сервера
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                // Преобразуем полученные данные в строку и выводим
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                System.out.println("Получено от сервера:\n" + receivedMessage);
                FEditor.save("Получено от сервера:\n" + receivedMessage);

            }
            close_socket_on_server(socket, ID, FEditor);
            socket.close();
            get.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getID(Scanner in, FileEditorClient FEditor){
        while (true){
            System.out.print("Введите ID сервера: ");
            String strID = in.nextLine();
            try{
                int temp = Integer.parseInt(strID);
            }catch (NumberFormatException ex) {
                System.out.println("ID может содержать только цифры");
                continue;
            }
            try {
                InetAddress addr = InetAddress.getByName(HOST);
                DatagramSocket socket = new DatagramSocket();
                String request = "_" + strID;
                byte[] data2 = (request).getBytes();
                DatagramPacket packet2 = new DatagramPacket(data2, data2.length, addr, PORT);
                socket.send(packet2);

                FEditor.save("Отправлено на сервер:"+(new String(packet2.getData())).trim());
                System.out.println("Отправлено на сервер:"+(new String(packet2.getData())).trim());

                // Получаем ответ от сервера
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                // Преобразуем полученные данные в строку и выводим
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                System.out.println("Получено от сервера:\n" + receivedMessage);
                if (receivedMessage.equals("true")){
                    ID = "_"+ strID;
                    socket.close();
                    return;
                } else {

                    System.out.println("Данный ID клиента уже занят");
                }

            } catch (SocketException e) {
            e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    private static String getRequest(int operation) {
        ArrayList<Integer> arrayTypes = new ArrayList<>();
        String req = "";
        int massType = 1;
        if (operation != 0){
            Scanner in = new Scanner(System.in);
            int indexRow, indexColumn;
            while (massType != 0){
                massType = getMassType(in);
                if (massType != 0){
                    arrayTypes.add(massType);
                }
                if (massType != 0 && operation != 3){
                    indexRow = getIndex(in, "строки");
                    indexColumn = getIndex(in, "столбца");
                    req += String.format("%d,%d,%d,", massType, indexRow, indexColumn);
                } else if(massType != 0 ){
                    req += String.format("%d", massType);
                    massType = 0;
                }
            }
            if (!arrayTypes.isEmpty()){
                boolean match = arrayTypes.stream().allMatch(s -> s.equals(arrayTypes.get(0)));
                if (match){
                    massType = arrayTypes.get(0);
                    req += String.format("%s", getNewValue(in, operation, massType));
                } else if (operation == 2){
                    System.out.println("Вы ввели разные типы массивов. Введенные ячейки будут изменены на значения по умолчанию");
                }
            }

        }
        return req;
    }

    private static int  getOperation(Scanner in) {
        while (true) {
            System.out.print("\n1. Посмотреть данные\n" +
                    "2. Записать данные\n" +
                    "3. Посмотреть размерность массива\n" +
                    "0. Выход\n");
            System.out.print("Выберите действие ---> ");
            String ans = in.nextLine();
            if (ans.equals("1") || ans.equals("2") || ans.equals("0") || ans.equals("3")) {
                return Integer.parseInt(ans);
            } else {
                System.out.print("Введен неверный вид операции\n");
            }
        }
    }

    private static int getMassType(Scanner in) {
        while (true) {
            System.out.print("1. Целочисленный\n" +
                    "2. Вещественный\n" +
                    "3. Строковый\n" +
                    "0. Выход\n");
            System.out.print("Выберите действие ---> ");
            String ans = in.nextLine();
            if (ans.equals("0") || ans.equals("1") || ans.equals("2") || ans.equals("3")) {
                return Integer.parseInt(ans);
            } else {
                System.out.print("Введен неверный тип массива\n");
            }
        }
    }

    private static int getIndex(Scanner in, String name) {
        while (true) {
            System.out.format("Введите индекс " + name + " от 0 до 9. При вводе -1 выведется полностью строка/столбец \n", name);
            System.out.print("Индекс строки --->");
            String ans = in.nextLine();
            if (Integer.parseInt(ans) >= -1 && Integer.parseInt(ans) <= 9) {
                return Integer.parseInt(ans);
            } else {
                System.out.print("Индекс должен быть в пределах [0,9]\n");
            }
        }
    }

    private static String getNewValue(Scanner in, int operation, int MassType) {
        System.out.println(MassType);
        if (operation != 2 ){
            return "";
        }
        String req="";

        try{
            while (true) {
                System.out.print("Введите новое значение ячейки\n");
                System.out.print("Значение ячейки--->");
                String ans = in.nextLine();

                if (MassType == 1) {
                    int newValue = Integer.parseInt(ans);
                    req += newValue;
                    break;
                } else if (MassType == 2) {
                    float newValue = Float.parseFloat(ans);
                    req += newValue;
                    break;
                } else if (MassType == 3) {
                    req += ans;
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Введен неверный тип данных нового значения");
        }
        return req;
    }

    private static void close_socket_on_server(DatagramSocket socket, String ID, FileEditorClient FEditor){
        String toThrow = String.format("%s, 0", ID);
        try {
            InetAddress addr = InetAddress.getByName(HOST);
            byte[] data2 = (toThrow).getBytes();
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length, addr, PORT);
            socket.send(packet2);

            FEditor.save("Отправлено на сервер:"+(new String(packet2.getData())).trim());
            System.out.println("Отправлено на сервер:"+(new String(packet2.getData())).trim());

            // Получаем ответ от сервера
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            // Преобразуем полученные данные в строку и выводим
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
            System.out.println("Получено от сервера:\n" + receivedMessage);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int get_settings(String[] args){
        try {
            HOST = args[0];
            PORT = Integer.parseInt(args[1]);
            System.out.printf("Клиент запущен. Адрес сервера: %s, Порт сервера %d\n", HOST, PORT);

           get_path_from_file();
            return 0;
        } catch (NumberFormatException e) {
            System.err.println("Неверный формат ввода Порта: " + e.getMessage());
            return 1;
        } catch (IOException e){
            System.err.println("Не удалось считать путь до журнала");
            return 1;
        } catch (Exception e) {
            System.err.println("Количество аргументов меньше 2х");
            return 1;
        }
    }

    private static void get_path_from_file() throws IOException{
        Properties pros;
        pros = new Properties();
        FileInputStream ip = new FileInputStream(PropertiesPath);
        pros.load(ip);
        JOURNAL_PATH = (pros.getProperty("LogFilePath"));

    }
}
