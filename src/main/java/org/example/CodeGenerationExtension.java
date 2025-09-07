package org.example;

import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.*;

/**
 * Pebble extension that provides custom functions and filters for code generation.
 * Adds domain-specific functionality for AST generation templates.
 */
public class CodeGenerationExtension extends AbstractExtension {

    @Override
    public Map<String, Function> getFunctions() {
        Map<String, Function> functions = new HashMap<>();

        functions.put("camelCase", new CamelCaseFunction());
        functions.put("pascalCase", new PascalCaseFunction());
        functions.put("indent", new IndentFunction());
        functions.put("javadoc", new JavadocFunction());
        functions.put("isKeyword", new IsKeywordFunction());
        functions.put("formatCode", new FormatCodeFunction());
        functions.put("formatMethod", new FormatMethodFunction());
        functions.put("formatClass", new FormatClassFunction());
        functions.put("formatVariable", new FormatVariableFunction());

        return functions;
    }

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filters = new HashMap<>();

        filters.put("camelCase", new CamelCaseFilter());
        filters.put("pascalCase", new PascalCaseFilter());
        filters.put("indent", new IndentFilter());
        filters.put("javadoc", new JavadocFilter());
        filters.put("capitalize", new CapitalizeFilter());
        filters.put("quote", new QuoteFilter());
        filters.put("formatCode", new FormatCodeFilter());
        filters.put("formatMethod", new FormatMethodFilter());
        filters.put("formatClass", new FormatClassFilter());
        filters.put("formatVariable", new FormatVariableFilter());

