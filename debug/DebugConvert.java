import org.example.*;

public class DebugConvert {
    public static void main(String[] args) {
        System.out.println("=== Debugging RegexToDFAConverter.convert() step by step ===");
        
        // Test the convert method directly
        System.out.println("Testing RegexToDFAConverter.convert(\"a{0,}\"):");
        DFA dfa = RegexToDFAConverter.convert("a{0,}");
        
        System.out.println("Result:");
        System.out.println("  Start state: " + dfa.getStartState());
        System.out.println("  Accepting states: " + dfa.getAcceptingStates());
        System.out.println("  Empty string: " + dfa.accepts(""));
        System.out.println("  'a': " + dfa.accepts("a"));
        System.out.println("  'aa': " + dfa.accepts("aa"));
        
        // Test with a simple case that should work
        System.out.println("\nTesting RegexToDFAConverter.convert(\"a*\"):");
        DFA dfaStar = RegexToDFAConverter.convert("a*");
        
        System.out.println("Result:");
        System.out.println("  Start state: " + dfaStar.getStartState());
        System.out.println("  Accepting states: " + dfaStar.getAcceptingStates());
        System.out.println("  Empty string: " + dfaStar.accepts(""));
        System.out.println("  'a': " + dfaStar.accepts("a"));
        System.out.println("  'aa': " + dfaStar.accepts("aa"));
    }
}
