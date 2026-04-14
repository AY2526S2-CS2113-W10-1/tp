package seedu.interntrack;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the generation of a comprehensive summary report
 * for the user's internship applications.
 */
public class SummaryCommand {

    /**
     * Generates and displays a summary including total counts,
     * status breakdowns, and upcoming deadlines.
     *
     * @param userApplications The current list of applications.
     */
    public static void execute(ArrayList<Application> userApplications) {
        if (userApplications.isEmpty()) {
            System.out.println("You currently have no internship applications to summarize.");
            return;
        }

        ArrayList<Application> activeApplications = ApplicationList.getActiveApplications(userApplications);
        ArrayList<Application> archivedApplications = ApplicationList.getArchivedApplications(userApplications);

        System.out.println("   INTERNSHIP APPLICATION SUMMARY   ");
        System.out.println("------------------------------------");

        System.out.println("Total Applications Tracked: " + userApplications.size());
        System.out.println("Active Applications: " + activeApplications.size());
        System.out.println("Archived Applications: " + archivedApplications.size());
        System.out.println();

        printStatusBreakdown(activeApplications);
        System.out.println();

        printUpcomingDeadlines(activeApplications, 7);
    }

    /**
     * Helper method to group and print applications by their status.
     */
    private static void printStatusBreakdown(ArrayList<Application> userApplications) {
        Map<String, Integer> statusCounts = new HashMap<>();

        if (userApplications.isEmpty()) {
            System.out.println("Status Overview (Active Only):");
            System.out.println(" - No active applications to summarize.");
            return;
        }
        for (Application app : userApplications) {
            String status = app.getStatus();

            if (status == null || status.trim().isEmpty()) {
                status = "Unknown";
            }
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
        }

        System.out.println("Status Overview (Active Only):");
        for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
            System.out.println(" - " + entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Helper method to find and print applications with deadlines approaching soon.
     */
    private static void printUpcomingDeadlines(ArrayList<Application> userApplications, int daysAhead) {
        System.out.println("Upcoming Deadlines (Next " + daysAhead + " days, Active Only):");

        LocalDate today = LocalDate.now();
        LocalDate cutoffDate = today.plusDays(daysAhead);
        boolean foundDeadlines = false;

        for (Application app : userApplications) {
            LocalDate deadline = app.getDeadline();

            if (deadline != null && !deadline.isBefore(today) && !deadline.isAfter(cutoffDate)) {
                long daysUntil = ChronoUnit.DAYS.between(today, deadline);
                System.out.println(" - " + app.getCompany() + " (" + app.getRole() + ") : Due in "
                        + daysUntil + " days.");
                foundDeadlines = true;
            }
        }

        if (!foundDeadlines) {
            System.out.println(" - You have no immediate deadlines approaching. Great job!");
        }
    }
}
