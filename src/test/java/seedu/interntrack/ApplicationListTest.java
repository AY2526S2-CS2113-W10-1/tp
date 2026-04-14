package seedu.interntrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ApplicationListTest {
    @Test
    public void addApplication_validApplication_sizeIncreases() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Google r/Intern";
        ApplicationList.addApplication(testList, testLine);
        assertEquals(1, testList.size());
    }

    @Test
    public void addApplication_emptyCompany_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/ r/Backend Intern d/2023-30-30";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplication(testList, testLine)
        );
        assertEquals("Company name cannot be empty.", exception.getMessage());
    }

    @Test
    public void addApplication_emptyRole_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/ d/2023-30-30";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplication(testList, testLine)
        );
        assertEquals("Role name cannot be empty.", exception.getMessage());
    }

    @Test
    public void addApplication_invalidDate_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Shopee r/Backend Intern d/2023-30-30";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplication(testList, testLine)
        );
        String expectedMessage = "Date must be in YYYY-MM-DD format."
                + "Invalid month: 30. Month must be between 1 and 12.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void addApplication_emptyDate_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Shopee r/Backend Intern d/";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplication(testList, testLine)
        );
        assertEquals("Deadline date cannot be empty.", exception.getMessage());
    }

    @Test
    public void addApplication_emptyContact_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Shopee r/Backend Intern ct/";

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.addApplication(testList, testLine)
        );
        assertEquals("Contact name cannot be empty.", exception.getMessage());
    }

    @Test
    public void getApplication_validIndex_returnsCorrectApplication() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/Shopee r/Backend Intern d/2023-11-30 ct/Johns ";
        ApplicationList.addApplication(testList, testLine);
        assertEquals("Shopee", testList.get(0).getCompany());
        assertEquals("Backend Intern", testList.get(0).getRole());
        assertEquals("Pending", testList.get(0).getStatus());
        assertEquals(LocalDate.parse("2023-11-30"), testList.get(0).getDeadline());
        assertEquals("Johns", testList.get(0).getContact());
    }

    @Test
    public void editApplication_validFields_updatesSelectedFields() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern d/2026-05-01 ct/Alice");
        LocalDate updatedDeadline = LocalDate.parse("2026-06-15");
        EditDetails editDetails = new EditDetails(
                "Meta",
                "Backend Engineer",
                updatedDeadline,
                "Bob",
                "Applied");

        Application updatedApplication = ApplicationList.editApplication(testList, 1, editDetails);

        assertEquals("Meta", updatedApplication.getCompany());
        assertEquals("Backend Engineer", updatedApplication.getRole());
        assertEquals(updatedDeadline, updatedApplication.getDeadline());
        assertEquals("Bob", updatedApplication.getContact());
        assertEquals("Applied", updatedApplication.getStatus());
    }

    @Test
    public void editApplicationStatus_validIndex_updatesStatus() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");

        Application updatedApplication = ApplicationList.editApplicationStatus(testList, 1, "Applied");

        assertEquals("Applied", updatedApplication.getStatus());
        assertEquals("Applied", testList.get(0).getStatus());
    }

    @Test
    public void editApplication_invalidIndex_throwsException() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");

        InternTrackException exception = assertThrows(
                InternTrackException.class,
                () -> ApplicationList.editApplication(testList, 2,
                        new EditDetails("Meta", null, null, null, null)));

        assertEquals("Active application index is out of range. Use list to view the available "
                + "active application indices.", exception.getMessage());
    }

    @Test
    public void filterApplications_companyCriterion_returnsFilteredList() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/NUS r/TA");

        ArrayList<Application> filteredApplications = ApplicationList.filterApplications(
                testList,
                FilterCriteria.forText(FilterCriteria.Field.COMPANY, "google"));

        assertEquals(1, filteredApplications.size());
        assertEquals("Google", filteredApplications.get(0).getCompany());
    }

    @Test
    public void filterApplications_companySubstringCriterion_returnsFilteredList() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Meta Platforms r/SWE");
        ApplicationList.addApplication(testList, "c/Google r/SWE");

        ArrayList<Application> filteredApplications = ApplicationList.filterApplications(
                testList,
                FilterCriteria.forText(FilterCriteria.Field.COMPANY, "meta"));

        assertEquals(1, filteredApplications.size());
        assertEquals("Meta Platforms", filteredApplications.get(0).getCompany());
    }

    @Test
    public void filterApplications_roleSubstringCriterion_isCaseInsensitive() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Meta r/Software Engineer Intern");
        ApplicationList.addApplication(testList, "c/Google r/Product Manager");

        ArrayList<Application> filteredApplications = ApplicationList.filterApplications(
                testList,
                FilterCriteria.forText(FilterCriteria.Field.ROLE, "engineer"));

        assertEquals(1, filteredApplications.size());
        assertEquals("Software Engineer Intern", filteredApplications.get(0).getRole());
    }

    @Test
    public void filterApplications_statusCriterion_returnsFilteredList() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/NUS r/TA");
        ApplicationList.editApplicationStatus(testList, 2, "Applied");

        ArrayList<Application> filteredApplications = ApplicationList.filterApplications(
                testList,
                FilterCriteria.forText(FilterCriteria.Field.STATUS, "applied"));

        assertEquals(1, filteredApplications.size());
        assertEquals("Applied", filteredApplications.get(0).getStatus());
    }

    @Test
    public void filterApplications_textCriterion_archivedApplicationExcluded() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Meta Platforms r/SWE");
        ApplicationList.addApplication(testList, "c/Meta AI r/Research Engineer");
        ApplicationList.archiveApplication(testList, 1);

        ArrayList<Application> filteredApplications = ApplicationList.filterApplications(
                testList,
                FilterCriteria.forText(FilterCriteria.Field.COMPANY, "meta"));

        assertEquals(1, filteredApplications.size());
        assertEquals("Meta AI", filteredApplications.get(0).getCompany());
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
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplication(testList, testLine);
        String[] testCriteria = {"DEADLINE", "DESC", "NONNULL"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals(5, sortedList.size());
    }

    @Test
    public void sortApplication_contactCriteria_correctSize() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/TA ct/Prof X";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplication(testList, testLine);
        String[] testCriteria = {"CONTACT", "DESC", "NONNULL"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals(5, sortedList.size());
    }

    @Test
    public void sortApplication_contactCriteria_correctFirst() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/TA ct/Prof X";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplication(testList, testLine);
        String[] testCriteria = {"CONTACT", "DESC", "NONNULL"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals("Shopee", sortedList.get(0).getCompany());
    }
    @Test
    public void sortApplication_roleCriteria_correctFirst() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/TA ct/Prof X";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplication(testList, testLine);
        String[] testCriteria = {"ROLE", "DESC"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals("TA", sortedList.get(0).getRole());
    }

    @Test
    public void sortApplication_companyCriteria_correctSecond() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        String testLine = "c/NUS r/TA ct/Prof X";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Embedded d/2027-01-01";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/ShopBack r/SE d/2025-11-11 ct/Harim";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/NVIDIA r/HE d/2026-03-27 ct/Prof X";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Shopee r/Sales d/2030-10-23 ct/Prof Y";
        ApplicationList.addApplication(testList, testLine);
        testLine = "c/Micron r/Marketing d/2026-03-27 ct/Limux";
        ApplicationList.addApplication(testList, testLine);
        String[] testCriteria = {"COMPANY", "DESC"};
        ArrayList<Application> sortedList = ApplicationList.sortApplicationsByCriteria(testList, testCriteria);
        assertEquals("ShopBack", sortedList.get(1).getCompany());
    }

    @Test
    public void filterApplications_deadlineCriterion_returnsFilteredList() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        ApplicationList.addApplication(testList, "c/Google r/Intern d/" + today);
        ApplicationList.addApplication(testList, "c/Meta r/Intern d/" + today.plusDays(5));
        ApplicationList.addApplication(testList, "c/Amazon r/Intern d/" + today.minusDays(5));

        ArrayList<Application> filteredApplications = ApplicationList.filterApplications(
                testList,
                FilterCriteria.forDeadline(today.plusDays(3)));

        assertEquals(2, filteredApplications.size());
    }

    @Test
    public void filterApplicationsOnOrBefore_emptyList_returnsEmpty() {
        ArrayList<Application> testList = new ArrayList<>();
        ArrayList<Application> filteredApplications = ApplicationList.filterApplicationsOnOrBefore(
                testList,
                LocalDate.now().plusDays(7));

        assertEquals(0, filteredApplications.size());
    }

    @Test
    public void filterApplicationsOnOrBefore_nullDeadlines_excluded() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        ApplicationList.addApplication(testList, "c/Google r/Intern d/" + today.plusDays(2));
        ApplicationList.addApplication(testList, "c/Meta r/Intern");

        ArrayList<Application> filteredApplications = ApplicationList.filterApplicationsOnOrBefore(
                testList,
                today.plusDays(7));

        assertEquals(1, filteredApplications.size());
    }

    @Test
    public void filterApplicationsOnOrBefore_exactToday_included() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        ApplicationList.addApplication(testList, "c/Google r/Intern d/" + today);

        ArrayList<Application> filtered = ApplicationList.filterApplicationsOnOrBefore(testList, today);

        assertEquals(1, filtered.size());
        assertEquals("Google", filtered.get(0).getCompany());
    }


    @Test
    public void filterApplicationsOnOrBefore_oneDayAfter_excluded() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.plusDays(3);
        ApplicationList.addApplication(testList, "c/Google r/Intern d/" + cutoff.plusDays(1));

        ArrayList<Application> filtered = ApplicationList.filterApplicationsOnOrBefore(testList, cutoff);

        assertEquals(0, filtered.size());
    }


    @Test
    public void filterApplicationsOnOrBefore_multipleOnSameCutoff_allIncluded() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.plusDays(5);
        for (int i = 0; i < 5; i++) {
            ApplicationList.addApplication(testList, "c/Company" + i + " r/Role d/" + cutoff);
        }

        ArrayList<Application> filtered = ApplicationList.filterApplicationsOnOrBefore(testList, cutoff);

        assertEquals(5, filtered.size());
        for (Application app : filtered) {
            assertEquals(cutoff, app.getDeadline());
        }
    }

    @Test
    public void filterApplicationsOnOrBefore_leapYearDate_handledCorrectly() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        // 2024 is a leap year, so Feb 29 exists
        ApplicationList.addApplication(testList, "c/Google r/Intern d/2024-02-29");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer d/2024-03-01");

        ArrayList<Application> filtered = ApplicationList.filterApplicationsOnOrBefore(
                testList,
                LocalDate.parse("2024-02-29"));

        assertEquals(1, filtered.size());
        assertEquals("Google", filtered.get(0).getCompany());
    }


    @Test
    public void filterApplicationsOnOrBefore_allPastDeadline_emptyResult() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate cutoff = LocalDate.now();
        ApplicationList.addApplication(testList, "c/Google r/Intern d/" + cutoff.plusDays(10));
        ApplicationList.addApplication(testList, "c/Meta r/Engineer d/" + cutoff.plusDays(20));

        ArrayList<Application> filtered = ApplicationList.filterApplicationsOnOrBefore(testList, cutoff);

        assertEquals(0, filtered.size());
    }


    @Test
    public void filterApplicationsByDaysAhead_oneDayBoundary_tomorrowIncluded() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate dayAfter = today.plusDays(2);

        ApplicationList.addApplication(testList, "c/Google r/Intern d/" + today);
        ApplicationList.addApplication(testList, "c/Meta r/Engineer d/" + tomorrow);
        ApplicationList.addApplication(testList, "c/Amazon r/PM d/" + dayAfter);

        ArrayList<Application> filtered = ApplicationList.filterApplicationsByDaysAhead(testList, 1);

        // Today and tomorrow should be included, but day after tomorrow excluded
        assertEquals(2, filtered.size());
        assertEquals(today, filtered.get(0).getDeadline());
        assertEquals(tomorrow, filtered.get(1).getDeadline());
    }



    @Test
    public void filterApplicationsByDaysAhead_largeNumberOfDays_yearAheadIncluded() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate dayInSixMonths = today.plusDays(180);
        LocalDate dayInOneYear = today.plusDays(365);
        LocalDate dayAfterYear = today.plusDays(366);

        ApplicationList.addApplication(testList, "c/Google r/Intern d/" + dayInSixMonths);
        ApplicationList.addApplication(testList, "c/Meta r/Engineer d/" + dayInOneYear);
        ApplicationList.addApplication(testList, "c/Amazon r/PM d/" + dayAfterYear);

        ArrayList<Application> filtered = ApplicationList.filterApplicationsByDaysAhead(testList, 365);

        // Applications within 365 days should be included (all except day 366+)
        assertEquals(2, filtered.size());
        assertEquals(dayInSixMonths, filtered.get(0).getDeadline());
        assertEquals(dayInOneYear, filtered.get(1).getDeadline());
    }

    
    @Test
    public void filterApplicationsByDaysAhead_multipleBoundaryApplications_allIncluded() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate cutoffDate = today.plusDays(7);

        // Add 5 applications all with the exact cutoff date
        for (int i = 0; i < 5; i++) {
            ApplicationList.addApplication(testList, "c/Company" + i + " r/Role d/" + cutoffDate);
        }

        // Add one application just beyond the cutoff
        ApplicationList.addApplication(testList, "c/Beyond r/Role d/" + cutoffDate.plusDays(1));

        ArrayList<Application> filtered = ApplicationList.filterApplicationsByDaysAhead(testList, 7);

        // All 5 applications on the boundary should be included, the one beyond excluded
        assertEquals(5, filtered.size());
        for (Application app : filtered) {
            assertEquals(cutoffDate, app.getDeadline());
        }
    }

    @Test
    public void filterApplicationsByDaysAhead_pastDueApplication_excluded() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate pastDueDate = LocalDate.parse("2024-01-01");
        LocalDate futureDate = today.plusDays(5);

        // Add past-due application (multiple years old)
        ApplicationList.addApplication(testList, "c/Google r/DevOps d/" + pastDueDate);
        // Add future application for reference
        ApplicationList.addApplication(testList, "c/Meta r/Backend d/" + futureDate);

        // Test with different remind periods - past-due should never appear
        ArrayList<Application> filteredRemind1 = ApplicationList.filterApplicationsByDaysAhead(testList, 1);
        ArrayList<Application> filteredRemind5 = ApplicationList.filterApplicationsByDaysAhead(testList, 5);
        ArrayList<Application> filteredRemind365 = ApplicationList.filterApplicationsByDaysAhead(testList, 365);
        
        assertEquals(0, filteredRemind1.size());

        assertEquals(1, filteredRemind5.size());
        assertEquals("Meta", filteredRemind5.get(0).getCompany());

        assertEquals(1, filteredRemind365.size());
        assertEquals("Meta", filteredRemind365.get(0).getCompany());
    }

    @Test
    public void filterApplicationsByDaysAhead_todayDeadline_included() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate yesterday = today.minusDays(1);

        // Add applications with deadlines at various boundaries
        ApplicationList.addApplication(testList, "c/Google r/Intern d/" + yesterday);
        ApplicationList.addApplication(testList, "c/Meta r/Engineer d/" + today);
        ApplicationList.addApplication(testList, "c/Amazon r/PM d/" + tomorrow);

        ArrayList<Application> filtered = ApplicationList.filterApplicationsByDaysAhead(testList, 0);

        // Only today's deadline should be included (yesterday excluded, tomorrow excluded)
        assertEquals(1, filtered.size());
        assertEquals(today, filtered.get(0).getDeadline());
        assertEquals("Meta", filtered.get(0).getCompany());
    }
}
