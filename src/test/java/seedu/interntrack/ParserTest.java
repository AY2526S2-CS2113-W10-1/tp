package seedu.interntrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


public class ParserTest {
    @Test
    public void parse_validAddCommand_success() throws InternTrackException {
        String input = "add c/Google r/Software Engineer";
        Application result = Parser.createApplication(input);
        assertEquals("Google", result.getCompany());
        assertEquals("Software Engineer", result.getRole());
        assertEquals("Pending", result.getStatus());
        assertNull(result.getDeadline());
        assertNull(result.getContact());
    }

    @Test
    public void parse_addCommandWithExtraSpaces_success() throws InternTrackException {
        String input = "add c/ Google r/ Software Engineer";
        Application result = Parser.createApplication(input);
        assertEquals("Google", result.getCompany());
        assertEquals("Software Engineer", result.getRole());
        assertEquals("Pending", result.getStatus());
        assertNull(result.getDeadline());
        assertNull(result.getContact());
    }

    @Test
    public void parse_missingCompanyPrefix_throwsInternTrackException() {
        String input = "add r/Software Engineer";
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.createApplication(input);
        });
        assertEquals("Both company (c/) and role (r/) are required!", exception.getMessage());
    }

    @Test
    public void parse_invalidDateFormat_throwsException() {
        String input = "add c/Google r/Intern d/30-11-2023";
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.createApplication(input);
        });
        assertEquals("Date must be in YYYY-MM-DD format.", exception.getMessage());
    }

    @Test
    public void parse_filterCommand_success() throws InternTrackException {
        String input = "filter s/Applied";
        String status = Parser.parseFilterStatus(input);
        assertEquals("Applied", status);
    }

    @Test
    public void parse_filterCommandMissingPrefix_throwsException() {
        String input = "filter Applied";
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.parseFilterStatus(input);
        });
        assertEquals("Use format: filter s/STATUS", exception.getMessage());
    }

    @Test
    public void parse_filterCommandEmptyStatus_throwsException() {
        String input = "filter s/ ";
        InternTrackException exception = assertThrows(InternTrackException.class, () -> {
            Parser.parseFilterStatus(input);
        });
        assertEquals("Status cannot be empty.", exception.getMessage());
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
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseRemindDays(input)
        );
        assertEquals("Number of days must be greater than 0.", exception.getMessage());
    }

    @Test
    public void parseRemindDays_negativeNumber_throwsException() {
        String input = "remind -5";
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseRemindDays(input)
        );
        assertEquals("Number of days must be greater than 0.", exception.getMessage());
    }

    @Test
    public void parseRemindDays_nonIntegerInput_throwsException() {
        String input = "remind abc";
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseRemindDays(input)
        );
        assertEquals("Days must be a valid number. Use format: remind [DAYS]", exception.getMessage());
    }

    @Test
    public void parseRemindDays_decimalNumber_throwsException() {
        String input = "remind 3.5";
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseRemindDays(input)
        );
        assertEquals("Days must be a valid number. Use format: remind [DAYS]", exception.getMessage());
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
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseRemindDays(input)
        );
        assertEquals("Days must be a valid number. Use format: remind [DAYS]", exception.getMessage());
    }

}
