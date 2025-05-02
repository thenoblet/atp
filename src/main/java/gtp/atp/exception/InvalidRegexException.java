package gtp.atp.exception;

/**
 * Custom exception thrown when an invalid regular expression pattern is encountered.
 * Provides multiple constructors for different error reporting scenarios.
 */
public class InvalidRegexException extends Exception {

    /**
     * Constructs a new exception with null as its detail message.
     */
    public InvalidRegexException() {
        super();
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause the underlying cause of the exception (may be null)
     */
    public InvalidRegexException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified invalid regex pattern.
     *
     * @param regex the invalid regular expression pattern that caused the exception
     */
    public InvalidRegexException(String regex) {
        super(String.format("Invalid regex: %s", regex));
    }

    /**
     * Constructs a new exception with the specified invalid regex pattern and cause.
     *
     * @param regex the invalid regular expression pattern that caused the exception
     * @param cause the underlying cause of the exception
     */
    public InvalidRegexException(String regex, Throwable cause) {
        super(String.format("Invalid regex: %s.", regex), cause);
    }

    /**
     * Constructs a new exception with details about where the validation failed.
     *
     * @param regex the invalid regular expression pattern
     * @param validationContext the context in which validation failed (e.g., "email validation")
     */
    public InvalidRegexException(String regex, String validationContext) {
        super(String.format("Invalid regex '%s' in %s", regex, validationContext));
    }

    /**
     * Constructs a new exception with details about where the validation failed and the cause.
     *
     * @param regex the invalid regular expression pattern
     * @param validationContext the context in which validation failed (e.g., "email validation")
     * @param cause the underlying cause of the exception
     */
    public InvalidRegexException(String regex, String validationContext, Throwable cause) {
        super(String.format("Invalid regex '%s' in %s.", regex, validationContext), cause);
    }
}