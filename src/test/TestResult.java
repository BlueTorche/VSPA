package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import utils.SanitizeJSON;


public class TestResult {
    static String[] entetes = {"Documents ID", "Automaton Time (µs)", "Automaton Memory", "Result"};


    public static void main(String[] args) {
        System.out.println("Basic Types :");
        // basicTypes();
        recuriveList();
    }

    public static void basicTypes() {
        // Alphabet & keyset
        Set<String> keySymbols = new HashSet<>();
        keySymbols.add("\"string\"");
        keySymbols.add("\"double\"");
        keySymbols.add("\"integer\"");
        keySymbols.add("\"boolean\"");
        keySymbols.add("\"object\"");
        keySymbols.add("\"array\"");
        keySymbols.add("\"anything\"");

        VSPAAlphabet alphabet = new VSPAAlphabet();
        alphabet.addCallAndReturnSymbol("[", "]");
        alphabet.addCallAndReturnSymbol("{", "}");
        alphabet.addInternalSymbol("false"); 
        alphabet.addInternalSymbol("true");
        alphabet.addInternalSymbol("\"\\\\S\"");
        alphabet.addInternalSymbol("\"\\\\I\"");
        alphabet.addInternalSymbol("\"\\\\D\"");

        for (String k : keySymbols)
            alphabet.addInternalSymbol(k);

        // Procedural Automaton A^S0
        State q0S0 = new State("q0S0", false);
        State q1S0 = new State("q1S0", false);
        State q2S0 = new State("q2S0", false);
        State q3S0 = new State("q3S0", false);
        State q4S0 = new State("q4S0", false);
        State q5S0 = new State("q5S0", false);
        State q6S0 = new State("q6S0", false);
        State q7S0 = new State("q7S0", false);
        State q8S0 = new State("q8S0", false);
        State q9S0 = new State("q9S0", false);
        State q10S0 = new State("q10S0", false);
        State q11S0 = new State("q11S0", false);
        State q12S0 = new State("q12S0", false);
        State q13S0 = new State("q13S0", false);
        State q14S0 = new State("q14S0", false);
        State q15S0 = new State("q15S0", false);
        State q16S0 = new State("q16S0", false);
        State q17S0 = new State("q17S0", true);
        
        q0S0.addTransition("\"string\"", q1S0);
        q1S0.addTransition("\"\\\\S\"", q2S0);
        q2S0.addTransition("#", q3S0);
        q3S0.addTransition("\"double\"", q4S0);
        q4S0.addTransition("\"\\\\D\"", q5S0);
        q5S0.addTransition("#", q6S0);
        q6S0.addTransition("\"integer\"", q7S0);
        q7S0.addTransition("\"\\\\I\"", q8S0);
        q8S0.addTransition("#", q9S0);
        q9S0.addTransition("\"boolean\"", q10S0);
        q10S0.addTransition("true", q11S0);
        q10S0.addTransition("false", q11S0);
        q11S0.addTransition("#", q12S0);
        q12S0.addTransition("\"object\"", q13S0);
        q13S0.addTransition("S1", q14S0);
        q14S0.addTransition("#", q15S0);
        q15S0.addTransition("\"array\"", q16S0);
        q16S0.addTransition("S2", q17S0);

        JSONProceduralAutomaton S0 = new JSONProceduralAutomaton("S0");
        S0.setInitialState(q0S0);


        // Procedural Automaton A^S1
        State q0S1 = new State("q0S1", false);
        State q1S1 = new State("q1S1", false);
        State q2S1 = new State("q2S1", true);

        q0S1.addTransition("\"anything\"", q1S1);
        q1S1.addTransition("\"\\\\S\"", q2S1);
        q1S1.addTransition("\"\\\\I\"", q2S1);
        q1S1.addTransition("\"\\\\D\"", q2S1);
        q1S1.addTransition("true", q2S1);
        q1S1.addTransition("false", q2S1);

        JSONProceduralAutomaton S1 = new JSONProceduralAutomaton("S1");
        S1.setInitialState(q0S1);


        // Procedural Automaton A^S2
        State q0S2 = new State("q0S2", false);
        State q1S2 = new State("q1S2", false);
        State q2S2 = new State("q2S2", false);
        State q3S2 = new State("q3S2", true);
        State q4S2 = new State("q4S2", false);

        q0S2.addTransition("\"\\\\S\"", q1S2);
        q1S2.addTransition("#", q2S2);
        q2S2.addTransition("\"\\\\S\"", q3S2);
        q3S2.addTransition("#", q4S2);
        q4S2.addTransition("\"\\\\S\"", q3S2);

        JSONProceduralAutomaton S2 = new JSONProceduralAutomaton("S2");
        S2.setInitialState(q0S2);


        // Definition VSPA
        JSONVSPA vspa = new JSONVSPA();
        vspa.setVSPAAlphabet(alphabet);
        vspa.addProceduralAutomaton(S0, "{");
        vspa.addProceduralAutomaton(S1, "{");
        vspa.addProceduralAutomaton(S2, "[");
        vspa.setStartingAutomaton(S0);

        vspa.createKeyGraphs(keySymbols);

        // KeyGraph keyGraph = S0.getKeyGraph(); 
        // for (Vertex v : keyGraph.getVertices()) {
        //     if (keyGraph.isInitialVertex(v)) {
        //         System.out.print("Initial ");
        //     }
        //     System.out.println("Vertex: " + v.toString());
        //     for (Vertex v2 : keyGraph.getEdges(v)) {
        //         System.out.println("  Adjacent: " + v2.toString());
        //     }
        // }

        
        runResult("BasicTypes", vspa);
    }

    

