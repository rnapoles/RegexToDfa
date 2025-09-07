package org.example;

import java.util.*;

/**
 * Represents a state-transition table for a DFA.
 * Follows Single Responsibility Principle by only managing transition table operations.
 * Implements Open/Closed Principle by being extensible for different table representations.
 */
public class TransitionTable {
    private final List<TransitionEntry> transitions;
    private final Set<String> states;
    private final Set<Character> alphabet;
    
    /**
     * Creates an empty transition table.
     */
    public TransitionTable() {
        this.transitions = new ArrayList<>();
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
    }
    
    /**
     * Creates a transition table with the given transitions.
     * 
     * @param transitions the list of transitions
     * @throws IllegalArgumentException if transitions is null
     */
    public TransitionTable(List<TransitionEntry> transitions) {
        if (transitions == null) {
            throw new IllegalArgumentException("Transitions list cannot be null");
        }
        
        this.transitions = new ArrayList<>(transitions);
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        
        // Extract states and alphabet from transitions
        for (TransitionEntry transition : transitions) {
            this.states.add(transition.getFromState());
            this.states.add(transition.getToState());
            this.alphabet.add(transition.getSymbol());
        }
    }
    
    /**
     * Adds a transition to the table.
     * 
     * @param transition the transition to add
     * @throws IllegalArgumentException if transition is null
     */
    public void addTransition(TransitionEntry transition) {
        if (transition == null) {
            throw new IllegalArgumentException("Transition cannot be null");
        }
        
        transitions.add(transition);
        states.add(transition.getFromState());
        states.add(transition.getToState());
        alphabet.add(transition.getSymbol());
    }
    
    /**
     * Adds a transition to the table.
     * 
     * @param fromState the source state
     * @param symbol the input symbol
     * @param toState the destination state
     */
    public void addTransition(String fromState, char symbol, String toState) {
        addTransition(new TransitionEntry(fromState, symbol, toState));
    }
    
    /**
     * Gets the next state for a given current state and input symbol.
     * 
     * @param currentState the current state
     * @param symbol the input symbol
     * @return the next state, or null if no transition exists
     */
    public String getNextState(String currentState, char symbol) {
        for (TransitionEntry transition : transitions) {
            if (transition.matches(currentState, symbol)) {
                return transition.getToState();
            }
        }
        return null;
    }
    
    /**
     * Gets all transitions from a specific state.
     * 
     * @param fromState the source state
     * @return list of transitions from the given state
     */
    public List<TransitionEntry> getTransitionsFrom(String fromState) {
        List<TransitionEntry> result = new ArrayList<>();
        for (TransitionEntry transition : transitions) {
            if (transition.getFromState().equals(fromState)) {
                result.add(transition);
            }
        }
        return result;
    }
    
    /**
     * Gets all transitions to a specific state.
     * 
     * @param toState the destination state
     * @return list of transitions to the given state
     */
    public List<TransitionEntry> getTransitionsTo(String toState) {
        List<TransitionEntry> result = new ArrayList<>();
        for (TransitionEntry transition : transitions) {
            if (transition.getToState().equals(toState)) {
                result.add(transition);
            }
        }
        return result;
    }
    
    /**
     * Gets all transitions in the table.
     * 
     * @return unmodifiable list of all transitions
     */
    public List<TransitionEntry> getAllTransitions() {
        return Collections.unmodifiableList(transitions);
    }
    
    /**
     * Gets all states in the table.
     * 
     * @return unmodifiable set of all states
     */
    public Set<String> getStates() {
        return Collections.unmodifiableSet(states);
    }
    
    /**
     * Gets all symbols in the alphabet.
     * 
     * @return unmodifiable set of all symbols
     */
    public Set<Character> getAlphabet() {
        return Collections.unmodifiableSet(alphabet);
    }
    
    /**
     * Gets the number of transitions in the table.
     * 
     * @return the number of transitions
     */
    public int size() {
        return transitions.size();
    }
    
    /**
     * Checks if the table is empty.
     * 
     * @return true if the table has no transitions, false otherwise
     */
    public boolean isEmpty() {
        return transitions.isEmpty();
    }
    
    /**
     * Creates a 2D array representation of the transition table.
     * Rows represent states, columns represent symbols.
     * 
     * @return 2D array where [stateIndex][symbolIndex] = nextStateIndex
     */
    public String[][] toArray() {
        List<String> stateList = new ArrayList<>(states);
        List<Character> symbolList = new ArrayList<>(alphabet);
        
        String[][] table = new String[stateList.size()][symbolList.size()];
        
        // Initialize with null values
        for (int i = 0; i < stateList.size(); i++) {
            for (int j = 0; j < symbolList.size(); j++) {
                table[i][j] = null;
            }
        }
        
        // Fill in the transitions
        for (TransitionEntry transition : transitions) {
            int stateIndex = stateList.indexOf(transition.getFromState());
            int symbolIndex = symbolList.indexOf(transition.getSymbol());
            int nextStateIndex = stateList.indexOf(transition.getToState());
            
            if (stateIndex >= 0 && symbolIndex >= 0 && nextStateIndex >= 0) {
                table[stateIndex][symbolIndex] = transition.getToState();
            }
        }
        
        return table;
    }
    
    /**
     * Creates a map representation of the transition table.
     * 
     * @return map where key is (fromState, symbol) and value is toState
     */
    public Map<String, Map<Character, String>> toMap() {
        Map<String, Map<Character, String>> result = new HashMap<>();
        
        for (TransitionEntry transition : transitions) {
            String fromState = transition.getFromState();
            char symbol = transition.getSymbol();
            String toState = transition.getToState();
            
            result.computeIfAbsent(fromState, k -> new HashMap<>())
                  .put(symbol, toState);
        }
        
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        TransitionTable that = (TransitionTable) obj;
        return transitions.equals(that.transitions);
    }
    
    @Override
    public int hashCode() {
        return transitions.hashCode();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TransitionTable{\n");
        for (TransitionEntry transition : transitions) {
            sb.append("  ").append(transition).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
