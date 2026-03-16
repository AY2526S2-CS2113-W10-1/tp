package seedu.interntrack;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Manages the list of internship applications.
 */
public class ApplicationList {
    private static final Logger logger = Logger.getLogger("ApplicationList");

    /**
     * Adds a new application parsed from the given input line to the list.
     *
     * @param userApplications The list to add the application to.
     * @param line             The raw input string containing application details.
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
     * @param index The 1-based index of the application to edit.
     * @param status The new status value.
     * @return The updated Application object.
     * @throws InternTrackException If the index is invalid.
     */
    public static Application editApplicationStatus(ArrayList<Application> userApplications,
                                                    int index, String status) throws InternTrackException {
        if (index < 1 || index > userApplications.size()) {
            throw new InternTrackException("Application index is out of range.");
        }

        Application application = userApplications.get(index - 1);
        application.setStatus(status);
        return application;
    }

    /**
     * Filters applications by status.
     *
     * @param userApplications The list to filter.
     * @param status The status to match.
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
}
