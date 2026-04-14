package seedu.interntrack;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class SummaryCommandTest {

    @Test
    public void execute_emptyList_printsNoApplicationsMessage() {
        ArrayList<Application> testList = new ArrayList<>();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            SummaryCommand.execute(testList);
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("You currently have no internship applications to summarize."));
    }

    @Test
    public void execute_mixedActiveAndArchived_printsSeparateCounts() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");
        ApplicationList.addApplication(testList, "c/Amazon r/PM");

        ApplicationList.archiveApplication(testList, 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            SummaryCommand.execute(testList);
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Total Applications Tracked: 3"));
        assertTrue(output.contains("Active Applications: 2"));
        assertTrue(output.contains("Archived Applications: 1"));
    }

    @Test
    public void execute_statusOverview_includesOnlyActiveApplications() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.addApplication(testList, "c/Meta r/Engineer");

        ApplicationList.editApplicationStatus(testList, 1, "Applied");
        ApplicationList.editApplicationStatus(testList, 2, "Rejected");
        ApplicationList.archiveApplication(testList, 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            SummaryCommand.execute(testList);
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Status Overview (Active Only):"));
        assertTrue(output.contains(" - Applied: 1"));
        assertTrue(!output.contains(" - Rejected: 1"));
    }

    @Test
    public void execute_upcomingDeadlines_includesOnlyActiveApplications() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        LocalDate soon = LocalDate.now().plusDays(3);

        ApplicationList.addApplication(testList, "c/Google r/Intern d/" + soon);
        ApplicationList.addApplication(testList, "c/Meta r/Engineer d/" + soon);

        ApplicationList.archiveApplication(testList, 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            SummaryCommand.execute(testList);
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Upcoming Deadlines (Next 7 days, Active Only):"));
        assertTrue(output.contains("Google (Intern)"));
        assertTrue(!output.contains("Meta (Engineer)"));
    }

    @Test
    public void execute_allArchived_printsNoActiveApplicationsMessage() throws InternTrackException {
        ArrayList<Application> testList = new ArrayList<>();
        ApplicationList.addApplication(testList, "c/Google r/Intern");
        ApplicationList.archiveApplication(testList, 1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            SummaryCommand.execute(testList);
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Active Applications: 0"));
        assertTrue(output.contains("Archived Applications: 1"));
        assertTrue(output.contains(" - No active applications to summarize."));
    }
}
