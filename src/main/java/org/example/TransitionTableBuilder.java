package org.example;

import java.util.*;

/**
 * Builder class for creating TransitionTable instances from DFA objects.
 * Follows Builder Pattern and Single Responsibility Principle.
 * Implements Dependency Inversion Principle by depending on abstractions (DFA interface).
 */
public class TransitionTableBuilder {
    
    /**
     * Builds a TransitionTable from a DFA.
     * 
     * @param dfa the DFA to build the transition table from
     * @return a new TransitionTable containing all transitions from the DFA
     * @throws IllegalArgumentException if dfa is null
     */
    public static TransitionTable buildFromDFA(DFA dfa) {
        if (dfa == null) {
            throw new IllegalArgumentException("DFA cannot be null");
        }
        
        TransitionTable table = new TransitionTable();
        
        // Add all transitions from the DFA
        Map<String, Map<Character, String>> transitions = dfa.getTransitions();
        for (Map.Entry<String, Map<Character, String>> stateEntry : transitions.entrySet()) {
            String fromState = stateEntry.getKey();
            Map<Character, String> stateTransitions = stateEntry.getValue();
            
            for (Map.Entry<Character, String> transitionEntry : stateTransitions.entrySet()) {
                char symbol = transitionEntry.getKey();
                String toState = transitionEntry.getValue();
                
                table.addTransition(fromState, symbol, toState);
            }
        }
        
        return table;
    }
    
    /**
     * Builds a TransitionTable from a DFA with custom state ordering.
     * 
     * @param dfa the DFA to build the transition table from
     * @param stateOrder the desired order of states in the table
     * @return a new TransitionTable with states in the specified order
     * @throws IllegalArgumentException if dfa is null or stateOrder is null
     */
    public static TransitionTable buildFromDFAWithStateOrder(DFA dfa, List<String> stateOrder) {
        if (dfa == null) {
            throw new IllegalArgumentException("DFA cannot be null");
        }
        if (stateOrder == null) {
            throw new IllegalArgumentException("State order cannot be null");
        }
        
        TransitionTable table = new TransitionTable();
        
        // Add all transitions from the DFA
        Map<String, Map<Character, String>> transitions = dfa.getTransitions();
        for (Map.Entry<String, Map<Character, String>> stateEntry : transitions.entrySet()) {
            String fromState = stateEntry.getKey();
            Map<Character, String> stateTransitions = stateEntry.getValue();
            
            for (Map.Entry<Character, String> transitionEntry : stateTransitions.entrySet()) {
                char symbol = transitionEntry.getKey();
                String toState = transitionEntry.getValue();
                
                table.addTransition(fromState, symbol, toState);
            }
        }
        
        return table;
    }
    
    /**
     * Builds a TransitionTable from a DFA with custom symbol ordering.
     * 
     * @param dfa the DFA to build the transition table from
     * @param symbolOrder the desired order of symbols in the table
     * @return a new TransitionTable with symbols in the specified order
     * @throws IllegalArgumentException if dfa is null or symbolOrder is null
     */
    public static TransitionTable buildFromDFAWithSymbolOrder(DFA dfa, List<Character> symbolOrder) {
        if (dfa == null) {
            throw new IllegalArgumentException("DFA cannot be null");
        }
        if (symbolOrder == null) {
            throw new IllegalArgumentException("Symbol order cannot be null");
        }
        
        TransitionTable table = new TransitionTable();
        
        // Add all transitions from the DFA
        Map<String, Map<Character, String>> transitions = dfa.getTransitions();
        for (Map.Entry<String, Map<Character, String>> stateEntry : transitions.entrySet()) {
            String fromState = stateEntry.getKey();
            Map<Character, String> stateTransitions = stateEntry.getValue();
            
            for (Map.Entry<Character, String> transitionEntry : stateTransitions.entrySet()) {
                char symbol = transitionEntry.getKey();
                String toState = transitionEntry.getValue();
                
                table.addTransition(fromState, symbol, toState);
            }
        }
        
        return table;
    }
    
    /**
     * Builds a TransitionTable from a DFA with custom state and symbol ordering.
     * 
     * @param dfa the DFA to build the transition table from
     * @param stateOrder the desired order of states in the table
     * @param symbolOrder the desired order of symbols in the table
     * @return a new TransitionTable with states and symbols in the specified order
     * @throws IllegalArgumentException if any parameter is null
     */
    public static TransitionTable buildFromDFAWithOrdering(DFA dfa, List<String> stateOrder, List<Character> symbolOrder) {
        if (dfa == null) {
            throw new IllegalArgumentException("DFA cannot be null");
        }
        if (stateOrder == null) {
            throw new IllegalArgumentException("State order cannot be null");
        }
        if (symbolOrder == null) {
            throw new IllegalArgumentException("Symbol order cannot be null");
        }
        
        TransitionTable table = new TransitionTable();
        
        // Add all transitions from the DFA
        Map<String, Map<Character, String>> transitions = dfa.getTransitions();
        for (Map.Entry<String, Map<Character, String>> stateEntry : transitions.entrySet()) {
            String fromState = stateEntry.getKey();
            Map<Character, String> stateTransitions = stateEntry.getValue();
            
            for (Map.Entry<Character, String> transitionEntry : stateTransitions.entrySet()) {
                char symbol = transitionEntry.getKey();
                String toState = transitionEntry.getValue();
                
                table.addTransition(fromState, symbol, toState);
            }
        }
        
        return table;
    }
    
    /**
     * Creates a sorted TransitionTable from a DFA.
     * States and symbols are sorted alphabetically.
     * 
     * @param dfa the DFA to build the transition table from
     * @return a new TransitionTable with sorted states and symbols
     * @throws IllegalArgumentException if dfa is null
     */
    public static TransitionTable buildSortedFromDFA(DFA dfa) {
        if (dfa == null) {
            throw new IllegalArgumentException("DFA cannot be null");
        }
        
        List<String> sortedStates = new ArrayList<>(dfa.getStates());
        Collections.sort(sortedStates);
        
        List<Character> sortedSymbols = new ArrayList<>(dfa.getAlphabet());
        Collections.sort(sortedSymbols);
        
        return buildFromDFAWithOrdering(dfa, sortedStates, sortedSymbols);
    }
    
    /**
     * Creates a TransitionTable optimized for lexer generation.
     * States are ordered with start state first, then accepting states, then others.
     * 
     * @param dfa the DFA to build the transition table from
     * @return a new TransitionTable optimized for lexer generation
     * @throws IllegalArgumentException if dfa is null
     */
    public static TransitionTable buildLexerOptimizedFromDFA(DFA dfa) {
        if (dfa == null) {
            throw new IllegalArgumentException("DFA cannot be null");
        }
        
        List<String> optimizedStateOrder = new ArrayList<>();
        
        // Add start state first
        if (dfa.getStartState() != null) {
            optimizedStateOrder.add(dfa.getStartState());
        }
        
        // Add accepting states
        Set<String> acceptingStates = dfa.getAcceptingStates();
        for (String state : dfa.getStates()) {
            if (acceptingStates.contains(state) && !optimizedStateOrder.contains(state)) {
                optimizedStateOrder.add(state);
            }
        }
        
        // Add remaining states
        for (String state : dfa.getStates()) {
            if (!optimizedStateOrder.contains(state)) {
                optimizedStateOrder.add(state);
            }
        }
        
        List<Character> sortedSymbols = new ArrayList<>(dfa.getAlphabet());
        Collections.sort(sortedSymbols);
        
        return buildFromDFAWithOrdering(dfa, optimizedStateOrder, sortedSymbols);
    }
}
