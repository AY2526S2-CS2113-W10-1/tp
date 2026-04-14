package seedu.interntrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ApplicationListArchiveTest {

    @Test
    public void archiveApplication_validActiveIndex_archivesCorrectApplication() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        Application archivedApplication = ApplicationList.archiveApplication(testList, 1);

        assertEquals("Google", archivedApplication.getCompany());
        assertTrue(testList.get(0).isArchived());
        assertFalse(testList.get(1).isArchived());
    }

    @Test
    public void archiveApplication_afterArchivingFirstItem_secondVisibleItemCanBeArchivedWithIndexOne()
            throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        ApplicationList.archiveApplication(testList, 1);
        Application archivedApplication = ApplicationList.archiveApplication(testList, 1);

        assertEquals("Meta", archivedApplication.getCompany());
        assertTrue(testList.get(0).isArchived());
        assertTrue(testList.get(1).isArchived());
    }

    @Test
    public void unarchiveApplication_validArchivedIndex_unarchivesCorrectApplication() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        ApplicationList.archiveApplication(testList, 1);
        Application unarchivedApplication = ApplicationList.unarchiveApplication(testList, 1);

        assertEquals("Google", unarchivedApplication.getCompany());
        assertFalse(testList.get(0).isArchived());
    }

    @Test
    public void unarchiveApplication_afterUnarchivingFirstArchivedItem_nextArchivedItemBecomesIndexOne()
            throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        ApplicationList.archiveApplication(testList, 1);
        ApplicationList.archiveApplication(testList, 1);

        ApplicationList.unarchiveApplication(testList, 1);
        Application unarchivedApplication = ApplicationList.unarchiveApplication(testList, 1);

        assertEquals("Meta", unarchivedApplication.getCompany());
        assertFalse(testList.get(0).isArchived());
        assertFalse(testList.get(1).isArchived());
    }

    @Test
    public void deleteActiveApplication_validActiveIndex_deletesCorrectApplication() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        Application deletedApplication = ApplicationList.deleteActiveApplication(testList, 2);

        assertEquals("Meta", deletedApplication.getCompany());
        assertEquals(1, testList.size());
        assertEquals("Google", testList.get(0).getCompany());
    }

    @Test
    public void deleteActiveApplication_afterArchivingFirstItem_indexOneDeletesVisibleActiveItem()
            throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        ApplicationList.archiveApplication(testList, 1);
        Application deletedApplication = ApplicationList.deleteActiveApplication(testList, 1);

        assertEquals("Meta", deletedApplication.getCompany());
        assertEquals(1, testList.size());
        assertTrue(testList.get(0).isArchived());
        assertEquals("Google", testList.get(0).getCompany());
    }

    @Test
    public void getActiveApplications_archivedItemsExcluded() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        ApplicationList.archiveApplication(testList, 1);

        ArrayList<Application> activeApplications = ApplicationList.getActiveApplications(testList);

        assertEquals(1, activeApplications.size());
        assertEquals("Meta", activeApplications.get(0).getCompany());
    }

    @Test
    public void getArchivedApplications_onlyArchivedItemsReturned() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        ApplicationList.archiveApplication(testList, 1);

        ArrayList<Application> archivedApplications = ApplicationList.getArchivedApplications(testList);

        assertEquals(1, archivedApplications.size());
        assertEquals("Google", archivedApplications.get(0).getCompany());
    }

    @Test
    public void getApplicationCounts_mixedActiveAndArchived_correctCounts() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");
        ApplicationList.addApplication(testList, "c/Amazon r/PM");

        ApplicationList.archiveApplication(testList, 2);

        assertEquals(2, ApplicationList.getActiveApplicationCount(testList));
        assertEquals(1, ApplicationList.getArchivedApplicationCount(testList));
    }

    @Test
    public void hasArchivedApplications_noneArchived_returnsFalse() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");

        assertFalse(ApplicationList.hasArchivedApplications(testList));
    }

    @Test
    public void hasArchivedApplications_archivedExists_returnsTrue() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");

        ApplicationList.archiveApplication(testList, 1);

        assertTrue(ApplicationList.hasArchivedApplications(testList));
    }

    @Test
    public void archiveApplication_noActiveApplications_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.archiveApplication(testList, 1);

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.archiveApplication(testList, 1));

        assertEquals("There are no active applications to select. Use add to create an application, "
                + "or list to view active applications.", exception.getMessage());
    }

    @Test
    public void unarchiveApplication_noArchivedApplications_throwsException() {
        ArrayList<Application> testList = new ArrayList<>();

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.unarchiveApplication(testList, 1));

        assertEquals("There are no archived applications to select.", exception.getMessage());
    }

    @Test
    public void deleteActiveApplication_activeIndexOutOfRange_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.deleteActiveApplication(testList, 2));

        assertEquals("Active application index is out of range. Use list to view the available "
                + "active application indices.", exception.getMessage());
    }
}
