package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.HashSet;

/**
 * Test class for StateSet class functionality.
 * Tests StateSet creation, manipulation, and behavior.
 */
public class StateSetTests {
    
    @Test
    public void testDefaultConstructor() {
        StateSet stateSet = new StateSet();
        
        assertNotNull(stateSet, "StateSet should not be null");
        assertTrue(stateSet.isEmpty(), "New StateSet should be empty");
        assertEquals(0, stateSet.size(), "New StateSet should have size 0");
    }
    
    @Test
    public void testConstructorWithSet() {
        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");
        states.add("q2");
        
        StateSet stateSet = new StateSet(states);
        
        assertNotNull(stateSet, "StateSet should not be null");
        assertFalse(stateSet.isEmpty(), "StateSet should not be empty");
        assertEquals(3, stateSet.size(), "StateSet should have size 3");
        assertTrue(stateSet.contains("q0"), "StateSet should contain q0");
        assertTrue(stateSet.contains("q1"), "StateSet should contain q1");
        assertTrue(stateSet.contains("q2"), "StateSet should contain q2");
    }
    
    @Test
    public void testConstructorWithEmptySet() {
        Set<String> emptyStates = new HashSet<>();
        
        StateSet stateSet = new StateSet(emptyStates);
        
        assertNotNull(stateSet, "StateSet should not be null");
        assertTrue(stateSet.isEmpty(), "StateSet should be empty");
        assertEquals(0, stateSet.size(), "StateSet should have size 0");
    }
    
    @Test
    public void testAddState() {
        StateSet stateSet = new StateSet();
        
        stateSet.addState("q0");
        
        assertFalse(stateSet.isEmpty(), "StateSet should not be empty after adding state");
        assertEquals(1, stateSet.size(), "StateSet should have size 1");
        assertTrue(stateSet.contains("q0"), "StateSet should contain q0");
    }
    
    @Test
    public void testAddStateWithDuplicate() {
        StateSet stateSet = new StateSet();
        
        stateSet.addState("q0");
        stateSet.addState("q0"); // Add duplicate
        
        assertEquals(1, stateSet.size(), "StateSet should have size 1 after adding duplicate");
        assertTrue(stateSet.contains("q0"), "StateSet should contain q0");
    }
    
    @Test
    public void testAddStateWithMultipleStates() {
        StateSet stateSet = new StateSet();
        
        stateSet.addState("q0");
        stateSet.addState("q1");
        stateSet.addState("q2");
        
        assertEquals(3, stateSet.size(), "StateSet should have size 3");
        assertTrue(stateSet.contains("q0"), "StateSet should contain q0");
        assertTrue(stateSet.contains("q1"), "StateSet should contain q1");
        assertTrue(stateSet.contains("q2"), "StateSet should contain q2");
    }
    
    @Test
    public void testAddAllStates() {
        StateSet stateSet = new StateSet();
        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");
        states.add("q2");
        
        stateSet.addAllStates(states);
        
        assertEquals(3, stateSet.size(), "StateSet should have size 3");
        assertTrue(stateSet.contains("q0"), "StateSet should contain q0");
        assertTrue(stateSet.contains("q1"), "StateSet should contain q1");
        assertTrue(stateSet.contains("q2"), "StateSet should contain q2");
    }
    
    @Test
    public void testAddAllStatesWithEmptySet() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        Set<String> emptyStates = new HashSet<>();
        
        stateSet.addAllStates(emptyStates);
        
