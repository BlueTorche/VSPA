package vspa;

import java.util.*;

public class VSPAAlphabet {
    private final Set<String> callSymbols = new HashSet<>();
    private final Set<String> returnSymbols = new HashSet<>();
    private final Set<String> internalSymbols = new HashSet<>();
    private final Set<String> proceduralSymbols = new HashSet<>();

    private final Map<String,String> callToReturn = new HashMap<>();
    private final Map<String,String> returnToCall = new HashMap<>();
    private final Map<String, String> proceduralToCall = new HashMap<>();
    private final Map<String, Set<String>> callToProcedurals = new HashMap<>();

    public enum SymbolType {
        CALL,
        INTERNAL,
        RETURN,
        PROCEDURAL
    }

    public void addCallAndReturnSymbol(String c, String r) {
        callSymbols.add(c);
        returnSymbols.add(r);
        callToReturn.put(c,r);
        returnToCall.put(r,c);
        callToProcedurals.put(c, new HashSet<>());
    }

    public void addInternalSymbol(String symbol) {
        internalSymbols.add(symbol);
    }
    public void addProceduralSymbol(String p, String c) {
        proceduralSymbols.add(p);
        proceduralToCall.put(p,c);
        callToProcedurals.get(c).add(p);
    }

    public Set<String> getCallSymbols() {
        return callSymbols;
    }

    public Set<String> getReturnSymbols() {
        return returnSymbols;
    }

    public Set<String> getInternalSymbols() {
        return internalSymbols;
    }
    public Set<String> getProceduralSymbols() {
        return proceduralSymbols;
    }



    public String getReturnFromCallSymbol(String c) {
        return callToReturn.get(c);
    }

    public String getCallFromReturnSymbol(String r) {
        return returnToCall.get(r);
    }

    public String getCallFromProcedural(String p) { return proceduralToCall.get(p); }
    public Set<String> getProceduralsFromCall(String c) { return callToProcedurals.get(c); }


    public SymbolType kindOfSymbol(String symbol) {
        if (callSymbols.contains(symbol))
            return SymbolType.CALL;
        if (returnSymbols.contains(symbol))
            return SymbolType.RETURN;
        if (internalSymbols.contains(symbol))
            return SymbolType.INTERNAL;
        if (proceduralSymbols.contains(symbol))
            return SymbolType.PROCEDURAL;
        return null;
    }
}
