package lab4.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class UPDServer {
    private static final ArrayList <String> IDClients = new ArrayList<>();
    private static final ArrayEditor editor = new ArrayEditor();
    private static final int LENGTH_PACKET = 1024;
    private static int PORT;
    private static String JOURNAL_PATH;
    private static final int INT_DEFAULT= 0;
    private static final float FLOAT_DEFAULT = 0.0f;
    private static final String STRING_DEFAULT = "DEFAULT";

    public static void main(String[] args) {
        if (!Arrays.asList(args).isEmpty())
            JOURNAL_PATH = args[0];

        FileEditor FEditor = new FileEditor(JOURNAL_PATH);

        DatagramSocket servSocket = null;
        DatagramPacket datagram;
        InetAddress clientAddr;
        int clientPort;
        byte[] data;

        Scanner in = new Scanner(System.in);

        if (get_settings(args, in) != 0){
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
               if (operation_split.length == 1){
                    // Добавление id клиента
                   System.out.printf("Принято от клиента: %s\n", operation);
                   FEditor.save(String.format("Принято от клиента: %s\n", operation));
                   toThrow = check_ID_client(operation_split[0]);
               } else{
                    System.out.printf("Принято от клиента (id = %s):\n %s\n", operation_split[0], operation);
                    FEditor.save(String.format("Принято от клиента (id = %s):\n %s\n", operation_split[0], operation));
                    System.out.println(operation_split[1]);
                    if (Integer.parseInt(operation_split[1]) == 0){
                        // закрытие id
                        toThrow = remove_ID_client(operation_split[0]);

                    } else  if (!(Integer.parseInt(operation_split[1]) == 3 & operation_split.length == 2)){
                        toThrow = request_processing(operation_split);
                    }

                }

                clientAddr = operationPacket.getAddress();
                clientPort = operationPacket.getPort();

                operationPacket.setData(((toThrow).trim()).getBytes());
                data = operationPacket.getData();
                datagram = new DatagramPacket(data, data.length, clientAddr, clientPort);
                servSocket.send(datagram);
                System.out.printf("Отправлено клиенту (id = %s):\n",operation_split[0]);
                FEditor.save(String.format("Отправлено клиенту (id = %s):\n",operation_split[0]));
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

    private static String check_ID_client(String request){
        if (IDClients.contains(request)){
            return String.valueOf(false);
        } else {
            IDClients.add(request);
            return String.valueOf(true);
        }
    }

    private static String request_processing(String[] request){
        String toThrow = "";
        int operation = Integer.parseInt(request[1]);
        String [] content;
        content = Arrays.copyOfRange(request,2,request.length);

        switch (operation) {
            case (1):
                toThrow = read_date(content);
                break;
            case (2):
                toThrow += change_values(content);
                break;
            case (3):
                switch (content[0]) {
                    case ("1"):
                        toThrow = (Arrays.toString(editor.getLength("int")));
                        break;
                    case ("2"):
                        toThrow = Arrays.toString(editor.getLength("float"));
                        break;
                    case ("3"):
                        toThrow = Arrays.toString(editor.getLength("string"));
                        break;
                }
                toThrow = toThrow.substring(1, toThrow.length() - 1);
                break;
        }
        return toThrow;
    }
    private static void getSendData(byte[] b, FileEditor FEditor){
        System.out.println(new String(b));
        FEditor.save(new String(b));
        byte[] result = new byte[b.length];
        System.arraycopy(b, 0, result, 0, b.length);
    }

    private static String read_date(String[] ans_split){
        String toThrow = "";
        int len = (ans_split.length);
        for (int i = 0; i < len - 2; i += 3 ){
            switch (ans_split[i]) {
                case ("1"):
                    toThrow += read_date_int(Integer.parseInt(ans_split[i + 1]), Integer.parseInt(ans_split[i + 2]));
                    break;
                case ("2"):
                    toThrow += read_date_float(Integer.parseInt(ans_split[i + 1]), Integer.parseInt(ans_split[i + 2]));
                    break;
                case ("3"):
                    toThrow += read_date_string(Integer.parseInt(ans_split[i + 1]), Integer.parseInt(ans_split[i + 2]));
                    break;
            }
        }

        return toThrow;
    }

    private static String read_date_int(int row, int column){
        String toThrow = "";
        if (row == -1 ^ column == -1){
            int[] arr = new int[10];
            String r = "", c = "";
            for (int k = 0; k < 10;k++){
                int i, j;
                if (row == -1){
                    i = k;
                    j = column;
                    c = String.valueOf(column);
                } else {
                    i = row;
                    j = k;
                    r = String.valueOf(row);
                }
                arr[k] = editor.getInt(i, j);
            }
            toThrow = String.format("arrInt[%s][%s] = ", r, c) + Arrays.toString(arr) + "\n" ;
        } else if (row == -1){
            int [][] arr = editor.getIntArray();
            toThrow = "arrInt[][]:\n" ;
            for (int i = 0; i < 10; i++){
                int[] tempArr = new int[10];
                System.arraycopy(arr[i], 0, tempArr, 0, 10);
                toThrow +=  Arrays.toString(tempArr) + '\n';
            }
        } else {
            toThrow = String.format("arrInt[%d][%d] = %d\n", row, column, editor.getInt(row, column));
        }
        return toThrow;
    }

    private static String read_date_float(int row, int column){
        String toThrow = "";
        if (row == -1 ^ column == -1){
            float[] arr = new float[10];
            String r = "", c = "";
            for (int k = 0; k < 10;k++){
                int i, j;
                if (row == -1){
                    i = k;
                    j = column;
                    c = String.valueOf(column);
                } else {
                    i = row;
                    j = k;
                    r = String.valueOf(row);
                }
                arr[k] = editor.getFloat(i, j);
            }
            toThrow = String.format("arrFloat[%s][%s] = ", r, c) + Arrays.toString(arr) + "\n" ;
        } else if (row == -1){
            toThrow = "arrFloat[][]:\n" ;
            float [][] arr = editor.getFloatArray();
            for (int i = 0; i < 10; i++){
                float[] tempArr = new float[10];
                for (int j = 0; j < 10; j++){
                    tempArr[j] = arr[i][j];
                }
                toThrow += Arrays.toString(tempArr) + '\n';
            }
        } else {
            toThrow = String.format("arrFloat[%d][%d] = %f\n", row, column, editor.getFloat(row, column));
        }
        return toThrow;
    }

    private static String read_date_string(int row, int column){
        String toThrow = "";
        if (row == -1 ^ column == -1){
            String[] arr = new String[10];
            String r = "", c = "";
            for (int k = 0; k < 10;k++){
                int i, j;
                if (row == -1){
                    i = k;
                    j = column;
                    c = String.valueOf(column);
                } else {
                    i = row;
                    j = k;
                    r = String.valueOf(row);
                }
                arr[k] = editor.getString(i, j);
            }
            toThrow = String.format("arrStr[%s][%s] = ", r, c) + Arrays.toString(arr) + "\n" ;
        } else if (row == -1){
            String [][] arr = editor.getStrArray();
            toThrow = "arrStr[][]:\n" ;
            for (int i = 0; i < 10; i++){
                String[] tempArr = new String[10];
                for (int j = 0; j < 10; j++){
                    tempArr[j] = arr[i][j];
                }
                toThrow += Arrays.toString(tempArr) + '\n';
            }
        } else {
            toThrow = String.format("arrStr[%d][%d] = %s\n", row, column, editor.getString(row, column));
        }
        return toThrow;
    }

    private static String change_values(String[] ans_split){
        String toThrow = "";

        int len = (ans_split.length);
        String newValue = ans_split[len - 1];

        for (int i = 0; i < len - 2; i += 3 ) {
            switch (ans_split[i]) {
                case ("1") -> {
                    if (ans_split.length % 3 == 0) {
                        newValue = String.valueOf(INT_DEFAULT);
                    }
                    toThrow += change_value_int(Integer.parseInt(ans_split[i + 1]),
                            Integer.parseInt(ans_split[i + 2]),
                            Integer.parseInt(newValue));
                }
                case ("2") -> {
                    if (ans_split.length % 3 == 0) {
                        newValue = String.valueOf(FLOAT_DEFAULT);
                    }
                    toThrow += change_value_float(Integer.parseInt(ans_split[i + 1]),
                            Integer.parseInt(ans_split[i + 2]),
                            Float.parseFloat(newValue));
                }
                case ("3") -> {
                    if (ans_split.length % 3 == 0) {
                        newValue = STRING_DEFAULT;
                    }
                    toThrow += change_value_string(Integer.parseInt(ans_split[i + 1]),
                            Integer.parseInt(ans_split[i + 2]),
                            newValue);
                }
            }
        }
        return  toThrow;
    }

    private static String change_value_int(int row, int column, int newValue){
        String toThrow = String.format("До изменений: %s\n", read_date_int(row, column));

        if (row == -1 ^ column == -1){
            for (int k = 0; k < 10;k++){
                int i, j;
                if (row == -1){
                    i = k;
                    j = column;
                } else {
                    i = row;
                    j = k;
                }
                editor.setInt(i, j, newValue);
            }
        } else if (row == -1){
            for (int i = 0; i < 10; i++){
                for (int j = 0; j < 10; j++){
                    editor.setInt(i, j, newValue);
                }
            }
        } else {
            editor.setInt(row, column, newValue);
             toThrow += String.format("После изменений: %s\n", read_date_int(row, column));
        }
        return toThrow;

    }

    private static String change_value_float(int row, int column, float newValue){
        String toThrow = String.format("До изменений: %s\n", read_date_float(row, column));

        if (row == -1 ^ column == -1){
            for (int k = 0; k < 10;k++){
                int i, j;
                if (row == -1){
                    i = k;
                    j = column;
                } else {
                    i = row;
                    j = k;
                }
                editor.setFloat(i, j, newValue);
            }
        } else if (row == -1){
            for (int i = 0; i < 10; i++){
                for (int j = 0; j < 10; j++){
                    editor.setFloat(i, j, newValue);
                }
            }
        } else {
            editor.setFloat(row, column, newValue);
            toThrow += String.format("После изменений: %s\n", read_date_float(row, column));
        }
        return toThrow;

    }

    private static String change_value_string(int row, int column, String newValue){
        String toThrow = String.format("До изменений: %s\n", read_date_string(row, column));

        if (row == -1 ^ column == -1){
            for (int k = 0; k < 10;k++){
                int i, j;
                if (row == -1){
                    i = k;
                    j = column;
                } else {
                    i = row;
                    j = k;
                }
                editor.setString(i, j, newValue);
            }
        } else if (row == -1){
            for (int i = 0; i < 10; i++){
                for (int j = 0; j < 10; j++){
                    editor.setString(i, j, newValue);
                }
            }
        } else {
            editor.setString(row, column, newValue);
            toThrow += String.format("После изменений: %s\n", read_date_string(row, column));
        }
        return toThrow;

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

    private static int get_settings(String[] args, Scanner in) {
        try {
            System.out.print("Введите порт: ");
                PORT = Integer.parseInt(in.nextLine());

            JOURNAL_PATH = args[0];
            System.out.printf("Сервер запущен. Порт сервера %d\n", PORT);
            return 0;

        } catch (NumberFormatException e) {
            System.err.println("Неверный формат ввода: " + e.getMessage());
            return 1;
        }  catch (Exception e) {
            System.err.println("Количество аргументов меньше 1го");
            return 1;
        }
    }
}
