package seedu.interntrack;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the list of internship applications.
 */
public class ApplicationList {
    private static final Logger logger = Logger.getLogger("ApplicationList");

    /**
     * Adds a new application parsed from the given input line to the list.
     *
     * @param userApplications The list to add the application to.
     * @param line The raw input string containing application details.
     * @return The newly created Application object.
     * @throws InternTrackException If the input is missing required fields or has an invalid date.
     */
    public static Application addApplications(ArrayList<Application> userApplications,
                                              String line) throws InternTrackException {
        Application newApplication = Parser.createApplication(line);
        assert newApplication.getCompany() != null && !newApplication.getCompany().isEmpty() :
                "Application company should be valid after creation";
        assert newApplication.getRole() != null && !newApplication.getRole().isEmpty() :
                "Application role should be valid after creation";
        userApplications.add(newApplication);
        logger.log(Level.INFO, "Added new application to list. Total applications: " + userApplications.size());
        return newApplication;
    }

    /**
     * Edits the status of an existing application.
     *
     * @param userApplications The list containing the application.
     * @param index            The 1-based index of the application to edit.
     * @param status           The new status value.
     * @return The updated Application object.
     * @throws InternTrackException If the index is invalid.
     */
    public static Application editApplicationStatus(ArrayList<Application> userApplications,
                                                    int index, String status) throws InternTrackException {
        if (index < 1 || index > userApplications.size()) {
            logger.warning("Edit failed: application index out of range: " + index);
            throw new InternTrackException("Application index is out of range.");
        }

        if (status == null || status.trim().isEmpty()) {
            logger.warning("Edit failed: status cannot be null or empty");
            throw new InternTrackException("Status cannot be empty.");
        }

        Application application = userApplications.get(index - 1);
        assert application != null : "Application to edit should not be null";

        String oldStatus = application.getStatus();
        logger.info("Updating application at index " + index
                + " from status '" + oldStatus + "' to '" + status + "'");

        application.setStatus(status);

        assert application.getStatus().equals(status) : "Application status should be updated correctly";
        return application;
    }

    /**
     * Filters applications by status.
     *
     * @param userApplications The list to filter.
     * @param status           The status to match.
     * @return A list of applications that match the status.
     */
    public static ArrayList<Application> filterApplicationsByStatus(ArrayList<Application> userApplications,
                                                                    String status) {
        ArrayList<Application> filteredApplications = new ArrayList<>();
        for (Application application : userApplications) {
            String applicationStatus = application.getStatus();
            assert applicationStatus != null : "Existing application must have status";
            if (applicationStatus.equalsIgnoreCase(status)) {
                filteredApplications.add(application);
            }
        }
        logger.log(Level.INFO, "Filtered applications by status=" + status
                + ". Matches: " + filteredApplications.size());
        return filteredApplications;
    }

    /**
     * Filters applications that have a deadline on or before the specified date.
     * Iterates through the provided list and collects applications where the
     * deadline is not after the given date.
     *
     * @param userApplications The list of applications to filter.
     * @param deadline The cutoff date for the filter.
     * @return A list of applications matching the date criteria.
     */
    public static ArrayList<Application> filterApplicationsOnOrBefore(ArrayList<Application> userApplications,
                                                                      LocalDate deadline) {
        ArrayList<Application> filteredApplications = new ArrayList<>();
        for (Application application : userApplications) {
            LocalDate applicationDeadline = application.getDeadline();
            if (applicationDeadline != null && !applicationDeadline.isAfter(deadline)) {
                filteredApplications.add(application);
            }
        }
        logger.log(Level.INFO, "Filtered applications on or before deadline=" + deadline
                + ". Matches: " + filteredApplications.size());
        return filteredApplications;
    }


    /**
     * Sort applications by criteria.
     *
     * @param userApplications The list to filter.
     * @param criteria         status to match.
     * @return A list of applications that has been sorted.
     *
     */
    public static ArrayList<Application> sortApplicationsByCriteria(ArrayList<Application> userApplications,
                                                                    String[] criteria) throws InternTrackException {
        assert criteria.length > 0 : "There must be some sorting criteria";
        assert criteria.length < 4 : "There are at most 3 criteria";
        boolean isDesc = false;
        boolean isNonnull = false;
        for (int i = 1; i < criteria.length; i++) {
            assert criteria[i].equals("DESC") || criteria[i].equals("NONNULL") : "Unknown flag: " + criteria[i];
            if (criteria[i].equals("DESC")) {
                isDesc = true;
            } else {
                isNonnull = true;
            }
        }
        final boolean finalIsDesc = isDesc;
        final boolean finalIsNonnull = isNonnull;

        ArrayList<Application> sortedApplicationList = new ArrayList<>(userApplications);
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
                String contactA = a.getContact();
                String contactB = b.getContact();
                if (contactA == null && contactB == null) {
                    return 0;
                } else if (contactA == null) { // a null so a should at the back
                    return 1;
                } else if (contactB == null) {// b null so b should at the back
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
                LocalDate deadlineA = a.getDeadline();
                LocalDate deadlineB = b.getDeadline();
                if (deadlineA == null && deadlineB == null) {
                    return 0;
                } else if (deadlineA == null) { // a null so a should at the back
                    return 1;
                } else if (deadlineB == null) {// b null so b should at the back
                    return -1;
                }
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
}
