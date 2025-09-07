package org.example;

import java.util.*;

/**
 * Generated lexer class from DFA.
 * This class provides tokenization capabilities based on the DFA structure.
 */
public class AbLexer {
    
    // Token types
    //public static final String q0 = "q0";

    public static final String q1 = "q1";
    public static final String q2 = "q2";
    public static final String q0 = "q0";

    public static final String TOKEN_Q1 = "TOKEN_Q1";
    public static final String TOKEN_Q2 = "TOKEN_Q2";
    public static final String TOKEN_Q0 = "TOKEN_Q0";

    public static final String EOF = "EOF";
    public static final String ERROR = "ERROR";
    
    // Current state
    private String currentState;
    private final String input;
    private int position;
    private int startPosition;
    
    // States
    private final Set<String> states;
    
    // State-transition table as 2D array
    // Rows represent states, columns represent symbols
    private static final String[][] TRANSITION_TABLE = {
  { "q1", null}
,  { null, "q2"}
,  { null, null}

    };
    
    // State index mapping for efficient lookup
    private static final Map<String, Integer> STATE_INDEX = new HashMap<String, Integer>();
    static {
        STATE_INDEX.put("q0", 0);
        STATE_INDEX.put("q1", 1);
        STATE_INDEX.put("q2", 2);
    }
    
    // Symbol index mapping for efficient lookup
    private static final Map<Character, Integer> SYMBOL_INDEX = new HashMap<Character, Integer>();
    static {
        SYMBOL_INDEX.put('a', 0);
        SYMBOL_INDEX.put('b', 1);
    }
    
    // Alphabet of the DFA
    private static final Set<Character> alphabet = new HashSet<Character>();
    static {
        alphabet.add('a');
        alphabet.add('b');
    }
    
    // Accepting states of the DFA
    private static final Set<String> acceptingStates = new HashSet<String>();
    static {
        acceptingStates.add("q2");
    }
    
    public AbLexer(String input) {
        this.states = new HashSet<String>();
        this.input = input;
        this.position = 0;
        this.startPosition = 0;
        this.currentState = q0;
        
        this.states.add(q1);
        this.states.add(q2);
        this.states.add(q0);

    }
    
    /**
     * Gets the next token from the input.
     * @return the next token or null if end of input
     */
    public Token nextToken() {
        if (position >= input.length()) {
            return new Token(EOF, "", position);
        }
        
        currentState = q0;
        startPosition = position;
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
        // Use the transition table array for efficient lookup
        Integer stateIndex = STATE_INDEX.get(currentState);
        Integer symbolIndex = SYMBOL_INDEX.get(symbol);
        
        if (stateIndex == null || symbolIndex == null) {
            return null;
        }
        
        return TRANSITION_TABLE[stateIndex][symbolIndex];
    }
    
    private Set<String> getStates() {
        return this.states;
    }

    private boolean isAcceptingState(String state) {
        return q2.equals(state);
    }
    
    private String getTokenType(String state) {

        if (q1.equals(state)) {
            return TOKEN_Q1;
        }

        if (q2.equals(state)) {
            return TOKEN_Q2;
        }

        if (q0.equals(state)) {
            return TOKEN_Q0;
        }


        return ERROR;
    }
    
    /**
     * Returns a string representation of the transition table for debugging.
     * 
     * @return formatted string of the transition table
     */
    public String getTransitionTableString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transition Table:\n");
        sb.append("States: ");
        sb.append(String.join(", ", STATE_INDEX.keySet()));
        sb.append("\nSymbols: ");
        sb.append(SYMBOL_INDEX.keySet().toString());
        sb.append("\n\n");
        
        // Header row
        sb.append(String.format("%-10s", "State"));
        for (Character symbol : SYMBOL_INDEX.keySet()) {
            sb.append(String.format("%-8s", symbol.toString()));
        }
        sb.append("\n");
        
        // Data rows
        for (String state : STATE_INDEX.keySet()) {
            sb.append(String.format("%-10s", state));
            Integer stateIndex = STATE_INDEX.get(state);
            for (Character symbol : SYMBOL_INDEX.keySet()) {
                Integer symbolIndex = SYMBOL_INDEX.get(symbol);
                String nextState = TRANSITION_TABLE[stateIndex][symbolIndex];
                sb.append(String.format("%-8s", nextState != null ? nextState : "-"));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }

}