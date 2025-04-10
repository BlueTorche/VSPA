package vspa;

import automaton.*;
import java.util.*;

public class VisiblySystemProceduralAutomata {
    private final Set<ProceduralAutomaton> proceduralAutomata = new HashSet<>();
    private final Map<ProceduralAutomaton, String> linkingFunction = new HashMap<>();
    private VSPAAlphabet alphabet;
    private ProceduralAutomaton startingAutomaton;

    public void setVSPAAlphabet(VSPAAlphabet alphabet) {
        this.alphabet = alphabet;
    }

    public void addProceduralAutomaton(ProceduralAutomaton nfa, String callSymbol) {
        proceduralAutomata.add(nfa);
        linkingFunction.put(nfa, callSymbol);
    }

    public void setStartingAutomaton(ProceduralAutomaton nfa) {
        startingAutomaton = nfa;
    }

    public boolean accepts(String word) {
        if (!linkingFunction.get(startingAutomaton).equals(String.valueOf(word.charAt(0)))) {
            return false;
        }

        Set<Map.Entry<State, String>> currentStates = Set.of(
            new AbstractMap.SimpleEntry<>(startingAutomaton.getInitalState(), startingAutomaton.getProceduralSymbol()));
        List<Set<Map.Entry<State, String>>> stack = new ArrayList<>();

        for (int i = 1; i < word.length() - 1 ; i++) {
            String symbol = String.valueOf(word.charAt(i));
            Set<Map.Entry<State, String>> nextStates = new HashSet<>();
            if (alphabet.getInternalSymbols().contains(symbol)) {
                for (Map.Entry<State, String> pair : currentStates) {
                    State state = pair.getKey();
                    for (State q : state.getTransitions(symbol))
                        nextStates.add(new AbstractMap.SimpleEntry<>(q, pair.getValue()));
                }
            } else if (alphabet.getCallSymbols().contains(symbol)) {
                stack.add(currentStates);
                for (Map.Entry<State, String> pair : currentStates) {
                    State state = pair.getKey();
                    for (ProceduralAutomaton pa : proceduralAutomata) {
                        String procSymbol = pa.getProceduralSymbol();
                        if (linkingFunction.get(pa).equals(symbol) &&
                                !state.getTransitions(procSymbol).isEmpty()) {
                                    nextStates.add(new AbstractMap.SimpleEntry<>(pa.getInitalState(), procSymbol));
                        }
                    }
                }
            } else if (alphabet.getReturnSymbols().contains(symbol)) {
                if (stack.isEmpty()) return false;
                Set<Map.Entry<State, String>> possibleNextState = stack.removeLast();
                for (Map.Entry<State, String> pair : currentStates) {
                    State state = pair.getKey();
                    if (state.isFinal()) { // && bar f(j) = symbol ! 
                        for (Map.Entry<State, String> pair2 : possibleNextState) {
                            for (State q : pair2.getKey().getTransitions(pair.getValue())) {
                                nextStates.add(new AbstractMap.SimpleEntry<>(q, pair2.getValue()));
                            }
                        }
                    }
                }
            }
            currentStates = nextStates;
        }
        if (alphabet.getReturnFromCallSymbol(linkingFunction.get(startingAutomaton)).equals(String.valueOf(word.charAt(word.length() - 1))) && 
                stack.isEmpty()) {
            for (Map.Entry<State, String> pair : currentStates) {
                if (pair.getKey().isFinal()) 
                    return true;
            }
        }
        return false;
    }
}