        return filters;
    }

    /**
     * Function to convert text to camelCase
     */
    public static class CamelCaseFunction implements Function {
        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String input = (String) args.get("input");
            if (input == null) return "";
            return toCamelCase(input);
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("input");
        }
    }

    /**
     * Function to convert text to PascalCase
     */
    public static class PascalCaseFunction implements Function {
        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String input = (String) args.get("input");
            if (input == null) return "";
            return toPascalCase(input);
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("input");
        }
    }

    /**
     * Function to add indentation to text
     */
    public static class IndentFunction implements Function {
        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String text = (String) args.get("text");
            Integer level = (Integer) args.get("level");
            String indentChar = (String) args.get("indentChar");

            if (text == null) return "";
            if (level == null) level = 1;
            if (indentChar == null) indentChar = "    "; // 4 spaces

            String indent = indentChar.repeat(level);
            return text.replaceAll("(?m)^", indent);
        }

        @Override
        public List<String> getArgumentNames() {
            return Arrays.asList("text", "level", "indentChar");
        }
    }

    /**
     * Function to create JavaDoc comments
     */
    public static class JavadocFunction implements Function {
        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String text = (String) args.get("text");
            Integer indentLevel = (Integer) args.get("indentLevel");

            if (text == null) return "";
            if (indentLevel == null) indentLevel = 1;

            String indent = "    ".repeat(indentLevel);
            String[] lines = text.split("\\n");
            StringBuilder result = new StringBuilder();

            result.append(indent).append("/**\n");
            for (String line : lines) {
                result.append(indent).append(" * ").append(line.trim()).append("\n");
            }
            result.append(indent).append(" */");

            return result.toString();
        }

        @Override
        public List<String> getArgumentNames() {
            return Arrays.asList("text", "indentLevel");
        }
    }

    /**
     * Function to check if a string is a Java keyword
     */
    public static class IsKeywordFunction implements Function {
        private static final Set<String> JAVA_KEYWORDS = Set.of(
                "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
                "class", "const", "continue", "default", "do", "double", "else", "enum",
                "extends", "final", "finally", "float", "for", "goto", "if", "implements",
                "import", "instanceof", "int", "interface", "long", "native", "new",
                "package", "private", "protected", "public", "return", "short", "static",
                "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
                "transient", "try", "void", "volatile", "while"
        );

        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String word = (String) args.get("word");
            if (word == null) return false;
            return JAVA_KEYWORDS.contains(word.toLowerCase());
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("word");
        }
    }

    // FILTERS

    /**
     * Filter to convert text to camelCase
     */
    public static class CamelCaseFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "";
            return toCamelCase(input.toString());
        }

        @Override
        public List<String> getArgumentNames() {
            return null;
        }
    }

    /**
     * Filter to convert text to PascalCase
     */
    public static class PascalCaseFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "";
            return toPascalCase(input.toString());
        }

        @Override
        public List<String> getArgumentNames() {
            return null;
        }
    }

    /**
     * Filter to add indentation to text
     */
    public static class IndentFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "";

            String text = input.toString();
            Integer level = (Integer) args.get("level");
            String indentChar = (String) args.get("indentChar");

            if (level == null) level = 1;
            if (indentChar == null) indentChar = "    ";

            String indent = indentChar.repeat(level);
            return text.replaceAll("(?m)^", indent);
        }

        @Override
        public List<String> getArgumentNames() {
            return Arrays.asList("level", "indentChar");
        }
    }

    /**
     * Filter to create JavaDoc format
     */
    public static class JavadocFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "";

            String text = input.toString();
            Integer indentLevel = (Integer) args.get("indentLevel");

            if (indentLevel == null) indentLevel = 1;

            String indent = "    ".repeat(indentLevel);
            String[] lines = text.split("\\n");
            StringBuilder result = new StringBuilder();

            result.append(indent).append("/**\n");
            for (String line : lines) {
                result.append(indent).append(" * ").append(line.trim()).append("\n");
            }
            result.append(indent).append(" */");

            return result.toString();
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("indentLevel");
        }
    }

    /**
     * Filter to capitalize first letter
     */
    public static class CapitalizeFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "";
            String text = input.toString();
            if (text.isEmpty()) return text;
            return Character.toUpperCase(text.charAt(0)) + text.substring(1);
        }

        @Override
        public List<String> getArgumentNames() {
            return null;
        }
    }

    /**
     * Filter to add quotes around text
     */
    public static class QuoteFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "\"\"";
            String quoteChar = (String) args.get("quoteChar");
            if (quoteChar == null) quoteChar = "\"";
            return quoteChar + input + quoteChar;
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("quoteChar");
        }
    }

    // UTILITY METHODS

    /**
     * Converts a string to camelCase
     */
    private static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) return input;

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : input.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                if (capitalizeNext || (result.isEmpty() && Character.isUpperCase(c))) {
                    result.append(result.isEmpty() ? Character.toLowerCase(c) : Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            } else {
                capitalizeNext = true;
            }
        }

        return result.toString();
    }

    /**
     * Converts a string to PascalCase
     */
    private static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) return input;

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            } else {
                capitalizeNext = true;
            }
        }

        return result.toString();
    }

    // NEW FORMATTING FUNCTIONS

    /**
     * Function to format Java code
     */
    public static class FormatCodeFunction implements Function {
        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String code = (String) args.get("code");
            if (code == null) return "";
            return JavaCodeFormatter.formatComplete(code);
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("code");
        }
    }

    /**
     * Function to format method signatures
     */
    public static class FormatMethodFunction implements Function {
        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String signature = (String) args.get("signature");
            if (signature == null) return "";
            return JavaCodeFormatter.formatMethodSignature(signature);
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("signature");
        }
    }

    /**
     * Function to format class declarations
     */
    public static class FormatClassFunction implements Function {
        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String declaration = (String) args.get("declaration");
            if (declaration == null) return "";
            return JavaCodeFormatter.formatClassDeclaration(declaration);
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("declaration");
        }
    }

    /**
     * Function to format variable declarations
     */
    public static class FormatVariableFunction implements Function {
        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String declaration = (String) args.get("declaration");
            if (declaration == null) return "";
            return JavaCodeFormatter.formatVariableDeclaration(declaration);
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("declaration");
        }
    }

    // NEW FORMATTING FILTERS

    /**
     * Filter to format Java code
     */
    public static class FormatCodeFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "";
            return JavaCodeFormatter.formatComplete(input.toString());
        }

        @Override
        public List<String> getArgumentNames() {
            return null;
        }
    }

    /**
     * Filter to format method signatures
     */
    public static class FormatMethodFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "";
            return JavaCodeFormatter.formatMethodSignature(input.toString());
        }

        @Override
        public List<String> getArgumentNames() {
            return null;
        }
    }

    /**
     * Filter to format class declarations
     */
    public static class FormatClassFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "";
            return JavaCodeFormatter.formatClassDeclaration(input.toString());
        }

        @Override
        public List<String> getArgumentNames() {
            return null;
        }
    }

    /**
     * Filter to format variable declarations
     */
    public static class FormatVariableFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                            EvaluationContext context, int lineNumber) {
            if (input == null) return "";
            return JavaCodeFormatter.formatVariableDeclaration(input.toString());
        }

        @Override
        public List<String> getArgumentNames() {
            return null;
        }
    }
}