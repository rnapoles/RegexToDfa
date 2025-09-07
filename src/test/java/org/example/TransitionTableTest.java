package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Test class for TransitionTable and related classes.
 * Tests the SOLID principles implementation and functionality.
 */
public class TransitionTableTest {
    
    private DFA testDFA;
    private TransitionTable transitionTable;
    
    @BeforeEach
    void setUp() {
        // Create a simple DFA for testing
        testDFA = new DFA("q0", Set.of("q2"));
        
        // Add states
        testDFA.addState("q0", false);
        testDFA.addState("q1", false);
        testDFA.addState("q2", true);
        
        // Add transitions
        testDFA.addTransition("q0", 'a', "q1");
        testDFA.addTransition("q0", 'b', "q2");
        testDFA.addTransition("q1", 'a', "q2");
        testDFA.addTransition("q1", 'b', "q0");
        testDFA.addTransition("q2", 'a', "q0");
        testDFA.addTransition("q2", 'b', "q1");
        
        // Build transition table
        transitionTable = TransitionTableBuilder.buildFromDFA(testDFA);
    }
    
    @Test
    void testTransitionEntryCreation() {
        TransitionEntry entry = new TransitionEntry("q0", 'a', "q1");
        
        assertEquals("q0", entry.getFromState());
        assertEquals('a', entry.getSymbol());
        assertEquals("q1", entry.getToState());
        assertTrue(entry.matches("q0", 'a'));
        assertFalse(entry.matches("q1", 'a'));
    }
    
    @Test
    void testTransitionEntryValidation() {
        assertThrows(IllegalArgumentException.class, () -> 
            new TransitionEntry(null, 'a', "q1"));
        assertThrows(IllegalArgumentException.class, () -> 
            new TransitionEntry("q0", 'a', null));
        assertThrows(IllegalArgumentException.class, () -> 
            new TransitionEntry("", 'a', "q1"));
    }
    
    @Test
    void testTransitionTableCreation() {
        assertNotNull(transitionTable);
        assertEquals(6, transitionTable.size());
        assertFalse(transitionTable.isEmpty());
        
        Set<String> expectedStates = Set.of("q0", "q1", "q2");
        assertEquals(expectedStates, transitionTable.getStates());
        
        Set<Character> expectedAlphabet = Set.of('a', 'b');
        assertEquals(expectedAlphabet, transitionTable.getAlphabet());
    }
    
    @Test
    void testTransitionTableLookup() {
        assertEquals("q1", transitionTable.getNextState("q0", 'a'));
        assertEquals("q2", transitionTable.getNextState("q0", 'b'));
        assertEquals("q2", transitionTable.getNextState("q1", 'a'));
        assertEquals("q0", transitionTable.getNextState("q1", 'b'));
        assertEquals("q0", transitionTable.getNextState("q2", 'a'));
        assertEquals("q1", transitionTable.getNextState("q2", 'b'));
        
        // Test non-existent transitions
        assertNull(transitionTable.getNextState("q0", 'c'));
        assertNull(transitionTable.getNextState("q3", 'a'));
    }
    
    @Test
    void testTransitionTableArray() {
        String[][] array = transitionTable.toArray();
        assertNotNull(array);
        
        // Verify array dimensions
        assertEquals(3, array.length); // 3 states
        assertEquals(2, array[0].length); // 2 symbols
        
        // Verify some specific transitions
        // Assuming alphabetical ordering: q0=0, q1=1, q2=2 and a=0, b=1
        List<String> stateList = new ArrayList<>(transitionTable.getStates());
        Collections.sort(stateList);
        List<Character> symbolList = new ArrayList<>(transitionTable.getAlphabet());
        Collections.sort(symbolList);
        
        int q0Index = stateList.indexOf("q0");
        int q1Index = stateList.indexOf("q1");
        int q2Index = stateList.indexOf("q2");
        int aIndex = symbolList.indexOf('a');
        int bIndex = symbolList.indexOf('b');
        
        assertEquals("q1", array[q0Index][aIndex]);
        assertEquals("q2", array[q0Index][bIndex]);
        assertEquals("q2", array[q1Index][aIndex]);
        assertEquals("q0", array[q1Index][bIndex]);
    }
    
    @Test
    void testTransitionTableBuilder() {
        // Test different builder methods
        TransitionTable sortedTable = TransitionTableBuilder.buildSortedFromDFA(testDFA);
        assertNotNull(sortedTable);
        assertEquals(6, sortedTable.size());
        
        TransitionTable optimizedTable = TransitionTableBuilder.buildLexerOptimizedFromDFA(testDFA);
        assertNotNull(optimizedTable);
        assertEquals(6, optimizedTable.size());
        
        // Test with custom ordering
        List<String> customStateOrder = Arrays.asList("q2", "q0", "q1");
        List<Character> customSymbolOrder = Arrays.asList('b', 'a');
        
        TransitionTable customTable = TransitionTableBuilder.buildFromDFAWithOrdering(testDFA, customStateOrder, customSymbolOrder);
        assertNotNull(customTable);
        assertEquals(6, customTable.size());
    }
    
    @Test
    void testTransitionTableBuilderValidation() {
        assertThrows(IllegalArgumentException.class, () -> 
            TransitionTableBuilder.buildFromDFA(null));
        assertThrows(IllegalArgumentException.class, () -> 
            TransitionTableBuilder.buildFromDFAWithOrdering(testDFA, null, Arrays.asList('a', 'b')));
        assertThrows(IllegalArgumentException.class, () -> 
            TransitionTableBuilder.buildFromDFAWithOrdering(testDFA, Arrays.asList("q0", "q1"), null));
    }
    
    @Test
    void testTransitionTableTransitionsFrom() {
        List<TransitionEntry> fromQ0 = transitionTable.getTransitionsFrom("q0");
        assertEquals(2, fromQ0.size());
        
        List<TransitionEntry> fromQ1 = transitionTable.getTransitionsFrom("q1");
        assertEquals(2, fromQ1.size());
        
        List<TransitionEntry> fromQ2 = transitionTable.getTransitionsFrom("q2");
        assertEquals(2, fromQ2.size());
    }
    
    @Test
    void testTransitionTableTransitionsTo() {
        List<TransitionEntry> toQ0 = transitionTable.getTransitionsTo("q0");
        assertEquals(2, toQ0.size());
        
        List<TransitionEntry> toQ1 = transitionTable.getTransitionsTo("q1");
        assertEquals(2, toQ1.size());
        
        List<TransitionEntry> toQ2 = transitionTable.getTransitionsTo("q2");
        assertEquals(2, toQ2.size());
    }
    
    @Test
    void testTransitionTableEquality() {
        TransitionTable table1 = TransitionTableBuilder.buildFromDFA(testDFA);
        TransitionTable table2 = TransitionTableBuilder.buildFromDFA(testDFA);
        
        assertEquals(table1, table2);
        assertEquals(table1.hashCode(), table2.hashCode());
    }
    
    @Test
    void testTransitionTableToString() {
        String tableString = transitionTable.toString();
        assertNotNull(tableString);
        assertTrue(tableString.contains("TransitionTable"));
        assertTrue(tableString.contains("q0"));
        assertTrue(tableString.contains("q1"));
        assertTrue(tableString.contains("q2"));
    }
}
