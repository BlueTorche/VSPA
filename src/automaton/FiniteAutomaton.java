package automaton;

import java.util.*;

public class FiniteAutomaton<T extends State<T>> {
    private T initialState;

    public FiniteAutomaton() {};
    public FiniteAutomaton(T initialState) {
        this.initialState = initialState;
    }

    public void setInitialState(T state) {
        initialState = state;
    }

    public void addTransition(T from, String symbol, T to) {
        from.addTransition(symbol, to);
    }

    public boolean accepts(String input) {
        Set<T> currentStates = Set.of(initialState);

        for (char c : input.toCharArray()) {
            String symbol =  String.valueOf(c);
            Set<T> nextStates = new HashSet<>();
            for (T state : currentStates) {
                nextStates.addAll(state.getTransitions(symbol));
            }
            currentStates = nextStates;
        }

        return currentStates.stream().anyMatch(State::isFinal);
    }

    public T getInitalState() {
        return this.initialState;
    }
}
