package org.example;

import java.util.*;

/**
 * Deterministic Finite Automaton implementation.
 * Follows SOLID principles with clear separation of concerns.
 */
public class DFA {
    private final Set<String> states;
    private final Set<Character> alphabet;
    private final Map<String, Map<Character, String>> transitions;
    private final String startState;
    private final Set<String> acceptingStates;
    
    public DFA() {
        this.states = new HashSet<String>();
        this.alphabet = new HashSet<Character>();
        this.transitions = new HashMap<String, Map<Character, String>>();
        this.startState = null;
        this.acceptingStates = new HashSet<String>();
    }
    
    public DFA(String startState, Set<String> acceptingStates) {
        this.states = new HashSet<String>();
        this.alphabet = new HashSet<Character>();
        this.transitions = new HashMap<String, Map<Character, String>>();
        this.startState = startState;
        this.acceptingStates = new HashSet<String>(acceptingStates);
        if (startState != null) {
            this.states.add(startState);
        }
        this.states.addAll(acceptingStates);
    }
    
    /**
     * Checks if the DFA accepts the given input string.
     */
    public boolean accepts(String input) {
        if (startState == null) {
            return false;
        }
        
        String currentState = startState;
        
        for (char symbol : input.toCharArray()) {
            if (!transitions.containsKey(currentState) || 
                !transitions.get(currentState).containsKey(symbol)) {
                return false; // No transition defined for this symbol
            }
            currentState = transitions.get(currentState).get(symbol);
        }
        
        return acceptingStates.contains(currentState);
    }
    
    /**
     * Adds a state to the DFA.
     * 
     * @param stateName the name of the state to add
     * @param isAccepting whether this state is an accepting state
     * @throws IllegalArgumentException if stateName is null or empty
     */
    public void addState(String stateName, boolean isAccepting) {
        // Validate input
        if (stateName == null || stateName.trim().isEmpty()) {
            throw new IllegalArgumentException("State name cannot be null or empty");
        }
        
        // Add the state to the set of states
        states.add(stateName);
        
        // Add to accepting states if specified
        if (isAccepting) {
            acceptingStates.add(stateName);
        }
        
        // Note: Since startState is final, it cannot be set here.
        // The start state must be set during construction or through a separate method.
        // This design ensures immutability of the start state once the DFA is created.
    }

    /**
     * Sets the start state of the DFA by creating a new DFA instance.
     * Since the start state is final, this method returns a new DFA with the specified start state.
     * 
     * @param newStartState the state to set as the start state
     * @return a new DFA instance with the specified start state and all existing data
     * @throws IllegalArgumentException if newStartState is not in the current set of states
     */
    public DFA setStartState(String newStartState) {
        if (newStartState == null || !states.contains(newStartState)) {
            throw new IllegalArgumentException("Start state must be a valid state in the DFA");
        }
        
        // Create new DFA with the new start state
        DFA newDFA = new DFA(newStartState, acceptingStates);
        
        // Copy all states
        newDFA.states.addAll(this.states);
        
        // Copy alphabet
        newDFA.alphabet.addAll(this.alphabet);
        
        // Copy all transitions
        for (Map.Entry<String, Map<Character, String>> entry : this.transitions.entrySet()) {
            String fromState = entry.getKey();
            Map<Character, String> stateTransitions = entry.getValue();
            newDFA.transitions.put(fromState, new HashMap<Character, String>(stateTransitions));
        }
        
        return newDFA;
    }
    
    /**
     * Adds a transition from one state to another on a given symbol.
     */
    public void addTransition(String fromState, char symbol, String toState) {
        states.add(fromState);
        states.add(toState);
        alphabet.add(symbol);
        
        if (!transitions.containsKey(fromState)) {
            transitions.put(fromState, new HashMap<Character, String>());
        }
        transitions.get(fromState).put(symbol, toState);
    }
    
    /**
     * Minimizes the DFA using the table-filling algorithm.
     */
    public DFA minimizeDFA() {
        // Step 1: Remove unreachable states
        DFA reachableDFA = removeUnreachableStates();
        
        // Step 2: Remove dead states
        DFA liveDFA = removeDeadStates(reachableDFA);
        
        // Step 3: Use table-filling algorithm to find equivalent states
        Map<String, String> equivalenceClasses = findEquivalentStates(liveDFA);
        
        // Step 4: Build minimized DFA
        return buildMinimizedDFA(liveDFA, equivalenceClasses);
    }
    
