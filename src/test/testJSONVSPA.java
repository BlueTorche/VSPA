package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import automaton.State;
import json_vspa.JSONProceduralAutomaton;
import json_vspa.JSONVSPA;
import json_vspa.KeyGraph;
import json_vspa.Vertex;
import vspa.VSPAAlphabet;

public class testJSONVSPA {
    public static void main(String[] args) {
        System.out.println("JSON VSPA :");
        testKeyGraph();
        testJSONVSPA1();
    }

    private static void testKeyGraph() {
        Set<String> keys = new HashSet<>();
        keys.add("a");
        keys.add("b");
        keys.add("c");
        keys.add("d");
        keys.add("e");

               // Procedural Automaton A^S2
        State q20 = new State("q20", true);
        State q21 = new State("q21", false);
        State q22 = new State("q22", false);
        State q23 = new State("q23", false);
        State q24 = new State("q24", false);
        State q25 = new State("q25", true);
        State q26 = new State("q26", false);
        State q27 = new State("q27", true);
        State q28 = new State("q28", false);
        State q29 = new State("q29", true);

        q20.addTransition("d", q21);
        q21.addTransition("n", q22);
        q22.addTransition("#", q23);
        q23.addTransition("e", q24);
        q24.addTransition("i", q25);

        q20.addTransition("d", q26);
        q26.addTransition("n", q27);

        q20.addTransition("e", q28);
        q28.addTransition("i", q29);

        JSONProceduralAutomaton S2 = new JSONProceduralAutomaton("S2");
        S2.setInitialState(q20);

        S2.createKeyGraph(keys);
        KeyGraph keyGraph0 = S2.getKeyGraph();

        for (Vertex v : keyGraph0.getVertices()) {
            if (keyGraph0.isInitialVertex(v)) {
                System.out.print("Initial ");
            }
            System.out.println("Vertex: " + v.toString());
            for (Vertex v2 : keyGraph0.getEdges(v)) {
                System.out.println("  Adjacent: " + v2.toString());
            }
        }

        System.out.println("-------------------------------------------------");

        // Procedural Automaton A^S0
        State q00 = new State("q00", false);
        State q01 = new State("q01", false);
        State q02 = new State("q02", false);
        State q03 = new State("q03", false);
        State q04 = new State("q04", false);
        State q05 = new State("q05", false);
        State q06 = new State("q06", false);
        State q07 = new State("q07", false);
        State q08 = new State("q08", true);
        
        q00.addTransition("a", q01);
        q01.addTransition("s", q02);
        q02.addTransition("#", q03);
        q03.addTransition("b", q04);
        q04.addTransition("S1", q05);
        q05.addTransition("#", q06);
        q06.addTransition("c", q07);
        q07.addTransition("S2", q08);

        JSONProceduralAutomaton S0 = new JSONProceduralAutomaton("S0");
        S0.setInitialState(q00);
    
        S0.createKeyGraph(keys);
        KeyGraph keyGraph = S0.getKeyGraph();

        for (Vertex v : keyGraph.getVertices()) {
            if (keyGraph.isInitialVertex(v)) {
                System.out.print("Initial ");
            }
            System.out.println("Vertex: " + v.toString());
            for (Vertex v2 : keyGraph.getEdges(v)) {
                System.out.println("  Adjacent: " + v2.toString());
            }
        }
    }

