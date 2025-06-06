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
import vspa.VSPAAlphabet;
import utils.SanitizeJSON;


public class TestResult {
    static String[] entetes = {"Documents ID", "Automaton Time (µs)", "Automaton Memory", "Result"};
    static boolean DEBUG = false;

    public static void main(String[] args) {
        // basicTypes();
        // recuriveList();
        // vim();
        proxies();
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
        alphabet.addInternalSymbol("#");

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

        

        System.out.println("BASIC Key Graph");
        keyGraphMeasures(vspa, keySymbols);

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
        alphabet.addInternalSymbol("null");
        alphabet.addInternalSymbol("\"\\\\S\"");
        alphabet.addInternalSymbol("\"\\\\I\"");
        alphabet.addInternalSymbol("\"\\\\D\"");
        alphabet.addInternalSymbol("\"\\\\E\"");
        alphabet.addInternalSymbol("#");

        for (String k : keySymbols)
            alphabet.addInternalSymbol(k);

        // Procedural Automaton A^S0
        State q0S0 = new State("q0S0", false);
        State q1S0 = new State("q1S0", false);
        State q2S0 = new State("q2S0", true);
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

        System.out.println("RECURSIVE Key Graph");
        keyGraphMeasures(vspa, keySymbols);

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
        keySymbols.add("\"version\"");
        keySymbols.add("\"description\"");
        keySymbols.add("\"homepage\"");
        keySymbols.add("\"author\"");
        keySymbols.add("\"maintainer\"");
        keySymbols.add("\"repository\"");
        keySymbols.add("\"dependencies\"");
        keySymbols.add("\"type\"");
        keySymbols.add("\"url\"");
        keySymbols.add("\"deprecated\"");
        keySymbols.add("\"vim_script_nr\"");
        keySymbols.add("\"script-type\"");
        keySymbols.add("\"\\\\S\"");

        VSPAAlphabet alphabet = new VSPAAlphabet();
        alphabet.addCallAndReturnSymbol("[", "]");
        alphabet.addCallAndReturnSymbol("{", "}");
        alphabet.addInternalSymbol("null"); 
        alphabet.addInternalSymbol("false"); 
        alphabet.addInternalSymbol("true");
        alphabet.addInternalSymbol("\"\\\\I\"");
        alphabet.addInternalSymbol("\"\\\\D\"");
        alphabet.addInternalSymbol("\"\\\\S\"");
        alphabet.addInternalSymbol("\"\\\\E\"");
        alphabet.addInternalSymbol("#");

        for (String k : keySymbols)
            alphabet.addInternalSymbol(k);

        // Procedural Automaton A^S0
        State q0S0 = new State("q0S0", false);
        State q1S0 = new State("q1S0", false);
        q0S0.addTransition("\"name\"", q1S0);
        State q2S0 = new State("q2S0", false);
        q1S0.addTransition("\"\\\\S\"", q2S0);
        State q3S0 = new State("q3S0", false);
        q2S0.addTransition("#", q3S0);
        State q4S0 = new State("q4S0", false);
        q3S0.addTransition("\"version\"", q4S0);
        State q5S0 = new State("q5S0", false);
        q4S0.addTransition("\"\\\\S\"", q5S0);
        State q6S0 = new State("q6S0", false);
        q5S0.addTransition("#", q6S0);
        State q7S0 = new State("q7S0", false);
        q6S0.addTransition("\"description\"", q7S0);
        State q8S0 = new State("q8S0", false);
        q7S0.addTransition("\"\\\\S\"", q8S0);
        State q9S0 = new State("q9S0", false);
        q8S0.addTransition("#", q9S0);
        State q10S0 = new State("q10S0", false);
        q9S0.addTransition("\"homepage\"", q10S0);
        State q11S0 = new State("q11S0", false);
        q10S0.addTransition("\"\\\\S\"", q11S0);
        State q12S0 = new State("q12S0", false);
        q11S0.addTransition("#", q12S0);
        State q13S0 = new State("q13S0", false);
        q12S0.addTransition("\"author\"", q13S0);
        State q14S0 = new State("q14S0", false);
        q13S0.addTransition("\"\\\\S\"", q14S0);
        State q15S0 = new State("q15S0", false);
        q14S0.addTransition("#", q15S0);
        State q16S0 = new State("q16S0", false);
        q15S0.addTransition("\"maintainer\"", q16S0);
        State q17S0 = new State("q17S0", false);
        q16S0.addTransition("\"\\\\S\"", q17S0);
        State q18S0 = new State("q18S0", false);
        q17S0.addTransition("#", q18S0);
        State q19S0 = new State("q19S0", false);
        q18S0.addTransition("\"repository\"", q19S0);
        State q20S0 = new State("q20S0", false);
        q19S0.addTransition("S1", q20S0);
        State q21S0 = new State("q21S0", false);
        q20S0.addTransition("#", q21S0);
        State q22S0 = new State("q22S0", false);
        q21S0.addTransition("\"dependencies\"", q22S0);
        State q23S0 = new State("q23S0", true);
        q22S0.addTransition("S2", q23S0);
        State q24S0 = new State("q24S0", false);
        q23S0.addTransition("#", q24S0);
        State q25S0 = new State("q25S0", false);
        q24S0.addTransition("\"\\\\S\"", q25S0);
        State q26S0 = new State("q26S0", true);
        q25S0.addTransition("Uo", q26S0);
        q25S0.addTransition("Ua", q26S0);
        q25S0.addTransition("null", q26S0);
        q25S0.addTransition("true", q26S0);
        q25S0.addTransition("false", q26S0);
        q25S0.addTransition("\"\\\\I\"", q26S0);
        q25S0.addTransition("\"\\\\D\"", q26S0);
        q25S0.addTransition("\"\\\\S\"", q26S0);
        q25S0.addTransition("\"\\\\E\"", q26S0);

        JSONProceduralAutomaton S0 = new JSONProceduralAutomaton("S0");
        S0.setInitialState(q0S0);

        // Procedural Automaton A^S1  --> SelfRepository
        State q0S1 = new State("q0S1", false);
        State q1S1 = new State("q1S1", false);
        q0S1.addTransition("\"type\"", q1S1);
        State q2S1 = new State("q2S1", false);
        q1S1.addTransition("\"\\\\E\"", q2S1);
        State q3S1 = new State("q3S1", false);
        q2S1.addTransition("#", q3S1);
        State q4S1 = new State("q4S1", false);
        q3S1.addTransition("\"url\"", q4S1);
        State q5S1 = new State("q5S1", false);
        q4S1.addTransition("\"\\\\S\"", q5S1);
        State q6S1 = new State("q6S1", false);
        q5S1.addTransition("#", q6S1);
        State q7S1 = new State("q7S1", false);
        q6S1.addTransition("\"deprecated\"", q7S1);
        State q8S1 = new State("q8S1", true);
        q7S1.addTransition("\"\\\\S\"", q8S1);
        State q9S1 = new State("q9S1", false);
        q8S1.addTransition("#", q9S1);
        State q10S1 = new State("q10S1", false);
        q9S1.addTransition("\"\\\\S\"", q10S1);
        State q11S1 = new State("q11S1", true);
        q10S1.addTransition("Uo", q11S1);
        q10S1.addTransition("Ua", q11S1);
        q10S1.addTransition("null", q11S1);
        q10S1.addTransition("true", q11S1);
        q10S1.addTransition("false", q11S1);
        q10S1.addTransition("\"\\\\I\"", q11S1);
        q10S1.addTransition("\"\\\\D\"", q11S1);
        q10S1.addTransition("\"\\\\S\"", q11S1);
        q10S1.addTransition("\"\\\\E\"", q11S1);
        
        JSONProceduralAutomaton S1 = new JSONProceduralAutomaton("S1");
        S1.setInitialState(q0S1);
        

        // Procedural Automaton A^S2 --> dependencies
        State q0S2 = new State("q0S2", true);
        State q1S2 = new State("q1S2", false);
        q0S2.addTransition("\"\\\\S\"", q1S2);
        State q2S2 = new State("q2S2", true);
        q1S2.addTransition("S3", q2S2);
        
        JSONProceduralAutomaton S2 = new JSONProceduralAutomaton("S2");
        S2.setInitialState(q0S2);


        // Procedural Automaton A^S3  --> OtherRepository
        State q0S3 = new State("q0S3", false);
        State q1S3 = new State("q1S3", false);
        q0S3.addTransition("\"homepage\"", q1S3);
        State q2S3 = new State("q2S3", false);
        q1S3.addTransition("\"\\\\S\"", q2S3);
        State q3S3 = new State("q3S3", false);
        q2S3.addTransition("#", q3S3);
        State q4S3 = new State("q4S3", false);
        q3S3.addTransition("\"type\"", q4S3);
        State q5S3 = new State("q5S3", false);
        q4S3.addTransition("\"\\\\E\"", q5S3);
        State q6S3 = new State("q6S3", false);
        q5S3.addTransition("#", q6S3);
        State q7S3 = new State("q7S3", false);
        q6S3.addTransition("\"url\"", q7S3);
        State q8S3 = new State("q8S3", true);
        q7S3.addTransition("\"\\\\S\"", q8S3);
        State q9S3 = new State("q9S3", false);
        q8S3.addTransition("#", q9S3);
        State q10S3 = new State("q10S3", false);
        q9S3.addTransition("\"vim_script_nr\"", q10S3);
        State q11S3 = new State("q11S3", true);
        q10S3.addTransition("\"\\\\D\"", q11S3);
        State q12S3 = new State("q12S3", false);
        q11S3.addTransition("#", q12S3);
        q8S3.addTransition("#", q12S3);
        State q13S3 = new State("q13S3", false);
        q12S3.addTransition("\"script-type\"", q13S3);
        State q14S3 = new State("q14S3", true);
        q13S3.addTransition("\"\\\\E\"", q14S3);
        State q15S2 = new State("q15S2", false);
        q14S3.addTransition("#", q15S2);
        q11S3.addTransition("#", q15S2);
        q8S3.addTransition("#", q15S2);
        State q16S2 = new State("q16S2", false);
        q15S2.addTransition("\"\\\\S\"", q16S2);
        State q17S2 = new State("q17S2", true);
        q16S2.addTransition("Uo", q17S2);
        q16S2.addTransition("Ua", q17S2);
        q16S2.addTransition("null", q17S2);
        q16S2.addTransition("true", q17S2);
        q16S2.addTransition("false", q17S2);
        q16S2.addTransition("\"\\\\I\"", q17S2);
        q16S2.addTransition("\"\\\\D\"", q17S2);
        q16S2.addTransition("\"\\\\S\"", q17S2);
        q16S2.addTransition("\"\\\\E\"", q17S2);

        JSONProceduralAutomaton S3 = new JSONProceduralAutomaton("S3");
        S3.setInitialState(q0S3);

        

        // Procedural Automaton A^Uo  --> Universal Object
        State q0Uo = new State("q0Uo", true);
        State q1Uo = new State("q1Uo", false);
        q0Uo.addTransition("\"\\\\S\"", q1Uo);
        State q2Uo = new State("q2Uo", true);
        q1Uo.addTransition("\"\\\\S\"", q2Uo);
        q1Uo.addTransition("Uo", q2Uo);
        q1Uo.addTransition("Ua", q2Uo);
        q1Uo.addTransition("null", q2Uo);
        q1Uo.addTransition("true", q2Uo);
        q1Uo.addTransition("false", q2Uo);
        q1Uo.addTransition("\"\\\\I\"", q2Uo);
        q1Uo.addTransition("\"\\\\D\"", q2Uo);
        q1Uo.addTransition("\"\\\\S\"", q2Uo);
        q1Uo.addTransition("\"\\\\E\"", q2Uo);

        JSONProceduralAutomaton Uo = new JSONProceduralAutomaton("Uo");
        Uo.setInitialState(q0Uo);

        
        // Procedural Automaton A^Uo  --> Universal Object
        State q0Ua = new State("q0Ua", true);
        State q1Ua = new State("q1Ua", true);
        q0Ua.addTransition("\"\\\\S\"", q1Ua);
        q0Ua.addTransition("Uo", q1Ua);
        q0Ua.addTransition("Ua", q1Ua);
        q0Ua.addTransition("null", q1Ua);
        q0Ua.addTransition("true", q1Ua);
        q0Ua.addTransition("false", q1Ua);
        q0Ua.addTransition("\"\\\\I\"", q1Ua);
        q0Ua.addTransition("\"\\\\D\"", q1Ua);
        q0Ua.addTransition("\"\\\\S\"", q1Ua);
        q0Ua.addTransition("\"\\\\E\"", q1Ua);
        State q2Ua = new State("q2Ua", false);
        q1Ua.addTransition("#", q2Ua);

        q2Ua.addTransition("\"\\\\S\"", q1Ua);
        q2Ua.addTransition("Uo", q1Ua);
        q2Ua.addTransition("Ua", q1Ua);
        q2Ua.addTransition("null", q1Ua);
        q2Ua.addTransition("true", q1Ua);
        q2Ua.addTransition("false", q1Ua);
        q2Ua.addTransition("\"\\\\I\"", q1Ua);
        q2Ua.addTransition("\"\\\\D\"", q1Ua);
        q2Ua.addTransition("\"\\\\S\"", q1Ua);
        q2Ua.addTransition("\"\\\\E\"", q1Ua);

        JSONProceduralAutomaton Ua = new JSONProceduralAutomaton("Ua");
        Ua.setInitialState(q0Ua);


        // Definition VSPA
        JSONVSPA vspa = new JSONVSPA();
        vspa.setVSPAAlphabet(alphabet);
        vspa.addProceduralAutomaton(S0, "{");
        vspa.addProceduralAutomaton(S1, "{");
        vspa.addProceduralAutomaton(S2, "{");
        vspa.addProceduralAutomaton(S3, "{");
        vspa.addProceduralAutomaton(Uo, "{");
        vspa.addProceduralAutomaton(Ua, "[");
        vspa.setStartingAutomaton(S0);

        System.out.println("VIM Key Graph");
        keyGraphMeasures(vspa, keySymbols);

        // KeyGraph keyGraph = S3.getKeyGraph(); 
        // for (Vertex v : keyGraph.getVertices()) {
        //     if (keyGraph.isInitialVertex(v)) {
        //         System.out.print("Initial ");
        //     }
        //     System.out.println("Vertex: " + v.toString());
        //     for (Vertex v2 : keyGraph.getEdges(v)) {
        //         System.out.println("  Adjacent: " + v2.toString());
        //     }
        // }

        runResult("vim", vspa);
    }


