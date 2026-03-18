package seedu.interntrack;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Serves as the main entry point and command handler for InternTrack.
 */
public class InternTrack {
    private static final String ADD_COMMAND = "add";
    private static final String BYE_COMMAND = "bye";
    private static final String EDIT_COMMAND = "edit";
    private static final String FILTER_COMMAND = "filter";
    private static final String LIST_COMMAND = "list";
    private static final String DELETE_COMMAND = "delete";
    private static final Logger logger = Logger.getLogger("InternTrack");

    /**
     * Starts the InternTrack application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        ArrayList<Application> userApplications = new ArrayList<>();
        Storage.loadApplications(userApplications);
        Ui.printWelcome();
        while (Ui.hasMoreCommands()) {
            String line = Ui.readCommand();
            if (line == null || line.trim().equals(BYE_COMMAND)) {
                break;
            }
            Ui.printBorder();
            handleCommand(line, userApplications);
            Ui.printBorder();
        }
        Ui.printGoodbye();
    }

    /**
     * Dispatches the given command to the appropriate handler.
     *
     * @param line             The raw command string entered by the user.
     * @param userApplications The current list of applications.
     */
    private static void handleCommand(String line, ArrayList<Application> userApplications) {
        try {
            if (line.startsWith(ADD_COMMAND)) {
                logger.log(Level.INFO, "Processing ADD command");

                int sizeBefore = userApplications.size();
                Application newApplication = ApplicationList.addApplications(userApplications, line);

                assert userApplications.size() == sizeBefore + 1 :
                        "List size should increment after a successful add";

                logger.log(Level.INFO, "Successfully added application: "
                        + newApplication.getCompany() + " - " + newApplication.getRole());
                Ui.printAddApplication(newApplication, userApplications);
                Storage.saveApplications(userApplications);
            } else if (line.startsWith(EDIT_COMMAND)) {
                handleEditCommand(line, userApplications);
            } else if (line.startsWith(DELETE_COMMAND)) {
                handleDeleteCommand(line, userApplications);
            } else if (line.startsWith(FILTER_COMMAND)) {
                handleFilterCommand(line, userApplications);
            } else if (line.startsWith(LIST_COMMAND)) {
                handleListCommand(line, userApplications);
            } else {
                logger.log(Level.WARNING, "Unknown command received: " + line);
                Ui.printUnknownCommand();
            }
        } catch (InternTrackException e) {
            logger.log(Level.WARNING, "InternTrackException during command processing: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the delete command by removing an application.
     *
     * @param line             The raw command string entered by the user.
     * @param userApplications The current list of applications.
     */
    private static void handleDeleteCommand(String line, ArrayList<Application> userApplications)
            throws InternTrackException {

        int index = Parser.parseDeleteIndex(line);

        if (index < 0 || index >= userApplications.size()) {
            throw new InternTrackException("Invalid application index.");
        }

        Application removedApplication = userApplications.remove(index);

        logger.log(Level.INFO, "Deleted application: "
                + removedApplication.getCompany() + " - " + removedApplication.getRole());

        Ui.printDeleteApplication(removedApplication, index);

        Storage.saveApplications(userApplications);
    }

    /**
     * Handles the list command by listing all applications.
     */
    private static void handleListCommand(String line, ArrayList<Application> userApplications)
            throws InternTrackException {
        Ui.printAllApplications(userApplications);
    }

    /**
     * Handles the edit command by updating an application's status.
     */
    private static void handleEditCommand(String line, ArrayList<Application> userApplications)
            throws InternTrackException {
        logger.info("Processing edit command: " + line);

        int index = Parser.parseEditIndex(line);
        String status = Parser.parseEditStatus(line);

        assert !status.isBlank() : "Parsed status should not be blank";

        logger.info("Editing application at index " + index + " with new status: " + status);

        Application updatedApplication =
                ApplicationList.editApplicationStatus(userApplications, index, status);

        assert updatedApplication.getStatus().equals(status)
                : "Application status should match edited status";

        Ui.printEditApplication(updatedApplication, index);
        Storage.saveApplications(userApplications);

        logger.info("Successfully updated application at index " + index);
    }

    /**
     * Handles the filter command by listing applications that match the status.
     */
    private static void handleFilterCommand(String line, ArrayList<Application> userApplications)
            throws InternTrackException {
        String status = Parser.parseFilterStatus(line);
        ArrayList<Application> filteredApplications =
                ApplicationList.filterApplicationsByStatus(userApplications, status);
        Ui.printFilteredApplications(filteredApplications, status);
    }
}
