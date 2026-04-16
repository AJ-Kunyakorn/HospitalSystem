package com.healthcare.models;

/**
 * Represents a patient in the appointment system.
 * Extends User to add patient-specific attributes.
 */
public class Patient extends User {
    private String idNumber;
    private boolean isGuest;

    /**
     * Constructor for Patient.
     *
     * @param id       unique identifier
     * @param firstName patient's first name
     * @param lastName patient's last name
     * @param phone    patient's phone number
     * @param email    patient's email address
     * @param password patient's password
     * @param idNumber patient's ID number
     * @param isGuest  whether the patient is a guest
     */
    public Patient(int id, String firstName, String lastName, String phone, String email, 
                   String password, String idNumber, boolean isGuest) {
        super(id, firstName, lastName, phone, email, password);
        this.idNumber = idNumber;
        this.isGuest = isGuest;
    }

    /**
     * Gets the patient's ID number.
     *
     * @return the ID number
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * Sets the patient's ID number.
     *
     * @param idNumber the ID number to set
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * Checks if this patient is a guest.
     *
     * @return true if guest, false otherwise
     */
    public boolean isGuest() {
        return isGuest;
    }

    /**
     * Sets whether this patient is a guest.
     *
     * @param guest true to mark as guest, false otherwise
     */
    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    /**
     * Returns a string representation of the patient.
     *
     * @return formatted patient information
     */
    @Override
    public String toString() {
        return getId() + "," + getFirstName() + "," + getLastName() + "," + getPhone() + "," 
               + getEmail() + "," + getPassword() + "," + idNumber + "," + isGuest;
    }
}
