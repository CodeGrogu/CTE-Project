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

## Stage Boundary

Only valid lines should reach Target Machine Code in Binary:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`
