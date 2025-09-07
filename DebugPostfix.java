import org.example.*;

public class DebugPostfix {
    public static void main(String[] args) {
        System.out.println("=== Debugging postfix processing for a{0,} ===");
        
        // Test the postfix processing step by step
        String postfix = "aQA0";
        System.out.println("Postfix: " + postfix);
        
        // Manually build the NFA from postfix to see what happens
        java.util.Stack<NFA> nfaStack = new java.util.Stack<>();
        
        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);
            System.out.println("Processing char: '" + c + "' at index " + i);
            
            if (c == 'a') {
                // Create NFA for 'a'
                NFA nfaA = new NFA();
                String startA = "start_a";
                String acceptA = "accept_a";
                nfaA.addState(startA, false);
                nfaA.addState(acceptA, true);
                nfaA.setStartState(startA);
                nfaA.addTransition(startA, 'a', acceptA);
                nfaStack.push(nfaA);
                System.out.println("  Pushed NFA for 'a'");
            } else if (c == 'Q') {
                System.out.println("  Found quantifier operator");
                // Parse QA0
                StringBuilder quantifierOp = new StringBuilder();
                quantifierOp.append('Q');
                i++; // Skip the Q
                
                while (i < postfix.length() && (Character.isLetterOrDigit(postfix.charAt(i)) || postfix.charAt(i) == ',')) {
                    quantifierOp.append(postfix.charAt(i));
                    i++;
                }
                i--; // Back up one position
                
                String op = quantifierOp.toString();
                System.out.println("  Quantifier operator: " + op);
                
                if (nfaStack.isEmpty()) {
                    System.out.println("  ERROR: No operand for quantifier!");
                    return;
                }
                
                NFA operand = nfaStack.pop();
                System.out.println("  Popped operand NFA");
                
                if (op.startsWith("QA")) {
                    int n = Integer.parseInt(op.substring(2));
                    System.out.println("  Creating at-least quantifier with n=" + n);
                    NFA result = operand.star(); // This should be the same as createAtLeastQuantifierNFA(operand, 0)
                    nfaStack.push(result);
                    System.out.println("  Pushed result NFA");
                }
            }
        }
        
        if (nfaStack.isEmpty()) {
            System.out.println("ERROR: No NFA left on stack!");
            return;
        }
        
        NFA finalNFA = nfaStack.pop();
        System.out.println("\nFinal NFA test:");
        System.out.println("  Empty string: " + finalNFA.accepts(""));
        System.out.println("  'a': " + finalNFA.accepts("a"));
        System.out.println("  'aa': " + finalNFA.accepts("aa"));
        
        // Convert to DFA
        DFA finalDFA = finalNFA.toDFA();
        System.out.println("\nFinal DFA test:");
        System.out.println("  Empty string: " + finalDFA.accepts(""));
        System.out.println("  'a': " + finalDFA.accepts("a"));
        System.out.println("  'aa': " + finalDFA.accepts("aa"));
    }
}
