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
            throw new IllegalArgumentException("Invalid regex: empty expression");
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
        // Validate the regex for basic syntax errors
        validateRegex(regex);
        
        // Preprocess the regex to handle escape sequences and character classes
        String processedRegex = preprocessRegex(regex);
        
        // Convert to postfix notation (Reverse Polish Notation)
        String postfixRegex = infixToPostfix(processedRegex);
        
        // Debug: print postfix for troubleshooting
        // System.out.println("Postfix for '" + processedRegex + "': '" + postfixRegex + "'");
        
        // Build NFA from postfix notation
        return buildNFAFromPostfix(postfixRegex);
    }
    
    /**
     * Validates the regex for basic syntax errors.
     * @param regex the regex to validate
     * @throws IllegalArgumentException if the regex is invalid
     */
    private static void validateRegex(String regex) {
        // Check for operators without operands
        if (regex.startsWith("*") || regex.startsWith("+") || regex.startsWith("?")) {
            throw new IllegalArgumentException("Invalid regex: " + regex.charAt(0) + " without operand");
        }
        if (regex.startsWith("|")) {
            throw new IllegalArgumentException("Invalid regex: | requires two operands");
        }
        
        // Check for operators at the end (except for escaped ones)
        char lastChar = regex.charAt(regex.length() - 1);
        if (lastChar == '|' && (regex.length() < 2 || regex.charAt(regex.length() - 2) != '\\')) {
            throw new IllegalArgumentException("Invalid regex: | requires two operands");
        }
        if (lastChar == '\\' && (regex.length() < 2 || regex.charAt(regex.length() - 2) != '\\')) {
            throw new IllegalArgumentException("Incomplete escape sequence");
        }
        
        // Check for invalid operator sequences
        for (int i = 0; i < regex.length() - 1; i++) {
            char current = regex.charAt(i);
            char next = regex.charAt(i + 1);
            
            // Check for \ at end of string is handled in preprocessRegex
            
            // Check for invalid operator sequences like **, ++, ??, etc.
            if ((current == '*' || current == '+' || current == '?') && 
                (next == '*' || next == '+' || next == '?')) {
                throw new IllegalArgumentException("Invalid regex: " + current + " without operand");
            }
            
            // Check for | at start or end, or consecutive |
            if (current == '|' && (i == 0 || next == '|' || next == ')')) {
                throw new IllegalArgumentException("Invalid regex: | requires two operands");
            }
        }
        
        // Check for balanced parentheses
        int balance = 0;
        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
                if (balance < 0) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
            }
        }
        if (balance != 0) {
            throw new IllegalArgumentException("Mismatched parentheses");
        }
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
                    case 'x':
                        // Handle hex escape sequences (\xHH)
                        if (i + 2 >= regex.length()) {
                            throw new IllegalArgumentException("Invalid hex character");
                        }
                        String hexDigits = regex.substring(i + 1, i + 3);
                        if (!hexDigits.matches("[0-9a-fA-F]{2}")) {
                            throw new IllegalArgumentException("Invalid regex: invalid hex character " + hexDigits);
                        }
                        // Convert hex to actual character
                        int hexValue = Integer.parseInt(hexDigits, 16);
                        result.append((char) hexValue);
                        i += 2; // Skip the hex digits
                        break;
                    case '\\':
                        result.append("\\\\"); // literal backslash
                        break;
                    case '[':
                    case ']':
                    case '(':
                    case ')':
                    case '*':
                    case '+':
                    case '?':
                    case '|':
                    case '.':
                    case '^':
                    case '$':
                        // Escape special regex characters
                        result.append("\\").append(c);
                        break;
                    default:
                        // Unrecognized escape sequence, treat as literal
                        result.append(c);
                        break;
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '[') {
                if (inCharacterClass) {
                    throw new IllegalArgumentException("Nested character classes are not supported");
                }
                inCharacterClass = true;
                result.append(c);
                
                // Check for empty character class
                if (i + 1 < regex.length() && regex.charAt(i + 1) == ']') {
                    throw new IllegalArgumentException("Invalid regex: empty character class");
                }
            } else if (c == ']') {
                if (!inCharacterClass) {
                    throw new IllegalArgumentException("Unmatched ']'");
                }
                inCharacterClass = false;
                result.append(c);
            } else if (c == '-' && inCharacterClass) {
                // Handle character ranges in character class
                if (i == 0 || i == regex.length() - 1 || regex.charAt(i - 1) == '[' || regex.charAt(i - 1) == '^') {
                    // Treat as literal -
                    result.append("\\-");
                } else {
                    char prev = regex.charAt(i - 1);
                    char next = i + 1 < regex.length() ? regex.charAt(i + 1) : '\0';
                    
                    if (next == ']') {
                        // Treat as literal -
                        result.append("\\-");
                    } else if (next <= prev) {
                        throw new IllegalArgumentException("Invalid character range: " + prev + "-" + next);
                    } else {
                        // Valid range, leave as is
                        result.append("-");
                    }
                }
            } else if (c == '.' && !inCharacterClass) {
                // Keep . as is for now - it will be handled as a wildcard in postfix conversion
                result.append(c);
            } else {
                result.append(c);
            }
        }
        
        if (escaped) {
            throw new IllegalArgumentException("Incomplete escape sequence");
        }
        
        if (inCharacterClass) {
            throw new IllegalArgumentException("Unclosed character class");
        }
        
        return result.toString();
    }
    
    /**
     * Converts infix notation to postfix notation (Reverse Polish Notation).
     * Handles operator precedence and implicit concatenation.
     * 
     * @param infix the infix regex expression
     * @return the postfix notation of the regex
     * @throws IllegalArgumentException if the regex has invalid syntax
     */
    private static String infixToPostfix(String infix) {
        if (infix == null || infix.isEmpty()) {
            return "";
        }
        
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operators = new Stack<>();
        boolean needConcat = false;
        
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            
            if (isOperand(c) || c == '[') {
                // Handle operands and character classes
                if (needConcat) {
                    // Add implicit concatenation operator
                    while (!operators.isEmpty() && 
                           getPrecedence(operators.peek()) >= getPrecedence('.')) {
                        postfix.append(operators.pop());
                    }
                    operators.push('.');
                }
                
                if (c == '\\' && i + 1 < infix.length()) {
                    // Handle escape sequences
                    postfix.append(c);
                    postfix.append(infix.charAt(i + 1));
                    i++; // Skip the next character
                    needConcat = true;
                } else if (c == '[') {
                    // Handle character class
                    StringBuilder charClass = new StringBuilder();
                    charClass.append('[');
                    i++; // Skip '['
                    
                    boolean inEscape = false;
                    while (i < infix.length()) {
                        char cc = infix.charAt(i);
                        charClass.append(cc);
                        
                        if (inEscape) {
                            inEscape = false;
                        } else if (cc == '\\') {
                            inEscape = true;
                        } else if (cc == ']') {
                            break;
                        }
                        i++;
                    }
                    
                    if (i >= infix.length()) {
                        throw new IllegalArgumentException("Unclosed character class");
                    }
                    
                    postfix.append(charClass.toString());
                } else {
                    postfix.append(c);
                }
                needConcat = true;
            } else if (c == '(') {
                if (needConcat) {
                    // Add implicit concatenation operator before opening parenthesis
                    while (!operators.isEmpty() && 
                           getPrecedence(operators.peek()) >= getPrecedence('.')) {
                        postfix.append(operators.pop());
                    }
                    operators.push('.');
                }
                operators.push(c);
                needConcat = false;
            } else if (c == ')') {
                // Pop until matching '(' is found
                while (!operators.isEmpty() && operators.peek() != '(') {
                    postfix.append(operators.pop());
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                operators.pop(); // Remove '('
                needConcat = true;
            } else if (isOperator(c)) {
                if (c == '|' && (i == 0 || i == infix.length() - 1)) {
                    throw new IllegalArgumentException("Invalid use of | operator");
                }
                
                // Handle unary operators (they don't need an operand before them)
                if (c == '*' || c == '+' || c == '?') {
                    if (i == 0) {
                        throw new IllegalArgumentException("Nothing to repeat at position " + i);
                    }
                    char prevChar = infix.charAt(i - 1);
                    if (isOperator(prevChar) && prevChar != ')') {
                        throw new IllegalArgumentException("Nothing to repeat at position " + i);
                    }
                }
                
                // Handle binary operators
                while (!operators.isEmpty() && 
                       operators.peek() != '(' &&
                       getPrecedence(operators.peek()) >= getPrecedence(c)) {
                    postfix.append(operators.pop());
                }
                operators.push(c);
                
                // For unary postfix operators (*, +, ?), we still need concatenation after them
                if (c == '*' || c == '+' || c == '?') {
                    needConcat = true;
                } else {
                    needConcat = false;
                }
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
                // Handle escape sequences
                if (c == '\\' && i + 1 < postfix.length()) {
                    char nextChar = postfix.charAt(i + 1);
                    char actualChar = getEscapedCharacter(nextChar);
                    nfaStack.push(createCharacterNFA(actualChar));
                    i++; // Skip the next character as it's part of the escape sequence
                } else if (c == '.') {
                    // Create NFA for wildcard (any character)
                    nfaStack.push(createWildcardNFA());
                } else {
                    // Create NFA for single character
                    nfaStack.push(createCharacterNFA(c));
                }
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
            // For empty postfix, create an empty string NFA
            return createEmptyStringNFA();
        }
        
        if (nfaStack.size() > 1) {
            throw new IllegalArgumentException("Invalid regex: too many operands");
        }
        
        return nfaStack.pop();
    }
    
    /**
     * Converts an escape sequence character to its actual character value.
     */
    private static char getEscapedCharacter(char c) {
        switch (c) {
            case 't':
                return '\t';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case '\\':
                return '\\';
            case '[':
            case ']':
            case '(':
            case ')':
            case '*':
            case '+':
            case '?':
            case '|':
            case '.':
            case '^':
            case '$':
                return c; // Return the literal character
            default:
                return c; // For unrecognized escape sequences, return the character as-is
        }
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
                            throw new IllegalArgumentException("Invalid regex: invalid hex range \\x" + startHex + "-\\x" + endHex);
                        }
                        
                        for (int val = startValue; val <= endValue; val++) {
                            characters.add((char) val);
                        }
                        i += 10; // Skip \x20-\x7E
                        continue;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid regex: invalid hex range \\x" + startHex + "-\\x" + endHex);
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
                        throw new IllegalArgumentException("Incomplete escape sequence");
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
                            throw new IllegalArgumentException("Invalid regex: invalid hex character \\x" + hex);
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
     * Creates an NFA that accepts only the empty string.
     */
     private static NFA createEmptyStringNFA() {
        Set<String> acceptingStates = new HashSet<String>();
        String state = "empty_" + System.nanoTime();
        acceptingStates.add(state);
        
        NFA nfa = new NFA(state, acceptingStates);
        return nfa;
    }
    
    /**
     * Creates an NFA for wildcard (matches any character).
     */
    private static NFA createWildcardNFA() {
        Set<String> acceptingStates = new HashSet<String>();
        String acceptState = "accept_" + System.nanoTime();
        acceptingStates.add(acceptState);
        
        String startState = "start_" + System.nanoTime();
        NFA nfa = new NFA(startState, acceptingStates);
        
        // Add transitions for all printable ASCII characters
        for (char c = 32; c <= 126; c++) {
            nfa.addTransition(startState, c, acceptState);
        }
        // Add common whitespace characters
        nfa.addTransition(startState, '\t', acceptState);
        nfa.addTransition(startState, '\n', acceptState);
        nfa.addTransition(startState, '\r', acceptState);
        
        return nfa;
    }

    /**
     * Returns true if the character is an operand (not an operator).
     * In regex, operands are any characters that are not operators or special characters.
     * 
     * @param c the character to check
     * @return true if the character is an operand
     */
    private static boolean isOperand(char c) {
        // Special case: if it's a backslash, it's part of an escape sequence
        if (c == '\\') {
            return true;
        }
        
        // Check if it's a special character that's not an operator
        // Note: '.' is a wildcard operand, not an operator
        if ("()[]{}*+?|^$".indexOf(c) != -1) {
            return false;
        }
        
        // All other characters are operands
        return true;
    }
    
    /**
     * Returns true if the character is an operator in regex.
     * 
     * @param c the character to check
     * @return true if the character is an operator
     */
    private static boolean isOperator(char c) {
        return "*+?|.".indexOf(c) != -1;
    }
    
    /**
     * Returns the precedence of an operator.
     * Higher number means higher precedence.
     * 
     * @param op the operator character
     * @return the precedence value
     * @throws IllegalArgumentException if the character is not a valid operator
     */
    private static int getPrecedence(char op) {
        switch (op) {
            case '|':
                return 1;  // Lowest precedence
            case '.':
                return 2;  // Concatenation (implicit operator)
            case '?':
            case '*':
            case '+':
                return 3;  // Quantifiers (highest precedence)
            case '^':
            case '$':
                return 4;  // Anchors (highest precedence)
            case '(':
            case ')':
                return 0;  // Parentheses have lowest precedence for stack operations
            default:
                throw new IllegalArgumentException("Not an operator: " + op);
        }
    }
}
