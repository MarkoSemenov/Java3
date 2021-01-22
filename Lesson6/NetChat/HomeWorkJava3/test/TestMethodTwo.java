import lesson6.Main;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestMethodTwo {

    @Test
    public void testMethodTwo1() {
        int[] a = {1, 1, 4, 1, 4};
        Assertions.assertTrue(Main.methodTwo(a));
    }

    @Test
    public void testMethodTwo2() {
        int[] a = {4, 4, 4, 4, 4};
        Assertions.assertTrue(Main.methodTwo(a));
    }

    @Test
    public void testMethodTwo3() {
        int[] a = {4, 4, 4, 4, 1};
        Assertions.assertTrue(Main.methodTwo(a));
    }

    @Test
    public void testMethodTwo4() {
        int[] a = {1, 1, 1, 1, 1};
        Assertions.assertTrue(Main.methodTwo(a));
    }

    @Test
    public void testMethodTwo5() {
        int[] a = {};
        Assertions.assertTrue(Main.methodTwo(a));
    }
}
