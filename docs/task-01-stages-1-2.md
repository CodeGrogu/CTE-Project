# Task 1: Stages 1 and 2

## Assigned Stages

1. Lexical Analysis (Scanner)
2. Syntax Analysis (Parser)

## Assigned Team Member

Pascal

## Assigned Java Files

- `stages/LexicalAnalysis.java`
- `stages/SyntaxAnalysis.java`

## Purpose

Task 1 implements the first compiler responsibilities: identifying valid language elements and checking whether token order follows the assignment grammar and rules.

## Stage 1: Lexical Analysis

Lexical Analysis should classify source text into the assignment's allowed language elements:

- Keywords
- Identifiers
- Operators
- Symbols

It should also identify lexical errors such as misspelled keywords, including `WRITEE`.

On success, Stage 1 returns the clean source line for Stage 2. On failure, it returns an error beginning with `ERROR: Lexical error - ...`.

## Stage 2: Syntax Analysis

Syntax Analysis should check whether tokens appear in a valid structure according to the assignment rules.

Syntax errors include:

- Combined operators such as `*/`, `+*`, `-/`, and `*+`
- Semicolons at the end of a line
- Digits from `0` to `9`
- Unsupported characters classified by the assignment as syntax errors

On success, Stage 2 returns the clean source line for Stage 3. On failure, it returns an error beginning with `ERROR: Syntax error - ...`.

## Stage Boundary

Only valid lines should continue beyond Task 1 into the remaining compiler stages. Lines with lexical or syntax errors should report the error and stop further compiler-stage processing.
