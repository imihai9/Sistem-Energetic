package entities;

public interface Subject {
    void addObserver(Distributor d);

    void removeObserver(Distributor d);

    void notifyObservers();
}
