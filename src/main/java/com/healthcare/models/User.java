package com.healthcare.models;

/**
 * Abstract base class representing a user in the appointment system.
 * Both guests and registered patients extend this class.
 */
public abstract class User {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;

    /**
     * Constructor for User.
     *
     * @param id       unique identifier
     * @param firstName user's first name
     * @param lastName user's last name
     * @param phone    user's phone number
     * @param email    user's email address
     * @param password user's password
     */
    public User(int id, String firstName, String lastName, String phone, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters

    /**
     * Gets the user's unique identifier.
     *
     * @return the user id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user's unique identifier.
     *
     * @param id the user id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user's first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the user's phone number.
     *
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the user's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
