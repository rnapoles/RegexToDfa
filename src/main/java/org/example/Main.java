package org.example;

import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("=== NFA and DFA Implementation Test ===\n");
        
        // Test 1: Basic NFA creation and acceptance
        testBasicNFA();
        
        // Test 2: NFA operations (star, plus, question, union, concatenate)
        testNFAOperations();
        
        // Test 3: NFA to DFA conversion
        testNFAToDFAConversion();
        
        // Test 4: DFA minimization
        testDFAMinimization();
        
        // Test 5: DFA DOT generation
        testDFADotGeneration();
    }
    
    private static void testBasicNFA() {
        System.out.println("--- Test 1: Basic NFA Creation and Acceptance ---");
        
        // Create a simple NFA that accepts "ab"
        NFA nfa = new NFA();
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q2");
        
        // Set accepting state
        Set<String> acceptingStates = new HashSet<String>();
        acceptingStates.add("q2");
        NFA simpleNFA = new NFA("q0", acceptingStates);
        simpleNFA.addTransition("q0", 'a', "q1");
        simpleNFA.addTransition("q1", 'b', "q2");
        
        System.out.println("Testing NFA that accepts 'ab':");
        
        // Test cases with expected vs received results
        String[] testStrings = {"ab", "a", "b", "abc"};
        boolean[] expectedResults = {true, false, false, false};
        
        for (int i = 0; i < testStrings.length; i++) {
            boolean received = simpleNFA.accepts(testStrings[i]);
            boolean expected = expectedResults[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            System.out.println("'" + testStrings[i] + "' - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        System.out.println();
    }
    
    private static void testNFAOperations() {
        System.out.println("--- Test 2: NFA Operations ---");
        
        // Create NFA for 'a'
        Set<String> acceptingStatesA = new HashSet<String>();
        acceptingStatesA.add("q1");
        NFA nfaA = new NFA("q0", acceptingStatesA);
        nfaA.addTransition("q0", 'a', "q1");
        
        // Create NFA for 'b'
        Set<String> acceptingStatesB = new HashSet<String>();
        acceptingStatesB.add("q3");
        NFA nfaB = new NFA("q2", acceptingStatesB);
        nfaB.addTransition("q2", 'b', "q3");
        
        // Test union (a|b)
        NFA unionNFA = nfaA.union(nfaB);
        System.out.println("Union (a|b) test:");
        String[] unionTests = {"a", "b", "ab"};
        boolean[] unionExpected = {true, true, false};
        for (int i = 0; i < unionTests.length; i++) {
            boolean received = unionNFA.accepts(unionTests[i]);
            boolean expected = unionExpected[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            System.out.println("'" + unionTests[i] + "' - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        
        // Test concatenation (ab)
        NFA concatNFA = nfaA.concatenate(nfaB);
        System.out.println("\nConcatenation (ab) test:");
        String[] concatTests = {"ab", "a", "b"};
        boolean[] concatExpected = {true, false, false};
        for (int i = 0; i < concatTests.length; i++) {
            boolean received = concatNFA.accepts(concatTests[i]);
            boolean expected = concatExpected[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            System.out.println("'" + concatTests[i] + "' - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        
        // Test star operation (a*)
        NFA starNFA = nfaA.star();
        System.out.println("\nStar (a*) test:");
        String[] starTests = {"", "a", "aa", "aaa", "b"};
        boolean[] starExpected = {true, true, true, true, false};
        for (int i = 0; i < starTests.length; i++) {
            boolean received = starNFA.accepts(starTests[i]);
            boolean expected = starExpected[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            String displayString = starTests[i].isEmpty() ? "'' (empty)" : "'" + starTests[i] + "'";
            System.out.println(displayString + " - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        
        // Test plus operation (a+)
        NFA plusNFA = nfaA.plus();
        System.out.println("\nPlus (a+) test:");
        String[] plusTests = {"", "a", "aa", "b"};
        boolean[] plusExpected = {false, true, true, false};
        for (int i = 0; i < plusTests.length; i++) {
            boolean received = plusNFA.accepts(plusTests[i]);
            boolean expected = plusExpected[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            String displayString = plusTests[i].isEmpty() ? "'' (empty)" : "'" + plusTests[i] + "'";
            System.out.println(displayString + " - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        
        // Test question operation (a?)
        NFA questionNFA = nfaA.question();
        System.out.println("\nQuestion (a?) test:");
        String[] questionTests = {"", "a", "aa"};
        boolean[] questionExpected = {true, true, false};
        for (int i = 0; i < questionTests.length; i++) {
            boolean received = questionNFA.accepts(questionTests[i]);
            boolean expected = questionExpected[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            String displayString = questionTests[i].isEmpty() ? "'' (empty)" : "'" + questionTests[i] + "'";
            System.out.println(displayString + " - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        System.out.println();
    }
    
    private static void testNFAToDFAConversion() {
        System.out.println("--- Test 3: NFA to DFA Conversion ---");
        
        // Create NFA for (a|b)*
        Set<String> acceptingStatesA = new HashSet<String>();
        acceptingStatesA.add("q1");
        NFA nfaA = new NFA("q0", acceptingStatesA);
        nfaA.addTransition("q0", 'a', "q1");
        
        Set<String> acceptingStatesB = new HashSet<String>();
        acceptingStatesB.add("q3");
        NFA nfaB = new NFA("q2", acceptingStatesB);
        nfaB.addTransition("q2", 'b', "q3");
        
        NFA unionNFA = nfaA.union(nfaB);
        NFA starNFA = unionNFA.star();
        
        // Convert to DFA
        DFA dfa = starNFA.toDFA();
        
        System.out.println("NFA (a|b)* converted to DFA:");
        String[] dfaTests = {"", "a", "b", "ab", "ba", "aab", "c"};
        boolean[] dfaExpected = {true, true, true, true, true, true, false};
        
        for (int i = 0; i < dfaTests.length; i++) {
            boolean received = dfa.accepts(dfaTests[i]);
            boolean expected = dfaExpected[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            String displayString = dfaTests[i].isEmpty() ? "'' (empty)" : "'" + dfaTests[i] + "'";
            System.out.println(displayString + " - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        System.out.println();
    }
    
    private static void testDFAMinimization() {
        System.out.println("--- Test 4: DFA Minimization ---");
        
        // Create a DFA that can be minimized
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", false);
        dfa.addState("q2", true);
        dfa.addState("q3", true);
        
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addTransition("q1", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        dfa.addTransition("q2", 'a', "q1");
        dfa.addTransition("q2", 'b', "q3");
        dfa.addTransition("q3", 'a', "q1");
        dfa.addTransition("q3", 'b', "q3");
        
        // Set the start state
        dfa = dfa.setStartState("q0");
        
        System.out.println("Original DFA test:");
        String[] originalTests = {"bb", "bbb", "ab", "a"};
        boolean[] originalExpected = {true, true, true, false};
        
        for (int i = 0; i < originalTests.length; i++) {
            boolean received = dfa.accepts(originalTests[i]);
            boolean expected = originalExpected[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            System.out.println("'" + originalTests[i] + "' - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        
        // Minimize DFA
        DFA minimizedDFA = dfa.minimizeDFA();
        
        System.out.println("\nMinimized DFA test:");
        String[] minimizedTests = {"bb", "bbb", "ab", "a"};
        boolean[] minimizedExpected = {true, true, true, false};
        
        for (int i = 0; i < minimizedTests.length; i++) {
            boolean received = minimizedDFA.accepts(minimizedTests[i]);
            boolean expected = minimizedExpected[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            System.out.println("'" + minimizedTests[i] + "' - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        System.out.println();
    }
    
    private static void testDFADotGeneration() {
        System.out.println("--- Test 5: DFA DOT Generation ---");
        
        // Create a simple DFA
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'a', "q1");
        
        // Set the start state
        dfa = dfa.setStartState("q0");
        
        String dotRepresentation = dfa.toDot();
        System.out.println("DOT representation of DFA:");
        System.out.println(dotRepresentation);
        
        // Test that the DFA still works correctly after DOT generation
        System.out.println("\nVerifying DFA functionality after DOT generation:");
        String[] dotTests = {"a", "aa", "aaa", "b", ""};
        boolean[] dotExpected = {true, true, true, false, false};
        
        for (int i = 0; i < dotTests.length; i++) {
            boolean received = dfa.accepts(dotTests[i]);
            boolean expected = dotExpected[i];
            String status = (received == expected) ? "✓ PASS" : "✗ FAIL";
            String displayString = dotTests[i].isEmpty() ? "'' (empty)" : "'" + dotTests[i] + "'";
            System.out.println(displayString + " - Expected: " + expected + 
                             ", Received: " + received + " " + status);
        }
        System.out.println();
    }
}