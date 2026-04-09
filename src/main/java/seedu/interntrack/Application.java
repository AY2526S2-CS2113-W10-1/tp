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
    private boolean isArchived;

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
        this.isArchived = false;
        // Assertion to verify default status postcondition
        assert this.status.equals(DEFAULT_STATUS) : "New applications must start with Pending status";
    }

    /**
     * Initialises a copy of the specified application.
     *
     * @param other The application to copy.
     */
    public Application(Application other) {
        this.company = other.company;
        this.role = other.role;
        this.deadline = other.deadline;
        this.contact = other.contact;
        this.status = other.status;
        this.isArchived = other.isArchived;
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
     * Returns whether this application is archived.
     *
     * @return true if archived, false otherwise.
     */
    public boolean isArchived() {
        return isArchived;
    }

    /**
     * Sets the company name of this application.
     *
     * @param company The updated company name.
     */
    public void setCompany(String company) {
        if (company == null || company.trim().isEmpty()) {
            throw new IllegalArgumentException("Company cannot be null or empty.");
        }

        this.company = company.trim();
        assert !this.company.isBlank() : "Application company should not be blank after setting";
    }

    /**
     * Sets the role name of this application.
     *
     * @param role The updated role name.
     */
    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty.");
        }

        this.role = role.trim();
        assert !this.role.isBlank() : "Application role should not be blank after setting";
    }

    /**
     * Sets the deadline of this application.
     *
     * @param deadline The updated deadline, or null if absent.
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    /**
     * Sets the contact details of this application.
     *
     * @param contact The updated contact, or null if absent.
     */
    public void setContact(String contact) {
        if (contact != null && contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact cannot be blank.");
        }

        this.contact = (contact == null) ? null : contact.trim();
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

    /**
     * Sets whether this application is archived.
     * Archived applications remain in storage but are excluded from active views.
     *
     * @param isArchived The new archived state.
     */
    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    @Override
    public String toString() {
        String archiveLabel = isArchived ? "[Archived] " : "";
        String tempDeadline = (deadline != null) ? "Deadline: " + deadline + ", " : "";
        String tempContact = (contact != null) ? "Contact: " + contact + ", " : "";
        return archiveLabel + company + " - " + role
                + " (" + tempDeadline + tempContact + "Status: " + status + ")";
    }
}
