package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for escape sequences: \s, \t, \n, \r, \x, \\
 * Tests various escape sequences and their combinations.
 */
public class EscapeSequenceTests extends BaseRegexToDFATest {
    
    @Test
    public void testWhitespaceEscape() {
        convertRegex("\\s");
        
        assertMultipleTestCases(
            testCase(" ", true),     // space
            testCase("\t", true),    // tab
            testCase("\n", true),    // newline
            testCase("\r", true),    // carriage return
            testCase("", false),     // empty string
            testCase("a", false),     // letter
            testCase("0", false),     // digit
            testCase("  ", false),   // multiple spaces
            testCase("a ", false)    // mixed
        );
    }
    
    @Test
    public void testTabEscape() {
        convertRegex("\\t");
        
        assertMultipleTestCases(
            testCase("\t", true),    // tab
            testCase("", false),     // empty string
            testCase(" ", false),    // space
            testCase("\n", false),   // newline
            testCase("\r", false),   // carriage return
            testCase("a", false),    // letter
            testCase("\t\t", false), // multiple tabs
            testCase("a\t", false)   // mixed
        );
    }
    
    @Test
    public void testNewlineEscape() {
        convertRegex("\\n");
        
        assertMultipleTestCases(
            testCase("\n", true),    // newline
            testCase("", false),     // empty string
            testCase(" ", false),    // space
            testCase("\t", false),   // tab
            testCase("\r", false),   // carriage return
            testCase("a", false),    // letter
            testCase("\n\n", false), // multiple newlines
            testCase("a\n", false)   // mixed
        );
    }
    
    @Test
    public void testCarriageReturnEscape() {
        convertRegex("\\r");
        
        assertMultipleTestCases(
            testCase("\r", true),    // carriage return
            testCase("", false),     // empty string
            testCase(" ", false),    // space
            testCase("\t", false),   // tab
            testCase("\n", false),   // newline
            testCase("a", false),    // letter
            testCase("\r\r", false), // multiple carriage returns
            testCase("a\r", false)   // mixed
        );
    }
    
    @Test
    public void testBackslashEscape() {
        convertRegex("\\\\");
        
        assertMultipleTestCases(
            testCase("\\", true),    // literal backslash
            testCase("", false),     // empty string
            testCase("a", false),    // letter
            testCase("\\\\", false), // multiple backslashes
            testCase("a\\", false)   // mixed
        );
    }
    
    @Test
    public void testHexEscape() {
        convertRegex("\\x41"); // ASCII 'A'
        
        assertMultipleTestCases(
            testCase("A", true),     // ASCII 65 (0x41)
            testCase("", false),     // empty string
            testCase("a", false),    // lowercase
            testCase("B", false),    // different uppercase
            testCase("AA", false),   // multiple characters
            testCase("AB", false)    // multiple characters
        );
    }
    
    @Test
    public void testHexEscapeLowercase() {
        convertRegex("\\x61"); // ASCII 'a'
        
        assertMultipleTestCases(
            testCase("a", true),     // ASCII 97 (0x61)
            testCase("", false),     // empty string
            testCase("A", false),    // uppercase
            testCase("b", false),    // different lowercase
            testCase("aa", false),   // multiple characters
            testCase("ab", false)    // multiple characters
        );
    }
    
    @Test
    public void testHexEscapeDigit() {
        convertRegex("\\x30"); // ASCII '0'
        
        assertMultipleTestCases(
            testCase("0", true),     // ASCII 48 (0x30)
            testCase("", false),     // empty string
            testCase("1", false),    // different digit
            testCase("a", false),    // letter
            testCase("00", false),   // multiple characters
            testCase("01", false)    // multiple characters
        );
    }
    
    @Test
    public void testHexRange() {
        convertRegex("[\\x41-\\x43]"); // ASCII 'A' to 'C'
        
        assertMultipleTestCases(
            testCase("A", true),     // ASCII 65 (0x41)
            testCase("B", true),     // ASCII 66 (0x42)
            testCase("C", true),     // ASCII 67 (0x43)
            testCase("", false),     // empty string
            testCase("D", false),    // ASCII 68 (0x44) - out of range
            testCase("a", false),    // lowercase
            testCase("AB", false),   // multiple characters
            testCase("AC", false)    // multiple characters
        );
    }
    
