public class ArraySizeMaxException extends Exception implements IConstants{
    public String toString() {
        return "Ошибка ввода параметров: Размер массива больше числа " + ArraySizeMax;
    }
}
