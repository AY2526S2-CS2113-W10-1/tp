package seedu.interntrack;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class ParserTest {
    @Test
    public void parse_validAddCommand_success() throws InternTrackException{
        String input = "add c/Google r/Software Engineer";
        Application result = Parser.createApplication(input);
        assertEquals("Google", result.getCompany());
        assertEquals("Software Engineer", result.getRole());
        assertEquals("Pending", result.getStatus());
        assertNull(result.getDeadline());
        assertNull(result.getContact());
    }

    @Test
    public void parse_addCommandWithExtraSpaces_success () throws InternTrackException {
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
}
