import java.time.LocalDate;

public class Appointment {

    private String id;
    private String patientId;
    private LocalDate selectedDate;
    private String proposedTime;
    private AppointmentStatus status;

    public Appointment(String id, String patientId, LocalDate date) {
        this.id = id;
        this.patientId = patientId;
        this.selectedDate = date;
        this.status = AppointmentStatus.PENDING;
    }

    public void proposeTime(String time) throws InvalidActionException {
        if (status != AppointmentStatus.PENDING)
            throw new InvalidActionException("Cannot propose time!");

        this.proposedTime = time;
        this.status = AppointmentStatus.TIME_PROPOSE;

        System.out.println("Doctor proposed time: " + time);
    }

    public void confirm() throws InvalidActionException {
        if (status != AppointmentStatus.TIME_PROPOSE)
            throw new InvalidActionException("Cannot confirm!");

        status = AppointmentStatus.CONFIRMED;
    }

    public void reject() {
        status = AppointmentStatus.PENDING;
        System.out.println("Rejected → Back to PENDING");
    }

    public String getProposedTime() { return proposedTime; }
    public AppointmentStatus getStatus() { return status; }
}