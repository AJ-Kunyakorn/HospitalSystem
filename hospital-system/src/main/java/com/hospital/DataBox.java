// Parametric Polymorphism using Generics
// Can store any data type safely
public class DataBox<T> {

    private T data;

    public void set(T data) {
        this.data = data;
    }

    public T get() {
        return data;
    }
}