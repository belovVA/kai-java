package lab3;

import java.util.*;

public class OutputToConsoleObservable extends Observable {
    public void notifyObs() {
        setChanged();
        notifyObservers();
    }
}
