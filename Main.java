import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("===== PATIENT APPOINTMENT SYSTEM =====");

        Patient p;

        // Input validation
        String guest;
        while (true) {
            System.out.print("Are you guest? (y/n): ");
            guest = sc.nextLine().trim();
            if (guest.equalsIgnoreCase("y") || guest.equalsIgnoreCase("n")) break;
        }

        if (guest.equalsIgnoreCase("y")) {
            GuestUser g = new GuestUser();
            String q = g.generatePK();
            p = new Patient(q, "Guest", "-", "-", true);
            System.out.println("Queue: " + q);
        } else {

            String fname;
            while (true) {
                System.out.print("First Name: ");
                fname = sc.nextLine().trim();
                if (fname.matches("[a-zA-Z]+")) break;
            }

            String lname;
            while (true) {
                System.out.print("Last Name: ");
                lname = sc.nextLine().trim();
                if (lname.matches("[a-zA-Z]+")) break;
            }

            String id;
            while (true) {
                System.out.print("ID (13 digits): ");
                id = sc.nextLine();
                if (id.matches("\\d{13}")) break;
            }

            p = new Patient("P001", fname, lname, id, false);
        }

        // Symptom
        SymptomForm form = new SymptomForm("F001", p.id);
        System.out.print("Symptom: ");
        form.addSymptom(sc.nextLine());
        p.submitSymptom(form);

        // Date
        LocalDate date;
        while (true) {
            try {
                System.out.print("Enter date (yyyy-MM-dd): ");
                date = LocalDate.parse(sc.nextLine());

                LocalDate today = LocalDate.now();

                if (!date.isAfter(today)) {
                    System.out.println("Must book 1 day ahead");
                    continue;
                }

                if (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    System.out.println("Weekend closed");
                    continue;
                }

                break;

            } catch (DateTimeParseException e) {
                System.out.println("Invalid format");
            }
        }

        p.showSelectedDate(date);

        Appointment app = new Appointment("A001", p.id, date);

        try {
            int hour = 9, minute = 0;
            boolean confirmed = false;

            while (!confirmed) {

                String time = String.format("%02d:%02d", hour, minute);
                app.proposeTime(time);

                System.out.print("Accept? (y/n): ");
                String c = sc.nextLine();

                if (c.equalsIgnoreCase("y")) {
                    app.confirm();
                    confirmed = true;
                } else {
                    app.reject();

                    minute += 20;
                    if (minute >= 60) { minute = 0; hour++; }

                    if (hour >= 17) {
                        System.out.println("No more slots");
                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        p.addAppointment(app);

        // Parametric Polymorphism usage
        DataBox<Appointment> box = new DataBox<>();
        box.set(app);

        // SUMMARY
        System.out.println("\n===== SUMMARY =====");
        System.out.println("ID: " + p.id);
        System.out.println("Name: " + p.getFirstName() + " " + p.getLastName());
        System.out.println("Symptom: " + form.getSymptoms());
        System.out.println("Date: " + date);
        System.out.println("Time: " + box.get().getProposedTime());
        System.out.println("Status: " + box.get().getStatus());
    }
}
