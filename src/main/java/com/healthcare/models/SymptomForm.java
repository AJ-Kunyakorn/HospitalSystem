package com.healthcare.models;

/**
 * Represents a symptom form submitted by a patient.
 */
public class SymptomForm {
    private int patientId;
    private String symptoms;
    private String description;
    private String severity;

    /**
     * Constructor for SymptomForm.
     *
     * @param patientId   the patient who submitted the form
     * @param symptoms    description of symptoms
     * @param description detailed description
     * @param severity    severity level (Low/Medium/High)
     */
    public SymptomForm(int patientId, String symptoms, String description, String severity) {
        this.patientId = patientId;
        this.symptoms = symptoms;
        this.description = description;
        this.severity = severity;
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
     * Gets the symptoms description.
     *
     * @return the symptoms
     */
    public String getSymptoms() {
        return symptoms;
    }

    /**
     * Sets the symptoms description.
     *
     * @param symptoms the symptoms to set
     */
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    /**
     * Gets the detailed description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the detailed description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the severity level.
     *
     * @return the severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Sets the severity level.
     *
     * @param severity the severity to set
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Returns a string representation of the symptom form.
     *
     * @return formatted symptom form information
     */
    @Override
    public String toString() {
        return patientId + "," + symptoms + "," + description + "," + severity;
    }
}
