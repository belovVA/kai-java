
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import static java.lang.String.*;

public class Test extends JFrame {
    private DemoPanel demoPanel;
    private JTextField  contentField, newContentField;
    private JComboBox<String> selectComboBox, colorComboBox, newcolorComboBox, speedComboBox, newspeedComboBox;
    private Timer timer;
    private Set<Integer> usedNumbers = new HashSet<>();
    private final Set<String> validContents = Set.of("image");


    public Test() {
        setTitle("Управляющее окно");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1)); // Используем GridLayout для размещения компонентов по одной строке

        contentField = new JTextField(10);
        add(new JLabel("Содержимое:"));
        add(contentField);

        String[] colors = { "Синий", "Красный", "Зеленый", "Черный", "Желтый"};
        colorComboBox = new JComboBox<>(colors);
        add(new JLabel("Цвет:"));
        add(colorComboBox);

        String[] speeds = {"1", "2", "3", "5", "10", "15"};
        speedComboBox = new JComboBox<>(speeds);
        add(new JLabel("Скорость:"));
        add(speedComboBox);

        JButton startButton = new JButton("Пуск");
        startButton.addActionListener(e -> startFigure());
        add(startButton);

        selectComboBox = new JComboBox<>();
        selectComboBox.addItem(""); // Добавляем пустую строку
        add(new JLabel("Выбор фигуры:"));
        add(selectComboBox);

        JButton showButton = new JButton("Изменить");
        showButton.addActionListener(e -> loadFigureData());
        add(showButton);
        newContentField = new JTextField(10);
        add(new JLabel("Содержимое:"));
        add(newContentField);

        newspeedComboBox = new JComboBox<>(speeds);
        add(new JLabel("Скорость:"));
        add(newspeedComboBox);


        newcolorComboBox = new JComboBox<>(colors);
        add(new JLabel("Цвет:"));
        add(newcolorComboBox);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> editFigure());
        add(saveButton);


        demoPanel = new DemoPanel();
        JFrame demoFrame = new JFrame("Демонстрационное окно");
        demoFrame.setSize(400, 400);
        demoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demoFrame.setResizable(false); // Запрещаем изменение размеров окна
        demoFrame.add(demoPanel);
        demoFrame.setVisible(true);

        timer = new Timer(30, e -> demoPanel.updateFigures());
        timer.start();
    }

    private void startFigure() {
        String content = contentField.getText();
        if (Objects.equals(content, "")){
            JOptionPane.showMessageDialog(this, "Поле содержимого не должно быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean isImage = validContents.contains(content);
        int number = DemoPanel.getId();
        int speed = Integer.parseInt((String) speedComboBox.getSelectedItem());
        Color color = getColorFromName((String) colorComboBox.getSelectedItem());

        Random random = new Random();
        int dx = random.nextInt(21) - 10; // Генерируем случайное значение dx в диапазоне [-10, 10]
        int dy = random.nextInt(21) - 10; // Генерируем случайное значение dy в диапазоне [-10, 10]

        while (dx * dx + dy * dy > 100) {
            dx = random.nextInt(21) - 10;
            dy = random.nextInt(21) - 10;
        }
        dx *= speed;
        dy *= speed;
        Figure figure = new Figure(0, 0, dx, dy, 30, color, content, number, isImage);

        demoPanel.addFigure(figure);
        usedNumbers.add(number);
        selectComboBox.addItem(valueOf(number));
        demoPanel.incId();
        clearFields(); // Очищаем текстовые поля после добавления объекта
        demoPanel.repaint(); // Обновляем отрисовку панели
    }

    private void editFigure() {
        int number;
        try {
            number = Integer.parseInt((String) selectComboBox.getSelectedItem());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Номер должен быть числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!usedNumbers.contains(number)) {
            JOptionPane.showMessageDialog(this, "Номер не используется", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Figure figure = demoPanel.getFigureByNumber(number);

        if (figure != null) {
            int  newSpeed = Integer.parseInt((String) newspeedComboBox.getSelectedItem());
            Color newColor = getColorFromName((String) newcolorComboBox.getSelectedItem());
            figure.setSpeed(newSpeed, newSpeed); // Простое назначение скорости одинаково по x и y
            figure.setColor(newColor);
            // Обновление текста фигуры
            String newContent = newContentField.getText();

            boolean isImage = validContents.contains(newContent);
            figure.setContent(newContent, isImage);
            clearFields(); // Очищаем текстовые поля после изменения объекта
        }
    }

    private Color getColorFromName(String colorName) {
        switch (colorName.toLowerCase()) {
            case "красный":
                return Color.RED;
            case "зеленый":
                return Color.GREEN;
            case "синий":
                return Color.BLUE;
            case "желтый":
                return Color.YELLOW;
            case "черный":
                return Color.BLACK;
            default:
                return Color.BLACK;
        }
    }
    private void loadFigureData() {
        int number = Integer.parseInt((String) selectComboBox.getSelectedItem());
        Figure figure = demoPanel.getFigureByNumber(number);

        if (figure != null) {
            newContentField.setText(figure.content);

            String colorName = "Черный";
            if (figure.color.equals(Color.RED)) {
                colorName = "Красный";
            } else if (figure.color.equals(Color.GREEN)) {
                colorName = "Зеленый";
            } else if (figure.color.equals(Color.BLUE)) {
                colorName = "Синий";
            } else if (figure.color.equals(Color.YELLOW)) {
                colorName = "Желтый";
            }
            newcolorComboBox.setSelectedItem(colorName);
        }
    }

    private void clearFields() {

        speedComboBox.setSelectedIndex(0);
        newspeedComboBox.setSelectedIndex(0);
        selectComboBox.setSelectedIndex(0);
        contentField.setText("");
        newContentField.setText("");
        newcolorComboBox.setSelectedIndex(0);
        colorComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Test test = new Test();
            test.setVisible(true);
        });
    }
}