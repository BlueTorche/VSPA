package json_vspa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import automaton.State;
import vspa.ProceduralAutomaton;

public class JSONProceduralAutomaton extends ProceduralAutomaton {
    KeyGraph keyGraph;

    public JSONProceduralAutomaton(String proceduralSymbol, State initialState) {
        super(proceduralSymbol, initialState);
    }

    public void createKeyGraph(Set<String> keySymbols) {
        keyGraph = new KeyGraph();

        Set<Vertex> vertices = new HashSet<>();

        Set<State> visited = new HashSet<>();
        List<State> states = new ArrayList<>();
        states.add(this.getInitalState());
        visited.add(this.getInitalState());

        while (!states.isEmpty()) {
            State state = states.remove(0);
            for (String key : keySymbols) {
                for (State successor : state.getTransitions(key)) {
                    for (Set<State> endStates : successor.getAllTransitions().values()) {
                        for (State endState : endStates) {
                            vertices.add(new Vertex(state, endState, key));
                            
                            for (State nextState : endState.getTransitions("#")) {
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

        for (Vertex v1 : vertices) {
            keyGraph.addVertex(v1);
            for (Vertex v2 : vertices) {
                if (v1.endState.getTransitions("#").contains(v2.startState)) {
                    keyGraph.addEdge(v1, v2);
                }
            }
            if (v1.startState == this.getInitalState()) {
                keyGraph.addInitialVertex(v1);
            }
        }
    }

    public KeyGraph getKeyGraph() {
        return keyGraph;
    }
}
