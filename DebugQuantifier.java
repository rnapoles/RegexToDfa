import org.example.*;

public class DebugQuantifier {
    public static void main(String[] args) {
        System.out.println("=== Debugging QA0 quantifier ===");
        
        // Test 1: Create a simple NFA for 'a'
        NFA nfaA = new NFA();
        String startA = "start_a";
        String acceptA = "accept_a";
        nfaA.addState(startA, false);
        nfaA.addState(acceptA, true);
        nfaA.setStartState(startA);
        nfaA.addTransition(startA, 'a', acceptA);
        
        System.out.println("1. Original NFA for 'a':");
        System.out.println("   Empty string: " + nfaA.accepts(""));
        System.out.println("   'a': " + nfaA.accepts("a"));
        
        // Test 2: Test star operation directly
        System.out.println("\n2. Testing star operation directly:");
        NFA nfaStar = nfaA.star();
        System.out.println("   Empty string: " + nfaStar.accepts(""));
        System.out.println("   'a': " + nfaStar.accepts("a"));
        System.out.println("   'aa': " + nfaStar.accepts("aa"));
        
        // Test 3: Convert to DFA
        System.out.println("\n3. Converting to DFA:");
        DFA dfaStar = nfaStar.toDFA();
        System.out.println("   Empty string: " + dfaStar.accepts(""));
        System.out.println("   'a': " + dfaStar.accepts("a"));
        System.out.println("   'aa': " + dfaStar.accepts("aa"));
    }
}
