package com.healthcare.ui;


import java.time.LocalDate;
import java.util.Scanner;


import com.healthcare.exceptions.InvalidAppointmentException;
import com.healthcare.models.Appointment;
import com.healthcare.models.AppointmentStatus;
import com.healthcare.models.GuestUser;
import com.healthcare.models.Patient;
import com.healthcare.models.SymptomForm;
import com.healthcare.repository.DataRepository;
import com.healthcare.utils.ValidationUtils;


/**
 * Console application demonstrating patient appointment workflows.
 * Manages guest and registered patient registration, appointment scheduling, and symptom management.
 */
public class Main {
    /**
     * Application entry point.
     *
     * @param args runtime arguments
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            DataRepository<Patient> patientRepository = new DataRepository<>();
            DataRepository<SymptomForm> symptomRepository = new DataRepository<>();
            DataRepository<Appointment> appointmentRepository = new DataRepository<>();


            // Load existing data from files
            patientRepository.loadFromFile("patients.txt", Main::parsePatient);
            symptomRepository.loadFromFile("symptoms.txt", Main::parseSymptomForm);
            appointmentRepository.loadFromFile("appointments.txt", Main::parseAppointment);


            // Add shutdown hook to save data on unexpected termination
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nSaving data due to program termination...");
                saveAll(patientRepository, symptomRepository, appointmentRepository);
            }));


            System.out.println("=== Appointment Console System ===");
            boolean appRunning = true;


            while (appRunning) {
                Patient currentPatient = null;
                while (currentPatient == null && appRunning) {
                    printDivider();
                    System.out.println("1. Login as Guest");
                    System.out.println("2. Login as Patient");
                    System.out.println("   (Existing patients sign in with email and password.)");
                    System.out.println("3. Register as Patient");
                    System.out.println("   (New patients create an account with email and password.)");
                    System.out.println("4. Exit");
                    int choice = ValidationUtils.getValidatedInteger(scanner, "Select option: ", 1, 4);


                    switch (choice) {
                        case 1:
                            currentPatient = loginAsGuest(scanner, patientRepository);
                            break;
                        case 2:
                            currentPatient = loginAsPatient(scanner, patientRepository);
                            break;
                        case 3:
                            currentPatient = registerAsPatient(scanner, patientRepository);
                            break;
                        case 4:
                            saveAll(patientRepository, symptomRepository, appointmentRepository);
                            System.out.println("Goodbye!");
                            appRunning = false;
                            break;
                        default:
                            break;
                    }
                }


                if (!appRunning) {
                    break;
                }
                SymptomForm currentForm = null;


                Appointment appointment = null;
                if (currentPatient.isGuest()) {
                    appointment = scheduleGuestAppointment(scanner, currentPatient, appointmentRepository, currentForm);
                }
                if (appointment == null) {
                    appointment = findAppointmentForPatient(appointmentRepository, currentPatient.getId());
                }


                String displayFirstName = ValidationUtils.capitalizeName(currentPatient.getFirstName());
                String displayLastName = ValidationUtils.capitalizeName(currentPatient.getLastName());
                System.out.println("\nWelcome, " + displayFirstName + " " + displayLastName + (currentPatient.isGuest() ? " (Guest)" : "") + "!");
                System.out.println("Symptom details submitted. You can update them from the menu.");


                boolean sessionActive = true;
                while (sessionActive) {
                    printDivider();
                    System.out.println("\n=== Patient Menu ===");
                    int maxOption;
                    if (currentPatient.isGuest()) {
                        System.out.println("1. Register as Patient");
                        System.out.println("2. View Appointment");
                        System.out.println("3. Logout");
                        System.out.println("4. Exit");
                        maxOption = 4;
                    } else {
                        System.out.println("1. Submit or update symptom details");
                        System.out.println("2. Choose Date");
                        System.out.println("3. View Appointment");
                        System.out.println("4. Logout");
                        System.out.println("5. Exit");
                        maxOption = 5;
                    }
                    int selection = ValidationUtils.getValidatedInteger(scanner, "Select option: ", 1, maxOption);


                    if (currentPatient.isGuest()) {
                        switch (selection) {
                            case 1:
                                int oldId = currentPatient.getId();
                                currentPatient = registerGuestAsPatient(scanner, currentPatient, patientRepository);
                                // Update all appointments from old guest ID to new patient ID
                                for (Appointment app : appointmentRepository.getAll()) {
                                    if (app.getPatientId() == oldId) {
                                        app.setPatientId(currentPatient.getId());
                                    }
                                }
                                break;
                            case 2:
                                if (appointment == null) {
                                    System.out.println("No appointment found.");
                                    break;
                                }
                                currentPatient = reviewAppointment(scanner, appointment, currentPatient, patientRepository, symptomRepository);
                                break;
                            case 3:
                                System.out.println("Logging out. Returning to the login menu.");
                                sessionActive = false;
                                break;
                            case 4:
                                saveAll(patientRepository, symptomRepository, appointmentRepository);
                                System.out.println("Data saved. Exiting.");
                                sessionActive = false;
                                appRunning = false;
                                break;
                            default:
                                System.out.println("Please enter a valid menu option.");
                                break;
                        }
                    } else {
                        switch (selection) {
                            case 1: {
                                if (currentForm == null) {
                                currentForm = submitSymptomForm(scanner, currentPatient);
                                symptomRepository.add(currentForm);
                                } else {
                                    updateSymptomForm(scanner, currentForm);
                                }
                                break;
                            }
                            case 2:
                                if (appointment == null) {
                                    appointment = new Appointment(getNextAppointmentId(appointmentRepository), currentPatient.getId(), "", "");
                                }
                                LocalDate appointmentDate = ValidationUtils.getValidatedDate(scanner,
                                    "Enter desired appointment date (YYYY-MM-DD) [Earliest: " + LocalDate.now() + "]: ");
                                appointment.setAppointmentDate(appointmentDate.toString());
                                appointment.proposeTime("10:00 AM");


                                appointment.setSymptomForm(currentForm);


                                if (!appointmentRepository.getAll().contains(appointment)) {
                                    appointmentRepository.add(appointment);
                                }
                                System.out.println("Appointment date chosen and time proposed.");
                                break;
                            case 3:
                                if (appointment == null) {
                                    System.out.println("No appointment found. Please choose a date first.");
                                    break;
                                }
                                currentPatient = reviewAppointment(scanner, appointment, currentPatient, patientRepository, symptomRepository);
                                break;
                            case 4:
                                System.out.println("Logging out. Returning to the login menu.");
                                sessionActive = false;
                                break;
                            case 5:
                                saveAll(patientRepository, symptomRepository, appointmentRepository);
                                System.out.println("Data saved. Exiting.");
                                sessionActive = false;
                                appRunning = false;
                                break;
                            default:
                                System.out.println("Please enter a valid menu option.");
                                break;
                        }
                    }
                }
            }


            saveAll(patientRepository, symptomRepository, appointmentRepository);
        }
    }


    /**
     * Prompts the user to log in as a guest and creates a guest patient.
     *
     * @param scanner           scanner for keyboard input
     * @param patientRepository repository for patients
     * @return created patient or null on invalid input
     */
    private static Patient loginAsGuest(Scanner scanner, DataRepository<Patient> patientRepository) {
        String firstName = ValidationUtils.getValidatedName(scanner, "Enter first name: ");
        String lastName = ValidationUtils.getValidatedName(scanner, "Enter last name: ");
        String idNumber = ValidationUtils.getValidatedNonEmptyString(scanner, "Enter guest ID number: ", "Guest ID number cannot be empty.");


        GuestUser guestUser = new GuestUser(getNextGuestId(patientRepository), firstName, lastName, idNumber);
        Patient patient = guestUser.enterDetails();
        patientRepository.add(patient);
        return patient;
    }


