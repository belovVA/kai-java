package lab3;

import java.util.*;


public class IsEqualObservable  extends Observable {
    public void notifyObs() {
        setChanged();
    }
}
