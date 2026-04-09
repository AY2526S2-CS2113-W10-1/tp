package seedu.interntrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class ParserTest {
    /**
     * Verifies all fields of a successfully parsed application.
     * Eliminates repeated assertion patterns in add feature success tests and
     * reduces code duplication.
     *
     * @param result The application object returned by the parser.
     * @param expectedCompany The expected company name string.
     * @param expectedRole The expected role title string.
     * @param expectedStatus The expected application status string.
     * @param expectedDeadline The expected deadline date.
     * @param expectedContact The expected contact information string.
     */
    private void assertApplicationFields(Application result, String expectedCompany,
                                         String expectedRole, String expectedStatus,
                                         LocalDate expectedDeadline, String expectedContact) {
        assertEquals(expectedCompany, result.getCompany());
        assertEquals(expectedRole, result.getRole());
        assertEquals(expectedStatus, result.getStatus());
        assertEquals(expectedDeadline, result.getDeadline());
        assertEquals(expectedContact, result.getContact());
    }

    /**
     * Verifies that the correct exception is thrown with the expected message.
     * Eliminates repeated exception handling patterns in add feature error tests.
     *
     * @param input The raw input string to be parsed.
     * @param expectedMessage The error message expected from the exception.
     */
    private void verifyParserExceptionThrown(String input, String expectedMessage) {
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.createApplication(input);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void parse_validAddCommand_success() throws InternTrackException {
        String input = "add c/Google r/Software Engineer";
        Application result = Parser.createApplication(input);
        assertApplicationFields(result, "Google", "Software Engineer", "Pending", null, null);
    }

    @Test
    public void parse_addCommandWithExtraSpaces_success() throws InternTrackException {
        String input = "add c/ Google r/ Software Engineer";
        Application result = Parser.createApplication(input);
        assertApplicationFields(result, "Google", "Software Engineer", "Pending", null, null);
    }

    @Test
    public void parse_missingCompanyPrefix_throwsInternTrackException() {
        String input = "add r/Software Engineer";
        verifyParserExceptionThrown(input, "Both company (c/) and role (r/) are required!");
    }

    @Test
    public void parse_invalidDateFormat_throwsException() {
        String input = "add c/Google r/Intern d/30-11-2023";
        verifyParserExceptionThrown(input, "Date must be in YYYY-MM-DD format.");
    }

    @Test
    public void parseEditDetails_multipleFields_success() throws InternTrackException {
        String input = "edit 2 c/Meta r/Backend Engineer d/2026-04-30 ct/Bob s/Applied";

        EditDetails editDetails = Parser.parseEditDetails(input);

        assertEquals("Meta", editDetails.getCompany());
        assertEquals("Backend Engineer", editDetails.getRole());
        assertEquals(LocalDate.parse("2026-04-30"), editDetails.getDeadline());
        assertEquals("Bob", editDetails.getContact());
        assertEquals("Applied", editDetails.getStatus());
    }

    @Test
    public void parseEditDetails_statusOnly_success() throws InternTrackException {
        String input = "edit 3 s/Accepted";

        EditDetails editDetails = Parser.parseEditDetails(input);

        assertEquals("Accepted", editDetails.getStatus());
        assertNull(editDetails.getCompany());
        assertNull(editDetails.getRole());
        assertNull(editDetails.getDeadline());
        assertNull(editDetails.getContact());
    }

    @Test
    public void parseEditDetails_noFields_throwsException() {
        String input = "edit 1";
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.parseEditDetails(input);
        });
        assertEquals("Use format: edit INDEX [c/COMPANY] [r/ROLE] [d/DEADLINE] [ct/CONTACT] [s/STATUS]",
                exception.getMessage());
    }

    @Test
    public void parseEditDetails_duplicateFields_throwsException() {
        String input = "edit 1 c/Google c/Meta";
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.parseEditDetails(input);
        });
        assertEquals("Each field can only be specified once.", exception.getMessage());
    }

    @Test
    public void parseFilterCriteria_company_success() throws InternTrackException {
        String input = "filter c/Google";

        FilterCriteria criteria = Parser.parseFilterCriteria(input);

        assertEquals(FilterCriteria.Field.COMPANY, criteria.getField());
        assertEquals("Google", criteria.getTextValue());
    }

    @Test
    public void parseFilterCriteria_deadline_success() throws InternTrackException {
        String input = "filter d/2026-04-30";

        FilterCriteria criteria = Parser.parseFilterCriteria(input);

        assertEquals(FilterCriteria.Field.DEADLINE, criteria.getField());
        assertEquals(LocalDate.parse("2026-04-30"), criteria.getDeadlineValue());
    }

    @Test
    public void parseFilterCriteria_multipleFields_throwsException() {
        String input = "filter c/Google r/Intern";
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.parseFilterCriteria(input);
        });
        assertEquals("Filter command accepts exactly one field.", exception.getMessage());
    }

    @Test
    public void parse_filterStatusCommand_success() throws InternTrackException {
        String input = "filter s/Applied";
        String status = Parser.parseFilterStatus(input);
        assertEquals("Applied", status);
    }

    @Test
    public void parse_filterCommandMissingPrefix_throwsException() {
        String input = "filter Applied";
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.parseFilterCriteria(input);
        });
        assertEquals("Use format: filter c/COMPANY, r/ROLE, d/DEADLINE, ct/CONTACT, or s/STATUS",
                exception.getMessage());
    }

    @Test
    public void parse_filterCommandEmptyStatus_throwsException() {
        String input = "filter s/ ";
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.parseFilterCriteria(input);
        });
        assertEquals("Status cannot be empty.", exception.getMessage());
    }

    /**
     * Verifies that the remind command throws an exception with the correct message.
     * Eliminates repeated exception handling pattern from various remind error tests.
     *
     * @param input The raw input string containing the remind command.
     * @param expectedMessage The error message expected from the exception.
     */
    private void verifyRemindExceptionThrown(String input, String expectedMessage) {
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.parseRemindDays(input);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void parseRemindDays_noArgument_returnsDefault() throws InternTrackException {
        String input = "remind";
        int days = Parser.parseRemindDays(input);
        assertEquals(7, days);
    }

    @Test
    public void parseRemindDays_singleDigit_success() throws InternTrackException {
        String input = "remind 3";
        int days = Parser.parseRemindDays(input);
        assertEquals(3, days);
    }

    @Test
    public void parseRemindDays_multipleDigits_success() throws InternTrackException {
        String input = "remind 14";
        int days = Parser.parseRemindDays(input);
        assertEquals(14, days);
    }

    @Test
    public void parseRemindDays_largeNumber_success() throws InternTrackException {
        String input = "remind 365";
        int days = Parser.parseRemindDays(input);
        assertEquals(365, days);
    }


    @Test
    public void parseRemindDays_zero_throwsException() {
        String input = "remind 0";
        verifyRemindExceptionThrown(input, "Number of days must be greater than 0.");
    }

    @Test
    public void parseRemindDays_negativeNumber_throwsException() {
        String input = "remind -5";
        verifyRemindExceptionThrown(input, "Number of days must be greater than 0.");
    }

    @Test
    public void parseRemindDays_nonIntegerInput_throwsException() {
        String input = "remind abc";
        verifyRemindExceptionThrown(input, "Days must be a valid number. Use format: remind [DAYS]");
    }

    @Test
    public void parseRemindDays_decimalNumber_throwsException() {
        String input = "remind 3.5";
        verifyRemindExceptionThrown(input, "Days must be a valid number. Use format: remind [DAYS]");
    }

    @Test
    public void parseRemindDays_extraSpaces_success() throws InternTrackException {
        String input = "remind    7";
        int days = Parser.parseRemindDays(input);
        assertEquals(7, days);
    }

    @Test
    public void parseRemindDays_specialCharacters_throwsException() {
        String input = "remind @5";
        verifyRemindExceptionThrown(input, "Days must be a valid number. Use format: remind [DAYS]");
    }
}
