import org.example.*;

public class DebugEmptyDFA {
    public static void main(String[] args) {
        System.out.println("=== Debugging createEmptyStringDFA ===");
        
        // Test the createEmptyStringDFA method directly
        DFA emptyDFA = new DFA();
        String state = "q0";
        emptyDFA.addState(state, true); // accepting state
        emptyDFA = emptyDFA.setStartState(state);
        
        System.out.println("Empty DFA test:");
        System.out.println("  Empty string: " + emptyDFA.accepts(""));
        System.out.println("  'a': " + emptyDFA.accepts("a"));
        
        // Test the DFA's accepts method with empty string
        System.out.println("\nTesting DFA accepts method:");
        System.out.println("  Start state: " + emptyDFA.getStartState());
        System.out.println("  Accepting states: " + emptyDFA.getAcceptingStates());
        System.out.println("  All states: " + emptyDFA.getStates());
        
        // Test with a simple DFA that should accept 'a'
        DFA simpleDFA = new DFA();
        simpleDFA.addState("q0", false);
        simpleDFA.addState("q1", true);
        simpleDFA.addTransition("q0", 'a', "q1");
        simpleDFA = simpleDFA.setStartState("q0");
        
        System.out.println("\nSimple DFA test:");
        System.out.println("  Empty string: " + simpleDFA.accepts(""));
        System.out.println("  'a': " + simpleDFA.accepts("a"));
    }
}
