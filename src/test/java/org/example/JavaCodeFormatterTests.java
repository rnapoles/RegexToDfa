package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the JavaCodeFormatter class.
 */
public class JavaCodeFormatterTests {
    
    @Test
    public void testFormatBasicCode() {
        String unformattedCode = "public class Test{private String name;public void setName(String name){this.name=name;}}";
        
        String formattedCode = JavaCodeFormatter.formatComplete(unformattedCode);
        
        assertNotNull(formattedCode);
        assertTrue(formattedCode.contains("public class Test {"));
        assertTrue(formattedCode.contains("private String name;"));
        assertTrue(formattedCode.contains("public void setName(String name) {"));
        assertTrue(formattedCode.contains("this.name = name;"));
    }
    
    @Test
    public void testFormatMethodSignature() {
        String signature = "public void method(String param1,int param2)";
        
        String formatted = JavaCodeFormatter.formatMethodSignature(signature);
        
        assertTrue(formatted.contains("public void method (String param1, int param2)"));
    }
    
    @Test
    public void testFormatClassDeclaration() {
        String declaration = "public class Test extends BaseClass implements Interface1,Interface2";
        
        String formatted = JavaCodeFormatter.formatClassDeclaration(declaration);
        
        assertTrue(formatted.contains("public class Test extends BaseClass implements Interface1, Interface2"));
    }
    
    @Test
    public void testFormatVariableDeclaration() {
        String declaration = "String name=\"test\";";
        
        String formatted = JavaCodeFormatter.formatVariableDeclaration(declaration);
        
        assertEquals("String name = \"test\";", formatted);
    }
    
    @Test
    public void testFormatSwitchStatement() {
        String switchCode = "switch(value){case 1:return \"one\";case 2:return \"two\";default:return \"unknown\";}";
        
        String formatted = JavaCodeFormatter.formatSwitchStatement(switchCode);
        
        assertNotNull(formatted);
        assertTrue(formatted.contains("switch (value) {"));
        assertTrue(formatted.contains("case 1:"));
        assertTrue(formatted.contains("return \"one\";"));
        assertTrue(formatted.contains("case 2:"));
        assertTrue(formatted.contains("return \"two\";"));
        assertTrue(formatted.contains("default:"));
        assertTrue(formatted.contains("return \"unknown\";"));
    }
    
    @Test
    public void testRemoveExcessiveBlankLines() {
        String codeWithExcessiveLines = "line1\n\n\n\nline2\n\n\nline3";
        
        String formatted = JavaCodeFormatter.removeExcessiveBlankLines(codeWithExcessiveLines);
        
        assertTrue(formatted.contains("line1"));
        assertTrue(formatted.contains("line2"));
        assertTrue(formatted.contains("line3"));
        assertFalse(formatted.contains("\n\n\n"));
    }
    
    @Test
    public void testFormatWithComments() {
        String codeWithComments = "// This is a comment\npublic class Test{\n/* Multi-line\ncomment */\nprivate String name;}";
        
        String formatted = JavaCodeFormatter.formatComplete(codeWithComments);
        
        assertNotNull(formatted);
        assertTrue(formatted.contains("// This is a comment"));
        assertTrue(formatted.contains("/* Multi-line"));
        assertTrue(formatted.contains("comment */"));
        assertTrue(formatted.contains("public class Test {"));
        assertTrue(formatted.contains("private String name;"));
    }
    
    @Test
    public void testFormatEmptyCode() {
        String emptyCode = "";
        String nullCode = null;
        
        assertEquals("", JavaCodeFormatter.formatComplete(emptyCode));
        assertEquals("", JavaCodeFormatter.formatComplete(nullCode));
    }
}
