public class Test2 {

    private int a;
    private int b;

    public Test2(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @BeforeSuite
    public void printInfo() {
        System.out.println("Test2:");
        System.out.format("Date initialization: a = %d, b = %d\n", a, b);
    }

    public int factorial(int num) {
        int result = 1;
        if (num < 0) {
            return 0;
        }
        for (int i = 1; i <= num; i++) {
            result *= i;
        }
        return result;
    }

    @Test(priority = 6)
    public void printFactorialA() {
        System.out.println("Factorial of " + a + " = " + factorial(a));
    }

    @Test(priority = 10)
    public void printFactorialB() {
        System.out.println("Factorial of " + b + " = " + factorial(b));
    }

}
