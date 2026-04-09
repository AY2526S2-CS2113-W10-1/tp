package seedu.interntrack;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses user input strings into Application objects.
 */
public class Parser {
    private static final String COMPANY_PREFIX = "c/";
    private static final String ROLE_PREFIX = "r/";
    private static final String CONTACT_PREFIX = "ct/";
    private static final String DEADLINE_PREFIX = "d/";
    private static final String REGEX = "(?=c/|r/|ct/|d/)";
    private static final String DATE_FORMAT_ERROR = "Date must be in YYYY-MM-DD format.";
    private static final String STATUS_PREFIX = "s/";
    private static final String SORT_PREFIX = "by/";
    private static final String SORT_CRITERIA1 = "ROLE";
    private static final String SORT_CRITERIA2 = "COMPANY";
    private static final String SORT_CRITERIA3 = "DEADLINE";
    private static final String SORT_CRITERIA4 = "CONTACT";
    private static final String SORT_CRITERIA5 = "STATUS";
    private static final String SORT_FLAG1 = "DESC";
    private static final String SORT_FLAG2 = "NONNULL";
    private static final String EDIT_FORMAT_ERROR = "Use format: edit INDEX [c/COMPANY] [r/ROLE]"
            + " [d/DEADLINE] [ct/CONTACT] [s/STATUS]";
    private static final String FILTER_FORMAT_ERROR = "Use format: filter c/COMPANY, r/ROLE, d/DEADLINE,"
            + " ct/CONTACT, or s/STATUS";
    private static final String DUPLICATE_FIELD_ERROR = "Each field can only be specified once.";
    private static final String FILTER_SINGLE_FIELD_ERROR = "Filter command accepts exactly one field.";
    private static final Pattern PREFIX_PATTERN = Pattern.compile("ct/|c/|r/|d/|s/");
    private static final Logger logger = Logger.getLogger("Parser");
    private static final int DEFAULT_REMIND_DAYS = 7;

