# Task 4: Stage 6

## Assigned Stage

6. Code Optimization (CO)

## Assigned Team Member

Celine

## Assigned Java File

- `stages/CodeOptimization.java`

## Purpose

Task 4 documents the optimization responsibility for generated code that comes from valid assignment lines.

## Stage Responsibility

Code Optimization should represent the compiler phase where generated code may be improved while preserving the meaning of the original valid source line.

The implementation belongs in `stages/CodeOptimization.java`.

## Stage Boundary

Only generated output from valid lines should reach Code Optimization:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`
