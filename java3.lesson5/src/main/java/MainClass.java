import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {

    public static final int CARS_COUNT = 4;
    public static CyclicBarrier barrier;
    public static ExecutorService executorService;
    public static Lock locker;


    public static void main(String[] args) {
        locker = new ReentrantLock();
        barrier = new CyclicBarrier(CARS_COUNT + 1);
        executorService = Executors.newFixedThreadPool(CARS_COUNT);
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];


        try {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
            for (int i = 0; i < CARS_COUNT; i++) {
                cars[i] = new Car(race, 20 + (int) (Math.random() * 10), barrier, locker);
                executorService.execute(cars[i]);
            }

            barrier.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

            barrier.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

    }
}






