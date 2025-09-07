package org.example;

/**
 * Represents a single transition entry in a state-transition table.
 * Follows Single Responsibility Principle by only handling transition data.
 */
public class TransitionEntry {
    private final String fromState;
    private final char symbol;
    private final String toState;
    
    /**
     * Creates a new transition entry.
     * 
     * @param fromState the source state
     * @param symbol the input symbol
     * @param toState the destination state
     * @throws IllegalArgumentException if any parameter is null or invalid
     */
    public TransitionEntry(String fromState, char symbol, String toState) {
        if (fromState == null || fromState.trim().isEmpty()) {
            throw new IllegalArgumentException("From state cannot be null or empty");
        }
        if (toState == null || toState.trim().isEmpty()) {
            throw new IllegalArgumentException("To state cannot be null or empty");
        }
        
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }
    
    /**
     * Gets the source state of this transition.
     * 
     * @return the from state
     */
    public String getFromState() {
        return fromState;
    }
    
    /**
     * Gets the input symbol for this transition.
     * 
     * @return the symbol
     */
    public char getSymbol() {
        return symbol;
    }
    
    /**
     * Gets the destination state of this transition.
     * 
     * @return the to state
     */
    public String getToState() {
        return toState;
    }
    
    /**
     * Checks if this transition matches the given state and symbol.
     * 
     * @param state the state to check
     * @param symbol the symbol to check
     * @return true if this transition matches, false otherwise
     */
    public boolean matches(String state, char symbol) {
        return this.fromState.equals(state) && this.symbol == symbol;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        TransitionEntry that = (TransitionEntry) obj;
        return symbol == that.symbol &&
               fromState.equals(that.fromState) &&
               toState.equals(that.toState);
    }
    
    @Override
    public int hashCode() {
        int result = fromState.hashCode();
        result = 31 * result + (int) symbol;
        result = 31 * result + toState.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("TransitionEntry{from='%s', symbol='%c', to='%s'}", 
                           fromState, symbol, toState);
    }
}
