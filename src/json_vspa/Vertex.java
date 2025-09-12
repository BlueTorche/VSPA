package json_vspa;
import automaton.State;

public class Vertex<L> {
    public L startState;
    public L endState;
    public String key;

    public Vertex(L startState, L endState, String key) {
        this.startState = startState;
        this.endState = endState;
        this.key = key;
    }

    @Override
    public int hashCode() {
        return (startState.toString()+ ";" + key + ";" + endState.toString()).hashCode();
    }
    
    @Override
    public String toString() {
        return "(" + startState.toString()+ ", " + key + ", " + endState.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertex vertex = (Vertex) obj;
        return startState.equals(vertex.startState) && endState.equals(vertex.endState) && key.equals(vertex.key);
    }
}
