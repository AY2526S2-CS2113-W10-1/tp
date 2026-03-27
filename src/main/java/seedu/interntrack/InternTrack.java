package seedu.interntrack;

import java.util.ArrayList;
import java.util.Stack;
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
    private static final String SORT_COMMAND = "sort";
    private static final String UNDO_COMMAND = "undo";
    private static final Logger logger = Logger.getLogger("InternTrack");

    /**
     * Starts the InternTrack application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        ArrayList<Application> userApplications = new ArrayList<>();
        Stack<ArrayList<Application>> undoHistory = new Stack<>();
        Storage.loadApplications(userApplications);
        Ui.printWelcome();
        while (Ui.hasMoreCommands()) {
            String line = Ui.readCommand();
            if (line == null || line.trim().equals(BYE_COMMAND)) {
                break;
            }
            Ui.printBorder();
            handleCommand(line, userApplications, undoHistory);
            Ui.printBorder();
        }
        Ui.printGoodbye();
    }

    /**
     * Dispatches the given command to the appropriate handler.
     *
     * @param line             The raw command string entered by the user.
     * @param userApplications The current list of applications.
     * @param undoHistory The stack storing previous application list states for undo.
     */
    private static void handleCommand(String line, ArrayList<Application> userApplications,
                                      Stack<ArrayList<Application>> undoHistory) {
        String trimmedLine = line.trim();

        if (trimmedLine.isEmpty()) {
            return;
        }

        String[] parts = trimmedLine.split("\\s+", 2);
        String command = parts[0];

        try {
            if (command.equals(ADD_COMMAND)) {
                handleAddCommand(trimmedLine, userApplications, undoHistory);
            } else if (command.equals(EDIT_COMMAND)) {
                handleEditCommand(trimmedLine, userApplications, undoHistory);
            } else if (command.equals(DELETE_COMMAND)) {
                handleDeleteCommand(trimmedLine, userApplications, undoHistory);
            } else if (command.equals(UNDO_COMMAND)) {
                handleUndoCommand(userApplications, undoHistory);
            } else if (command.equals(FILTER_COMMAND)) {
                handleFilterCommand(trimmedLine, userApplications);
            } else if (command.equals(LIST_COMMAND)) {
                handleListCommand(userApplications);
            } else if (command.equals(SORT_COMMAND)) {
                handleSortCommand(trimmedLine, userApplications);
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
     * Handles the add command by adding a new application to current application lists.
     */
    private static void handleAddCommand(String line, ArrayList<Application> userApplications,
                                         Stack<ArrayList<Application>> undoHistory)
            throws InternTrackException {
        logger.log(Level.INFO, "Processing ADD command");

        Application newApplication = Parser.createApplication(line);
        saveStateForUndo(userApplications, undoHistory);

        int sizeBefore = userApplications.size();
        userApplications.add(newApplication);

        assert userApplications.size() == sizeBefore + 1
                : "List size should increment after a successful add";

        logger.log(Level.INFO, "Successfully added application: "
                + newApplication.getCompany() + " - " + newApplication.getRole());
        Ui.printAddApplication(newApplication, userApplications);
        Storage.saveApplications(userApplications);
    }

    /**
     * Handles the delete command by removing an application.
     *
     * @param line             The raw command string entered by the user.
     * @param userApplications The current list of applications.
     */
    private static void handleDeleteCommand(String line, ArrayList<Application> userApplications,
                                            Stack<ArrayList<Application>> undoHistory)
            throws InternTrackException {
        int index = Parser.parseDeleteIndex(line);

        if (index < 0 || index >= userApplications.size()) {
            throw new InternTrackException("Invalid application index.");
        }

        saveStateForUndo(userApplications, undoHistory);

        Application removedApplication = userApplications.remove(index);

        logger.log(Level.INFO, "Deleted application: "
                + removedApplication.getCompany() + " - " + removedApplication.getRole());

        Ui.printDeleteApplication(removedApplication, index);

        Storage.saveApplications(userApplications);
    }

    /**
     * Handles the list command by listing all applications.
     */
    private static void handleListCommand(ArrayList<Application> userApplications)
            throws InternTrackException {
        Ui.printAllApplications(userApplications);
        logger.info("Showing all current applications");
    }

    /**
     * Handles the edit command by updating an application's status.
     */
    private static void handleEditCommand(String line, ArrayList<Application> userApplications,
                                          Stack<ArrayList<Application>> undoHistory)
            throws InternTrackException {
        logger.info("Processing edit command: " + line);

        int index = Parser.parseEditIndex(line);
        String status = Parser.parseEditStatus(line);

        assert !status.isBlank() : "Parsed status should not be blank";

        logger.info("Editing application at index " + index + " with new status: " + status);

        saveStateForUndo(userApplications, undoHistory);

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

    /**
     * Handles the sort command by giving new application lists with some criteria.
     */
    private static void handleSortCommand(String line, ArrayList<Application> userApplications)
            throws InternTrackException {
        String[] criteria = Parser.parseSortCriteria(line);

        assert criteria.length > 0 : "There must be some sorting criteria";
        assert criteria.length < 4 : "There are at most 3 criteria";
        ArrayList<Application> sortedApps = ApplicationList.sortApplicationsByCriteria(userApplications, criteria);
        Ui.printSortedApplications(sortedApps, criteria);
    }

    /**
     * Handles the undo command by restoring the most recent application list state.
     *
     * @param userApplications The current list of applications.
     * @param undoHistory The stack storing previous application list states.
     * @throws InternTrackException If there is no previous state to restore.
     */
    private static void handleUndoCommand(ArrayList<Application> userApplications,
                                          Stack<ArrayList<Application>> undoHistory)
            throws InternTrackException {
        if (undoHistory.isEmpty()) {
            throw new InternTrackException("No command to undo.");
        }

        ArrayList<Application> previousState = undoHistory.pop();
        userApplications.clear();
        userApplications.addAll(previousState);

        Storage.saveApplications(userApplications);
        Ui.printUndoSuccess();
        logger.info("Successfully restored previous application list state");
    }

    /**
     * Returns a deep copy of the current application list.
     *
     * @param userApplications The application list to copy.
     * @return A deep copy of the application list.
     */
    private static ArrayList<Application> copyApplicationList(ArrayList<Application> userApplications) {
        ArrayList<Application> copiedList = new ArrayList<>();
        for (Application application : userApplications) {
            copiedList.add(new Application(application));
        }
        return copiedList;
    }

    /**
     * Saves the current application list state for undo.
     *
     * @param userApplications The current application list.
     * @param undoHistory The stack storing previous application list states.
     */
    private static void saveStateForUndo(ArrayList<Application> userApplications,
                                         Stack<ArrayList<Application>> undoHistory) {
        undoHistory.push(copyApplicationList(userApplications));
    }

}
