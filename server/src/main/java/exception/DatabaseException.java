package exception;

public class DatabaseException extends Exception {
    public DatabaseException(int errorCode, String message) {super(message);}
}
