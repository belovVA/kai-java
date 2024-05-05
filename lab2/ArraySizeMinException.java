public class ArraySizeMinException extends Exception implements IConstants{
  public String toString() {
      return "Ошибка ввода параметров: Размер массива меньше числа " + ArraySizeMin;
  }
}