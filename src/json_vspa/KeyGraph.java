package json_vspa;

import automaton.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeyGraph<L extends State<L>> {
    Map<Vertex<L>, Set<Vertex<L>>> edges = new HashMap<>();
    Map<String, Set<Vertex<L>>> vertexByKey = new HashMap<>();
    Map<String, Set<L>> startingStateByKey = new HashMap<>();
    Set<Vertex<L>> initialVertices = new HashSet<>();

    final List<String> sortedKeySymbols;
    public KeyGraph(List<String> sortedKeySymbols) {
        this.sortedKeySymbols = sortedKeySymbols;
    }

    public void addEdge(Vertex<L> source, Vertex<L> target) {
        addVertex(source);
        addVertex(target);
        edges.get(source).add(target);
    }

    public void addVertex(Vertex<L> vertex) {
        if (!edges.containsKey(vertex)) {
            edges.put(vertex, new HashSet<>());

            if (!vertexByKey.containsKey(vertex.key)) {
                vertexByKey.put(vertex.key, new HashSet<>());
                startingStateByKey.put(vertex.key, new HashSet<>());
            }
            vertexByKey.get(vertex.key).add(vertex);
            startingStateByKey.get(vertex.key).add(vertex.startState);
        }

    }

    public void addInitialVertex(Vertex<L> vertex) {
        initialVertices.add(vertex);
    }

    /*
     * seenKeys must be a subset of sortedKeySymbols.
     * Good must be a subset of all vertices of the graph.
     */
    public boolean Valid(Set<String> seenKeys, Set<Vertex<L>> Good) {
        Set<Vertex<L>> vertexSet = new HashSet<>();
        boolean initialised = false;
        for (String keySymbol : sortedKeySymbols) {
            if (seenKeys.contains(keySymbol)) {
                if (vertexByKey.get(keySymbol) == null)
                    return false;

                if (!initialised) {
                    for (Vertex<L> vertex : vertexByKey.get(keySymbol)) {
                        if (Good.contains(vertex) && initialVertices.contains(vertex)) {
                            vertexSet.add(vertex);
                        }
                    }
                    initialised = true;
                }
                else {
                    final Set<Vertex<L>> nextVertexSet = new HashSet<>();
                    for (Vertex<L> nextVertex : vertexByKey.get(keySymbol)) {
                        for  (Vertex<L> prevVertex : vertexSet) {
                            if (edges.get(prevVertex).contains(nextVertex)) {
                                nextVertexSet.add(nextVertex);
                            }
                        }
                    }
                    vertexSet = nextVertexSet;
                }
            }
        }

        for (Vertex<L> vertex : vertexSet) {
            if(vertex.endState.isFinal())
                return true;
        }

        return false;
    }
/*
    public boolean Valid(Set<String> keys, Set<Vertex<L>> Good) {
        Map<Vertex<L>, Integer> depth = new HashMap<>();
        int n = keys.size();

        List<Vertex<L>> stack = new ArrayList<>();
        for (Vertex<L> vertex : Good) {
            if (initialVertices.contains(vertex)) {
                stack.add(vertex);
                depth.put(vertex, 1);
                if (keys.size() == 1 && vertex.endState.isFinal())
                    return true;
            }
        }

        while (!stack.isEmpty()) {
            Vertex<L> nextVertex = stack.removeLast();
            int currentDepth = depth.get(nextVertex);

            if (currentDepth == n) {
                continue;
            }

            for (Vertex<L> neighbor : edges.get(nextVertex)) {
                if (Good.contains(neighbor)) {
                    if (currentDepth + 1 == n) {
                        if (neighbor.endState.isFinal()) {
                            return true;
                        }
                    } else {
                        depth.put(neighbor, currentDepth + 1);
                        stack.add(neighbor);
                    }
                }
            }
        } 

        
        return false;
    }
*/
    public Set<Vertex<L>> getVerticesByKey(String key) {
        return vertexByKey.getOrDefault(key, new HashSet<>());
    }
    public Set<L> getLocationsReadingKey(String key) { return startingStateByKey.getOrDefault(key, new HashSet<>()); }

    public Set<Vertex<L>> getVertices() {
        return edges.keySet();
    }
    
    public Set<Vertex<L>> getEdges(Vertex<L> node) {
        return edges.get(node);
    }

    public boolean isInitialVertex(Vertex<L> v) {
        return initialVertices.contains(v);
    }
}
