# Defect Report — CS 5040 Project 1

Name: Yun-Ting Ho
PID:

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
