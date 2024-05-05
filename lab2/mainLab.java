public class mainLab implements IConstants{
  public static void main(String[] args) {
    int sumEvenNegative = 0;
    int sumOddPositive = 0;
    int number;
    try{
      TestMain Test = new TestMain();
      Test.CheckNumberException(args.length);
      for (String arg : args) {
        number = Integer.parseInt(arg);
        Test.CheckValueException(number);
        if (number % 2 == 0 && number < 0) {
          sumEvenNegative += number;
        } else if (number % 2 != 0 && number > 0) {
          sumOddPositive += number;
        }
      }
          
      System.out.println("Сумма <четных и отрицательных> чисел посл-ти: " + sumEvenNegative);
      System.out.println("Сумма <нечетных и положительынх> чисел посл-ти: " + sumOddPositive);
    } catch(Exception e){
      System.err.println(e);
    }
  }
}
