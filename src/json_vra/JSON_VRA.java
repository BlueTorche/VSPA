package json_vra;

import java.util.*;

import automaton.State;

import com.google.common.testing.GcFinalization;
import test.TestResult;
import utils.Pair;
import vspa.ProceduralAutomaton;
import vspa.VRA_State;
import vspa.VSPAAlphabet;
import vspa.VisiblySystemProceduralAutomata;
import json_vspa.JSONProceduralAutomaton;
import json_vspa.Vertex;

public class JSON_VRA extends VisiblySystemProceduralAutomata {
    private JSONProceduralAutomaton startingAutomaton;
    private final Map<String, JSONProceduralAutomaton> proceduralAutomata = new HashMap<>();
    public long maxTimeKeyGraph = 0;
    public long totalTimeKeyGraph = 0;

    public JSON_VRA(VSPAAlphabet alphabet) {
        super(alphabet);
    }

    public void setStartingAutomaton(JSONProceduralAutomaton startingAutomaton) {
        this.startingAutomaton = startingAutomaton;
    }


    public void addProceduralAutomaton(JSONProceduralAutomaton nfa, String callSymbol) {
        proceduralAutomata.put(nfa.getProceduralSymbol(), nfa);
        super.addProceduralAutomaton(nfa, callSymbol);
    }