    private static void testJSONVSPA1() {
        // Alphabet
        VSPAAlphabet alphabet = new VSPAAlphabet();
        alphabet.addCallAndReturnSymbol("[", "]");
        alphabet.addCallAndReturnSymbol("{", "}");
        alphabet.addInternalSymbol("a"); // Keys of JSON document are replaced by "a", "b", "c", "d", "e"
        alphabet.addInternalSymbol("b");
        alphabet.addInternalSymbol("c");
        alphabet.addInternalSymbol("d");
        alphabet.addInternalSymbol("e");
        alphabet.addInternalSymbol("s");
        alphabet.addInternalSymbol("i");
        alphabet.addInternalSymbol("n");
        alphabet.addInternalSymbol("#");


        // Procedural Automaton A^S0
        State q00 = new State("q00", false);
        State q01 = new State("q01", false);
        State q02 = new State("q02", false);
        State q03 = new State("q03", false);
        State q04 = new State("q04", false);
        State q05 = new State("q05", false);
        State q06 = new State("q06", false);
        State q07 = new State("q07", false);
        State q08 = new State("q08", true);
        
        q00.addTransition("a", q01);
        q01.addTransition("s", q02);
        q02.addTransition("#", q03);
        q03.addTransition("b", q04);
        q04.addTransition("S1", q05);
        q05.addTransition("#", q06);
        q06.addTransition("c", q07);
        q07.addTransition("S2", q08);

        JSONProceduralAutomaton S0 = new JSONProceduralAutomaton("S0");
        S0.setInitialState(q00);


        // Procedural Automaton A^S1
        State q10 = new State("q10", true);
        State q11 = new State("q11", true);
        State q12 = new State("q12", false);

        q10.addTransition("s", q11);
        q11.addTransition("#", q12);
        q12.addTransition("s", q11);

        JSONProceduralAutomaton S1 = new JSONProceduralAutomaton("S1");
        S1.setInitialState(q10);


        // Procedural Automaton A^S2
        State q20 = new State("q20", true);
        State q21 = new State("q21", false);
        State q22 = new State("q22", false);
        State q23 = new State("q23", false);
        State q24 = new State("q24", false);
        State q25 = new State("q25", true);
        State q26 = new State("q26", false);
        State q27 = new State("q27", true);
        State q28 = new State("q28", false);
        State q29 = new State("q29", true);

        q20.addTransition("d", q21);
        q21.addTransition("n", q22);
        q22.addTransition("#", q23);
        q23.addTransition("e", q24);
        q24.addTransition("i", q25);

        q20.addTransition("d", q26);
        q26.addTransition("n", q27);

        q20.addTransition("e", q28);
        q28.addTransition("i", q29);

        JSONProceduralAutomaton S2 = new JSONProceduralAutomaton("S2");
        S2.setInitialState(q20);


        // Definition VSPA
        JSONVSPA vspa = new JSONVSPA();
        vspa.setVSPAAlphabet(alphabet);
        vspa.addProceduralAutomaton(S0, "{");
        vspa.addProceduralAutomaton(S1, "[");
        vspa.addProceduralAutomaton(S2, "{");
        vspa.setStartingAutomaton(S0);

        Set<String> keySymbols = new HashSet<>();
        keySymbols.add("a");
        keySymbols.add("b");
        keySymbols.add("c");
        keySymbols.add("d");
        keySymbols.add("e");
        vspa.createKeyGraphs(keySymbols);



        // Test - L(VPA) == grammar : S -> a(S+R)*z  ; R -> abz
        String[] acceptedWords = {
                        "{as#b[s#s#s]#c{dn#ei}}", 
                        "{b[s#s#s]#c{dn#ei}#as}", 
                        "{as#b[s#s#s]#c{}}", 
                        "{as#b[s#s#s#s#s#s#s]#c{}}",
                        "{as#b[]#c{}}", 
                        "{c{}#as#b[]}", 
                        "{as#c{}#b[]}", 
                        "{b[]#as#c{}}", 
                        "{c{}#b[]#as}", 
                        "{as#b[s#s#s#s]#c{dn}}", 
                        "{as#b[s#s#s#s]#c{ei}}", 
                        "{as#b[s]#c{}}"
                        };

        List<List<String>> wordsList = new ArrayList<>();
        for (String w : acceptedWords) {
            List<String> wordList = new ArrayList<>();
            for (String symbol : w.split("")) {
                wordList.add(symbol);
            }
            wordsList.add(wordList);
        }
        
        for (List<String> w : wordsList) {
            System.out.println("-------------------------Accepted----------------------------------------");
            System.out.println("Input: " + String.join("", w) + " -> " + (vspa.accepts(w, false, false).first ? "ACCEPTED" : "REJECTED"));
        }

        
        String[] rejectedWords = {
                        "{}", 
                        "{b[s#s#s]#c{dn#ei}}",
                        "{as#b[s#s#]#c{}}", 
                        "{as#b[}#c{}}", 
                        "[s#s]", 
                        };

                        
        wordsList = new ArrayList<>();
        for (String w : rejectedWords) {
            List<String> wordList = new ArrayList<>();
            for (String symbol : w.split("")) {
                wordList.add(symbol);
            }
            wordsList.add(wordList);
        }
        
        for (List<String> w : wordsList) {
            System.out.println("-------------------------Rejected----------------------------------------");
            System.out.println("Input: " + String.join("", w) + " -> " + (vspa.accepts(w, false, false).first ? "ACCEPTED" : "REJECTED"));
        }
    }
}
