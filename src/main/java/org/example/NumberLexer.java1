package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generated lexer class from DFA.
 * This class provides tokenization capabilities based on the DFA structure.
 */
public class NumberLexer {

    // Token types
    public static final String TOKEN_Q1 = "TOKEN_Q1";
    public static final String EOF = "EOF";
    public static final String ERROR = "ERROR";

    // Current state
    private String currentState;
    private final String input;
    private int position;
    private int startPosition;

    public NumberLexer(String input) {
        this.input = input;
        this.position = 0;
        this.startPosition = 0;
        this.currentState = "q0";
    }

    public static void main(String[] args) {

        // Test with various inputs
        String[] testInputs = {"1234567890", "42", "0", "999", "abc123", "123abc", "12 34"};
        
        for (String input : testInputs) {
            System.out.println("Testing input: \"" + input + "\"");
            NumberLexer lexer = new NumberLexer(input);
            List<Token> tokens = lexer.getAllTokens();

            System.out.println("Tokens found:");
            System.out.println("------------");

            for (Token token : tokens) {
                System.out.printf("Type: %-15s | Value: \"%s\" | Position: %d%n",
                        token.getType(),
                        token.getValue(),
                        token.getPosition());
            }

            System.out.println();
            System.out.println("Total tokens: " + tokens.size());
            
            // Check for errors
            System.out.println("Checking for errors...");
            System.out.println("Tokens: " + tokens);
            var errorTokens = tokens.stream()
                .filter(token -> "ERROR".equals(token.getType()))
                .toList();
            
            if (!errorTokens.isEmpty()) {

                long errorCount = errorTokens.size();
                System.out.println("WARNING: " + errorCount + " error token(s) found!");

                for (Token token : errorTokens) {
                    System.out.println("Error token: " + token.getValue());
                    System.out.println("Error position: " + token.getPosition());
                    System.out.println("Error type: " + token.getType());
                    System.out.println("Error state: " + lexer.currentState);
                    //System.out.println("Error symbol: " + token.getValue().charAt(token.getPosition()));
                }

            } else {
                System.out.println("✓ No errors found - all input was successfully tokenized!");
            }

            System.out.println();
        }
    }

    /**
     * Gets the next token from the input.
     *
     * @return the next token or null if end of input
     */
    public Token nextToken() {
        if (position >= input.length()) {
            return new Token(EOF, "", position);
        }

        startPosition = position;
        currentState = "q0";
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
     *
     * @return list of all tokens
     */
    public List<Token> getAllTokens() {
        List<Token> tokens = new ArrayList<>();
        Token token;

        while ((token = nextToken()) != null && !token.getType().equals(EOF)) {
            if (token.getType().equals(ERROR)) {
                /*
                System.out.println("Error token: " + token.getValue());
                System.out.println("Error position: " + token.getPosition());
                System.out.println("Error type: " + token.getType());
                System.out.println("Error state: " + currentState);
                //System.out.println("Error symbol: " + token.getValue().charAt(token.getPosition()));
                System.out.println("Error input: " + input);
                //System.out.println("Error next state: " + getNextState(currentState, token.getValue().charAt(token.getPosition())));
                System.out.println("Error accepting state: " + isAcceptingState(currentState));
                System.out.println("Error token type: " + getTokenType(currentState));
                */
                tokens.add(token);
                break;
            }
            tokens.add(token);
        }

        tokens.add(new Token(EOF, "", position));
        return tokens;
    }

    private String getNextState(String currentState, char symbol) {
        // Generated transition logic for [0-9]+ pattern
        if ("q1".equals(currentState)) {
            // From accepting state q1, any digit stays in q1
            if (symbol >= '0' && symbol <= '9') {
                return "q1";
            }
        }
        if ("q0".equals(currentState)) {
            // From start state q0, any digit goes to accepting state q1
            if (symbol >= '0' && symbol <= '9') {
                return "q1";
            }
        }
        return null;
    }

    private boolean isAcceptingState(String state) {
        return "q1".equals(state);
    }

    private String getTokenType(String state) {
        if ("q1".equals(state)) {
            return TOKEN_Q1;
        }
        return ERROR;
    }
}