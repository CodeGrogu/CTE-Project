# Task 3: Stage 5

## Assigned Stage

5. Code Generation (CG)

## Assigned Team Member

Aloys

## Assigned Java File

- `stages/CodeGeneration.java`

## Purpose

Task 3 documents the code generation responsibility for source lines that have successfully passed lexical, syntax, semantic, and intermediate-representation stages.

## Stage Responsibility

Code Generation should represent the compiler phase where the intermediate representation is converted into a lower-level generated form.

The implementation belongs in `stages/CodeGeneration.java`.

## Stage Boundary

Only valid lines should reach Code Generation:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`
