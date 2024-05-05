package lab3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class InputFromConsoleObserver implements Observer{
    String logPath = "";

    public InputFromConsoleObserver(String logPath)
    {
        this.logPath = logPath;
    }

    @Override
    public void update(Observable obs, Object arg)
    {
        File file = new File(logPath);
            try
            {
                FileWriter fileWriter = new FileWriter(file, true);
                fileWriter.write("Обращение к потоку ввода с консоли\n");
                fileWriter.close();
            }
            catch (IOException e)
            {
                System.out.println("Вы ввели неправильный путь до журнала");

            }
    }
}