    /**
     * Creates an Application from a raw user input string.
     *
     * @param input The full command string entered by the user.
     * @return The constructed Application object.
     * @throws InternTrackException If required fields are missing or the date
     *                              format is invalid.
     */
    public static Application createApplication(String input) throws InternTrackException {
        String[] parts = input.split(REGEX);
        String company = null;
        String role = null;
        String contact = null;
        LocalDate deadline = null;
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.startsWith(COMPANY_PREFIX)) {
                company = parseRequiredTextValue(trimmed.substring(COMPANY_PREFIX.length()),
                        "Company name cannot be empty.");
            } else if (trimmed.startsWith(ROLE_PREFIX)) {
                role = parseRequiredTextValue(trimmed.substring(ROLE_PREFIX.length()),
                        "Role name cannot be empty.");
            } else if (trimmed.startsWith(CONTACT_PREFIX)) {
                contact = parseRequiredTextValue(trimmed.substring(CONTACT_PREFIX.length()),
                        "Contact name cannot be empty.");
            } else if (trimmed.startsWith(DEADLINE_PREFIX)) {
                deadline = parseDateValue(trimmed.substring(DEADLINE_PREFIX.length()));
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
            if (index <= 0) {
                logger.warning("Edit failed: index must be greater than 0");
                throw new InternTrackException("Application index must be greater than 0.");
            }

            return index;
        } catch (NumberFormatException e) {
            logger.warning("Invalid application index in edit command: " + parts[1]);
            throw new InternTrackException("Application index must be a valid number.");
        }
    }

    /**
     * Parses the updated fields from an edit command.
     *
     * @param input The raw user input string.
     * @return The parsed edit payload.
     * @throws InternTrackException If the format or any supplied field is invalid.
     */
    public static EditDetails parseEditDetails(String input) throws InternTrackException {
        assert input != null : "Edit command input should not be null";

        String[] parts = input.trim().split("\\s+", 3);

        if (parts.length < 3) {
            logger.warning("Invalid edit command format: missing editable fields");
            throw new InternTrackException(EDIT_FORMAT_ERROR);
        }

        ArrayList<PrefixedValue> fields = parsePrefixedValues(parts[2], EDIT_FORMAT_ERROR);
        String company = null;
        String role = null;
        LocalDate deadline = null;
        String contact = null;
        String status = null;

        for (PrefixedValue field : fields) {
            switch (field.prefix) {
            case COMPANY_PREFIX:
                if (company != null) {
                    throw new InternTrackException(DUPLICATE_FIELD_ERROR);
                }
                company = parseRequiredTextValue(field.value, "Company name cannot be empty.");
                break;
            case ROLE_PREFIX:
                if (role != null) {
                    throw new InternTrackException(DUPLICATE_FIELD_ERROR);
                }
                role = parseRequiredTextValue(field.value, "Role name cannot be empty.");
                break;
            case DEADLINE_PREFIX:
                if (deadline != null) {
                    throw new InternTrackException(DUPLICATE_FIELD_ERROR);
                }
                deadline = parseDateValue(field.value);
                break;
            case CONTACT_PREFIX:
                if (contact != null) {
                    throw new InternTrackException(DUPLICATE_FIELD_ERROR);
                }
                contact = parseRequiredTextValue(field.value, "Contact name cannot be empty.");
                break;
            case STATUS_PREFIX:
                if (status != null) {
                    throw new InternTrackException(DUPLICATE_FIELD_ERROR);
                }
                status = parseRequiredTextValue(field.value, "Status cannot be empty.");
                break;
            default:
                throw new InternTrackException(EDIT_FORMAT_ERROR);
            }
        }

        return new EditDetails(company, role, deadline, contact, status);
    }

    /**
     * Parses the filter criterion from a filter command.
     *
     * @param input The raw user input string.
     * @return The parsed filter criterion.
     * @throws InternTrackException If the format or filter field is invalid.
     */
    public static FilterCriteria parseFilterCriteria(String input) throws InternTrackException {
        String[] parts = input.trim().split("\\s+", 2);

        if (parts.length < 2) {
            throw new InternTrackException(FILTER_FORMAT_ERROR);
        }

        ArrayList<PrefixedValue> fields = parsePrefixedValues(parts[1], FILTER_FORMAT_ERROR);
        if (fields.size() != 1) {
            throw new InternTrackException(FILTER_SINGLE_FIELD_ERROR);
        }

        PrefixedValue field = fields.get(0);
        return switch (field.prefix) {
        case COMPANY_PREFIX -> FilterCriteria.forText(FilterCriteria.Field.COMPANY,
                    parseRequiredTextValue(field.value, "Company name cannot be empty."));
        case ROLE_PREFIX -> FilterCriteria.forText(FilterCriteria.Field.ROLE,
                    parseRequiredTextValue(field.value, "Role name cannot be empty."));
        case DEADLINE_PREFIX -> FilterCriteria.forDeadline(parseDateValue(field.value));
        case CONTACT_PREFIX -> FilterCriteria.forText(FilterCriteria.Field.CONTACT,
                    parseRequiredTextValue(field.value, "Contact name cannot be empty."));
        case STATUS_PREFIX -> FilterCriteria.forText(FilterCriteria.Field.STATUS,
                    parseRequiredTextValue(field.value, "Status cannot be empty."));
        default -> throw new InternTrackException(FILTER_FORMAT_ERROR);
        };
    }

    /**
     * Parses the status from a filter command.
     *
     * @param input The raw user input string.
     * @return The parsed status string.
     * @throws InternTrackException If the status filter is missing.
     */
    public static String parseFilterStatus(String input) throws InternTrackException {
        FilterCriteria criteria = parseFilterCriteria(input);
        if (criteria.getField() != FilterCriteria.Field.STATUS) {
            throw new InternTrackException(FILTER_FORMAT_ERROR);
        }

        return criteria.getTextValue();
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

    /**
     * Parses sorting criteria and optional flags from a sort command.
     *
     * <p>The command format is: sort by/CRITERIA [DESC] [NONNULL]</p>
     *
     * @param input The raw user input string.
     * @return A String array where the first element is the sorting field,
     *     followed by optional flags such as DESC or NONNULL.
     * @throws InternTrackException If the format, criteria, or flags are invalid.
     */
    public static String[] parseSortCriteria(String input) throws InternTrackException {
        String[] parts = input.trim().split("\\s+", 2);
        if (parts.length < 2) {
            throw new InternTrackException("Wrong format for sort command: sort by/CRITERIA [DIRECTION]");
        }
        if (!parts[1].startsWith(SORT_PREFIX)) {
            throw new InternTrackException("Wrong format for sort command: sort by/CRITERIA [DIRECTION]");
        }
        String[] criterias = parts[1].substring(SORT_PREFIX.length()).trim().split("\\s+", 2);
        if (!criterias[0].equals(SORT_CRITERIA1) && !criterias[0].equals(SORT_CRITERIA2)
                && !criterias[0].equals(SORT_CRITERIA3) && !criterias[0].equals(SORT_CRITERIA4)
                && !criterias[0].equals(SORT_CRITERIA5)) {
            throw new InternTrackException("Wrong sort criteria, use either these: ROLE, COMPANY, DEADLINE, CONTACT");
        }
        if (criterias.length == 1) { //No other flags
            return criterias;
        }
        //Extra flag
        boolean isDesc = false;
        boolean isNonnull = false;
        for (String criteria : criterias[1].split("\\s+")) {
            if (!criteria.equals(SORT_FLAG1) && !criteria.equals(SORT_FLAG2)) {
                throw new InternTrackException("Wrong flag, use either these: DESC, NONNULL");
            }
            if (criteria.equals(SORT_FLAG1)) {
                isDesc = true;
            }
            if (criteria.equals(SORT_FLAG2)) {
                isNonnull = true;
            }
        }
        ArrayList<String> newCriteriaList = new ArrayList<>();
        newCriteriaList.add(criterias[0]);
        if (isDesc) {
            newCriteriaList.add(SORT_FLAG1);
        }
        if (isNonnull) {
            newCriteriaList.add(SORT_FLAG2);
        }

        return newCriteriaList.toArray(new String[0]);
    }

    /**
     * Parses the number of days from a remind command.
     * If no number is provided, defaults to 7 days.
     *
     * @param input The raw user input string.
     * @return The parsed number of days.
     * @throws InternTrackException If the input is not a valid positive integer.
     */
    public static int parseRemindDays(String input) throws InternTrackException {
        assert input != null : "Remind command input should not be null";

        String[] parts = input.trim().split("\\s+", 2);

        // If no days specified, default to DEFAULT_REMIND_DAYS
        if (parts.length < 2) {
            logger.log(Level.INFO, "No days specified for remind command, defaulting to "
                    + DEFAULT_REMIND_DAYS + " days");
            return DEFAULT_REMIND_DAYS;
        }

        String daysString = parts[1].trim();

        try {
            int days = Integer.parseInt(daysString);

            if (days <= 0) {
                logger.warning("Invalid number of days in remind command: " + daysString);
                throw new InternTrackException("Number of days must be greater than 0.");
            }

            logger.log(Level.INFO, "Successfully parsed remind command with " + days + " days");
            return days;

        } catch (NumberFormatException e) {
            logger.warning("Invalid day format in remind command: " + daysString);
            throw new InternTrackException("Days must be a valid number. Use format: remind [DAYS]");
        }
    }

    /**
     * Parses a required text value by trimming whitespace and ensuring it is not empty.
     *
     * @param value        The raw text value to parse.
     * @param emptyMessage The error message to throw if the value is empty.
     * @return The trimmed text value.
     * @throws InternTrackException If the value is empty.
     */
    private static String parseRequiredTextValue(String value, String emptyMessage) throws InternTrackException {
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            logger.log(Level.WARNING, "Empty value provided for a prefixed field");
            throw new InternTrackException(emptyMessage);
        }

        return trimmedValue;
    }

    /**
     * Parses a LocalDate value from a string.
     *
     * @param value The raw date string.
     * @return The parsed LocalDate.
     * @throws InternTrackException If the date is empty or not in YYYY-MM-DD format.
     */
    private static LocalDate parseDateValue(String value) throws InternTrackException {
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            logger.log(Level.WARNING, "Deadline field is empty in input");
            throw new InternTrackException("Deadline date cannot be empty.");
        }

        try {
            return LocalDate.parse(trimmedValue);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Invalid date format in input: " + trimmedValue);
            throw new InternTrackException(DATE_FORMAT_ERROR);
        }
    }

    /**
     * Extracts prefixed values from an input string.
     *
     * <p>Each value must begin with a valid prefix such as c/, r/, d/, ct/, or s/.
     * This method separates the input into prefix-value pairs.</p>
     *
     * @param input       The input string containing prefixed values.
     * @param formatError The error message to throw if the format is invalid.
     * @return A list of PrefixedValue objects containing prefixes and their values.
     * @throws InternTrackException If the input format is invalid.
     */
    private static ArrayList<PrefixedValue> parsePrefixedValues(String input, String formatError)
            throws InternTrackException {
        String trimmedInput = input.trim();
        Matcher matcher = PREFIX_PATTERN.matcher(trimmedInput);
        ArrayList<PrefixedValue> prefixedValues = new ArrayList<>();
        ArrayList<Integer> prefixStarts = new ArrayList<>();
        ArrayList<String> prefixes = new ArrayList<>();

        while (matcher.find()) {
            prefixStarts.add(matcher.start());
            prefixes.add(matcher.group());
        }

        if (prefixStarts.isEmpty() || prefixStarts.get(0) != 0) {
            throw new InternTrackException(formatError);
        }

        for (int i = 0; i < prefixes.size(); i++) {
            int valueStart = prefixStarts.get(i) + prefixes.get(i).length();
            int valueEnd = (i == prefixes.size() - 1) ? trimmedInput.length() : prefixStarts.get(i + 1);
            String value = trimmedInput.substring(valueStart, valueEnd);
            prefixedValues.add(new PrefixedValue(prefixes.get(i), value));
        }

        return prefixedValues;
    }

    private static class PrefixedValue {
        private final String prefix;
        private final String value;

        private PrefixedValue(String prefix, String value) {
            this.prefix = prefix;
            this.value = value;
        }
    }
}
