package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Transition class functionality.
 * Tests Transition creation, properties, and behavior.
 */
public class TransitionTests {
    
    @Test
    public void testConstructorWithSymbol() {
        Transition transition = new Transition("q0", 'a', "q1");
        
        assertNotNull(transition, "Transition should not be null");
        assertEquals("q0", transition.getFromState(), "From state should be 'q0'");
        assertEquals(Character.valueOf('a'), transition.getSymbol(), "Symbol should be 'a'");
        assertEquals("q1", transition.getToState(), "To state should be 'q1'");
        assertFalse(transition.isEpsilonTransition(), "Transition should not be epsilon transition");
    }
    
    @Test
    public void testConstructorWithEpsilonTransition() {
        Transition transition = new Transition("q0", null, "q1");
        
        assertNotNull(transition, "Transition should not be null");
        assertEquals("q0", transition.getFromState(), "From state should be 'q0'");
        assertNull(transition.getSymbol(), "Symbol should be null for epsilon transition");
        assertEquals("q1", transition.getToState(), "To state should be 'q1'");
        assertTrue(transition.isEpsilonTransition(), "Transition should be epsilon transition");
    }
    
    @Test
    public void testConstructorWithSpecialCharacters() {
        Transition transition = new Transition("q0", '!', "q1");
        
        assertNotNull(transition, "Transition should not be null");
        assertEquals("q0", transition.getFromState(), "From state should be 'q0'");
        assertEquals(Character.valueOf('!'), transition.getSymbol(), "Symbol should be '!'");
        assertEquals("q1", transition.getToState(), "To state should be 'q1'");
        assertFalse(transition.isEpsilonTransition(), "Transition should not be epsilon transition");
    }
    
    @Test
    public void testConstructorWithNumericSymbol() {
        Transition transition = new Transition("q0", '0', "q1");
        
        assertNotNull(transition, "Transition should not be null");
        assertEquals("q0", transition.getFromState(), "From state should be 'q0'");
        assertEquals(Character.valueOf('0'), transition.getSymbol(), "Symbol should be '0'");
        assertEquals("q1", transition.getToState(), "To state should be 'q1'");
        assertFalse(transition.isEpsilonTransition(), "Transition should not be epsilon transition");
    }
    
    @Test
    public void testConstructorWithUnicodeSymbol() {
        Transition transition = new Transition("q0", 'ñ', "q1");
        
        assertNotNull(transition, "Transition should not be null");
        assertEquals("q0", transition.getFromState(), "From state should be 'q0'");
        assertEquals(Character.valueOf('ñ'), transition.getSymbol(), "Symbol should be 'ñ'");
        assertEquals("q1", transition.getToState(), "To state should be 'q1'");
        assertFalse(transition.isEpsilonTransition(), "Transition should not be epsilon transition");
    }
    
    @Test
    public void testConstructorWithWhitespaceSymbol() {
        Transition transition = new Transition("q0", ' ', "q1");
        
        assertNotNull(transition, "Transition should not be null");
        assertEquals("q0", transition.getFromState(), "From state should be 'q0'");
        assertEquals(Character.valueOf(' '), transition.getSymbol(), "Symbol should be ' '");
        assertEquals("q1", transition.getToState(), "To state should be 'q1'");
        assertFalse(transition.isEpsilonTransition(), "Transition should not be epsilon transition");
    }
    
    @Test
    public void testConstructorWithEmptyStringStates() {
        Transition transition = new Transition("", 'a', "");
        
        assertNotNull(transition, "Transition should not be null");
        assertEquals("", transition.getFromState(), "From state should be empty string");
        assertEquals(Character.valueOf('a'), transition.getSymbol(), "Symbol should be 'a'");
        assertEquals("", transition.getToState(), "To state should be empty string");
        assertFalse(transition.isEpsilonTransition(), "Transition should not be epsilon transition");
    }
    
    @Test
    public void testConstructorWithSpecialStateNames() {
        Transition transition = new Transition("q0_accepting", 'a', "q1_final");
        
        assertNotNull(transition, "Transition should not be null");
        assertEquals("q0_accepting", transition.getFromState(), "From state should be 'q0_accepting'");
        assertEquals(Character.valueOf('a'), transition.getSymbol(), "Symbol should be 'a'");
        assertEquals("q1_final", transition.getToState(), "To state should be 'q1_final'");
        assertFalse(transition.isEpsilonTransition(), "Transition should not be epsilon transition");
    }
    
