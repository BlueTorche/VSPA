package json_vspa;

import automaton.State;
import vspa.VRA_State;

public class Reach {
    public final VRA_State first;
    public final VRA_State second;

    public Reach(VRA_State first, VRA_State second) {
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
        if (!(obj instanceof Reach other)) return false;
        return first.equals(other.first) && second.equals(other.second);
    }

}
