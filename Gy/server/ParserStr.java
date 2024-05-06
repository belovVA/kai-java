import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserStr {
    private String input;
    private List<Object> tokens;

    public ParserStr(String input) {
        this.input = input;
        this.tokens = new ArrayList<>();
    }

    public void parse() throws IllegalArgumentException {
        tokens.clear(); // Очищаем список токенов перед парсингом

        // Регулярное выражение для извлечения чисел (целых и дробных) и операций
        String regex = "\\d+\\.\\d+|\\d+|[+\\-*/]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        Stack<String> operatorStack = new Stack<>();

        while (matcher.find()) {
            String token = matcher.group();
            if (isNumber(token)) {
                // Преобразуем строку числа в double и добавляем в список токенов
                tokens.add(Double.parseDouble(token));
            } else if (isOperator(token)) {
                // Обработка операторов
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(token)) {
                    tokens.add(operatorStack.pop()); // Помещаем операторы в список токенов по приоритету
                }
                operatorStack.push(token); // Помещаем текущий оператор в стек
            } else {
                throw new IllegalArgumentException("Недопустимый символ в строке: " + token);
            }
        }

        // Переносим все оставшиеся операторы из стека в список токенов
        while (!operatorStack.isEmpty()) {
            tokens.add(operatorStack.pop());
        }
    }

    public List<Object> getTokens() {
        return new ArrayList<>(tokens); // Возвращаем копию списка токенов
    }

    private boolean isNumber(String token) {
        // Проверяем, является ли строка числом (целым или дробным)
        return token.matches("\\d+\\.\\d+|\\d+");
    }

    private boolean isOperator(String token) {
        // Проверяем, является ли строка оператором (+, -, *, /)
        return token.matches("[+\\-*/]");
    }

    private int precedence(String operator) {
        // Определяем приоритет оператора
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0;
        }
    }
}
