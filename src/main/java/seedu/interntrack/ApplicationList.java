package seedu.interntrack;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the list of internship applications.
 */
public class ApplicationList {
    private static final Logger logger = Logger.getLogger("ApplicationList");

    /**
     * Checks if the given application is a duplicate of any existing application in the list.
     *
     * @param userApplications The list of existing applications.
     * @param newApplication The application to check for duplicates.
     * @return true if a duplicate exists in the list, false otherwise.
     */
    private static boolean isApplicationDuplicate(ArrayList<Application> userApplications,
                                                  Application newApplication) {
        for (Application existingApplication : userApplications) {
            if (newApplication.equals(existingApplication)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new application parsed from the given input line to the list.
     *
     * @param userApplications The list to add the application to.
     * @param line             The raw input string containing application details.
     * @return The newly created Application object.
     * @throws InternTrackException If the input is missing required fields or has an invalid date.
     */
    public static Application addApplication(ArrayList<Application> userApplications,
                                             String line) throws InternTrackException {
        Application newApplication = Parser.createApplication(line);
        assert newApplication.getCompany() != null && !newApplication.getCompany().isEmpty() :
                "Application company should be valid after creation";
        assert newApplication.getRole() != null && !newApplication.getRole().isEmpty() :
                "Application role should be valid after creation";

        // Check if the new application is a duplicate
        if (isApplicationDuplicate(userApplications, newApplication)) {
            logger.log(Level.WARNING, "Duplicate application rejected: " + newApplication);
            throw new InternTrackException(
                    "This internship application already exists in your list. "
                            + "Please check your applications before adding a new entry.");
        }

        int sizeBefore = userApplications.size();
        userApplications.add(newApplication);
        assert userApplications.size() == sizeBefore + 1
                : "List size should increment after a successful add";

        logger.log(Level.INFO, "Added new application to list. Total applications: " + userApplications.size());
        return newApplication;
    }

    /**
     * Edits the fields of an existing application.
     *
     * @param userApplications The list containing the application.
     * @param index            The 1-based index of the application to edit.
     * @param editDetails      The collection of updated field values.
     * @return The updated Application object.
     * @throws InternTrackException If the index is invalid or no update fields were supplied.
     */
    public static Application editApplication(ArrayList<Application> userApplications,
                                              int index, EditDetails editDetails) throws InternTrackException {
        if (editDetails == null || !editDetails.hasUpdates()) {
            logger.warning("Edit failed: no update fields were provided");
            throw new InternTrackException("Provide at least one field to edit.");
        }

        Application application = getActiveApplicationByIndex(userApplications, index);

        if (editDetails.getCompany() != null) {
            application.setCompany(editDetails.getCompany());
        }
        if (editDetails.getRole() != null) {
            application.setRole(editDetails.getRole());
        }
        if (editDetails.getDeadline() != null) {
            application.setDeadline(editDetails.getDeadline());
        }
        if (editDetails.getContact() != null) {
            application.setContact(editDetails.getContact());
        }
        if (editDetails.getStatus() != null) {
            application.setStatus(editDetails.getStatus());

        }

        logger.info("Updated application at index " + index + ": " + application);
        return application;
    }

    /**
     * Edits only the status of an existing application.
     *
     * @param userApplications The list containing the application.
     * @param index            The 1-based index of the application to edit.
     * @param status           The updated status value.
     * @return The updated Application object.
     * @throws InternTrackException If the index is invalid.
     */
    public static Application editApplicationStatus(ArrayList<Application> userApplications,
                                                    int index, String status) throws InternTrackException {
        return editApplication(userApplications, index, new EditDetails(null, null, null, null, status));
    }

    /**
     * Filters applications using the supplied criterion.
     *
     * @param userApplications The list to filter.
     * @param criteria         The field and value used for filtering.
     * @return A list of applications that match the criterion.
     */
    public static ArrayList<Application> filterApplications(ArrayList<Application> userApplications,
                                                            FilterCriteria criteria) {
        if (criteria.getField() == FilterCriteria.Field.DEADLINE) {
            return filterApplicationsOnOrBefore(userApplications, criteria.getDeadlineValue());
        }

        ArrayList<Application> filteredApplications = new ArrayList<>();
        for (Application application : userApplications) {
            if (application.isArchived()) {
                continue;
            }
            String applicationValue = getTextFieldValue(application, criteria.getField());
            if (matchesTextCriterion(applicationValue, criteria.getTextValue())) {
                filteredApplications.add(application);
            }
        }
        logger.log(Level.INFO, "Filtered applications by " + criteria.getSummary()
                + ". Matches: " + filteredApplications.size());
        return filteredApplications;
    }

    /**
     * Filters applications whose deadlines are on or before the specified date.
     *
     * @param userApplications The list to filter.
     * @param deadline         The cutoff date for filtering.
     * @return A list of applications with deadlines on or before the given date.
     */
    public static ArrayList<Application> filterApplicationsOnOrBefore(ArrayList<Application> userApplications,
                                                                      LocalDate deadline) {
        ArrayList<Application> filteredApplications = new ArrayList<>();
        for (Application application : userApplications) {
            LocalDate applicationDeadline = application.getDeadline();
            if (!application.isArchived() && applicationDeadline != null 
                    && !applicationDeadline.isAfter(deadline)) {
                filteredApplications.add(application);
            }
        }
        logger.log(Level.INFO, "Filtered applications on or before deadline=" + deadline
                + ". Matches: " + filteredApplications.size());
        return filteredApplications;
    }

    /**
     * Filters applications with deadlines on or before the specified number of days from today.
     * Excludes applications with past deadlines (before today) for the remind feature.
     * Note: numDays is guaranteed to be positive by Parser.parseRemindDays().
     *
     * @param userApplications The list of applications to filter.
     * @param numDays          The number of days from today to consider as the deadline cutoff (always > 0).
     * @return A filtered list of applications with deadlines within the specified range and not in the past.
     */
    public static ArrayList<Application> filterApplicationsByDaysAhead(
            ArrayList<Application> userApplications, int numDays) {
        LocalDate today = LocalDate.now();
        LocalDate cutoffDate = today.plusDays(numDays);
        ArrayList<Application> results = new ArrayList<>();

        for (Application app : userApplications) {
            if (isWithinDateRange(app, today, cutoffDate)) {
                results.add(app);
            }
        }
        logger.info("Retrieved " + results.size() + " reminders for the next " + numDays + " days.");
        return results;
    }

    /**
     * Archives an application at the given index.
     *
     * @param userApplications The list containing the application.
     * @param index            The 1-based index of the application to archive.
     * @return The archived Application object.
     * @throws InternTrackException If the index is invalid or the application is already archived.
     */
    public static Application archiveApplication(ArrayList<Application> userApplications, int index)
            throws InternTrackException {
        Application application = getActiveApplicationByIndex(userApplications, index);
        ensureNotArchived(application);

        application.setArchived(true);
        logger.info("Archived application at index " + index + ": " + application);
        return application;
    }

    /**
     * Restores an archived application at the given index.
     *
     * @param userApplications The list containing the application.
     * @param index            The 1-based index of the application to restore.
     * @return The restored Application object.
     * @throws InternTrackException If the index is invalid or the application is not archived.
     */
    public static Application unarchiveApplication(ArrayList<Application> userApplications, int index)
            throws InternTrackException {
        Application application = getArchivedApplicationByIndex(userApplications, index);
        ensureArchived(application);

        application.setArchived(false);
        logger.info("Unarchived application at index " + index + ": " + application);
        return application;
    }

    /**
     * Deletes an active application using its 1-based displayed index from the active applications list.
     *
     * @param userApplications The full list containing all applications.
     * @param index            The 1-based index from the active applications view.
     * @return The removed active application.
     * @throws InternTrackException If there are no active applications or the index is invalid.
     */
    public static Application deleteActiveApplication(ArrayList<Application> userApplications, int index)
            throws InternTrackException {
        Application application = getActiveApplicationByIndex(userApplications, index);

        boolean wasRemoved = userApplications.remove(application);
        assert wasRemoved : "Active application selected for deletion should exist in the full list";

        logger.info("Deleted active application at index " + index + ": " + application);
        return application;
    }

    /**
     * Returns all active (non-archived) applications.
     *
     * @param userApplications The full list of applications.
     * @return A list containing only active applications.
     */
    public static ArrayList<Application> getActiveApplications(
            ArrayList<Application> userApplications) {
        ArrayList<Application> activeApplications = new ArrayList<>();

        for (Application application : userApplications) {
            if (!application.isArchived()) {
                activeApplications.add(application);
            }
        }

        logger.log(Level.INFO, "Retrieved active applications. Count: "
                + activeApplications.size());
        return activeApplications;
    }

    /**
     * Returns all archived applications.
     *
     * @param userApplications The full list of applications.
     * @return A list containing only archived applications.
     */
    public static ArrayList<Application> getArchivedApplications(
            ArrayList<Application> userApplications) {
        ArrayList<Application> archivedApplications = new ArrayList<>();

        for (Application application : userApplications) {
            if (application.isArchived()) {
                archivedApplications.add(application);
            }
        }

        logger.log(Level.INFO, "Retrieved archived applications. Count: "
                + archivedApplications.size());
        return archivedApplications;
    }

    /**
     * Returns a copy of all applications, including both active and archived ones.
     *
     * @param userApplications The full list of applications.
     * @return A shallow copy of the application list.
     */
    public static ArrayList<Application> getAllApplications(
            ArrayList<Application> userApplications) {
        return new ArrayList<>(userApplications);
    }

    /**
     * Returns the number of active applications.
     *
     * @param userApplications The full list of applications.
     * @return The number of non-archived applications.
     */
    public static int getActiveApplicationCount(ArrayList<Application> userApplications) {
        int count = 0;
        for (Application application : userApplications) {
            if (!application.isArchived()) {
                count++;
            }
        }
        logger.log(Level.INFO, "Counted active applications. Count: " + count);
        return count;
    }

    /**
     * Returns the number of archived applications.
     *
     * @param userApplications The full list of applications.
     * @return The number of archived applications.
     */
    public static int getArchivedApplicationCount(ArrayList<Application> userApplications) {
        int count = 0;
        for (Application application : userApplications) {
            if (application.isArchived()) {
                count++;
            }
        }
        logger.log(Level.INFO, "Counted archived applications. Count: " + count);
        return count;
    }

    /**
     * Checks whether there is at least one archived application.
     *
     * @param userApplications The full list of applications.
     * @return True if there is at least one archived application, otherwise false.
     */
    public static boolean hasArchivedApplications(ArrayList<Application> userApplications) {
        for (Application application : userApplications) {
            if (application.isArchived()) {
                logger.log(Level.INFO, "Archived applications exist in the list.");
                return true;
            }
        }
        logger.log(Level.INFO, "No archived applications found in the list.");
        return false;
    }

    /**
     * Sorts applications based on the given criteria.
     *
     * <p>The first element in the criteria array specifies the field to sort by
     * (e.g., "ROLE", "STATUS", "COMPANY", "CONTACT", "DEADLINE").</p>
     *
     * <p>Optional flags:</p>
     * <ul>
     *     <li>"DESC" - sorts in descending order</li>
     *     <li>"NONNULL" - excludes applications with null values for the chosen field</li>
     * </ul>
     *
     * @param userApplications The list of applications to sort.
     * @param criteria         An array specifying the sorting field and optional flags.
     * @return A new list of sorted applications.
     * @throws InternTrackException If the sorting criteria is invalid.
     */
    public static ArrayList<Application> sortApplicationsByCriteria(ArrayList<Application> userApplications,
                                                                    String[] criteria) throws InternTrackException {
        assert criteria.length > 0 : "There must be some sorting criteria";
        assert criteria.length < 4 : "There are at most 3 criteria";
        // Detect the flag
        boolean isDesc = false;
        boolean isNonnull = false;
        for (int i = 1; i < criteria.length; i++) {
            assert criteria[i].equals("DESC") || criteria[i].equals("NONNULL")
                    : "Unknown flag: " + criteria[i];
            if (criteria[i].equals("DESC")) {
                isDesc = true;
            } else {
                isNonnull = true;
            }
        }

        // Final flag use
        final boolean finalIsDesc = isDesc;
        final boolean finalIsNonnull = isNonnull;
        ArrayList<Application> sortedApplicationList = getActiveApplications(userApplications);
        if (criteria[0].equals("ROLE")) {
            sortedApplicationList.sort((a, b) -> {
                int condition = a.getRole().compareToIgnoreCase(b.getRole());
                return (finalIsDesc) ? -condition : condition;
            });
            return sortedApplicationList;
        } else if (criteria[0].equals("STATUS")) {
            sortedApplicationList.sort((a, b) -> {
                int condition = a.getStatus().compareToIgnoreCase(b.getStatus());
                return (finalIsDesc) ? -condition : condition;
            });
            return sortedApplicationList;
        } else if (criteria[0].equals("COMPANY")) {
            sortedApplicationList.sort((a, b) -> {
                int condition = a.getCompany().compareToIgnoreCase(b.getCompany());
                return (finalIsDesc) ? -condition : condition;
            });
            return sortedApplicationList;
        } else if (criteria[0].equals("CONTACT")) {
            sortedApplicationList.sort((a, b) -> {
                // Check the contact criteria
                String contactA = a.getContact();
                String contactB = b.getContact();
                if (contactA == null && contactB == null) {
                    return 0;
                } else if (contactA == null) {
                    return 1;
                } else if (contactB == null) {
                    return -1;
                }

                int condition = contactA.compareToIgnoreCase(contactB);
                return (finalIsDesc) ? -condition : condition;
            });

            if (!finalIsNonnull) {
                return sortedApplicationList;
            }

            ArrayList<Application> filteredApplicationList = new ArrayList<>();
            for (Application app : sortedApplicationList) {
                if (app.getContact() != null) {
                    filteredApplicationList.add(app);
                }
            }
            return filteredApplicationList;
        } else if (criteria[0].equals("DEADLINE")) {
            sortedApplicationList.sort((a, b) -> {
                // Check the deadline criteria
                LocalDate deadlineA = a.getDeadline();
                LocalDate deadlineB = b.getDeadline();
                if (deadlineA == null && deadlineB == null) {
                    return 0;
                } else if (deadlineA == null) {
                    return 1;
                } else if (deadlineB == null) {
                    return -1;
                }
                // Check if descending
                int condition;
                if (deadlineA.isEqual(deadlineB)) {
                    return 0;
                }
                if (deadlineA.isBefore(deadlineB)) {
                    condition = -1;
                } else {
                    condition = 1;
                }
                return (finalIsDesc) ? -condition : condition;
            });
            if (!finalIsNonnull) {
                return sortedApplicationList;
            }
            ArrayList<Application> filteredApplicationList = new ArrayList<>();
            for (Application app : sortedApplicationList) {
                if (app.getDeadline() != null) {
                    filteredApplicationList.add(app);
                }
            }
            return filteredApplicationList;
        }
        throw new InternTrackException("Wrong sorting criteria, please try again");
    }

    /**
     * Retrieves the string value of a specified text field from an application.
     *
     * @param application The application to retrieve the value from.
     * @param field       The field whose value is to be retrieved.
     * @return The string value of the specified field, or null if not applicable.
     */
    private static String getTextFieldValue(Application application, FilterCriteria.Field field) {
        switch (field) {
        case COMPANY:
            return application.getCompany();
        case ROLE:
            return application.getRole();
        case CONTACT:
            return application.getContact();
        case STATUS:
            return application.getStatus();
        default:
            return null;
        }
    }

    /**
     * Checks whether an application's text field contains the filter input, ignoring case.
     *
     * @param applicationValue The text field value from the application.
     * @param criterionValue   The text entered by the user for filtering.
     * @return True if the application value contains the criterion value, ignoring case.
     */
    private static boolean matchesTextCriterion(String applicationValue, String criterionValue) {
        if (applicationValue == null || criterionValue == null) {
            return false;
        }

        return applicationValue.toLowerCase(Locale.ROOT).contains(criterionValue.toLowerCase(Locale.ROOT));
    }

    /**
     * Returns true if an application is active and its deadline falls within the specified range.
     * Used by the Remind feature to determine which applications to display.
     *
     * @param app The application to check.
     * @param start The start date (inclusive) of the range.
     * @param end The end date (inclusive) of the range.
     * @return True if the application is not archived and the deadline is within the range.
     */
    private static boolean isWithinDateRange(Application app, LocalDate start, LocalDate end) {
        LocalDate deadline = app.getDeadline();
        if (app.isArchived() || deadline == null) {
            return false;
        }
        return !deadline.isBefore(start) && !deadline.isAfter(end);
    }

    /**
     * Validates that the given 1-based index is within range.
     *
     * @param userApplications The application list.
     * @param index            The 1-based index to validate.
     * @throws InternTrackException If the index is out of range.
     */
    private static void validateIndex(ArrayList<Application> userApplications, int index)
            throws InternTrackException {
        if (index < 1 || index > userApplications.size()) {
            logger.warning("Application index out of range: " + index);
            throw new InternTrackException("Application index is out of range.");
        }
    }

    /**
     * Validates that the given 1-based index is within range for a displayed application list.
     *
     * @param displayedApplications The visible application list being indexed.
     * @param index                 The 1-based index to validate.
     * @param emptyListMessage      The message to show when the visible list is empty.
     * @param outOfRangeMessage     The message to show when the index is out of range.
     * @throws InternTrackException If the list is empty or the index is out of range.
     */
    private static void validateDisplayedIndex(ArrayList<Application> displayedApplications, int index,
                                               String emptyListMessage, String outOfRangeMessage)
            throws InternTrackException {
        if (displayedApplications.isEmpty()) {
            logger.warning(emptyListMessage);
            throw new InternTrackException(emptyListMessage);
        }

        if (index < 1 || index > displayedApplications.size()) {
            logger.warning("Displayed application index out of range: " + index
                    + ", visible list size: " + displayedApplications.size());
            throw new InternTrackException(outOfRangeMessage);
        }
    }

    /**
     * Retrieves an application by its 1-based index after validating it.
     *
     * @param userApplications The application list.
     * @param index            The 1-based index of the application.
     * @return The application at the given index.
     * @throws InternTrackException If the index is invalid.
     */
    private static Application getApplicationByIndex(ArrayList<Application> userApplications, int index)
            throws InternTrackException {
        validateIndex(userApplications, index);
        Application application = userApplications.get(index - 1);
        assert application != null : "Application should not be null after index validation";
        return application;
    }

    /**
     * Retrieves an active application by its 1-based displayed index from the active applications list.
     *
     * @param userApplications The full application list.
     * @param index            The 1-based index from the active applications view.
     * @return The active application at the given displayed index.
     * @throws InternTrackException If there are no active applications or the index is invalid.
     */
    private static Application getActiveApplicationByIndex(ArrayList<Application> userApplications, int index)
            throws InternTrackException {
        ArrayList<Application> activeApplications = getActiveApplications(userApplications);
        validateDisplayedIndex(activeApplications, index,
                "There are no active applications to select. Use add to create an application, "
                        + "or list to view active applications.",
                "Active application index is out of range. Use list to view the available "
                        + "active application indices.");
        Application application = activeApplications.get(index - 1);
        assert application != null : "Active application should not be null after index validation";
        return application;
    }

    /**
     * Retrieves an archived application by its 1-based displayed index from the archived applications list.
     *
     * @param userApplications The full application list.
     * @param index            The 1-based index from the archived applications view.
     * @return The archived application at the given displayed index.
     * @throws InternTrackException If there are no archived applications or the index is invalid.
     */
    private static Application getArchivedApplicationByIndex(ArrayList<Application> userApplications, int index)
            throws InternTrackException {
        ArrayList<Application> archivedApplications = getArchivedApplications(userApplications);
        validateDisplayedIndex(archivedApplications, index,
                "There are no archived applications to select.",
                "Archived application index is out of range. Use listarchived to view archived applications.");
        Application application = archivedApplications.get(index - 1);
        assert application != null : "Archived application should not be null after index validation";
        return application;
    }

    /**
     * Ensures that an application is not archived before performing an operation.
     *
     * @param application The application to check.
     * @throws InternTrackException If the application is already archived.
     */
    private static void ensureNotArchived(Application application) throws InternTrackException {
        if (application.isArchived()) {
            logger.warning("Operation failed because the selected application is already archived.");
            throw new InternTrackException(
                    "The selected application is already archived. Use listarchived to view archived applications.");
        }
    }

    /**
     * Ensures that an application is archived before performing an operation.
     *
     * @param application The application to check.
     * @throws InternTrackException If the application is not archived.
     */
    private static void ensureArchived(Application application) throws InternTrackException {
        if (!application.isArchived()) {
            logger.warning("Operation failed because the selected application is not archived.");
            throw new InternTrackException(
                    "The selected application is not archived. Use list to view active applications.");
        }
    }
}
