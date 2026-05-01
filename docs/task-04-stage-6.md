# Task 4: Stage 6

## Assigned Stage

6. Code Optimization (CO)

## Assigned Team Member

Celine

## Assigned Java File

- `stages/CodeOptimization.java`

## Purpose

Task 4 implements the optimization responsibility for generated code that comes from valid assignment lines.

## Stage Responsibility

Code Optimization improves generated code while preserving the meaning of the original valid source line.

The implementation belongs in `stages/CodeOptimization.java`.

## Input

Stage 6 receives semicolon-separated lower-level instructions from Stage 5.

Example:

```text
LOAD A; DIV B; STORE t1; LOAD t1; ADD C; STORE t2; LOAD t2; STORE M
```

## Implemented Optimization

The current implementation is intentionally conservative because Stage 5 uses accumulator-style instructions. Removing the wrong `LOAD` or `STORE` can change the result of the expression.

Implemented behavior:

- Splits the generated instruction string into clean instruction parts.
- Removes exact duplicate consecutive `LOAD` instructions, such as `LOAD A; LOAD A`.
- Preserves `STORE t1; LOAD t1` pairs because those instructions are needed when a temporary value is used by the next operation.

## Output

Stage 6 returns the optimized instruction string in the same semicolon-separated format used by Stage 5.

## Stage Boundary

Only generated output from valid lines should reach Code Optimization:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`
