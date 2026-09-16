# CS 5040 Project 1 — Starter Code (Part A)

This package is a complete, compiling, running implementation of BigNumArithmetic.
It produces correct output on `BignumInput.txt`, matching `BignumOutput.txt`
byte for byte. Verify that for yourself before you change anything:

    javac -d bin src/*.java
    java -cp bin BigNumArithmetic BignumInput.txt > mine.txt
    diff mine.txt BignumOutput.txt

It also contains **six seeded defects**. Passing the sample is not evidence of
correctness — it is evidence that the sample does not cover everything.

## Defect categories

Each of the six falls into exactly one of these, one defect per category:

1. Exponentiation control flow
2. Linked list equality
3. The size invariant after a list is truncated
4. Access to the top of the stack
5. Input tokenization
6. Asymptotic cost of a list operation used inside a loop

Three of the six produce no visible symptom when you run `BigNumArithmetic` on
any input. You will not find those by running the program. Read the code, write
unit tests against `LinkedList`, `LinkedNode`, and `Stack` directly, and check
the class invariants documented at the top of `LinkedList.java`.

One of the six produces fully correct output on every input and is only
detectable by measurement.

## What to submit for Part A

`DEFECTS.md`, following `DEFECTS_template.md`, plus your repaired source and a
regression test per defect that fails before your fix and passes after it.

## Environment notes

- Digits are stored one per node, lowest order digit at the head.
- Several methods recurse once per digit, so operands beyond roughly 3,000
  digits overflow the default JVM stack. That is a property of the starter
  code, not a defect on the list above. You may want to think about it anyway
  before Part C.
