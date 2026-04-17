package com.healthcare.models;

import com.healthcare.exceptions.InvalidAppointmentException;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Represents an appointment with state machine and business hours validation.
 */
public class Appointment {
    private static final int BUSINESS_OPEN = 9;
    private static final int BUSINESS_CLOSE = 17;
    private static final String TIME_FORMATTER = "h:mm a";

    private int appointmentId;
    private int patientId;
    private String appointmentDate;
    private String proposedTime;
    private AppointmentStatus status;
    private SymptomForm symptomForm;

    /**
     * Constructor for Appointment.
     *
     * @param appointmentId   unique appointment identifier
     * @param patientId       the patient's ID
     * @param appointmentDate appointment date (YYYY-MM-DD format)
     * @param proposedTime    proposed time (h:mm a format)
     */
    public Appointment(int appointmentId, int patientId, String appointmentDate, String proposedTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.proposedTime = proposedTime;
        this.status = AppointmentStatus.PENDING;
    }

    /**
     * Gets the appointment ID.
     *
     * @return the appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the appointment ID.
     *
     * @param appointmentId the appointment ID to set
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets the patient ID.
     *
     * @return the patient ID
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     * Sets the patient ID.
     *
     * @param patientId the patient ID to set
     */
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    /**
     * Gets the appointment date.
     *
     * @return the appointment date
     */
    public String getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Sets the appointment date.
     *
     * @param appointmentDate the appointment date to set
     */
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    /**
     * Gets the proposed time.
     *
     * @return the proposed time
     */
    public String getProposedTime() {
        return proposedTime;
    }

    /**
     * Sets the proposed time.
     *
     * @param proposedTime the proposed time to set
     */
    public void setProposedTime(String proposedTime) {
        this.proposedTime = proposedTime;
    }

    /**
     * Gets the appointment status.
     *
     * @return the status
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Sets the appointment status.
     *
     * @param status the status to set
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Proposes a time for the appointment, enforcing business hours (9 AM - 5 PM).
     * If time is outside business hours, defaults to 9:00 AM.
     *
     * @param time the proposed time
     */
    public void proposeTime(String time) {
        int hour = parseTimeOrDefault(time);
        if (hour < BUSINESS_OPEN || hour >= BUSINESS_CLOSE) {
            this.proposedTime = "9:00 AM";
        } else {
            this.proposedTime = time;
        }
        this.status = AppointmentStatus.TIME_PROPOSED;
    }

    /**
     * Confirms the appointment time. Must be in TIME_PROPOSED status.
     *
     * @throws InvalidAppointmentException if status is not TIME_PROPOSED
     */
    public void confirmTime() throws InvalidAppointmentException {
        if (this.status != AppointmentStatus.TIME_PROPOSED) {
            throw new InvalidAppointmentException("Appointment status must be TIME_PROPOSED to confirm.");
        }
        this.status = AppointmentStatus.CONFIRMED;
    }

    /**
     * Rejects the current appointment and proposes a new time.
     * Increments by 1 hour; if exceeds 17:00, moves to next business day at 09:00.
     * Skips weekends (Saturday/Sunday).
     *
     * @return true if moved to next business day, false if same day
     */
    public boolean rejectAndRepropose() {
        this.status = AppointmentStatus.REJECTED;
        int currentHour = parseTimeOrDefault(this.proposedTime);
        boolean movedToNextDay = false;

        if (currentHour + 1 >= BUSINESS_CLOSE) {
            LocalDate nextDate = getNextBusinessDay(LocalDate.parse(this.appointmentDate));
            this.appointmentDate = nextDate.toString();
            this.proposedTime = "9:00 AM";
            movedToNextDay = true;
        } else {
            this.proposedTime = (currentHour + 1) + ":00 AM";
        }

        this.status = AppointmentStatus.TIME_PROPOSED;
        return movedToNextDay;
    }

    /**
     * Parses time string and returns hour, or defaults to BUSINESS_OPEN on error.
     *
     * @param time time string to parse
     * @return hour value
     */
    private int parseTimeOrDefault(String time) {
        try {
            if (time == null || time.isEmpty()) {
                return BUSINESS_OPEN;
            }
            return Integer.parseInt(time.split(":")[0]);
        } catch (Exception e) {
            return BUSINESS_OPEN;
        }
    }

    /**
     * Gets the next business day, skipping weekends.
     *
     * @param date current date
     * @return next business day
     */
    private LocalDate getNextBusinessDay(LocalDate date) {
        LocalDate nextDay = date.plusDays(1);
        while (nextDay.getDayOfWeek() == DayOfWeek.SATURDAY || nextDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            nextDay = nextDay.plusDays(1);
        }
        return nextDay;
    }

    /**
     * Returns a string representation of the appointment.
     *
     * @return formatted appointment information
     */
    @Override
    public String toString() {
        return appointmentId + "," + patientId + "," + appointmentDate + "," + proposedTime + "," + status;
    }

    public SymptomForm getSymptomForm() {
        return symptomForm;
    }

    public void setSymptomForm(SymptomForm symptomForm) {
        this.symptomForm = symptomForm;
    }
}
