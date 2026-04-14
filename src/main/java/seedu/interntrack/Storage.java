package seedu.interntrack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the loading and saving of applications to a local text file.
 * Converts application objects to a persistent string format and vice versa.
 */
public class Storage {
    private static final String FILE_PATH = "./data/applications.txt";
    private static final String FILE_DELIMITER = "|";
    private static final String FILE_DELIMITER_REGEX = "\\|";
    private static final String NULL_STRING = "null";
    private static final Logger logger = Logger.getLogger("Storage");
    private static final int COMPANY_INDEX = 0;
    private static final int ROLE_INDEX = 1;
    private static final int DEADLINE_INDEX = 2;
    private static final int CONTACT_INDEX = 3;
    private static final int STATUS_INDEX = 4;
    private static final int ARCHIVED_INDEX = 5;

    /**
     * Loads applications from the local file into the provided list.
     *
     * @param userApplications The list where loaded applications are stored.
     */
    public static void loadApplications(ArrayList<Application> userApplications) {
        try {
            File f = new File(FILE_PATH);

            if (f.getParentFile() != null && !f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
                logger.log(Level.INFO, "Created data directory: " + f.getParentFile().getAbsolutePath());
            }

            if (!f.exists()) {
                f.createNewFile();
                logger.log(Level.INFO, "Created new applications file: " + FILE_PATH);
                return;
            }

            try (Scanner s = new Scanner(f)) {
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    Application application = parseFileString(line);
                    if (application != null) {
                        userApplications.add(application);
                    }
                }
            }

        } catch (IOException e) {
            Ui.printStorageWarning();
            logger.log(Level.SEVERE, "IO error while loading applications: " + e.getMessage());
        }
    }

    /**
     * Parses a single line from the data file into an Application object.
     *
     * @param line The string representing an application.
     * @return The reconstructed Application object, or null if the line is invalid.
     */
    private static Application parseFileString(String line) {
        assert line != null : "Stored application line should not be null";

        String[] parts = line.split(FILE_DELIMITER_REGEX, -1);

        if (parts.length < 5) {
            logger.log(Level.WARNING, "Skipping malformed application record: " + line);
            return null;
        }

        try {
            String company = parts[COMPANY_INDEX];
            String role = parts[ROLE_INDEX];

            LocalDate deadline = null;
            if (!parts[DEADLINE_INDEX].equals(NULL_STRING) && !parts[DEADLINE_INDEX].isEmpty()) {
                deadline = LocalDate.parse(parts[DEADLINE_INDEX]);
            }

            String contact = (parts[CONTACT_INDEX].equals(NULL_STRING)) ? null : parts[CONTACT_INDEX];
            String status = parts[STATUS_INDEX];
            boolean isArchived = false;
            if (parts.length > 5) {
                isArchived = Boolean.parseBoolean(parts[ARCHIVED_INDEX]);
            }

            Application app = new Application(company, role, deadline, contact);
            app.setStatus(status);
            app.setArchived(isArchived);

            assert app.getStatus() != null && !app.getStatus().isBlank()
                    : "Loaded application status should not be blank";

            return app;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Skipping invalid application record: " + line, e);
            return null;
        }
    }

    /**
     * Saves the current list of applications to the local file.
     *
     * @param userApplications The list of applications to be saved.
     */
    public static void saveApplications(ArrayList<Application> userApplications) {
        try (FileWriter fw = new FileWriter(FILE_PATH)) {
            StringBuilder sb = new StringBuilder();

            // Convert all applications to string format
            for (Application application : userApplications) {
                sb.append(applicationToFileFormat(application)).append(System.lineSeparator());
            }

            fw.write(sb.toString());
            logger.log(Level.INFO, "Successfully saved " + userApplications.size() + " applications to file");

        } catch (IOException e) {
            Ui.printSaveFailureWarning();
            logger.log(Level.SEVERE, "IO error while saving applications: " + e.getMessage());
        }
    }

    /**
     * Formats an Application object into a string for file storage.
     *
     * @param application The application to format.
     * @return The formatted string.
     */
    private static String applicationToFileFormat(Application application) {
        return application.getCompany() + FILE_DELIMITER + application.getRole() + FILE_DELIMITER
                + application.getDeadline() + FILE_DELIMITER + application.getContact()
                + FILE_DELIMITER + application.getStatus() + FILE_DELIMITER + application.isArchived();
    }
}
