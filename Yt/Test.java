//  0101101110
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Test extends JFrame {
    private DemoPanel demoPanel;
    private JTextField contentField, numberField;
    private JComboBox<String> figureComboBox, speedComboBox;
    private Timer timer;
    private Set<Integer> usedNumbers = new HashSet<>();
    private final Set<String> validContents = Set.of("картинка");
    private Color selectedColor = Color.BLACK; // Выбранный цвет по умолчанию

    public Test() {
        setTitle("Управляющее окно");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1)); // Используем GridLayout для размещения компонентов по одной строке

        contentField = new JTextField(10);
        add(new JLabel("Содержимое:"));
        add(contentField);

        numberField = new JTextField(5);
        add(new JLabel("Номер:"));
        add(numberField);

        // Убираем выпадающий список выбора цвета и заменяем его кнопкой
        JButton colorButton = new JButton("Выбрать цвет");
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Выберите цвет текста", selectedColor);
            if (newColor != null) {
                selectedColor = newColor;
            }
        });
        add(new JLabel("Цвет текста:"));
        add(colorButton);

        String[] speeds = {"1", "10", "20", "30", "50", "100"};
        speedComboBox = new JComboBox<>(speeds);
        add(new JLabel("Скорость:"));
        add(speedComboBox);

        figureComboBox = new JComboBox<>();
        figureComboBox.addItem(""); // Добавляем пустую строку
        figureComboBox.addActionListener(e -> loadFigureData());
        add(new JLabel("Выбор объекта:"));
        add(figureComboBox);

        JButton startButton = new JButton("Пуск");
        startButton.addActionListener(e -> startFigure());
        add(startButton);

        JButton editButton = new JButton("Изменить");
        editButton.addActionListener(e -> editFigure());
        add(editButton);

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
        String content = contentField.getText().toLowerCase();
        boolean isImage = validContents.contains(content);
        int speed;
        int number;

        try {
            speed = Integer.parseInt((String) speedComboBox.getSelectedItem());
            number = Integer.parseInt(numberField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Номер должен быть числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usedNumbers.contains(number)) {
            JOptionPane.showMessageDialog(this, "Номер уже используется. Введите уникальный номер.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Random random = new Random();
        int dx = random.nextInt(speed) + 1;
        int dy = (int) Math.sqrt(speed * speed - dx * dx);
        Figure figure = new Figure(0, 0, dx, dy, 30, selectedColor, content, number, isImage);
        figure.setSpeed(speed, speed);
        demoPanel.addFigure(figure);
        figureComboBox.addItem(content + ", ID=" + number);
        usedNumbers.add(number);

        clearFields(); // Очищаем текстовые поля после добавления объекта
        demoPanel.repaint(); // Обновляем отрисовку панели
    }

    private void editFigure() {
        String selectedFigure = (String) figureComboBox.getSelectedItem();
        if (selectedFigure == null || selectedFigure.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Выберите объект для редактирования.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int number = Integer.parseInt(selectedFigure.split("=")[1]);
        Figure figure = demoPanel.getFigureByNumber(number);

        if (figure != null) {
            try {
                int newSpeed = Integer.parseInt((String) speedComboBox.getSelectedItem());

                figure.setSpeed(newSpeed, newSpeed); // Простое назначение скорости одинаково по x и y
                figure.setColor(selectedColor);

                // Обновление текста фигуры
                String newContent = contentField.getText().toLowerCase();
                boolean isImage = validContents.contains(newContent);
                figure.setContent(newContent, isImage);

                // Обновление ID фигуры
                int newNumber = Integer.parseInt(numberField.getText());
                if (newNumber != figure.number) {
                    if (usedNumbers.contains(newNumber)) {
                        JOptionPane.showMessageDialog(this, "Номер уже используется. Введите уникальный номер.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    usedNumbers.remove(figure.number);
                    figure.setNumber(newNumber);
                    usedNumbers.add(newNumber);
                }

                // Обновление списка фигур
                figureComboBox.removeItem(selectedFigure);
                figureComboBox.addItem(figure.content + ", ID=" + newNumber);

                clearFields(); // Очищаем текстовые поля после изменения объекта
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Скорость должна быть числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadFigureData() {
        String selectedFigure = (String) figureComboBox.getSelectedItem();
        if (selectedFigure == null || selectedFigure.isEmpty()) {
            return;
        }

        int number = Integer.parseInt(selectedFigure.split("=")[1]);
        Figure figure = demoPanel.getFigureByNumber(number);

        if (figure != null) {
            contentField.setText(figure.content);
            speedComboBox.setSelectedItem("" + figure.getSpeed() / 10);
            numberField.setText("" + figure.number);
            selectedColor = figure.color; // Обновляем выбранный цвет
        }
    }

    private void clearFields() {
        contentField.setText("");
        numberField.setText("");
        speedComboBox.setSelectedIndex(0);
        figureComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Test test = new Test();
            test.setVisible(true);
        });
    }
}