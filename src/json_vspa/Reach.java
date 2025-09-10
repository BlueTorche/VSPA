package json_vspa;

import automaton.State;

public class Reach {
    public final State first;
    public final State second;

    public Reach(State first, State second) {
        this.first = first;
        this.second = second;
    }

    public String toString() {
        return '(' + first.toString() + ", " + second.toString() + ')';
    }

    @Override
    public int hashCode() { return this.toString().hashCode(); }
}
