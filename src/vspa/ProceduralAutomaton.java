package vspa;

import automaton.FiniteAutomaton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProceduralAutomaton extends FiniteAutomaton<VRA_State> {
    private final String proceduralSymbol;

    public ProceduralAutomaton(String proceduralSymbol, VRA_State initialState) {
        super(initialState);
        this.proceduralSymbol = proceduralSymbol;
        fillProceduralAutomaton();
    }

    public String getProceduralSymbol() {
        return proceduralSymbol;
    }

    private void fillProceduralAutomaton() {
        Set<VRA_State> visited = new HashSet<>();
        VRA_State s = this.getInitalState();
        List<VRA_State> toVisit = new ArrayList<>();
        toVisit.add(s);
        while (!toVisit.isEmpty()) {
            s = toVisit.removeLast();
            if (visited.contains(s)) continue;
            System.out.println(s);
            s.setProceduralAutomaton(this);
            visited.add(s);
            for (Set<VRA_State> states : s.getAllTransitions().values()) {
                for (VRA_State state : states) {
                    if (!visited.contains(state)) {
                        toVisit.add(state);
                    }
                }
            }
        }
    }
}
