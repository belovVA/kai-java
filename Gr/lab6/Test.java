//1100010011
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Test extends JFrame {
    private DemoPanel demoPanel;
    private JTextField  speedField, selectField;
    private JComboBox<String>  speedComboBox, colorComboBox, contendComboBox, newcolorComboBox, newcontendComboBox;
    private Timer timer;
    private Set<Integer> usedNumbers = new HashSet<>();
    private final Set<String> validContents = Set.of("picture");
//    private Color selectedColor = Color.BLACK; // Выбранный цвет по умолчанию
    private final int MAX_FIGURES = 5;  // Максимальное количество фигур


    public Test() {
        setTitle("Управляющее окно");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1)); // Используем GridLayout для размещения компонентов по одной строке

        String[] contents = {"", "Hellow", "Kai", "World", "Bye", "Lab6", "picture"};
        contendComboBox = new JComboBox<>(contents);
        add(new JLabel(" Содержимое:"));
        add(contendComboBox);

        String[] colors = {"Красный", "Зеленый", "Синий", "Желтый", "Черный"};
        colorComboBox = new JComboBox<>(colors);
        add(new JLabel("Цвет:"));
        add(colorComboBox);



        speedField = new JTextField(5);
        add(new JLabel("Скорость:"));
        add(speedField);

        JButton startButton = new JButton("Пуск");
        startButton.addActionListener(e -> startFigure());
        add(startButton);

        selectField = new JTextField(5);
        add(new JLabel("ID для изменения:"));
        add(selectField);

        JButton showButton = new JButton("Изменить");
        showButton.addActionListener(e -> loadFigureData());
        add(showButton);
//         contents = {"", "Hellow", "Kai", "World", "Bye", "Lab6", "picture"};
        newcontendComboBox = new JComboBox<>(contents);
        add(new JLabel(" Содержимое:"));
        add(newcontendComboBox);

        String[] speeds = {"1", "5", "10", "25", "50"};
        speedComboBox = new JComboBox<>(speeds);
        add(new JLabel("Скорость:"));
        add(speedComboBox);

//        String colors = {"Красный", "Зеленый", "Синий", "Желтый", "Черный"};
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
        demoFrame.add(demoPanel);
        demoFrame.setVisible(true);

        timer = new Timer(30, e -> demoPanel.updateFigures());
        timer.start();
    }

    private void startFigure() {
        if (demoPanel.getFiguresCount() >= MAX_FIGURES) {
            JOptionPane.showMessageDialog(this, "Достигнуто максимальное количество фигур.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String content = (String) contendComboBox.getSelectedItem();
        if (Objects.equals(content, "")){
            JOptionPane.showMessageDialog(this, "Поле содержимого не должно быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean isImage = validContents.contains(content);
        int speed;
        int number;

        try {
            speed = Integer.parseInt(speedField.getText());
            if (speed <= 0 || speed > 50) {
                JOptionPane.showMessageDialog(this, "Скорость должна быть в диапазоне от 1 до 100.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
//            speed = Integer.parseInt((String) speedComboBox.getSelectedItem());
            number = demoPanel.tempId;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Номер должен быть числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usedNumbers.contains(number)) {
            JOptionPane.showMessageDialog(this, "Номер уже используется. Введите уникальный номер.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Color color = getColorFromName((String) colorComboBox.getSelectedItem());

        Random random = new Random();
        int dx = random.nextInt(speed) + 1;
        int dy = (int) Math.sqrt(speed * speed - dx * dx);
        Figure figure = new Figure(0, 0, dx, dy, 30, color, content, number, isImage);
        figure.setSpeed(speed, speed);
        demoPanel.addFigure(figure);
        usedNumbers.add(number);
        demoPanel.incId();

        clearFields(); // Очищаем текстовые поля после добавления объекта
        demoPanel.repaint(); // Обновляем отрисовку панели
    }

    private void editFigure() {
        int number;
        try {
             number = Integer.parseInt(selectField.getText());
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
            try {
                int newSpeed = Integer.parseInt((String) speedComboBox.getSelectedItem());
                Color newColor = getColorFromName((String) newcolorComboBox.getSelectedItem());

                figure.setSpeed(newSpeed, newSpeed); // Простое назначение скорости одинаково по x и y
                figure.setColor(newColor);

                // Обновление текста фигуры
                String newContent = (String) newcontendComboBox.getSelectedItem();

                boolean isImage = validContents.contains(newContent);
                figure.setContent(newContent, isImage);
                clearFields(); // Очищаем текстовые поля после изменения объекта
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Скорость должна быть числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
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
        int number = Integer.parseInt(selectField.getText());
        Figure figure = demoPanel.getFigureByNumber(number);

        if (figure != null) {
            speedComboBox.setSelectedItem("" + figure.getSpeed() / 10);
            newcontendComboBox.setSelectedItem(figure.content);
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
        contendComboBox.setSelectedIndex(0);
        speedField.setText("");
        speedComboBox.setSelectedIndex(0);
        selectField.setText("");
        newcontendComboBox.setSelectedIndex(0);
        newcolorComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Test test = new Test();
            test.setVisible(true);
        });
    }
}