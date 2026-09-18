import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class BigNumTest {
    @Test
    public void testBigNumNoZeroLeading() {
        BigNum number = new BigNum("001");
        assertEquals("1", number.toString());
    }
    @Test
    public void testLinkedListEqual() {
        LinkedList<Integer> list1 = new LinkedList<Integer>();
        LinkedList<Integer> list2 = new LinkedList<Integer>();

        for (int i=1; i<10; i++) {
            list1.add(i);
        };
        for (int i=1; i<10; i++) {
            list2.add(i);
        };

        assertTrue(
            "Two linked list should be the same,",
            list1.equals(list2)
        );
    }
}