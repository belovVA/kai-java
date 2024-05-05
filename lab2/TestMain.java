public class TestMain implements IFunctions, IConstants{
  @Override
  public void CheckNumberException(int numb) throws ArraySizeMaxException, ArraySizeMinException{
    if (numb > ArraySizeMax) {
      throw new ArraySizeMaxException();
    }
    if (numb < ArraySizeMin) {
        throw new ArraySizeMinException();
    }
  }

  @Override
  public void CheckValueException(int numb) throws ValueOfNumbException{
    if (numb > ValueOfNumb){
      throw new ValueOfNumbException();
    }
  }
}
