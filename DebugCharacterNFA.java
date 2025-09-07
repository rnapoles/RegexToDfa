import org.example.*;

public class DebugCharacterNFA {
    public static void main(String[] args) {
        System.out.println("=== Comparing manual NFA vs createCharacterNFA ===");
        
        // Test 1: Manual NFA creation
        System.out.println("1. Manual NFA creation:");
        NFA nfaManual = new NFA();
        String startA = "start_a";
        String acceptA = "accept_a";
        nfaManual.addState(startA, false);
        nfaManual.addState(acceptA, true);
        nfaManual.setStartState(startA);
        nfaManual.addTransition(startA, 'a', acceptA);
        
        System.out.println("  Start state: " + nfaManual.getStartState());
        System.out.println("  Accepting states: " + nfaManual.getAcceptingStates());
        System.out.println("  Empty string: " + nfaManual.accepts(""));
        System.out.println("  'a': " + nfaManual.accepts("a"));
        
        // Apply star operation
        NFA nfaStarManual = nfaManual.star();
        System.out.println("  After star - Start state: " + nfaStarManual.getStartState());
        System.out.println("  After star - Accepting states: " + nfaStarManual.getAcceptingStates());
        System.out.println("  After star - Empty string: " + nfaStarManual.accepts(""));
        System.out.println("  After star - 'a': " + nfaStarManual.accepts("a"));
        
        // Convert to DFA
        DFA dfaManual = nfaStarManual.toDFA();
        System.out.println("  DFA - Start state: " + dfaManual.getStartState());
        System.out.println("  DFA - Accepting states: " + dfaManual.getAcceptingStates());
        System.out.println("  DFA - Empty string: " + dfaManual.accepts(""));
        System.out.println("  DFA - 'a': " + dfaManual.accepts("a"));
        
        // Test 2: Test the actual converter
        System.out.println("\n2. Testing RegexToDFAConverter for 'a{0,}':");
        DFA dfaFromRegex = RegexToDFAConverter.convert("a{0,}");
        System.out.println("  Start state: " + dfaFromRegex.getStartState());
        System.out.println("  Accepting states: " + dfaFromRegex.getAcceptingStates());
        System.out.println("  Empty string: " + dfaFromRegex.accepts(""));
        System.out.println("  'a': " + dfaFromRegex.accepts("a"));
    }
}
