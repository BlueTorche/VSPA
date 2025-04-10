package vspa;

import java.util.*;

public class VSPAAlphabet {
    private final Set<String> callSymbols = new HashSet<>();
    private final Set<String> returnSymbols = new HashSet<>();
    private final Set<String> internalSymbols = new HashSet<>();
    private final Map<String,String> callToReturn = new HashMap<>();
    private final Map<String,String> returnToCall = new HashMap<>();

    public void addCallAndReturnSymbol(String c, String r) {
        callSymbols.add(c);
        returnSymbols.add(r);
        callToReturn.put(c,r);
        returnToCall.put(r,c);
    }

    public void addInternalSymbol(String symbol) {
        internalSymbols.add(symbol);
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

    public String getReturnFromCallSymbol(String c) {
        return callToReturn.get(c);
    }

    public String getCallFromReturnSymbol(String r) {
        return returnToCall.get(r);
    }
}