    @Test
    public void testConstructorWithUnicodeStateNames() {
        Transition transition = new Transition("状态0", 'a', "状态1");
        
        assertNotNull(transition, "Transition should not be null");
        assertEquals("状态0", transition.getFromState(), "From state should be '状态0'");
        assertEquals(Character.valueOf('a'), transition.getSymbol(), "Symbol should be 'a'");
        assertEquals("状态1", transition.getToState(), "To state should be '状态1'");
        assertFalse(transition.isEpsilonTransition(), "Transition should not be epsilon transition");
    }
    
    @Test
    public void testEqualsWithSameTransition() {
        Transition transition1 = new Transition("q0", 'a', "q1");
        Transition transition2 = new Transition("q0", 'a', "q1");
        
        assertEquals(transition1, transition2, "Transitions with same properties should be equal");
        assertEquals(transition1.hashCode(), transition2.hashCode(), "Transitions with same properties should have same hash code");
    }
    
    @Test
    public void testEqualsWithDifferentFromState() {
        Transition transition1 = new Transition("q0", 'a', "q1");
        Transition transition2 = new Transition("q2", 'a', "q1");
        
        assertNotEquals(transition1, transition2, "Transitions with different from states should not be equal");
    }
    
    @Test
    public void testEqualsWithDifferentSymbol() {
        Transition transition1 = new Transition("q0", 'a', "q1");
        Transition transition2 = new Transition("q0", 'b', "q1");
        
        assertNotEquals(transition1, transition2, "Transitions with different symbols should not be equal");
    }
    
    @Test
    public void testEqualsWithDifferentToState() {
        Transition transition1 = new Transition("q0", 'a', "q1");
        Transition transition2 = new Transition("q0", 'a', "q2");
        
        assertNotEquals(transition1, transition2, "Transitions with different to states should not be equal");
    }
    
    @Test
    public void testEqualsWithEpsilonTransitions() {
        Transition transition1 = new Transition("q0", null, "q1");
        Transition transition2 = new Transition("q0", null, "q1");
        
        assertEquals(transition1, transition2, "Epsilon transitions with same properties should be equal");
        assertEquals(transition1.hashCode(), transition2.hashCode(), "Epsilon transitions with same properties should have same hash code");
    }
    
    @Test
    public void testEqualsWithMixedEpsilonTransitions() {
        Transition transition1 = new Transition("q0", 'a', "q1");
        Transition transition2 = new Transition("q0", null, "q1");
        
        assertNotEquals(transition1, transition2, "Epsilon and non-epsilon transitions should not be equal");
    }
    
    @Test
    public void testEqualsWithNull() {
        Transition transition = new Transition("q0", 'a', "q1");
        
        assertNotEquals(transition, null, "Transition should not be equal to null");
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        Transition transition = new Transition("q0", 'a', "q1");
        
        assertNotEquals(transition, "q0", "Transition should not be equal to different class");
    }
    
    @Test
    public void testEqualsWithSelf() {
        Transition transition = new Transition("q0", 'a', "q1");
        
        assertEquals(transition, transition, "Transition should be equal to itself");
    }
    
    @Test
    public void testHashCodeConsistency() {
        Transition transition = new Transition("q0", 'a', "q1");
        int hashCode1 = transition.hashCode();
        int hashCode2 = transition.hashCode();
        
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent");
    }
    
    @Test
    public void testHashCodeWithSameProperties() {
        Transition transition1 = new Transition("q0", 'a', "q1");
        Transition transition2 = new Transition("q0", 'a', "q1");
        
        assertEquals(transition1.hashCode(), transition2.hashCode(), "Transitions with same properties should have same hash code");
    }
    
    @Test
    public void testHashCodeWithDifferentProperties() {
        Transition transition1 = new Transition("q0", 'a', "q1");
        Transition transition2 = new Transition("q0", 'b', "q1");
        
        assertNotEquals(transition1.hashCode(), transition2.hashCode(), "Transitions with different properties should have different hash codes");
    }
    
