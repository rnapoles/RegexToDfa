package org.example;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * LexerGenerator generates lexer code from DFA and NFA using Pebble templates.
 * Supports generating Java lexer classes with token recognition capabilities.
 */
public class LexerGenerator {
    private final PebbleEngine engine;
    
    public LexerGenerator() {
        this.engine = new PebbleEngine.Builder().build();
    }
    
    /**
     * Generates a Java lexer class from a DFA.
     * 
     * @param dfa the DFA to generate lexer from
     * @param className the name of the generated lexer class
     * @param packageName the package name for the generated class
     * @return the generated Java source code
     * @throws IOException if template processing fails
     */
    public String generateLexerFromDFA(DFA dfa, String className, String packageName) throws IOException {
        Map<String, Object> context = createDFAContext(dfa, className, packageName);
        return processTemplate("dfa_lexer_template.peb", context);
    }
    
    /**
     * Generates a Java lexer class from an NFA.
     * 
     * @param nfa the NFA to generate lexer from
     * @param className the name of the generated lexer class
     * @param packageName the package name for the generated class
     * @return the generated Java source code
     * @throws IOException if template processing fails
     */
    public String generateLexerFromNFA(NFA nfa, String className, String packageName) throws IOException {
        Map<String, Object> context = createNFAContext(nfa, className, packageName);
        return processTemplate("nfa_lexer_template.peb", context);
    }
    
    /**
     * Generates a Java lexer class from a DFA with custom token types.
     * 
     * @param dfa the DFA to generate lexer from
     * @param className the name of the generated lexer class
     * @param packageName the package name for the generated class
     * @param tokenTypes map of accepting states to token type names
     * @return the generated Java source code
     * @throws IOException if template processing fails
     */
    public String generateLexerFromDFA(DFA dfa, String className, String packageName, 
                                     Map<String, String> tokenTypes) throws IOException {
        Map<String, Object> context = createDFAContext(dfa, className, packageName);
        context.put("tokenTypes", tokenTypes);
        return processTemplate("dfa_lexer_template.peb", context);
    }
    
    /**
     * Generates a Java lexer class from an NFA with custom token types.
     * 
     * @param nfa the NFA to generate lexer from
     * @param className the name of the generated lexer class
     * @param packageName the package name for the generated class
     * @param tokenTypes map of accepting states to token type names
     * @return the generated Java source code
     * @throws IOException if template processing fails
     */
    public String generateLexerFromNFA(NFA nfa, String className, String packageName, 
                                     Map<String, String> tokenTypes) throws IOException {
        Map<String, Object> context = createNFAContext(nfa, className, packageName);
        context.put("tokenTypes", tokenTypes);
        return processTemplate("nfa_lexer_template.peb", context);
    }
    
    private Map<String, Object> createDFAContext(DFA dfa, String className, String packageName) {
        Map<String, Object> context = new HashMap<>();
        context.put("className", className);
        context.put("packageName", packageName);
        context.put("states", dfa.getStates());
        context.put("alphabet", dfa.getAlphabet());
        context.put("transitions", dfa.getTransitions());
        context.put("startState", dfa.getStartState());
        context.put("acceptingStates", dfa.getAcceptingStates());
        context.put("isDFA", true);
        
        // Generate default token types if not provided
        Map<String, String> defaultTokenTypes = new HashMap<>();
        for (String state : dfa.getAcceptingStates()) {
            defaultTokenTypes.put(state, "TOKEN_" + state.toUpperCase());
        }
        context.put("tokenTypes", defaultTokenTypes);
        
        return context;
    }
    
    private Map<String, Object> createNFAContext(NFA nfa, String className, String packageName) {
        Map<String, Object> context = new HashMap<>();
        context.put("className", className);
        context.put("packageName", packageName);
        context.put("states", nfa.getStates());
        context.put("alphabet", nfa.getAlphabet());
        context.put("transitions", nfa.getTransitions());
        context.put("epsilonTransitions", nfa.getEpsilonTransitions());
        context.put("startState", nfa.getStartState());
        context.put("acceptingStates", nfa.getAcceptingStates());
        context.put("isDFA", false);
        
        // Generate default token types if not provided
        Map<String, String> defaultTokenTypes = new HashMap<>();
        for (String state : nfa.getAcceptingStates()) {
            defaultTokenTypes.put(state, "TOKEN_" + state.toUpperCase());
        }
        context.put("tokenTypes", defaultTokenTypes);
        
        return context;
    }
    
    private String processTemplate(String templateName, Map<String, Object> context) throws IOException {
        try {
            PebbleTemplate template = engine.getTemplate(templateName);
            Writer writer = new StringWriter();
            template.evaluate(writer, context);
            return writer.toString();
        } catch (Exception e) {
            throw new IOException("Failed to process template: " + templateName, e);
        }
    }

}
