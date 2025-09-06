package org.example;

import java.util.Objects;

/**
 * Represents a state in an automaton.
 * Follows the Single Responsibility Principle by only managing state identity.
 */
public class State {
    private final String name;
    private final boolean isAccepting;
    
    public State(String name, boolean isAccepting) {
        this.name = name;
        this.isAccepting = isAccepting;
    }
    
    public State(String name) {
        this(name, false);
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isAccepting() {
        return isAccepting;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        State state = (State) obj;
        return Objects.equals(name, state.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return name;
    }
}

