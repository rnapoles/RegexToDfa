package org.example;

import java.util.*;

/**
 * Generated lexer class from NFA.
 * This class provides tokenization capabilities based on the NFA structure.
 */
public class StarLexer {
    
    // Token types
    public static final String TOKEN_Q1 = "TOKEN_Q1";
    public static final String EOF = "EOF";
    public static final String ERROR = "ERROR";
    
    private String input;
    private int position;
    private int startPosition;
    
    public StarLexer(String input) {
        this.input = input;
        this.position = 0;
        this.startPosition = 0;
    }
    
    /**
     * Gets the next token from the input.
     * @return the next token or null if end of input
     */
    public Token nextToken() {
        if (position >= input.length()) {
            return new Token(EOF, "", position);
        }
        
        startPosition = position;
        Set<String> currentStates = new HashSet<>();
        currentStates.add("q0");
        
        // Apply epsilon closure
        currentStates = getEpsilonClosure(currentStates);
        
        StringBuilder tokenValue = new StringBuilder();
        Set<String> lastAcceptingStates = new HashSet<>();
        int lastAcceptingPosition = position;
        
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            Set<String> nextStates = new HashSet<>();
            
            // Get all possible next states
            for (String state : currentStates) {
                Set<String> transitions = getTransitions(state, currentChar);
                nextStates.addAll(transitions);
            }
            
            // Apply epsilon closure to next states
            nextStates = getEpsilonClosure(nextStates);
            
            if (nextStates.isEmpty()) {
                // No transitions available
                if (!lastAcceptingStates.isEmpty()) {
                    // Return token from last accepting state
                    position = lastAcceptingPosition;
                    return new Token(getTokenType(lastAcceptingStates), tokenValue.toString(), startPosition);
                } else {
                    // Error state
                    return new Token(ERROR, String.valueOf(currentChar), position);
                }
            }
            
            // Check for accepting states
            Set<String> acceptingStates = getAcceptingStates(nextStates);
            if (!acceptingStates.isEmpty()) {
                lastAcceptingStates = acceptingStates;
                lastAcceptingPosition = position + 1;
            }
            
            currentStates = nextStates;
            tokenValue.append(currentChar);
            position++;
        }
        
        // End of input reached
        if (!lastAcceptingStates.isEmpty()) {
            return new Token(getTokenType(lastAcceptingStates), tokenValue.toString(), startPosition);
        } else {
            return new Token(ERROR, tokenValue.toString(), startPosition);
        }
    }
    
    /**
     * Gets all tokens from the input.
     * @return list of all tokens
     */
    public List<Token> getAllTokens() {
        List<Token> tokens = new ArrayList<>();
        Token token;
        
        while ((token = nextToken()) != null && !token.getType().equals(EOF)) {
            tokens.add(token);
        }
        
        tokens.add(new Token(EOF, "", position));
        return tokens;
    }
    
    private Set<String> getTransitions(String state, char symbol) {
        Set<String> result = new HashSet<>();
        // Generated transition logic would go here
        // For now, return empty set
        return result;
    }
    
    private Set<String> getEpsilonClosure(Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        boolean changed = true;
        
        while (changed) {
            changed = false;
            Set<String> newStates = new HashSet<>();
            
            for (String state : closure) {
                // Generated epsilon transition logic would go here
                // For now, no epsilon transitions
            }
            
            closure.addAll(newStates);
        }
        
        return closure;
    }
    
    private Set<String> getAcceptingStates(Set<String> states) {
        Set<String> accepting = new HashSet<>();
        if (states.contains("q1")) {
            accepting.add("q1");
        }
        return accepting;
    }
    
    private String getTokenType(Set<String> acceptingStates) {
        // Return the first accepting state's token type
        // In practice, you might want to implement priority rules
        if (acceptingStates.contains("q1")) {
            return TOKEN_Q1;
        }
        return ERROR;
    }

}