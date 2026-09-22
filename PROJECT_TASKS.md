# CS 5040 Project 1 — 工作追蹤

> 這份文件是我們共同的工作清單。完成一項時，將 `[ ]` 改成 `[x]`，並在必要處補上證據（測試名稱、命令或 commit）。

## 目前目標

- Final deadline：2026-09-23 23:59
- 近期交付：Milestone 2（2026-09-18）— `DEFECTS.md` 中完整記錄三個 defects。
- 原則：每一項修正都要先能用自己的話解釋，再寫回歸測試；使用 AI 的內容要記錄在 `AI_USE.md`。

## 0. 專案基線與工作環境

- [x] 確認 starter code 可編譯：`javac -d bin src/*.java`。
- [x] 執行 sample：`java -cp bin BigNumArithmetic BignumInput.txt`。
- [x] 與 `BignumOutput.txt` 比較，確認 starter sample 輸出一致。
- [x] 將原始 starter source 建立一個 Git baseline commit（不提交 `bin/` 或 `.DS_Store`）。
- [x] 確認 JUnit 版本、jar 檔位置，以及本機執行測試的命令。
- [x] 建立/確認 `.gitignore`，排除 `bin/`、`.class`、`.DS_Store`。

## 1. Milestone 1：可執行程式與第一個測試

- [x] 將 `BigNumTest.java` 改成可編譯的 JUnit test class（目前已有草稿，但需確認 class、imports 與 `@Test` 位置正確）。
- [x] 寫一個最小通過測試，例如 `2 + 3 = 5`。
- [x] 在本機或 Web-CAT 成功執行至少一個測試。
- [x] 記錄測試命令與成功輸出。

## 2. Part A：找出、修復並記錄六個 seeded defects

每個 defect 都必須完成：最小失敗輸入／單元測試、可觀察症狀、根因（檔案、方法、違反的 invariant 或演算法性質）、修正理由、回歸測試名稱。

- [x] 建立 `DEFECTS.md`，由 `DEFECTS_template.md` 複製格式開始。
- [x] Defect 1：Exponentiation control flow
- [x] Defect 2：Linked list equality
- [x] Defect 3：List truncation 後的 `size` invariant
- [x] Defect 4：Stack top access (`peek`)
- [x] Defect 5：Input tokenization
- [x] Defect 6：迴圈內 list operation 的 asymptotic cost
- [x] 為以上每個 defect 建立一個「修正前失敗、修正後通過」的 regression test。
- [x] 先完成任三個 defect 的完整文件，交 Milestone 2。
- [x] 跑 sample input，確認修正後沒有破壞原有輸出。

## 3. Part B：Adversarial JUnit Test Suite

- [x] 為加法的 carry propagation 與最終 carry-out 寫測試。
- [x] 為零、加法 identity、乘法 identity 寫測試。
- [x] 為單位數與多位數的邊界情況寫測試。
- [x] 為乘法的左右長度不對稱寫測試。
- [x] 為 exponentiation by squaring 的奇偶 exponent 寫測試。
- [x] 為高位零正規化寫測試（非零多位數不得有 leading zero；`0` 仍應表示為 `"0"`）。
- [ ] 為 malformed RPN expression 的錯誤處理與下一行恢復寫測試。
- [x] 為 aliasing 寫測試，例如 `x x *` 的兩個 operand 指向同一個 `BigNum` 時仍正確。
- [ ] 確保所有測試在正確實作上通過，且單一測試少於 10 秒。

### Part B 補強待辦

