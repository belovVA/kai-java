package lab3;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Boolean checkFileInput = false, checkFileLog = false;
        String FileLogPath = "", FileInputPath = "";

        while (!checkFileInput || !checkFileLog){
            if (!checkFileInput) {
                System.out.print("Введите путь до файла: \n");
                System.out.println("Обращение к потоку вывода на консоль");

                FileInputPath = input.nextLine();
                File fileInput = new File(FileInputPath);
                System.out.println("Обращение к потоку ввода с консоли");

                if (fileInput.exists()) {
                    checkFileInput = true;
                }
            }

            if (!checkFileLog) {
                System.out.print("Введите путь до журнала: \n");
                System.out.println("Обращение к потоку вывода на консоль");

                FileLogPath = input.nextLine();
                File fileLog = new File(FileLogPath);
                System.out.println("Обращение к потоку ввода с консоли");

                if (fileLog.exists()){
                    checkFileLog = true;
                }
            }

            if (!checkFileInput || !checkFileLog){
                continue;
            }

            OutputToConsoleObserver outputToConsoleObserver = new OutputToConsoleObserver(FileLogPath);
            OutputToConsoleObservable outputToConsoleObservable = new OutputToConsoleObservable();
            outputToConsoleObservable.addObserver(outputToConsoleObserver);

            InputFromConsoleObserver inputFromConsoleObserver = new InputFromConsoleObserver(FileLogPath);
            InputFromConsoleObservable inputFromConsoleObservable = new InputFromConsoleObservable();
            inputFromConsoleObservable.addObserver(inputFromConsoleObserver);

            IsEqualObserver isEqualObserver = new IsEqualObserver(FileLogPath);
            IsEqualObservable isEqualObservable = new IsEqualObservable();
            isEqualObservable.addObserver(isEqualObserver);

            outputToConsoleObservable.notifyObs();
            inputFromConsoleObservable.notifyObs();
            outputToConsoleObservable.notifyObs();
            inputFromConsoleObservable.notifyObs();

            File fileInput = new File(FileInputPath);
            File fileJournal = new File(FileLogPath);
            try{
                BufferedReader reader = new BufferedReader(new FileReader(fileInput));
                String[] numbersStr = reader.readLine().split(" ");
                int sumOddPositive = 0;
                int sumEvenNegative = 0;

                for (String numStr : numbersStr) {
                    int number = Integer.parseInt(numStr);

                    if (number == IConstants.ValueOfNumb){
                        System.out.println("Элемент массива равен "+ IConstants.ValueOfNumb);
                        isEqualObservable.notifyObs();

                    }

                    if (number % 2 != 0 && number > 0) {
                        sumOddPositive += number;
                    } else if (number % 2 == 0 && number < 0) {
                        sumEvenNegative += number;
                    }
                }

                FileWriter fileWriter = new FileWriter(fileJournal,true);

                System.out.println("Сумма <нечетных и положительных> чисел посл-ти: " + sumOddPositive);
                fileWriter.write("Сумма <нечетных и положительных> чисел посл-ти: " + sumOddPositive + "\n");
                System.out.println("Обращение к потоку вывода на консоль");
                outputToConsoleObservable.notifyObs();

                System.out.println("Сумма <четных и отрицательных> чисел посл-ти: " + sumEvenNegative);
                fileWriter.write("Сумма <четных и отрицательных> чисел посл-ти: " + sumEvenNegative + "\n");
                System.out.println("Обращение к потоку вывода на консоль");
                outputToConsoleObservable.notifyObs();


                fileWriter.close();
                reader.close();
            }
            catch (IOException e)
            {
                System.out.println("You entered the wrong path to the input file or log file, please repeat: ");
            }
        }
        input.close();
    }
}
