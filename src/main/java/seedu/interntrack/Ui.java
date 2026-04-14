package seedu.interntrack;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the user interface for handling input and output.
 */
public class Ui {
    private static final String BORDER = "____________________________________________________________";
    private static final String GOODBYE_MESSAGE = BORDER
            + "\nBye. Hope to see you again soon!\n"
            + BORDER;
    private static final String UNKNOWN_COMMAND_MESSAGE = "Unknown command. Please try again.";

    private static final Scanner scanner = new Scanner(System.in);
    private static final String DEFAULT_STATUS1 = "Applied";
    private static final String DEFAULT_STATUS2 = "Pending";
    private static final String DEFAULT_STATUS3 = "Accepted";
    private static final String DEFAULT_STATUS4 = "Rejected";

    /**
     * Reads a line of user input from the console.
     *
     * @return The user input as a string.
     */
    public static String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Checks if there is more user input to read.
     *
     * @return true if there is more input, false otherwise.
     */
    public static boolean hasMoreCommands() {
        return scanner.hasNextLine();
    }

    /**
     * Prints a horizontal border to the console.
     */
    public static void printBorder() {
        System.out.println(BORDER);
    }

    /**
     * Prints the goodbye message to the console.
     */
    public static void printGoodbye() {
        System.out.println(GOODBYE_MESSAGE);
    }

    /**
     * Prints the welcome message to the console.
     */
    public static void printWelcome() {
        System.out.println(BORDER);
        System.out.println(" Hello! I'm InternTrack");
        System.out.println(" What can I do for you?");
        System.out.println(BORDER);
    }

    /**
     * Prints a confirmation message when a new application is added.
     *
     * @param newApplication   The application that was added.
     * @param userApplications The current list used to retrieve the total count.
     */
    public static void printAddApplication(Application newApplication, ArrayList<Application> userApplications) {
        System.out.println("Got it. I've added this application:");
        System.out.println("  " + newApplication.toString());
        System.out.println("Now you have " + userApplications.size() + " applications in the list.");
    }

    /**
     * Prints a single application entry.
     *
     * @param app   The application to print.
     * @param index The zero-based index of the application in the list.
     */
    private static void printApplication(Application app, int index) {
        String role = app.getRole();
        String company = app.getCompany();
        String status = app.getStatus();
        assert company != null && !company.isEmpty() : "Existing application must have company.";
        assert role != null && !role.isEmpty() : "Existing application must have role.";
        assert status != null : "Existing application must have status.";
        String deadline = (app.getDeadline() != null) ? " Apply by " + app.getDeadline().toString() + "." : "";
        String contact = (app.getContact() != null) ? " Contact with " + app.getContact() + "." : "";
        String archiveLabel = app.isArchived() ? "[Archived] " : "";
        System.out.println((index + 1) + ". " + archiveLabel
                + role + " at " + company + " is " + status + "." + deadline + contact);
    }

    /**
     * Prints all applications in the current list.
     *
     * @param userApplications The current list used to retrieve the total count.
     */
    public static void printAllApplications(ArrayList<Application> userApplications) {
        assert userApplications != null : "Application list should not be null";
        if (userApplications.isEmpty()) {
            System.out.println("You have not applied for any roles yet, start applying now!");
            return;
        }
        int applicationCount = userApplications.size();
        assert applicationCount > 0 : "Application count should be positive when not empty";
        System.out.println("You have applied for " + applicationCount + ((applicationCount > 1) ? " roles" : " role"));
        for (int i = 0; i < applicationCount; i++) {
            Application app = userApplications.get(i);
            printApplication(app, i);
        }
    }

    public static void printArchivedApplications(ArrayList<Application> archivedApplications) {
        if (archivedApplications.isEmpty()) {
            System.out.println("You have no archived applications.");
            return;
        }

        int applicationCount = archivedApplications.size();
        System.out.println("You have " + applicationCount
                + ((applicationCount > 1) ? " archived applications" : " archived application"));

        for (int i = 0; i < applicationCount; i++) {
            Application app = archivedApplications.get(i);
            printApplication(app, i);
        }
    }

    public static void printArchiveApplication(Application application, int index) {
        System.out.println("Nice! I've archived application " + index + ":");
        System.out.println("  " + application.toString());
    }

    public static void printUnarchiveApplication(Application application, int index) {
        System.out.println("Nice! I've restored application " + index + ":");
        System.out.println("  " + application.toString());
    }

