import org.example.*;

public class DebugEmptyString {
    public static void main(String[] args) {
        System.out.println("=== Debugging a{0,} empty string acceptance ===");
        
        // Test 1: Create a simple NFA for 'a'
        NFA nfaA = new NFA();
        String startA = "start_a";
        String acceptA = "accept_a";
        nfaA.addState(startA, false);
        nfaA.addState(acceptA, true);
        nfaA.setStartState(startA);
        nfaA.addTransition(startA, 'a', acceptA);
        
        System.out.println("1. Testing NFA for 'a':");
        System.out.println("   Empty string: " + nfaA.accepts(""));
        System.out.println("   'a': " + nfaA.accepts("a"));
        System.out.println("   'aa': " + nfaA.accepts("aa"));
        
        // Test 2: Create star NFA (a*)
        NFA nfaStar = nfaA.star();
        System.out.println("\n2. Testing NFA for 'a*' (star):");
        System.out.println("   Empty string: " + nfaStar.accepts(""));
        System.out.println("   'a': " + nfaStar.accepts("a"));
        System.out.println("   'aa': " + nfaStar.accepts("aa"));
        
        // Test 3: Convert to DFA
        DFA dfaStar = nfaStar.toDFA();
        System.out.println("\n3. Testing DFA for 'a*' (star):");
        System.out.println("   Empty string: " + dfaStar.accepts(""));
        System.out.println("   'a': " + dfaStar.accepts("a"));
        System.out.println("   'aa': " + dfaStar.accepts("aa"));
        
        // Test 4: Test the actual regex conversion
        System.out.println("\n4. Testing RegexToDFAConverter for 'a{0,}':");
        DFA dfaFromRegex = RegexToDFAConverter.convert("a{0,}");
        System.out.println("   Empty string: " + dfaFromRegex.accepts(""));
        System.out.println("   'a': " + dfaFromRegex.accepts("a"));
        System.out.println("   'aa': " + dfaFromRegex.accepts("aa"));
        
        // Test 5: Test with simple star
        System.out.println("\n5. Testing RegexToDFAConverter for 'a*':");
        DFA dfaStarRegex = RegexToDFAConverter.convert("a*");
        System.out.println("   Empty string: " + dfaStarRegex.accepts(""));
        System.out.println("   'a': " + dfaStarRegex.accepts("a"));
        System.out.println("   'aa': " + dfaStarRegex.accepts("aa"));
    }
}
