package dasturlashuz.giybat.exceptions;

public class EmailOrPhoneAlreadyExistsException extends RuntimeException{
    public EmailOrPhoneAlreadyExistsException(String message) {
        super(message);
    }
}
