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
    private static long uniqueIdCounter = 0L;
    private static synchronized String uniqueId() {
        return "u" + (uniqueIdCounter++);
    }
    
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
        // '.' is a wildcard operand; do not treat it as concatenation here
        
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
            
            // Note: '.' is a wildcard operand, not an explicit concatenation operator
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
                i = inCharacterClass
                        ? handleEscapedInsideClass(regex, i, result)
                        : handleEscapedOutsideClass(regex, i, result);
                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                continue;
            }

            if (c == '[') {
                if (inCharacterClass) {
                    throw new IllegalArgumentException("Nested character classes are not supported");
                }
                ensureNonEmptyCharacterClass(regex, i);
                inCharacterClass = true;
                result.append(c);
                continue;
            }

            if (c == ']') {
                if (!inCharacterClass) {
                    throw new IllegalArgumentException("Unmatched ']'");
                }
                inCharacterClass = false;
                result.append(c);
                continue;
            }

            if (c == '-' && inCharacterClass) {
                appendDashInCharacterClass(regex, i, result);
                continue;
            }

            if (c == '.' && !inCharacterClass) {
                result.append(c);
                continue;
            }

            if (c == '{' && !inCharacterClass) {
                result.append(c);
                continue;
            }

            result.append(c);
        }

        if (escaped) {
            throw new IllegalArgumentException("Incomplete escape sequence");
        }

        if (inCharacterClass) {
            throw new IllegalArgumentException("Unclosed character class");
        }

        return result.toString();
    }

    private static void ensureNonEmptyCharacterClass(String regex, int indexOfOpenBracket) {
        if (indexOfOpenBracket + 1 < regex.length() && regex.charAt(indexOfOpenBracket + 1) == ']') {
            throw new IllegalArgumentException("Invalid regex: empty character class");
        }
    }

    private static int handleEscapedInsideClass(String regex, int i, StringBuilder result) {
        char c = regex.charAt(i);
        if (c == ']') {
            throw new IllegalArgumentException("Invalid regex");
        }
        if (c == 'x') {
            validateHasHexDigits(regex, i);
            String hexDigits = regex.substring(i + 1, i + 3);
            result.append("\\x").append(hexDigits);
            return i + 2; // Skip the hex digits
        }
        result.append("\\").append(c);
        return i;
    }

    private static int handleEscapedOutsideClass(String regex, int i, StringBuilder result) {
        char c = regex.charAt(i);
        switch (c) {
            case 's':
                result.append("[\\s]");
                break;
            case 't':
                result.append("\\t");
                break;
            case 'n':
                result.append("\\n");
                break;
            case 'r':
                result.append("\\r");
                break;
            case 'x':
                validateHasHexDigits(regex, i);
                String hexDigits = regex.substring(i + 1, i + 3);
                int hexValue = Integer.parseInt(hexDigits, 16);
                result.append((char) hexValue);
                return i + 2; // Skip digits
            case '\\':
                result.append("\\\\");
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
                result.append("\\").append(c);
                break;
            case ']':
                result.append("\\").append(c);
                break;
            default:
                result.append(c);
                break;
        }
        return i;
    }

    private static void validateHasHexDigits(String regex, int startIndexAfterBackslashOrX) {
        if (startIndexAfterBackslashOrX + 2 >= regex.length()) {
            throw new IllegalArgumentException("Invalid hex character");
        }
        String hexDigits = regex.substring(startIndexAfterBackslashOrX + 1, startIndexAfterBackslashOrX + 3);
        if (!hexDigits.matches("[0-9a-fA-F]{2}")) {
            throw new IllegalArgumentException("Invalid hex character");
        }
    }

    private static void appendDashInCharacterClass(String regex, int i, StringBuilder result) {
        if (i == 0 || i == regex.length() - 1 || regex.charAt(i - 1) == '[' || regex.charAt(i - 1) == '^') {
            result.append('-');
            return;
        }
        char prev = regex.charAt(i - 1);
        char next = i + 1 < regex.length() ? regex.charAt(i + 1) : '\0';

        if (next == ']') {
            result.append('-');
            return;
        }

        if (next <= prev) {
            if (i >= 3 && regex.substring(i - 3, i).matches("\\\\x[0-9a-fA-F]{2}") &&
                    i + 4 < regex.length() && regex.substring(i + 1, i + 4).matches("\\\\x[0-9a-fA-F]{2}")) {
                throw new IllegalArgumentException("Invalid hex range");
            }
            throw new IllegalArgumentException("Invalid character range: " + prev + "-" + next);
        }

        result.append('-');
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
        // Then, (m-n) optional occurrences appended as "&?&? ..."
        int additional = m - n;
        for (int i = 0; i < additional; i++) {
            result.append("&?");
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
        
        // For m > 1: (m-1) occurrences of "?&" followed by a final "?"
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < m - 1; i++) {
            result.append("?&");
        }
        result.append("?");
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
        boolean lastCharWasOperand = false;

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (shouldInsertConcat(lastCharWasOperand, c)) {
                popHigherOrEqualPrecedence(operators, postfix, '&');
                operators.push('&');
            }

            if (isOperand(c)) {
                i = appendOperandToken(infix, i, postfix);
                lastCharWasOperand = true;
                continue;
            }

            if (c == '[') {
                i = appendCharacterClassToken(infix, i, postfix);
                lastCharWasOperand = true;
                continue;
            }

            if (c == '(') {
                operators.push(c);
                lastCharWasOperand = false;
                continue;
            }

            if (c == ')') {
                popUntilOpenParen(operators, postfix);
                lastCharWasOperand = true;
                continue;
            }

            if (isOperator(c)) {
                validatePipeUsage(infix, i, c);
                popHigherOrEqualPrecedence(operators, postfix, c);
                operators.push(c);
                lastCharWasOperand = (c == '*' || c == '+' || c == '?');
                continue;
            }

            if (c == '{') {
                i = appendQuantifierOperator(infix, i, postfix);
                lastCharWasOperand = true;
                continue;
            }

            postfix.append(c);
            lastCharWasOperand = true;
        }

        popRemainingOperators(operators, postfix);
        return postfix.toString();
    }

    private static boolean shouldInsertConcat(boolean lastCharWasOperand, char c) {
        return lastCharWasOperand && (isOperand(c) || c == '(' || c == '[');
    }

    private static void popHigherOrEqualPrecedence(Stack<Character> operators, StringBuilder postfix, char currentOp) {
        while (!operators.isEmpty() && operators.peek() != '(' && getPrecedence(operators.peek()) >= getPrecedence(currentOp)) {
            postfix.append(operators.pop());
        }
    }

    private static int appendOperandToken(String infix, int i, StringBuilder postfix) {
        char c = infix.charAt(i);
        if (c == '\\' && i + 1 < infix.length()) {
            postfix.append(c).append(infix.charAt(i + 1));
            return i + 1;
        }
        postfix.append(c);
        return i;
    }

    private static int appendCharacterClassToken(String infix, int i, StringBuilder postfix) {
        StringBuilder charClass = new StringBuilder();
        charClass.append('[');
        i++;
        boolean inEscape = false;
        while (i < infix.length()) {
            char cc = infix.charAt(i);
            charClass.append(cc);
            if (inEscape) {
                inEscape = false;
            } else if (cc == '\\' && cc != ']') {
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
        return i;
    }

    private static void popUntilOpenParen(Stack<Character> operators, StringBuilder postfix) {
        while (!operators.isEmpty() && operators.peek() != '(') {
            postfix.append(operators.pop());
        }
        if (operators.isEmpty()) {
            throw new IllegalArgumentException("Mismatched parentheses");
        }
        operators.pop();
    }

    private static void validatePipeUsage(String infix, int i, char c) {
        if (c == '|' && (i == 0 || i == infix.length() - 1)) {
            throw new IllegalArgumentException("Invalid use of | operator");
        }
    }

    private static int appendQuantifierOperator(String infix, int i, StringBuilder postfix) {
        QuantifierInfo quantifierInfo = parseQuantifier(infix, i);
        if (quantifierInfo == null) {
            postfix.append('{');
            return i;
        }
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
                throw new IllegalStateException("Unexpected quantifier type");
        }
        postfix.append(quantifierOp.toString());
        return i + quantifierInfo.length - 1;
    }

    private static void popRemainingOperators(Stack<Character> operators, StringBuilder postfix) {
        while (!operators.isEmpty()) {
            if (operators.peek() == '(') {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            postfix.append(operators.pop());
        }
    }
    
    /**
     * Builds an NFA from postfix notation using Thompson construction.
     */
    private static NFA buildNFAFromPostfix(String postfix) {
        Stack<NFA> nfaStack = new Stack<NFA>();
        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            if (isOperand(c)) {
                i = handleOperandToken(postfix, i, nfaStack);
                continue;
            }

            if (c == '[') {
                i = handleCharacterClassToken(postfix, i, nfaStack);
                continue;
            }

            if (c == '*' || c == '+' || c == '?') {
                applyUnaryOperator(c, nfaStack);
                continue;
            }

            if (c == '|' || c == '&') {
                applyBinaryOperator(c, nfaStack);
                continue;
            }

            if (c == 'Q') {
                i = handleQuantifierOperator(postfix, i, nfaStack);
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

    private static int handleOperandToken(String postfix, int i, Stack<NFA> nfaStack) {
        char c = postfix.charAt(i);
        if (c == '\\' && i + 1 < postfix.length()) {
            char nextChar = postfix.charAt(i + 1);
            char actualChar = getEscapedCharacter(nextChar);
            nfaStack.push(createCharacterNFA(actualChar));
            return i + 1;
        }
        if (c == '.') {
            nfaStack.push(createWildcardNFA());
        } else {
            nfaStack.push(createCharacterNFA(c));
        }
        return i;
    }

    private static int handleCharacterClassToken(String postfix, int i, Stack<NFA> nfaStack) {
        StringBuilder charClass = new StringBuilder();
        i++;
        while (i < postfix.length() && postfix.charAt(i) != ']') {
            charClass.append(postfix.charAt(i));
            i++;
        }
        if (i >= postfix.length()) {
            throw new IllegalArgumentException("Unclosed character class");
        }
        nfaStack.push(createCharacterClassNFA(charClass.toString()));
        return i;
    }

    private static void applyUnaryOperator(char op, Stack<NFA> nfaStack) {
        if (nfaStack.isEmpty()) {
            throw new IllegalArgumentException("Invalid regex: " + op + " without operand");
        }
        NFA nfa = nfaStack.pop();
        switch (op) {
            case '*':
                nfaStack.push(nfa.star());
                break;
            case '+':
                nfaStack.push(nfa.plus());
                break;
            case '?':
                nfaStack.push(nfa.question());
                break;
            default:
                throw new IllegalArgumentException("Unknown unary operator: " + op);
        }
    }

    private static void applyBinaryOperator(char op, Stack<NFA> nfaStack) {
        if (nfaStack.size() < 2) {
            String message = (op == '|') ? "Invalid regex: | requires two operands" : "Invalid regex: concatenation requires two operands";
            throw new IllegalArgumentException(message);
        }
        NFA nfa2 = nfaStack.pop();
        NFA nfa1 = nfaStack.pop();
        if (op == '|') {
            nfaStack.push(nfa1.union(nfa2));
        } else {
            nfaStack.push(nfa1.concatenate(nfa2));
        }
    }

    private static int handleQuantifierOperator(String postfix, int i, Stack<NFA> nfaStack) {
        if (nfaStack.isEmpty()) {
            throw new IllegalArgumentException("Invalid regex: quantifier without operand");
        }

        StringBuilder quantifierOp = new StringBuilder();
        quantifierOp.append('Q');
        i++;
        while (i < postfix.length() && (Character.isLetterOrDigit(postfix.charAt(i)) || postfix.charAt(i) == ',')) {
            quantifierOp.append(postfix.charAt(i));
            i++;
        }
        i--; // compensate for loop increment in caller

        String op = quantifierOp.toString();
        NFA operand = nfaStack.pop();
        if (op.startsWith("QE")) {
            int n = Integer.parseInt(op.substring(2));
            nfaStack.push(createExactQuantifierNFA(operand, n));
        } else if (op.startsWith("QA")) {
            int n = Integer.parseInt(op.substring(2));
            nfaStack.push(createAtLeastQuantifierNFA(operand, n));
        } else if (op.startsWith("QM")) {
            int m = Integer.parseInt(op.substring(2));
            nfaStack.push(createAtMostQuantifierNFA(operand, m));
        } else if (op.startsWith("QR")) {
            String[] parts = op.substring(2).split(",");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            nfaStack.push(createRangeQuantifierNFA(operand, n, m));
        } else {
            throw new IllegalArgumentException("Invalid quantifier operator: " + op);
        }
        return i;
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
        String acceptState = "accept_" + uniqueId();
        acceptingStates.add(acceptState);
        
        String startState = "start_" + uniqueId();
        NFA nfa = new NFA(startState, acceptingStates);
        nfa.addTransition(startState, c, acceptState);
        
        return nfa;
    }
    
    /**
     * Creates an NFA for a character class.
     */
    private static NFA createCharacterClassNFA(String charClass) {
        Set<String> acceptingStates = new HashSet<String>();
        String acceptState = "accept_" + uniqueId();
        acceptingStates.add(acceptState);
        
        String startState = "start_" + uniqueId();
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

        if (charClass.startsWith("^")) {
            negated = true;
            i = 1;
        }

        while (i < charClass.length()) {
            char currentChar = charClass.charAt(i);

            // First, handle a hex range like \xHH-\xHH starting at current position
            if (currentChar == '\\' && tryConsumeHexRange(charClass, i, characters)) {
                i += 9; // consumes "\\xHH-\\xHH"
                continue;
            }

            if (currentChar == '\\') {
                i = handleEscapedInCharClass(charClass, i, characters);
                continue;
            }

            if (isLiteralDashPosition(charClass, i)) {
                characters.add('-');
                i++;
                continue;
            }

            if (isPotentialRange(charClass, i)) {
                i = consumeSimpleRange(charClass, i, characters);
                continue;
            }

            characters.add(currentChar);
            i++;
        }

        if (negated) {
            Set<Character> allChars = new HashSet<Character>();
            for (char c = 32; c <= 126; c++) {
                allChars.add(c);
            }
            allChars.removeAll(characters);
            return allChars;
        }

        return characters;
    }

    private static int handleEscapedInCharClass(String charClass, int i, Set<Character> characters) {
        i++;
        if (i >= charClass.length()) {
            throw new IllegalArgumentException("Incomplete escape sequence");
        }
        char escapedChar = charClass.charAt(i);
        if (escapedChar == 'x' && i + 2 < charClass.length()) {
            String hex = charClass.substring(i + 1, i + 3);
            try {
                int hexValue = Integer.parseInt(hex, 16);
                characters.add((char) hexValue);
                return i + 3;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid regex: invalid hex character \\x" + hex);
            }
        }
        if (escapedChar == '-') {
            characters.add('-');
            return i + 1;
        }
        switch (escapedChar) {
            case 's':
                addWhitespaceCharacters(characters);
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
                characters.add(escapedChar);
                break;
        }
        return i + 1;
    }

    private static void addWhitespaceCharacters(Set<Character> characters) {
        characters.add(' ');
        characters.add('\t');
        characters.add('\n');
        characters.add('\r');
    }

    private static boolean isLiteralDashPosition(String charClass, int i) {
        return charClass.charAt(i) == '-' && (i == 0 || i == charClass.length() - 1 ||
                (i > 0 && charClass.charAt(i - 1) == '[') || (i > 0 && charClass.charAt(i - 1) == '^'));
    }

    private static boolean isPotentialRange(String charClass, int i) {
        return i + 2 < charClass.length() && charClass.charAt(i + 1) == '-';
    }

    private static boolean tryConsumeHexRange(String charClass, int i, Set<Character> characters) {
        char currentChar = charClass.charAt(i);
        if (currentChar == '\\' && i + 4 < charClass.length()) {
            String startHex = charClass.substring(i + 2, i + 4);
            boolean startHexValid = startHex.matches("[0-9a-fA-F]{2}");
            boolean hasDashAndSecondHex = (i + 8 < charClass.length() && charClass.charAt(i + 4) == '-' &&
                    charClass.charAt(i + 5) == '\\' && charClass.charAt(i + 6) == 'x' &&
                    Character.toString(charClass.charAt(i + 7)).matches("[0-9a-fA-F]") &&
                    Character.toString(charClass.charAt(i + 8)).matches("[0-9a-fA-F]"));
            if (startHexValid && hasDashAndSecondHex) {
                String endHex = charClass.substring(i + 7, i + 9);
                try {
                    int startValue = Integer.parseInt(startHex, 16);
                    int endValue = Integer.parseInt(endHex, 16);
                    if (startValue > endValue) {
                        throw new IllegalArgumentException("Invalid regex: invalid hex range \\x" + startHex + "-\\x" + endHex);
                    }
                    for (int val = startValue; val <= endValue; val++) {
                        characters.add((char) val);
                    }
                    return true;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid regex: invalid hex range \\x" + startHex + "-\\x" + endHex);
                }
            }
        }
        return false;
    }

    private static int consumeSimpleRange(String charClass, int i, Set<Character> characters) {
        char start = charClass.charAt(i);
        char end = charClass.charAt(i + 2);
        if (start > end) {
            throw new IllegalArgumentException("Invalid character range: " + start + "-" + end);
        }
        for (char c = start; c <= end; c++) {
            characters.add(c);
        }
        return i + 3;
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
        String state = "empty_" + uniqueId();
        acceptingStates.add(state);
        
        NFA nfa = new NFA(state, acceptingStates);
        return nfa;
    }
    
    /**
     * Creates an NFA for wildcard (matches any character).
     */
    private static NFA createWildcardNFA() {
        Set<String> acceptingStates = new HashSet<String>();
        String acceptState = "accept_" + uniqueId();
        acceptingStates.add(acceptState);
        
        String startState = "start_" + uniqueId();
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
            return cloneNFA(operand);
        }
        
        // Build by concatenating n clones onto an initial empty-string NFA
        NFA result = createEmptyStringNFA();
        for (int i = 0; i < n; i++) {
            result = result.concatenate(cloneNFA(operand));
        }
        return result;
    }
    
    /**
     * Creates an NFA for at-least quantifier: n or more occurrences.
     */
    private static NFA createAtLeastQuantifierNFA(NFA operand, int n) {
        if (n == 0) {
            return cloneNFA(operand).star();
        }
        if (n == 1) {
            return cloneNFA(operand).plus();
        }
        
        // First, create exactly n occurrences
        NFA exactN = createExactQuantifierNFA(operand, n);
        // Then, add zero or more additional occurrences of the same operand
        NFA tail = cloneNFA(operand).star();
        return exactN.concatenate(tail);
    }
    
    /**
     * Creates an NFA for at-most quantifier: at most m occurrences.
     */
    private static NFA createAtMostQuantifierNFA(NFA operand, int m) {
        if (m == 0) {
            return createEmptyStringNFA();
        }
        if (m == 1) {
            return cloneNFA(operand).question();
        }
        
        // For small values of m, use union approach
        if (m <= 5) {
            NFA result = createEmptyStringNFA(); // 0 occurrences
            for (int i = 1; i <= m; i++) {
                NFA current = createExactQuantifierNFA(operand, i);
                result = result.union(current);
            }
            return result;
        }
        
        // For larger values, use a more efficient approach
        // Create: (operand?)^m but limit the complexity
        NFA result = createEmptyStringNFA();
        for (int i = 0; i < Math.min(m, 10); i++) { // Limit to 10 to avoid complexity
            result = result.concatenate(cloneNFA(operand).question());
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
        
        // Use concatenation approach: exactly n occurrences followed by up to (m-n) optional
        NFA exactN = createExactQuantifierNFA(operand, n);
        NFA additional = createAtMostQuantifierNFA(operand, m - n);
        return exactN.concatenate(additional);
    }

    /**
     * Creates a deep copy of an NFA with freshly generated state names to avoid collisions.
     */
    private static NFA cloneNFA(NFA original) {
        String suffix = "_" + uniqueId();
        Map<String, String> rename = new HashMap<String, String>();

        // Create new NFA and map states
        NFA copy = new NFA();
        for (String state : original.getStates()) {
            String newName = state + suffix;
            rename.put(state, newName);
            boolean isAccepting = original.getAcceptingStates().contains(state);
            copy.addState(newName, isAccepting);
        }

        // Set start state
        String originalStart = original.getStartState();
        if (originalStart != null) {
            copy.setStartState(rename.get(originalStart));
        }

        // Copy transitions
        Map<String, Map<Character, Set<String>>> transitions = original.getTransitions();
        for (Map.Entry<String, Map<Character, Set<String>>> fromEntry : transitions.entrySet()) {
            String from = rename.get(fromEntry.getKey());
            for (Map.Entry<Character, Set<String>> symEntry : fromEntry.getValue().entrySet()) {
                char symbol = symEntry.getKey();
                for (String toOrig : symEntry.getValue()) {
                    String to = rename.get(toOrig);
                    copy.addTransition(from, symbol, to);
                }
            }
        }

        // Copy epsilon transitions
        Map<String, Set<String>> eps = original.getEpsilonTransitions();
        for (Map.Entry<String, Set<String>> e : eps.entrySet()) {
            String from = rename.get(e.getKey());
            for (String toOrig : e.getValue()) {
                String to = rename.get(toOrig);
                copy.addEpsilonTransition(from, to);
            }
        }

        return copy;
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
        // Internal quantifier token should not be treated as operand in postfix
        if (c == 'Q') {
            return false;
        }
        
        // Check if it's a special character that's not an operator
        // Note: '.' is a wildcard operand, not an operator
        if ("()[]{}+?*|^$&".indexOf(c) != -1) {
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
