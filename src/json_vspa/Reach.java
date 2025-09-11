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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Reach)) return false;
        Reach other = (Reach) obj;
        return first.equals(other.first) && second.equals(other.second);
    }

}
