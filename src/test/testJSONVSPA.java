package test;

import java.util.*;

import json_vspa.JSONProceduralAutomaton;
import json_vspa.JSONVSPA;
import json_vspa.KeyGraph;
import json_vspa.Vertex;
import vspa.VRA_State;
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
        VRA_State q20 = new VRA_State("q20", true);
        VRA_State q21 = new VRA_State("q21", false);
        VRA_State q22 = new VRA_State("q22", false);
        VRA_State q23 = new VRA_State("q23", false);
        VRA_State q24 = new VRA_State("q24", false);
        VRA_State q25 = new VRA_State("q25", true);
        VRA_State q26 = new VRA_State("q26", false);
        VRA_State q27 = new VRA_State("q27", true);
        VRA_State q28 = new VRA_State("q28", false);
        VRA_State q29 = new VRA_State("q29", true);

        q20.addTransition("d", q21);
        q21.addTransition("n", q22);
        q22.addTransition("#", q23);
        q23.addTransition("e", q24);
        q24.addTransition("i", q25);

        q20.addTransition("d", q26);
        q26.addTransition("n", q27);

        q20.addTransition("e", q28);
        q28.addTransition("i", q29);

        JSONProceduralAutomaton S2 = new JSONProceduralAutomaton("S2", q20);

        S2.createKeyGraph(keys);
        KeyGraph<VRA_State> keyGraph0 = S2.getKeyGraph();

        for (Vertex<VRA_State> v : keyGraph0.getVertices()) {
            if (keyGraph0.isInitialVertex(v)) {
                System.out.print("Initial ");
            }
            System.out.println("Vertex: " + v.toString());
            for (Vertex<VRA_State> v2 : keyGraph0.getEdges(v)) {
                System.out.println("  Adjacent: " + v2.toString());
            }
        }

        System.out.println("-------------------------------------------------");

        // Procedural Automaton A^S0
        VRA_State q00 = new VRA_State("q00", false);
        VRA_State q01 = new VRA_State("q01", false);
        VRA_State q02 = new VRA_State("q02", false);
        VRA_State q03 = new VRA_State("q03", false);
        VRA_State q04 = new VRA_State("q04", false);
        VRA_State q05 = new VRA_State("q05", false);
        VRA_State q06 = new VRA_State("q06", false);
        VRA_State q07 = new VRA_State("q07", false);
        VRA_State q08 = new VRA_State("q08", true);
        
        q00.addTransition("a", q01);
        q01.addTransition("s", q02);
        q02.addTransition("#", q03);
        q03.addTransition("b", q04);
        q04.addTransition("S1", q05);
        q05.addTransition("#", q06);
        q06.addTransition("c", q07);
        q07.addTransition("S2", q08);

        JSONProceduralAutomaton S0 = new JSONProceduralAutomaton("S0", q00);
    
        S0.createKeyGraph(keys);
        KeyGraph<VRA_State> keyGraph = S0.getKeyGraph();

        for (Vertex<VRA_State> v : keyGraph.getVertices()) {
            if (keyGraph.isInitialVertex(v)) {
                System.out.print("Initial ");
            }
            System.out.println("Vertex: " + v.toString());
            for (Vertex<VRA_State> v2 : keyGraph.getEdges(v)) {
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
        VRA_State q00 = new VRA_State("q00", false);
        VRA_State q01 = new VRA_State("q01", false);
        VRA_State q02 = new VRA_State("q02", false);
        VRA_State q03 = new VRA_State("q03", false);
        VRA_State q04 = new VRA_State("q04", false);
        VRA_State q05 = new VRA_State("q05", false);
        VRA_State q06 = new VRA_State("q06", false);
        VRA_State q07 = new VRA_State("q07", false);
        VRA_State q08 = new VRA_State("q08", true);
        
        q00.addTransition("a", q01);
        q01.addTransition("s", q02);
        q02.addTransition("#", q03);
        q03.addTransition("b", q04);
        q04.addTransition("S1", q05);
        q05.addTransition("#", q06);
        q06.addTransition("c", q07);
        q07.addTransition("S2", q08);

        JSONProceduralAutomaton S0 = new JSONProceduralAutomaton("S0", q00);


        // Procedural Automaton A^S1
        VRA_State q10 = new VRA_State("q10", true);
        VRA_State q11 = new VRA_State("q11", true);
        VRA_State q12 = new VRA_State("q12", false);

        q10.addTransition("s", q11);
        q11.addTransition("#", q12);
        q12.addTransition("s", q11);

        JSONProceduralAutomaton S1 = new JSONProceduralAutomaton("S1", q10);


        // Procedural Automaton A^S2
        VRA_State q20 = new VRA_State("q20", true);
        VRA_State q21 = new VRA_State("q21", false);
        VRA_State q22 = new VRA_State("q22", false);
        VRA_State q23 = new VRA_State("q23", false);
        VRA_State q24 = new VRA_State("q24", false);
        VRA_State q25 = new VRA_State("q25", true);
        VRA_State q26 = new VRA_State("q26", false);
        VRA_State q27 = new VRA_State("q27", true);
        VRA_State q28 = new VRA_State("q28", false);
        VRA_State q29 = new VRA_State("q29", true);

        q20.addTransition("d", q21);
        q21.addTransition("n", q22);
        q22.addTransition("#", q23);
        q23.addTransition("e", q24);
        q24.addTransition("i", q25);

        q20.addTransition("d", q26);
        q26.addTransition("n", q27);

        q20.addTransition("e", q28);
        q28.addTransition("i", q29);

        JSONProceduralAutomaton S2 = new JSONProceduralAutomaton("S2", q20);


        // Definition VSPA
        JSONVSPA vspa = new JSONVSPA(alphabet);
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
            Collections.addAll(wordList, w.split(""));
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
            Collections.addAll(wordList, w.split(""));
            wordsList.add(wordList);
        }
        
        for (List<String> w : wordsList) {
            System.out.println("-------------------------Rejected----------------------------------------");
            System.out.println("Input: " + String.join("", w) + " -> " + (vspa.accepts(w, false, false).first ? "ACCEPTED" : "REJECTED"));
        }
    }
}
