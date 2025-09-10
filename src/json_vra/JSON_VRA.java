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
        Set<Reach> reach = new HashSet<>();
        reach.add(new Reach(startingAutomaton.getInitalState(), startingAutomaton.getInitalState()));
        Set<String> K = new HashSet<>();
        String k = "";
        Set<Vertex> Good = new HashSet<>();

        for (int i = 1; i < word.size() - 1 ; i++) {
            if (measureMemory) {
                System.gc();
            }

            String symbol = word.get(i);
            if (debug) debug(symbol, stack, reach, K, k, Good);

            Set<Reach> nextR = new HashSet<>();

            if (symbol.equals("[")) {
                stack.add(new StackElement(reach, K, k, Good));
                for (Reach r : reach) {
                    State state = r.second;
                    for (String procSymbol : state.getTransitionsSymbols()) {
                        if (linkingFunction.containsKey(procSymbol) && linkingFunction.get(procSymbol).equals(symbol)) {
                            nextR.add(new Reach(proceduralAutomata.get(procSymbol).getInitalState(), proceduralAutomata.get(procSymbol).getInitalState()));
                        }
                    }
                }
            }

            else if (symbol.equals("{")) {
                String nextSymbol = word.get(i + 1);
                stack.add(new StackElement(reach, K, k, Good));
                K = new HashSet<>();
                Good = new HashSet<>();

                if (nextSymbol.equals("}")) {
                    for (Reach r : reach) {
                        State state = r.second;
                        for (String procSymbol : state.getTransitionsSymbols()) {
                            if (linkingFunction.containsKey(procSymbol) && linkingFunction.get(procSymbol).equals(symbol)) {
                                nextR.add(new Reach(proceduralAutomata.get(procSymbol).getInitalState(), proceduralAutomata.get(procSymbol).getInitalState()));
                            }
                        }
                    }
                }
                else {
                    K.add(nextSymbol);
                    k = nextSymbol;

                    for (Reach r : reach) {
                        State state = r.second;
                        for (String procSymbol : state.getTransitionsSymbols()) {
                            if (linkingFunction.containsKey(procSymbol) && linkingFunction.get(procSymbol).equals(symbol)) {
                                JSONProceduralAutomaton pa = proceduralAutomata.get(procSymbol);
                                for (Vertex vertex : pa.getKeyGraph().getVerticesByKey(nextSymbol)) {
                                    nextR.add(new Reach(vertex.startState, vertex.startState));
                                }
                            }
                        }
                    }
                }
            }

            else if ((alphabet.getInternalSymbols().contains(symbol) && !symbol.equals("#")) ||
                    (symbol.equals("#") && reach.isEmpty())) {

                for (Reach r : reach) {
                    for (State q : r.second.getTransitions(symbol))
                        nextR.add(new Reach(r.first, q));
                }
            }

            else if (symbol.equals("#")) {
                for (Reach r : reach) {
                    Good.add(new Vertex(r.first, r.second, k));
                }
                String nextSymbol = word.get(i + 1);
                K.add(nextSymbol);
                k = nextSymbol;

                for (Reach r : reach) {
                    State state = r.second;
                    String procSymbol = state.getProceduralSymbol();
                    for (Vertex vertex : proceduralAutomata.get(procSymbol).getKeyGraph().getVerticesByKey(nextSymbol)) {
                        nextR.add(new Reach(vertex.startState, vertex.startState));
                    }
                }
            }

            else if (symbol.equals("]") || (symbol.equals("}") && reach.isEmpty())) {
                if (stack.isEmpty()) return new Pair<Boolean,Long>(false, maxMemory-memoryStart);

                Set<String> finalSymbols = new HashSet<>();
                for (Reach r : reach) {
                    State state = r.second;
                    if (state.isFinal() && alphabet.getReturnFromCallSymbol(linkingFunction.get(state.getProceduralSymbol())).equals(symbol)) {
                        finalSymbols.add(state.getProceduralSymbol());
                    }
                }

                StackElement stackElement = stack.removeLast();
                K = stackElement.K;
                k = stackElement.k;
                Good = stackElement.Good;
                reach = stackElement.R;

                for (String procSymbol : finalSymbols) {
                    for (Reach r : reach) {
                        for (State q : r.second.getTransitions(procSymbol)) {
                                nextR.add(new Reach(r.first, r.second));
                        }
                    }
                }
            }

            else if (symbol.equals("}")) {
                if (stack.isEmpty()) return new Pair<Boolean,Long>(false, maxMemory-memoryStart);

                for (Reach r : reach) {
                    Good.add(new Vertex(r.first, r.second, k));
                }

                // Recuperation of active procedural automata
                Set<String> finalSymbols = new HashSet<>();
                for (Reach r : reach) {
                    String procSymbol = r.second.getProceduralSymbol();
                    JSONProceduralAutomaton pa = proceduralAutomata.get(procSymbol);
                    if (pa.getKeyGraph() != null && pa.getKeyGraph().Valid(K, Good)) {
                        finalSymbols.add(procSymbol);
                    }
                }

                StackElement stackElement = stack.removeLast();
                reach = stackElement.R;
                k = stackElement.k;
                K = stackElement.K;
                Good = stackElement.Good;

                for (String procSymbol : finalSymbols) {
                    for (Reach r : reach) {
                        for (State q : r.second.getTransitions(procSymbol)) {
                            nextR.add(new Reach(r.first, q));
                        }
                    }
                }
            }

            reach = nextR;

            if (measureMemory) {
                maxMemory = Math.max(maxMemory, TestResult.getMemoryUse());
            }

            if (reach.isEmpty()) {
                return new Pair<Boolean,Long>(false, maxMemory-memoryStart);
            }
        }

        if (measureMemory) {
            System.gc();
        }

        boolean isAccepted = false;
        for (Reach r : reach) {
            isAccepted = r.second.isFinal() || isAccepted;
        }

        if (measureMemory) {
            maxMemory = Math.max(maxMemory, TestResult.getMemoryUse());
        }

        return new Pair<Boolean,Long>(isAccepted, maxMemory-memoryStart);
    }

    private void debug(String symbol, List<StackElement> stack, Set<Reach> R, Set<String> K, String k, Set<Vertex> Good) {
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
