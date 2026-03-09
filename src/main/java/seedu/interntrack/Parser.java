package seedu.interntrack;
import java.time.LocalDate;

/**
 * Parses user input strings into Application objects.
 */
public class Parser {

    public static final String REGEX = "(?=c/|r/|ct/|d/)";
    private static final String DATE_FORMAT_ERROR = "Date must be in YYYY-MM-DD format.";

    /**
     * Creates an Application from a raw user input string.
     *
     * @param input The full command string entered by the user.
     * @return The constructed Application object.
     * @throws InternTrackException If required fields are missing or the date format is invalid.
     */
    public static Application createApplication(String input) throws InternTrackException {
        String[] parts = input.split(REGEX);
        String company = null;
        String role = null;
        String contact = null;
        LocalDate deadline = null;
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.startsWith("c/")) {
                company = trimmed.substring(2).trim();
            } else if (trimmed.startsWith("r/")) {
                role = trimmed.substring(2).trim();
            } else if (trimmed.startsWith("ct/")) {
                contact = trimmed.substring(3).trim();
            } else if (trimmed.startsWith("d/")) {
                try {
                    deadline = LocalDate.parse(trimmed.substring(2).trim());
                } catch (Exception e) {
                    throw new InternTrackException(DATE_FORMAT_ERROR);
                }
            }
        }
        if (company == null || role == null) {
            throw new InternTrackException("Both company (c/) and role (r/) are required!");
        }
        return new Application(company, role, deadline, contact);
    }
}
