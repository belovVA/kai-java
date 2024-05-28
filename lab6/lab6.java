import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
//  0001001100
class Figure {
    int x, y, dx, dy, size, number;
    Color color;
    String shape;

    public Figure(int x, int y, int dx, int dy, int size, Color color, String shape, int number) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        this.color = color;
        this.shape = shape;
        this.number = number;
    }

    public void move(int width, int height) {
        x += dx;
        y += dy;
        if (x < 0 || x + size > width) dx = -dx;
        if (y < 0 || y + size > height) dy = -dy;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        switch (shape.toLowerCase()) {
            case "круг":
                g.fillOval(x, y, size, size);
                break;
            case "квадрат":
                g.fillRect(x, y, size, size);
                break;
            case "овал":
                g.fillOval(x, y, size, size / 2);
                break;
            case "прямоугольник":
                g.fillRect(x, y, size, size / 2);
                break;
            case "треугольник":
                int[] xPoints = {x, x + size / 2, x + size};
                int[] yPoints = {y + size, y, y + size};
                g.fillPolygon(xPoints, yPoints, 3);
                break;
        }
        g.setColor(Color.BLACK);
        g.drawString("" + number, x, y - 5);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSpeed(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSpeed() {
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
}

class DemoPanel extends JPanel {
    ArrayList<Figure> figures = new ArrayList<>();

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public Figure getFigureByNumber(int number) {
        for (Figure figure : figures) {
            if (figure.number == number) {
                return figure;
            }
        }
        return null;
    }

    public void updateFigures() {
        for (Figure figure : figures) {
            figure.move(getWidth(), getHeight());
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Figure figure : figures) {
            figure.draw(g);
        }
    }
}
