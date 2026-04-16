package com.healthcare.models;

/**
 * Data Transfer Object (DTO) for guest user registration.
 * Converts guest details to a Patient object.
 */
public class GuestUser {
    private int guestId;
    private String firstName;
    private String lastName;
    private String idNumber;

    /**
     * Constructor for GuestUser.
     *
     * @param guestId  auto-generated guest identifier
     * @param firstName guest's first name
     * @param lastName guest's last name
     * @param idNumber guest's ID number
     */
    public GuestUser(int guestId, String firstName, String lastName, String idNumber) {
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
    }

    /**
     * Gets the guest ID.
     *
     * @return the guest ID
     */
    public int getGuestId() {
        return guestId;
    }

    /**
     * Sets the guest ID.
     *
     * @param guestId the guest ID to set
     */
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    /**
     * Gets the guest's first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the guest's first name.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the guest's last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the guest's last name.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the guest's ID number.
     *
     * @return the ID number
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * Sets the guest's ID number.
     *
     * @param idNumber the ID number to set
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * Converts guest details to a Patient object.
     *
     * @return Patient object with guest flag set to true
     */
    public Patient enterDetails() {
        return new Patient(guestId, firstName, lastName, "", "", "", idNumber, true);
    }
}