    /**
     * Prompts the user to log in as an existing patient.
     *
     * @param scanner           scanner for keyboard input
     * @param patientRepository repository for patients
     * @return logged in patient or null on invalid input
     */
    private static Patient loginAsPatient(Scanner scanner, DataRepository<Patient> patientRepository) {
        String email = ValidationUtils.getValidatedEmail(scanner, "Enter email: ");


        Patient existingPatient = findPatientByEmail(patientRepository, email);
        if (existingPatient != null) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();
            if (existingPatient.getPassword().equals(password)) {
                System.out.println("Patient login successful.");
                return existingPatient;
            }
            System.out.println("Incorrect password for that email. Please try again.");
            return null;
        }


        System.out.println("No patient account found with that email. Please register as a new patient.");
        return null;
    }


    /**
     * Prompts the user to register as a new patient.
     *
     * @param scanner           scanner for keyboard input
     * @param patientRepository repository for patients
     * @return registered patient or null on invalid input
     */
    private static Patient registerAsPatient(Scanner scanner, DataRepository<Patient> patientRepository) {
        String email = ValidationUtils.getValidatedEmail(scanner, "Enter email: ");


        // Verify email doesn't already exist
        if (findPatientByEmail(patientRepository, email) != null) {
            System.out.println("Error: An account with that email already exists. Please login instead.");
            return null;
        }


        String firstName = ValidationUtils.getValidatedName(scanner, "Enter first name: ");
        String lastName = ValidationUtils.getValidatedName(scanner, "Enter last name: ");
        String phone = ValidationUtils.getValidatedPhone(scanner, "Enter phone: ");
        String password = ValidationUtils.getValidatedNonEmptyString(scanner, "Enter password: ", "Password cannot be empty.");
        String idNumber = ValidationUtils.getValidatedNonEmptyString(scanner, "Enter ID number: ", "ID number cannot be empty.");


        Patient patient = new Patient(getNextPatientId(patientRepository), firstName, lastName, phone, email, password, idNumber, false);
        patientRepository.add(patient);
        System.out.println("Patient account registered successfully.");
        return patient;
    }


    private static Patient findPatientByEmail(DataRepository<Patient> patientRepository, String email) {
        for (Patient patient : patientRepository.getAll()) {
            if (patient.getEmail().equalsIgnoreCase(email)) {
                return patient;
            }
        }
        return null;
    }


    /**
     * Collects symptom information from the patient.
     *
     * @param scanner scanner for keyboard input
     * @param patient current patient
     * @return submitted symptom form
     */
    private static SymptomForm submitSymptomForm(Scanner scanner, Patient patient) {
        String symptoms = ValidationUtils.getValidatedNonEmptyString(scanner, "Describe your symptoms: ", "Symptoms cannot be empty.");
        String description = ValidationUtils.getValidatedNonEmptyString(scanner, "Enter details/description: ", "Description cannot be empty.");
        String severity = ValidationUtils.getValidatedSeverity(scanner, "Enter severity (Low/Medium/High): ");
        return new SymptomForm(patient.getId(), symptoms, description, severity);
    }


    private static void updateSymptomForm(Scanner scanner, SymptomForm form) {
        System.out.println("Update the current symptom form. Leave blank to keep the current value.");
        System.out.println("Current symptoms: " + form.getSymptoms());
        System.out.print("Enter additional symptoms: ");
        String moreSymptoms = scanner.nextLine().trim();
        if (!moreSymptoms.isEmpty()) {
            form.setSymptoms(form.getSymptoms() + "; " + moreSymptoms);
        }


        System.out.println("Current description: " + form.getDescription());
        System.out.print("Enter additional details: ");
        String moreDetails = scanner.nextLine().trim();
        if (!moreDetails.isEmpty()) {
            form.setDescription(form.getDescription() + " " + moreDetails);
        }


        System.out.println("Current severity: " + form.getSeverity());
        System.out.print("Enter severity (Low/Medium/High) or leave blank to keep current: ");
        String severityInput = scanner.nextLine().trim();
        if (!severityInput.isEmpty()) {
            while (true) {
                if (severityInput.equalsIgnoreCase("low")) {
                    form.setSeverity("Low");
                    break;
                }
                if (severityInput.equalsIgnoreCase("medium")) {
                    form.setSeverity("Medium");
                    break;
                }
                if (severityInput.equalsIgnoreCase("high")) {
                    form.setSeverity("High");
                    break;
                }
                System.out.println("Invalid severity. Please enter Low, Medium, or High.");
                System.out.print("Enter severity (Low/Medium/High) or leave blank to keep current: ");
                severityInput = scanner.nextLine().trim();
                if (severityInput.isEmpty()) {
                    break;
                }
            }
        }
    }


    private static void printDivider() {
        System.out.println("\n----------------------------------------");
    }


    private static Appointment scheduleGuestAppointment(Scanner scanner,Patient guestPatient, DataRepository<Appointment> appointmentRepository, SymptomForm currentForm) {
    LocalDate appointmentDate = ValidationUtils.getValidatedDate(
        scanner,
        "Enter desired appointment date (YYYY-MM-DD): "
    );


    Appointment appointment = new Appointment(
        getNextAppointmentId(appointmentRepository),
        guestPatient.getId(),
        appointmentDate.toString(),
        ""
    );


    appointment.proposeTime("10:00 AM");
    appointment.setSymptomForm(currentForm);
    appointmentRepository.add(appointment);


    return appointment;
}


    private static Patient offerGuestPromotion(Scanner scanner, Patient guestPatient, DataRepository<Patient> patientRepository) {
        System.out.print("Would you like to register as a Patient to save this record? (Y/N): ");
        String response = scanner.nextLine().trim();
        if (response.equalsIgnoreCase("Y")) {
            return registerGuestAsPatient(scanner, guestPatient, patientRepository);
        }
        return guestPatient;
    }


    private static Patient registerGuestAsPatient(Scanner scanner, Patient guestPatient, DataRepository<Patient> patientRepository) {
        String email = ValidationUtils.getValidatedEmail(scanner, "Enter email for your new account: ");
        if (findPatientByEmail(patientRepository, email) != null) {
            System.out.println("Error: An account with that email already exists. Returning to session as guest.");
            return guestPatient;
        }


        String phone = ValidationUtils.getValidatedPhone(scanner, "Enter phone: ");
        String password = ValidationUtils.getValidatedNonEmptyString(scanner, "Enter password: ", "Password cannot be empty.");


        Patient patient = new Patient(getNextPatientId(patientRepository), guestPatient.getFirstName(), guestPatient.getLastName(), phone,
            email, password, guestPatient.getIdNumber(), false);
        patientRepository.add(patient);
        System.out.println("Patient account registered successfully.");
        return patient;
    }


    private static int getNextGuestId(DataRepository<Patient> patientRepository) {
        int maxId = 0;
        for (Patient patient : patientRepository.getAll()) {
            if (patient.getId() > maxId) {
                maxId = patient.getId();
            }
        }
        return maxId + 1;
    }


    private static int getNextPatientId(DataRepository<Patient> patientRepository) {
        int maxId = 0;
        for (Patient patient : patientRepository.getAll()) {
            if (patient.getId() > maxId) {
                maxId = patient.getId();
            }
        }
        return maxId + 1;
    }


    private static int getNextAppointmentId(DataRepository<Appointment> appointmentRepository) {
        int maxId = 0;
        for (Appointment appointment : appointmentRepository.getAll()) {
            if (appointment.getAppointmentId() > maxId) {
                maxId = appointment.getAppointmentId();
            }
        }
        return maxId + 1;
    }


    /**
     * Displays the proposed appointment and lets the patient accept or reject it.
     *
     * @param scanner     scanner for keyboard input
     * @param appointment current appointment
     * @param currentPatient the current patient
     * @param patientRepository repository for patients
     * @return the updated patient (may change if promoted from guest)
     */
    private static Patient reviewAppointment(Scanner scanner, Appointment appointment, Patient currentPatient, DataRepository<Patient> patientRepository, DataRepository<SymptomForm> symptomRepository) {
        SymptomForm form =appointment.getSymptomForm();


        if (form == null) {
            form = findSymptomByPatientId(symptomRepository, currentPatient.getId());
        }


        System.out.println("\n--- Symptom Details ---");
            if (form != null) {
                System.out.println("Symptoms: " + form.getSymptoms());
                System.out.println("Description: " + form.getDescription());
                System.out.println("Severity: " + form.getSeverity());
            } else {
            System.out.println("No symptom information found.");
            }
        System.out.println("\nCurrent Appointment");
        System.out.println("Date: " + appointment.getAppointmentDate());
        System.out.println("Proposed time: " + appointment.getProposedTime());
        System.out.println("Status: " + appointment.getStatus());


        if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
            System.out.println("This appointment is already confirmed.");
            return currentPatient;
        }


        if (appointment.getStatus() != AppointmentStatus.TIME_PROPOSED) {
            System.out.println("No proposed appointment time is available. Please submit a symptom form or choose a date first.");
            return currentPatient;
        }


        System.out.println("1. Accept");
        System.out.println("2. Reject");
        int response = ValidationUtils.getValidatedInteger(scanner, "Choose option: ", 1, 2);


        switch (response) {
            case 1:
                try {
                    appointment.confirmTime();
                    System.out.println("Appointment confirmed.");
                    if (currentPatient.isGuest()) {
                        currentPatient = offerGuestPromotion(scanner, currentPatient, patientRepository);
                    }
                    // If patient was promoted from guest, update appointment patient ID
                    if (!currentPatient.isGuest()) {
                        appointment.setPatientId(currentPatient.getId());
                    }
                } catch (InvalidAppointmentException e) {
                    System.out.println("Cannot confirm appointment: " + e.getMessage());
                }
                break;
            case 2:
                boolean movedToNextDay = appointment.rejectAndRepropose();
                if (movedToNextDay) {
                    System.out.println("No more slots available today. Proposing new time for the next available day.");
                }
                System.out.println("Appointment rejected. New proposed time: " + appointment.getProposedTime());
                break;
            default:
                break;
        }
        return currentPatient;
    }


    /**
     * Persists all repository data to disk.
     *
     * @param patientRepository     repository for patient objects
     * @param symptomRepository     repository for symptom forms
     * @param appointmentRepository repository for appointment objects
     */
    private static void saveAll(DataRepository<Patient> patientRepository,
                                DataRepository<SymptomForm> symptomRepository,
                                DataRepository<Appointment> appointmentRepository) {
        System.out.println("Saving all data to files...");
        patientRepository.saveToFile("patients.txt");
        symptomRepository.saveToFile("symptoms.txt");
        appointmentRepository.saveToFile("appointments.txt");
        System.out.println("Data saved to patients.txt, symptoms.txt, appointments.txt");
    }


    /**
     * Parses a Patient object from a comma-separated string.
     *
     * @param line the line to parse
     * @return the parsed Patient or null if parsing fails
     */
    private static Patient parsePatient(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length != 8) return null;
            int id = Integer.parseInt(parts[0]);
            String firstName = parts[1];
            String lastName = parts[2];
            String phone = parts[3];
            String email = parts[4];
            String password = parts[5];
            String idNumber = parts[6];
            boolean isGuest = Boolean.parseBoolean(parts[7]);
            return new Patient(id, firstName, lastName, phone, email, password, idNumber, isGuest);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Parses a SymptomForm object from a comma-separated string.
     *
     * @param line the line to parse
     * @return the parsed SymptomForm or null if parsing fails
     */
    private static SymptomForm parseSymptomForm(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length != 4) return null;
            int patientId = Integer.parseInt(parts[0]);
            String symptoms = parts[1];
            String description = parts[2];
            String severity = parts[3];
            return new SymptomForm(patientId, symptoms, description, severity);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Parses an Appointment object from a comma-separated string.
     *
     * @param line the line to parse
     * @return the parsed Appointment or null if parsing fails
     */
    private static Appointment parseAppointment(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length != 5) return null;
            int appointmentId = Integer.parseInt(parts[0]);
            int patientId = Integer.parseInt(parts[1]);
            String appointmentDate = parts[2];
            String proposedTime = parts[3];
            AppointmentStatus status = AppointmentStatus.valueOf(parts[4]);
            Appointment appointment = new Appointment(appointmentId, patientId, appointmentDate, proposedTime);
            // Set the status by manipulating the appointment state
            if (status == AppointmentStatus.CONFIRMED) {
                appointment.confirmTime();
            } else if (status == AppointmentStatus.REJECTED) {
                appointment.rejectAndRepropose();
            }
            return appointment;
        } catch (Exception e) {
            return null;
        }
    }


    private static Appointment findAppointmentForPatient(DataRepository<Appointment> appointmentRepository, int patientId) {
        for (Appointment app : appointmentRepository.getAll()) {
            if (app.getPatientId() == patientId) {
                return app;
            }
        }
        return null;
    }


    private static SymptomForm findSymptomByPatientId(DataRepository<SymptomForm> repo, int patientId) {
        SymptomForm latest = null;


    for (SymptomForm form : repo.getAll()) {
        if (form.getPatientId() == patientId) {
            latest = form; // keep ล่าสุด
        }
    }


    return latest;
}
}