    @Test
    public void testToStringWithSymbol() {
        Transition transition = new Transition("q0", 'a', "q1");
        
        String toString = transition.toString();
        
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("q0"), "toString should contain from state");
        assertTrue(toString.contains("a"), "toString should contain symbol");
        assertTrue(toString.contains("q1"), "toString should contain to state");
        assertTrue(toString.contains("-->"), "toString should contain arrow");
    }
    
    @Test
    public void testToStringWithEpsilonTransition() {
        Transition transition = new Transition("q0", null, "q1");
        
        String toString = transition.toString();
        
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("q0"), "toString should contain from state");
        assertTrue(toString.contains("q1"), "toString should contain to state");
        assertTrue(toString.contains("ε"), "toString should contain epsilon symbol");
        assertTrue(toString.contains("-->"), "toString should contain arrow");
    }
    
    @Test
    public void testToStringWithSpecialSymbol() {
        Transition transition = new Transition("q0", '!', "q1");
        
        String toString = transition.toString();
        
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("q0"), "toString should contain from state");
        assertTrue(toString.contains("!"), "toString should contain special symbol");
        assertTrue(toString.contains("q1"), "toString should contain to state");
    }
    
    @Test
    public void testToStringWithNumericSymbol() {
        Transition transition = new Transition("q0", '0', "q1");
        
        String toString = transition.toString();
        
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("q0"), "toString should contain from state");
        assertTrue(toString.contains("0"), "toString should contain numeric symbol");
        assertTrue(toString.contains("q1"), "toString should contain to state");
    }
    
    @Test
    public void testToStringWithUnicodeSymbol() {
        Transition transition = new Transition("q0", 'ñ', "q1");
        
        String toString = transition.toString();
        
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("q0"), "toString should contain from state");
        assertTrue(toString.contains("ñ"), "toString should contain Unicode symbol");
        assertTrue(toString.contains("q1"), "toString should contain to state");
    }
    
    @Test
    public void testToStringWithWhitespaceSymbol() {
        Transition transition = new Transition("q0", ' ', "q1");
        
        String toString = transition.toString();
        
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("q0"), "toString should contain from state");
        assertTrue(toString.contains(" "), "toString should contain whitespace symbol");
        assertTrue(toString.contains("q1"), "toString should contain to state");
    }
    
    @Test
    public void testTransitionInHashSet() {
        Transition transition1 = new Transition("q0", 'a', "q1");
        Transition transition2 = new Transition("q0", 'a', "q1");
        Transition transition3 = new Transition("q0", 'b', "q1");
        
        java.util.Set<Transition> transitionSet = new java.util.HashSet<>();
        transitionSet.add(transition1);
        transitionSet.add(transition2);
        transitionSet.add(transition3);
        
        assertEquals(2, transitionSet.size(), "HashSet should contain 2 unique transitions");
        assertTrue(transitionSet.contains(transition1), "HashSet should contain transition1");
        assertTrue(transitionSet.contains(transition2), "HashSet should contain transition2");
        assertTrue(transitionSet.contains(transition3), "HashSet should contain transition3");
    }
    
    @Test
    public void testTransitionInHashMap() {
        Transition transition1 = new Transition("q0", 'a', "q1");
        Transition transition2 = new Transition("q0", 'a', "q1");
        Transition transition3 = new Transition("q0", 'b', "q1");
        
        java.util.Map<Transition, String> transitionMap = new java.util.HashMap<>();
        transitionMap.put(transition1, "value1");
        transitionMap.put(transition2, "value2");
        transitionMap.put(transition3, "value3");
        
        assertEquals(2, transitionMap.size(), "HashMap should contain 2 unique transitions");
        assertEquals("value2", transitionMap.get(transition1), "HashMap should return value2 for transition1");
        assertEquals("value2", transitionMap.get(transition2), "HashMap should return value2 for transition2");
        assertEquals("value3", transitionMap.get(transition3), "HashMap should return value3 for transition3");
    }
    
    @Test
    public void testTransitionImmutability() {
        Transition transition = new Transition("q0", 'a', "q1");
        
        // Transition should be immutable - we can't change its properties after creation
        // This test verifies that the constructor works correctly
        assertEquals("q0", transition.getFromState(), "From state should remain unchanged");
        assertEquals(Character.valueOf('a'), transition.getSymbol(), "Symbol should remain unchanged");
        assertEquals("q1", transition.getToState(), "To state should remain unchanged");
    }
    
    @Test
    public void testEpsilonTransitionProperties() {
        Transition transition = new Transition("q0", null, "q1");
        
        assertTrue(transition.isEpsilonTransition(), "Transition should be epsilon transition");
        assertNull(transition.getSymbol(), "Symbol should be null for epsilon transition");
        assertEquals("q0", transition.getFromState(), "From state should be 'q0'");
        assertEquals("q1", transition.getToState(), "To state should be 'q1'");
    }
    
    @Test
    public void testNonEpsilonTransitionProperties() {
        Transition transition = new Transition("q0", 'a', "q1");
        
        assertFalse(transition.isEpsilonTransition(), "Transition should not be epsilon transition");
        assertNotNull(transition.getSymbol(), "Symbol should not be null for non-epsilon transition");
        assertEquals(Character.valueOf('a'), transition.getSymbol(), "Symbol should be 'a'");
        assertEquals("q0", transition.getFromState(), "From state should be 'q0'");
        assertEquals("q1", transition.getToState(), "To state should be 'q1'");
    }
}
