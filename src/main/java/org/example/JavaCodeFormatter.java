package org.example;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Utility class for formatting Java code to improve readability.
 * Provides methods to format generated code with proper indentation,
 * spacing, and line breaks.
 */
public class JavaCodeFormatter {
    
    private static final int INDENT_SIZE = 4;
    private static final String INDENT = " ".repeat(INDENT_SIZE);
    
    // Patterns for various Java constructs
    private static final Pattern BRACE_PATTERN = Pattern.compile("\\{");
    private static final Pattern CLOSE_BRACE_PATTERN = Pattern.compile("\\}");
    private static final Pattern SEMICOLON_PATTERN = Pattern.compile(";");
    private static final Pattern IF_PATTERN = Pattern.compile("\\bif\\s*\\(");
    private static final Pattern ELSE_PATTERN = Pattern.compile("\\belse\\b");
    private static final Pattern WHILE_PATTERN = Pattern.compile("\\bwhile\\s*\\(");
    private static final Pattern FOR_PATTERN = Pattern.compile("\\bfor\\s*\\(");
    private static final Pattern SWITCH_PATTERN = Pattern.compile("\\bswitch\\s*\\(");
    private static final Pattern CASE_PATTERN = Pattern.compile("\\bcase\\b");
    private static final Pattern DEFAULT_PATTERN = Pattern.compile("\\bdefault\\s*:");
    private static final Pattern TRY_PATTERN = Pattern.compile("\\btry\\s*\\{");
    private static final Pattern CATCH_PATTERN = Pattern.compile("\\bcatch\\s*\\(");
    private static final Pattern FINALLY_PATTERN = Pattern.compile("\\bfinally\\s*\\{");
    
    /**
     * Formats Java source code with proper indentation and spacing.
     * 
     * @param code the unformatted Java code
     * @return the formatted Java code
     */
    public static String format(String code) {
        if (code == null || code.trim().isEmpty()) {
            return code;
        }
        
        // Normalize line endings
        code = code.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
        
        // First, add basic spacing and line breaks
        code = addBasicSpacing(code);
        
        // Split into lines and process
        String[] lines = code.split("\n");
        List<String> formattedLines = new ArrayList<>();
        
        int indentLevel = 0;
        boolean inMultiLineComment = false;
        
        for (String line : lines) {
            String trimmedLine = line.trim();
            
            // Skip empty lines but preserve them
            if (trimmedLine.isEmpty()) {
                formattedLines.add("");
                continue;
            }
            
            // Handle multi-line comments
            if (inMultiLineComment) {
                formattedLines.add(INDENT.repeat(indentLevel) + trimmedLine);
                if (trimmedLine.contains("*/")) {
                    inMultiLineComment = false;
                }
                continue;
            }
            
            // Check for start of multi-line comment
            if (trimmedLine.startsWith("/*") && !trimmedLine.contains("*/")) {
                inMultiLineComment = true;
                formattedLines.add(INDENT.repeat(indentLevel) + trimmedLine);
                continue;
            }
            
            // Handle single-line comments
            if (trimmedLine.startsWith("//")) {
                formattedLines.add(INDENT.repeat(indentLevel) + trimmedLine);
                continue;
            }
            
            // Process the line for indentation
            String formattedLine = processLine(trimmedLine, indentLevel);
            
            // Adjust indent level for next line
            indentLevel = adjustIndentLevel(indentLevel, trimmedLine);
            
            formattedLines.add(formattedLine);
        }
        
        return String.join("\n", formattedLines);
    }
    
    /**
     * Adds basic spacing and line breaks to code.
     */
    private static String addBasicSpacing(String code) {
        // Add spaces around braces
        code = code.replaceAll("\\{", " {");
        code = code.replaceAll("\\}", "} ");
        
        // Add line breaks after semicolons (but not in for loops)
        code = code.replaceAll(";(?!\\s*[^\\s])", ";\n");
        
        // Add line breaks after closing braces
        code = code.replaceAll("\\}", "}\n");
        
        // Add line breaks after opening braces
        code = code.replaceAll("\\{", "{\n");
        
        // Clean up multiple newlines
        code = code.replaceAll("\n+", "\n");
        
        return code;
    }
    
