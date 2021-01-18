import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    private ArrayBlockingQueue<Car> tunnelCapacity;
    private Semaphore capacity;


    public Tunnel() {

        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        tunnelCapacity = new ArrayBlockingQueue<>(MainClass.CARS_COUNT / 2);
        capacity = new Semaphore(MainClass.CARS_COUNT / 2);
    }

    @Override
    public void go(Car c) {

        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
//                capacity.acquire();
//                tunnelCapacity.put(c);
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
//                capacity.release();
//                tunnelCapacity.take();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
