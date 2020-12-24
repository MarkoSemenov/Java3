import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lesson1 {

    public static void main(String[] args) {

        int rand = (int) (Math.random() * 50);

        Integer[] array = {5, 8, 12, 14, 75, 90};
        Double[] arrayDouble = {5.5, 8.1, 4.3, 14.2, 7.5, 9.0};
        String[] strArr = {"88", "3", "22", "15"};

        swap(1, 3, array);
        swap(2, 4, arrayDouble);
        swap(0, 3, strArr);

        Box<Apple> appleGoldenBox = new Box<>(new Apple("Голден", 1.0f));
        Box<Apple> appleAntonovkaBox = new Box<>(new Apple("Антоновка", 1.0f));

        Box<Apple> apple = new Box<>(new Apple("Гренни", 1.0f));
        apple.putSomeFruitInTheBox(new Apple("Гренни", 1.0f));
        apple.putSomeFruitInTheBox(new Apple("Гренни", 1.0f));

        Box<Orange> orangeBox = new Box<>(new Orange("Желтый апельсин", 1.5f));
        orangeBox.putSomeFruitInTheBox(new Orange("Желтый апельсин", 1.5f));

        for (int i = 0; i < rand; i++) {
            appleGoldenBox.putSomeFruitInTheBox(new Apple("Голден", 1.0f));
            appleAntonovkaBox.putSomeFruitInTheBox(new Apple("Антоновка", 1.0f));
        }


        System.out.println("\nВ коробке " + appleGoldenBox.getFruit().type + ": " + appleGoldenBox.getFruitBox().size() + " шт.");
        System.out.println("В коробке " + appleAntonovkaBox.getFruit().type + ": " + appleAntonovkaBox.getFruitBox().size() + " шт.");
        System.out.println("В коробке " + orangeBox.getFruit().type + ": " + orangeBox.getFruitBox().size() + " шт.\n");

        appleAntonovkaBox.takeFruitsFromBox(appleGoldenBox);
        System.out.println("Высыпали из одной коробки в другую");
        System.out.println("Результат: " + appleGoldenBox.getFruitBox().size() + " шт.");
        System.out.println("Результат: " + appleAntonovkaBox.getFruitBox().size() + " шт.");

        System.out.println("\nВес коробки " + appleGoldenBox.getFruit().type + ": " + appleGoldenBox.getWeight());
        System.out.println("Вес коробки " + appleAntonovkaBox.getFruit().type + ": " + appleAntonovkaBox.getWeight());
        System.out.println("Вес коробки " + orangeBox.getFruit().type + ": " + orangeBox.getWeight());


        System.out.println(appleAntonovkaBox.compare(orangeBox));
        System.out.println(apple.compare(orangeBox));
    }

    public static <T> void swap(int i, int k, T... array) {
        T a = array[i];
        array[i] = array[k];
        array[k] = a;
        String inf = array.getClass().getName();
        System.out.println(inf.substring(12, inf.length()-1) + ":" + Arrays.toString(array));
    }


    public static <T> List<T> getArrListFromArr(T... array) {
        return new ArrayList<T>(Arrays.asList(array));
    }


}
