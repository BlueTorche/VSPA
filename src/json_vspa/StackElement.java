package json_vspa;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import automaton.State;

public class StackElement {
    public Set<Map.Entry<State, String>> states;
    public Set<Reach> R = new HashSet<>();
    public Set<String> K = new HashSet<>();
    public String k;
    public Set<Vertex> Good = new HashSet<>();

    public StackElement(Set<Map.Entry<State, String>> states, Set<Reach> R, Set<String> K, String k, Set<Vertex> Good) {
        this.states = states;
        this.R = R;
        this.K = K;
        this.k = k;
        this.Good = Good;
    }

    @Override
    public String toString() {
        return states.toString() + " ; " + R.toString() + " ; " + K.toString() + " ; " + k + " ; " + Good.toString();
    }
}
