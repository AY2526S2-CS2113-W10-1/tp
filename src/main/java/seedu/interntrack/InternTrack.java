package seedu.interntrack;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point and command handler for the InternTrack application.
 * Handles user commands, application management, undo functionality,
 * and coordinates interactions between Parser, UI, Storage, and ApplicationList.
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
    private static final String REMIND_COMMAND = "remind";
    private static final String SUMMARY_COMMAND = "summary";
    private static final String ARCHIVE_COMMAND = "archive";
    private static final String UNARCHIVE_COMMAND = "unarchive";
    private static final String LIST_ARCHIVED_COMMAND = "listarchived";
    private static final Logger logger = Logger.getLogger("InternTrack");

    /**
     * Starts the InternTrack application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        //Set up application list, history, storage and logger
        ArrayList<Application> userApplications = new ArrayList<>();
        Stack<ArrayList<Application>> undoHistory = new Stack<>();
        Storage.loadApplications(userApplications);
        LoggerConfig.setup();
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
     * @param undoHistory      The stack storing previous application list states for undo.
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
                ensureNoArguments(trimmedLine, "Use format: undo");
                handleUndoCommand(userApplications, undoHistory);
            } else if (command.equals(FILTER_COMMAND)) {
                handleFilterCommand(trimmedLine, userApplications);
            } else if (command.equals(LIST_COMMAND)) {
                ensureNoArguments(trimmedLine, "Use format: list");
                handleListCommand(userApplications);
            } else if (command.equals(SORT_COMMAND)) {
                handleSortCommand(trimmedLine, userApplications);
            } else if (command.equals(REMIND_COMMAND)) {
                handleRemindCommand(trimmedLine, userApplications);
            } else if (command.equals(SUMMARY_COMMAND)) {
                ensureNoArguments(trimmedLine, "Use format: summary");
                handleSummaryCommand(userApplications);
            } else if (command.equals(ARCHIVE_COMMAND)) {
                handleArchiveCommand(trimmedLine, userApplications, undoHistory);
            } else if (command.equals(UNARCHIVE_COMMAND)) {
                handleUnarchiveCommand(trimmedLine, userApplications, undoHistory);
            } else if (command.equals(LIST_ARCHIVED_COMMAND)) {
                ensureNoArguments(trimmedLine, "Use format: listarchived");
                handleListArchivedCommand(userApplications);
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
     * Adds a new application to the current application list.
     * Parses the input line to create an application, saves the current state
     * for undo functionality, logs the operation, displays confirmation,
     * and persists changes to local storage.
     *
     * @param line             The raw input string containing application details.
     * @param userApplications The list of applications to be updated.
     * @param undoHistory      The stack used to store previous states of the list.
     * @throws InternTrackException If the input format is invalid, required fields are missing, or parsing fails.
     */
    private static void handleAddCommand(String line, ArrayList<Application> userApplications,
                                         Stack<ArrayList<Application>> undoHistory)
            throws InternTrackException {
        logger.log(Level.INFO, "Processing ADD command");
        saveStateForUndo(userApplications, undoHistory);
        Application newApplication = ApplicationList.addApplication(userApplications, line);
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

        saveStateForUndo(userApplications, undoHistory);

        Application removedApplication =
                ApplicationList.deleteActiveApplication(userApplications, index);

        logger.log(Level.INFO, "Deleted active application: "
                + removedApplication.getCompany() + " - " + removedApplication.getRole());

        Ui.printDeleteApplication(removedApplication, index);

        Storage.saveApplications(userApplications);
    }

    /**
     * Handles the list command by displaying all applications.
     *
     * @param userApplications The current list of applications.
     * @throws InternTrackException If printing fails.
     */
    private static void handleListCommand(ArrayList<Application> userApplications)
            throws InternTrackException {
        Ui.printAllApplications(ApplicationList.getActiveApplications(userApplications));
        logger.info("Showing all current applications");
    }

    /**
     * Handles the edit command by updating fields of an existing application.
     * Saves the current state for undo before editing and updates storage.
     *
     * @param line             The raw command string entered by the user.
     * @param userApplications The current list of applications.
     * @param undoHistory      The stack storing previous application list states.
     * @throws InternTrackException If parsing fails or index is invalid.
     */
    private static void handleEditCommand(String line, ArrayList<Application> userApplications,
                                          Stack<ArrayList<Application>> undoHistory)
            throws InternTrackException {
        logger.info("Processing edit command: " + line);

        int index = Parser.parseEditIndex(line);
        EditDetails editDetails = Parser.parseEditDetails(line);

        logger.info("Editing application at index " + index);

        saveStateForUndo(userApplications, undoHistory);

        Application updatedApplication = ApplicationList.editApplication(userApplications, index, editDetails);

        Ui.printEditApplication(updatedApplication, index, editDetails);
        Storage.saveApplications(userApplications);

        logger.info("Successfully updated application at index " + index);
    }

    /**
     * Handles the filter command by displaying applications that match the given criteria.
     *
     * @param line             The raw command string entered by the user.
     * @param userApplications The current list of applications.
     * @throws InternTrackException If filter criteria is invalid.
     */
    private static void handleFilterCommand(String line, ArrayList<Application> userApplications)
            throws InternTrackException {
        FilterCriteria criteria = Parser.parseFilterCriteria(line);
        ArrayList<Application> filteredApplications = ApplicationList.filterApplications(userApplications, criteria);
        Ui.printFilteredApplications(filteredApplications, criteria);
    }

    /**
     * Handles the sort command by sorting applications based on specified criteria.
     *
     * @param line             The raw command string entered by the user.
     * @param userApplications The current list of applications.
     * @throws InternTrackException If sorting criteria is invalid.
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
     * @param undoHistory      The stack storing previous application list states.
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
     * Handles the remind command by filtering and displaying applications with deadlines.
     * Parses the number of days from the input, calculates the cutoff date, and
     * triggers the UI to print applications due on or before that date.
     *
     * @param line             The raw command string containing the number of days.
     * @param userApplications The current list of applications to filter.
     * @throws InternTrackException If the input format is invalid.
     */
    private static void handleRemindCommand(String line, ArrayList<Application> userApplications)
            throws InternTrackException {
        int numDays = Parser.parseRemindDays(line);
        ArrayList<Application> filteredApplications =
                ApplicationList.filterApplicationsByDaysAhead(userApplications, numDays);
        LocalDate remindDate = LocalDate.now().plusDays(numDays);
        assert remindDate.isAfter(LocalDate.now()) : "Remind date should be in the future";
        Ui.printUpcomingDeadlines(filteredApplications, numDays, remindDate);
    }

    /**
     * Handles the summary command by generating and displaying a report of applications.
     * ADDED METHOD.
     *
     * @param userApplications The current list of applications to summarize.
     */
    private static void handleSummaryCommand(ArrayList<Application> userApplications) {
        logger.info("Processing summary command");
        SummaryCommand.execute(userApplications);
    }

    /**
     * Handles the archive command by marking an application as archived.
     * Saves the current state for undo before performing the operation.
     *
     * @param line             Raw user input containing the index.
     * @param userApplications The current list of applications.
     * @param undoHistory      The undo stack storing previous states.
     * @throws InternTrackException If the index is invalid or already archived.
     */
    private static void handleArchiveCommand(String line, ArrayList<Application> userApplications,
                                             Stack<ArrayList<Application>> undoHistory)
            throws InternTrackException {

        int index = Parser.parseArchiveIndex(line);

        saveStateForUndo(userApplications, undoHistory);

        Application archivedApplication = ApplicationList.archiveApplication(userApplications, index);

        Ui.printArchiveApplication(archivedApplication, index);
        Storage.saveApplications(userApplications);

        logger.info("Archived application at index " + index);
    }

    /**
     * Handles the unarchive command by restoring an archived application.
     * Saves the current state for undo before performing the operation.
     *
     * @param line             Raw user input containing the index.
     * @param userApplications The current list of applications.
     * @param undoHistory      The undo stack storing previous states.
     * @throws InternTrackException If the index is invalid or not archived.
     */
    private static void handleUnarchiveCommand(String line, ArrayList<Application> userApplications,
                                               Stack<ArrayList<Application>> undoHistory)
            throws InternTrackException {

        int index = Parser.parseUnarchiveIndex(line);

        saveStateForUndo(userApplications, undoHistory);

        Application unarchivedApplication =
                ApplicationList.unarchiveApplication(userApplications, index);

        Ui.printUnarchiveApplication(unarchivedApplication, index);
        Storage.saveApplications(userApplications);

        logger.info("Unarchived application at index " + index);
    }

    /**
     * Handles the listarchived command by displaying all archived applications.
     *
     * @param userApplications The current list of applications.
     * @throws InternTrackException If printing fails.
     */
    private static void handleListArchivedCommand(ArrayList<Application> userApplications)
            throws InternTrackException {

        ArrayList<Application> archivedApplications =
                ApplicationList.getArchivedApplications(userApplications);

        Ui.printArchivedApplications(archivedApplications);

        logger.info("Showing archived applications");
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
     * Ensures that a command has no extra arguments beyond the command word.
     *
     * @param input       The raw input string.
     * @param formatError The error message to throw if extra arguments are present.
     * @throws InternTrackException If extra arguments are detected.
     */
    private static void ensureNoArguments(String input, String formatError)
            throws InternTrackException {

        String[] parts = input.trim().split("\\s+", 2);

        if (parts.length > 1) {
            throw new InternTrackException(formatError);
        }
    }

    /**
     * Saves the current application list state for undo.
     *
     * @param userApplications The current application list.
     * @param undoHistory      The stack storing previous application list states.
     */
    private static void saveStateForUndo(ArrayList<Application> userApplications,
                                         Stack<ArrayList<Application>> undoHistory) {
        undoHistory.push(copyApplicationList(userApplications));
    }
}
