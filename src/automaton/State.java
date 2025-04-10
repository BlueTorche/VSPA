package automaton;

import java.util.*;

public class State {
    private final String name;
    private final boolean isFinal;
    private final Map<String, Set<State>> transitions = new HashMap<>();


    public State(String name, boolean isFinal) {
        this.name = name;
        this.isFinal = isFinal;
    }

    public String getName() {
        return name;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void addTransition(String symbol, State target) {
        transitions.computeIfAbsent(symbol, _ -> new HashSet<>()).add(target);
    }

    public Set<State> getTransitions(String symbol) {
        return transitions.getOrDefault(symbol, Collections.emptySet());
    }
    
    public Map<String, Set<State>> getAllTransitions() {
        return transitions;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof State)) return false;
        State other = (State) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + (isFinal ? " (final)" : "");
    }
}
