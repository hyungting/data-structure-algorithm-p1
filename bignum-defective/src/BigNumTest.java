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
}