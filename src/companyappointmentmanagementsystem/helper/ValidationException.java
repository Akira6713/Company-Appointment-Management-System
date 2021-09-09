package companyappointmentmanagementsystem.helper;

/**
 * An {@link RuntimeException} sub class which is thrown when
 * a validation rule violates
 *
 * @see Helper#nonEmptyString(String, String)
 * @see Helper#nonNull(Object, String)
 * @see Helper#isTrue(boolean, String)
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructor method to create new instance with error message
     *
     * @param message the error message
     */
    public ValidationException(String message) {
        super(message);
    }
}
