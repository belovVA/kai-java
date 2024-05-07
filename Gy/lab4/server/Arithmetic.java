import java.math.BigDecimal;
import java.util.List;
import java.util.Stack;



    public class Arithmetic {
        private List<Object> tokens;
        private BigDecimal result;

        public Arithmetic(List<Object> tokens) {
            this.tokens = tokens;
        }

        public void evaluate() {
            Stack<BigDecimal> stack = new Stack<>();

            for (Object token : tokens) {
                if (token instanceof Double) {
                    stack.push(BigDecimal.valueOf((Double) token));
                } else if (token instanceof String) {
                    if (stack.size() < 2) {
                        throw new IllegalArgumentException("Недостаточно операндов для оператора: " + token);
                    }

                    BigDecimal right = stack.pop();
                    BigDecimal left = stack.pop();
                    BigDecimal value;

                    switch ((String) token) {
                        case "+":
                            value = left.add(right);
                            break;
                        case "-":
                            value = left.subtract(right);
                            break;
                        case "*":
                            value = left.multiply(right);
                            break;
                        case "/":
                            if (right.equals(BigDecimal.ZERO)) {
                                throw new ArithmeticException("Деление на ноль");
                            }
                            // Делим с округлением до 10 знаков после запятой
                            value = left.divide(right, 10, BigDecimal.ROUND_HALF_UP);
                            break;
                        default:
                            throw new IllegalArgumentException("Недопустимый оператор: " + token);
                    }

                    stack.push(value);
                } else {
                    throw new IllegalArgumentException("Недопустимый токен: " + token);
                }
            }

            if (stack.size() != 1) {
                throw new IllegalArgumentException("Недостаточно операторов для операции");
            }

            result = stack.pop();
        }

        public double getResult() {
            return result.doubleValue(); // Возвращаем результат как double
        }
    }
