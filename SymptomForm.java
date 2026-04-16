import java.io.*;
import java.util.*;

// File I/O: store symptom to file for persistence
public class SymptomForm {

    private String id;
    private String patientId;
    private List<String> symptoms = new ArrayList<>();

    public SymptomForm(String id, String patientId) {
        this.id = id;
        this.patientId = patientId;
    }

    public void addSymptom(String s) {
        symptoms.add(s);
    }

    public void submitForm() {
        try (FileWriter fw = new FileWriter("symptoms.txt", true)) {
            fw.write(patientId + " : " + symptoms + "\n");
            System.out.println("Symptom saved to file");
        } catch (IOException e) {
            System.out.println("Error saving file");
        }
    }

    public String getSymptoms() {
        return symptoms.toString();
    }
}