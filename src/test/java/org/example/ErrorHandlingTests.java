package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for error handling and invalid regex patterns.
 * Tests various invalid inputs and ensures proper exception handling.
 */
public class ErrorHandlingTests extends BaseRegexToDFATest {
    
    @Test
    public void testMismatchedParentheses() {
        assertThrowsIllegalArgumentException("(a", "Mismatched parentheses");
    }
    
    @Test
    public void testMismatchedParenthesesClosing() {
        assertThrowsIllegalArgumentException("a)", "Mismatched parentheses");
    }
    
    @Test
    public void testMultipleMismatchedParentheses() {
        assertThrowsIllegalArgumentException("((a)", "Mismatched parentheses");
    }
    
    @Test
    public void testNestedMismatchedParentheses() {
        assertThrowsIllegalArgumentException("((a))", "Mismatched parentheses");
    }
    
    @Test
    public void testUnclosedCharacterClass() {
        assertThrowsIllegalArgumentException("[abc", "Unclosed character class");
    }
    
    @Test
    public void testUnclosedCharacterClassInMiddle() {
        assertThrowsIllegalArgumentException("a[bc", "Unclosed character class");
    }
    
    @Test
    public void testEmptyCharacterClass() {
        assertThrowsIllegalArgumentException("[]", "Invalid regex");
    }
    
    @Test
    public void testInvalidCharacterRange() {
        assertThrowsIllegalArgumentException("[z-a]", "Invalid character range");
    }
    
    @Test
    public void testInvalidHexRange() {
        assertThrowsIllegalArgumentException("[\\x50-\\x40]", "Invalid hex range");
    }
    
    @Test
    public void testInvalidHexCharacter() {
        assertThrowsIllegalArgumentException("\\xGG", "Invalid hex character");
    }
    
    @Test
    public void testIncompleteHexCharacter() {
        assertThrowsIllegalArgumentException("\\x4", "Invalid hex character");
    }
    
    @Test
    public void testIncompleteEscapeSequence() {
        assertThrowsIllegalArgumentException("\\", "Incomplete escape sequence");
    }
    
    @Test
    public void testIncompleteEscapeSequenceAtEnd() {
        assertThrowsIllegalArgumentException("abc\\", "Incomplete escape sequence");
    }
    
    @Test
    public void testIncompleteEscapeSequenceInCharacterClass() {
        assertThrowsIllegalArgumentException("[abc\\", "Incomplete escape sequence");
    }
    
    @Test
    public void testStarWithoutOperand() {
        assertThrowsIllegalArgumentException("*", "Invalid regex: * without operand");
    }
    
    @Test
    public void testPlusWithoutOperand() {
        assertThrowsIllegalArgumentException("+", "Invalid regex: + without operand");
    }
    
    @Test
    public void testQuestionWithoutOperand() {
        assertThrowsIllegalArgumentException("?", "Invalid regex: ? without operand");
    }
    
    @Test
    public void testUnionWithoutOperands() {
        assertThrowsIllegalArgumentException("|", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testUnionWithOneOperand() {
        assertThrowsIllegalArgumentException("a|", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testUnionWithOneOperandAtStart() {
        assertThrowsIllegalArgumentException("|a", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testConcatenationWithoutOperands() {
        assertThrowsIllegalArgumentException(".", "Invalid regex: concatenation requires two operands");
    }
    
    @Test
    public void testEmptyExpression() {
        assertThrowsIllegalArgumentException("", "Invalid regex: empty expression");
    }
    
    @Test
    public void testTooManyOperands() {
        assertThrowsIllegalArgumentException("abc", "Invalid regex: too many operands");
    }
    
    @Test
    public void testInvalidOperatorSequence() {
        assertThrowsIllegalArgumentException("a**", "Invalid regex: * without operand");
    }
    
    @Test
    public void testInvalidOperatorSequencePlus() {
        assertThrowsIllegalArgumentException("a++", "Invalid regex: + without operand");
    }
    
    @Test
    public void testInvalidOperatorSequenceQuestion() {
        assertThrowsIllegalArgumentException("a??", "Invalid regex: ? without operand");
    }
    
    @Test
    public void testInvalidOperatorSequenceUnion() {
        assertThrowsIllegalArgumentException("a||b", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testInvalidOperatorSequenceConcatenation() {
        assertThrowsIllegalArgumentException("a..b", "Invalid regex: concatenation requires two operands");
    }
    
    @Test
    public void testComplexInvalidPattern() {
        assertThrowsIllegalArgumentException("(a|b|c|)", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testInvalidCharacterClassRange() {
        assertThrowsIllegalArgumentException("[a-]", "Invalid regex");
    }
    
    @Test
    public void testInvalidCharacterClassRangeStart() {
        assertThrowsIllegalArgumentException("[-a]", "Invalid regex");
    }
    
    @Test
    public void testInvalidHexRangeFormat() {
        assertThrowsIllegalArgumentException("[\\x4-\\x5]", "Invalid regex");
    }
    
    @Test
    public void testInvalidHexRangeFormat2() {
        assertThrowsIllegalArgumentException("[\\x41-\\x5]", "Invalid regex");
    }
    
    @Test
    public void testInvalidHexRangeFormat3() {
        assertThrowsIllegalArgumentException("[\\x4-\\x41]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass() {
        assertThrowsIllegalArgumentException("[\\]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass2() {
        assertThrowsIllegalArgumentException("[\\x]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass3() {
        assertThrowsIllegalArgumentException("[\\xG]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass4() {
        assertThrowsIllegalArgumentException("[\\x4G]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass5() {
        assertThrowsIllegalArgumentException("[\\x4]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass6() {
        assertThrowsIllegalArgumentException("[\\x4-]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass7() {
        assertThrowsIllegalArgumentException("[\\x4-\\x]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass8() {
        assertThrowsIllegalArgumentException("[\\x4-\\xG]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass9() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4G]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass10() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass11() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass12() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-\\x4]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass13() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-\\x4G]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass14() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-\\x4G]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass15() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-\\x4G]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass16() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-\\x4G]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass17() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-\\x4G]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass18() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-\\x4G]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass19() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-\\x4G]", "Invalid regex");
    }
    
    @Test
    public void testInvalidEscapeSequenceInCharacterClass20() {
        assertThrowsIllegalArgumentException("[\\x4-\\x4-\\x4G]", "Invalid regex");
    }
}
