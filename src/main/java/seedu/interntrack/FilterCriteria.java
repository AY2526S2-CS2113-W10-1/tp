package seedu.interntrack;

import java.time.LocalDate;

/**
 * Represents a single filter criterion for an application list.
 */
public class FilterCriteria {
    /**
     * Supported application fields that can be filtered.
     */
    public enum Field {
        COMPANY,
        ROLE,
        DEADLINE,
        CONTACT,
        STATUS
    }

    private final Field field;
    private final String textValue;
    private final LocalDate deadlineValue;

    private FilterCriteria(Field field, String textValue, LocalDate deadlineValue) {
        this.field = field;
        this.textValue = textValue;
        this.deadlineValue = deadlineValue;
    }

    /**
     * Creates a text-based filter criterion.
     *
     * @param field     The application field being filtered.
     * @param textValue The user-provided search text.
     * @return The created filter criterion.
     */
    public static FilterCriteria forText(Field field, String textValue) {
        assert field != Field.DEADLINE : "Deadline filters must use a date value";
        return new FilterCriteria(field, textValue, null);
    }

    /**
     * Creates a deadline filter criterion.
     *
     * @param deadlineValue The deadline cutoff date.
     * @return The created filter criterion.
     */
    public static FilterCriteria forDeadline(LocalDate deadlineValue) {
        return new FilterCriteria(Field.DEADLINE, null, deadlineValue);
    }

    public Field getField() {
        return field;
    }

    public String getTextValue() {
        return textValue;
    }

    public LocalDate getDeadlineValue() {
        return deadlineValue;
    }

    /**
     * Returns a human-readable description of the current filter criterion.
     *
     * @return A short summary for use in UI messages.
     */
    public String getSummary() {
        if (field == Field.DEADLINE) {
            return "deadline on or before " + deadlineValue;
        }

        return getFieldName() + " " + textValue;
    }

    private String getFieldName() {
        return switch (field) {
        case COMPANY -> "company";
        case ROLE -> "role";
        case CONTACT -> "contact";
        case STATUS -> "status";
        default -> "deadline";
        };
    }
}
