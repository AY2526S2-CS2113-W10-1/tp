package seedu.interntrack;

import java.time.LocalDate;

/**
 * Represents an application with company, role, deadline, contact and status.
 */
public class Application {
    private static final String DEFAULT_STATUS = "Pending";

    private String company;
    private String role;
    private LocalDate deadline;
    private String contact;
    private String status;

    /**
     * Initialises a new application with the specified details.
     * The status is set to "Pending" by default.
     *
     * @param company  Name of the company.
     * @param role     Job role being applied for.
     * @param deadline Date of the application deadline.
     * @param contact  Contact details provided.
     */
    public Application(String company, String role, LocalDate deadline, String contact) {
        this.company = company;
        this.role = role;
        this.deadline = deadline;
        this.contact = contact;
        this.status = DEFAULT_STATUS;
        // Assertion to verify default status postcondition
        assert this.status.equals("Pending") : "New applications must start with Pending status";
    }

    /**
     * Returns the company name of this application.
     *
     * @return The company name.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Returns the job role of this application.
     *
     * @return The job role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Returns the deadline of this application.
     *
     * @return The deadline as a LocalDate, or null if not set.
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Returns the contact details of this application.
     *
     * @return The contact string, or null if not set.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Returns the current status of this application.
     *
     * @return The status string.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of this application.
     *
     * @param status The new status value.
     */
    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }

        this.status = status.trim();
        assert !this.status.isBlank() : "Application status should not be blank after setting";
    }

    @Override
    public String toString() {
        String tempDeadline = (deadline != null) ? "Deadline: " + deadline + ", " : "";
        String tempContact = (contact != null) ? "Contact: " + contact + ", " : "";
        return company + " - " + role
                + " (" + tempDeadline + tempContact + "Status: " + status + ")";
    }
}
