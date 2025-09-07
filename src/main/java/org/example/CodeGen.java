package org.example;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CodeGen
{

    /**
     * Generates a Java lexer class from this NFA with custom token types.
     *
     * @param nfa
     * @param className the name of the generated lexer class
     * @param packageName the package name for the generated class
     * @param tokenTypes map of accepting states to token type names
     * @return the generated Java source code
     * @throws IOException if template processing fails
     */
    public static String generateLexer(NFA nfa, String className, String packageName,
                                       Map<String, String> tokenTypes) throws IOException {
        LexerGenerator generator = new LexerGenerator();
        return generator.generateLexerFromNFA(nfa, className, packageName, tokenTypes);
    }

    /**
     * Generates a Java lexer class from this NFA.
     *
     * @param nfa
     * @param className the name of the generated lexer class
     * @param packageName the package name for the generated class
     * @return the generated Java source code
     * @throws IOException if template processing fails
     */
    public static String generateLexer(NFA nfa, String className, String packageName) throws IOException {
        LexerGenerator generator = new LexerGenerator();
        return generator.generateLexerFromNFA(nfa, className, packageName);
    }

    /**
     * Generates a Java lexer class from this DFA.
     *
     * @param dfa
     * @param className the name of the generated lexer class
     * @param packageName the package name for the generated class
     * @return the generated Java source code
     * @throws IOException if template processing fails
     */
    public static String generateLexer(DFA dfa, String className, String packageName) throws IOException {
        LexerGenerator generator = new LexerGenerator();
        return generator.generateLexerFromDFA(dfa, className, packageName);
    }

    /**
     * Generates a Java lexer class from this DFA with custom token types.
     *
     * @param dfa
     * @param className the name of the generated lexer class
     * @param packageName the package name for the generated class
     * @param tokenTypes map of accepting states to token type names
     * @return the generated Java source code
     * @throws IOException if template processing fails
     */
    public static String generateLexer(DFA dfa, String className, String packageName,
                                       Map<String, String> tokenTypes) throws IOException {
        LexerGenerator generator = new LexerGenerator();
        return generator.generateLexerFromDFA(dfa, className, packageName, tokenTypes);
    }
}
