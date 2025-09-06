public class DebugTest {
    public static void main(String[] args) {
        try {
            String regex = "[a-z]+[0-9]*";
            System.out.println("Original regex: " + regex);
            
            // Let's manually trace through the conversion
            String processed = preprocessRegex(regex);
            System.out.println("Processed regex: " + processed);
            
            String postfix = infixToPostfix(processed);
            System.out.println("Postfix: " + postfix);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Copy the methods from RegexToDFAConverter for debugging
    // ... (we'll need to copy the relevant methods)
}