package seedu.interntrack;

import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Parses user input strings into Application objects.
 */
public class Parser {

    public static final String REGEX = "(?=c/|r/|ct/|d/)";
    private static final String DATE_FORMAT_ERROR = "Date must be in YYYY-MM-DD format.";
    private static final String STATUS_PREFIX = "s/";
    private static final String EDIT_FORMAT_ERROR = "Use format: edit INDEX s/NEW_STATUS";
    private static final String FILTER_FORMAT_ERROR = "Use format: filter s/STATUS";
    private static final Logger logger = Logger.getLogger("Parser");
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
                if (company.isEmpty()) {
                    logger.log(Level.WARNING, "Company field is empty in input");
                    throw new InternTrackException("Company name cannot be empty.");
                }
            } else if (trimmed.startsWith("r/")) {
                role = trimmed.substring(2).trim();
                if (role.isEmpty()) {
                    logger.log(Level.WARNING, "Role field is empty in input");
                    throw new InternTrackException("Role name cannot be empty.");
                }
            } else if (trimmed.startsWith("ct/")) {
                contact = trimmed.substring(3).trim();
                if (contact.isEmpty()) {
                    logger.log(Level.WARNING, "Contact field is empty in input");
                    throw new InternTrackException("Contact name cannot be empty.");
                }
            } else if (trimmed.startsWith("d/")) {
                String dateString = trimmed.substring(2).trim();
                if (dateString.isEmpty()) {
                    logger.log(Level.WARNING, "Deadline field is empty in input");
                    throw new InternTrackException("Deadline date cannot be empty.");
                }
                try {
                    deadline = LocalDate.parse(dateString.trim());
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Invalid date format in input: " + trimmed);
                    throw new InternTrackException(DATE_FORMAT_ERROR);
                }
            }
        }
        if (company == null || role == null) {
            logger.log(Level.WARNING, "Missing required fields in add command");
            throw new InternTrackException("Both company (c/) and role (r/) are required!");
        }
        // Assertions to verify internal invariants after parsing
        assert company != null && !company.isEmpty() : "Company name should have been captured";
        assert role != null && !role.isEmpty() : "Role title should have been captured";
        logger.log(Level.INFO, "Successfully parsed application: company=" + company + ", role=" + role);
        return new Application(company, role, deadline, contact);
    }

    /**
     * Parses the application index from an edit command.
     *
     * @param input The raw user input string.
     * @return The parsed 1-based index.
     * @throws InternTrackException If the index is missing or invalid.
     */
    public static int parseEditIndex(String input) throws InternTrackException {
        assert input != null : "Edit command input should not be null";

        String[] parts = input.trim().split("\\s+", 3);

        if (parts.length < 3) {
            logger.warning("Invalid edit command format: missing index or status");
            throw new InternTrackException(EDIT_FORMAT_ERROR);
        }

        try {
            int index = Integer.parseInt(parts[1]);
            assert index > 0 : "Application index should be positive";
            return index;
        } catch (NumberFormatException e) {
            logger.warning("Invalid application index in edit command: " + parts[1]);
            throw new InternTrackException("Application index must be a valid number.");
        }
    }

    /**
     * Parses the new status from an edit command.
     *
     * @param input The raw user input string.
     * @return The parsed status string.
     * @throws InternTrackException If the status is missing.
     */
    public static String parseEditStatus(String input) throws InternTrackException {
        assert input != null : "Edit command input should not be null";

        String[] parts = input.trim().split("\\s+", 3);

        if (parts.length < 3 || !parts[2].startsWith(STATUS_PREFIX)) {
            logger.warning("Invalid edit command format: missing status prefix");
            throw new InternTrackException(EDIT_FORMAT_ERROR);
        }

        String status = parts[2].substring(STATUS_PREFIX.length()).trim();

        if (status.isEmpty()) {
            logger.warning("Empty status provided in edit command");
            throw new InternTrackException("Status cannot be empty.");
        }

        assert !status.isBlank() : "Parsed status should not be blank";
        return status;
    }

    /**
     * Parses the status from a filter command.
     *
     * @param input The raw user input string.
     * @return The parsed status string.
     * @throws InternTrackException If the status is missing.
     */
    public static String parseFilterStatus(String input) throws InternTrackException {
        String[] parts = input.trim().split("\\s+", 2);

        if (parts.length < 2 || !parts[1].startsWith(STATUS_PREFIX)) {
            throw new InternTrackException(FILTER_FORMAT_ERROR);
        }

        String status = parts[1].substring(STATUS_PREFIX.length()).trim();

        if (status.isEmpty()) {
            throw new InternTrackException("Status cannot be empty.");
        }

        return status;
    }
    /**
     * Parses the application index from a delete command.
     *
     * @param input The raw user input string.
     * @return The parsed 0-based index.
     * @throws InternTrackException If the index is missing or invalid.
     */
    public static int parseDeleteIndex(String input) throws InternTrackException {
        String[] parts = input.trim().split("\\s+", 2);

        if (parts.length < 2) {
            throw new InternTrackException("Use format: delete INDEX");
        }

        try {
            int index = Integer.parseInt(parts[1]);
            if (index <= 0) {
                throw new InternTrackException("Application index must be greater than 0.");
            }
            return index - 1; // convert to 0-based index
        } catch (NumberFormatException e) {
            throw new InternTrackException("Application index must be a valid number.");
        }
    }
}


