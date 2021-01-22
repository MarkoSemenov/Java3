package lesson6;

import java.util.Arrays;

public class Main {

    public static final int NUMBER = 4;

    public static void main(String[] args) {
        int[] array = {1, 2, 4, 4, 2, 3, 2, 4, 7, 9, 3, 1, 5};
        System.out.println(Arrays.toString(methodOne(array)));

        System.out.println(methodTwo(1, 4, 4, 1, 1, 4, 4, 4));
    }

    public static int[] methodOne(int... array) throws RuntimeException {

        if (Arrays.stream(array).noneMatch(i -> i == 4)) {
            throw new RuntimeException("Array don't contain number 4");
        }

        int k = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == NUMBER) {
                k = i;
            }
        }

        return Arrays.stream(array).skip(k + 1).toArray();
    }

    public static Boolean methodTwo(int... array) {
        if (array.length == 0){
            return false;
        }
        return Arrays.stream(array).allMatch(i -> {
            if (Arrays.stream(array).noneMatch(e -> e == 4) || Arrays.stream(array).noneMatch(e -> e == 1)) {
                return false;
            } else return Arrays.stream(array).allMatch(e -> e == 1 || e == 4);
        });
    }

}