- [ ] 加法 identity 的另一個方向：`123 + 0 = 123`。
- [ ] 乘法不對稱 operand 的交換順序：確認 `7 * 12345` 和 `12345 * 7` 都是 `86415`。
- [ ] Alias 的含零多位數案例，例如 `x = 101` 時 `x.multiply(x) = 10201`，且 `x` 保持 `101`。
- [ ] 空 RPN expression：`new BigNumExpression("").parseExpression()` 應丟出 `IllegalStateException`。
- [ ] 使用 `FileProcessor` 測試 recovery：一行 malformed expression 後，下一行合法 expression 仍會被處理並輸出正確結果。
- [ ] 指數的額外邊界：`0 ^ 5 = 0`，以及非 2 的 base（如 `3 ^ 4 = 81`）。
- [ ] 高位零的運算結果：例如 `1000 * 0` 的字串結果必須是單一 `"0"`。
- [ ] 單位數乘法 carry：例如 `9 * 9 = 81`。
- [ ] LinkedList：`remove(index)` 分別移除 head、middle、tail，驗證回傳節點、順序、head、tail、size 與被移除 node 的 `next`。
- [ ] LinkedList：`truncateAfter(tail)` 不改變 size；`truncateAfter(head)` 後 head/tail 相同、tail.next 為 `null`、size 為 1。
- [ ] LinkedList：反方向 equality，`[1, 2].equals([1])` 必須為 `false`。
- [ ] LinkedList：空 list 的 `removeFirst()` 回傳 `null`，並維持 head/tail 為 `null`、size 為 0。
- [ ] 移除或改寫重複測試 `testLinkedList()`；它目前與負 index 的 `getItemAt` 測試重複。

## 4. Part C：Instrumentation 與 Empirical Cost Study

- [ ] 在 `LinkedNode` 的 node allocation 處呼叫 `OpCounter.nodeAlloc()`。
- [ ] 在加法的每個 digit position 呼叫 `OpCounter.digitAdd()`。
- [ ] 在乘法內層的每次單位數乘法呼叫 `OpCounter.digitMul()`；定義並一致實施 carry addition 的計數規則。
- [ ] 在每次真的沿 linked list 前進一個 node 時呼叫 `OpCounter.traverse()`。
- [ ] 在 `BigNumArithmetic` 支援 `--count`，並使用 `OpCounter.reset()` 與 `OpCounter.report()`。
- [ ] 用小型輸入手算/檢查 counter，驗證計數不是事後估計。
- [ ] 先修正 list append 的效率問題，再進行大型測量。
- [ ] 以 25、100、400、1600、3000 digits 的 operands 進行測量；每種五次。
- [ ] 以 exponent 2^1 到 2^12 進行測量；每種五次。
- [ ] 建立 `measurements.csv`，包含輸入規模、trial、counter 值與 wall-clock time。
- [ ] 撰寫四頁內的 `ANALYSIS.pdf`：方法、數據、圖/比較、Big-O 解釋與 counter 約定。

## 5. Part D：功能與提交前驗證

- [ ] 所有 source 可從乾淨的 `bin/` 重新編譯。
- [ ] sample input 的輸出正確。
- [ ] 有效的加法、乘法、冪次 RPN expression 都正確。
- [ ] malformed expression 會印出 `=` 後空白結果，並繼續處理下一行。
- [ ] 沒有禁止的 `java.util` imports。
- [ ] 所有 data fields 都是 `private` 或 `protected`。
- [ ] `LinkedList` 與 `LinkedNode` 都保持 generic。
- [ ] JavaDoc 的 `@param` / `@return` 補齊需要說明的方法。
- [ ] `BigNumArithmetic.java` 含正確的 honor code pledge 與自己的作者資料。
- [ ] 整理有意義的 Git commits，避免最後只剩一次大 commit。
- [ ] 寫完 `AI_USE.md`：如實記錄使用的工具、問題與採納的修改。

## 6. Part E：10 分鐘 Video Walkthrough

- [ ] 準備並能解釋 number representation（低位數在 head）。
- [ ] 準備並能解釋 RPN evaluation 與 stack。
- [ ] 準備並能解釋 addition、multiplication、exponentiation by squaring。
- [ ] 準備並能解釋 error handling。
- [ ] 清楚說出 addition、multiplication、exponentiation 的 Big-O。
- [ ] 選兩個具體的 efficiency decisions 指著自己的程式說明。
- [ ] 準備一次雙輸入規模的 counter/timing live demonstration。
- [ ] 錄影前確認：10 分鐘內、單一連續錄製、前十秒說姓名與 PID、畫面包含程式碼和 terminal。

## 下一個行動

1. 完成 Part B 的 malformed-expression recovery test 與上方高優先補強項目。
2. 在本機執行完整 `BigNumTest`，確認正確實作全部通過且沒有慢測試。
3. 開始 Part C instrumentation，先確認每種 counter 的定義與放置位置。
