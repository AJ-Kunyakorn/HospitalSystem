import java.time.LocalDate;
import java.util.*;

// Inheritance: Patient extends User
// Reuse common fields like name, id
public class Patient extends User {

    private String idNumber;
    private boolean isGuest;

    // Generics: ensures type safety (only Appointment allowed)
    private List<Appointment> appointments = new ArrayList<>();

    public Patient(String id, String fname, String lname, String idNumber, boolean isGuest) {
        super(id, fname, lname);
        this.idNumber = idNumber;
        this.isGuest = isGuest;
    }

    @Override
    public boolean login() {
        System.out.println("Patient login...");
        return true;
    }

    public void submitSymptom(SymptomForm form) {
        form.submitForm();
    }

    public void addAppointment(Appointment a) {
        appointments.add(a);
    }

    public void viewAppointments() {
        for (Appointment a : appointments) {
            System.out.println(a);
        }
    }

    public void showSelectedDate(LocalDate d) {
        System.out.println("Selected Date: " + d);
    }
}