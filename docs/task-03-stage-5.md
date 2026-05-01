# Task 3: Stage 5

## Assigned Stage

5. Code Generation (CG)

## Assigned Team Member

Aloys

## Assigned Java File

- `stages/CodeGeneration.java`

## Purpose

Task 3 implements the code generation responsibility for source lines that have successfully passed lexical, syntax, semantic, and intermediate-representation stages.

## Stage Responsibility

Code Generation converts Stage 4 three-address code into a lower-level accumulator-style instruction format.

The implementation belongs in `stages/CodeGeneration.java`.

## Input

Stage 5 receives semicolon-separated three-address code from Stage 4.

Example:

```text
t1 = A / B; t2 = t1 + C; M = t2
```

## Output Format

Stage 5 returns semicolon-separated lower-level instructions using:

- `LOAD value`
- `ADD value`
- `SUB value`
- `MUL value`
- `DIV value`
- `STORE target`

Example output:

```text
LOAD A; DIV B; STORE t1; LOAD t1; ADD C; STORE t2; LOAD t2; STORE M
```

If Stage 5 receives invalid intermediate code, it returns an error beginning with `ERROR: Code generation error - ...`.

## Stage Boundary

Only valid lines should reach Code Generation:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`
