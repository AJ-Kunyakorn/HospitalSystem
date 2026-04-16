import java.util.Random;

// Guest mode: generate queue number instead of full registration
public class GuestUser {

    public String generatePK() {
        Random r = new Random();
        return "G" + (1000 + r.nextInt(9000));
    }
}