package automaton;

import java.util.*;

public class FiniteAutomaton {
    private State initialState;

    public void setInitialState(State state) {
        initialState = state;
    }

    public void addTransition(State from, String symbol, State to) {
        from.addTransition(symbol, to);
    }

    public boolean accepts(String input) {
        Set<State> currentStates = Set.of(initialState);

        for (char c : input.toCharArray()) {
            String symbol =  String.valueOf(c);
            Set<State> nextStates = new HashSet<>();
            for (State state : currentStates) {
                nextStates.addAll(state.getTransitions(symbol));
            }
            currentStates = nextStates;
        }

        return currentStates.stream().anyMatch(State::isFinal);
    }

    public State getInitalState() {
        return this.initialState;
    }
}
