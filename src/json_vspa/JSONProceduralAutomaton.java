package json_vspa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vspa.VRA_State;
import vspa.ProceduralAutomaton;

public class JSONProceduralAutomaton extends ProceduralAutomaton {
    KeyGraph<VRA_State> keyGraph;

    public JSONProceduralAutomaton(String proceduralSymbol, VRA_State initialState) {
        super(proceduralSymbol, initialState);
    }

    public void createKeyGraph(Set<String> keySymbols) {
        keyGraph = new KeyGraph<>();

        Set<Vertex<VRA_State>> vertices = new HashSet<>();

        Set<VRA_State> visited = new HashSet<>();
        List<VRA_State> states = new ArrayList<>();
        states.add(this.getInitalState());
        visited.add(this.getInitalState());

        while (!states.isEmpty()) {
            VRA_State state = states.removeFirst();
            for (String key : keySymbols) {
                for (VRA_State successor : state.getTransitions(key)) {
                    for (Set<VRA_State> endStates : successor.getAllTransitions().values()) {
                        for (VRA_State endState : endStates) {
                            vertices.add(new Vertex<>(state, endState, key));
                            
                            for (VRA_State nextState : endState.getTransitions("#")) {
                                if (!visited.contains(nextState)) {
                                    visited.add(nextState);
                                    states.add(nextState);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Vertex<VRA_State> v1 : vertices) {
            keyGraph.addVertex(v1);
            for (Vertex<VRA_State> v2 : vertices) {
                if (v1.endState.getTransitions("#").contains(v2.startState)) {
                    keyGraph.addEdge(v1, v2);
                }
            }
            if (v1.startState == this.getInitalState()) {
                keyGraph.addInitialVertex(v1);
            }
        }
    }

    public KeyGraph<VRA_State> getKeyGraph() {
        return keyGraph;
    }
}
