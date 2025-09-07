package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for quantifier operators: {, }
 */
public class QuantifierTests2 extends BaseRegexToDFATest {

  @Test
  public void testQuantifierAtLeastOne() {
      convertRegex("a{1,}");

      assertMultipleTestCases(
          testCase("", false),                  // zero a's not allowed
          testCase("a", true),                  // minimum 1
          testCase("aa", true),                 // 2
          testCase("aaa", true),                // 3
          testCase("aaaaaaa", true),            // many
          testCase("b", false),                 // invalid char
          testCase("ab", false)                 // extra char
      );
  }

  @Test
  public void testQuantifierOneOrTwo() {
      convertRegex("a{1,2}");

      assertMultipleTestCases(
          testCase("", false),                  // zero not allowed
          testCase("a", true),                  // 1 is valid
          testCase("aa", true),                 // 2 is valid
          testCase("aaa", false),               // too many
          testCase("b", false),                 // invalid char
          testCase("aab", false)                // invalid extra
      );
  }

  @Test
  public void testQuantifierOneToSix() {
      convertRegex("a{1,6}");

      assertMultipleTestCases(
          testCase("", false),                  // must be at least 1
          testCase("a", true),                  // 1
          testCase("aaaaaa", true),             // 6
          testCase("aaaaaaa", false),           // 7 not allowed
          testCase("b", false),                 // invalid
          testCase("aaab", false)               // invalid suffix
      );
  }

  @Test
  public void testQuantifierOneToTen() {
      convertRegex("a{1,10}");

      assertMultipleTestCases(
          testCase("", false),                  
          testCase("a", true),                  // min
          testCase("aaaaaaaaaa", true),         // 10
          testCase("aaaaaaaaaaa", false),       // 11
          testCase("b", false),                 
          testCase("aaab", false)               
      );
  }

  @Test
  public void testQuantifierFiveToTen() {
      convertRegex("a{5,10}");

      assertMultipleTestCases(
          testCase("", false),                  
          testCase("aaaa", false),              // 4 not enough
          testCase("aaaaa", true),              // 5
          testCase("aaaaaaaaaa", true),         // 10
          testCase("aaaaaaaaaaa", false),       // 11
          testCase("b", false),                 
          testCase("aaaaab", false)             
      );
  }

  @Test
  public void testQuantifierFifteenToTwentyFive() {
      convertRegex("a{15,25}");

      assertMultipleTestCases(
          testCase("", false),                  
          testCase("aaaaaaaaaaaaaa", false),    // 14 not enough
          testCase("aaaaaaaaaaaaaaa", true),    // 15
          testCase("aaaaaaaaaaaaaaaaaaaaaaaaa", true),  // 25
          testCase("aaaaaaaaaaaaaaaaaaaaaaaaaa", false), // 26
          testCase("b", false)                  
      );
  }

  @Test
  public void testQuantifierExactlyTwelve() {
      convertRegex("a{12}");

      assertMultipleTestCases(
          testCase("", false),                  
          testCase("aaaaaaaaaaa", false),       // 11
          testCase("aaaaaaaaaaaa", true),       // 12
          testCase("aaaaaaaaaaaaa", false),     // 13
          testCase("b", false)                  
      );
  }

  @Test
  public void testQuantifierZeroOrMore() {
      convertRegex("a{0,}");

      assertMultipleTestCases(
          testCase("", true),                   // zero is allowed
          testCase("a", true),                  // one
          testCase("aaaaaa", true),             // many
          testCase("b", false),                 // wrong char
          testCase("ab", false)                 // invalid suffix
      );
  }

  @Test
  public void testQuantifierZeroOrOne() {
      convertRegex("a{0,1}");

      assertMultipleTestCases(
          testCase("", true),                   // zero allowed
          testCase("a", true),                  // one allowed
          testCase("aa", false),                // too many
          testCase("b", false)                  
      );
  }

  @Test
  public void testQuantifierAtLeastTwo() {
      convertRegex("a{2,}");

      assertMultipleTestCases(
          testCase("", false),                  
          testCase("a", false),                 // not enough
          testCase("aa", true),                 // 2 is ok
          testCase("aaa", true),                // 3 is ok
          testCase("aaaaaaa", true),            // many ok
          testCase("ab", false)                 
      );
  }

  @Test
  public void testQuantifierThreeToFive() {
      convertRegex("a{3,5}");

      assertMultipleTestCases(
          testCase("", false),                  
          testCase("a", false),                 
          testCase("aa", false),                
          testCase("aaa", true),                // 3 min
          testCase("aaaa", true),               // 4
          testCase("aaaaa", true),              // 5 max
          testCase("aaaaaa", false),            // too many
          testCase("aab", false)                
      );
  }

  @Test
  public void testQuantifierZeroToThree() {
      convertRegex("a{0,3}");

      assertMultipleTestCases(
          testCase("", true),                   // 0 allowed
          testCase("a", true),                  // 1
          testCase("aa", true),                 // 2
          testCase("aaa", true),                // 3
          testCase("aaaa", false),              // 4 too many
          testCase("b", false)                  
      );
  }

  @Test
  public void testQuantifierUpToFour() {
      // Some regex engines allow {,4} instead of {0,4}
      convertRegex("a{,4}");

      assertMultipleTestCases(
          testCase("", true),                   
          testCase("a", true),                  
          testCase("aa", true),                 
          testCase("aaa", true),                
          testCase("aaaa", true),               // 4 max
          testCase("aaaaa", false),             // 5 not allowed
          testCase("b", false)                  
      );
  }

  @Test
  public void testInvalidRegexEmptyBraces() {
      convertRegex("a{}");

      assertMultipleTestCases(
          testCase("a", false),
          testCase("aa", false),
          testCase("", false)
      );
  }

  @Test
  public void testInvalidRegexOnlyComma() {
      convertRegex("a{,}");

      assertMultipleTestCases(
          testCase("a", false),
          testCase("aa", false),
          testCase("", false)
      );
  }

  @Test
  public void testInvalidRegexNonNumericBound() {
      convertRegex("a{,a}");

      assertMultipleTestCases(
          testCase("a", false),
          testCase("aa", false),
          testCase("", false)
      );
  }

  @Test
  public void testInvalidRegexReversedBounds() {
      convertRegex("a{5,2}");  // lower bound > upper bound

      assertMultipleTestCases(
          testCase("aa", false),
          testCase("aaaaa", false),
          testCase("", false)
      );
  }

  @Test
  public void testInvalidRegexMissingUpperBound() {
      convertRegex("a{,}"); // ambiguous in most regex engines

      assertMultipleTestCases(
          testCase("a", false),
          testCase("aaaaa", false),
          testCase("", false)
      );
  }


}
