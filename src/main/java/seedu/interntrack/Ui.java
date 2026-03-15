package seedu.interntrack;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the user interface for handling input and output.
 */
public class Ui {
    private static final String BORDER = "____________________________________________________________";
    private static final String GOODBYE_MESSAGE = BORDER
            + " Bye. Hope to see you again soon!\n"
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
    public static void printAllApplications(ArrayList<Application> userApplications) {
        if (userApplications.isEmpty()) {
            System.out.println("You have not applied for any roles yet, start applying now!");
            return;
        }
        int applicationCount = userApplications.size();
        System.out.println("You have applied for " + applicationCount + ((applicationCount > 1) ? " roles" : " role"));
        for (int i = 0; i < applicationCount; i++) {
            Application app = userApplications.get(i);
            String roles = app.getRole();
            String company = app.getCompany();
            String status = app.getStatus();
            assert app.getCompany() != null && !app.getCompany().isEmpty() :
                    "Existing application must have company";
            assert app.getRole() != null && !app.getRole().isEmpty() :
                    "Existing application must have role";
            assert app.getStatus() != null:
                    "Existing application must have status";
            String deadline = (app.getDeadline() != null) ? " Apply by " + app.getDeadline().toString() + "." : "";
            String contact = (app.getContact() != null) ? " Contact with " + app.getContact() + "." : "";
            System.out.println((i + 1) + ". " + roles + " at " + company + " is " + status + "." + deadline + contact);
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
     * Prints an error message when an unrecognised command is entered.
     */
    public static void printUnknownCommand() {
        System.out.println(UNKNOWN_COMMAND_MESSAGE);
    }
}