    /**
     * Generates a DOT representation of the DFA for visualization.
     */
    public String toDot() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph DFA {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  size=\"8,5\";\n");
        
        // Add start arrow
        if (startState != null) {
            dot.append("  start [shape=point];\n");
            dot.append("  start -> \"").append(startState).append("\";\n");
        }
        
        // Add states
        for (String state : states) {
            if (acceptingStates.contains(state)) {
                dot.append("  \"").append(state).append("\" [shape=doublecircle];\n");
            } else {
                dot.append("  \"").append(state).append("\" [shape=circle];\n");
            }
        }
        
        // Add transitions
        for (Map.Entry<String, Map<Character, String>> entry : transitions.entrySet()) {
            String fromState = entry.getKey();
            for (Map.Entry<Character, String> transition : entry.getValue().entrySet()) {
                char symbol = transition.getKey();
                String toState = transition.getValue();
                dot.append("  \"").append(fromState).append("\" -> \"").append(toState)
                   .append("\" [label=\"").append(symbol).append("\"];\n");
            }
        }
        
        dot.append("}\n");
        return dot.toString();
    }
    
    // Private helper methods for minimization
    
    private DFA removeUnreachableStates() {
        Set<String> reachableStates = new HashSet<String>();
        if (startState != null) {
            reachableStates.add(startState);
            findReachableStates(startState, reachableStates);
        }
        
        DFA reachableDFA = new DFA();
        if (startState != null && reachableStates.contains(startState)) {
            reachableDFA.addState(startState, acceptingStates.contains(startState));
        }
        
        for (String state : reachableStates) {
            if (!state.equals(startState)) {
                reachableDFA.addState(state, acceptingStates.contains(state));
            }
        }
        
        for (Map.Entry<String, Map<Character, String>> entry : transitions.entrySet()) {
            String fromState = entry.getKey();
            if (reachableStates.contains(fromState)) {
                for (Map.Entry<Character, String> transition : entry.getValue().entrySet()) {
                    char symbol = transition.getKey();
                    String toState = transition.getValue();
                    if (reachableStates.contains(toState)) {
                        reachableDFA.addTransition(fromState, symbol, toState);
                    }
                }
            }
        }
        
        // Set the start state
        if (startState != null && reachableStates.contains(startState)) {
            reachableDFA = reachableDFA.setStartState(startState);
        }
        
        return reachableDFA;
    }
    
    private void findReachableStates(String state, Set<String> reachableStates) {
        if (transitions.containsKey(state)) {
            for (String nextState : transitions.get(state).values()) {
                if (!reachableStates.contains(nextState)) {
                    reachableStates.add(nextState);
                    findReachableStates(nextState, reachableStates);
                }
            }
        }
    }
    
    private DFA removeDeadStates(DFA dfa) {
        Set<String> liveStates = new HashSet<String>();
        liveStates.addAll(dfa.acceptingStates);
        
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String state : dfa.states) {
                if (!liveStates.contains(state)) {
                    for (String nextState : dfa.transitions.get(state).values()) {
                        if (liveStates.contains(nextState)) {
                            liveStates.add(state);
                            changed = true;
                            break;
                        }
                    }
                }
            }
        }
        
        DFA liveDFA = new DFA();
        if (dfa.startState != null && liveStates.contains(dfa.startState)) {
            liveDFA.addState(dfa.startState, dfa.acceptingStates.contains(dfa.startState));
        }
        
        for (String state : liveStates) {
            if (!state.equals(dfa.startState)) {
                liveDFA.addState(state, dfa.acceptingStates.contains(state));
            }
        }
        
        for (Map.Entry<String, Map<Character, String>> entry : dfa.transitions.entrySet()) {
            String fromState = entry.getKey();
            if (liveStates.contains(fromState)) {
                for (Map.Entry<Character, String> transition : entry.getValue().entrySet()) {
                    char symbol = transition.getKey();
                    String toState = transition.getValue();
                    if (liveStates.contains(toState)) {
                        liveDFA.addTransition(fromState, symbol, toState);
                    }
                }
            }
        }
        
        // Set the start state
        if (dfa.startState != null && liveStates.contains(dfa.startState)) {
            liveDFA = liveDFA.setStartState(dfa.startState);
        }
        
        return liveDFA;
    }
    
    private Map<String, String> findEquivalentStates(DFA dfa) {
        Map<String, String> equivalenceClasses = new HashMap<String, String>();
        
        // Initialize: all states start in their own equivalence class
        for (String state : dfa.states) {
            equivalenceClasses.put(state, state);
        }
        
        boolean changed = true;
        while (changed) {
            changed = false;
            Map<String, String> newEquivalenceClasses = new HashMap<String, String>();
            
            for (String state : dfa.states) {
                String currentClass = equivalenceClasses.get(state);
                boolean isAccepting = dfa.acceptingStates.contains(state);
                
                // Find if there's an existing class with same accepting status and transitions
                String equivalentClass = null;
                for (Map.Entry<String, String> entry : newEquivalenceClasses.entrySet()) {
                    String otherState = entry.getKey();
                    String otherClass = entry.getValue();
                    boolean otherAccepting = dfa.acceptingStates.contains(otherState);
                    
                    if (isAccepting == otherAccepting && 
                        haveEquivalentTransitions(dfa, state, otherState, equivalenceClasses)) {
                        equivalentClass = otherClass;
                        break;
                    }
                }
                
                if (equivalentClass != null) {
                    newEquivalenceClasses.put(state, equivalentClass);
                } else {
                    newEquivalenceClasses.put(state, currentClass);
                }
            }
            
            if (!equivalenceClasses.equals(newEquivalenceClasses)) {
                equivalenceClasses = newEquivalenceClasses;
                changed = true;
            }
        }
        
        return equivalenceClasses;
    }
    
    private boolean haveEquivalentTransitions(DFA dfa, String state1, String state2, 
                                           Map<String, String> equivalenceClasses) {
        Map<Character, String> transitions1 = dfa.transitions.get(state1);
        Map<Character, String> transitions2 = dfa.transitions.get(state2);
        
        if (transitions1 == null && transitions2 == null) {
            return true;
        }
        if (transitions1 == null || transitions2 == null) {
            return false;
        }
        
        for (char symbol : dfa.alphabet) {
            String nextState1 = transitions1.get(symbol);
            String nextState2 = transitions2.get(symbol);
            
            if (nextState1 == null && nextState2 == null) {
                continue;
            }
            if (nextState1 == null || nextState2 == null) {
                return false;
            }
            
            String class1 = equivalenceClasses.get(nextState1);
            String class2 = equivalenceClasses.get(nextState2);
            if (!class1.equals(class2)) {
                return false;
            }
        }
        
        return true;
    }
    
    private DFA buildMinimizedDFA(DFA dfa, Map<String, String> equivalenceClasses) {
        Map<String, String> classToRepresentative = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : equivalenceClasses.entrySet()) {
            String state = entry.getKey();
            String className = entry.getValue();
            if (!classToRepresentative.containsKey(className)) {
                classToRepresentative.put(className, state);
            }
        }
        
        DFA minimizedDFA = new DFA();
        
        // Add states
        for (String className : classToRepresentative.keySet()) {
            String representative = classToRepresentative.get(className);
            boolean isAccepting = dfa.acceptingStates.contains(representative);
            minimizedDFA.addState(className, isAccepting);
        }
        
        // Add transitions
        for (Map.Entry<String, String> entry : classToRepresentative.entrySet()) {
            String className = entry.getKey();
            String representative = entry.getValue();
            
            if (dfa.transitions.containsKey(representative)) {
                for (Map.Entry<Character, String> transition : dfa.transitions.get(representative).entrySet()) {
                    char symbol = transition.getKey();
                    String nextState = transition.getValue();
                    String nextClassName = equivalenceClasses.get(nextState);
                    minimizedDFA.addTransition(className, symbol, nextClassName);
                }
            }
        }
        
        // Set the start state for the minimized DFA
        if (dfa.startState != null) {
            String startClassName = equivalenceClasses.get(dfa.startState);
            minimizedDFA = minimizedDFA.setStartState(startClassName);
        }
        
        return minimizedDFA;
    }
    
    // Getters for lexer generation
    
    public Set<String> getStates() {
        return new HashSet<>(states);
    }
    
    public Set<Character> getAlphabet() {
        return new HashSet<>(alphabet);
    }
    
    public Map<String, Map<Character, String>> getTransitions() {
        Map<String, Map<Character, String>> result = new HashMap<>();
        for (Map.Entry<String, Map<Character, String>> entry : transitions.entrySet()) {
            result.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        return result;
    }
    
    public String getStartState() {
        return startState;
    }
    
    public Set<String> getAcceptingStates() {
        return new HashSet<>(acceptingStates);
    }

}
