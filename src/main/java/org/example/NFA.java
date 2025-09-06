package org.example;

import java.util.*;

/**
 * Non-deterministic Finite Automaton implementation.
 * Follows SOLID principles with clear separation of concerns.
 */
public class NFA {
    private final Set<String> states;
    private final Set<Character> alphabet;
    private final Map<String, Map<Character, Set<String>>> transitions;
    private final Map<String, Set<String>> epsilonTransitions;
    private String startState;
    private final Set<String> acceptingStates;
    
    public NFA() {
        this.states = new HashSet<String>();
        this.alphabet = new HashSet<Character>();
        this.transitions = new HashMap<String, Map<Character, Set<String>>>();
        this.epsilonTransitions = new HashMap<String, Set<String>>();
        this.startState = null;
        this.acceptingStates = new HashSet<String>();
    }
    
    public NFA(String startState, Set<String> acceptingStates) {
        this.states = new HashSet<String>();
        this.alphabet = new HashSet<Character>();
        this.transitions = new HashMap<String, Map<Character, Set<String>>>();
        this.epsilonTransitions = new HashMap<String, Set<String>>();
        this.startState = startState;
        this.acceptingStates = new HashSet<String>(acceptingStates);
        if (startState != null) {
            this.states.add(startState);
        }
        this.states.addAll(acceptingStates);
    }
    
