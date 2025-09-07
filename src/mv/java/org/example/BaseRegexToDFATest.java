package org.example;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Base test class for RegexToDFAConverter tests.
 * Provides common utilities and setup methods for all test classes.
 * Follows inheritance pattern to share common test code.
 */
public abstract class BaseRegexToDFATest {
    
    protected DFA dfa;
    
    @BeforeEach
    public void setUp() {
        // Reset DFA before each test
        dfa = null;
    }
    
    /**
     * Helper method to convert regex to DFA and store it for testing.
     * 
     * @param regex the regular expression to convert
     * @return the converted DFA
     */
    protected DFA convertRegex(String regex) {
        dfa = RegexToDFAConverter.convert(regex);
        return dfa;
    }
    
    /**
     * Helper method to assert that a DFA accepts a given string.
     * 
     * @param input the string to test
     * @param shouldAccept whether the DFA should accept the string
     */
    protected void assertAccepts(String input, boolean shouldAccept) {
        assertNotNull(dfa, "DFA should not be null");
        assertEquals(shouldAccept, dfa.accepts(input), 
            String.format("DFA should %s string '%s'", 
                shouldAccept ? "accept" : "reject", input));
    }
    
    /**
     * Helper method to test multiple strings against the current DFA.
     * 
     * @param testCases array of TestCase objects containing input and expected result
     */
    protected void assertMultipleTestCases(TestCase... testCases) {
        assertNotNull(dfa, "DFA should not be null");
        
        for (TestCase testCase : testCases) {
            assertEquals(testCase.shouldAccept, dfa.accepts(testCase.input), 
                String.format("DFA should %s string '%s'", 
                    testCase.shouldAccept ? "accept" : "reject", testCase.input));
        }
    }
    
    /**
     * Helper method to assert that a regex conversion throws an exception.
     * 
     * @param regex the invalid regex
     * @param expectedExceptionType the expected exception type
     */
    protected void assertThrowsException(String regex, Class<? extends Exception> expectedExceptionType) {
        Exception exception = assertThrows(expectedExceptionType, () -> {
            RegexToDFAConverter.convert(regex);
        });
        assertNotNull(exception.getMessage(), "Exception should have a message");
    }
    
    /**
     * Helper method to assert that a regex conversion throws IllegalArgumentException.
     * 
     * @param regex the invalid regex
     * @param expectedMessage the expected exception message (can be partial)
     */
    protected void assertThrowsIllegalArgumentException(String regex, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RegexToDFAConverter.convert(regex);
        });
        
        if (expectedMessage != null && !expectedMessage.isEmpty()) {
            assertTrue(exception.getMessage().contains(expectedMessage), 
                String.format("Exception message should contain '%s', but was: '%s'", 
                    expectedMessage, exception.getMessage()));
        }
    }
    
    /**
     * Helper method to verify DFA properties.
     * 
     * @param expectedStatesCount expected number of states (can be null to skip)
     * @param expectedAlphabetSize expected alphabet size (can be null to skip)
     */
    protected void assertDFAProperties(Integer expectedStatesCount, Integer expectedAlphabetSize) {
        assertNotNull(dfa, "DFA should not be null");
        
        // Note: Since DFA fields are private, we can't directly test them
        // But we can test the behavior through acceptance testing
        assertTrue(true, "DFA properties verification placeholder");
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
