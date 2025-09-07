package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.HashSet;

/**
 * Base test class for RegexToDFAConverter following SOLID principles.
 * Provides shared utilities and common test data for all converter tests.
 * 
 * Single Responsibility: Provides common testing infrastructure
 * Open/Closed: Extensible for new test scenarios
 * Liskov Substitution: Maintains consistent testing interface
 * Interface Segregation: Focused on testing utilities only
 * Dependency Inversion: Depends on abstractions (reflection for private methods)
 */
public abstract class RegexToDFAConverterBaseTest {
    
    protected static final String VALID_SIMPLE_REGEX = "a";
    protected static final String VALID_CONCAT_REGEX = "ab";
    protected static final String VALID_UNION_REGEX = "a|b";
    protected static final String VALID_STAR_REGEX = "a*";
    protected static final String VALID_PLUS_REGEX = "a+";
    protected static final String VALID_QUESTION_REGEX = "a?";
    protected static final String VALID_PARENTHESES_REGEX = "(a|b)*";
    protected static final String VALID_CHAR_CLASS_REGEX = "[abc]";
    protected static final String VALID_ESCAPE_REGEX = "\\.";
    protected static final String VALID_EMPTY_REGEX = "";
    
    protected static final String INVALID_STAR_START = "*a";
    protected static final String INVALID_PLUS_START = "+a";
    protected static final String INVALID_QUESTION_START = "?a";
    protected static final String INVALID_UNION_START = "|a";
    protected static final String INVALID_UNION_END = "a|";
    protected static final String INVALID_MISMATCHED_PARENS = "(a";
    protected static final String INVALID_MISMATCHED_PARENS2 = "a)";
    protected static final String INVALID_EMPTY_CHAR_CLASS = "[]";
    protected static final String INVALID_UNCLOSED_CHAR_CLASS = "[a";
    protected static final String INVALID_INCOMPLETE_ESCAPE = "\\";
    
    protected static final String[] VALID_REGEXES = {
        "a", "ab", "a|b", "a*", "a+", "a?", "(a|b)*", "[abc]", "\\.", ""
    };
    
    protected static final String[] INVALID_REGEXES = {
        "*a", "+a", "?a", "|a", "a|", "(a", "a)", "[]", "[a", "\\"
    };
    
    protected static final char[] OPERATORS = {'*', '+', '?', '|', '&'};
    protected static final char[] OPERANDS = {'a', 'b', 'c', '1', '2', '3'};
    protected static final char[] SPECIAL_CHARS = {'(', ')', '[', ']', '.', '^', '$'};
    
    /**
     * Invokes a private static method using reflection.
     * 
     * @param methodName the name of the method to invoke
     * @param parameterTypes the parameter types of the method
     * @param args the arguments to pass to the method
     * @return the result of the method invocation
     * @throws Exception if the method cannot be invoked
     */
    protected Object invokePrivateMethod(String methodName, Class<?>[] parameterTypes, Object... args) 
            throws Exception {
        Method method = RegexToDFAConverter.class.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(null, args);
    }
    
    /**
     * Invokes a private static method with a single String parameter.
     */
    protected Object invokePrivateMethod(String methodName, String arg) throws Exception {
        return invokePrivateMethod(methodName, new Class<?>[]{String.class}, arg);
    }
    
    /**
     * Invokes a private static method with a single char parameter.
     */
    protected Object invokePrivateMethod(String methodName, char arg) throws Exception {
        return invokePrivateMethod(methodName, new Class<?>[]{char.class}, arg);
    }
    
    /**
     * Invokes a private static method with a single int parameter.
     */
    protected Object invokePrivateMethod(String methodName, int arg) throws Exception {
        return invokePrivateMethod(methodName, new Class<?>[]{int.class}, arg);
    }
    
    /**
     * Invokes a private static method with String and int parameters.
     */
    protected Object invokePrivateMethod(String methodName, String arg1, int arg2) throws Exception {
        return invokePrivateMethod(methodName, new Class<?>[]{String.class, int.class}, arg1, arg2);
    }
    
    /**
     * Invokes a private static method with NFA and int parameters.
     */
    protected Object invokePrivateMethod(String methodName, NFA arg1, int arg2) throws Exception {
        return invokePrivateMethod(methodName, new Class<?>[]{NFA.class, int.class}, arg1, arg2);
    }
    
    /**
     * Invokes a private static method with NFA, int, and int parameters.
     */
    protected Object invokePrivateMethod(String methodName, NFA arg1, int arg2, int arg3) throws Exception {
        return invokePrivateMethod(methodName, new Class<?>[]{NFA.class, int.class, int.class}, arg1, arg2, arg3);
    }
    
