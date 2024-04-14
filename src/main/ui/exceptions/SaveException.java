package ui.exceptions;

// After saving the session, the application ends
public class SaveException extends Exception {
    public SaveException(String message) {
        super(message);
    }
}
