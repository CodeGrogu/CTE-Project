# Assignment Brief

## Project Title

Development of a Mini Compiler System Using Java

## Course Context

- Course: CTE711S Compiler Techniques
- Semester: Semester 1, 2026
- Assessment: Group assignment and presentation
- Main deliverable: a Java mini compiler system with documentation

## Required Processing Modes

The compiler should support both required processing approaches:

- Line-by-line processing.
- All-at-once processing.

## Source Program

```text
BEGIN
INTEGER A, B, C, E, M, N, G, H, I, a, c
INPUT A, B, C
LET B = A */ M
LET G = a + c
temp = <s%**h - j / w +d +*$&;
M = A/B+C
N = G/H-I+a*B/c
WRITE M
WRITEE F;
END
```

## Valid Lines for Full Compilation

Only these lines should pass through all seven compiler stages:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`

Other lines should be checked for errors and should report the error found, if any. They should not continue through every compiler stage after an error has been identified.

## Accepted Language Elements

- Keywords: `BEGIN`, `INTEGER`, `LET`, `INPUT`, `WRITE`, `END`
- Identifiers: lowercase words and single letters from `A` to `Z` and `a` to `z`
- Operators: `+`, `-`, `/`, `*`
- Symbols: `=` and `;`

## Error Rules

- Symbols such as `%`, `$`, `&`, `<`, `>`, and invalid uses of `;` should produce semantic errors where specified by the assignment.
- Combined operators such as `+*`, `-/`, `*/`, and `*+` should produce syntax errors.
- A semicolon at the end of a line should produce a syntax error.
- Numbers `0` to `9` are not allowed and should produce syntax errors.
- Misspelled keywords, such as `WRITEE`, should produce lexical errors.
- Any other unsupported keyboard character should produce a syntax error.

## Seven Compiler Stages

1. Lexical Analysis (Scanner)
2. Syntax Analysis (Parser)
3. Semantic Analysis (Syntactic Analysis)
4. Intermediate Code Representation (ICR)
5. Code Generation (CG)
6. Code Optimization (CO)
7. Target Machine Code (TMC) in Binary
