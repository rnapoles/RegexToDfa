package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for State class functionality.
 * Tests State creation, properties, and behavior.
 */
public class StateTests {
    
    @Test
    public void testConstructorWithNameAndAcceptingFlag() {
        State state = new State("q0", true);
        
        assertNotNull(state, "State should not be null");
        assertEquals("q0", state.getName(), "State name should be 'q0'");
        assertTrue(state.isAccepting(), "State should be accepting");
    }
    
    @Test
    public void testConstructorWithNameOnly() {
        State state = new State("q1");
        
        assertNotNull(state, "State should not be null");
        assertEquals("q1", state.getName(), "State name should be 'q1'");
        assertFalse(state.isAccepting(), "State should not be accepting by default");
    }
    
    @Test
    public void testConstructorWithNonAcceptingFlag() {
        State state = new State("q2", false);
        
        assertNotNull(state, "State should not be null");
        assertEquals("q2", state.getName(), "State name should be 'q2'");
        assertFalse(state.isAccepting(), "State should not be accepting");
    }
    
    @Test
    public void testEqualsWithSameState() {
        State state1 = new State("q0", true);
        State state2 = new State("q0", true);
        
        assertEquals(state1, state2, "States with same name should be equal");
        assertEquals(state1.hashCode(), state2.hashCode(), "States with same name should have same hash code");
    }
    
    @Test
    public void testEqualsWithDifferentAcceptingFlag() {
        State state1 = new State("q0", true);
        State state2 = new State("q0", false);
        
        assertEquals(state1, state2, "States with same name should be equal regardless of accepting flag");
        assertEquals(state1.hashCode(), state2.hashCode(), "States with same name should have same hash code");
    }
    
    @Test
    public void testEqualsWithDifferentName() {
        State state1 = new State("q0", true);
        State state2 = new State("q1", true);
        
        assertNotEquals(state1, state2, "States with different names should not be equal");
    }
    
    @Test
    public void testEqualsWithNull() {
        State state = new State("q0", true);
        
        assertNotEquals(state, null, "State should not be equal to null");
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        State state = new State("q0", true);
        String string = "q0";
        
        assertNotEquals(state, string, "State should not be equal to different class");
    }
    
    @Test
    public void testEqualsWithSelf() {
        State state = new State("q0", true);
        
        assertEquals(state, state, "State should be equal to itself");
    }
    
    @Test
    public void testHashCodeConsistency() {
        State state = new State("q0", true);
        int hashCode1 = state.hashCode();
        int hashCode2 = state.hashCode();
        
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent");
    }
    
    @Test
    public void testHashCodeWithSameName() {
        State state1 = new State("q0", true);
        State state2 = new State("q0", false);
        
        assertEquals(state1.hashCode(), state2.hashCode(), "States with same name should have same hash code");
    }
    
    @Test
    public void testHashCodeWithDifferentName() {
        State state1 = new State("q0", true);
        State state2 = new State("q1", true);
        
        assertNotEquals(state1.hashCode(), state2.hashCode(), "States with different names should have different hash codes");
    }
    
    @Test
    public void testToString() {
        State state = new State("q0", true);
        
        assertEquals("q0", state.toString(), "toString should return state name");
    }
    
    @Test
    public void testToStringWithDifferentName() {
        State state = new State("accepting_state", false);
        
        assertEquals("accepting_state", state.toString(), "toString should return state name");
    }
    
    @Test
    public void testStateWithEmptyName() {
        State state = new State("", true);
        
        assertNotNull(state, "State should not be null");
        assertEquals("", state.getName(), "State name should be empty string");
        assertTrue(state.isAccepting(), "State should be accepting");
    }
    
    @Test
    public void testStateWithSpecialCharacters() {
        State state = new State("q0_accepting", true);
        
        assertNotNull(state, "State should not be null");
        assertEquals("q0_accepting", state.getName(), "State name should contain special characters");
        assertTrue(state.isAccepting(), "State should be accepting");
    }
    
    @Test
    public void testStateWithUnicodeCharacters() {
        State state = new State("状态0", true);
        
        assertNotNull(state, "State should not be null");
        assertEquals("状态0", state.getName(), "State name should contain Unicode characters");
        assertTrue(state.isAccepting(), "State should be accepting");
    }
    
    @Test
    public void testStateWithNumericName() {
        State state = new State("123", false);
        
        assertNotNull(state, "State should not be null");
        assertEquals("123", state.getName(), "State name should be numeric");
        assertFalse(state.isAccepting(), "State should not be accepting");
    }
    
    @Test
    public void testStateWithWhitespaceName() {
        State state = new State("  q0  ", true);
        
        assertNotNull(state, "State should not be null");
        assertEquals("  q0  ", state.getName(), "State name should preserve whitespace");
        assertTrue(state.isAccepting(), "State should be accepting");
    }
    
    @Test
    public void testStateImmutability() {
        State state = new State("q0", true);
        
        // State should be immutable - we can't change its properties after creation
        // This test verifies that the constructor works correctly
        assertEquals("q0", state.getName(), "State name should remain unchanged");
        assertTrue(state.isAccepting(), "State accepting flag should remain unchanged");
    }
    
    @Test
    public void testStateInHashSet() {
        State state1 = new State("q0", true);
        State state2 = new State("q0", false);
        State state3 = new State("q1", true);
        
        java.util.Set<State> stateSet = new java.util.HashSet<>();
        stateSet.add(state1);
        stateSet.add(state2);
        stateSet.add(state3);
        
        assertEquals(2, stateSet.size(), "HashSet should contain 2 unique states");
        assertTrue(stateSet.contains(state1), "HashSet should contain state1");
        assertTrue(stateSet.contains(state2), "HashSet should contain state2");
        assertTrue(stateSet.contains(state3), "HashSet should contain state3");
    }
    
    @Test
    public void testStateInHashMap() {
        State state1 = new State("q0", true);
        State state2 = new State("q0", false);
        State state3 = new State("q1", true);
        
        java.util.Map<State, String> stateMap = new java.util.HashMap<>();
        stateMap.put(state1, "value1");
        stateMap.put(state2, "value2");
        stateMap.put(state3, "value3");
        
        assertEquals(2, stateMap.size(), "HashMap should contain 2 unique states");
        assertEquals("value2", stateMap.get(state1), "HashMap should return value2 for state1");
        assertEquals("value2", stateMap.get(state2), "HashMap should return value2 for state2");
        assertEquals("value3", stateMap.get(state3), "HashMap should return value3 for state3");
    }
}
