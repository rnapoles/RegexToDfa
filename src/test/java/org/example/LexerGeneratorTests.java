package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

/**
 * Tests for the LexerGenerator class.
 */
public class LexerGeneratorTests {
    
    private LexerGenerator generator;
    
    @BeforeEach
    public void setUp() {
        generator = new LexerGenerator();
    }
    
    @Test
    public void testGenerateLexerFromDFA() throws IOException {
        // Create a simple DFA that recognizes "ab"
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", false);
        dfa.addState("q2", true);
        
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        
        dfa = dfa.setStartState("q0");
        
        String lexerCode = generator.generateLexerFromDFA(dfa, "TestLexer", "com.example");
        
        assertNotNull(lexerCode);
        assertTrue(lexerCode.contains("package com.example"));
        assertTrue(lexerCode.contains("class TestLexer"));
        assertTrue(lexerCode.contains("TOKEN_Q2"));
        assertTrue(lexerCode.contains("public Token nextToken()"));
    }
    
    @Test
    public void testGenerateLexerFromDFAWithCustomTokenTypes() throws IOException {
        // Create a DFA that recognizes identifiers
        DFA dfa = new DFA();
        dfa.addState("start", false);
        dfa.addState("identifier", true);
        
        dfa.addTransition("start", 'a', "identifier");
        dfa.addTransition("start", 'b', "identifier");
        dfa.addTransition("identifier", 'a', "identifier");
        dfa.addTransition("identifier", 'b', "identifier");
        
        dfa = dfa.setStartState("start");
        
        Map<String, String> tokenTypes = new HashMap<>();
        tokenTypes.put("identifier", "IDENTIFIER");
        
        String lexerCode = generator.generateLexerFromDFA(dfa, "IdentifierLexer", "com.example", tokenTypes);
        
        assertNotNull(lexerCode);
        assertTrue(lexerCode.contains("IDENTIFIER"));
        assertTrue(lexerCode.contains("public static final String IDENTIFIER"));
    }
    
    @Test
    public void testGenerateLexerFromNFA() throws IOException {
        // Create a simple NFA that recognizes "a*"
        NFA nfa = new NFA();
        nfa.addState("q0", false);
        nfa.addState("q1", true);
        
        nfa.addTransition("q0", 'a', "q1");
        nfa.addEpsilonTransition("q1", "q0");
        
        nfa.setStartState("q0");
        
        String lexerCode = generator.generateLexerFromNFA(nfa, "StarLexer", "com.example");
        
        assertNotNull(lexerCode);
        assertTrue(lexerCode.contains("package com.example"));
        assertTrue(lexerCode.contains("class StarLexer"));
        assertTrue(lexerCode.contains("TOKEN_Q1"));
        assertTrue(lexerCode.contains("getEpsilonClosure"));
    }
    
    @Test
    public void testGenerateLexerFromNFAWithCustomTokenTypes() throws IOException {
        // Create an NFA that recognizes numbers
        NFA nfa = new NFA();
        nfa.addState("start", false);
        nfa.addState("digit", true);
        
        nfa.addTransition("start", '0', "digit");
        nfa.addTransition("start", '1', "digit");
        nfa.addTransition("digit", '0', "digit");
        nfa.addTransition("digit", '1', "digit");
        
        nfa.setStartState("start");
        
        Map<String, String> tokenTypes = new HashMap<>();
        tokenTypes.put("digit", "NUMBER");
        
        String lexerCode = generator.generateLexerFromNFA(nfa, "NumberLexer", "com.example", tokenTypes);
        
        assertNotNull(lexerCode);
        assertTrue(lexerCode.contains("NUMBER"));
        assertTrue(lexerCode.contains("public static final String NUMBER"));
    }
    
    @Test
    public void testDFAGenerateLexerMethod() throws IOException {
        // Create a DFA that recognizes "hello"
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", false);
        dfa.addState("q2", false);
        dfa.addState("q3", false);
        dfa.addState("q4", false);
        dfa.addState("q5", true);
        
        dfa.addTransition("q0", 'h', "q1");
        dfa.addTransition("q1", 'e', "q2");
        dfa.addTransition("q2", 'l', "q3");
        dfa.addTransition("q3", 'l', "q4");
        dfa.addTransition("q4", 'o', "q5");
        
        dfa = dfa.setStartState("q0");
        
        String lexerCode = CodeGen.generateLexer(dfa, "HelloLexer", "com.example");
        
        assertNotNull(lexerCode);
        assertTrue(lexerCode.contains("class HelloLexer"));
        assertTrue(lexerCode.contains("TOKEN_Q5"));
    }
    
    @Test
    public void testNFAGenerateLexerMethod() throws IOException {
        // Create an NFA that recognizes "a|b"
        NFA nfa = new NFA();
        nfa.addState("start", false);
        nfa.addState("a_state", true);
        nfa.addState("b_state", true);
        
        nfa.addTransition("start", 'a', "a_state");
        nfa.addTransition("start", 'b', "b_state");
        
        nfa.setStartState("start");
        
        String lexerCode = CodeGen.generateLexer(nfa, "ChoiceLexer", "com.example");
        
        assertNotNull(lexerCode);
        assertTrue(lexerCode.contains("class ChoiceLexer"));
        assertTrue(lexerCode.contains("TOKEN_A_STATE"));
        assertTrue(lexerCode.contains("TOKEN_B_STATE"));
    }
    
