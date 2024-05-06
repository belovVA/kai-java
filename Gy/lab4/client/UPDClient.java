package lab4.client;

import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;
import java.util.ArrayList;

public class UPDClient {
    private static String ID;
    private static final String PropertiesPath = "/Users/zubatshr/kai-java/lab4/client/config.properties";
    private static String HOST = "";
    private static int PORT;
    private static String JOURNAL_PATH = "";

    public static void main (String[] args) {
        Scanner in = new Scanner(System.in);

        if (get_settings(in) == 1){
            return;
        }

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
        } finally {
            in.close();
        }
    }

    private static void getID(Scanner in, FileEditorClient FEditor){
        while (true){
            System.out.print("Введите ID клиента: ");
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
            System.out.print("Введите выражение:");
            req = in.nextLine();
            while (Objects.equals(req, "")){
                System.out.print("Вы ввели пустое выражение, Введите выражение:");
                req = in.nextLine();
            }
        }
        return req;
    }

    private static int  getOperation(Scanner in) {
        while (true) {
            System.out.print("\n1. Ввести выражение\n" +
                    "0. Выход\n");
            System.out.print("Выберите действие ---> ");
            String ans = in.nextLine();
            if (ans.equals("1") || ans.equals("0")) {
                return Integer.parseInt(ans);
            } else {
                System.out.print("Введен неверный вид операции\n");
            }
        }
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

    private static int get_settings(Scanner in){
        int flag;
        try {
            System.out.print("Введите Адрес сервера: ");
            HOST = in.nextLine();

            System.out.print("Введите Порт сервера: ");
            PORT = in.nextInt();
            in.nextLine();

            System.out.print("Введите Абсолтный путь до Журнала логов: ");
            JOURNAL_PATH = in.nextLine();

            // Потом убрать
            JOURNAL_PATH = "C:\\Users\\Vladimir\\Documents\\user\\3course\\2sem\\java\\Gy\\lab4\\client\\log.txt";

            System.out.printf("Клиент запущен. Адрес сервера: %s, Порт сервера %d\n", HOST, PORT);
            flag =  0;
        } catch (NumberFormatException e) {
            System.err.println("Неверный формат ввода Порта: " + e.getMessage());
            flag =  1;
        } catch (Exception e) {
            System.err.println("Что-то пошло не так");
            flag =  1;
        }
        return flag;
    }

}
