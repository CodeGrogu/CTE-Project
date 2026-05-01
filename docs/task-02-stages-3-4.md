# Task 2: Stages 3 and 4

## Assigned Stages

3. Semantic Analysis (Syntactic Analysis)
4. Intermediate Code Representation (ICR)

## Assigned Team Member

Jaden

## Assigned Java Files

- `stages/SemanticAnalysis.java`
- `stages/IntermediateCodeRepresentation.java`

## Purpose

Task 2 documents the compiler responsibilities after lexical and syntax checks have passed.

## Stage 3: Semantic Analysis

Semantic Analysis should confirm that the valid source line is meaningful under the assignment's language rules.

Semantic errors include disallowed symbols such as:

- `%`
- `$`
- `&`
- `<`
- `>`

## Stage 4: Intermediate Code Representation

Intermediate Code Representation should document the point where a valid expression is represented in an internal compiler-friendly form before final code generation stages.

This documentation stage identifies the responsibility and boundary of the stage. The implementation belongs in `stages/IntermediateCodeRepresentation.java`.

## Stage Boundary

Only the valid lines identified in the assignment brief should reach Intermediate Code Representation:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`
