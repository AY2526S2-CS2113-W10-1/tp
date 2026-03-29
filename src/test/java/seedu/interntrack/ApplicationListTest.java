package seedu.interntrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;


public class ApplicationListTest {
    @Test
    public void addApplication_validApplication_sizeIncreases() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Google r/Intern";
        ApplicationList.addApplications(testList, testLine);
        assertEquals(1, testList.size());
    }

    @Test
    public void addApplication_emptyCompany_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/ r/Backend Intern d/2023-30-30";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplications(testList, testLine)
        );
        assertEquals("Company name cannot be empty.", exception.getMessage());
    }

    @Test
    public void addApplication_emptyRole_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/ d/2023-30-30";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplications(testList, testLine)
        );
        assertEquals("Role name cannot be empty.", exception.getMessage());
    }

    @Test
    public void addApplication_invalidDate_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Shopee r/Backend Intern d/2023-30-30";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplications(testList, testLine)
        );
        assertEquals("Date must be in YYYY-MM-DD format.", exception.getMessage());
    }

    @Test
    public void addApplication_emptyDate_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Shopee r/Backend Intern d/";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplications(testList, testLine)
        );
        assertEquals("Deadline date cannot be empty.", exception.getMessage());
    }

    @Test
    public void addApplication_emptyContact_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Shopee r/Backend Intern ct/";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplications(testList, testLine)
        );
        assertEquals("Contact name cannot be empty.", exception.getMessage());
    }

    @Test
    public void getApplication_validIndex_returnsCorrectApplication() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Shopee r/Backend Intern d/2023-11-30 ct/Johns ";
        ApplicationList.addApplications(testList, testLine);
        assertEquals("Shopee", testList.get(0).getCompany());
        assertEquals("Backend Intern", testList.get(0).getRole());
        assertEquals("Pending", testList.get(0).getStatus());
        assertEquals(LocalDate.parse("2023-11-30"), testList.get(0).getDeadline());
        assertEquals("Johns", testList.get(0).getContact());
    }


    @Test
    public void editApplicationStatus_validIndex_updatesStatus() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Google r/Intern";

        ApplicationList.addApplications(testList, testLine);

        Application updatedApplication =
                ApplicationList.editApplicationStatus(testList, 1, "Applied");

        assertEquals("Applied", updatedApplication.getStatus());
        assertEquals("Applied", testList.get(0).getStatus());
    }

    @Test
    public void editApplicationStatus_invalidIndex_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Google r/Intern";

        ApplicationList.addApplications(testList, testLine);

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.editApplicationStatus(testList, 2, "Applied")
        );

        assertEquals("Application index is out of range.", exception.getMessage());
    }

    @Test
    public void filterApplicationsByStatus_matchingStatus_returnsFilteredList() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplications(testList, "c/Google r/Intern");
        ApplicationList.addApplications(testList, "c/NUS r/TA");
        ApplicationList.editApplicationStatus(testList, 2, "Applied");

        ArrayList<Application> filteredApplications =
                ApplicationList.filterApplicationsByStatus(testList, "applied");

        assertEquals(1, filteredApplications.size());
        assertEquals("Applied", filteredApplications.get(0).getStatus());
    }

    @Test
    public void sortApplication_invalidCriteria_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String[] testCriteria = {"WEIRDCRITERIA", "DESC", "NONNULL"};
        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.sortApplicationsByCriteria(testList, testCriteria)
        );
        assertEquals("Wrong sorting criteria, please try again", exception.getMessage());
    }

    @Test
    public void sortApplication_deadlineCriteria_correctSize() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/TA ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplications(testList, testLine);
        String[] testCriteria = {"DEADLINE", "DESC", "NONNULL"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals(5, sortedList.size());
    }

    @Test
    public void sortApplication_contactCriteria_correctSize() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/TA ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplications(testList, testLine);
        String[] testCriteria = {"CONTACT", "DESC", "NONNULL"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals(5, sortedList.size());
    }

    @Test
    public void sortApplication_contactCriteria_correctFirst() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/TA ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplications(testList, testLine);
        String[] testCriteria = {"CONTACT", "DESC", "NONNULL"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals("Shopee", sortedList.get(0).getCompany());
    }
    @Test
    public void sortApplication_roleCriteria_correctFirst() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/TA ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplications(testList, testLine);
        String[] testCriteria = {"ROLE", "DESC"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals("TA", sortedList.get(0).getRole());
    }

    @Test
    public void sortApplication_companyCriteria_correctSecond() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/TA ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplications(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplications(testList, testLine);
        String[] testCriteria = {"COMPANY", "DESC"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals("ShopBack", sortedList.get(1).getCompany());
    }
}
