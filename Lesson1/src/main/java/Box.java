import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {

    private T fruit;
    private float weight;
    private final List<T> fruitBox = new ArrayList<>();

    public Box(T fruit) {
        this.fruit = fruit;
        fruitBox.add(fruit);
    }

    public void putSomeFruitInTheBox(T fruit) {
        fruitBox.add(fruit);
    }

    public float getWeight() {
        if (fruitBox.size() != 0) {
            weight = fruit.weight * fruitBox.size();
        }
        return weight;
    }

    public void takeFruitsFromBox(Box<T> box) {
        this.fruitBox.addAll(box.fruitBox);
        box.fruitBox.clear();
    }

    public boolean compare(Box<? extends Fruit> box) {
        return Math.abs(this.getWeight()) == Math.abs(box.getWeight());
    }

    public List<T> getFruitBox() {
        return fruitBox;
    }

    public String getKindOfFruit() {
        return fruit.type;
    }

    public T getFruit() {
        return fruit;
    }

    public void setFruit(T fruit) {
        this.fruit = fruit;
    }

}
