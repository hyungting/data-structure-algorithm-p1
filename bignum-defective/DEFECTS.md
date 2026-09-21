# Defect Report — CS 5040 Project 1

Name: Yun-Ting Ho
PID: 906844294

Complete one block per defect. Six blocks. If you find fewer than six, submit
what you have; do not pad the report with defects that are not in the code.

---

## Defect 1: `LinkedList.add` traverses the list instead of appending in O(1)

**Category:** asymptotic cost of a list operation used inside a loop

**Symptom.**
Appending a new element to a long list takes noticeably longer than appending an element to a short list.
The slowdown becomes increasingly visible as more elements are added.

**Minimal failing input.**
- input: Create an empty LinkedList<Integer> and append 1, 2, and 3.
- actual: Appending the third element walks from head to tail. Repeated appends therefore accumulate a quadratic number of traversals.
- expected: Because the list maintains a tail pointer, each append should use that pointer  directly and take O(1) time.

With only one element, no existing link must be followed.
When appending the second element, the head is also the tail, so no link traversal occurs.
Therefore, three appended elements are required to expose the defect.

**Root cause.** 
- File: `LinkedList.java`
- Method: `LinkedList.add`
- Line: `67-74`

The `LinkedList.add` method can run in O(1) time by appending the new node directly to the tail.
However, the method appends a new node by traversing the list from the head instead.
Each append takes K operations when the list length is K.
When N elements are added to an initially empty LinkedList, the total number of traversed nodes is N(N − 1)/2.
Therefore, repeatedly calling add to build a list of length N takes O(N^2) time.

**Fix.**
I will add new node directly to the current tail and then update the tail reference in `add` method.
This provides O(1) append operations without changing the list’s data structure.
Traversing from the head was rejected because it unnecessarily makes repeated append operations quadratic.

**Regression test.** `testLinkedListAddOperationCount`

---


## Defect 2: `LinkedList.truncateAfter` leaves size inconsistent and performs an unnecessary traversal

**Category:** the size invariant after a list is truncated

**Symptom.**
After truncation, the list contains fewer elements than the value reported by size.
For example, after truncating a two-element list after its head, only the head remains reachable, but size still reports two elements.

**Minimal failing input.**
- input: Create an empty `LinkedList<Integer>`, append 1 and 2, then call `truncateAfter(head)`.
- actual: The list contains only 1, but `size()` returns 2.
- expected: The list contains only 1, and `size()` returns 1.

A one-element list cannot expose this defect because truncating after its head removes no elements.
Therefore, its size remains correct.

**Root cause.**
- File: `LinkedList.java`
- Method: `LinkedList.truncateAfter`
- Line: `142-145`

The violated class invariant is that size must equal the number of nodes reachable from head.
The method disconnects the nodes after the specified node but does not update size to reflect the nodes that were removed.
In addition, the method traverses nodes even though the traversal is unnecessary for performing the truncation.
Once the truncation point is known, the list can be ended by updating the relevant links and tail reference directly.
The extra loop makes the operation linear rather than constant time.

**Fix.**
I will add size update operation in `truncateAfter` so that the size equals the number of remaining nodes.
I also removed the redundant traversal because it has nothing to do with truncation.

**Regression test.** 
`testLinkedListSizeTruncateAfter` -> verifies size consistency after truncation.
`testLinkedListTruncateAfterOperationCount` -> verifies that truncation does not perform an unnecessary traversal.

---

## Defect 3: `LinkedList.equals` can treat a same prefix as equal

**Category:** linked list equality

**Symptom.**
`LinkedList.equals` returns `true` when two lists have the same values up to the end of the shorter list, even if the other list contains additional nodes.
For example, an empty list is incorrectly considered equal to a list containing one element.

**Minimal failing input.**

- input: Create two empty `LinkedList<Integer>`, one append 1 and the other append 1 & 2, then call `equals`.
- actual: `true`
- expected: `false`

This is minimal because a strict-prefix comparison requires one list to have at least one node that the other list does not have.
An empty list and a one-element list use the fewest possible nodes in that family.

