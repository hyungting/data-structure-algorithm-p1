import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class BigNumTest {
    @Test
    public void testLinkedListAddOperationCount(){
        LinkedList<Integer> list = new LinkedList<>();
        OpCounter.reset();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(0L, OpCounter.getTraversals());
    }
    @Test
    public void testLinkedListSizeTruncateAfter(){
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.truncateAfter(list.getHead());
        assertEquals(1, list.getSize());
    }
    @Test
    public void testTwoLinkedListEqual(){
        LinkedList<Integer> list_1 = new LinkedList<>();
        list_1.add(1);
        LinkedList<Integer> list_2 = new LinkedList<>();
        list_2.add(1);
        list_2.add(2);
        assertFalse(list_1.equals(list_2));
    }

    @Test
    public void testBigNumEvenExponent() {
        BigNum num = new BigNum("2");
        BigNum result = num.exponentiate(4);
        assertEquals("16", result.toString());
    }

    @Test
    public void testStackPeek() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        assertEquals("2", stack.peek().toString());
    }

    @Test
    public void testBigNumParseTab() {
        BigNumExpression exp = new BigNumExpression("1\t2\t+");
        assertEquals("3", exp.parseExpression().toString());
    }

    /** Part B */
    @Test
    public void testNoLeadingZero() {
        BigNum a = new BigNum("000001");
        assertEquals("1", a.toString());
    }

    @Test
    public void testHealthyZero() {
        BigNum a = new BigNum("00000");
        assertEquals("0", a.toString());
    }

    @Test
    public void testBigNumAddCarry() {
        BigNum a = new BigNum("99999");
        BigNum b = new BigNum("1");
        BigNum res = a.add(b);
        assertEquals("100000", res.toString());
    }

    @Test
    public void testBigNumAddDoubleCarry() {
        BigNum a = new BigNum("999");
        BigNum b = new BigNum("999");
        BigNum res = a.add(b);
        assertEquals("1998", res.toString());
    }

    @Test
    public void testBigNumAddShortCarry() {
        BigNum a = new BigNum("9");
        BigNum b = new BigNum("1");
        BigNum res = a.add(b);
        assertEquals("10", res.toString());
    }

    @Test
    public void testBigNumAddZero(){
        BigNum a = new BigNum("0");
        BigNum b = new BigNum("1000");
        BigNum res = a.add(b);
        assertEquals("1000", res.toString());
    }

    @Test
    public void testBigNumAddTwoZero(){
        BigNum a = new BigNum("0");
        BigNum b = new BigNum("0");
        BigNum res = a.add(b);
        assertEquals("0", res.toString());
    }

    @Test
    public void testBigNumAddAlias(){
        BigNum a = new BigNum("9");
        BigNum res = a.add(a);
        assertEquals("18", res.toString());
        assertEquals("9", a.toString());
    }

    @Test
    public void testBigNumMultiplyZero(){
        BigNum a = new BigNum("0");
        BigNum b = new BigNum("1000");
        BigNum res = a.multiply(b);
        assertEquals("0", res.toString());
    }

    @Test
    public void testBigNumMultiplyTwoZero(){
        BigNum a = new BigNum("0");
        BigNum b = new BigNum("0");
        BigNum res = a.multiply(b);
        assertEquals("0", res.toString());
    }

    @Test
    public void testBigNumMultiplyIdentity(){
        BigNum a = new BigNum("100");
        BigNum b = new BigNum("1");
        BigNum res = a.multiply(b);
        assertEquals("100", res.toString());
    }

    @Test
    public void testBigNumMultiplyTwoMultiDigits(){
        BigNum a = new BigNum("17");
        BigNum b = new BigNum("17");
        BigNum res = a.multiply(b);
        assertEquals("289", res.toString());
    }

    @Test
    public void testBigNumMultiplyLargeMultiDigits(){
        BigNum a = new BigNum("123456789");
        BigNum b = new BigNum("999");
        BigNum res = a.multiply(b);
        assertEquals("123333332211", res.toString());
    }

    @Test
    public void testBigNumMultiplyTwoLargeMultiDigits(){
        BigNum a = new BigNum("123456789");
        BigNum b = new BigNum("123456789");
        BigNum res = a.multiply(b);
        assertEquals("15241578750190521", res.toString());
    }

    @Test
    public void testbBigNumMultiplyWithAliasedOperands() {
        BigNum x = new BigNum("12");
        BigNum result = x.multiply(x);
        assertEquals("144", result.toString());
        assertEquals("12", x.toString());
    }

    @Test
    public void testBigNumExponentiateZero(){
        BigNum a = new BigNum("1000");
        int e = 0;
        BigNum res = a.exponentiate(e);
        assertEquals("1", res.toString());
    }

    @Test
    public void testBigNumExponentiateTwoZero(){
        BigNum a = new BigNum("0");
        int e = 0;
        BigNum res = a.exponentiate(e);
        assertEquals("1", res.toString());
    }

    @Test
    public void testBigNumExponentiateEven(){
        BigNum a = new BigNum("2");
        int e = 10;
        BigNum res = a.exponentiate(e);
        assertEquals("1024", res.toString());
    }

    @Test
    public void testBigNumExponentiateOdd(){
        BigNum a = new BigNum("2");
        int e = 5;
        BigNum res = a.exponentiate(e);
        assertEquals("32", res.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testRejectsLeftoverOperands() {
        BigNumExpression exp = new BigNumExpression("2 3");
        exp.parseExpression();
    }

    @Test(expected = IllegalStateException.class)
    public void testRejectsLeftoverOperators() {
        BigNumExpression exp = new BigNumExpression("2 3 + + +");
        exp.parseExpression();
    }

    @Test(expected = IllegalStateException.class)
    public void testRejectsIllegalOperators() {
        BigNumExpression exp = new BigNumExpression("2 3 /");
        exp.parseExpression();
    }

    @Test
    public void testEmptyLinkedList() {
        LinkedList<Integer> list = new LinkedList<>();
        assertEquals(null, list.getHead());
        assertEquals(null, list.getTail());
        assertEquals(0, list.getSize());
    }

    @Test
    public void testLinkedListAdd() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i=0; i<10; i++) {
            list.add(i);
        }
        assertEquals(Integer.valueOf(0), list.getHead().getData());
        assertEquals(Integer.valueOf(9), list.getTail().getData());
        assertEquals("0123456789", list.toString());
        assertEquals(null, list.getTail().getNext());
        assertEquals(10, list.getSize());
    }

    @Test
    public void testLinkedListAddFirst() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(2);
        list.addFirst(1);
        assertEquals(Integer.valueOf(1), list.getHead().getData());
        assertEquals(Integer.valueOf(2), list.getTail().getData());
        assertEquals(2, list.getSize());
    }

    @Test
    public void testLinkedListAddFirstFromEmpty() {
        LinkedList<Integer> list = new LinkedList<>();
        list.addFirst(1);
        assertEquals(Integer.valueOf(1), list.getHead().getData());
        assertEquals(Integer.valueOf(1), list.getTail().getData());
        assertEquals(1, list.getSize());
    }

    @Test
    public void testLinkedListRemoveFirst() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(2);
        list.addFirst(1);
        list.removeFirst();
        assertEquals(Integer.valueOf(2), list.getHead().getData());
        assertEquals(Integer.valueOf(2), list.getTail().getData());
        assertEquals(1, list.getSize());
    }

    @Test
    public void testLinkedListRemoveFirstToEmpty() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.removeFirst();
        assertEquals(null, list.getHead());
        assertEquals(null, list.getTail());
        assertEquals(0, list.getSize());
    }

    @Test
    public void testLinkedListGetItemAt() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i=0; i<10; i++) {
            list.add(i);
        }
        assertEquals(Integer.valueOf(3), list.getItemAt(3).getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkedListGetItemAtEmpty() {
        LinkedList<Integer> list = new LinkedList<>();
        list.getItemAt(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkedListGetItemAtRejectOutOfBoundIndex() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i=0; i<10; i++) {
            list.add(i);
        }
        list.getItemAt(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkedListGetItemAtRejectNegativeIndex() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i=0; i<10; i++) {
            list.add(i);
        }
        list.getItemAt(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkedList() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i=0; i<10; i++) {
            list.add(i);
        }
        list.getItemAt(-1);
    }
}