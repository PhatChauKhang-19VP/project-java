package pck.java.be.app.util;

public class Pair<T,K> {
    private T first;
    private K second;

    public Pair(T t, K k) {
        this.first = t;
        this.second = k;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public K getSecond() {
        return second;
    }

    public void setSecond(K second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return first.toString() + "\n\t\t" + second.toString() +  "\n";
    }
}
