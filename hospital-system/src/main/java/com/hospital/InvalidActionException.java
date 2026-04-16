// Custom Exception improves system robustness
public class InvalidActionException extends Exception {
    public InvalidActionException(String msg) {
        super(msg);
    }
}