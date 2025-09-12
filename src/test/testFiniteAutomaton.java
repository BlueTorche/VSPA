package test;

import automaton.*;

public class testFiniteAutomaton {
    public static void main(String[] args) {
        System.out.println("DFA :");
        testDFA();
        System.out.println("NFA :");
        testNFA();
    }

    private static void testDFA() {
        // États
        DFAState q0 = new DFAState("q0", false);
        DFAState q1 = new DFAState("q1", true);
        DFAState q2 = new DFAState("q2", false);
        DFAState q3 = new DFAState("q3", true);

        // Transitions 
        q0.addTransition("a", q2);
        q0.addTransition("b", q1);
        q1.addTransition("a", q2);
        q1.addTransition("b", q1);
        q2.addTransition("a", q2);
        q2.addTransition("b", q3);
        q3.addTransition("a", q2);
        q3.addTransition("b", q3);


        // Définition du NFA 
        FiniteAutomaton<DFAState> dfa = new FiniteAutomaton<DFAState>();
        dfa.setInitialState(q0);

        // Test - L(dfa) = Suff(w) = "b"
        String[] words = {"", "a", "ab", "aaab", "b", "bb", "baa", "baba", "aab", "aabb"};
        for (String w : words) {
            System.out.println("Input: " + w + " -> " + (dfa.accepts(w) ? "ACCEPTED" : "REJECTED"));
        }
    }

    private static void testNFA() {
        // États
        DFAState q0 = new DFAState("q0", false);
        DFAState q1 = new DFAState("q1", false);
        DFAState q2 = new DFAState("q2", true);
        DFAState q3 = new DFAState("q3", false);
        DFAState q4 = new DFAState("q4", true);

        // Transitions 
        q0.addTransition("a", q0);
        q0.addTransition("b", q0);
        q0.addTransition("a", q3);
        q0.addTransition("b", q1);
        q1.addTransition("b", q2);
        q3.addTransition("b", q4);
        q4.addTransition("a", q4);
        q4.addTransition("b", q4);

        // Définition du NFA 
        FiniteAutomaton<DFAState> nfa = new FiniteAutomaton<DFAState>();
        nfa.setInitialState(q0);

        // Test - L(nfa1) = Suff(w) = "bb" or w.contains("ab")
        String[] words = {"", "a", "ab", "aaab", "b", "bb", "baa", "baba", "aab", "aabb"};
        for (String w : words) {
            System.out.println("Input: " + w + " -> " + (nfa.accepts(w) ? "ACCEPTED" : "REJECTED"));
        }
    }
}
