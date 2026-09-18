import static org.junit.Assert.assertEquals;
import org.junit.Test;

@Test
public void testBigNumNoZeroLeading() {
    BigNum bad = new BigNum("001");
    assertFalse(
            "BigNum's leading 0 should be removed.",
            bad.toString().startsWith("0")
    );
}