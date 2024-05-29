//  01 0110 0010
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class Figure {
    int x, y, dx, dy, size, number;
    Color color;
    String content;
    boolean isImage;

    public Figure(int x, int y, int dx, int dy, int size, Color color, String content, int number, boolean isImage) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        this.color = color;
        this.content = content;
        this.number = number;
        this.isImage = isImage;
    }

    public void move(int width, int height) {
        x += dx;
        y += dy;
        if (x < 0 || x + size > width) dx = -dx;
        if (y < 0 || y + size > height) dy = -dy;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void draw(Graphics g, Image image) {
        g.setColor(color);
        if (isImage) {

            g.drawImage(image, x, y, size, size, null);
        } else {
            g.drawString(content, x, y + size);
        }
        g.setColor(Color.BLACK);
        g.drawString("" + number, x + 1, y + 1);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSpeed(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setContent(String content, boolean isImage){
        this.content = content;
        this.isImage = isImage;

    }

    public int getSpeed() {
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
}

class DemoPanel extends JPanel {
    ArrayList<Figure> figures = new ArrayList<>();
    Image image;

    static int  tempId = 0;

    public DemoPanel() {
        try {
            image = new ImageIcon("/Users/zubatshr/kai-java/Kh/laba6/logo.jpeg").getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getFiguresCount() {
        return figures.size();
    }

    public static int getId(){
        return tempId;
    }

    public void incId(){
        this.tempId ++;
    }
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
            figure.draw(g, image);
        }
    }
}