    public void createKeyGraphs(List<String> keySymbols) {
        for (JSONProceduralAutomaton pa : proceduralAutomata.values()) {
            if (alphabet.getCallFromProcedural(pa.getProceduralSymbol()).equals("{")) {
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

    /*
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
        Set<Vertex<State>> Good = new HashSet<>();

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
                                for (Vertex<State> vertex : pa.getKeyGraph().getVerticesByKey(nextSymbol)) {
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
                            Good.add(new Vertex<>(start_state, end_state, k));
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
    */

    public ValidationState<VRA_State> getInitialState() {
        final Set<VRA_State> setWithInitialLocation = new LinkedHashSet<>();
        setWithInitialLocation.add(startingAutomaton.getInitalState());
        return new ValidationState<>(PairSourceToReached.getIdentityPairs(setWithInitialLocation), null);
    }

    public ValidationState<VRA_State> getSuccessor(ValidationState<VRA_State> state, String symbol, String nextSymbol) {
        return switch (alphabet.kindOfSymbol(symbol)) {
            case CALL -> getCallSuccessor(state, symbol, nextSymbol);
            case RETURN -> getReturnSuccessor(state, symbol);
            case INTERNAL -> getInternalSuccessor(state, symbol, nextSymbol);
            default -> null;
        };
    }

    private ValidationState<VRA_State> getInternalSuccessor(ValidationState<VRA_State> state, String symbol, String nextSymbol) {
        if (symbol.equals("#")
                && !state.getStack().peekSeenKeys().isEmpty()) {
            return getCommaInObjectSuccessor(state, nextSymbol);
        }

        final Set<PairSourceToReached<VRA_State>> sourceToReachedLocations = state.getSourceToReachedLocations();
        final Set<PairSourceToReached<VRA_State>> successorSourceToReachedLocations = new LinkedHashSet<>();

        for (PairSourceToReached<VRA_State> p : sourceToReachedLocations) {
            for (VRA_State reached : p.getReachedLocation().getTransitions(symbol))
                successorSourceToReachedLocations.add(PairSourceToReached.of(p.getSourceLocation(), reached));
        }

        return new ValidationState<>(successorSourceToReachedLocations, state.getStack());
    }

    private ValidationState<VRA_State> getCommaInObjectSuccessor(ValidationState<VRA_State> state, String nextSymbol) {
        final Set<PairSourceToReached<VRA_State>> sourceToReachedLocations = state.getSourceToReachedLocations();
        final ValidationStackContents<VRA_State> currentStack = state.getStack();
        final String currentKey = currentStack.peekCurrentKey();

        for (PairSourceToReached<VRA_State> p : sourceToReachedLocations) {
            currentStack.markAccepted(new Vertex<>(p.getSourceLocation(), p.getReachedLocation(), currentKey));
        }

        if (!currentStack.addKey(nextSymbol))
            return null;

        final Set<PairSourceToReached<VRA_State>> successorSourceToReachedLocations = new LinkedHashSet<>();

        for (JSONProceduralAutomaton pa : proceduralAutomata.values()) {
            if (pa.getKeyGraph() != null) {
                successorSourceToReachedLocations.addAll(
                        PairSourceToReached.getIdentityPairs(pa.getKeyGraph().getLocationsReadingKey(nextSymbol)));
            }
        }

        return new ValidationState<>(successorSourceToReachedLocations, state.getStack());
    }

    private ValidationState<VRA_State> getCallSuccessor(ValidationState<VRA_State> state, String callSymbol, String nextSymbol) {
        final Set<PairSourceToReached<VRA_State>> sourceToReachedLocations = state.getSourceToReachedLocations();
        final ValidationStackContents<VRA_State> currentStack = state.getStack();
        final ValidationStackContents<VRA_State> newStack = ValidationStackContents.push(sourceToReachedLocations, currentStack);

        final Set<PairSourceToReached<VRA_State>> successorSourceToReachedLocations = new LinkedHashSet<>();

        for (String procSymbol : alphabet.getProceduralsFromCall(callSymbol)) {
            if (alphabet.getCallFromProcedural(procSymbol).equals(callSymbol)) {
                JSONProceduralAutomaton pa = proceduralAutomata.get(procSymbol);
                if (callSymbol.equals("{") && !nextSymbol.equals("}")) {
                    newStack.addKey(nextSymbol);
                    successorSourceToReachedLocations.addAll(
                            PairSourceToReached.getIdentityPairs(pa.getKeyGraph().getLocationsReadingKey(nextSymbol)));
                } else {
                    successorSourceToReachedLocations.add(PairSourceToReached.of(pa.getInitalState(),pa.getInitalState()));
                }
            }
        }

        if (successorSourceToReachedLocations.isEmpty())
            return null;
        return new ValidationState<>(successorSourceToReachedLocations, newStack);
    }

    private ValidationState<VRA_State> getReturnSuccessor(ValidationState<VRA_State> state, String symbol) {
        final ValidationStackContents<VRA_State> currentStack = state.getStack();
        if (currentStack == null) {
            return null;
        }
        Set<String> finalProceduralSymbols;
        if (symbol.equals("}") && !currentStack.peekSeenKeys().isEmpty()) {
            finalProceduralSymbols = getEndObject(state);
        } else {
            final Set<PairSourceToReached<VRA_State>> sourceToReachedLocations = state.getSourceToReachedLocations();
            finalProceduralSymbols = new LinkedHashSet<>();

            for (PairSourceToReached<VRA_State> p : sourceToReachedLocations) {
                VRA_State reached = p.getReachedLocation();
                if (reached.isFinal() &&
                        alphabet.getReturnFromProcedural(reached.getProceduralAutomaton().getProceduralSymbol()).equals(symbol)) {
                    finalProceduralSymbols.add(reached.getProceduralAutomaton().getProceduralSymbol());
                }
            }
        }

        final Set<PairSourceToReached<VRA_State>> sourceToReachedLocationsBeforeCall = currentStack
                .peekSourceToReachedLocationsBeforeCall();
        final Set<PairSourceToReached<VRA_State>> successorSourceToReachedLocations = new LinkedHashSet<>();

        for (PairSourceToReached<VRA_State> p : sourceToReachedLocationsBeforeCall) {
            for (String proceduralSymbols : finalProceduralSymbols) {
                for (VRA_State reached : p.getReachedLocation().getTransitions(proceduralSymbols))
                    successorSourceToReachedLocations.add(PairSourceToReached.of(p.getSourceLocation(), reached));
            }
        }

        if (successorSourceToReachedLocations.isEmpty()) {
            return null;
        }
        return new ValidationState<>(successorSourceToReachedLocations, currentStack.pop());
    }

    private Set<String> getEndObject(ValidationState<VRA_State> state) {
        final long start_time = System.nanoTime();

        final Set<PairSourceToReached<VRA_State>> sourceToReachedLocations = state.getSourceToReachedLocations();
        final ValidationStackContents<VRA_State> currentStack = state.getStack();
        final String currentKey = currentStack.peekCurrentKey();

        final Set<JSONProceduralAutomaton> potentialFinalProceduralAutomata = new LinkedHashSet<>();

        for (PairSourceToReached<VRA_State> p : sourceToReachedLocations) {
            currentStack.markAccepted(new Vertex<>(p.getSourceLocation(), p.getReachedLocation(), currentKey));
            potentialFinalProceduralAutomata.add((JSONProceduralAutomaton) p.getReachedLocation().getProceduralAutomaton());
        }

        final Set<String> finalProceduralSymbols = new LinkedHashSet<>();

        for (JSONProceduralAutomaton pa : potentialFinalProceduralAutomata) {
            if (pa.getKeyGraph() != null && pa.getKeyGraph().Valid(currentStack.peekSeenKeys(), currentStack.peekAcceptedNodes())) {
                finalProceduralSymbols.add(pa.getProceduralSymbol());
            }
        }

        final long tot_time = System.nanoTime()-start_time;
        maxTimeKeyGraph = Math.max(maxTimeKeyGraph, tot_time);
        totalTimeKeyGraph += tot_time;

        return finalProceduralSymbols;
    }

    public boolean isFinal(ValidationState<VRA_State> state) {
        if (state == null || state.getStack() != null) {
            return false;
        }

        final Set<PairSourceToReached<VRA_State>> sourceToReachedLocations = state.getSourceToReachedLocations();
        for  (PairSourceToReached<VRA_State> p : sourceToReachedLocations) {
            if (p.getReachedLocation().isFinal()) {
                return true;
            }
        }

        return false;
    }

    /* private void debug(String symbol, List<StackElement> stack, Map<State, Set<State>> R, Set<String> K, String k, Set<Vertex> Good) {
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
    } */

    public static Pair<Boolean,Long> isAccepted(List<String> word, JSON_VRA vspa, boolean measureMemory, boolean DEBUG) {
        final long memoryStart;
        long maxMemory;

        vspa.maxTimeKeyGraph = 0;
        vspa.totalTimeKeyGraph = 0;

        if (measureMemory) {
            System.gc();
            GcFinalization.awaitFullGc();
            System.gc();
            memoryStart = TestResult.getMemoryUse();
            maxMemory = memoryStart;
        } else {
            memoryStart = maxMemory = 0;
        }

        ValidationState<VRA_State> state = vspa.getInitialState();
        String currentSymbol = word.getFirst();

        for (int i = 1; i < word.size(); i++) {
            if (measureMemory) {
                System.gc();
            }

            if (DEBUG) {
                System.out.println(currentSymbol + " : ");
                System.out.println("    " + state.getReachedLocations().toString());
                System.out.println("    " + (state.getStack() == null ? "null" : state.getStack().toString()));
            }
            final String nextSymbol = word.get(i);
            state = vspa.getSuccessor(state, currentSymbol, nextSymbol);
            currentSymbol = nextSymbol;

            if (measureMemory) {
                maxMemory = Math.max(maxMemory, TestResult.getMemoryUse());
            }

            if (state == null) {
                return new Pair<>(false, maxMemory-memoryStart);
            }
        }

        if (measureMemory) {
            System.gc();
        }

        if (DEBUG) {
            System.out.println(currentSymbol + " : ");
            System.out.println("    " + state.getReachedLocations().toString());
            System.out.println("    " + (state.getStack() == null ? "null" : state.getStack().toString()));
        }

        state = vspa.getSuccessor(state, currentSymbol, null);

        if (measureMemory) {
            maxMemory = Math.max(maxMemory, TestResult.getMemoryUse());
        }

        return new Pair<>(vspa.isFinal(state), maxMemory-memoryStart);
    }
}
