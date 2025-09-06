package org.example;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a set of states, used for DFA state representation.
 * Follows the Single Responsibility Principle by managing state sets.
 */
public class StateSet {
    private final Set<String> states;
    
    public StateSet() {
        this.states = new HashSet<String>();
    }
    
    public StateSet(Set<String> states) {
        this.states = new HashSet<String>(states);
    }
    
    public void addState(String state) {
        states.add(state);
    }
    
    public void addAllStates(Set<String> states) {
        this.states.addAll(states);
    }
    
    public boolean contains(String state) {
        return states.contains(state);
    }
    
    public Set<String> getStates() {
        return new HashSet<String>(states);
    }
    
    public boolean isEmpty() {
        return states.isEmpty();
    }
    
    public int size() {
        return states.size();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StateSet stateSet = (StateSet) obj;
        return states.equals(stateSet.states);
    }
    
    @Override
    public int hashCode() {
        return states.hashCode();
    }
    
    @Override
    public String toString() {
        return states.toString();
    }
}

