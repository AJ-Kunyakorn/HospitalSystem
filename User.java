// Abstract class (Inheritance base)
// Helps reuse common attributes and enforce login behavior
public abstract class User {
    protected String id;
    protected String firstName;
    protected String lastName;

    public User(String id, String fname, String lname) {
        this.id = id;
        this.firstName = fname;
        this.lastName = lname;
    }

    public abstract boolean login(); // Polymorphism

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}