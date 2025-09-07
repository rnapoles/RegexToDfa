package org.example;

import java.util.*;

/**
 * Generated lexer class from DFA.
 * This class provides tokenization capabilities based on the DFA structure.
 */
public class NumberLexer {
    
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
  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1"}
,  { "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1", "q1"}
,  { "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", "q2", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}

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
        SYMBOL_INDEX.put('0', 0);
        SYMBOL_INDEX.put('1', 1);
        SYMBOL_INDEX.put('2', 2);
        SYMBOL_INDEX.put('3', 3);
        SYMBOL_INDEX.put('4', 4);
        SYMBOL_INDEX.put('5', 5);
        SYMBOL_INDEX.put('6', 6);
        SYMBOL_INDEX.put('7', 7);
        SYMBOL_INDEX.put('8', 8);
        SYMBOL_INDEX.put('9', 9);
        SYMBOL_INDEX.put('A', 10);
        SYMBOL_INDEX.put('B', 11);
        SYMBOL_INDEX.put('C', 12);
        SYMBOL_INDEX.put('D', 13);
        SYMBOL_INDEX.put('E', 14);
        SYMBOL_INDEX.put('F', 15);
        SYMBOL_INDEX.put('G', 16);
        SYMBOL_INDEX.put('H', 17);
        SYMBOL_INDEX.put('I', 18);
        SYMBOL_INDEX.put('J', 19);
        SYMBOL_INDEX.put('K', 20);
        SYMBOL_INDEX.put('L', 21);
        SYMBOL_INDEX.put('M', 22);
        SYMBOL_INDEX.put('N', 23);
        SYMBOL_INDEX.put('O', 24);
        SYMBOL_INDEX.put('P', 25);
        SYMBOL_INDEX.put('Q', 26);
        SYMBOL_INDEX.put('R', 27);
        SYMBOL_INDEX.put('S', 28);
        SYMBOL_INDEX.put('T', 29);
        SYMBOL_INDEX.put('U', 30);
        SYMBOL_INDEX.put('V', 31);
        SYMBOL_INDEX.put('W', 32);
        SYMBOL_INDEX.put('X', 33);
        SYMBOL_INDEX.put('Y', 34);
        SYMBOL_INDEX.put('Z', 35);
        SYMBOL_INDEX.put('a', 36);
        SYMBOL_INDEX.put('b', 37);
        SYMBOL_INDEX.put('c', 38);
        SYMBOL_INDEX.put('d', 39);
        SYMBOL_INDEX.put('e', 40);
        SYMBOL_INDEX.put('f', 41);
        SYMBOL_INDEX.put('g', 42);
        SYMBOL_INDEX.put('h', 43);
        SYMBOL_INDEX.put('i', 44);
        SYMBOL_INDEX.put('j', 45);
        SYMBOL_INDEX.put('k', 46);
        SYMBOL_INDEX.put('l', 47);
        SYMBOL_INDEX.put('m', 48);
        SYMBOL_INDEX.put('n', 49);
        SYMBOL_INDEX.put('o', 50);
        SYMBOL_INDEX.put('p', 51);
        SYMBOL_INDEX.put('q', 52);
        SYMBOL_INDEX.put('r', 53);
        SYMBOL_INDEX.put('s', 54);
        SYMBOL_INDEX.put('t', 55);
        SYMBOL_INDEX.put('u', 56);
        SYMBOL_INDEX.put('v', 57);
        SYMBOL_INDEX.put('w', 58);
        SYMBOL_INDEX.put('x', 59);
        SYMBOL_INDEX.put('y', 60);
        SYMBOL_INDEX.put('z', 61);
    }
    
    // Alphabet of the DFA
    private static final Set<Character> alphabet = new HashSet<Character>();
    static {
        alphabet.add('0');
        alphabet.add('1');
        alphabet.add('2');
        alphabet.add('3');
        alphabet.add('4');
        alphabet.add('5');
        alphabet.add('6');
        alphabet.add('7');
        alphabet.add('8');
        alphabet.add('9');
        alphabet.add('A');
        alphabet.add('B');
        alphabet.add('C');
        alphabet.add('D');
        alphabet.add('E');
        alphabet.add('F');
        alphabet.add('G');
        alphabet.add('H');
        alphabet.add('I');
        alphabet.add('J');
        alphabet.add('K');
        alphabet.add('L');
        alphabet.add('M');
        alphabet.add('N');
        alphabet.add('O');
        alphabet.add('P');
        alphabet.add('Q');
        alphabet.add('R');
        alphabet.add('S');
        alphabet.add('T');
        alphabet.add('U');
        alphabet.add('V');
        alphabet.add('W');
        alphabet.add('X');
        alphabet.add('Y');
        alphabet.add('Z');
        alphabet.add('a');
        alphabet.add('b');
        alphabet.add('c');
        alphabet.add('d');
        alphabet.add('e');
        alphabet.add('f');
        alphabet.add('g');
        alphabet.add('h');
        alphabet.add('i');
        alphabet.add('j');
        alphabet.add('k');
        alphabet.add('l');
        alphabet.add('m');
        alphabet.add('n');
        alphabet.add('o');
        alphabet.add('p');
        alphabet.add('q');
        alphabet.add('r');
        alphabet.add('s');
        alphabet.add('t');
        alphabet.add('u');
        alphabet.add('v');
        alphabet.add('w');
        alphabet.add('x');
        alphabet.add('y');
        alphabet.add('z');
    }
    
    // Accepting states of the DFA
    private static final Set<String> acceptingStates = new HashSet<String>();
    static {
        acceptingStates.add("q2");
    }
    
    public NumberLexer(String input) {
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