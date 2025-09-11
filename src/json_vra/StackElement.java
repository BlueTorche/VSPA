package json_vra;

import java.util.Map;
import java.util.Set;

import automaton.State;
import json_vspa.Reach;
import json_vspa.Vertex;

public class StackElement {
    public Map<State, Set<State>> R;
    public Set<String> K ;
    public String k;
    public Set<Vertex> Good;

    public StackElement(Map<State, Set<State>> R, Set<String> K, String k, Set<Vertex> Good) {
        this.R = R;
        this.K = K;
        this.k = k;
        this.Good = Good;
    }

    @Override
    public String toString() {
        return R.toString() + " ; " + K.toString() + " ; " + k + " ; " + Good.toString();
    }
}
