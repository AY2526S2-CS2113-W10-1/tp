package seedu.interntrack;

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

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Reads a line of user input from the console.
     *
     * @return The user input as a string.
     */
    public static String readCommand() {
        return scanner.nextLine(); // Used here to return the string
    }

    /**
     * Check if there is more user input to read.
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
     * Prints all applications in the current list.
     *
     * @param userApplications The current list used to retrieve the total count.
     */
    private static void printApplication(Application app, int index) {
        String roles = app.getRole();
        String company = app.getCompany();
        String status = app.getStatus();
        assert company != null && !company.isEmpty() :
                "Existing application must have company";
        assert roles != null && !roles.isEmpty() :
                "Existing application must have role";
        assert status != null :
                "Existing application must have status";
        String deadline = (app.getDeadline() != null) ? " Apply by " + app.getDeadline().toString() + "." : "";
        String contact = (app.getContact() != null) ? " Contact with " + app.getContact() + "." : "";
        System.out.println((index + 1) + ". " + roles + " at " + company + " is " + status + "." + deadline + contact);
    }

    public static void printAllApplications(ArrayList<Application> userApplications) {
        if (userApplications.isEmpty()) {
            System.out.println("You have not applied for any roles yet, start applying now!");
            return;
        }
        int applicationCount = userApplications.size();
        System.out.println("You have applied for " + applicationCount + ((applicationCount > 1) ? " roles" : " role"));
        for (int i = 0; i < applicationCount; i++) {
            Application app = userApplications.get(i);
            printApplication(app, i);
        }
    }

    /**
     * Prints applications filtered by the given status.
     *
     * @param filteredApplications The filtered list to display.
     * @param status The status used for filtering.
     */
    public static void printFilteredApplications(ArrayList<Application> filteredApplications, String status) {
        if (filteredApplications.isEmpty()) {
            System.out.println("No applications found with status: " + status + ".");
            return;
        }
        int applicationCount = filteredApplications.size();
        System.out.println("You have " + applicationCount
                + ((applicationCount > 1) ? " applications" : " application")
                + " with status " + status + ".");
        for (int i = 0; i < applicationCount; i++) {
            Application app = filteredApplications.get(i);
            printApplication(app, i);
        }
    }

    /**
     * Prints confirmation after editing an application.
     *
     * @param application The updated application.
     * @param index       The index of the application.
     */
    public static void printEditApplication(Application application, int index) {
        System.out.println("Nice! I've updated application " + index + ":");
        System.out.println("  " + application.toString());
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
     * Prints an error message when an unrecognised command is entered.
     */
    public static void printUnknownCommand() {
        System.out.println(UNKNOWN_COMMAND_MESSAGE);
    }
}
