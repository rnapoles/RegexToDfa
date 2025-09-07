package org.example;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.HashSet;

/**
 * Base test class for DFA and NFA tests.
 * Provides common utilities and setup methods for all automaton test classes.
 * Follows inheritance pattern to share common test code.
 */
public abstract class BaseAutomatonTest {
    
    protected DFA dfa;
    protected NFA nfa;
    
    @BeforeEach
    public void setUp() {
        // Reset automata before each test
        dfa = null;
        nfa = null;
    }
    
    /**
     * Helper method to create a simple DFA for testing.
     * 
     * @param startState the start state
     * @param acceptingStates the accepting states
     * @return a new DFA instance
     */
    protected DFA createSimpleDFA(String startState, Set<String> acceptingStates) {
        return new DFA(startState, acceptingStates);
    }
    
    /**
     * Helper method to create a simple NFA for testing.
     * 
     * @param startState the start state
     * @param acceptingStates the accepting states
     * @return a new NFA instance
     */
    protected NFA createSimpleNFA(String startState, Set<String> acceptingStates) {
        return new NFA(startState, acceptingStates);
    }
    
    /**
     * Helper method to create a set of strings from varargs.
     * 
     * @param states the state names
     * @return a Set of state names
     */
    protected Set<String> stateSet(String... states) {
        Set<String> set = new HashSet<>();
        for (String state : states) {
            set.add(state);
        }
        return set;
    }
    
    /**
     * Helper method to assert that a DFA accepts a given string.
     * 
     * @param dfa the DFA to test
     * @param input the string to test
     * @param shouldAccept whether the DFA should accept the string
     */
    protected void assertDFAAccepts(DFA dfa, String input, boolean shouldAccept) {
        assertNotNull(dfa, "DFA should not be null");
        assertEquals(shouldAccept, dfa.accepts(input), 
            String.format("DFA should %s string '%s'", 
                shouldAccept ? "accept" : "reject", input));
    }
    
    /**
     * Helper method to assert that an NFA accepts a given string.
     * 
     * @param nfa the NFA to test
     * @param input the string to test
     * @param shouldAccept whether the NFA should accept the string
     */
    protected void assertNFAAccepts(NFA nfa, String input, boolean shouldAccept) {
        assertNotNull(nfa, "NFA should not be null");
        assertEquals(shouldAccept, nfa.accepts(input), 
            String.format("NFA should %s string '%s'", 
                shouldAccept ? "accept" : "reject", input));
    }
    
    /**
     * Helper method to test multiple strings against a DFA.
     * 
     * @param dfa the DFA to test
     * @param testCases array of TestCase objects containing input and expected result
     */
    protected void assertDFAMultipleTestCases(DFA dfa, TestCase... testCases) {
        assertNotNull(dfa, "DFA should not be null");
        
        for (TestCase testCase : testCases) {
            assertEquals(testCase.shouldAccept, dfa.accepts(testCase.input), 
                String.format("DFA should %s string '%s'", 
                    testCase.shouldAccept ? "accept" : "reject", testCase.input));
        }
    }
    
    /**
     * Helper method to test multiple strings against an NFA.
     * 
     * @param nfa the NFA to test
     * @param testCases array of TestCase objects containing input and expected result
     */
    protected void assertNFAMultipleTestCases(NFA nfa, TestCase... testCases) {
        assertNotNull(nfa, "NFA should not be null");
        
        for (TestCase testCase : testCases) {
            assertEquals(testCase.shouldAccept, nfa.accepts(testCase.input), 
                String.format("NFA should %s string '%s'", 
                    testCase.shouldAccept ? "accept" : "reject", testCase.input));
        }
    }
    
    /**
     * Helper method to assert that a method throws an exception.
     * 
     * @param executable the code to execute
     * @param expectedExceptionType the expected exception type
     * @param <T> the exception type
     * @return the caught exception
     */
    protected <T extends Exception> T assertThrowsException(org.junit.jupiter.api.function.Executable executable, Class<T> expectedExceptionType) {
        return assertThrows(expectedExceptionType, executable);
    }
    
    /**
     * Helper method to assert that a method throws IllegalArgumentException with specific message.
     * 
     * @param executable the code to execute
     * @param expectedMessage the expected exception message (can be partial)
     */
    protected void assertThrowsIllegalArgumentException(org.junit.jupiter.api.function.Executable executable, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        
        if (expectedMessage != null && !expectedMessage.isEmpty()) {
            assertTrue(exception.getMessage().contains(expectedMessage), 
                String.format("Exception message should contain '%s', but was: '%s'", 
                    expectedMessage, exception.getMessage()));
        }
    }
    
    /**
     * Inner class to represent a test case with input and expected result.
     */
    protected static class TestCase {
        public final String input;
        public final boolean shouldAccept;
        
        public TestCase(String input, boolean shouldAccept) {
            this.input = input;
            this.shouldAccept = shouldAccept;
        }
        
        @Override
        public String toString() {
            return String.format("TestCase{input='%s', shouldAccept=%s}", input, shouldAccept);
        }
    }
    
    /**
     * Helper method to create test cases more easily.
     * 
     * @param input the input string
     * @param shouldAccept whether it should be accepted
     * @return a TestCase object
     */
    protected TestCase testCase(String input, boolean shouldAccept) {
        return new TestCase(input, shouldAccept);
    }
}
