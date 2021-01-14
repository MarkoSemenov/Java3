package Lesson4;

import java.util.concurrent.Future;

public class Lesson4 {

    public static final Object lock = new Object();
    public static volatile char letter = 'A';

    public static void main(String[] args) {

        threadOneGo();
        threadTwoGo();
        threadThreeGo();



    }

    public static void threadOneGo() {

        new Thread(() -> {
            synchronized (lock) {
                try {
                    for (int i = 0; i < 5; i++) {
                        while (letter != 'A') {
                            lock.wait();
                        }
                        System.out.print(letter);
                        letter = 'B';
                        lock.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread A").start();
    }


    public static void threadTwoGo() {

        new Thread(() -> {
            synchronized (lock) {
                try {
                    for (int i = 0; i < 5; i++) {
                        while (letter != 'B') {
                            lock.wait();
                        }
                        System.out.print(letter);
                        letter = 'C';
                        lock.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread B").start();
    }

    public static void threadThreeGo() {
        new Thread(() -> {
            synchronized (lock) {
                try {
                    for (int i = 0; i < 5; i++) {
                        while (letter != 'C') {
                            lock.wait();
                        }
                        System.out.print(letter);
                        letter = 'A';
                        lock.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread C").start();

    }
}
