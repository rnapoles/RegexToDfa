import org.example.*;

public class DebugStar {
    public static void main(String[] args) {
        System.out.println("=== Debugging a* step by step ===");
        
        // Step 1: Create NFA for 'a'
        System.out.println("1. Creating NFA for 'a':");
        NFA nfaA = new NFA();
        String startA = "start_a";
        String acceptA = "accept_a";
        nfaA.addState(startA, false);
        nfaA.addState(acceptA, true);
        nfaA.setStartState(startA);
        nfaA.addTransition(startA, 'a', acceptA);
        
        System.out.println("   Start state: " + nfaA.getStartState());
        System.out.println("   Accepting states: " + nfaA.getAcceptingStates());
        System.out.println("   Empty string: " + nfaA.accepts(""));
        System.out.println("   'a': " + nfaA.accepts("a"));
        
        // Step 2: Apply star operation
        System.out.println("\n2. Applying star operation:");
        NFA nfaStar = nfaA.star();
        System.out.println("   Start state: " + nfaStar.getStartState());
        System.out.println("   Accepting states: " + nfaStar.getAcceptingStates());
        System.out.println("   Empty string: " + nfaStar.accepts(""));
        System.out.println("   'a': " + nfaStar.accepts("a"));
        System.out.println("   'aa': " + nfaStar.accepts("aa"));
        
        // Step 3: Convert to DFA
        System.out.println("\n3. Converting to DFA:");
        DFA dfaStar = nfaStar.toDFA();
        System.out.println("   Start state: " + dfaStar.getStartState());
        System.out.println("   Accepting states: " + dfaStar.getAcceptingStates());
        System.out.println("   Empty string: " + dfaStar.accepts(""));
        System.out.println("   'a': " + dfaStar.accepts("a"));
        System.out.println("   'aa': " + dfaStar.accepts("aa"));
        
        // Step 4: Test the actual regex conversion
        System.out.println("\n4. Testing RegexToDFAConverter for 'a*':");
        DFA dfaFromRegex = RegexToDFAConverter.convert("a*");
        System.out.println("   Start state: " + dfaFromRegex.getStartState());
        System.out.println("   Accepting states: " + dfaFromRegex.getAcceptingStates());
        System.out.println("   Empty string: " + dfaFromRegex.accepts(""));
        System.out.println("   'a': " + dfaFromRegex.accepts("a"));
        System.out.println("   'aa': " + dfaFromRegex.accepts("aa"));
    }
}