    /**
     * Checks if the NFA accepts the given input string.
     */
    public boolean accepts(String input) {
        if (startState == null) {
            return false;
        }
        
        Set<String> currentStates = new HashSet<String>();
        currentStates.add(startState);
        
        // Apply epsilon closures
        currentStates = getEpsilonClosure(currentStates);
        
        for (char symbol : input.toCharArray()) {
            Set<String> nextStates = new HashSet<String>();
            for (String state : currentStates) {
                Set<String> transitions = getTransitions(state, symbol);
                nextStates.addAll(transitions);
            }
            currentStates = getEpsilonClosure(nextStates);
        }
        
        // Check if any current state is accepting
        for (String state : currentStates) {
            if (acceptingStates.contains(state)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Creates the Kleene star operation (zero or more repetitions).
     */
    public NFA star() {
        NFA result = new NFA();
        String newStart = "start_" + System.nanoTime();
        String newAccept = "accept_" + System.nanoTime();
        
        result.states.add(newStart);
        result.states.add(newAccept);
        result.acceptingStates.add(newAccept);
        result.startState = newStart; // Set the start state
        
        // Add epsilon transition from new start to original start
        if (startState != null) {
            result.addEpsilonTransition(newStart, startState);
            result.states.addAll(this.states);
            result.alphabet.addAll(this.alphabet);
            result.transitions.putAll(this.transitions);
            result.epsilonTransitions.putAll(this.epsilonTransitions);
        }
        
        // Add epsilon transition from new start to new accept
        result.addEpsilonTransition(newStart, newAccept);
        
        // Add epsilon transitions from original accepting states to original start
        for (String acceptingState : acceptingStates) {
            if (startState != null) {
                result.addEpsilonTransition(acceptingState, startState);
            }
            result.addEpsilonTransition(acceptingState, newAccept);
        }
        
        return result;
    }
    
    /**
     * Creates the plus operation (one or more repetitions).
     */
    public NFA plus() {
        NFA starResult = this.star();
        NFA result = this.concatenate(starResult);
        return result;
    }
    
    /**
     * Creates the question operation (zero or one occurrence).
     */
    public NFA question() {
        NFA result = new NFA();
        String newStart = "start_" + System.nanoTime();
        String newAccept = "accept_" + System.nanoTime();
        
        result.states.add(newStart);
        result.states.add(newAccept);
        result.acceptingStates.add(newAccept);
        result.startState = newStart; // Set the start state
        
        // Add epsilon transition from new start to new accept (zero occurrences)
        result.addEpsilonTransition(newStart, newAccept);
        
        // Add the original NFA
        if (startState != null) {
            result.addEpsilonTransition(newStart, startState);
            result.states.addAll(this.states);
            result.alphabet.addAll(this.alphabet);
            result.transitions.putAll(this.transitions);
            result.epsilonTransitions.putAll(this.epsilonTransitions);
            
            // Add epsilon transitions from original accepting states to new accept
            for (String acceptingState : acceptingStates) {
                result.addEpsilonTransition(acceptingState, newAccept);
            }
        }
        
        return result;
    }
    
    /**
     * Creates the union of this NFA with another NFA.
     */
    public NFA union(NFA other) {
        NFA result = new NFA();
        String newStart = "start_" + System.nanoTime();
        String newAccept = "accept_" + System.nanoTime();
        
        result.states.add(newStart);
        result.states.add(newAccept);
        result.acceptingStates.add(newAccept);
        result.startState = newStart; // Set the start state
        
        // Add this NFA
        if (startState != null) {
            result.addEpsilonTransition(newStart, startState);
            result.states.addAll(this.states);
            result.alphabet.addAll(this.alphabet);
            result.transitions.putAll(this.transitions);
            result.epsilonTransitions.putAll(this.epsilonTransitions);
            
            for (String acceptingState : acceptingStates) {
                result.addEpsilonTransition(acceptingState, newAccept);
            }
        }
        
        // Add other NFA
        if (other.startState != null) {
            result.addEpsilonTransition(newStart, other.startState);
            result.states.addAll(other.states);
            result.alphabet.addAll(other.alphabet);
            result.transitions.putAll(other.transitions);
            result.epsilonTransitions.putAll(other.epsilonTransitions);
            
            for (String acceptingState : other.acceptingStates) {
                result.addEpsilonTransition(acceptingState, newAccept);
            }
        }
        
        return result;
    }
    
    /**
     * Creates the concatenation of this NFA with another NFA.
     */
    public NFA concatenate(NFA other) {
        NFA result = new NFA();
        
        // Add this NFA
        if (startState != null) {
            result.states.addAll(this.states);
            result.alphabet.addAll(this.alphabet);
            result.transitions.putAll(this.transitions);
            result.epsilonTransitions.putAll(this.epsilonTransitions);
        }
        
        // Add other NFA
        if (other.startState != null) {
            result.states.addAll(other.states);
            result.alphabet.addAll(other.alphabet);
            result.transitions.putAll(other.transitions);
            result.epsilonTransitions.putAll(other.epsilonTransitions);
        }
        
        // Connect accepting states of this NFA to start state of other NFA
        if (startState != null && other.startState != null) {
            for (String acceptingState : acceptingStates) {
                result.addEpsilonTransition(acceptingState, other.startState);
            }
        }
        
        // Set start state and accepting states
        if (startState != null) {
            result.startState = startState;
        } else if (other.startState != null) {
            result.startState = other.startState;
        }
        
        if (other.startState != null) {
            result.acceptingStates.addAll(other.acceptingStates);
        } else if (startState != null) {
            result.acceptingStates.addAll(this.acceptingStates);
        }
        
        return result;
    }
    
    /**
     * Adds a transition from one state to another on a given symbol.
     */
    public void addTransition(String fromState, char symbol, String toState) {
        states.add(fromState);
        states.add(toState);
        alphabet.add(symbol);
        
        if (!transitions.containsKey(fromState)) {
            transitions.put(fromState, new HashMap<Character, Set<String>>());
        }
        if (!transitions.get(fromState).containsKey(symbol)) {
            transitions.get(fromState).put(symbol, new HashSet<String>());
        }
        transitions.get(fromState).get(symbol).add(toState);
    }
    
    /**
     * Adds an epsilon transition from one state to another.
     */
    public void addEpsilonTransition(String fromState, String toState) {
        states.add(fromState);
        states.add(toState);
        
        if (!epsilonTransitions.containsKey(fromState)) {
            epsilonTransitions.put(fromState, new HashSet<String>());
        }
        epsilonTransitions.get(fromState).add(toState);
    }
    
    /**
     * Converts this NFA to a DFA using the subset construction algorithm.
     */
    public DFA toDFA() {
        DFA dfa = new DFA();
        Map<StateSet, String> stateSetToName = new HashMap<StateSet, String>();
        Queue<StateSet> workQueue = new LinkedList<StateSet>();
        
        // Create initial state set
        StateSet initialStateSet = new StateSet();
        if (startState != null) {
            initialStateSet.addState(startState);
            initialStateSet = getEpsilonClosure(initialStateSet);
        }
        
        String initialStateName = "q0";
        stateSetToName.put(initialStateSet, initialStateName);
        dfa.addState(initialStateName, isAcceptingStateSet(initialStateSet));
        workQueue.offer(initialStateSet);
        
        while (!workQueue.isEmpty()) {
            StateSet currentStateSet = workQueue.poll();
            String currentStateName = stateSetToName.get(currentStateSet);
            
            for (char symbol : alphabet) {
                StateSet nextStateSet = new StateSet();
                
                for (String state : currentStateSet.getStates()) {
                    Set<String> transitions = getTransitions(state, symbol);
                    nextStateSet.addAllStates(transitions);
                }
                
                nextStateSet = getEpsilonClosure(nextStateSet);
                
                if (!nextStateSet.isEmpty()) {
                    String nextStateName = stateSetToName.get(nextStateSet);
                    if (nextStateName == null) {
                        nextStateName = "q" + stateSetToName.size();
                        stateSetToName.put(nextStateSet, nextStateName);
                        dfa.addState(nextStateName, isAcceptingStateSet(nextStateSet));
                        workQueue.offer(nextStateSet);
                    }
                    dfa.addTransition(currentStateName, symbol, nextStateName);
                }
            }
        }
        
        // Set the start state
        dfa = dfa.setStartState(initialStateName);
        
        return dfa;
    }
    
    // Private helper methods
    
    private Set<String> getTransitions(String state, char symbol) {
        if (transitions.containsKey(state) && transitions.get(state).containsKey(symbol)) {
            return new HashSet<String>(transitions.get(state).get(symbol));
        }
        return new HashSet<String>();
    }
    
    private StateSet getEpsilonClosure(StateSet stateSet) {
        StateSet closure = new StateSet(stateSet.getStates());
        boolean changed = true;
        
        while (changed) {
            changed = false;
            Set<String> newStates = new HashSet<String>();
            
            for (String state : closure.getStates()) {
                if (epsilonTransitions.containsKey(state)) {
                    for (String epsilonTarget : epsilonTransitions.get(state)) {
                        if (!closure.contains(epsilonTarget)) {
                            newStates.add(epsilonTarget);
                            changed = true;
                        }
                    }
                }
            }
            
            closure.addAllStates(newStates);
        }
        
        return closure;
    }
    
    private Set<String> getEpsilonClosure(Set<String> states) {
        StateSet stateSet = new StateSet(states);
        StateSet closure = getEpsilonClosure(stateSet);
        return closure.getStates();
    }
    
    private boolean isAcceptingStateSet(StateSet stateSet) {
        for (String state : stateSet.getStates()) {
            if (acceptingStates.contains(state)) {
                return true;
            }
        }
        return false;
    }
}