    public static void recuriveList() {
        // Alphabet & keyset
        Set<String> keySymbols = new HashSet<>();
        keySymbols.add("\"name\"");
        keySymbols.add("\"children\"");

        VSPAAlphabet alphabet = new VSPAAlphabet();
        alphabet.addCallAndReturnSymbol("[", "]");
        alphabet.addCallAndReturnSymbol("{", "}");
        alphabet.addInternalSymbol("false"); 
        alphabet.addInternalSymbol("true");
        alphabet.addInternalSymbol("\"\\\\S\"");
        alphabet.addInternalSymbol("\"\\\\I\"");
        alphabet.addInternalSymbol("\"\\\\D\"");

        for (String k : keySymbols)
            alphabet.addInternalSymbol(k);

        // Procedural Automaton A^S0
        State q0S0 = new State("q0S0", false);
        State q1S0 = new State("q1S0", false);
        State q2S0 = new State("q2S0", false);
        State q3S0 = new State("q3S0", false);
        State q4S0 = new State("q4S0", false);
        State q5S0 = new State("q5S0", true);
        
        q0S0.addTransition("\"name\"", q1S0);
        q1S0.addTransition("\"\\\\S\"", q2S0);
        q2S0.addTransition("#", q3S0);
        q3S0.addTransition("\"children\"", q4S0);
        q4S0.addTransition("S1", q5S0);

        JSONProceduralAutomaton S0 = new JSONProceduralAutomaton("S0");
        S0.setInitialState(q0S0);


        // Procedural Automaton A^S1
        State q0S1 = new State("q0S1", true);
        State q1S1 = new State("q1S1", true);

        q0S1.addTransition("S0", q1S1);

        JSONProceduralAutomaton S1 = new JSONProceduralAutomaton("S1");
        S1.setInitialState(q0S1);

        // Definition VSPA
        JSONVSPA vspa = new JSONVSPA();
        vspa.setVSPAAlphabet(alphabet);
        vspa.addProceduralAutomaton(S0, "{");
        vspa.addProceduralAutomaton(S1, "[");
        vspa.setStartingAutomaton(S0);

        vspa.createKeyGraphs(keySymbols);

        // KeyGraph keyGraph = S0.getKeyGraph(); 
        // for (Vertex v : keyGraph.getVertices()) {
        //     if (keyGraph.isInitialVertex(v)) {
        //         System.out.print("Initial ");
        //     }
        //     System.out.println("Vertex: " + v.toString());
        //     for (Vertex v2 : keyGraph.getEdges(v)) {
        //         System.out.println("  Adjacent: " + v2.toString());
        //     }
        // }

        runResult("recursiveList", vspa);
    }
    

