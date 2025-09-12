package vspa;

import automaton.State;

import java.util.Set;
import java.util.stream.Collectors;

public class VRA_State extends State<VRA_State> {
    private ProceduralAutomaton proceduralAutomaton;

    public VRA_State(String name, boolean isFinal) {
        super(name, isFinal);
    }

    public VRA_State(String name, boolean isFinal, ProceduralAutomaton proceduralAutomaton) {
        super(name, isFinal);
        this.proceduralAutomaton = proceduralAutomaton;
    }

    public ProceduralAutomaton getProceduralSymbol() {
        return proceduralAutomaton;
    }

    public void setProceduralAutomaton(ProceduralAutomaton proceduralAutomaton) {
        this.proceduralAutomaton = proceduralAutomaton;
    }

    public Set<String> getProceduralTransitions(VSPAAlphabet alphabet) {
        return this.getTransitionsSymbols().stream()
                .filter(symbol -> alphabet.kindOfSymbol(symbol) == VSPAAlphabet.SymbolType.PROCEDURAL)
                .collect(Collectors.toSet());
    }
}