    @Test
    public void testEscapeSequenceWithQuantifier() {
        convertRegex("\\s*");
        
        assertMultipleTestCases(
            testCase("", true),      // zero whitespace characters
            testCase(" ", true),     // one space
            testCase("\t", true),    // one tab
            testCase("\n", true),    // one newline
            testCase("\r", true),    // one carriage return
            testCase(" \t\n\r", true), // mixed whitespace
            testCase("a", false),    // letter
            testCase(" a", false),   // whitespace then letter
            testCase("a ", false)    // letter then whitespace
        );
    }
    
    @Test
    public void testEscapeSequenceWithPlus() {
        convertRegex("\\s+");
        
        assertMultipleTestCases(
            testCase("", false),     // zero whitespace characters - should reject
            testCase(" ", true),     // one space
            testCase("\t", true),    // one tab
            testCase("\n", true),    // one newline
            testCase("\r", true),    // one carriage return
            testCase(" \t\n\r", true), // mixed whitespace
            testCase("a", false),    // letter
            testCase(" a", false),   // whitespace then letter
            testCase("a ", false)    // letter then whitespace
        );
    }
    
    @Test
    public void testEscapeSequenceWithQuestion() {
        convertRegex("\\s?");
        
        assertMultipleTestCases(
            testCase("", true),      // zero whitespace characters
            testCase(" ", true),     // one space
            testCase("\t", true),    // one tab
            testCase("\n", true),    // one newline
            testCase("\r", true),    // one carriage return
            testCase("  ", false),   // two spaces - should reject
            testCase("\t\t", false), // two tabs - should reject
            testCase("a", false),    // letter
            testCase(" a", false),   // whitespace then letter
            testCase("a ", false)    // letter then whitespace
        );
    }
    
    @Test
    public void testEscapeSequenceWithConcatenation() {
        convertRegex("\\sa");
        
        assertMultipleTestCases(
            testCase(" a", true),    // space then 'a'
            testCase("\ta", true),   // tab then 'a'
            testCase("\na", true),   // newline then 'a'
            testCase("\ra", true),   // carriage return then 'a'
            testCase("", false),     // empty string
            testCase("a", false),    // just 'a'
            testCase(" ", false),    // just space
            testCase("aa", false),   // 'a' then 'a'
            testCase(" a ", false),  // space, 'a', space
            testCase("ab", false)    // space then 'b'
        );
    }
    
    @Test
    public void testEscapeSequenceWithUnion() {
        convertRegex("\\s|\\t");
        
        assertMultipleTestCases(
            testCase(" ", true),     // space
            testCase("\t", true),   // tab
            testCase("\n", true),   // newline
            testCase("\r", true),   // carriage return
            testCase("", false),     // empty string
            testCase("a", false),    // letter
            testCase("0", false),    // digit
            testCase(" \t", false),  // space then tab
            testCase("a ", false)    // letter then space
        );
    }
    
    @Test
    public void testComplexEscapePattern() {
        convertRegex("\\s+[a-z]\\s*");
        
        assertMultipleTestCases(
            testCase(" a", true),    // space, 'a', zero spaces
            testCase(" \ta", true), // space+tab, 'a', zero spaces
            testCase(" a ", true),   // space, 'a', space
            testCase(" \ta \t", true), // space+tab, 'a', space+tab
            testCase("", false),     // empty string
            testCase("a", false),    // no leading whitespace
            testCase(" a ", false),  // space, 'a', space (but 'a' is not lowercase in this context)
            testCase(" A", false),   // space, 'A' (uppercase)
            testCase(" 0", false),   // space, '0' (digit)
            testCase("ab", false),   // no leading whitespace
            testCase(" a b", false)  // space, 'a', space, 'b'
        );
    }
    
    @Test
    public void testInvalidHexEscape() {
        assertThrowsIllegalArgumentException("\\xGG", "Invalid hex character");
    }
    
    @Test
    public void testIncompleteHexEscape() {
        assertThrowsIllegalArgumentException("\\x4", "Invalid hex character");
    }
    
    @Test
    public void testIncompleteEscapeSequence() {
        assertThrowsIllegalArgumentException("\\", "Incomplete escape sequence");
    }
    
    @Test
    public void testInvalidHexRange() {
        assertThrowsIllegalArgumentException("[\\x50-\\x40]", "Invalid hex range");
    }
}
