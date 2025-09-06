package org.example;

import java.util.*;

/**
 * Converts regular expressions to Deterministic Finite Automata (DFA).
 * Supports metacharacters: *, ?, +, ., [, ], (, ), ^, $, |, \, \s, \t, \n, \r
 * 
 * Follows SOLID principles:
 * - Single Responsibility: Only handles regex to DFA conversion
 * - Open/Closed: Extensible for new metacharacters
 * - Liskov Substitution: Maintains interface contracts
 * - Interface Segregation: Focused interface
 * - Dependency Inversion: Depends on abstractions (NFA, DFA)
 */
public class RegexToDFAConverter {
    
    /**
     * Converts a regular expression string to a DFA.
     * 
     * @param regex the regular expression string
     * @return a DFA that accepts the language described by the regex
     * @throws IllegalArgumentException if the regex is invalid
     */
    public static DFA convert(String regex) {
        if (regex == null) {
            throw new IllegalArgumentException("Regex cannot be null");
        }
        
        if (regex.isEmpty()) {
            // Empty regex matches empty string
            return createEmptyStringDFA();
        }
        
        // Parse the regex and convert to NFA using Thompson construction
        NFA nfa = parseRegexToNFA(regex);
        
        // Convert NFA to DFA using subset construction
        return nfa.toDFA();
    }
    
    /**
     * Parses a regular expression and converts it to an NFA using Thompson construction.
     */
    private static NFA parseRegexToNFA(String regex) {
        // Preprocess the regex to handle escape sequences and character classes
        String processedRegex = preprocessRegex(regex);
        
        // Convert to postfix notation (Reverse Polish Notation)
        String postfixRegex = infixToPostfix(processedRegex);
        
        // Build NFA from postfix notation
        return buildNFAFromPostfix(postfixRegex);
    }
    
    /**
     * Preprocesses the regex to handle escape sequences and character classes.
     */
    private static String preprocessRegex(String regex) {
        StringBuilder result = new StringBuilder();
        boolean inCharacterClass = false;
        boolean escaped = false;
        
        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);
            
            if (escaped) {
                // Handle escape sequences
                switch (c) {
                    case 's':
                        result.append("[\\s]"); // whitespace character class
                        break;
                    case 't':
                        result.append("\\t"); // tab character
                        break;
                    case 'n':
                        result.append("\\n"); // newline character
                        break;
                    case 'r':
                        result.append("\\r"); // carriage return character
                        break;
                    case '\\':
                        result.append("\\\\"); // literal backslash
                        break;
                    default:
                        result.append(c); // literal character
                        break;
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '[') {
                inCharacterClass = true;
                result.append(c);
            } else if (c == ']') {
                inCharacterClass = false;
                result.append(c);
            } else if (c == '.' && !inCharacterClass) {
                // Replace . with character class for all printable characters
                result.append("[\\x20-\\x7E]");
            } else {
                result.append(c);
            }
        }
        
        if (escaped) {
            throw new IllegalArgumentException("Incomplete escape sequence at end of regex");
        }
        
