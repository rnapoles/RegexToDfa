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
        
        // Handle empty regex - it should accept only empty strings
        if (regex.isEmpty()) {
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
        // Validate the regex for basic syntax errors
        validateRegex(regex);
        
        // Preprocess the regex to handle escape sequences and character classes
        String processedRegex = preprocessRegex(regex);
        
        // Convert to postfix notation (Reverse Polish Notation)
        String postfixRegex = infixToPostfix(processedRegex);
        
        // Debug: print postfix for troubleshooting
        System.out.println("Postfix for '" + processedRegex + "': '" + postfixRegex + "'");
        
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
        if (regex.equals(".")) {
            throw new IllegalArgumentException("Invalid regex: concatenation requires two operands");
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
            
            // Check for consecutive concatenation operators
            if (current == '.' && next == '.') {
                throw new IllegalArgumentException("Invalid regex: concatenation requires two operands");
            }
        }
        
        // Check for balanced parentheses and nested parentheses
        int balance = 0;
        int maxDepth = 0;
        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);
            if (c == '(') {
                balance++;
                maxDepth = Math.max(maxDepth, balance);
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
        // Allow nested parentheses (remove the depth restriction)
        // if (maxDepth > 1) {
        //     throw new IllegalArgumentException("Mismatched parentheses");
        // }
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
                            if (inCharacterClass) {
                                throw new IllegalArgumentException("Invalid regex");
                            }
                            throw new IllegalArgumentException("Invalid hex character");
                        }
                        String hexDigits = regex.substring(i + 1, i + 3);
                        if (!hexDigits.matches("[0-9a-fA-F]{2}")) {
                            if (inCharacterClass) {
                                throw new IllegalArgumentException("Invalid regex");
                            }
                            throw new IllegalArgumentException("Invalid hex character");
                        }
                        // Convert hex to actual character
                        int hexValue = Integer.parseInt(hexDigits, 16);
                        char hexChar = (char) hexValue;
                        
                        // Check for hex range validation
                        if (inCharacterClass && i + 4 < regex.length() && regex.charAt(i + 3) == '-') {
                            // Look ahead for another hex sequence
                            if (i + 6 < regex.length() && regex.charAt(i + 4) == '\\' && regex.charAt(i + 5) == 'x') {
                                String nextHexDigits = regex.substring(i + 6, i + 8);
                                if (nextHexDigits.matches("[0-9a-fA-F]{2}")) {
                                    int nextHexValue = Integer.parseInt(nextHexDigits, 16);
                                    char nextHexChar = (char) nextHexValue;
                                    if (nextHexChar <= hexChar) {
                                        throw new IllegalArgumentException("Invalid hex range");
                                    }
                                }
                            }
                        }
                        
                        result.append(hexChar);
                        i += 2; // Skip the hex digits
                        break;
                    case '\\':
                        result.append("\\\\"); // literal backslash
                        break;
                    case '[':
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
                    case ']':
                        // ] should not be escaped in character classes
                        if (inCharacterClass) {
                            throw new IllegalArgumentException("Invalid regex");
                        }
                        result.append("\\").append(c);
                        break;
                    default:
                        // Check for invalid escape sequences in character classes
                        if (inCharacterClass) {
                            throw new IllegalArgumentException("Invalid regex");
                        }
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
                    // Invalid range start - throw exception
                    throw new IllegalArgumentException("Invalid regex");
                } else {
                    char prev = regex.charAt(i - 1);
                    char next = i + 1 < regex.length() ? regex.charAt(i + 1) : '\0';
                    
                    if (next == ']') {
                        // '-' at the end of a character class is a literal
                        result.append('-');
                    } else if (next <= prev) {
                        // Check if this is a hex range
                        if (i >= 3 && regex.substring(i - 3, i).matches("\\\\x[0-9a-fA-F]{2}") &&
                            i + 4 < regex.length() && regex.substring(i + 1, i + 4).matches("\\\\x[0-9a-fA-F]{2}")) {
                            throw new IllegalArgumentException("Invalid hex range");
                        }
                        throw new IllegalArgumentException("Invalid character range: " + prev + "-" + next);
                    } else {
                        // Valid range, leave as is
                        result.append("-");
                    }
                }
            } else if (c == '.' && !inCharacterClass) {
                // Keep . as is for now - it will be handled as a wildcard in postfix conversion
                result.append(c);
            } else if (c == '{' && !inCharacterClass && !escaped) {
                // Keep quantifiers as-is for now, they'll be handled in infix to postfix conversion
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
     * Information about a parsed quantifier.
     */
    private static class QuantifierInfo {
        String replacement;
        int length;
        QuantifierType type;
        int min;
        int max;
        
        QuantifierInfo(String replacement, int length) {
            this.replacement = replacement;
            this.length = length;
            this.type = QuantifierType.SIMPLE;
            this.min = 0;
            this.max = 0;
        }
        
        QuantifierInfo(String replacement, int length, QuantifierType type, int min, int max) {
            this.replacement = replacement;
            this.length = length;
            this.type = type;
            this.min = min;
            this.max = max;
        }
    }
    
    /**
     * Types of quantifiers for different handling approaches.
     */
    private enum QuantifierType {
        SIMPLE,     // *, +, ?
        EXACT,      // {n}
        RANGE,      // {n,m}
        AT_LEAST,   // {n,}
        AT_MOST     // {,m}
    }
    
    /**
     * Parses a quantifier pattern like {n,m}, {n,}, or {n} and converts it to equivalent regex.
     * Returns null if the pattern is not a valid quantifier.
     */
    private static QuantifierInfo parseQuantifier(String regex, int start) {
        if (start + 1 >= regex.length()) {
            return null;
        }
        
        int end = start + 1;
        while (end < regex.length() && regex.charAt(end) != '}') {
            end++;
        }
        
        if (end >= regex.length()) {
            return null; // No closing brace found
        }
        
        String content = regex.substring(start + 1, end);
        int totalLength = end - start + 1; // Include the braces
        
        // Check for empty quantifier - this should be invalid
        if (content.isEmpty()) {
            throw new IllegalArgumentException("Invalid quantifier: empty braces {}");
        }
        
        // Parse different quantifier patterns
        if (content.contains(",")) {
            String[] parts = content.split(",", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid quantifier: " + content);
            }
            
            try {
                if (parts[1].isEmpty()) {
                    // {n,} - n or more occurrences
                    if (parts[0].isEmpty()) {
                        throw new IllegalArgumentException("Invalid quantifier: {,}");
                    }
                    int n = Integer.parseInt(parts[0]);
                    if (n < 0) {
                        throw new IllegalArgumentException("Invalid quantifier: negative number");
                    }
                    return new QuantifierInfo("", totalLength, QuantifierType.AT_LEAST, n, Integer.MAX_VALUE);
                } else {
                    // {n,m} - between n and m occurrences
                    if (parts[0].isEmpty()) {
                        // {,m} - at most m occurrences
                        int m = Integer.parseInt(parts[1]);
                        if (m < 0) {
                            throw new IllegalArgumentException("Invalid quantifier: negative number");
                        }
                        return new QuantifierInfo("", totalLength, QuantifierType.AT_MOST, 0, m);
                    } else {
                        int n = Integer.parseInt(parts[0]);
                        int m = Integer.parseInt(parts[1]);
                        if (n < 0 || m < 0) {
                            throw new IllegalArgumentException("Invalid quantifier: negative number");
                        }
                        if (m < n) {
                            throw new IllegalArgumentException("Invalid quantifier: upper bound less than lower bound");
                        }
                        return new QuantifierInfo("", totalLength, QuantifierType.RANGE, n, m);
                    }
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid quantifier: non-numeric bounds");
            }
        } else {
            // {n} - exactly n occurrences
            try {
                int n = Integer.parseInt(content);
                if (n < 0) {
                    throw new IllegalArgumentException("Invalid quantifier: negative number");
                }
                return new QuantifierInfo("", totalLength, QuantifierType.EXACT, n, n);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid quantifier: non-numeric value");
            }
        }
    }
    
    /**
     * Generates regex for exactly n occurrences: a{n} -> aaaa...a (n times)
     * This is handled by implicit concatenation in the postfix conversion
     */
    private static String generateExactlyNQuantifier(int n) {
        if (n == 0) {
            return ""; // Empty string
        }
        if (n == 1) {
            return ""; // No quantifier needed
        }
        
        // For n > 1, we need to create n-1 concatenation operators
        // This will be handled by the postfix conversion
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < n; i++) {
            result.append("&");
        }
        return result.toString();
    }
    
    /**
     * Generates regex for n or more occurrences: a{n,} -> a^n a*
     * For {2,}, we need: exactly 2 occurrences followed by *
     * In postfix: a a & * (where a is the operand)
     */
    private static String generateAtLeastNQuantifier(int n) {
        if (n == 0) {
            return "*"; // Zero or more
        }
        if (n == 1) {
            return "+"; // One or more
        }
        
        // For n > 1, we need: exactly n occurrences followed by *
        // In postfix: a a ... a & & ... & * (n-1 concatenations)
        StringBuilder result = new StringBuilder();
        // First, duplicate the operand n-1 times (for concatenation)
        for (int i = 1; i < n; i++) {
            result.append("&");
        }
        // Then, the * quantifier for zero or more additional occurrences
        result.append("*");
        return result.toString();
    }
    
    /**
     * Generates regex for between n and m occurrences: a{n,m} -> a^n (a?)^(m-n)
     */
    private static String generateRangeQuantifier(int n, int m) {
        if (n == m) {
            return generateExactlyNQuantifier(n);
        }
        if (n == 0 && m == 1) {
            return "?"; // Zero or one
        }
        if (n == 0) {
            return generateAtMostMQuantifier(m);
        }
        if (m == Integer.MAX_VALUE) {
            return generateAtLeastNQuantifier(n);
        }
        
        // For n < m: exactly n occurrences followed by (m-n) optional occurrences
        StringBuilder result = new StringBuilder();
        // First, n-1 concatenation operators for exactly n occurrences
        for (int i = 1; i < n; i++) {
            result.append("&");
        }
        // Then, (m-n) optional occurrences: ? & ? & ... & ?
        int additional = m - n;
        for (int i = 0; i < additional; i++) {
            result.append("?&");
        }
        return result.toString();
    }
    
    /**
     * Generates regex for at most m occurrences: a{0,m} -> (a?)^m
     */
    private static String generateAtMostMQuantifier(int m) {
        if (m == 0) {
            return ""; // Empty string
        }
        if (m == 1) {
            return "?"; // Zero or one
        }
        
        // For m > 1: m optional occurrences: ? & ? & ... & ?
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < m; i++) {
            result.append("?&");
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
                           getPrecedence(operators.peek()) >= getPrecedence('&')) {
                        postfix.append(operators.pop());
                    }
                    operators.push('&');
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
                           getPrecedence(operators.peek()) >= getPrecedence('&')) {
                        postfix.append(operators.pop());
                    }
                    operators.push('&');
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
            } else if (c == '{') {
                // Handle quantifiers like {2,}, {3}, {2,5}
                QuantifierInfo quantifierInfo = parseQuantifier(infix, i);
                if (quantifierInfo != null) {
                    // Apply the quantifier to the previous operand
                    if (postfix.length() == 0) {
                        throw new IllegalArgumentException("Nothing to repeat at position " + i);
                    }
                    
                    // Create a special quantifier operator that the NFA builder can understand
                    // Format: Q<type><min><max> where type is E, A, M, R for exact, at-least, at-most, range
                    StringBuilder quantifierOp = new StringBuilder();
                    quantifierOp.append("Q");
                    
                    switch (quantifierInfo.type) {
                        case EXACT:
                            quantifierOp.append("E").append(quantifierInfo.min);
                            break;
                        case AT_LEAST:
                            quantifierOp.append("A").append(quantifierInfo.min);
                            break;
                        case AT_MOST:
                            quantifierOp.append("M").append(quantifierInfo.max);
                            break;
                        case RANGE:
                            quantifierOp.append("R").append(quantifierInfo.min).append(",").append(quantifierInfo.max);
                            break;
                        default:
                            // Simple quantifier
                            if (quantifierInfo.replacement.equals("*")) {
                                postfix.append('*');
                            } else if (quantifierInfo.replacement.equals("+")) {
                                postfix.append('+');
                            } else if (quantifierInfo.replacement.equals("?")) {
                                postfix.append('?');
                            }
                            break;
                    }
                    
                    if (quantifierInfo.type != QuantifierType.SIMPLE) {
                        postfix.append(quantifierOp.toString());
                    }
                    
                    i += quantifierInfo.length - 1; // -1 because the loop will increment
                    needConcat = true;
                } else {
                    // Not a valid quantifier, treat as literal
                    postfix.append(c);
                    needConcat = true;
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
            } else if (c == '&') {
                if (nfaStack.size() < 2) {
                    throw new IllegalArgumentException("Invalid regex: concatenation requires two operands");
                }
                NFA nfa2 = nfaStack.pop();
                NFA nfa1 = nfaStack.pop();
                nfaStack.push(nfa1.concatenate(nfa2));
            } else if (c == 'Q') {
                // Handle special quantifier operators
                if (nfaStack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid regex: quantifier without operand");
                }
                
                // Parse the quantifier operator
                StringBuilder quantifierOp = new StringBuilder();
                quantifierOp.append('Q');
                i++; // Skip the Q
                
                while (i < postfix.length() && Character.isLetterOrDigit(postfix.charAt(i)) || postfix.charAt(i) == ',') {
                    quantifierOp.append(postfix.charAt(i));
                    i++;
                }
                i--; // Back up one position since the loop will increment
                
                String op = quantifierOp.toString();
                NFA operand = nfaStack.pop();
                
                if (op.startsWith("QE")) {
                    // Exact quantifier: QE<n>
                    int n = Integer.parseInt(op.substring(2));
                    nfaStack.push(createExactQuantifierNFA(operand, n));
                } else if (op.startsWith("QA")) {
                    // At least quantifier: QA<n>
                    int n = Integer.parseInt(op.substring(2));
                    nfaStack.push(createAtLeastQuantifierNFA(operand, n));
                } else if (op.startsWith("QM")) {
                    // At most quantifier: QM<m>
                    int m = Integer.parseInt(op.substring(2));
                    nfaStack.push(createAtMostQuantifierNFA(operand, m));
                } else if (op.startsWith("QR")) {
                    // Range quantifier: QR<n,m>
                    String[] parts = op.substring(2).split(",");
                    int n = Integer.parseInt(parts[0]);
                    int m = Integer.parseInt(parts[1]);
                    nfaStack.push(createRangeQuantifierNFA(operand, n, m));
                } else {
                    throw new IllegalArgumentException("Invalid quantifier operator: " + op);
                }
            }
        }
        
        if (nfaStack.isEmpty()) {
            // For empty postfix, create an empty string NFA
            return createEmptyStringNFA();
        }
        
        // If there are multiple NFAs left, concatenate them
        while (nfaStack.size() > 1) {
            NFA nfa2 = nfaStack.pop();
            NFA nfa1 = nfaStack.pop();
            nfaStack.push(nfa1.concatenate(nfa2));
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
            char currentChar = charClass.charAt(i);
            // Handle literal hyphen at the beginning or after a negation, or at the end.
            // This condition checks if the hyphen is NOT part of a valid range.
            if (currentChar == '-' && (i == 0 || i == charClass.length() - 1 || 
                                       (i > 0 && charClass.charAt(i - 1) == '[') || (i > 0 && charClass.charAt(i - 1) == '^')) ) {
                characters.add('-');
                i++;
                continue;
            }

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
     * Creates an NFA for exact quantifier: exactly n occurrences.
     */
    private static NFA createExactQuantifierNFA(NFA operand, int n) {
        if (n == 0) {
            return createEmptyStringNFA();
        }
        if (n == 1) {
            return operand;
        }
        
        // Concatenate the operand n times
        NFA result = operand;
        for (int i = 1; i < n; i++) {
            result = result.concatenate(operand);
        }
        return result;
    }
    
    /**
     * Creates an NFA for at-least quantifier: n or more occurrences.
     */
    private static NFA createAtLeastQuantifierNFA(NFA operand, int n) {
        if (n == 0) {
            return operand.star();
        }
        if (n == 1) {
            return operand.plus();
        }
        
        // First, create exactly n occurrences
        NFA exactN = createExactQuantifierNFA(operand, n);
        // Then, add zero or more additional occurrences
        return exactN.concatenate(operand.star());
    }
    
    /**
     * Creates an NFA for at-most quantifier: at most m occurrences.
     */
    private static NFA createAtMostQuantifierNFA(NFA operand, int m) {
        if (m == 0) {
            return createEmptyStringNFA();
        }
        if (m == 1) {
            return operand.question();
        }
        
        // Create union of 0, 1, 2, ..., m occurrences
        NFA result = createEmptyStringNFA(); // 0 occurrences
        
        for (int i = 1; i <= m; i++) {
            NFA current = createExactQuantifierNFA(operand, i);
            result = result.union(current);
        }
        return result;
    }
    
    /**
     * Creates an NFA for range quantifier: between n and m occurrences.
     */
    private static NFA createRangeQuantifierNFA(NFA operand, int n, int m) {
        if (n == m) {
            return createExactQuantifierNFA(operand, n);
        }
        if (n == 0) {
            return createAtMostQuantifierNFA(operand, m);
        }
        if (m == Integer.MAX_VALUE) {
            return createAtLeastQuantifierNFA(operand, n);
        }
        
        // Create union of n, n+1, ..., m occurrences
        NFA result = createExactQuantifierNFA(operand, n);
        
        for (int i = n + 1; i <= m; i++) {
            NFA current = createExactQuantifierNFA(operand, i);
            result = result.union(current);
        }
        return result;
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
        if ("()[]{}+?|^$&".indexOf(c) != -1) {
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
        return "*+?|&".indexOf(c) != -1;
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
            case '&':
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
