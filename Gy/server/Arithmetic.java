import java.util.List;
import java.util.Stack;

class Arithmetic {
    private List<Object> tokens;
    private double result; // Поле для хранения результата вычислений

    public Arithmetic(List<Object> tokens) {
        this.tokens = tokens;
    }

    public void evaluate() throws IllegalArgumentException {
        Stack<Double> stack = new Stack<>();

        for (Object token : tokens) {
            if (token instanceof Double) {
                stack.push((Double) token);
            } else if (token instanceof String) {
                String operator = (String) token;
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Недостаточно операндов для оператора: " + operator);
                }

                double operand2 = stack.pop();
                double operand1 = stack.pop();

                switch (operator) {
                    case "+":
                        stack.push(operand1 + operand2);
                        break;
                    case "-":
                        stack.push(operand1 - operand2);
                        break;
                    case "*":
                        stack.push(operand1 * operand2);
                        break;
                    case "/":
                        if (operand2 == 0) {
                            throw new ArithmeticException("Деление на ноль");
                        }
                        stack.push(operand1 / operand2);
                        break;
                    default:
                        throw new IllegalArgumentException("Недопустимый оператор: " + operator);
                }
            } else {
                throw new IllegalArgumentException("Недопустимый токен: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Некорректное выражение");
        }

        this.result = stack.pop(); // Сохраняем результат вычислений
    }

    public double getResult() {
        return result; // Возвращаем сохраненный результат вычислений
    }
}
