// 303 - 01 0010 1111
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Test extends JFrame {
    private DemoPanel demoPanel;
    private JTextField  numberField, newNuberField;
    private JComboBox<String>  speedComboBox, newspeedComboBox, selectComboBox,  colorComboBox, contendComboBox, newcolorComboBox, newcontendComboBox;
    private Timer timer;
    private Set<Integer> usedNumbers = new HashSet<>();
    private final Set<String> validContents = Set.of("picture");
    //    private Color selectedColor = Color.BLACK; // Выбранный цвет по умолчанию


    public Test() {
        setTitle("Управляющее окно");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1)); // Используем GridLayout для размещения компонентов по одной строке

        String[] contents = {"", "laba", "Azat", "KAI", "JAVA", "picture"};
        contendComboBox = new JComboBox<>(contents);
        add(new JLabel(" Содержимое:"));
        add(contendComboBox);

        String[] colors = {"Красный", "Зеленый", "Синий", "Желтый", "Черный"};
        colorComboBox = new JComboBox<>(colors);
        add(new JLabel("Цвет:"));
        add(colorComboBox);

        String[] speeds = {"1", "3", "5", "7", "10", "15"};
        speedComboBox = new JComboBox<>(speeds);
        add(new JLabel("Скорость:"));
        add(speedComboBox);

        numberField = new JTextField(5);
        add(new JLabel("Номер:"));
        add(numberField);

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

        newcontendComboBox = new JComboBox<>(contents);
        add(new JLabel(" Содержимое:"));
        add(newcontendComboBox);

        newspeedComboBox = new JComboBox<>(speeds);
        add(new JLabel("Скорость:"));
        add(newspeedComboBox);

        newNuberField = new JTextField(5);
        add(new JLabel("Номер:"));
        add(newNuberField);

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

        String content = (String) contendComboBox.getSelectedItem();
        if (Objects.equals(content, "")){
            JOptionPane.showMessageDialog(this, "Поле содержимого не должно быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean isImage = validContents.contains(content);
        int speed;
        int number;

        try {
            speed = Integer.parseInt((String) speedComboBox.getSelectedItem());
//            speed = Integer.parseInt((String) speedComboBox.getSelectedItem());
            number = Integer.parseInt(numberField.getText());
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
        int dx = random.nextInt(21) - 10; // Генерируем случайное значение dx в диапазоне [-10, 10]
        int dy = random.nextInt(21) - 10; // Генерируем случайное значение dy в диапазоне [-10, 10]

        while (dx * dx + dy * dy > 100) {
            dx = random.nextInt(21) - 10 * speed;
            dy = random.nextInt(21) - 10 * speed;
        }
        dx *= speed;
        dy *= speed;
        Figure figure = new Figure(0, 0, dx, dy, 30, color, content, number, isImage);

        demoPanel.addFigure(figure);
        usedNumbers.add(number);
        selectComboBox.addItem(String.valueOf(number));
        clearFields();
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
            int newSpeed, newNumber;
            Color newColor;
            try {
                newSpeed = Integer.parseInt( (String)  newspeedComboBox.getSelectedItem());

                newNumber = Integer.parseInt(newNuberField.getText());
                newColor = getColorFromName((String) newcolorComboBox.getSelectedItem());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Номер должен быть числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newNumber != figure.number){
                if (usedNumbers.contains(newNumber)) {
                    JOptionPane.showMessageDialog(this, "Номер уже используется. Введите уникальный номер.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            figure.setSpeed(newSpeed, newSpeed);
            figure.setColor(newColor);
            figure.setNumber(newNumber);
            // Обновление текста фигуры
            String newContent = (String) newcontendComboBox.getSelectedItem();

            boolean isImage = validContents.contains(newContent);
            figure.setContent(newContent, isImage);
            selectComboBox.removeItem(number);
            selectComboBox.addItem(String.valueOf(newNumber));
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
            newspeedComboBox.setSelectedItem(String.valueOf(figure.getSpeed()));
            newNuberField.setText(String.valueOf(figure.number));
            newcontendComboBox.setSelectedItem(figure.content);
            newNuberField.setText(String.valueOf(number));
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
        speedComboBox.setSelectedIndex(0);
        newspeedComboBox.setSelectedIndex(0);
        selectComboBox.setSelectedIndex(0);
        newNuberField.setText("");
        numberField.setText("");
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