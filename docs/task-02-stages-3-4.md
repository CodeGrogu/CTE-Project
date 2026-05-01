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

Task 2 implements Stage 3 and Stage 4 responsibilities after lexical and syntax checks have passed.

## Stage 3: Semantic Analysis

Semantic Analysis should confirm that the valid source line is meaningful under the assignment's language rules.

### Input

Stage 3 receives the output from Pascal's Syntax Analysis stage.

### Output

- If no semantic error is found, the original line is returned for the next stage.
- If a semantic error is found, the stage returns `ERROR: Semantic error - ...` and the main compiler stops processing that line.

### Implemented Semantic Rule

The current Stage 3 implementation rejects these disallowed symbols:

- `%`
- `$`
- `&`
- `<`
- `>`

Pascal's stages still own lexical and syntax checks, including misspelled keywords, combined operators, numbers, and semicolon placement.

## Stage 4: Intermediate Code Representation

Intermediate Code Representation converts valid assignment expressions into three-address code before final code generation stages.

### Input

Stage 4 receives only valid assignment lines that have passed analysis and are allowed by the assignment to continue through all seven stages.

### Output Format

Stage 4 returns semicolon-separated three-address code. Temporary variables use the format `t1`, `t2`, `t3`, and so on.

### Examples

```text
LET G = a + c
t1 = a + c; G = t1

M = A/B+C
t1 = A / B; t2 = t1 + C; M = t2

N = G/H-I+a*B/c
t1 = G / H; t2 = t1 - I; t3 = a * B; t4 = t3 / c; t5 = t2 + t4; N = t5
```

The implementation belongs in `stages/IntermediateCodeRepresentation.java`.

## Stage Boundary

Only the valid lines identified in the assignment brief should reach Intermediate Code Representation:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`
