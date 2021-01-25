public class Test1 {

    private int a;
    private int b;

    public Test1(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @BeforeSuite
    public void init() {
        System.out.println("Test1:");
        System.out.format("Date initialization: a = %d, b = %d\n", a, b);
    }

    @Test(priority = 6)
    public void sum() {
        System.out.format("Sum: %d\n", (a + b));
    }

    @Test(priority = 9)
    public void divide() {
        System.out.format("Divide: %d\n", (a / b));
    }

    @Test(priority = 10)
    public void multiplication() {
        System.out.format("Multiplication: %d\n", (a * b));
    }

    @Test(priority = 1)
    public void minus() {
        System.out.format("Minus %d\n", (a - b));
    }

    @AfterSuite
    public void end() {
        System.out.println("End");
    }

}
