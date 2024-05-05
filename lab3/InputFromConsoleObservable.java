package lab3;
import java.util.*;

public class InputFromConsoleObservable extends Observable {
    public void notifyObs() {
        setChanged();
        notifyObservers();
    }
}