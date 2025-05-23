package json_vspa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeyGraph {
    Map<Vertex, Set<Vertex>> edges = new HashMap<>();
    Map<String, Set<Vertex>> vertexByKey = new HashMap<>();

    Set<Vertex> initialVertices = new HashSet<>();

    public void addEdge(Vertex source, Vertex target) {
        addVertex(source);
        addVertex(target);
        edges.get(source).add(target);
    }

    public void addVertex(Vertex vertex) {
        if (!edges.containsKey(vertex)) {
            edges.put(vertex, new HashSet<>());

            if (!vertexByKey.containsKey(vertex.key)) {
                vertexByKey.put(vertex.key, new HashSet<>());
            }
            vertexByKey.get(vertex.key).add(vertex);
        }

    }

    public void addInitialVertex(Vertex vertex) {
        initialVertices.add(vertex);
    }

    public boolean Valid(Set<String> keys, Set<Vertex> Good) {
        Map<Vertex, Integer> depth = new HashMap<>();
        int n = keys.size();

        List<Vertex> stack = new ArrayList<>();
        for (Vertex vertex : Good) {
            if (initialVertices.contains(vertex)) {
                stack.add(vertex);
                depth.put(vertex, 1);
                if (keys.size() == 1 && vertex.endState.isFinal())
                    return true;
            }
        }

        while (!stack.isEmpty()) {
            Vertex nextVertex = stack.remove(stack.size() - 1);
            int currentDepth = depth.get(nextVertex);

            if (currentDepth == n) {
                continue;
            }

            for (Vertex neighbor : edges.get(nextVertex)) {
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

    public Set<Vertex> getVerticesByKey(String key) {
        return vertexByKey.getOrDefault(key, new HashSet<>());
    }

    public Set<Vertex> getVertices() {
        return edges.keySet();
    }
    
    public Set<Vertex> getEdges(Vertex node) {
        return edges.get(node);
    }

    public boolean isInitialVertex(Vertex v) {
        return initialVertices.contains(v);
    }
}
