package seedu.interntrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ParserCommandFormatTest {

    @Test
    public void parseArchiveIndex_validInput_returnsCorrectIndex() throws InternTrackException {
        int index = Parser.parseArchiveIndex("archive 1");
        assertEquals(1, index);
    }

    @Test
    public void parseArchiveIndex_extraSpaces_valid() throws InternTrackException {
        int index = Parser.parseArchiveIndex("archive    2");
        assertEquals(2, index);
    }

    @Test
    public void parseArchiveIndex_withGarbage_throwsException() {
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseArchiveIndex("archive 1 extra"));

        assertEquals("Use format: archive INDEX", exception.getMessage());
    }

    @Test
    public void parseUnarchiveIndex_validInput_returnsCorrectIndex() throws InternTrackException {
        int index = Parser.parseUnarchiveIndex("unarchive 1");
        assertEquals(1, index);
    }

    @Test
    public void parseUnarchiveIndex_extraSpaces_valid() throws InternTrackException {
        int index = Parser.parseUnarchiveIndex("unarchive    2");
        assertEquals(2, index);
    }

    @Test
    public void parseUnarchiveIndex_withGarbage_throwsException() {
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseUnarchiveIndex("unarchive 1 abc"));

        assertEquals("Use format: unarchive INDEX", exception.getMessage());
    }

    @Test
    public void parseDeleteIndex_validInput_returnsCorrectIndex() throws InternTrackException {
        int index = Parser.parseDeleteIndex("delete 3");
        assertEquals(3, index);
    }

    @Test
    public void parseDeleteIndex_extraSpaces_valid() throws InternTrackException {
        int index = Parser.parseDeleteIndex("delete    2");
        assertEquals(2, index);
    }

    @Test
    public void parseDeleteIndex_withGarbage_throwsException() {
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseDeleteIndex("delete 1 extra"));

        assertEquals("Use format: delete INDEX", exception.getMessage());
    }

    @Test
    public void parseDeleteIndex_negativeIndex_throwsException() {
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseDeleteIndex("delete -1"));

        assertEquals("Application index must be greater than 0.", exception.getMessage());
    }

    @Test
    public void parseDeleteIndex_nonNumber_throwsException() {
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> Parser.parseDeleteIndex("delete abc"));

        assertEquals("Application index must be a valid number.", exception.getMessage());
    }
}