    /**
     * Processes a single line for proper formatting.
     */
    private static String processLine(String line, int indentLevel) {
        // Apply base indentation
        String result = INDENT.repeat(indentLevel) + line;
        
        // Add spacing around operators and keywords
        result = addSpacingAroundOperators(result);
        result = addSpacingAroundKeywords(result);
        
        return result;
    }
    
    /**
     * Adds proper spacing around operators.
     */
    private static String addSpacingAroundOperators(String line) {
        // Don't modify string literals or comments
        if (line.contains("\"") || line.trim().startsWith("//")) {
            return line;
        }
        
        // Add spaces around assignment operators
        line = line.replaceAll("(?<!\\s)(=)(?!\\s)", " $1 ");
        
        // Add spaces around comparison operators
        line = line.replaceAll("(?<!\\s)([!<>]=?)(?!\\s)", " $1 ");
        
        // Add spaces around logical operators
        line = line.replaceAll("(?<!\\s)(&&|\\|\\|)(?!\\s)", " $1 ");
        
        // Add spaces around arithmetic operators (but not unary)
        line = line.replaceAll("(?<!\\s)([+\\-*/%])(?!\\s)", " $1 ");
        
        // Clean up multiple spaces
        line = line.replaceAll("\\s+", " ");
        
        return line;
    }
    
    /**
     * Adds proper spacing around keywords.
     */
    private static String addSpacingAroundKeywords(String line) {
        // Add spaces after keywords
        line = line.replaceAll("\\b(if|while|for|switch|catch|finally)\\s*\\(", "$1 (");
        line = line.replaceAll("\\b(else|try)\\s*\\{", "$1 {");
        
        // Add spaces around extends/implements
        line = line.replaceAll("\\bextends\\b", " extends ");
        line = line.replaceAll("\\bimplements\\b", " implements ");
        
        // Add spaces around commas
        line = line.replaceAll("\\s*,\\s*", ", ");
        
        return line;
    }
    
    /**
     * Adjusts the indentation level based on the current line.
     */
    private static int adjustIndentLevel(int currentLevel, String line) {
        int newLevel = currentLevel;
        
        // Increase indent for opening braces
        if (line.contains("{")) {
            newLevel++;
        }
        
        // Decrease indent for closing braces
        if (line.contains("}")) {
            newLevel--;
        }
        
        // Special handling for else, catch, finally
        if (line.matches(".*\\belse\\b.*") && !line.contains("{")) {
            newLevel++;
        }
        
        return Math.max(0, newLevel);
    }
    
    /**
     * Formats a method signature with proper spacing.
     */
    public static String formatMethodSignature(String signature) {
        if (signature == null) return "";
        
        // Add spaces around parentheses and commas
        signature = signature.replaceAll("\\s*,\\s*", ", ");
        signature = signature.replaceAll("\\s*\\(\\s*", " (");
        signature = signature.replaceAll("\\s*\\)\\s*", ")");
        
        return signature.trim();
    }
    
    /**
     * Formats a class declaration with proper spacing.
     */
    public static String formatClassDeclaration(String declaration) {
        if (declaration == null) return "";
        
        // Add spaces around extends/implements
        declaration = declaration.replaceAll("\\bextends\\b", " extends ");
        declaration = declaration.replaceAll("\\bimplements\\b", " implements ");
        
        // Add spaces around commas in implements clause
        declaration = declaration.replaceAll("\\s*,\\s*", ", ");
        
        // Clean up multiple spaces
        declaration = declaration.replaceAll("\\s+", " ");
        
        return declaration.trim();
    }
    
    /**
     * Formats variable declarations with proper spacing.
     */
    public static String formatVariableDeclaration(String declaration) {
        if (declaration == null) return "";
        
        // Add spaces around assignment operators
        declaration = declaration.replaceAll("\\s*=\\s*", " = ");
        
        return declaration.trim();
    }
    