    /**
     * Invokes a private static method with two int parameters.
     */
    protected Object invokePrivateMethod(String methodName, int arg1, int arg2) throws Exception {
        return invokePrivateMethod(methodName, new Class<?>[]{int.class, int.class}, arg1, arg2);
    }
    
    /**
     * Invokes a private static method with no parameters.
     */
    protected Object invokePrivateMethod(String methodName) throws Exception {
        return invokePrivateMethod(methodName, new Class<?>[]{}, new Object[]{});
    }
    
    /**
     * Asserts that a method throws IllegalArgumentException with the expected message.
     */
    protected void assertThrowsIllegalArgumentException(String methodName, String arg, String expectedMessage) {
        Exception exception = assertThrows(Exception.class, () -> {
            invokePrivateMethod(methodName, arg);
        });
        
        Throwable cause = exception.getCause();
        if (cause instanceof IllegalArgumentException) {
            assertEquals(expectedMessage, cause.getMessage());
        } else {
            fail("Expected IllegalArgumentException but got: " + exception.getClass().getSimpleName());
        }
    }
    
    /**
     * Asserts that a method throws IllegalArgumentException with the expected message.
     */
    protected void assertThrowsIllegalArgumentException(String methodName, char arg, String expectedMessage) {
        Exception exception = assertThrows(Exception.class, () -> {
            invokePrivateMethod(methodName, arg);
        });
        
        Throwable cause = exception.getCause();
        if (cause instanceof IllegalArgumentException) {
            assertEquals(expectedMessage, cause.getMessage());
        } else {
            fail("Expected IllegalArgumentException but got: " + exception.getClass().getSimpleName());
        }
    }
    
    /**
     * Asserts that a method throws IllegalArgumentException with the expected message.
     */
    protected void assertThrowsIllegalArgumentException(String methodName, int arg, String expectedMessage) {
        Exception exception = assertThrows(Exception.class, () -> {
            invokePrivateMethod(methodName, arg);
        });
        
        Throwable cause = exception.getCause();
        if (cause instanceof IllegalArgumentException) {
            assertEquals(expectedMessage, cause.getMessage());
        } else {
            fail("Expected IllegalArgumentException but got: " + exception.getClass().getSimpleName());
        }
    }
    
    /**
     * Creates a simple NFA for testing purposes.
     */
    protected NFA createSimpleNFA() {
        Set<String> acceptingStates = new HashSet<>();
        acceptingStates.add("accept");
        NFA nfa = new NFA("start", acceptingStates);
        nfa.addTransition("start", 'a', "accept");
        return nfa;
    }
    
    /**
     * Creates a simple DFA for testing purposes.
     */
    protected DFA createSimpleDFA() {
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        dfa.addTransition("q0", 'a', "q1");
        dfa = dfa.setStartState("q0");
        return dfa;
    }
    
    /**
     * Asserts that two NFAs are equivalent by checking their acceptance behavior.
     */
    protected void assertNFAEquivalent(NFA nfa1, NFA nfa2, String[] testStrings) {
        for (String testString : testStrings) {
            boolean accepts1 = nfa1.accepts(testString);
            boolean accepts2 = nfa2.accepts(testString);
            assertEquals(accepts1, accepts2, 
                String.format("NFAs should have same acceptance for string '%s'", testString));
        }
    }
    
    /**
     * Asserts that two DFAs are equivalent by checking their acceptance behavior.
     */
    protected void assertDFAEquivalent(DFA dfa1, DFA dfa2, String[] testStrings) {
        for (String testString : testStrings) {
            boolean accepts1 = dfa1.accepts(testString);
            boolean accepts2 = dfa2.accepts(testString);
            assertEquals(accepts1, accepts2, 
                String.format("DFAs should have same acceptance for string '%s'", testString));
        }
    }
    
    /**
     * Gets test strings for basic character testing.
     */
    protected String[] getBasicTestStrings() {
        return new String[]{"", "a", "b", "c", "ab", "ba", "abc", "aaa", "bbb"};
    }
    
    /**
     * Gets test strings for quantifier testing.
     */
    protected String[] getQuantifierTestStrings() {
        return new String[]{"", "a", "aa", "aaa", "aaaa", "aaaaa"};
    }
    
    /**
     * Gets test strings for union testing.
     */
    protected String[] getUnionTestStrings() {
        return new String[]{"", "a", "b", "ab", "ba", "aa", "bb"};
    }
}
