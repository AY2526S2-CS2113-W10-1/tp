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
    private static final String LIST_COMMAND = "list";
    private static final Logger logger = Logger.getLogger("InternTrack");

    /**
     * Starts the InternTrack application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        ArrayList<Application> userApplications = new ArrayList<>();
        assert userApplications != null : "userApplications list should not be null during command handling";
        Storage.loadApplications(userApplications);
        Ui.printWelcome();
        String line = "";
        while (Ui.hasMoreCommands()&&!line.equals(BYE_COMMAND)) {
            line = Ui.readCommand();
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
                assert line.contains(ADD_COMMAND)
                        : "Logic error: handleCommand reached 'add' block without 'add' in string";
                int sizeBefore = userApplications.size();
                Application newApplication = ApplicationList.addApplications(userApplications, line);
                assert userApplications.size() == sizeBefore + 1 : "List size should increment after a successful add";
                logger.log(Level.INFO, "Successfully added application: "
                        + newApplication.getCompany() + " - " + newApplication.getRole());
                Ui.printAddApplication(newApplication, userApplications);
                Storage.saveApplications(userApplications);
            } else if (line.startsWith(EDIT_COMMAND)) {
                handleEditCommand(line, userApplications);
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
     * Handles the list command by listing all applications.
     *
     * @param line             The raw command string entered by the user.
     * @param userApplications The current list of applications.
     * @throws InternTrackException If the command format or index is invalid.
     */
    private static void handleListCommand(String line, ArrayList<Application> userApplications)
            throws InternTrackException {
        Ui.printAllApplications(userApplications);
    }

    /**
     * Handles the edit command by updating an application's status.
     *
     * @param line             The raw command string entered by the user.
     * @param userApplications The current list of applications.
     * @throws InternTrackException If the command format or index is invalid.
     */
    private static void handleEditCommand(String line, ArrayList<Application> userApplications)
            throws InternTrackException {
        int index = Parser.parseEditIndex(line);
        String status = Parser.parseEditStatus(line);
        Application updatedApplication = ApplicationList.editApplicationStatus(userApplications, index, status);
        Ui.printEditApplication(updatedApplication, index);
        Storage.saveApplications(userApplications);
    }
}
