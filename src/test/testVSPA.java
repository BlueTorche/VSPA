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
        VRA_State qS0 = new VRA_State("qS0", true);
        VRA_State qR0 = new VRA_State("qR0", false);
        VRA_State qR1 = new VRA_State("qR1", true);

        // Transitions
        qS0.addTransition("R", qS0);
        qS0.addTransition("S", qS0);
        qR0.addTransition("b", qR1);

        // Définition du Procedural Automaton
        ProceduralAutomaton S = new ProceduralAutomaton("S", qS0);
        ProceduralAutomaton R = new ProceduralAutomaton("R", qR0);

        // Definition VSPA
        VisiblySystemProceduralAutomata vspa = new VisiblySystemProceduralAutomata(alphabet);
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

        ProceduralAutomaton S0 = new ProceduralAutomaton("S0", q00);


        // Procedural Automaton A^S1
        VRA_State q10 = new VRA_State("q10", true);
        VRA_State q11 = new VRA_State("q11", true);
        VRA_State q12 = new VRA_State("q12", false);

        q10.addTransition("s", q11);
        q11.addTransition("#", q12);
        q12.addTransition("s", q11);

        ProceduralAutomaton S1 = new ProceduralAutomaton("S1", q10);


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

        ProceduralAutomaton S2 = new ProceduralAutomaton("S2", q20);


        // Definition VSPA
        VisiblySystemProceduralAutomata vspa = new VisiblySystemProceduralAutomata(alphabet);
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
