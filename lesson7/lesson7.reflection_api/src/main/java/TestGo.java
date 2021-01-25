import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class TestGo {
    public static void main(String[] args) {

        try {
            start(Test1.class, 88, 35);
            start(Test2.class, 5, 6);
            start(Test2.class, 8, 9);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void start(Class testClass, int... i) throws Exception {

        Object o;
        Method methodWithAfterSuite = null;
        Class cl1 = testClass;

        Constructor constructor = cl1.getConstructor(int.class, int.class);
        o = constructor.newInstance(i[0], i[1]);
        Method[] methods = o.getClass().getDeclaredMethods();

        List<Method> list = new ArrayList<>();


        if ((Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(BeforeSuite.class))
                .count() > 1) || Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AfterSuite.class))
                .count() > 1) {
            throw new RuntimeException("Метод с аннотацией BeforeSuite или AfterSuite может быть только один");
        }
        

        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                method.invoke(o);
            }

            if (method.isAnnotationPresent(Test.class)) {
                list.add(method);
            }

            if (method.isAnnotationPresent(AfterSuite.class)) {
                methodWithAfterSuite = method;
            }
        }


        list.sort(Comparator.comparingInt(method -> method.getAnnotation(Test.class).priority()));
        for (int j = list.size() - 1; j >= 0; j--) {
            list.get(j).invoke(o);
        }

        if (methodWithAfterSuite != null) {
            methodWithAfterSuite.invoke(o);
        }
    }


}