        return result.toString();
    }
    
    /**
     * Converts infix notation to postfix notation (Reverse Polish Notation).
     */
    private static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operators = new Stack<Character>();
        boolean needConcat = false;
        
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            
            if (isOperand(c)) {
                if (needConcat) {
                    // Add concatenation operator
                    while (!operators.isEmpty() && 
                           getPrecedence(operators.peek()) >= getPrecedence('.')) {
                        postfix.append(operators.pop());
                    }
                    operators.push('.');
                }
                postfix.append(c);
                needConcat = true;
            } else if (c == '(') {
                if (needConcat) {
                    // Add concatenation operator
                    while (!operators.isEmpty() && 
                           getPrecedence(operators.peek()) >= getPrecedence('.')) {
                        postfix.append(operators.pop());
                    }
                    operators.push('.');
                }
                operators.push(c);
                needConcat = false;
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    postfix.append(operators.pop());
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                operators.pop(); // Remove '('
                needConcat = true;
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && 
                       operators.peek() != '(' &&
                       getPrecedence(operators.peek()) >= getPrecedence(c)) {
                    postfix.append(operators.pop());
                }
                operators.push(c);
                needConcat = false;
            } else if (c == '[') {
                // Handle character class
                StringBuilder charClass = new StringBuilder();
                charClass.append(c);
                i++; // Skip '['
                
                while (i < infix.length() && infix.charAt(i) != ']') {
                    charClass.append(infix.charAt(i));
                    i++;
                }
                
                if (i >= infix.length()) {
                    throw new IllegalArgumentException("Unclosed character class");
                }
                charClass.append(']'); // Add closing ']'
                
                if (needConcat) {
                    // Add concatenation operator
                    while (!operators.isEmpty() && 
                           getPrecedence(operators.peek()) >= getPrecedence('.')) {
                        postfix.append(operators.pop());
                    }
                    operators.push('.');
                }
                postfix.append(charClass.toString());
                needConcat = true;
            }
        }
        
        // Pop remaining operators
        while (!operators.isEmpty()) {
            if (operators.peek() == '(') {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            postfix.append(operators.pop());
        }
        
        return postfix.toString();
    }
    
    /**
     * Builds an NFA from postfix notation using Thompson construction.
     */
    private static NFA buildNFAFromPostfix(String postfix) {
        Stack<NFA> nfaStack = new Stack<NFA>();
        
        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);
            
            if (isOperand(c)) {
                // Create NFA for single character
                nfaStack.push(createCharacterNFA(c));
            } else if (c == '[') {
                // Handle character class
                StringBuilder charClass = new StringBuilder();
                i++; // Skip '['
                
                while (i < postfix.length() && postfix.charAt(i) != ']') {
                    charClass.append(postfix.charAt(i));
                    i++;
                }
                
                if (i >= postfix.length()) {
                    throw new IllegalArgumentException("Unclosed character class");
                }
                
                // Create NFA for character class
                nfaStack.push(createCharacterClassNFA(charClass.toString()));
            } else if (c == '*') {
                if (nfaStack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid regex: * without operand");
                }
                NFA nfa = nfaStack.pop();
                nfaStack.push(nfa.star());
            } else if (c == '+') {
                if (nfaStack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid regex: + without operand");
                }
                NFA nfa = nfaStack.pop();
                nfaStack.push(nfa.plus());
            } else if (c == '?') {
                if (nfaStack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid regex: ? without operand");
                }
                NFA nfa = nfaStack.pop();
                nfaStack.push(nfa.question());
            } else if (c == '|') {
                if (nfaStack.size() < 2) {
                    throw new IllegalArgumentException("Invalid regex: | requires two operands");
                }
                NFA nfa2 = nfaStack.pop();
                NFA nfa1 = nfaStack.pop();
                nfaStack.push(nfa1.union(nfa2));
            } else if (c == '.') {
                if (nfaStack.size() < 2) {
                    throw new IllegalArgumentException("Invalid regex: concatenation requires two operands");
                }
                NFA nfa2 = nfaStack.pop();
                NFA nfa1 = nfaStack.pop();
                nfaStack.push(nfa1.concatenate(nfa2));
            }
        }
        
        if (nfaStack.isEmpty()) {
            throw new IllegalArgumentException("Invalid regex: empty expression");
        }
        
        if (nfaStack.size() > 1) {
            throw new IllegalArgumentException("Invalid regex: too many operands");
        }
        
        return nfaStack.pop();
    }
    
    /**
     * Creates an NFA for a single character.
     */
    private static NFA createCharacterNFA(char c) {
        Set<String> acceptingStates = new HashSet<String>();
        String acceptState = "accept_" + System.nanoTime();
        acceptingStates.add(acceptState);
        
        String startState = "start_" + System.nanoTime();
        NFA nfa = new NFA(startState, acceptingStates);
        nfa.addTransition(startState, c, acceptState);
        
        return nfa;
    }
    
    /**
     * Creates an NFA for a character class.
     */
    private static NFA createCharacterClassNFA(String charClass) {
        Set<String> acceptingStates = new HashSet<String>();
        String acceptState = "accept_" + System.nanoTime();
        acceptingStates.add(acceptState);
        
        String startState = "start_" + System.nanoTime();
        NFA nfa = new NFA(startState, acceptingStates);
        
        // Parse character class
        Set<Character> characters = parseCharacterClass(charClass);
        
        // Create transitions for each character in the class
        for (Character c : characters) {
            nfa.addTransition(startState, c, acceptState);
        }
        
        return nfa;
    }
    
    /**
     * Parses a character class string and returns the set of characters it represents.
     */
    private static Set<Character> parseCharacterClass(String charClass) {
        Set<Character> characters = new HashSet<Character>();
        boolean negated = false;
        int i = 0;
        
        // Check for negation
        if (charClass.startsWith("^")) {
            negated = true;
            i = 1;
        }
        
        while (i < charClass.length()) {
            if (i + 2 < charClass.length() && charClass.charAt(i + 1) == '-') {
                // Handle range (e.g., a-z or \x20-\x7E)
                char start = charClass.charAt(i);
                char end = charClass.charAt(i + 2);
                
                // Handle hex ranges
                if (start == '\\' && i + 10 < charClass.length() && charClass.charAt(i + 1) == 'x' && 
                    charClass.charAt(i + 5) == '-' && charClass.charAt(i + 6) == '\\' && charClass.charAt(i + 7) == 'x') {
                    // Parse hex range like \x20-\x7E
                    String startHex = charClass.substring(i + 2, i + 4);
                    String endHex = charClass.substring(i + 8, i + 10);
                    
                    try {
                        int startValue = Integer.parseInt(startHex, 16);
                        int endValue = Integer.parseInt(endHex, 16);
                        
                        if (startValue > endValue) {
                            throw new IllegalArgumentException("Invalid hex range: \\x" + startHex + "-\\x" + endHex);
                        }
                        
                        for (int val = startValue; val <= endValue; val++) {
                            characters.add((char) val);
                        }
                        i += 10; // Skip \x20-\x7E
                        continue;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid hex range: \\x" + startHex + "-\\x" + endHex);
                    }
                }
                
                if (start > end) {
                    throw new IllegalArgumentException("Invalid character range: " + start + "-" + end);
                }
                
                for (char c = start; c <= end; c++) {
                    characters.add(c);
                }
                i += 3;
            } else {
                // Handle escape sequences in character class
                char c = charClass.charAt(i);
                if (c == '\\') {
                    i++;
                    if (i >= charClass.length()) {
                        throw new IllegalArgumentException("Incomplete escape sequence in character class");
                    }
                    c = charClass.charAt(i);
                    
                    // Handle hex characters like \x20
                    if (c == 'x' && i + 2 < charClass.length()) {
                        String hex = charClass.substring(i + 1, i + 3);
                        try {
                            int hexValue = Integer.parseInt(hex, 16);
                            characters.add((char) hexValue);
                            i += 3; // Skip \x20
                            continue;
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid hex character: \\x" + hex);
                        }
                    }
                    
                    switch (c) {
                        case 's':
                            characters.add(' '); // space
                            characters.add('\t'); // tab
                            characters.add('\n'); // newline
                            characters.add('\r'); // carriage return
                            break;
                        case 't':
                            characters.add('\t');
                            break;
                        case 'n':
                            characters.add('\n');
                            break;
                        case 'r':
                            characters.add('\r');
                            break;
                        case '\\':
                            characters.add('\\');
                            break;
                        default:
                            characters.add(c);
                            break;
                    }
                } else {
                    characters.add(c);
                }
                i++;
            }
        }
        
        if (negated) {
            // Create set of all printable ASCII characters and remove the specified ones
            Set<Character> allChars = new HashSet<Character>();
            for (char c = 32; c <= 126; c++) { // printable ASCII range
                allChars.add(c);
            }
            allChars.removeAll(characters);
            return allChars;
        }
        
        return characters;
    }
    
    /**
     * Creates a DFA that accepts only the empty string.
     */
    private static DFA createEmptyStringDFA() {
        DFA dfa = new DFA();
        String state = "q0";
        dfa.addState(state, true); // accepting state
        dfa = dfa.setStartState(state);
        return dfa;
    }
    
    /**
     * Checks if a character is an operand (not an operator).
     */
    private static boolean isOperand(char c) {
        return c != '*' && c != '+' && c != '?' && c != '|' && c != '.' && 
               c != '(' && c != ')' && c != '[' && c != ']';
    }
    
    /**
     * Checks if a character is an operator.
     */
    private static boolean isOperator(char c) {
        return c == '*' || c == '+' || c == '?' || c == '|' || c == '.';
    }
    
    /**
     * Returns the precedence of an operator.
     */
    private static int getPrecedence(char operator) {
        switch (operator) {
            case '*':
            case '+':
            case '?':
                return 3; // highest precedence
            case '.':
                return 2; // concatenation
            case '|':
                return 1; // union - lowest precedence
            default:
                return 0;
        }
    }
}
