package com.example;

import java.util.*;

/**
 * Generated lexer class from DFA.
 * This class provides tokenization capabilities based on the DFA structure.
 */
public class NumberLexer {
    
    // Token types
    public static final String q0 = "q0";
        public static final String TOKEN_Q1 = "TOKEN_Q1";
        public static final String EOF = "EOF";
    public static final String ERROR = "ERROR";
    
    // Current state
    private String currentState;
    private final String input;
    private int position;
    private int startPosition;
    
    // States
    private final Set<String> states;
    
    public NumberLexer(String input) {
        this.states = new HashSet<String>();
        this.input = input;
        this.position = 0;
        this.startPosition = 0;
        this.currentState = q0;
        
          this.states.add("q1");
          this.states.add("q0");
    
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
        currentState = q0;
        StringBuilder tokenValue = new StringBuilder();
        
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            String nextState = getNextState(currentState, currentChar);
            
            if (nextState == null) {
                // No transition available, check if current state is accepting
                if (isAcceptingState(currentState)) {
                    return new Token(getTokenType(currentState), tokenValue.toString(), startPosition);
                } else {
                    // Error state
                    return new Token(ERROR, String.valueOf(currentChar), position);
                }
            }
            
            currentState = nextState;
            tokenValue.append(currentChar);
            position++;
        }
        
        // End of input reached
        if (isAcceptingState(currentState)) {
            return new Token(getTokenType(currentState), tokenValue.toString(), startPosition);
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

            if (token.getType().equals(ERROR)) {
                tokens.add(token);
                break;
            }

            tokens.add(token);
        }
        
        tokens.add(new Token(EOF, "", position));
        return tokens;
    }
    
    private String getNextState(String currentState, char symbol) {
        // Generated transition logic
                if ("q1".equals(currentState)) {
                        if (symbol == '0') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '1') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '2') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '3') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '4') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '5') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '6') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '7') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '8') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '9') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                    }
                if ("q0".equals(currentState)) {
                        if (symbol == '0') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '1') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '2') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '3') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '4') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '5') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '6') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '7') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '8') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                        if (symbol == '9') {
                // This would need to be generated based on actual transitions
                // For now, return null to indicate no transition
                return null;
            }
                    }
                return null;
    }
    
    private Set<String> getStates() {
        return this.states;
    }

    private boolean isAcceptingState(String state) {

                  return "q1".equals(state))
            }
    
    private String getTokenType(String state) {
                if ("q1".equals(state)) {
            return TOKEN_Q1;
        }
                return ERROR;
    }

}