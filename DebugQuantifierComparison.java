import org.example.*;

public class DebugQuantifierComparison {
    public static void main(String[] args) {
        System.out.println("=== Comparing operand.star() vs createAtLeastQuantifierNFA(operand, 0) ===");
        
        // Create NFA for 'a'
        NFA nfaA = new NFA();
        String startA = "start_a";
        String acceptA = "accept_a";
        nfaA.addState(startA, false);
        nfaA.addState(acceptA, true);
        nfaA.setStartState(startA);
        nfaA.addTransition(startA, 'a', acceptA);
        
        System.out.println("Original NFA for 'a':");
        System.out.println("  Start state: " + nfaA.getStartState());
        System.out.println("  Accepting states: " + nfaA.getAcceptingStates());
        System.out.println("  Empty string: " + nfaA.accepts(""));
        System.out.println("  'a': " + nfaA.accepts("a"));
        
        // Test 1: operand.star()
        System.out.println("\n1. Testing operand.star():");
        NFA nfaStar = nfaA.star();
        System.out.println("  Start state: " + nfaStar.getStartState());
        System.out.println("  Accepting states: " + nfaStar.getAcceptingStates());
        System.out.println("  Empty string: " + nfaStar.accepts(""));
        System.out.println("  'a': " + nfaStar.accepts("a"));
        System.out.println("  'aa': " + nfaStar.accepts("aa"));
        
        // Convert to DFA
        DFA dfaStar = nfaStar.toDFA();
        System.out.println("  DFA - Start state: " + dfaStar.getStartState());
        System.out.println("  DFA - Accepting states: " + dfaStar.getAcceptingStates());
        System.out.println("  DFA - Empty string: " + dfaStar.accepts(""));
        System.out.println("  DFA - 'a': " + dfaStar.accepts("a"));
        System.out.println("  DFA - 'aa': " + dfaStar.accepts("aa"));
        
        // Test 2: createAtLeastQuantifierNFA(operand, 0)
        System.out.println("\n2. Testing createAtLeastQuantifierNFA(operand, 0):");
        // We can't call this directly since it's private, but we can test the postfix processing
        System.out.println("  This should be equivalent to operand.star() when n=0");
        
        // Test 3: Test the actual postfix processing
        System.out.println("\n3. Testing postfix processing 'aQA0':");
        // This is what the converter does internally
        System.out.println("  The converter processes 'aQA0' which should call createAtLeastQuantifierNFA(operand, 0)");
        System.out.println("  Which should be equivalent to operand.star()");
    }
}
