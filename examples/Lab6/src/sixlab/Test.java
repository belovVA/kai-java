package sixlab;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

class OurWindowAdapter extends WindowAdapter {
    public void windowClosing(WindowEvent wE) {
        System.exit(0);
    }
}

class Frames extends Frame {
    java.util.ArrayList<Integer> numbers = new ArrayList<>();
    java.util.List<Figure> figures = new ArrayList<>();

    Frame childFrame;
    Canvas cnv;
    Label numberLab, objectLab, speedLab, objectLabForChange, speedLabForChange, colorLab;
    TextField numberTB, speedTB, objectChForChange, speedCh;
    Choice colorTB, figureTB;
    Button addBut, changeBut;
    Color color;

    Frames() {
        this.setTitle("LuchnikovRI");
        this.setSize(360, 400);

        numberLab = new Label("Номер: ");
        objectLab = new Label("Фигура: ");
        speedLab = new Label("Скорость: ");
        objectLabForChange = new Label("Фигура: ");
        speedLabForChange = new Label("Скорость: ");
        colorLab = new Label("Цвет: ");

        numberTB = new TextField();
        numberTB.setText("1");
        numberTB.setEditable(false);
        speedTB = new TextField();
        speedTB.setText("1");

        colorTB = new Choice();
        colorTB.addItem("Красный");
        colorTB.addItem("Оранжевый");
        colorTB.addItem("Синий");
        colorTB.addItem("Белый");
        colorTB.addItem("Черный");

        figureTB = new Choice();
        figureTB.addItem("Круг");
        figureTB.addItem("Овал");
        figureTB.addItem("Треугольник");
        figureTB.addItem("Квадрат");
        figureTB.addItem("Прямоугольник");

        addBut = new Button("Добавить");
        changeBut = new Button("Изменить");

        objectChForChange = new TextField();
        speedCh = new TextField();

        cnv = new Canvas() {
            public void paint(Graphics g) {
                for (Figure figure : figures) {
                    figure.show(true);
                }
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(numberLab, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(numberTB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(objectLab, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(figureTB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(colorLab, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        this.add(colorTB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(speedLab, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(speedTB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        this.add(addBut, gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 1;
        this.add(new Label(""), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        this.add(objectLabForChange, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        this.add(objectChForChange, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        this.add(speedLabForChange, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        this.add(speedCh, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        this.add(changeBut, gbc);

        gbc.gridy = 9;
        gbc.gridwidth = 1;
        this.add(new Label(""), gbc);

        addBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aE) {
                AddButFunc();
            }
        });

        changeBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aE) {
                ChangeButFunc();
            }
        });

        this.addWindowListener(new OurWindowAdapter());

        childFrame = new Frame();
        childFrame.setSize(800, 600);
        childFrame.setLocation(450, 0);
        childFrame.add(cnv);
        childFrame.show();
        childFrame.addWindowListener(new OurWindowAdapter());
    }

    public void AddButFunc() {
        if (IsInteger(numberTB.getText())) {
            int num = Integer.parseInt(numberTB.getText());
            if (CheckNumbers(num)) {
                if (IsInteger(speedTB.getText())) {
                    int speed = Integer.parseInt(speedTB.getText());
                    if (CheckSpeed(speed)) {
                        if (CheckColor(colorTB.getSelectedItem())) {
                            numbers.add(num);
                            if (CheckFigure(figureTB.getSelectedItem())) {
                                figures.get(numbers.size() - 1).start();
                            }
                        }
                    }
                }
            }
            num++;
            numberTB.setText(Integer.toString(num));
        }
    }

    public void ChangeButFunc() {
        if (IsInteger(objectChForChange.getText())) {
            int num = Integer.parseInt(objectChForChange.getText());
            int foundNumber = -1;
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i) == num)
                    foundNumber = i;
            }
            if (foundNumber != -1 && IsInteger(speedCh.getText())) {
                figures.get(foundNumber).speed = Integer.parseInt(speedCh.getText());
            }
        }
    }

    public boolean IsInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean CheckNumbers(int num) {
        return !numbers.contains(num);
    }

    public boolean CheckSpeed(int speed) {
        return speed > 0;
    }

    public boolean CheckFigure(String fgr) {
        switch (fgr.toLowerCase()) {
            case "круг":
                figures.add(new Circle(this.cnv, color, Integer.parseInt(speedTB.getText()), Integer.parseInt(numberTB.getText())));
                break;
            case "овал":
                figures.add(new Oval(this.cnv, color, Integer.parseInt(speedTB.getText()), Integer.parseInt(numberTB.getText())));
                break;
            case "треугольник":
                figures.add(new Triangle(this.cnv, color, Integer.parseInt(speedTB.getText()), Integer.parseInt(numberTB.getText())));
                break;
            case "квадрат":
                figures.add(new Square(this.cnv, color, Integer.parseInt(speedTB.getText()), Integer.parseInt(numberTB.getText())));
                break;
            case "прямоугольник":
                figures.add(new Rectangle(this.cnv, color, Integer.parseInt(speedTB.getText()), Integer.parseInt(numberTB.getText())));
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean CheckColor(String clr) {
        switch (clr.toLowerCase()) {
            case "красный":
                this.color = Color.red;
                break;
            case "оранжевый":
                this.color = Color.orange;
                break;
            case "синий":
                this.color = Color.blue;
                break;
            case "белый":
                this.color = Color.white;
                break;
            case "черный":
                this.color = Color.black;
                break;
            default:
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Frames Fr = new Frames();
        Fr.show();
    }
}

abstract class Figure extends Thread {
    Point point = new Point(50, 50);
    Canvas cnv;
    Graphics g;
    Color color;
    public int speed;
    double angle;
    int id;
    int step = 4;
    final Random random = new Random();

    Figure(Canvas cnv, Color color, int speed, int id) {
        this.cnv = cnv;
        this.color = color;
        this.speed = speed;
        this.angle = Math.random() * 2 * Math.PI;
        this.g = cnv.getGraphics();
        this.id = id;
    }

    void moveTo() {
        this.show(false);
        this.point.x += this.speed * Math.cos(this.angle) * step;
        this.point.y += this.speed * Math.sin(this.angle) * step;
        checkBorder();
        this.show(true);
    }

    abstract void show(boolean sh);

    abstract void checkBorder();

    public void run() {
        while (true) {
            moveTo();
            try {
                sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Circle extends Figure {
    int radius = 20;

    Circle(Canvas cnv, Color color, int speed, int id) {
        super(cnv, color, speed, id);
    }

    void show(boolean sh) {
        if (sh) g.setColor(color);
        else g.setColor(Color.white);
        g.fillOval(point.x, point.y, radius, radius);
        g.drawString(Integer.toString(id), point.x - 10, point.y - 10);
    }

    void checkBorder() {
        boolean border = false;
        if ((cnv.getWidth() - point.x <= radius) && (Math.cos(angle) > 0)) {
            angle = Math.PI - angle;
            border = true;
        } else if ((point.x <= 0) && (Math.cos(angle) < 0)) {
            angle = Math.PI - angle;
            border = true;
        }

        if ((cnv.getHeight() - point.y <= radius) && (Math.sin(angle) > 0)) {
            angle *= (-1);
            border = true;
        } else if ((point.y <= 0) && (Math.sin(angle) < 0)) {
            angle *= (-1);
            border = true;
        }

        if (border) {
            point.x += speed * Math.cos(angle);
            point.y += speed * Math.sin(angle);
        }
    }
}



class Oval extends Figure {
    int width = 30;
    int height = 20;

    Oval(Canvas cnv, Color color, int speed, int id) {
        super(cnv, color, speed, id);
    }

    void show(boolean sh) {
        if (sh) g.setColor(color);
        else g.setColor(Color.white);
        g.fillOval(point.x, point.y, width, height);
        g.drawString(Integer.toString(id), point.x - 10, point.y - 10);
    }

    void checkBorder() {
        boolean border = false;
        if ((cnv.getWidth() - point.x <= width) && (Math.cos(angle) > 0)) {
            angle = Math.PI - angle;
            border = true;
        } else if ((point.x <= 0) && (Math.cos(angle) < 0)) {
            angle = Math.PI - angle;
            border = true;
        }

        if ((cnv.getHeight() - point.y <= height) && (Math.sin(angle) > 0)) {
            angle *= (-1);
            border = true;
        } else if ((point.y <= 0) && (Math.sin(angle) < 0)) {
            angle *= (-1);
            border = true;
        }

        if (border) {
            point.x += speed * Math.cos(angle);
            point.y += speed * Math.sin(angle);
        }
    }
}

class Triangle extends Figure {
    int size = 20;

    Triangle(Canvas cnv, Color color, int speed, int id) {
        super(cnv, color, speed, id);
    }

    void show(boolean sh) {
        if (sh) g.setColor(color);
        else g.setColor(Color.white);
        int[] xPoints = {point.x, point.x + size, point.x + size / 2};
        int[] yPoints = {point.y, point.y, point.y - size};
        g.fillPolygon(xPoints, yPoints, 3);
        g.drawString(Integer.toString(id), point.x - 10, point.y - 10);
    }

    void checkBorder() {
        boolean border = false;
        if ((cnv.getWidth() - point.x <= size) && (Math.cos(angle) > 0)) {
            angle = Math.PI - angle;
            border = true;
        } else if ((point.x <= 0) && (Math.cos(angle) < 0)) {
            angle = Math.PI - angle;
            border = true;
        }

        if ((cnv.getHeight() - point.y <= size) && (Math.sin(angle) > 0)) {
            angle *= (-1);
            border = true;
        } else if ((point.y <= 0) && (Math.sin(angle) < 0)) {
            angle *= (-1);
            border = true;
        }

        if (border) {
            point.x += speed * Math.cos(angle);
            point.y += speed * Math.sin(angle);
        }
    }
}

class Square extends Figure {
    int side = 20;

    Square(Canvas cnv, Color color, int speed, int id) {
        super(cnv, color, speed, id);
    }

    void show(boolean sh) {
        if (sh) g.setColor(color);
        else g.setColor(Color.white);
        g.fillRect(point.x, point.y, side, side);
        g.drawString(Integer.toString(id), point.x - 10, point.y - 10);
    }

    void checkBorder() {
        boolean border = false;
        if ((cnv.getWidth() - point.x <= side) && (Math.cos(angle) > 0)) {
            angle = Math.PI - angle;
            border = true;
        } else if ((point.x <= 0) && (Math.cos(angle) < 0)) {
            angle = Math.PI - angle;
            border = true;
        }

        if ((cnv.getHeight() - point.y <= side) && (Math.sin(angle) > 0)) {
            angle *= (-1);
            border = true;
        } else if ((point.y <= 0) && (Math.sin(angle) < 0)) {
            angle *= (-1);
            border = true;
        }

        if (border) {
            point.x += speed * Math.cos(angle);
            point.y += speed * Math.sin(angle);
        }
    }
}

class Rectangle extends Figure {
    int width = 30;
    int height = 20;

    Rectangle(Canvas cnv, Color color, int speed, int id) {
        super(cnv, color, speed, id);
    }

    void show(boolean sh) {
        if (sh) g.setColor(color);
        else g.setColor(Color.white);
        g.fillRect(point.x, point.y, width, height);
        g.drawString(Integer.toString(id), point.x - 10, point.y - 10);
    }

    void checkBorder() {
        boolean border = false;
        if ((cnv.getWidth() - point.x <= width) && (Math.cos(angle) > 0)) {
            angle = Math.PI - angle;
            border = true;
        } else if ((point.x <= 0) && (Math.cos(angle) < 0)) {
            angle = Math.PI - angle;
            border = true;
        }

        if ((cnv.getHeight() - point.y <= height) && (Math.sin(angle) > 0)) {
            angle *= (-1);
            border = true;
        } else if ((point.y <= 0) && (Math.sin(angle) < 0)) {
            angle *= (-1);
            border = true;
        }

        if (border) {
            point.x += speed * Math.cos(angle);
            point.y += speed * Math.sin(angle);
        }
    }
}
