# Hospital Appointment System

A Java console application for managing patient appointments with guest and registered user workflows.

## Project Structure

```
src/main/java/com/healthcare/
├── models/               # Data models and entities
│   ├── User.java         # Abstract base class for users
│   ├── Patient.java      # Registered patient entity
│   ├── GuestUser.java    # Guest user DTO
│   ├── Appointment.java  # Appointment with state machine
│   ├── AppointmentStatus.java  # Appointment status enum
│   └── SymptomForm.java  # Patient symptom form
├── repository/           # Data persistence layer
│   └── DataRepository.java   # Generic repository with file I/O
├── exceptions/           # Custom exceptions
│   └── InvalidAppointmentException.java
├── utils/               # Utility functions
│   └── ValidationUtils.java   # Input validation methods
└── ui/                  # User interface layer
    └── Main.java        # Console application entry point
```

## Architecture Overview

### Packages

- **com.healthcare.models**: Entity classes representing users, appointments, and forms
- **com.healthcare.repository**: Generic data repository pattern for type-safe persistence
- **com.healthcare.exceptions**: Custom exceptions for appointment validation
- **com.healthcare.utils**: Centralized validation and utility functions
- **com.healthcare.ui**: Console-based user interface

### Key Features

- **Guest & Patient Workflows**: Different access levels and restrictions
- **Mandatory Symptom Submission**: All users must submit symptoms before menu access
- **Business Hours Enforcement**: Appointments constrained to 9 AM - 5 PM
- **Weekend Skipping**: Next business day scheduling avoids weekends
- **Duplicate Email Prevention**: No two patients can register with same email
- **Encapsulation**: All fields private with public getters/setters
- **Generic Repository**: Type-safe data persistence with file I/O
- **Input Validation**: Comprehensive validation for emails, phones, names, dates

## Compilation

### Prerequisites
- Java 11 or higher
- No external dependencies required

### Compile from src folder

```bash
# Navigate to project root
cd HS

# Compile all classes to output directory
javac -d out src/main/java/com/healthcare/*/*.java src/main/java/com/healthcare/*/**.java

# Or compile using individual paths
javac -d out \
  src/main/java/com/healthcare/models/*.java \
  src/main/java/com/healthcare/repository/*.java \
  src/main/java/com/healthcare/exceptions/*.java \
  src/main/java/com/healthcare/utils/*.java \
  src/main/java/com/healthcare/ui/*.java
```

## Running the Application

```bash
# Navigate to output directory
cd out

# Run the main application
java com.healthcare.ui.Main
```

## Data Persistence

The application saves data to three text files:
- `patients.txt` - Registered and guest patients
- `symptoms.txt` - Symptom forms submitted by patients
- `appointments.txt` - Appointment records with status

Data is automatically saved when exiting the application.

## Design Patterns Used

1. **State Machine Pattern**: Appointment status transitions (PENDING → TIME_PROPOSED → CONFIRMED/REJECTED)
2. **Repository Pattern**: Generic DataRepository<T> for abstracted data access
3. **Validation Pattern**: Centralized ValidationUtils for input validation
4. **DTO Pattern**: GuestUser as Data Transfer Object for guest registration
5. **Encapsulation**: All fields private with public accessors

## Input Validation

The system validates:
- **Email**: Single @, text before/after, dot after @, case-insensitive normalization
- **Phone**: Exactly 10 digits, numeric only
- **Names**: Letters only, auto-capitalized
- **Dates**: ISO format (YYYY-MM-DD), cannot be past dates
- **Severity**: Low/Medium/High (case-insensitive)
- **Menu Options**: Bounded integer input with range checking
- **Non-Empty Strings**: Passwords, ID numbers, symptoms, descriptions

## User Workflows

### Guest Flow
1. Login as guest with name and ID
2. Submit symptoms (mandatory before menu)
3. View auto-scheduled appointment (today at 10:00 AM)
4. Cannot choose date or modify symptoms
5. Option to promote to patient after appointment confirmation

### Registered Patient Flow
1. Register with email, name, phone, password, ID
2. Submit symptoms (mandatory before menu)
3. Full menu access: update symptoms, choose date, view appointment
4. Appointment scheduling with business hours and weekend skipping
5. Accept/Reject appointments with auto-rescheduling

