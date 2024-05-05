public interface IFunctions {
  void CheckNumberException(int numb) throws ArraySizeMaxException, ArraySizeMinException;
  void CheckValueException(int numb) throws ValueOfNumbException;
}