    public static void vim() {
        // Alphabet & keyset
        Set<String> keySymbols = new HashSet<>();
        keySymbols.add("\"name\"");
        keySymbols.add("\"children\"");

        VSPAAlphabet alphabet = new VSPAAlphabet();
        alphabet.addCallAndReturnSymbol("[", "]");
        alphabet.addCallAndReturnSymbol("{", "}");
        alphabet.addInternalSymbol("false"); 
        alphabet.addInternalSymbol("true");
        alphabet.addInternalSymbol("\"\\\\S\"");
        alphabet.addInternalSymbol("\"\\\\I\"");
        alphabet.addInternalSymbol("\"\\\\D\"");

        for (String k : keySymbols)
            alphabet.addInternalSymbol(k);

        // Procedural Automaton A^S0
        State q0S0 = new State("q0S0", false);
        State q1S0 = new State("q1S0", false);
        State q2S0 = new State("q2S0", false);
        State q3S0 = new State("q3S0", false);
        State q4S0 = new State("q4S0", false);
        State q5S0 = new State("q5S0", true);
        
        q0S0.addTransition("\"name\"", q1S0);
        q1S0.addTransition("\"\\\\S\"", q2S0);
        q2S0.addTransition("#", q3S0);
        q3S0.addTransition("\"children\"", q4S0);
        q4S0.addTransition("S1", q5S0);

        JSONProceduralAutomaton S0 = new JSONProceduralAutomaton("S0");
        S0.setInitialState(q0S0);


        // Procedural Automaton A^S1
        State q0S1 = new State("q0S1", true);
        State q1S1 = new State("q1S1", true);

        q0S1.addTransition("S0", q1S1);

        JSONProceduralAutomaton S1 = new JSONProceduralAutomaton("S1");
        S1.setInitialState(q0S1);

        // Definition VSPA
        JSONVSPA vspa = new JSONVSPA();
        vspa.setVSPAAlphabet(alphabet);
        vspa.addProceduralAutomaton(S0, "{");
        vspa.addProceduralAutomaton(S1, "[");
        vspa.setStartingAutomaton(S0);

        vspa.createKeyGraphs(keySymbols);

        // KeyGraph keyGraph = S0.getKeyGraph(); 
        // for (Vertex v : keyGraph.getVertices()) {
        //     if (keyGraph.isInitialVertex(v)) {
        //         System.out.print("Initial ");
        //     }
        //     System.out.println("Vertex: " + v.toString());
        //     for (Vertex v2 : keyGraph.getEdges(v)) {
        //         System.out.println("  Adjacent: " + v2.toString());
        //     }
        // }

        runResult("recursiveList", vspa);
    }


    public static void runResult(String jsontype, JSONVSPA automaton) {
        String directoryPath = "D:\\TFE2-code-gaetan\\ValidatingJSONDocumentsWithLearnedVPA\\schemas\\benchmarks\\"+ jsontype + "\\Documents\\" + jsontype + ".json\\Random";
        File directory = new File(directoryPath);
        System.out.println(directoryPath);
        List<List<String>> datas = new ArrayList<>();

        int i = 0;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        List<String> json = SanitizeJSON.sanitizeJSON(file.getAbsolutePath());
                        
                        long memory = getMemoryUse();
                        long time = System.nanoTime();


                        boolean result = automaton.accepts(json);

                        time = (System.nanoTime() - time);
                        memory = getMemoryUse() - memory;

                        System.out.println("----------------------------------------------------------------------");
                        System.out.println(file.getAbsolutePath());
                        System.out.println(json);
                        System.out.println("      -> " + (result ? "ACCEPTED" : "REJECTED"));
                        
                        List<String> data = new ArrayList<>();
                        data.add(file.getName());
                        data.add(String.valueOf((int) time/1_000));
                        data.add(String.valueOf(memory));
                        data.add(String.valueOf(result));
                        datas.add(data);

                        System.out.println(time);

                        // i++;
                        // if (i == 100)
                        //     break;
                        
                    } catch (IOException e) {
                        System.out.println("Error while opening file " + file.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
            }
        }

        System.out.println(datas.toString());

         try (FileWriter writer = new FileWriter("C:/Users/dubru/Documents/GitHub/VSPA/src/Result/result" + jsontype + ".csv")) {
            // Écrire les entêtes
            writer.write(String.join(";", entetes) + "\n");

            // Écrire les données
            for (List<String> ligne : datas) {
                writer.write(String.join(";", ligne) + "\n");
            }

            System.out.println("Fichier CSV créé avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long getMemoryUse() {
        final Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}
