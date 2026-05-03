# Task 5: Stage 7

## Assigned Stage

7. Target Machine Code (TMC) in Binary

## Assigned Team Member

Enoch

## Assigned Java File

- `stages/TargetMachineCode.java`

## Purpose

Task 5 documents the final compiler stage required by the assignment.

## Stage Responsibility

Target Machine Code in Binary should represent the final output stage for valid source lines after analysis, intermediate representation, code generation, and optimization have completed.

The implementation belongs in `stages/TargetMachineCode.java`.

## Output Format

Stage 7 converts the Stage 6 accumulator instructions into a simple binary-looking form.

- `LOAD` maps to `0001`
- `ADD` maps to `0010`
- `SUB` maps to `0011`
- `MUL` maps to `0100`
- `DIV` maps to `0101`
- `STORE` maps to `0110`

Each operand is written as 8-bit ASCII binary, so identifiers and temporary variables still have a readable binary result.

## Stage Boundary

Only valid lines should reach Target Machine Code in Binary:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`
