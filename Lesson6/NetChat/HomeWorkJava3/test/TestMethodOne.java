import lesson6.Main;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestMethodOne {

    @Test(expected = RuntimeException.class)
    public void testMethodOne1() {

        int[] a = {1, 5, 5, 6, 6, 1};
        int[] b = {};
        Assertions.assertArrayEquals(b, Main.methodOne(a));
    }

    @Test
    public void testMethodOne2() {
        int[] a = {1, 4, 5, 4, 3, 1, 7, 1, 8};
        int[] b = {3, 1, 7, 1, 8};
        Assertions.assertArrayEquals(b, Main.methodOne(a));
    }

    @Test
    public void testMethodOne3() {
        int[] a = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        int[] b = {1, 7};
        Assertions.assertArrayEquals(b, Main.methodOne(a));
    }

    @Test
    public void testMethodOne4() {
        int[] a = {4, 4, 4, 4, 4};
        int[] b = {};
        Assertions.assertArrayEquals(b, Main.methodOne(a));
    }

}
