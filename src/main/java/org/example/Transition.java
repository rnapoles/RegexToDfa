package org.example;

import java.util.Objects;

/**
 * Represents a transition in an automaton.
 * Follows the Single Responsibility Principle by only managing transition data.
 */
public class Transition {
    private final String fromState;
    private final Character symbol; // null for epsilon transitions
    private final String toState;
    
    public Transition(String fromState, Character symbol, String toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }
    
    public String getFromState() {
        return fromState;
    }
    
    public Character getSymbol() {
        return symbol;
    }
    
    public String getToState() {
        return toState;
    }
    
    public boolean isEpsilonTransition() {
        return symbol == null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transition that = (Transition) obj;
        return Objects.equals(fromState, that.fromState) &&
               Objects.equals(symbol, that.symbol) &&
               Objects.equals(toState, that.toState);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fromState, symbol, toState);
    }
    
    @Override
    public String toString() {
        if (isEpsilonTransition()) {
            return fromState + " --ε--> " + toState;
        } else {
            return fromState + " --" + symbol + "--> " + toState;
        }
    }
}

