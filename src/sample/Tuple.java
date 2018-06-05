package sample;

public class Tuple<X, Y> {
    public X first;
    public Y second;

    public Tuple() {}

    public Tuple(X x, Y y) {
        this.first = x;
        this.second = y;
    }

    public Y getSecond() {
        return second;
    }

    public X getFirst() {
        return first;
    }

    public Tuple<X,Y> clone() {
        Tuple<X,Y> nuevo = new Tuple<>();
        nuevo.first = this.first;
        nuevo.second = this.second;
        return nuevo;
    }
}
