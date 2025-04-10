package vspa;

import automaton.FiniteAutomaton;

public class ProceduralAutomaton extends FiniteAutomaton {
    private final String proceduralSymbol;

    public ProceduralAutomaton(String proceduralSymbol) {
        this.proceduralSymbol = proceduralSymbol;
    }

    public String getProceduralSymbol() {
        return proceduralSymbol;
    }
}
