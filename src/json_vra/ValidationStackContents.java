package json_vra;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import json_vspa.Vertex;

public class ValidationStackContents<L> {
    private final Set<PairSourceToReached<L>> sourceToReachedLocationsBeforeCall;
    private final Set<String> seenKeys = new LinkedHashSet<>();
    private final Set<Vertex<L>> acceptedNodes = new LinkedHashSet<>();
    private String currentKey = null;
    private final ValidationStackContents<L> rest;

    private ValidationStackContents(final Set<PairSourceToReached<L>> sourceToReachedLocations,
                                    final ValidationStackContents<L> rest) {
        this.sourceToReachedLocationsBeforeCall = sourceToReachedLocations;
        this.rest = rest;
    }

    public boolean addKey(String key) {
        currentKey = key;
        return seenKeys.add(key);
    }

    public Set<PairSourceToReached<L>> peekSourceToReachedLocationsBeforeCall() {
        return sourceToReachedLocationsBeforeCall;
    }

    public Set<L> peekReachedLocationsBeforeCall() {
        // @formatter:off
        return sourceToReachedLocationsBeforeCall.stream()
                .map(pair -> pair.getReachedLocation())
                .collect(Collectors.toSet());
        // @formatter:on
    }

    public Set<Vertex<L>> peekAcceptedNodes() {
        return acceptedNodes;
    }

    public void markAccepted(Vertex<L> node) {
        acceptedNodes.add(node);
    }

    public Set<String> peekSeenKeys() {
        return seenKeys;
    }

    public String peekCurrentKey() {
        return currentKey;
    }

    public ValidationStackContents<L> pop() {
        return rest;
    }

    public static <L> ValidationStackContents<L> push(final Set<PairSourceToReached<L>> sourceToReachedLocations, final ValidationStackContents<L> rest) {
        return new ValidationStackContents<>(sourceToReachedLocations, rest);
    }
}