    /**
     * Formats switch statements with proper indentation.
     */
    public static String formatSwitchStatement(String switchCode) {
        if (switchCode == null) return "";
        
        String[] lines = switchCode.split("\n");
        List<String> formattedLines = new ArrayList<>();
        
        int indentLevel = 0;
        boolean inSwitch = false;
        
        for (String line : lines) {
            String trimmedLine = line.trim();
            
            if (trimmedLine.startsWith("switch")) {
                inSwitch = true;
                formattedLines.add(INDENT.repeat(indentLevel) + trimmedLine);
                indentLevel++;
                continue;
            }
            
            if (trimmedLine.startsWith("case") || trimmedLine.startsWith("default")) {
                if (inSwitch) {
                    indentLevel = Math.max(0, indentLevel - 1);
                }
                formattedLines.add(INDENT.repeat(indentLevel) + trimmedLine);
                indentLevel++;
                continue;
            }
            
            if (trimmedLine.equals("}")) {
                if (inSwitch) {
                    indentLevel = Math.max(0, indentLevel - 1);
                    inSwitch = false;
                }
            }
            
            formattedLines.add(INDENT.repeat(indentLevel) + trimmedLine);
            
            if (trimmedLine.contains("{")) {
                indentLevel++;
            }
            if (trimmedLine.contains("}")) {
                indentLevel = Math.max(0, indentLevel - 1);
            }
        }
        
        return String.join("\n", formattedLines);
    }
    
    /**
     * Removes excessive blank lines while preserving structure.
     */
    public static String removeExcessiveBlankLines(String code) {
        if (code == null) return "";
        
        // Replace multiple consecutive newlines with at most two
        code = code.replaceAll("\n{3,}", "\n\n");
        
        // Remove trailing whitespace from lines
        code = code.replaceAll("(?m)\\s+$", "");
        
        return code;
    }
    
    /**
     * Formats the entire code with all formatting rules applied.
     */
    public static String formatComplete(String code) {
        if (code == null) return "";
        
        // Apply basic formatting steps
        code = formatBasic(code);
        code = removeExcessiveBlankLines(code);
        
        return code;
    }
    
    /**
     * Applies basic formatting to Java code.
     */
    public static String formatBasic(String code) {
        if (code == null || code.trim().isEmpty()) {
            return code;
        }
        
        // Normalize line endings
        code = code.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
        
        // Split into lines and process
        String[] lines = code.split("\n");
        List<String> formattedLines = new ArrayList<>();
        
        int indentLevel = 0;
        
        for (String line : lines) {
            String trimmedLine = line.trim();
            
            // Skip empty lines but preserve them
            if (trimmedLine.isEmpty()) {
                formattedLines.add("");
                continue;
            }
            
            // Handle comments
            if (trimmedLine.startsWith("//") || trimmedLine.startsWith("/*") || trimmedLine.startsWith("*")) {
                formattedLines.add(INDENT.repeat(indentLevel) + trimmedLine);
                continue;
            }
            
            // Apply basic spacing
            trimmedLine = addBasicSpacingToLine(trimmedLine);
            
            // Apply indentation
            String formattedLine = INDENT.repeat(indentLevel) + trimmedLine;
            
            // Adjust indent level for next line
            indentLevel = adjustIndentLevel(indentLevel, trimmedLine);
            
            formattedLines.add(formattedLine);
        }
        
        return String.join("\n", formattedLines);
    }
    
    /**
     * Adds basic spacing to a single line.
     */
    private static String addBasicSpacingToLine(String line) {
        // Add spaces around operators
        line = line.replaceAll("(?<!\\s)(=)(?!\\s)", " $1 ");
        line = line.replaceAll("(?<!\\s)([!<>]=?)(?!\\s)", " $1 ");
        line = line.replaceAll("(?<!\\s)(&&|\\|\\|)(?!\\s)", " $1 ");
        
        // Add spaces around braces
        line = line.replaceAll("(?<!\\s)(\\{)(?!\\s)", " $1");
        line = line.replaceAll("(?<!\\s)(\\})(?!\\s)", " $1");
        
        // Add spaces around parentheses
        line = line.replaceAll("(?<!\\s)(\\()(?!\\s)", " $1");
        line = line.replaceAll("(?<!\\s)(\\))(?!\\s)", " $1");
        
        // Add spaces around commas
        line = line.replaceAll("\\s*,\\s*", ", ");
        
        // Clean up multiple spaces
        line = line.replaceAll("\\s+", " ");
        
        return line.trim();
    }
}
