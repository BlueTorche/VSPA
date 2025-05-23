package json_vspa;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import automaton.State;
import vspa.VisiblySystemProceduralAutomata;

public class JSONVSPA extends VisiblySystemProceduralAutomata{
    private JSONProceduralAutomaton startingAutomaton;
    private Set<JSONProceduralAutomaton> proceduralAutomata = new HashSet<>();

    public void setStartingAutomaton(JSONProceduralAutomaton startingAutomaton) {
        this.startingAutomaton = startingAutomaton;
    }

    public void addProceduralAutomaton(JSONProceduralAutomaton nfa, String callSymbol) {
        proceduralAutomata.add(nfa);
        linkingFunction.put(nfa.getProceduralSymbol(), callSymbol);
    }

    public void createKeyGraphs(Set<String> keySymbols) {
        for (JSONProceduralAutomaton pa : proceduralAutomata) {
            if (linkingFunction.get(pa.getProceduralSymbol()).equals("{")) {
                pa.createKeyGraph(keySymbols);
            }
        }
    }

    public boolean accepts(List<String> word) {
        // for (Map.Entry<String, String> entry : linkingFunction.entrySet()) {
        //      System.out.println(entry.getKey() + " : " + entry.getValue());
        // }
        // System.out.println("Starting automaton: " + startingAutomaton.getProceduralSymbol());
        // System.out.print(linkingFunction.get(startingAutomaton.getProceduralSymbol()));
        // System.out.println(" " + word.get(0));
        if (!linkingFunction.get(startingAutomaton.getProceduralSymbol()).equals(word.get(0))) {
            return false;
        }

        Set<Map.Entry<State, String>> currentStates = new HashSet<>();
        List<StackElement> stack = new ArrayList<>();
        Set<Reach> R = new HashSet<>();
        Set<String> K = new HashSet<>();
        String k = "";
        Set<Vertex> Good = new HashSet<>();

        if (word.size() > 2) {
            String nextSymbol = String.valueOf(word.get(1));
            for (Vertex vertex : startingAutomaton.getKeyGraph().getVerticesByKey(nextSymbol)) {
                currentStates.add(new AbstractMap.SimpleEntry<>(vertex.startState, startingAutomaton.getProceduralSymbol()));
            }
            for (Map.Entry<State, String> state : currentStates) {
                R.add(new Reach(state.getKey(), state.getKey()));
            }
            K.add(nextSymbol);
            k = nextSymbol;
        }


        for (int i = 1; i < word.size() - 1 ; i++) {
            String symbol = word.get(i);

            // System.out.println("Symbol : " + symbol);
            // System.out.print("States : ");
            // for (Map.Entry<State, String> pair : currentStates) {
            //     System.out.print(pair.getKey().getName() + " ");
            // }
            // if (stack.size() > 0)
            //     System.out.println("\nTop Stack : " + stack.get(stack.size() - 1).toString());
            // else
            //     System.out.println("\nStack : empty");
            // System.out.println("R: " + R.toString());
            // System.out.println("K: " + K.toString());
            // System.out.println("k: " + k);
            // System.out.println("Good: " + Good.toString());
            // System.out.println("\n");

            Set<Map.Entry<State, String>> nextStates = new HashSet<>();
            Set<Reach> nextR = new HashSet<>();

            if (symbol.equals("[")) {
                stack.add(new StackElement(currentStates, R, K, k, Good));
                for (Map.Entry<State, String> pair : currentStates) {
                    State state = pair.getKey();
                    for (JSONProceduralAutomaton pa : proceduralAutomata) {
                        String procSymbol = pa.getProceduralSymbol();
                        if (linkingFunction.get(pa.getProceduralSymbol()).equals(symbol) &&
                                !state.getTransitions(procSymbol).isEmpty()) {
                                    nextStates.add(new AbstractMap.SimpleEntry<>(pa.getInitalState(), procSymbol));
                        }
                    }
                }
                R = new HashSet<>();
            }
            
            else if (symbol.equals("{")) {
                String nextSymbol = word.get(i + 1);
                stack.add(new StackElement(currentStates, R, K, k, Good));
                K = new HashSet<>();
                Good = new HashSet<>();

                if (nextSymbol.equals("}")) {
                    for (Map.Entry<State, String> pair : currentStates) {
                        State state = pair.getKey();
                        for (JSONProceduralAutomaton pa : proceduralAutomata) {
                            String procSymbol = pa.getProceduralSymbol();
                            if (linkingFunction.get(pa.getProceduralSymbol()).equals(symbol) &&
                                        !state.getTransitions(procSymbol).isEmpty()) {
                                nextStates.add(new AbstractMap.SimpleEntry<>(pa.getInitalState(), procSymbol));
                            }
                        }
                    }
                }
                else {
                    for (Map.Entry<State, String> pair : currentStates) {
                        State state = pair.getKey();
                        for (JSONProceduralAutomaton pa : proceduralAutomata) {
                            String procSymbol = pa.getProceduralSymbol();
                            if (linkingFunction.get(pa.getProceduralSymbol()).equals(symbol) &&
                                    !state.getTransitions(procSymbol).isEmpty()) {
                                for (Vertex vertex : pa.getKeyGraph().getVerticesByKey(nextSymbol)) {
                                    nextStates.add(new AbstractMap.SimpleEntry<>(vertex.startState, procSymbol));
                                }
                            }
                        }
                    }
                    
                    for (Map.Entry<State, String> state : nextStates) {
                        nextR.add(new Reach(state.getKey(), state.getKey()));
                    }
                    K.add(nextSymbol);
                    k = nextSymbol;
                }
            }

            else if ((alphabet.getInternalSymbols().contains(symbol) && !symbol.equals("#")) || 
                    (symbol.equals("#") && R.isEmpty())) {
                for (Map.Entry<State, String> pair : currentStates) {
                    State state = pair.getKey();
                    for (State q : state.getTransitions(symbol))
                        nextStates.add(new AbstractMap.SimpleEntry<>(q, pair.getValue()));
                }
                for (Reach reach : R) {
                    State state = reach.second;
                    for (State q : state.getTransitions(symbol))
                        nextR.add(new Reach(reach.first, q));
                }
            }

            else if (symbol.equals("#")) {
                for (Reach reach : R) {
                    Good.add(new Vertex(reach.first, reach.second, k));
                }
                String nextSymbol = word.get(i + 1);

                for (Map.Entry<State, String> pair : currentStates) {
                    String procSymbol = pair.getValue();
                    for (JSONProceduralAutomaton pa : proceduralAutomata) {
                        if (procSymbol.equals(pa.getProceduralSymbol())) {
                            for (Vertex vertex : pa.getKeyGraph().getVerticesByKey(nextSymbol)) {
                                nextStates.add(new AbstractMap.SimpleEntry<>(vertex.startState, procSymbol));
                            }
                        }
                    }
                }
                for (Map.Entry<State, String> state : nextStates) {
                    nextR.add(new Reach(state.getKey(), state.getKey()));
                }
                K.add(nextSymbol);
                k = nextSymbol;
            }

            else if (symbol.equals("]") || (symbol.equals("}") && R.isEmpty())) {
                if (stack.isEmpty()) return false;
                StackElement stackElement = stack.remove(stack.size() - 1);
                Set<Map.Entry<State, String>> possibleNextState = stackElement.states;
                R = stackElement.R;
                K = stackElement.K;
                k = stackElement.k;
                Good = stackElement.Good;

                for (Map.Entry<State, String> pair : currentStates) {
                    State state = pair.getKey();
                    if (state.isFinal() && alphabet.getReturnFromCallSymbol(linkingFunction.get(pair.getValue())).equals(symbol)) { 
                        for (Map.Entry<State, String> pair2 : possibleNextState) {
                            for (State q : pair2.getKey().getTransitions(pair.getValue())) {
                                nextStates.add(new AbstractMap.SimpleEntry<>(q, pair2.getValue()));

                                for (Reach reach : R) {
                                    if (pair2.getKey().equals(reach.second)) {
                                        nextR.add(new Reach(reach.first, q));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            else if (symbol.equals("}")) {
                for (Reach reach : R) {
                    Good.add(new Vertex(reach.first, reach.second, k));
                }
                StackElement stackElement = stack.remove(stack.size() - 1);
                Set<Map.Entry<State, String>> possibleNextState = stackElement.states;
                R = stackElement.R;
                k = stackElement.k;



                // Recuperation of active procedural automata 
                Set<String> procSymbols = new HashSet<>();
                for (Map.Entry<State, String> pair : currentStates) {
                    procSymbols.add(pair.getValue());
                }
                
                for (JSONProceduralAutomaton pa : proceduralAutomata) {
                    if (procSymbols.contains(pa.getProceduralSymbol())) {
                        if (pa.getKeyGraph() != null && pa.getKeyGraph().Valid(K, Good)) {
                            for (Map.Entry<State, String> pair : currentStates) {
                                if (pair.getValue().equals(pa.getProceduralSymbol())) {
                                    for (Map.Entry<State, String> pair2 : possibleNextState) {
                                        for (State q : pair2.getKey().getTransitions(pair.getValue())) {
                                            nextStates.add(new AbstractMap.SimpleEntry<>(q, pair2.getValue()));

                                            for (Reach reach : R) {
                                                if (pair2.getKey().equals(reach.second)) {
                                                    nextR.add(new Reach(reach.first, q));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                K = stackElement.K;
                Good = stackElement.Good;
            }

            currentStates = nextStates;
            R = nextR;
        }
        for (Reach reach : R) {
            Good.add(new Vertex(reach.first, reach.second, k));
        }

        // System.out.println("Symbol : " + word.get(word.size() - 1));
        // System.out.print("States : ");
        // for (Map.Entry<State, String> pair : currentStates) {
        //     System.out.print(pair.getKey().getName() + " ");
        // }
        // if (stack.size() > 0)
        //     System.out.println("\nTop Stack : " + stack.get(stack.size() - 1).toString());
        // else
        //     System.out.println("\nStack : empty");
        // System.out.println("R: " + R.toString());
        // System.out.println("K: " + K.toString());
        // System.out.println("k: " + k);
        // System.out.println("Good: " + Good.toString());
        // System.out.println("\n");

        if (alphabet.getReturnFromCallSymbol(linkingFunction.get(startingAutomaton.getProceduralSymbol())).equals(word.get(word.size() - 1)) && 
                stack.isEmpty() && 
                this.startingAutomaton.getKeyGraph().Valid(K, Good)) {
                    return true;
        }
        
        return false;
    }
}