    @Test
    public void testLexerCodeStructure() throws IOException {
        // Create a simple DFA
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        dfa.addTransition("q0", 'a', "q1");
        dfa = dfa.setStartState("q0");
        
        String lexerCode = CodeGen.generateLexer(dfa, "TestLexer", "com.example");
        
        // Check for essential components
        assertTrue(lexerCode.contains("class TestLexer"));
        assertTrue(lexerCode.contains("nextToken()"));
        assertTrue(lexerCode.contains("getAllTokens()"));
        assertTrue(lexerCode.contains("class Token"));
        assertTrue(lexerCode.contains("getType()"));
        assertTrue(lexerCode.contains("getValue()"));
        assertTrue(lexerCode.contains("getPosition()"));
    }
    
    @Test
    public void testMultipleAcceptingStates() throws IOException {
        // Create a DFA with multiple accepting states
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        dfa.addState("q2", true);
        
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        
        dfa = dfa.setStartState("q0");
        
        String lexerCode = CodeGen.generateLexer(dfa, "MultiLexer", "com.example");
        
        assertTrue(lexerCode.contains("TOKEN_Q1"));
        assertTrue(lexerCode.contains("TOKEN_Q2"));
    }
    
    @Test
    public void testEmptyDFA() throws IOException {
        // Create an empty DFA
        DFA dfa = new DFA();
        
        String lexerCode = CodeGen.generateLexer(dfa, "EmptyLexer", "com.example");
        
        assertNotNull(lexerCode);
        assertTrue(lexerCode.contains("class EmptyLexer"));
    }
    
    @Test
    public void testEmptyNFA() throws IOException {
        // Create an empty NFA
        NFA nfa = new NFA();
        
        String lexerCode = CodeGen.generateLexer(nfa, "EmptyNFALexer", "com.example");
        
        assertNotNull(lexerCode);
        assertTrue(lexerCode.contains("class EmptyNFALexer"));
    }
    
    @Test
    public void testGenerateLexerMainClass() throws IOException {
        // Create a simple DFA
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        dfa.addTransition("q0", 'a', "q1");
        dfa = dfa.setStartState("q0");

        String mainClassCode = CodeGen.generateLexerMainClass(dfa.getAcceptingStates(),"TestLexer", "TestMain", "com.example");
        
        assertNotNull(mainClassCode);
        assertTrue(mainClassCode.contains("class TestMain"));
        assertTrue(mainClassCode.contains("TestLexer lexer = new TestLexer(input)"));
        assertTrue(mainClassCode.contains("public static void main(String[] args)"));
        assertTrue(mainClassCode.contains("runInteractiveMode()"));
    }
    
    @Test
    public void testGenerateLexerMainClassWithCustomTokenTypes() throws IOException {
        // Create a DFA with custom token types
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        dfa.addTransition("q0", 'a', "q1");
        dfa = dfa.setStartState("q0");
        
        Map<String, String> tokenTypes = new HashMap<>();
        tokenTypes.put("q1", "LETTER_A");
        
        String mainClassCode = CodeGen.generateLexerMainClass(dfa.getAcceptingStates(), "CustomLexer", "CustomMain", "com.example", tokenTypes);
        
        assertNotNull(mainClassCode);
        assertTrue(mainClassCode.contains("class CustomMain"));
        assertTrue(mainClassCode.contains("CustomLexer lexer = new CustomLexer(input)"));
        assertTrue(mainClassCode.contains("LETTER_A"));
    }
    
    @Test
    public void testGenerateLexerMainClassFromNFA() throws IOException {
        // Create a simple NFA
        NFA nfa = new NFA();
        nfa.addState("start", false);
        nfa.addState("accept", true);
        nfa.addTransition("start", 'a', "accept");
        nfa.setStartState("start");
        
        String mainClassCode = CodeGen.generateLexerMainClass(nfa.getAcceptingStates(), "NFALexer", "NFAMain", "com.example");
        
        assertNotNull(mainClassCode);
        assertTrue(mainClassCode.contains("class NFAMain"));
        assertTrue(mainClassCode.contains("NFALexer lexer = new NFALexer(input)"));
        assertTrue(mainClassCode.contains("TOKEN_ACCEPT"));
    }
    
    @Test
    public void testLexerMainClassStructure() throws IOException {
        // Create a simple DFA
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        dfa.addTransition("q0", 'a', "q1");
        dfa = dfa.setStartState("q0");
        
        String mainClassCode = CodeGen.generateLexerMainClass(dfa.getAcceptingStates(),"TestLexer", "TestMain", "com.example");
        
        // Check for essential components
        assertTrue(mainClassCode.contains("public static void main(String[] args)"));
        assertTrue(mainClassCode.contains("printUsage()"));
        assertTrue(mainClassCode.contains("runInteractiveMode()"));
        assertTrue(mainClassCode.contains("escapeString"));
        assertTrue(mainClassCode.contains("getAllTokens()"));
        assertTrue(mainClassCode.contains("ERROR"));
    }
}
