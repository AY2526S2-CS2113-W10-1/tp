package seedu.interntrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ApplicationListEditIndexingTest {

    @Test
    public void editApplication_afterArchivingFirstItem_indexOneEditsVisibleActiveApplication()
            throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        ApplicationList.archiveApplication(testList, 1);

        EditDetails editDetails = new EditDetails("Meta Updated", null, null, null, "Applied");
        Application updatedApplication = ApplicationList.editApplication(testList, 1, editDetails);

        assertEquals("Meta Updated", updatedApplication.getCompany());
        assertEquals("Applied", updatedApplication.getStatus());

        assertEquals("Google", testList.get(0).getCompany());
        assertEquals("Meta Updated", testList.get(1).getCompany());
    }

    @Test
    public void editApplication_noFieldsProvided_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.editApplication(testList, 1,
                        new EditDetails(null, null, null, null, null)));

        assertEquals("Provide at least one field to edit.", exception.getMessage());
    }

    @Test
    public void editApplication_onlyDeadline_updatesDeadline() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");

        LocalDate newDeadline = LocalDate.parse("2026-12-31");
        EditDetails editDetails = new EditDetails(null, null, newDeadline, null, null);

        Application updatedApplication = ApplicationList.editApplication(testList, 1, editDetails);

        assertEquals(newDeadline, updatedApplication.getDeadline());
        assertEquals(newDeadline, testList.get(0).getDeadline());
    }

    @Test
    public void editApplication_activeIndexOutOfRangeAfterArchive_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.archiveApplication(testList, 1);

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.editApplication(testList, 1,
                        new EditDetails("Meta", null, null, null, null)));

        assertEquals("There are no active applications to select. Use add to create an application, "
                + "or list to view active applications.", exception.getMessage());
    }
}
