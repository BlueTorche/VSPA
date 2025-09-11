package json_vra;

import java.util.*;

import automaton.State;
import test.TestResult;
import utils.Pair;

import vspa.VisiblySystemProceduralAutomata;
import json_vspa.JSONProceduralAutomaton;
import json_vspa.Reach;
import json_vspa.Vertex;

public class JSON_VRA extends VisiblySystemProceduralAutomata{
    private JSONProceduralAutomaton startingAutomaton;
    private final Map<String, JSONProceduralAutomaton> proceduralAutomata = new HashMap<>();

    public void setStartingAutomaton(JSONProceduralAutomaton startingAutomaton) {
        this.startingAutomaton = startingAutomaton;
    }

    public void addProceduralAutomaton(JSONProceduralAutomaton nfa, String callSymbol) {
        proceduralAutomata.put(nfa.getProceduralSymbol(), nfa);
        linkingFunction.put(nfa.getProceduralSymbol(), callSymbol);
    }

    public void createKeyGraphs(Set<String> keySymbols) {
        for (JSONProceduralAutomaton pa : proceduralAutomata.values()) {
            if (linkingFunction.get(pa.getProceduralSymbol()).equals("{")) {
                pa.createKeyGraph(keySymbols);
            }
        }
    }

    public int getKeyGraphSize() {
        int size = 0;
        for (JSONProceduralAutomaton pa : proceduralAutomata.values()) {
            if (pa.getKeyGraph() != null) {
                size += pa.getKeyGraph().getVertices().size();
            }
        }
        return size;
    }

    public Pair<Boolean,Long> accepts(List<String> word, boolean measureMemory, boolean debug) {
        final long memoryStart;
        long maxMemory;

        if (measureMemory) {
            memoryStart = TestResult.getMemoryUse();
            maxMemory = memoryStart;
        } else {
            memoryStart = maxMemory = 0;
        }

        List<StackElement> stack = new ArrayList<>();
        Map<State, Set<State>> reach = new HashMap<>();
        reach.computeIfAbsent(startingAutomaton.getInitalState(), _ -> new HashSet<>()).add(startingAutomaton.getInitalState());
        Set<String> K = new HashSet<>();
        String k = "";
        Set<Vertex> Good = new HashSet<>();

        for (int i = 0; i < word.size() ; i++) {
            if (measureMemory) {
                System.gc();
            }

            String symbol = word.get(i);
            if (debug) debug(symbol, stack, reach, K, k, Good);

            Map<State, Set<State>> nextR = new HashMap<>();

            if (symbol.equals("[") || symbol.equals("{")) {
                String nextSymbol = word.get(i + 1);
                stack.add(new StackElement(reach, K, k, Good));
                K = new HashSet<>();
                Good = new HashSet<>();
                k = nextSymbol;
                for (State state : reach.keySet()) {
                    for (String procSymbol : state.getTransitionsSymbols()) {
                        if (linkingFunction.containsKey(procSymbol) && linkingFunction.get(procSymbol).equals(symbol)) {
                            JSONProceduralAutomaton pa = proceduralAutomata.get(procSymbol);
                            if (symbol.equals("{") && !nextSymbol.equals("}")) {
                                K.add(nextSymbol);
                                for (Vertex vertex : pa.getKeyGraph().getVerticesByKey(nextSymbol)) {
                                    nextR.computeIfAbsent(vertex.startState, _ -> new HashSet<>()).add(vertex.startState);
                                }
                            } else {
                                nextR.computeIfAbsent(pa.getInitalState(), _ -> new HashSet<>()).add(pa.getInitalState());
                            }
                        }
                    }
                }
            }

            else if ((alphabet.getInternalSymbols().contains(symbol) && !symbol.equals("#")) ||
                    (symbol.equals("#") && K.isEmpty())) {

                for (State end_state : reach.keySet()) {
                    for (State q : end_state.getTransitions(symbol)) {
                        nextR.computeIfAbsent(q, _ -> new HashSet<>()).addAll(reach.get(end_state));
                    }
                }
            }

            else if (symbol.equals("#")) {
                String nextSymbol = word.get(i + 1);
                for (State end_state : reach.keySet()) {
                    for (State start_state : reach.get(end_state))
                        Good.add(new Vertex(start_state, end_state, k));

                    String procSymbol = end_state.getProceduralSymbol();
                    for (Vertex vertex : proceduralAutomata.get(procSymbol).getKeyGraph().getVerticesByKey(nextSymbol)) {
                        nextR.computeIfAbsent(vertex.startState, _ -> new HashSet<>()).add(vertex.startState);
                    }
                }

                K.add(nextSymbol);
                k = nextSymbol;
            }

            else if (symbol.equals("]") || (symbol.equals("}"))) {
                if (stack.isEmpty()) return new Pair<>(false, maxMemory-memoryStart);

                Set<String> finalSymbols = new HashSet<>();
                if (K.isEmpty()) {
                    for (State state : reach.keySet()) {
                        if (state.isFinal() && alphabet.getReturnFromCallSymbol(linkingFunction.get(state.getProceduralSymbol())).equals(symbol)) {
                            finalSymbols.add(state.getProceduralSymbol());
                        }
                    }
                } else {
                    for (State end_state : reach.keySet()) {
                        for (State start_state : reach.get(end_state))
                            Good.add(new Vertex(start_state, end_state, k));
                    }
                    for (State state : reach.keySet()) {
                        String procSymbol = state.getProceduralSymbol();
                        JSONProceduralAutomaton pa = proceduralAutomata.get(procSymbol);
                        if (pa.getKeyGraph() != null && pa.getKeyGraph().Valid(K, Good)) {
                            finalSymbols.add(procSymbol);
                        }
                    }
                }

                StackElement stackElement = stack.removeLast();
                K = stackElement.K;
                k = stackElement.k;
                Good = stackElement.Good;
                reach = stackElement.R;

                for (String procSymbol : finalSymbols) {
                    for (State end_state : reach.keySet()) {
                        for (State q : end_state.getTransitions(procSymbol)) {
                            nextR.computeIfAbsent(q, _ -> new HashSet<>()).addAll(reach.get(end_state));
                        }
                    }
                }
            }

            reach = nextR;

            if (measureMemory) {
                maxMemory = Math.max(maxMemory, TestResult.getMemoryUse());
            }

            if (reach.isEmpty()) {
                return new Pair<>(false, maxMemory-memoryStart);
            }
        }

        if (measureMemory) {
            System.gc();
        }

        boolean isAccepted = false;
        for (State state : reach.keySet()) {
            isAccepted = state.isFinal() || isAccepted;
        }

        if (measureMemory) {
            maxMemory = Math.max(maxMemory, TestResult.getMemoryUse());
        }

        return new Pair<Boolean,Long>(isAccepted, maxMemory-memoryStart);
    }

    private void debug(String symbol, List<StackElement> stack, Map<State, Set<State>> R, Set<String> K, String k, Set<Vertex> Good) {
        System.out.println("Symbol : " + symbol);
        System.out.println("\nReach: " + R.toString());
        if (!stack.isEmpty())
            System.out.println("Top Stack : " + stack.getLast().toString());
        else
            System.out.println("Stack : empty");
        System.out.println("K: " + K.toString());
        System.out.println("k: " + k);
        System.out.println("Good: " + Good.toString());
        System.out.println("\n");
    }
}