    /**
     * Prints applications filtered by the given criterion.
     *
     * @param filteredApplications The filtered list to display.
     * @param criteria             The criterion used for filtering.
     */
    public static void printFilteredApplications(ArrayList<Application> filteredApplications,
                                                 FilterCriteria criteria) {
        String filterSummary = criteria.getSummary();
        if (filteredApplications.isEmpty()) {
            System.out.println("No applications found matching " + filterSummary + ".");
            return;
        }
        int applicationCount = filteredApplications.size();
        System.out.println("You have " + applicationCount
                + ((applicationCount > 1) ? " applications" : " application")
                + " matching " + filterSummary + ".");
        for (int i = 0; i < applicationCount; i++) {
            Application app = filteredApplications.get(i);
            printApplication(app, i);
        }
    }

    /**
     * Prints applications filtered by the given status.
     *
     * @param sortedApplications The sorted list to display.
     * @param criterias          The criteria used for sorting.
     */
    public static void printSortedApplications(ArrayList<Application> sortedApplications, String[] criterias) {
        if (sortedApplications.isEmpty()) {
            System.out.println("No applications found after sorting");
            return;
        }
        int applicationCount = sortedApplications.size();
        assert criterias.length > 0 : "There must be some sorting criteria";
        assert criterias.length < 4 : "There are at most 3 criteria";
        boolean isDesc = false;
        boolean isNonnull = false;
        for (int i = 1; i < criterias.length; i++) {
            assert criterias[i].equals("DESC") || criterias[i].equals("NONNULL") : "Unknown flag: " + criterias[i];
            if (criterias[i].equals("DESC")) {
                isDesc = true;
            } else {
                isNonnull = true;
            }
        }
        String criteria = (criterias[0].equals("ROLE")) ? "role" :
                (criterias[0].equals("COMPANY")) ? "company" :
                        (criterias[0].equals("DEADLINE")) ? "deadline" :
                                (criterias[0].equals("STATUS")) ? "status" : "contact";
        System.out.println("The application list has been sorted by " + criteria
                + " in " + (isDesc ? "descending order" : "ascending order")
                + (isNonnull ? " with null entries removed." : "."));
        for (int i = 0; i < applicationCount; i++) {
            Application app = sortedApplications.get(i);
            printApplication(app, i);
        }
    }

    /**
     * Prints confirmation after editing an application.
     *
     * @param application The updated application.
     * @param index       The index of the application.
     */
    public static void printEditApplication(Application application, int index, EditDetails editDetails) {
        System.out.println("Nice! I've updated application " + index + ":");
        System.out.println("  " + application.toString());
        if (editDetails.getStatus() != null
                && !editDetails.getStatus().equals(DEFAULT_STATUS1)
                && !editDetails.getStatus().equals(DEFAULT_STATUS2)
                && !editDetails.getStatus().equals(DEFAULT_STATUS3)
                && !editDetails.getStatus().equals(DEFAULT_STATUS4)) {
            System.out.println("Warning: you are using custom " +
                    "status outside pre-defined status: Applied, Pending, Accepted, Rejected");
        }
    }

    /**
     * Prints confirmation after deleting an application.
     *
     * @param application The removed application.
     * @param index       The index of the removed application.
     */
    public static void printDeleteApplication(Application application, int index) {
        System.out.println("Noted. I've removed this application:");
        System.out.println("  " + (index + 1) + ". " + application.toString());
    }

    /**
     * Prints confirmation after undoing the most recent modifying command.
     */
    public static void printUndoSuccess() {
        System.out.println("Done. I've undone the most recent change.");
    }

    /**
     * Prints an error message when an unrecognised command is entered.
     */
    public static void printUnknownCommand() {
        System.out.println(UNKNOWN_COMMAND_MESSAGE);
    }

    /**
     * Prints a warning when the save file cannot be accessed.
     */
    public static void printStorageWarning() {
        System.out.println("Warning: Unable to open save file, your changes may be lost. Please retry");
    }

    /**
     * Prints a warning when saving data to disk fails.
     */
    public static void printSaveFailureWarning() {
        System.out.println("Warning: Failed to save data to disk. Your changes may be lost.");
    }

    /**
     * Prints applications with upcoming deadlines within the specified number of days.
     *
     * @param upcomingApplications The list of applications with upcoming deadlines.
     * @param numDays              The number of days used for the deadline filter.
     * @param remindDate           The specific cutoff date for the reminder.
     */
    public static void printUpcomingDeadlines(ArrayList<Application> upcomingApplications,
                                              int numDays, LocalDate remindDate) {
        if (upcomingApplications.isEmpty()) {
            System.out.println("No applications due in the next " + numDays + " days.");
            return;
        }
        int applicationCount = upcomingApplications.size();
        String applicationLabel = (applicationCount > 1) ? " applications" : " application";
        String dateRange = " (up to " + remindDate.toString() + "):";
        System.out.println("You have " + applicationCount + applicationLabel
                + " due in the next " + numDays + " days" + dateRange);
        for (int i = 0; i < applicationCount; i++) {
            Application app = upcomingApplications.get(i);
            printApplication(app, i);
        }
    }
}
