package vspa;

import automaton.FiniteAutomaton;
import automaton.State;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProceduralAutomaton extends FiniteAutomaton {
    private final String proceduralSymbol;

    public ProceduralAutomaton(String proceduralSymbol, State initialState) {
        super(initialState);
        this.proceduralSymbol = proceduralSymbol;
        fillProceduralSymbols();
    }

    public String getProceduralSymbol() {
        return proceduralSymbol;
    }

    public void fillProceduralSymbols() {
        Set<State> visited = new HashSet<State>();
        State s = this.getInitalState();
        List<State> toVisit = new ArrayList<State>();
        toVisit.add(s);
        while (!toVisit.isEmpty()) {
            s = toVisit.removeLast();
            if (visited.contains(s)) continue;
            System.out.println(s);
            s.setProceduralSymbol(proceduralSymbol);
            visited.add(s);
            for (Set<State> states : s.getAllTransitions().values()) {
                for (State state : states) {
                    if (!visited.contains(state)) {
                        toVisit.add(state);
                    }
                }
            }
        }
    }
}