        assertEquals(1, stateSet.size(), "StateSet should still have size 1");
        assertTrue(stateSet.contains("q0"), "StateSet should still contain q0");
    }
    
    @Test
    public void testAddAllStatesWithOverlap() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        stateSet.addState("q1");
        
        Set<String> newStates = new HashSet<>();
        newStates.add("q1");
        newStates.add("q2");
        
        stateSet.addAllStates(newStates);
        
        assertEquals(3, stateSet.size(), "StateSet should have size 3");
        assertTrue(stateSet.contains("q0"), "StateSet should contain q0");
        assertTrue(stateSet.contains("q1"), "StateSet should contain q1");
        assertTrue(stateSet.contains("q2"), "StateSet should contain q2");
    }
    
    @Test
    public void testContains() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        stateSet.addState("q1");
        
        assertTrue(stateSet.contains("q0"), "StateSet should contain q0");
        assertTrue(stateSet.contains("q1"), "StateSet should contain q1");
        assertFalse(stateSet.contains("q2"), "StateSet should not contain q2");
        assertFalse(stateSet.contains(""), "StateSet should not contain empty string");
    }
    
    @Test
    public void testContainsWithNull() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        
        assertFalse(stateSet.contains(null), "StateSet should not contain null");
    }
    
    @Test
    public void testGetStates() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        stateSet.addState("q1");
        stateSet.addState("q2");
        
        Set<String> states = stateSet.getStates();
        
        assertNotNull(states, "getStates should not return null");
        assertEquals(3, states.size(), "Returned set should have size 3");
        assertTrue(states.contains("q0"), "Returned set should contain q0");
        assertTrue(states.contains("q1"), "Returned set should contain q1");
        assertTrue(states.contains("q2"), "Returned set should contain q2");
    }
    
    @Test
    public void testGetStatesReturnsCopy() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        
        Set<String> states = stateSet.getStates();
        states.add("q1"); // Modify the returned set
        
        assertFalse(stateSet.contains("q1"), "Original StateSet should not contain q1");
        assertEquals(1, stateSet.size(), "Original StateSet should still have size 1");
    }
    
    @Test
    public void testIsEmpty() {
        StateSet stateSet = new StateSet();
        
        assertTrue(stateSet.isEmpty(), "New StateSet should be empty");
        
        stateSet.addState("q0");
        assertFalse(stateSet.isEmpty(), "StateSet should not be empty after adding state");
    }
    
    @Test
    public void testSize() {
        StateSet stateSet = new StateSet();
        
        assertEquals(0, stateSet.size(), "New StateSet should have size 0");
        
        stateSet.addState("q0");
        assertEquals(1, stateSet.size(), "StateSet should have size 1 after adding one state");
        
        stateSet.addState("q1");
        assertEquals(2, stateSet.size(), "StateSet should have size 2 after adding two states");
        
        stateSet.addState("q0"); // Duplicate
        assertEquals(2, stateSet.size(), "StateSet should still have size 2 after adding duplicate");
    }
    
    @Test
    public void testEqualsWithSameStateSet() {
        StateSet stateSet1 = new StateSet();
        stateSet1.addState("q0");
        stateSet1.addState("q1");
        
        StateSet stateSet2 = new StateSet();
        stateSet2.addState("q0");
        stateSet2.addState("q1");
        
        assertEquals(stateSet1, stateSet2, "StateSets with same states should be equal");
        assertEquals(stateSet1.hashCode(), stateSet2.hashCode(), "StateSets with same states should have same hash code");
    }
    
    @Test
    public void testEqualsWithDifferentOrder() {
        StateSet stateSet1 = new StateSet();
        stateSet1.addState("q0");
        stateSet1.addState("q1");
        
        StateSet stateSet2 = new StateSet();
        stateSet2.addState("q1");
        stateSet2.addState("q0");
        
        assertEquals(stateSet1, stateSet2, "StateSets with same states in different order should be equal");
    }
    
    @Test
    public void testEqualsWithDifferentStates() {
        StateSet stateSet1 = new StateSet();
        stateSet1.addState("q0");
        stateSet1.addState("q1");
        
        StateSet stateSet2 = new StateSet();
        stateSet2.addState("q0");
        stateSet2.addState("q2");
        
        assertNotEquals(stateSet1, stateSet2, "StateSets with different states should not be equal");
    }
    
    @Test
    public void testEqualsWithDifferentSizes() {
        StateSet stateSet1 = new StateSet();
        stateSet1.addState("q0");
        
        StateSet stateSet2 = new StateSet();
        stateSet2.addState("q0");
        stateSet2.addState("q1");
        
        assertNotEquals(stateSet1, stateSet2, "StateSets with different sizes should not be equal");
    }
    
    @Test
    public void testEqualsWithNull() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        
        assertNotEquals(stateSet, null, "StateSet should not be equal to null");
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        
        assertNotEquals(stateSet, "q0", "StateSet should not be equal to different class");
    }
    
    @Test
    public void testEqualsWithSelf() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        
        assertEquals(stateSet, stateSet, "StateSet should be equal to itself");
    }
    
    @Test
    public void testHashCodeConsistency() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        stateSet.addState("q1");
        
        int hashCode1 = stateSet.hashCode();
        int hashCode2 = stateSet.hashCode();
        
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent");
    }
    
    @Test
    public void testHashCodeWithSameStates() {
        StateSet stateSet1 = new StateSet();
        stateSet1.addState("q0");
        stateSet1.addState("q1");
        
        StateSet stateSet2 = new StateSet();
        stateSet2.addState("q0");
        stateSet2.addState("q1");
        
        assertEquals(stateSet1.hashCode(), stateSet2.hashCode(), "StateSets with same states should have same hash code");
    }
    
    @Test
    public void testHashCodeWithDifferentStates() {
        StateSet stateSet1 = new StateSet();
        stateSet1.addState("q0");
        
        StateSet stateSet2 = new StateSet();
        stateSet2.addState("q1");
        
        assertNotEquals(stateSet1.hashCode(), stateSet2.hashCode(), "StateSets with different states should have different hash codes");
    }
    
    @Test
    public void testToString() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0");
        stateSet.addState("q1");
        
        String toString = stateSet.toString();
        
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("q0"), "toString should contain q0");
        assertTrue(toString.contains("q1"), "toString should contain q1");
    }
    
    @Test
    public void testToStringWithEmptyStateSet() {
        StateSet stateSet = new StateSet();
        
        String toString = stateSet.toString();
        
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("[]"), "toString should contain empty set representation");
    }
    
    @Test
    public void testStateSetWithSpecialCharacters() {
        StateSet stateSet = new StateSet();
        stateSet.addState("q0_accepting");
        stateSet.addState("q1_final");
        
        assertTrue(stateSet.contains("q0_accepting"), "StateSet should contain state with special characters");
        assertTrue(stateSet.contains("q1_final"), "StateSet should contain state with special characters");
    }
    
    @Test
    public void testStateSetWithUnicodeCharacters() {
        StateSet stateSet = new StateSet();
        stateSet.addState("状态0");
        stateSet.addState("状态1");
        
        assertTrue(stateSet.contains("状态0"), "StateSet should contain state with Unicode characters");
        assertTrue(stateSet.contains("状态1"), "StateSet should contain state with Unicode characters");
    }
    
    @Test
    public void testStateSetWithEmptyString() {
        StateSet stateSet = new StateSet();
        stateSet.addState("");
        
        assertTrue(stateSet.contains(""), "StateSet should contain empty string");
        assertEquals(1, stateSet.size(), "StateSet should have size 1");
    }
    
    @Test
    public void testStateSetWithWhitespace() {
        StateSet stateSet = new StateSet();
        stateSet.addState("  q0  ");
        
        assertTrue(stateSet.contains("  q0  "), "StateSet should contain state with whitespace");
        assertEquals(1, stateSet.size(), "StateSet should have size 1");
    }
    
    @Test
    public void testStateSetInHashSet() {
        StateSet stateSet1 = new StateSet();
        stateSet1.addState("q0");
        
        StateSet stateSet2 = new StateSet();
        stateSet2.addState("q0");
        
        StateSet stateSet3 = new StateSet();
        stateSet3.addState("q1");
        
        Set<StateSet> stateSetSet = new HashSet<>();
        stateSetSet.add(stateSet1);
        stateSetSet.add(stateSet2);
        stateSetSet.add(stateSet3);
        
        assertEquals(2, stateSetSet.size(), "HashSet should contain 2 unique StateSets");
        assertTrue(stateSetSet.contains(stateSet1), "HashSet should contain stateSet1");
        assertTrue(stateSetSet.contains(stateSet2), "HashSet should contain stateSet2");
        assertTrue(stateSetSet.contains(stateSet3), "HashSet should contain stateSet3");
    }
    
    @Test
    public void testStateSetInHashMap() {
        StateSet stateSet1 = new StateSet();
        stateSet1.addState("q0");
        
        StateSet stateSet2 = new StateSet();
        stateSet2.addState("q0");
        
        StateSet stateSet3 = new StateSet();
        stateSet3.addState("q1");
        
        java.util.Map<StateSet, String> stateSetMap = new java.util.HashMap<>();
        stateSetMap.put(stateSet1, "value1");
        stateSetMap.put(stateSet2, "value2");
        stateSetMap.put(stateSet3, "value3");
        
        assertEquals(2, stateSetMap.size(), "HashMap should contain 2 unique StateSets");
        assertEquals("value2", stateSetMap.get(stateSet1), "HashMap should return value2 for stateSet1");
        assertEquals("value2", stateSetMap.get(stateSet2), "HashMap should return value2 for stateSet2");
        assertEquals("value3", stateSetMap.get(stateSet3), "HashMap should return value3 for stateSet3");
    }
}
