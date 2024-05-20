package laba6.variant38;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class ApplicationFrames {
	static int countFigures;
	static MainFrame balls;
	public static int id = 0;

	public static void main(String[] args) throws IOException {
		countFigures = 0;
		balls = new MainFrame();
		System.out.println(Integer.toBinaryString(307));

	}

	static class MainFrame extends Frame implements Observer, ActionListener, ItemListener {
		private final LinkedList<Figure> figuresList = new LinkedList<>();
		private Color   color;
		private Frame   controlFrame;
		private Button  startFigureButton;
		private Button  changeFigureButton;
		private JLabel  alertFigureCountLabel;
		private Choice  choiceColorsTable;
		private Choice  choiceObjectType;
//		private TextField   inputSpeedX;
//		private TextField   inputSpeedY;
		private TextField   figureNumberText;
		private TextField   newFigureNumberText;
		private TextField   text;
		private TextField   speedTextX;
		private TextField   speedTextY;
		private TextField   checkAlreadyAddedFigureText;
		BufferedImage image;
		private Choice choiceSpeedTable;

		MainFrame() throws IOException {
			initializeMainFrame();
			initializeControlFrame();
			initializeColorTableOfFigure();
			initializeStartButtonOfFrame();
			initializeChangeFigureNumbers();
			initializeChangeFigureButton();
			initializeTableOfObjectType();

			controlFrame.setVisible(true);
			File file = new File("C:\\Users\\User\\Desktop\\laba6\\pick.png");
			image = ImageIO.read(file);
		}

		private void initializeChangeFigureNumbers() {
			checkAlreadyAddedFigureText = new TextField("Укажите Номер созданной фигуры");
			controlFrame.add(checkAlreadyAddedFigureText);
		}

		private void initializeChangeFigureButton() {
			changeFigureButton = new Button("Изменить");
			changeFigureButton.setActionCommand("CHANGE");
			changeFigureButton.addActionListener(this);
			controlFrame.add(changeFigureButton);
		}


		private void initializeMainFrame() {
			this.addWindowListener(new WindowCloser()); // кнопка закрытия окна
			this.setSize(300, 300);
			this.setResizable(true);
			this.setVisible(true);
			this.setLocation(700, 300);
		}

		private void initializeStartButtonOfFrame() {
			startFigureButton = new Button("Пуск");
			//startFigureButton.setSize(new Dimension(10, 40));
			startFigureButton.setActionCommand("OK");
			startFigureButton.addActionListener(this);
			controlFrame.add(startFigureButton, new Point(20, 20));
		}

		private void initializeControlFrame() {
			controlFrame = new Frame(); // создаем 2 окно с кнопками
			controlFrame.setResizable(true);
			controlFrame.setSize(700, 500);

			controlFrame.setResizable(true);
			controlFrame.setTitle("Контроль");
			controlFrame.setLayout(new GridLayout(4,3,5,5));
			controlFrame.addWindowListener(new WindowCloser());
			text = new TextField("Текст");
			text.setName("Введите текст");
			controlFrame.add(text);

			choiceSpeedTable = new Choice();
			choiceSpeedTable.addItem("1");
			choiceSpeedTable.addItem("5");
			choiceSpeedTable.addItem("10");
			choiceSpeedTable.addItem("15");
			choiceSpeedTable.addItem("20");
			choiceSpeedTable.addItem("30");
			choiceSpeedTable.addItemListener(this);
			controlFrame.add(choiceSpeedTable);

		}

		private void initializeColorTableOfFigure() {
			choiceColorsTable = new Choice();
			choiceColorsTable.addItem("Синий");
			choiceColorsTable.addItem("Зелёный");
			choiceColorsTable.addItem("Красный");
			choiceColorsTable.addItem("Чёрный");
			choiceColorsTable.addItem("Жёлтый");
			choiceColorsTable.addItemListener(this);
			controlFrame.add(choiceColorsTable, new Point(600, 20));
		}

		private void initializeTableOfObjectType() {
			choiceObjectType = new Choice();
			choiceObjectType.addItem(ObjectType.IMAGE.getType());
			choiceObjectType.addItem(ObjectType.TEXT.getType());
			controlFrame.add(choiceObjectType, 1);
		}

		@Override // Метод от Observable
		public void update(Observable o, Object arg) {
			Figure figure = (Figure) arg;
			System.out.println("x= " + figure.thr.getName() + figure.x);
			repaint();
		}

		public void paint(Graphics g) {
			if (!figuresList.isEmpty()) {
				for (Figure figure : figuresList) {
					g.setColor(figure.color);
					g.drawString(Integer.toString(figure.number), figure.x, figure.y);
					if (figure.objectType == ObjectType.IMAGE) {
						//g.drawOval(figure.x, figure.y, 50, 50);
						g.drawImage(image, figure.x, figure.y, 50, 50, null);
					}
					else {
						Font font = new Font("Serif", Font.BOLD, 25);
						g.setFont(font);
						g.drawString(figure.getText(), figure.x, figure.y + 25);
						g.setFont(new Font("Dialog", Font.ITALIC, 15));
					}
				}
			}
		}

		public void actionPerformed(ActionEvent aE) { // обработка нажатий 2-х кнопок
			String str = aE.getActionCommand();
			if (str.equals("OK")) {// обработка нажатия на кнопку "Пуск"
//				if (countFigures == 10) {
//					return;
//				}
				buttonOKIsPressed();
			}
			else if (str.equals("CHANGE")) {// обработка нажатия на кнопку "Изменить"
				try {
					if (figuresList.isEmpty())
						return;
					int changeableNumber = Integer.parseInt(checkAlreadyAddedFigureText.getText());
					for (int i = 0; i < figuresList.size(); i++) {
						if (figuresList.get(i).number == changeableNumber){
							color = getColorFromTable();
							changeFigureByIndex(i);

						}
					}
				}
				catch (Exception ignored) {
					return;
				}
			}
			repaint();
		}

		private void changeFigureByIndex(int figureIndex) {
			Figure figure = figuresList.get(figureIndex);
			figure.setSpeedX(getSpeedFromTable());
			figure.setSpeedY(getSpeedFromTable());
			figure.setColor(color);
		}

		private void buttonOKIsPressed() {
			Figure figure = null;
			int newFigureNumber = 0;
			color = getColorFromTable();
			ObjectType objectType = getObjectType();
			try {
				newFigureNumber = id;
				newFigureNumber = getUniqueNumberForFigure(newFigureNumber);
				figure = getFigureByFigureBuilder(objectType, newFigureNumber);
			}
			catch (Exception ignored) {
			}
			finally {
				if (newFigureNumber == 0){
					newFigureNumber = getUniqueNumberForFigure(newFigureNumber);
				}
				if (figure == null){
					figure = getFigureByFigureBuilderWithoutSpeeds(objectType, newFigureNumber);
				}
				figuresList.add(figure);
				figure.addObserver(this); // добавялем объект к слушателям и он тоже будет обновляться
			}
			id++;
		}

		private int getUniqueNumberForFigure(int newFigureNumber) {
			if (newFigureNumber == 0)
				newFigureNumber++;
			//int attemptsToAssignNumber = 1;
			for (int i = 0; i < figuresList.size(); i++){
				if (figuresList.get(i).number == newFigureNumber){
//					newFigureNumber = attemptsToAssignNumber;
//					attemptsToAssignNumber++;
					newFigureNumber++;
					i = -1;
				}
			}
			return newFigureNumber;
		}

		// использовали Builder чтобы не писать длинный конструктор из 5 аргументов
		private Figure getFigureByFigureBuilder(ObjectType objectType, int number) throws NumberFormatException {
			return Figure.FigureBuilder.aFigure()
					.withColor(color)
					.withNumber(number)
					.withObjectType(objectType)
					.withText(this.text.getText())
					.withSpeedX(getSpeedFromTable()) // здесь может быть исключение,
					.withSpeedY(getSpeedFromTable()) // но это не беда, тк мы можем просто исключить эти методы
					.build();
		}

		private Figure getFigureByFigureBuilderWithoutSpeeds(ObjectType objectType, int number) {
			return Figure.FigureBuilder.aFigure()
					.withColor(color)
					.withNumber(number)
					.withObjectType(objectType)
					.withText(this.text.getText())
					.build();
		}

		private ObjectType getObjectType() {
			switch (choiceObjectType.getSelectedIndex()) {
				case 0:
					return ObjectType.IMAGE;
				case 1:
					return ObjectType.TEXT;
			}
			return ObjectType.TEXT;
		}

		private Color getColorFromTable() {
			switch (choiceColorsTable.getSelectedIndex()) {
				case 0:
					return Color.blue;
				case 1:
					return Color.green;
				case 2:
					return Color.red;
				case 3:
					return Color.black;
				case 4:
					return Color.yellow;
			}
			return Color.blue;
		}

		private int getSpeedFromTable() {
			switch (choiceSpeedTable.getSelectedIndex()) {
				case 0:
					return 1;
				case 1:
					return 5;
				case 2:
					return 10;
				case 3:
					return 15;
				case 4:
					return 20;
				case 5:
					return 30;
			}
			return 1;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
		}

	}

	static class Figure extends Observable implements Runnable {
		Thread thr;
		private boolean xplus = true;
		private boolean yplus = true;
		public ObjectType objectType;
		private int speedX = 1;
		private int speedY = 1;

		final static int WIDTH = 50;
		final static int HEIGHT = 50;
		int x = 0;
		int y = 30;
		int number;
		private final String text;
		private Color color;

		// Создали конструктор который принимает наш Builder, он позволит нам без проблем получить все атрибуты
		public Figure(Figure.FigureBuilder builder){
			color = builder.color;
			number = builder.number;
			text = builder.text;
			speedX = builder.speedX;
			speedY = builder.speedY;
			objectType = builder.objectType;
			countFigures++;
			thr = new Thread(this, countFigures + ":" + text + ":");
			thr.start(); // врубаем новый поток для фигуры и переходим в run
		}

		public int getSpeedX() {
			return speedX;
		}

		public void setSpeedX(int speedX) {
			this.speedX = speedX;
		}

		public int getSpeedY() {
			return speedY;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public void setSpeedY(int speedY) {
			this.speedY = speedY;
		}

		public String getText() {
			return text;
		}

		public void run() {
			while (true) {
				Dimension mainFrameSize = balls.getSize();
				if (x > mainFrameSize.width - WIDTH)
					xplus = false;
				if (x < -1)
					xplus = true;
				if (y > mainFrameSize.height - HEIGHT)
					yplus = false;
				if (y < 31)
					yplus = true;
				if (xplus)
					x += speedX;
				else
					x -= speedX;
				if (yplus)
					y += speedY;
				else
					y -= speedY;
				setChanged();
				notifyObservers(this);
				try {
					Thread.sleep(10);
				}
				catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		//  использую паттерн проектирования Builder
		public static class FigureBuilder {
			private ObjectType objectType = ObjectType.IMAGE;
			private int         speedX = 1;
			private int         speedY = 1;
			private String      text = "default text";
			private Color       color = Color.black;
			private int number;

			private FigureBuilder(){
			}

			public static Figure.FigureBuilder aFigure() {
				return new Figure.FigureBuilder();
			}

			public Figure.FigureBuilder withColor(Color color){
				this.color = color;
				return this;
			}

			public Figure.FigureBuilder withNumber(int number){
				this.number = number;
				return this;
			}

			public Figure.FigureBuilder withText(String text) {
				this.text = text;
				return this;
			}

			public Figure.FigureBuilder withSpeedX(int speedX) {
				this.speedX = speedX;
				return this;
			}

			public Figure.FigureBuilder withSpeedY(int speedY) {
				this.speedY = speedY;
				return this;
			}

			public Figure.FigureBuilder withObjectType(ObjectType type) {
				this.objectType = type;
				return this;
			}

			// собираем все наши полученные атрибуты и передаем в
			// нужный класс через передачу нашего Builder
			public Figure build() {
				return new Figure(this);
			}
		}
	}

	static class WindowCloser extends WindowAdapter {
		@Override
		public void windowClosing (WindowEvent wE) {
			System.exit (0);
		}
	}

	static enum ObjectType {
		IMAGE("Картинка"),
		TEXT("Текст");

		private final String type;

		ObjectType(String type){
			this.type = type;
		}

		private String getType(){
			return type;
		}
	}

}
