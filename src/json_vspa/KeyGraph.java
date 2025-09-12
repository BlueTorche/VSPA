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
    Map<String, Set<L>> stateByKey = new HashMap<>();
    Set<Vertex<L>> initialVertices = new HashSet<>();

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
                stateByKey.put(vertex.key, new HashSet<>());
            }
            vertexByKey.get(vertex.key).add(vertex);
            stateByKey.get(vertex.key).add(vertex.startState);
        }

    }

    public void addInitialVertex(Vertex<L> vertex) {
        initialVertices.add(vertex);
    }

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

    public Set<Vertex<L>> getVerticesByKey(String key) {
        return vertexByKey.getOrDefault(key, new HashSet<>());
    }
    public Set<L> getLocationsReadingKey(String key) { return stateByKey.getOrDefault(key, new HashSet<>()); }

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
