# Regex to DFA Converter

A comprehensive Java implementation for converting regular expressions to Deterministic Finite Automata (DFA) using Thompson's construction algorithm. This project provides a complete automaton library with NFA and DFA implementations, following SOLID principles for maintainable and extensible code.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Core Classes](#core-classes)
  - [NFA (Non-deterministic Finite Automaton)](#nfa-non-deterministic-finite-automaton)
  - [DFA (Deterministic Finite Automaton)](#dfa-deterministic-finite-automaton)
  - [RegexToDFAConverter](#regextodfaconverter)
  - [Supporting Classes](#supporting-classes)
- [Usage Examples](#usage-examples)
- [Supported Regex Features](#supported-regex-features)
- [Installation and Setup](#installation-and-setup)
- [Testing](#testing)
- [Performance Considerations](#performance-considerations)
- [Contributing](#contributing)

## Overview

This project implements a complete regular expression to DFA conversion system using the following approach:

1. **Regex Parsing**: Converts regular expressions to postfix notation (Reverse Polish Notation)
2. **NFA Construction**: Uses Thompson's construction algorithm to build NFAs from postfix expressions
3. **DFA Conversion**: Converts NFAs to DFAs using the subset construction algorithm
4. **DFA Minimization**: Optimizes DFAs by removing unreachable and dead states, and merging equivalent states

## Features

- ✅ **Complete Regex Support**: Basic operators (`*`, `+`, `?`, `|`, `.`), character classes `[a-z]`, escape sequences
- ✅ **Quantifier Support**: `{n}`, `{n,m}`, `{n,}`, `{,m}` for exact, range, at-least, and at-most repetitions
- ✅ **Character Classes**: Support for ranges, negation (`^`), and escape sequences
- ✅ **DFA Minimization**: Automatic optimization using table-filling algorithm
- ✅ **Visualization**: DOT format output for graph visualization
- ✅ **Lexer Generation**: Generate Java lexer classes from DFA and NFA using Pebble templates
- ✅ **Comprehensive Testing**: Extensive test suite with 20+ test classes
- ✅ **SOLID Principles**: Clean, maintainable, and extensible code architecture

## Project Structure

```
src/
├── main/java/org/example/
│   ├── NFA.java                    # Non-deterministic Finite Automaton
│   ├── DFA.java                    # Deterministic Finite Automaton
│   ├── RegexToDFAConverter.java    # Main conversion logic
│   ├── LexerGenerator.java         # Lexer code generation using Pebble templates
│   ├── State.java                  # State representation
│   ├── Transition.java             # Transition representation
│   └── StateSet.java               # State set for DFA construction
├── main/resources/
│   ├── dfa_lexer_template.peb      # Pebble template for DFA lexer generation
│   └── nfa_lexer_template.peb      # Pebble template for NFA lexer generation
└── test/java/org/example/
    ├── BasicConversionTests.java
    ├── CharacterClassTests.java
    ├── ComplexPatternTests.java
    ├── DFAAcceptanceTests.java
    ├── DFAConstructionTests.java
    ├── DFAMinimizationTests.java
    ├── DFATransitionTests.java
    ├── DFAVisualizationTests.java
    ├── LexerGeneratorTests.java    # Tests for lexer generation functionality
    ├── NFAAcceptanceTests.java
    ├── NFAConstructionTests.java
    ├── NFAConversionTests.java
    ├── NFAOperationsTests.java
    ├── StateSetTests.java
    ├── StateTests.java
    └── TransitionTests.java
```

## Core Classes

### NFA (Non-deterministic Finite Automaton)

The `NFA` class represents a non-deterministic finite automaton with support for epsilon transitions.

#### Key Methods:

```java
// Construction
NFA nfa = new NFA();
NFA nfa = new NFA(startState, acceptingStates);

// Basic operations
boolean accepts(String input)           // Check if NFA accepts input
void addTransition(String from, char symbol, String to)  // Add transition
void addEpsilonTransition(String from, String to)       // Add epsilon transition

// Regex operations
NFA star()                             // Kleene star (zero or more)
NFA plus()                             // Plus (one or more)
NFA question()                         // Question (zero or one)
NFA union(NFA other)                   // Union (OR)
NFA concatenate(NFA other)             // Concatenation

// Conversion
DFA toDFA()                            // Convert to DFA

// Lexer generation
String generateLexer(String className, String packageName)  // Generate lexer code
String generateLexer(String className, String packageName, Map<String, String> tokenTypes)  // With custom token types
```

#### Example Usage:

```java
// Create a simple NFA for pattern "ab"
NFA nfa = new NFA();
String start = "q0";
String accept = "q1";

nfa.addTransition(start, 'a', "q2");
nfa.addTransition("q2", 'b', accept);

// Test acceptance
boolean result = nfa.accepts("ab");  // true
boolean result2 = nfa.accepts("a");  // false

// Apply Kleene star
NFA starred = nfa.star();  // Accepts "", "ab", "abab", "ababab", etc.
```

### DFA (Deterministic Finite Automaton)

The `DFA` class represents a deterministic finite automaton with optimization capabilities.

#### Key Methods:

```java
// Construction
DFA dfa = new DFA();
DFA dfa = new DFA(startState, acceptingStates);

// Basic operations
boolean accepts(String input)          // Check if DFA accepts input
void addState(String name, boolean isAccepting)  // Add state
void addTransition(String from, char symbol, String to)  // Add transition
DFA setStartState(String state)        // Set start state (returns new DFA)

// Optimization
DFA minimizeDFA()                      // Minimize DFA

// Visualization
String toDot()                         // Generate DOT format for visualization

// Lexer generation
String generateLexer(String className, String packageName)  // Generate lexer code
String generateLexer(String className, String packageName, Map<String, String> tokenTypes)  // With custom token types
```

#### Example Usage:

```java
// Create a DFA for pattern "ab*"
DFA dfa = new DFA();
dfa.addState("q0", false);
dfa.addState("q1", true);

dfa.addTransition("q0", 'a', "q1");
dfa.addTransition("q1", 'b', "q1");

dfa = dfa.setStartState("q0");

// Test acceptance
boolean result = dfa.accepts("a");     // true
boolean result2 = dfa.accepts("ab");   // true
boolean result3 = dfa.accepts("abb");  // true
boolean result4 = dfa.accepts("b");    // false

// Minimize the DFA
DFA minimized = dfa.minimizeDFA();

// Generate visualization
String dotFormat = dfa.toDot();
System.out.println(dotFormat);
```

### RegexToDFAConverter

The main converter class that handles the complete regex-to-DFA conversion process.

#### Key Methods:

```java
// Main conversion method
static DFA convert(String regex)       // Convert regex to DFA
```

### LexerGenerator

The `LexerGenerator` class generates Java lexer code from DFA and NFA using Pebble templates.

#### Key Methods:

```java
// DFA lexer generation
String generateLexerFromDFA(DFA dfa, String className, String packageName)
String generateLexerFromDFA(DFA dfa, String className, String packageName, Map<String, String> tokenTypes)

// NFA lexer generation
String generateLexerFromNFA(NFA nfa, String className, String packageName)
String generateLexerFromNFA(NFA nfa, String className, String packageName, Map<String, String> tokenTypes)
```

#### Example Usage:

```java
// Convert various regex patterns to DFA
DFA dfa1 = RegexToDFAConverter.convert("ab*");           // "a" followed by zero or more "b"
DFA dfa2 = RegexToDFAConverter.convert("(ab)+");         // One or more "ab"
DFA dfa3 = RegexToDFAConverter.convert("[a-z]+");        // One or more lowercase letters
DFA dfa4 = RegexToDFAConverter.convert("a{2,5}");        // 2 to 5 occurrences of "a"
DFA dfa5 = RegexToDFAConverter.convert("a|b");           // "a" or "b"

// Test the DFAs
System.out.println(dfa1.accepts("a"));      // true
System.out.println(dfa1.accepts("ab"));     // true
System.out.println(dfa1.accepts("abb"));    // true
System.out.println(dfa1.accepts("b"));      // false
```

#### Example Usage:

```java
// Generate lexer from DFA
LexerGenerator generator = new LexerGenerator();
DFA dfa = RegexToDFAConverter.convert("[a-z]+");
String lexerCode = generator.generateLexerFromDFA(dfa, "IdentifierLexer", "com.example");

// Generate lexer with custom token types
Map<String, String> tokenTypes = new HashMap<>();
tokenTypes.put("accepting_state", "IDENTIFIER");
String customLexerCode = dfa.generateLexer("CustomLexer", "com.example", tokenTypes);

// Generate lexer from NFA
NFA nfa = new NFA();
// ... build NFA ...
String nfaLexerCode = nfa.generateLexer("NFALexer", "com.example");
```

### Supporting Classes

#### State
Represents a single state in an automaton.

```java
State state = new State("q0", true);  // name, isAccepting
String name = state.getName();
boolean isAccepting = state.isAccepting();
```

#### Transition
Represents a transition between states.

```java
Transition trans = new Transition("q0", 'a', "q1");  // from, symbol, to
String from = trans.getFromState();
Character symbol = trans.getSymbol();
String to = trans.getToState();
boolean isEpsilon = trans.isEpsilonTransition();
```

#### StateSet
Represents a set of states, used in DFA construction.

```java
StateSet stateSet = new StateSet();
stateSet.addState("q0");
stateSet.addState("q1");
Set<String> states = stateSet.getStates();
boolean contains = stateSet.contains("q0");
```

## Usage Examples

### Basic Pattern Matching

```java
// Simple character matching
DFA dfa = RegexToDFAConverter.convert("hello");
System.out.println(dfa.accepts("hello"));  // true
System.out.println(dfa.accepts("world"));  // false

// Wildcard matching
DFA wildcard = RegexToDFAConverter.convert("h.llo");
System.out.println(wildcard.accepts("hello"));  // true
System.out.println(wildcard.accepts("hallo"));  // true
System.out.println(wildcard.accepts("hxllo"));  // true
```

### Quantifiers

```java
// Zero or more
DFA star = RegexToDFAConverter.convert("a*");
System.out.println(star.accepts(""));     // true
System.out.println(star.accepts("a"));    // true
System.out.println(star.accepts("aa"));   // true

// One or more
DFA plus = RegexToDFAConverter.convert("a+");
System.out.println(plus.accepts(""));     // false
System.out.println(plus.accepts("a"));    // true
System.out.println(plus.accepts("aa"));   // true

// Zero or one
DFA question = RegexToDFAConverter.convert("a?");
System.out.println(question.accepts(""));  // true
System.out.println(question.accepts("a")); // true
System.out.println(question.accepts("aa")); // false
```

### Character Classes

```java
// Lowercase letters
DFA lowercase = RegexToDFAConverter.convert("[a-z]+");
System.out.println(lowercase.accepts("hello"));  // true
System.out.println(lowercase.accepts("Hello"));  // false

// Digits
DFA digits = RegexToDFAConverter.convert("[0-9]+");
System.out.println(digits.accepts("123"));       // true
System.out.println(digits.accepts("abc"));       // false

// Negated character class
DFA notDigits = RegexToDFAConverter.convert("[^0-9]+");
System.out.println(notDigits.accepts("hello"));  // true
System.out.println(notDigits.accepts("123"));    // false
```

### Complex Patterns

```java
// Email-like pattern
DFA email = RegexToDFAConverter.convert("[a-z]+@[a-z]+\\.[a-z]+");
System.out.println(email.accepts("user@domain.com"));  // true

// Phone number pattern
DFA phone = RegexToDFAConverter.convert("\\d{3}-\\d{3}-\\d{4}");
System.out.println(phone.accepts("123-456-7890"));     // true

// Nested quantifiers
DFA nested = RegexToDFAConverter.convert("(ab){2,4}");
System.out.println(nested.accepts("abab"));            // true
System.out.println(nested.accepts("ababab"));          // true
System.out.println(nested.accepts("ab"));              // false
```

### DFA Optimization

```java
// Create a DFA
DFA original = RegexToDFAConverter.convert("a*b*");

// Minimize the DFA
DFA minimized = original.minimizeDFA();

// Both accept the same language but minimized has fewer states
System.out.println("Original states: " + original.getStates().size());
System.out.println("Minimized states: " + minimized.getStates().size());
```

### Visualization

```java
DFA dfa = RegexToDFAConverter.convert("a*b+");
String dotFormat = dfa.toDot();
System.out.println(dotFormat);

// Output can be used with Graphviz to create visual diagrams
// Example output:
// digraph DFA {
//   rankdir=LR;
//   size="8,5";
//   start [shape=point];
//   start -> "q0";
//   "q0" [shape=circle];
//   "q1" [shape=doublecircle];
//   "q0" -> "q1" [label="a"];
//   "q1" -> "q1" [label="b"];
// }
```

### Lexer Generation

Generate Java lexer classes from DFA and NFA for tokenization:

```java
// Generate a lexer for identifiers
DFA identifierDFA = RegexToDFAConverter.convert("[a-zA-Z][a-zA-Z0-9]*");
String lexerCode = identifierDFA.generateLexer("IdentifierLexer", "com.example");

// Generate a lexer for numbers
DFA numberDFA = RegexToDFAConverter.convert("[0-9]+");
String numberLexerCode = numberDFA.generateLexer("NumberLexer", "com.example");

// Generate a lexer with custom token types
Map<String, String> tokenTypes = new HashMap<>();
tokenTypes.put("accepting_state", "IDENTIFIER");
String customLexerCode = identifierDFA.generateLexer("CustomLexer", "com.example", tokenTypes);

// Generate a lexer from NFA
NFA nfa = new NFA();
nfa.addState("start", false);
nfa.addState("accept", true);
nfa.addTransition("start", 'a', "accept");
nfa.setStartState("start");
String nfaLexerCode = nfa.generateLexer("SimpleLexer", "com.example");
```

The generated lexer classes include:
- Token recognition methods (`nextToken()`, `getAllTokens()`)
- Token class with type, value, and position information
- State management for DFA/NFA traversal
- Error handling for invalid input

## Supported Regex Features

### Basic Operators
- `*` - Zero or more (Kleene star)
- `+` - One or more
- `?` - Zero or one
- `|` - Union (OR)
- `.` - Wildcard (any character)
- `()` - Grouping

### Character Classes
- `[abc]` - Character set
- `[a-z]` - Character range
- `[^abc]` - Negated character set
- `[^a-z]` - Negated character range

### Escape Sequences
- `\s` - Whitespace characters
- `\t` - Tab character
- `\n` - Newline character
- `\r` - Carriage return
- `\\` - Literal backslash
- `\xHH` - Hexadecimal character

### Quantifiers
- `{n}` - Exactly n occurrences
- `{n,m}` - Between n and m occurrences
- `{n,}` - n or more occurrences
- `{,m}` - At most m occurrences

### Special Characters
- `^` - Start of string (literal)
- `$` - End of string (literal)
- `[` `]` - Character class delimiters
- `(` `)` - Grouping delimiters

## Installation and Setup

### Prerequisites
- Java 8 or higher
- Maven 3.6 or higher

### Building the Project

```bash
# Clone the repository
git clone <repository-url>
cd RegexToDfa

# Compile the project
mvn compile

# Run tests
mvn test

# Package the project
mvn package
```

### Running Examples

```bash
# Compile and run the main class
mvn compile exec:java -Dexec.mainClass="org.example.Main"

# Or run specific test classes
mvn test -Dtest=BasicConversionTests
```

## Testing

The project includes a comprehensive test suite with over 20 test classes covering:

- **Basic Conversion Tests**: Simple regex patterns
- **Character Class Tests**: Character sets and ranges
- **Complex Pattern Tests**: Nested patterns and combinations
- **DFA Tests**: Acceptance, construction, minimization, and transitions
- **NFA Tests**: Construction, operations, and conversion
- **Lexer Generation Tests**: DFA and NFA lexer code generation
- **Edge Case Tests**: Error handling and boundary conditions

Run all tests:
```bash
mvn test
```

Run specific test categories:
```bash
mvn test -Dtest="*ConversionTests"
mvn test -Dtest="*DFATests"
mvn test -Dtest="*NFATests"
mvn test -Dtest="LexerGeneratorTests"
```

## Performance Considerations

### Time Complexity
- **Regex Parsing**: O(n) where n is the length of the regex
- **NFA Construction**: O(n) where n is the length of the postfix expression
- **NFA to DFA**: O(2^m) where m is the number of NFA states (worst case)
- **DFA Minimization**: O(n²) where n is the number of DFA states

### Space Complexity
- **NFA Storage**: O(n) where n is the number of states and transitions
- **DFA Storage**: O(n) where n is the number of states and transitions
- **State Set Storage**: O(2^m) for DFA construction where m is the number of NFA states

### Optimization Tips
1. **Use DFA Minimization**: Always minimize DFAs after construction
2. **Avoid Complex Quantifiers**: Simple quantifiers (`*`, `+`, `?`) are more efficient
3. **Character Classes**: Use character classes instead of unions when possible
4. **Test Performance**: Use the provided test suite to benchmark performance

## Contributing

### Code Style
- Follow SOLID principles
- Use meaningful variable and method names
- Add comprehensive JavaDoc comments
- Write unit tests for new features

### Adding New Features
1. Create feature branch
2. Implement the feature with tests
3. Ensure all tests pass
4. Update documentation
5. Submit pull request

### Reporting Issues
- Use descriptive issue titles
- Provide minimal reproduction examples
- Include expected vs actual behavior
- Specify Java version and environment

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Thompson's construction algorithm for NFA building
- Subset construction algorithm for NFA to DFA conversion
- Table-filling algorithm for DFA minimization
- Pebble Template Engine for lexer code generation
- Maven for build management
- JUnit for testing framework