    public static void proxies() {
        // Alphabet & keyset
        Set<String> keySymbols = new HashSet<>();
        keySymbols.add("\"$schema\"");
        keySymbols.add("\"proxies\"");
        keySymbols.add("\"<ProxyName>\"");
        keySymbols.add("\"matchCondition\"");
        keySymbols.add("\"route\"");
        keySymbols.add("\"methods\"");
        keySymbols.add("\"debug\"");
        keySymbols.add("\"responseOverrides\"");
        keySymbols.add("\"response.headers.<HeaderName>\"");
        keySymbols.add("\"response.statusCode\"");
        keySymbols.add("\"response.body\"");
        keySymbols.add("\"response.statusReason\"");
        keySymbols.add("\"^responseheaders.+$\"");
        keySymbols.add("\"backendUri\"");
        keySymbols.add("\"disabled\"");
        keySymbols.add("\"requestOverrides\"");
        keySymbols.add("\"backend.request.method\"");
        keySymbols.add("\"backend.request.querystring.<ParameterName>\"");
        keySymbols.add("\"backend.request.headers.<HeaderName>\"");
        keySymbols.add("\"^backendrequestheaders.+$\"");
        keySymbols.add("\"^backendrequestquerystring.+$\"");
        keySymbols.add("\"desc\"");
        keySymbols.add("\"\\\\S\"");

        VSPAAlphabet alphabet = new VSPAAlphabet();
        alphabet.addCallAndReturnSymbol("[", "]");
        alphabet.addCallAndReturnSymbol("{", "}");
        alphabet.addInternalSymbol("null"); 
        alphabet.addInternalSymbol("false"); 
        alphabet.addInternalSymbol("true");
        alphabet.addInternalSymbol("\"\\\\I\"");
        alphabet.addInternalSymbol("\"\\\\D\"");
        alphabet.addInternalSymbol("\"\\\\S\"");
        alphabet.addInternalSymbol("\"\\\\E\"");
        alphabet.addInternalSymbol("#");

        for (String k : keySymbols)
            alphabet.addInternalSymbol(k);

        // Procedural Automaton A^S0
        State q0S0 = new State("q0S0", false);
        State q1S0 = new State("q1S0", false);
        q0S0.addTransition("\"$schema\"", q1S0);
        State q2S0 = new State("q2S0", false);
        q1S0.addTransition("\"\\\\S\"", q2S0);
        State q3S0 = new State("q3S0", false);
        q2S0.addTransition("#", q3S0);
        State q4S0 = new State("q4S0", false);
        q3S0.addTransition("\"proxies\"", q4S0);
        State q5S0 = new State("q5S0", true);
        q4S0.addTransition("S1", q5S0);

        JSONProceduralAutomaton S0 = new JSONProceduralAutomaton("S0");
        S0.setInitialState(q0S0);

        // Procedural Automaton A^S1  --> SelfRepository
        State q0S1 = new State("q0S1", false);
        State q1S1 = new State("q1S1", false);
        q0S1.addTransition("\"<ProxyName>\"", q1S1);
        State q2S1 = new State("q2S1", true);
        q1S1.addTransition("S3", q2S1);
        State q3S1 = new State("q3S1", false);
        q2S1.addTransition("#", q3S1);
        State q4S1 = new State("q4S1", false);
        q3S1.addTransition("\"\\\\S\"", q4S1);
        State q5S1 = new State("q5S1", true);
        q4S1.addTransition("S3", q5S1);
        
        JSONProceduralAutomaton S1 = new JSONProceduralAutomaton("S1");
        S1.setInitialState(q0S1);
        

        // // Procedural Automaton A^S2 --> dependencies
        // State q0S2 = new State("q0S2", true);
        // State q1S2 = new State("q1S2", false);
        // q0S2.addTransition("\"\\\\S\"", q1S2);
        // State q2S2 = new State("q2S2", true);
        // q1S2.addTransition("S3", q2S2);
        
        // JSONProceduralAutomaton S2 = new JSONProceduralAutomaton("S2");
        // S2.setInitialState(q0S2);


        // Procedural Automaton A^S3  --> Proxy-Schema
        State q0S3 = new State("q0S3", false);
        State q1S3 = new State("q1S3", false);
        q0S3.addTransition("\"desc\"", q1S3);
        State q2S3 = new State("q2S3", false);
        q1S3.addTransition("S8", q2S3);
        State q3S3 = new State("q3S3", false);
        q2S3.addTransition("#", q3S3);
        State q4S3 = new State("q4S3", false);
        q3S3.addTransition("\"matchCondition\"", q4S3);
        State q5S3 = new State("q5S3", false);
        q4S3.addTransition("S4", q5S3);
        State q6S3 = new State("q6S3", false);
        q5S3.addTransition("#", q6S3);
        State q7S3 = new State("q7S3", false);
        q6S3.addTransition("\"backendUri\"", q7S3);
        State q8S3 = new State("q8S3", false);
        q7S3.addTransition("\"\\\\S\"", q8S3);
        State q9S3 = new State("q9S3", false);
        q8S3.addTransition("#", q9S3);
        State q10S3 = new State("q10S3", false);
        q9S3.addTransition("\"requestOverrides\"", q10S3);
        State q11S3 = new State("q11S3", false);
        q10S3.addTransition("S5", q11S3);
        State q12S3 = new State("q12S3", false);
        q11S3.addTransition("#", q12S3);
        State q13S3 = new State("q13S3", false);
        q12S3.addTransition("\"responseOverrides\"", q13S3);
        State q14S3 = new State("q14S3", false);
        q13S3.addTransition("S6", q14S3);
        State q15S2 = new State("q15S2", false);
        q14S3.addTransition("#", q15S2);
        State q16S2 = new State("q16S2", false);
        q15S2.addTransition("\"debug\"", q16S2);
        State q17S2 = new State("q17S2", false);
        q16S2.addTransition("true", q17S2);
        q16S2.addTransition("false", q17S2);
        State q18S2 = new State("q18S2", false);
        q17S2.addTransition("#", q18S2);
        State q19S2 = new State("q19S2", false);
        q18S2.addTransition("\"disabled\"", q19S2);
        State q20S2 = new State("q20S2", true);
        q19S2.addTransition("true", q20S2);
        q19S2.addTransition("false", q20S2);
        // q16S2.addTransition("Uo", q17S2);
        // q16S2.addTransition("Ua", q17S2);
        // q16S2.addTransition("null", q17S2);
        // q16S2.addTransition("true", q17S2);
        // q16S2.addTransition("false", q17S2);
        // q16S2.addTransition("\"\\\\I\"", q17S2);
        // q16S2.addTransition("\"\\\\D\"", q17S2);
        // q16S2.addTransition("\"\\\\S\"", q17S2);
        // q16S2.addTransition("\"\\\\E\"", q17S2);

        JSONProceduralAutomaton S3 = new JSONProceduralAutomaton("S3");
        S3.setInitialState(q0S3);

        
        // Procedural Automaton A^S4  --> MatchCondition
        State q0S4 = new State("q0S4", false);
        State q1S4 = new State("q1S4", false);
        q0S4.addTransition("\"methods\"", q1S4);
        State q2S4 = new State("q2S4", false);
        q1S4.addTransition("S7", q2S4);
        State q3S4 = new State("q3S4", false);
        q2S4.addTransition("#", q3S4);
        State q4S4 = new State("q4S4", false);
        q3S4.addTransition("\"route\"", q4S4);
        State q5S4 = new State("q5S4", true);
        q4S4.addTransition("\"\\\\S\"", q5S4);

        JSONProceduralAutomaton S4 = new JSONProceduralAutomaton("S4");
        S4.setInitialState(q0S4);

        // Procedural Automaton A^S5  --> RequestOverrides
        State q0S5 = new State("q0S5", false);
        State q1S5 = new State("q1S5", false);
        q0S5.addTransition("\"backend.request.method\"", q1S5);
        State q2S5 = new State("q2S5", false);
        q1S5.addTransition("Uo", q2S5);
        q1S5.addTransition("Ua", q2S5);
        q1S5.addTransition("null", q2S5);
        q1S5.addTransition("true", q2S5);
        q1S5.addTransition("false", q2S5);
        q1S5.addTransition("\"\\\\I\"", q2S5);
        q1S5.addTransition("\"\\\\D\"", q2S5);
        q1S5.addTransition("\"\\\\S\"", q2S5);
        q1S5.addTransition("\"\\\\E\"", q2S5);
        State q3S5 = new State("q3S5", false);
        q2S5.addTransition("#", q3S5);
        State q4S5 = new State("q4S5", false);
        q3S5.addTransition("\"backend.request.querystring.<ParameterName>\"", q4S5);
        State q5S5 = new State("q5S5", false);
        q4S5.addTransition("\"\\\\S\"", q5S5);
        State q6S5 = new State("q6S5", false);
        q5S5.addTransition("#", q6S5);
        State q7S5 = new State("q7S5", false);
        q6S5.addTransition("\"backend.request.headers.<HeaderName>\"", q7S5);
        State q8S5 = new State("q8S5", true);
        q7S5.addTransition("\"\\\\S\"", q8S5);
        State q9S5 = new State("q9S5", false);
        q8S5.addTransition("#", q9S5);
        State q10S5 = new State("q10S5", false);
        q9S5.addTransition("\"^backendrequestquerystring.+$\"", q10S5);
        State q11S5 = new State("q11S5", true);
        q10S5.addTransition("\"\\\\S\"", q11S5);
        State q12S5 = new State("q12S5", false);
        q11S5.addTransition("#", q12S5);
        q8S5.addTransition("#", q12S5);
        State q13S5 = new State("q13S5", false);
        q12S5.addTransition("\"^backendrequestheaders.+$\"", q13S5);
        State q14S5 = new State("q14S5", true);
        q13S5.addTransition("\"\\\\S\"", q14S5);
        
        JSONProceduralAutomaton S5 = new JSONProceduralAutomaton("S5");
        S5.setInitialState(q0S5);

        // Procedural Automaton A^S6  --> ResponseOverrides
        State q0S6 = new State("q0S6", false);
        State q1S6 = new State("q1S6", false);
        q0S6.addTransition("\"response.statusCode\"", q1S6);
        State q2S6 = new State("q2S6", false);
        q1S6.addTransition("\"\\\\S\"", q2S6);
        State q3S6 = new State("q3S6", false);
        q2S6.addTransition("#", q3S6);
        State q4S6 = new State("q4S6", false);
        q3S6.addTransition("\"response.headers.<HeaderName>\"", q4S6);
        State q5S6 = new State("q5S6", false);
        q4S6.addTransition("\"\\\\S\"", q5S6);
        State q6S6 = new State("q6S6", false);
        q5S6.addTransition("#", q6S6);
        State q7S6 = new State("q7S6", false);
        q6S6.addTransition("\"response.statusReason\"", q7S6);
        State q8S6 = new State("q8S6", false);
        q7S6.addTransition("\"\\\\S\"", q8S6);
        State q9S6 = new State("q9S6", false);
        q8S6.addTransition("#", q9S6);
        State q10S6 = new State("q10S6", false);
        q9S6.addTransition("\"response.body\"", q10S6);
        State q11S6 = new State("q11S6", true);
        q10S6.addTransition("\"\\\\S\"", q11S6);
        q10S6.addTransition("S9", q11S6);
        q10S6.addTransition("Uo", q11S6);
        State q12S6 = new State("q12S6", false);
        q11S6.addTransition("#", q12S6);
        State q13S6 = new State("q13S6", false);
        q12S6.addTransition("\"^responseheaders.+$\"", q13S6);
        State q14S6 = new State("q14S6", true);
        q13S6.addTransition("\"\\\\S\"", q14S6);

        JSONProceduralAutomaton S6 = new JSONProceduralAutomaton("S6");
        S6.setInitialState(q0S6);

        // Procedural Automaton A^S7  --> Methods
        State q0S7 = new State("q0S7", false);
        State q1S7 = new State("q1S7", true);
        q0S7.addTransition("\"\\\\E\"", q1S7);
        State q2S7 = new State("q2S7", false);
        q1S7.addTransition("#", q2S7);
        q2S7.addTransition("\"\\\\E\"", q1S7);

        JSONProceduralAutomaton S7 = new JSONProceduralAutomaton("S7");
        S7.setInitialState(q0S7);

        // Procedural Automaton A^S8  --> desc
        State q0S8 = new State("q0S8", true);
        State q1S8 = new State("q1S8", true);
        q0S8.addTransition("\"\\\\S\"", q1S8);
        State q2S8 = new State("q2S8", false);
        q1S8.addTransition("#", q2S8);
        q2S8.addTransition("\"\\\\S\"", q1S8);

        JSONProceduralAutomaton S8 = new JSONProceduralAutomaton("S8");
        S8.setInitialState(q0S8);

        // Procedural Automaton A^S9  --> ResponseBody
        State q0S9 = new State("q0S9", false);
        State q1S9 = new State("q1S9", true);
        q0S9.addTransition("Uo", q1S9);
        State q2S9 = new State("q2S9", false);
        q1S9.addTransition("#", q2S9);
        q2S9.addTransition("Uo", q1S9);

        JSONProceduralAutomaton S9 = new JSONProceduralAutomaton("S9");
        S9.setInitialState(q0S9);


        // Procedural Automaton A^Uo  --> Universal Object
        State q0Uo = new State("q0Uo", true);
        State q1Uo = new State("q1Uo", false);
        q0Uo.addTransition("\"\\\\S\"", q1Uo);
        State q2Uo = new State("q2Uo", true);
        q1Uo.addTransition("\"\\\\S\"", q2Uo);
        q1Uo.addTransition("Uo", q2Uo);
        q1Uo.addTransition("Ua", q2Uo);
        q1Uo.addTransition("null", q2Uo);
        q1Uo.addTransition("true", q2Uo);
        q1Uo.addTransition("false", q2Uo);
        q1Uo.addTransition("\"\\\\I\"", q2Uo);
        q1Uo.addTransition("\"\\\\D\"", q2Uo);
        q1Uo.addTransition("\"\\\\S\"", q2Uo);
        q1Uo.addTransition("\"\\\\E\"", q2Uo);

        JSONProceduralAutomaton Uo = new JSONProceduralAutomaton("Uo");
        Uo.setInitialState(q0Uo);

        
        // Procedural Automaton A^Uo  --> Universal Object
        State q0Ua = new State("q0Ua", true);
        State q1Ua = new State("q1Ua", true);
        q0Ua.addTransition("Uo", q1Ua);
        q0Ua.addTransition("Ua", q1Ua);
        q0Ua.addTransition("null", q1Ua);
        q0Ua.addTransition("true", q1Ua);
        q0Ua.addTransition("false", q1Ua);
        q0Ua.addTransition("\"\\\\I\"", q1Ua);
        q0Ua.addTransition("\"\\\\D\"", q1Ua);
        q0Ua.addTransition("\"\\\\S\"", q1Ua);
        q0Ua.addTransition("\"\\\\E\"", q1Ua);
        State q2Ua = new State("q2Ua", false);
        q1Ua.addTransition("#", q2Ua);

        q2Ua.addTransition("\"\\\\S\"", q1Ua);
        q2Ua.addTransition("Uo", q1Ua);
        q2Ua.addTransition("Ua", q1Ua);
        q2Ua.addTransition("null", q1Ua);
        q2Ua.addTransition("true", q1Ua);
        q2Ua.addTransition("false", q1Ua);
        q2Ua.addTransition("\"\\\\I\"", q1Ua);
        q2Ua.addTransition("\"\\\\D\"", q1Ua);
        q2Ua.addTransition("\"\\\\S\"", q1Ua);
        q2Ua.addTransition("\"\\\\E\"", q1Ua);

        JSONProceduralAutomaton Ua = new JSONProceduralAutomaton("Ua");
        Ua.setInitialState(q0Ua);


        // Definition VSPA
        JSONVSPA vspa = new JSONVSPA();
        vspa.setVSPAAlphabet(alphabet);
        vspa.addProceduralAutomaton(S0, "{");
        vspa.addProceduralAutomaton(S1, "{");
        vspa.addProceduralAutomaton(S3, "{");
        vspa.addProceduralAutomaton(S4, "{");
        vspa.addProceduralAutomaton(S5, "{");
        vspa.addProceduralAutomaton(S6, "{");
        vspa.addProceduralAutomaton(S7, "[");
        vspa.addProceduralAutomaton(S8, "[");
        vspa.addProceduralAutomaton(S9, "[");
        vspa.addProceduralAutomaton(Uo, "{");
        vspa.addProceduralAutomaton(Ua, "[");
        vspa.setStartingAutomaton(S0);

        System.out.println("PROXIES Key Graph");
        keyGraphMeasures(vspa, keySymbols);

        // if (TestResult.DEBUG) {
        //     KeyGraph keyGraph = S3.getKeyGraph(); 
        //     for (Vertex v : keyGraph.getVertices()) {
        //         if (keyGraph.isInitialVertex(v)) {
        //             System.out.print("Initial ");
        //         }
        //         System.out.println("Vertex: " + v.toString());
        //         for (Vertex v2 : keyGraph.getEdges(v)) {
        //             System.out.println("  Adjacent: " + v2.toString());
        //         }
        //     }
        // }

        runResult("proxies", vspa);
    }

    
    public static void runResult(String jsontype, JSONVSPA automaton) {
        String directoryPath = "D:\\TFE2-code-gaetan\\ValidatingJSONDocumentsWithLearnedVPA\\schemas\\benchmarks\\"+ jsontype + "\\Documents\\" + jsontype + ".json\\Random";
        if (jsontype.equals("vim")) {
            directoryPath = "D:\\TFE2-code-gaetan\\ValidatingJSONDocumentsWithLearnedVPA\\schemas\\benchmarks\\vim\\Documents\\vim-addon-info.json\\Random\\Done";
        }
        if (jsontype.equals("proxies")) {
            directoryPath = "D:\\TFE2-code-gaetan\\ValidatingJSONDocumentsWithLearnedVPA\\schemas\\benchmarks\\"+ jsontype + "\\Documents\\"+ jsontype +".json\\Random\\all";
        }
        File directory = new File(directoryPath);
        System.out.println(directoryPath);
        List<List<String>> datas = new ArrayList<>();

        int i = 0;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        if (i != 6355) {
                            i++;
                            continue;
                        } 
                        System.out.println("Processing file: " + file.getName());
                        if (file.getName().contains("valid-2737")) {
                            break;
                        }

                        List<String> json = SanitizeJSON.sanitizeJSON(file.getAbsolutePath());
                        
                        long tot_time = 0;
                        int test_case = 100;
                        for (int j = 0; j < test_case; j++) {
                            long time = System.nanoTime();
                            automaton.accepts(json,false,TestResult.DEBUG);
                            time = (System.nanoTime() - time);
                            tot_time += time;
                        }

                        long average_time = tot_time / test_case;

                        long time = System.nanoTime();
                        boolean result = automaton.accepts(json,false,TestResult.DEBUG).first;
                        time = (System.nanoTime() - time);
                        
                        Long memory;
                        if (!TestResult.DEBUG) {
                            System.gc();
                            memory = automaton.accepts(json,true,TestResult.DEBUG).second;
                        }
                        else {
                            memory = getMemoryUse();
                        }

                        System.out.println("----------------------------------------------------------------------");
                        System.out.println(file.getAbsolutePath());
                        System.out.println(json);
                        System.out.println("      -> " + (result ? "ACCEPTED" : "REJECTED"));
                        System.out.println("    Time: " + average_time/1_000 + "µs");
                        System.out.println("    Memory: " + memory + "KB");
                        
                        List<String> data = new ArrayList<>();
                        data.add(file.getName());
                        data.add(String.valueOf((int) time/1_000));
                        data.add(String.valueOf(memory));
                        data.add(String.valueOf(result));
                        datas.add(data);

                        System.out.println("i = " + i);
                        i++;
                        
                        if (TestResult.DEBUG) {
                            if (i > 30) {
                                break; 
                            }
                        }
                        
                    } catch (IOException e) {
                        System.out.println("Error while opening file " + file.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
            }
        }

        // System.out.println(datas.toString());

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

    public static void keyGraphMeasures(JSONVSPA vspa, Set<String> keySymbols) {
        long tot_time = 0;
        int test_case = 10;
        for (int i = 0; i < 10; i++) {
            vspa.createKeyGraphs(keySymbols);
        }

        for (int i = 0; i < test_case; i++) {
            System.gc();
            long time = System.nanoTime();
            vspa.createKeyGraphs(keySymbols);
            time = (System.nanoTime() - time);
            tot_time += time;
        }

        long tot_memory = 0;
        for (int i = 0; i < test_case; i++) {
            System.gc();
            long memory = getMemoryUse();
            vspa.createKeyGraphs(keySymbols);
            memory = getMemoryUse() - memory;
            tot_memory += memory;
        }

        System.out.println("     Key Graph created in " + tot_time/test_case/1_000 + "µs and " + tot_memory/test_case + "KB of memory.");
        System.out.println("     Size of all Key Graphs " + vspa.getKeyGraphSize() + " states.");
    }
}