**Root cause.**
- File: `LinkedList.java`
- Method: `LinkedList.equals`
- Line: `236-245`

This method checks only whether the two lists share the same prefix when one list is the full length.
It returns `true` without verifying that both lists end at the same time.

**Fix.**
I will compare the lists' sizes before comparing corresponding nodes.
If their sizes differ, equals will return false immediately, avoiding an unnecessary O(N) node-by-node comparison.

**Regression test.** `testTwoLinkedListEqual`

---

## Defect 4: `BigNum.exponentiateHelper`'s even exponent branch produce an incorrect result

**Category:** exponentiation control flow

**Symptom.**
Exponentiation returns an incorrect value when an even exponent greater than two is used.
For example, the program calculates `2^4` as `8` instead of `16`.

**Minimal failing input.**
- input: Create `BigNum` with base = 2, then call `exponentiate` with exponent.
- actual: "8"
- expected: "16"

The test must use an even exponent of at least four because exponent two happens to produce the correct result even with the defect.
At exponent four, `res` and `base` differ, making the error observable.

**Root cause.**
- File: `BigNum.java`
- Method: `BigNum.exponentiateHelper`
- Line: `225-228`

The even-exponent branch should return `res * res`, but it returns `res * base`.
Here, `res` is `base^(exponent / 2)`.
The current code therefore calculates `base^(exponent / 2 + 1)` instead of `base^exponent`.
For exponents greater than two, it reduces the resulting exponent by `exponent / 2 - 1`.

**Fix.**
I changed the even-exponent branch to return `res * res`.
This implements the exponentiation-by-squaring rule for an even exponent.

**Regression test.** `testBigNumEvenExponent`

---

## Defect 5: `Stack.peek` returns the bottom element instead of the top

**Category:** access to the top of the stack

**Symptom.**
`peek()` is meant to get the last pushed element.
Instead, it returns the element that was pushed first.

**Minimal failing input.**
- input: Create `Stack` with `1, 2` pushed accordingly, then call `peek()`.
- actual: "1"
- expected: "2"

Two pushed elements are minimal because the head and tail refer to the same node and the defect is not observable with only one element .

**Root cause.**
- File: `Stack.java`
- Method: `Stack.peek`
- Line: `48-54`

In `Stack`, `push()` adds each element to the head of the linked list.
Therefore, the top of the stack is stored at index `0`. 
However, `peek()` uses `getSize() - 1`, which retrieves the bottom of the stack.
This violates the stack's LIFO ordering invariant.

**Fix.**
I will let `peek()` to return the data from `getHead()`.
I chose this fix because `getHead()` directly represents the top of the stack. 
Using `getItemAt(0)` can also work, but `getHead()` is simpler, 
makes the representation invariant explicit, and avoids an unnecessary indexed-list access.

**Regression test.** `testStackPeek`

---

## Defect 6: Tab-separated tokenization are not parsed correctly

**Category:** input tokenization

**Symptom.**
A valid RPN expression whose tokens are separated by tab characters is not tokenized into separate operands and operators.
Instead, the expression is treated as one malformed operand and produces an incorrect result.

**Minimal failing input.**
- input: `1\t2\t+`
- actual: `11-21-1-`
- expected: `3`

A valid binary RPN expression requires two operands and one operator, so it must contain three tokens and two separators.
This is the shortest expression in that family that uses tabs as token separators.

**Root cause.**

- File: `BigNumExpression.java`
- Method: `BigNumExpression`
- Line: 19

The constructor uses one or more ordinary space characters to separate expression.
It does not match a tab character, so tab-separated tokens are not split before RPN evaluation.
The parser then receives one malformed token instead of the three tokens `2`, `3`, and `+`.

**Fix.**
I will include tab as identifier of separating expression using the regular expression `\\s+`.
It matches one or more whitespace characters, including ordinary spaces and tabs.
I chose it over `" +"` because it retains support for multiple spaces while also correctly handling other valid whitespace separators.

**Regression test.** `testBigNumParseTab`



