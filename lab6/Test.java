import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Test extends JFrame {
    private DemoPanel demoPanel;
    private JTextField shapeField, speedField, numberField;
    private JComboBox<String> colorComboBox, figureComboBox;
    private Timer timer;
    private Set<Integer> usedNumbers = new HashSet<>();
    private final Set<String> validShapes = Set.of("круг", "овал", "треугольник", "квадрат", "прямоугольник");

    public Test() {
        setTitle("Управляющее окно");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1)); // Используем GridLayout для размещения компонентов по одной строке

        shapeField = new JTextField(10);
        add(new JLabel("Фигура:"));
        add(shapeField);

        speedField = new JTextField(5);
        add(new JLabel("Скорость:"));
        add(speedField);

        numberField = new JTextField(5);
        add(new JLabel("Номер:"));
        add(numberField);

        String[] colors = {"Красный", "Зеленый", "Синий", "Желтый", "Черный"};
        colorComboBox = new JComboBox<>(colors);
        add(new JLabel("Цвет:"));
        add(colorComboBox);

        figureComboBox = new JComboBox<>();
        figureComboBox.addItem(""); // Добавляем пустую строку
        figureComboBox.addActionListener(e -> loadFigureData());
        add(new JLabel("Выбор фигуры:"));
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
        String shape = shapeField.getText().toLowerCase();
        if (!validShapes.contains(shape)) {
            JOptionPane.showMessageDialog(this, "Неверная фигура. Допустимые фигуры: круг, овал, треугольник, квадрат, прямоугольник.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int speed;
        int number;

        try {
            speed = Integer.parseInt(speedField.getText());
            if (speed <= 0 || speed > 100) {
                JOptionPane.showMessageDialog(this, "Скорость должна быть в диапазоне от 1 до 100.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            number = Integer.parseInt(numberField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Скорость и номер должны быть числами.", "Ошибка", JOptionPane.ERROR_MESSAGE);
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
        Figure figure = new Figure(0, 0, dx, dy, 30, color, shape, number);
        figure.setSpeed(speed, speed);

        demoPanel.addFigure(figure);
        figureComboBox.addItem(shape + ", ID=" + number);
        usedNumbers.add(number);

        clearFields(); // Очищаем текстовые поля после добавления фигуры
    }

    private void editFigure() {
        String selectedFigure = (String) figureComboBox.getSelectedItem();
        if (selectedFigure == null || selectedFigure.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Выберите фигуру для редактирования.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int number = Integer.parseInt(selectedFigure.split("=")[1]);
        Figure figure = demoPanel.getFigureByNumber(number);

        if (figure != null) {
            String newShape = shapeField.getText().toLowerCase();
            if (!validShapes.contains(newShape)) {
                JOptionPane.showMessageDialog(this, "Неверная фигура. Допустимые фигуры: круг, овал, треугольник, квадрат, прямоугольник.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int newSpeed = Integer.parseInt(speedField.getText());
                if (newSpeed <= 0 || newSpeed > 100) {
                    JOptionPane.showMessageDialog(this, "Скорость должна быть в диапазоне от 1 до 100.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Color newColor = getColorFromName((String) colorComboBox.getSelectedItem());

                figure.setSpeed(newSpeed, newSpeed); // Простое назначение скорости одинаково по x и y
                figure.setColor(newColor);
                figure.setShape(newShape);

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
                figureComboBox.addItem(figure.shape + ", ID=" + newNumber);

                clearFields(); // Очищаем текстовые поля после изменения фигуры
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Скорость и номер должны быть числами.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Фигура не найдена.", "Ошибка", JOptionPane.ERROR_MESSAGE);
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
        String selectedFigure = (String) figureComboBox.getSelectedItem();
        if (selectedFigure == null || selectedFigure.isEmpty()) {
            return;
        }

        int number = Integer.parseInt(selectedFigure.split("=")[1]);
        Figure figure = demoPanel.getFigureByNumber(number);

        if (figure != null) {
            shapeField.setText(figure.shape);
            speedField.setText("" + figure.getSpeed());
            numberField.setText("" + figure.number);

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
            colorComboBox.setSelectedItem(colorName);
        }
    }
    private void clearFields() {
        shapeField.setText("");
        speedField.setText("");
        numberField.setText("");
        colorComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Test test = new Test();
            test.setVisible(true);
        });
    }
}
