package test;

import automaton.*;
import vspa.*;

public class testVSPA {
    public static void main(String[] args) {
        //long start_memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
        testVSPAWithGeneric();
        //System.out.println("Memory used by testVSPAWithGeneric: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 - start_memory) + " KB");
        // long start_memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
        testVSPAWithJSON();
        // System.out.println("Memory used by testVSPAWithJSON: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 - start_memory) + " KB");
    }

    private static void testVSPAWithGeneric() {
        // Alphabet
        VSPAAlphabet alphabet = new VSPAAlphabet();
        alphabet.addCallAndReturnSymbol("a", "z");
        alphabet.addInternalSymbol("b");

        // États
        State qS0 = new State("qS0", true);
        State qR0 = new State("qR0", false);
        State qR1 = new State("qR1", true);

        // Transitions 
        qS0.addTransition("R", qS0);
        qS0.addTransition("S", qS0);
        qR0.addTransition("b", qR1);

        // Définition du Procedural Automaton 
        ProceduralAutomaton S = new ProceduralAutomaton("S", qS0);
        ProceduralAutomaton R = new ProceduralAutomaton("R", qR0);

        // Definition VSPA
        VisiblySystemProceduralAutomata vspa = new VisiblySystemProceduralAutomata();
        vspa.setVSPAAlphabet(alphabet);
        vspa.addProceduralAutomaton(S, "a");
        vspa.addProceduralAutomaton(R, "a");
        vspa.setStartingAutomaton(S);

        // Test - L(VSPA) == grammar : S -> a(S+R)*z  ; R -> abz
        String[] words = {"a", "abz", "ab", "aabzz", "az", "aabzazz", "aabazzz", "aaz", "b", "aazz", "aabzazz", "aaaabzabzzabzzz", "aaabzabzzabzzz"};
        for (String w : words) {
            System.out.println("Input: " + w + " -> " + (vspa.accepts(w) ? "ACCEPTED" : "REJECTED"));
        }
    }


    private static void testVSPAWithJSON() {
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

        ProceduralAutomaton S0 = new ProceduralAutomaton("S0", q00);


        // Procedural Automaton A^S1
        State q10 = new State("q10", true);
        State q11 = new State("q11", true);
        State q12 = new State("q12", false);

        q10.addTransition("s", q11);
        q11.addTransition("#", q12);
        q12.addTransition("s", q11);

        ProceduralAutomaton S1 = new ProceduralAutomaton("S1", q10);


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

        ProceduralAutomaton S2 = new ProceduralAutomaton("S2", q20);


        // Definition VSPA
        VisiblySystemProceduralAutomata vspa = new VisiblySystemProceduralAutomata();
        vspa.setVSPAAlphabet(alphabet);
        vspa.addProceduralAutomaton(S0, "{");
        vspa.addProceduralAutomaton(S1, "[");
        vspa.addProceduralAutomaton(S2, "{");
        vspa.setStartingAutomaton(S0);


        // Test - L(VPA) == grammar : S -> a(S+R)*z  ; R -> abz
        String[] words = {"{}", "{as#b[s#s#s]#c{dn#ei}}", "{b[s#s#s]#c{dn#ei}}",
                        "{as#b[s#s#s]#c{}}", "{as#b[s#s#s#s#s#s#s]#c{}}", "{as#b[}#c{}}", "{as#b[]#c{}}", 
                        "{as#b[s#s#]#c{}}", "{as#b[s#s#s#s]#c{dn}}", "{as#b[s#s#s#s]#c{ei}}", 
                        "[s#s]", "{as#b[s]#c{}}"
                        };
        for (String w : words) {
            System.out.println("Input: " + w + " -> " + (vspa.accepts(w) ? "ACCEPTED" : "REJECTED"));
        }
    }
}
