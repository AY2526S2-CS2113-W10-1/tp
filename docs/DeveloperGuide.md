# Developer Guide

## Table of Contents

1. [Acknowledgements](#acknowledgements)
2. [Setting up, getting started](#setting-up-getting-started)
3. [Design](#design)
   - [Architecture](#architecture)
   - [Ui component](#ui-component)
   - [Storage component](#storage-component)
   - [Parser component](#parser-component)
   - [Application component](#application-component)
   - [Application List component](#application-list-component)
   - [FilterCriteria component](#filtercriteria-component)
   - [EditDetails component](#editdetails-component)
4. [Implementation](#implementation)
   - [Add feature](#add-feature)
   - [List feature](#list-feature)
   - [Edit feature](#edit-feature)
   - [Filter feature](#filter-feature)
   - [Remind feature](#remind-feature)
   - [Delete feature](#delete-feature)
   - [Sort feature](#sort-feature)
   - [Undo feature](#undo-feature)
   - [Summary feature](#summary-feature)
5. [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)
6. [Appendix: Requirements](#appendix-requirements)
   - [Product scope](#product-scope)
   - [User stories](#user-stories)
   - [Use cases](#use-cases)
   - [Non-Functional Requirements](#non-functional-requirements)
   - [Glossary](#glossary)
7. [Appendix: Instructions for manual testing](#appendix-instructions-for-manual-testing)
   - [Launch and shutdown](#launch-and-shutdown)
   - [Deleting an application](#deleting-an-application)
   - [Saving data](#saving-data)

---

## Acknowledgements

This project was developed as part of the CS2113 Team Project at the National University of Singapore.

The project structure and development workflow follow the guidelines from the SE-EDU project template:
https://se-education.org/

The project focuses on the learning of Object-Oriented Programming (OOP) principles and good coding practices for
collaborative software development in a team environment.

The project was developed using Java and standard software engineering tools such as Git for version control and GitHub
for project management and collaboration.

Java standard libraries such as `java.util`, `java.io`, and `java.time` were used in the implementation.

---

## Setting up, getting started

{Provide step-by-step instructions for setting up the development environment and getting started with the project.}

---

## Design

### Architecture

![architecture\_diag.png](diagrams/architecture_diag.png)

The ***Architecture Diagram*** given above explains the high-level design of the App. The architecture of **InternTrack
** follows a layered design pattern with the following main components:

* InternTrack: Responsible for the app's lifecycle. It initializes components in the correct sequence at launch and
  ensures a clean shutdown by invoking necessary cleanup methods. It also orchestrates the application flow.
* UI (Ui): Handles user input and output via the command-line interface
* Parser: Processes user commands and handle back to InternTrack
* Model (Application): Maintains the in-memory data structure for applications
* Storage (Storage): Manages persistence of application data to disk
* Common : A suite of utility classes (e.g.,ApplicationList, EditDetails, FilterCriteria) shared across all components.
  The sequence of interaction follows a clear flow: User input → UI → Logic (Parser) → Model manipulation → Storage
  persistence.

## Application List component

![application\_list.png](diagrams/application_list_diag.png)

The ApplicationList component is a stateless utility class that functions as a logic middleware. 

It does not maintain its own state or store the application data internally; instead, it provides a suite of pure functions that perform operations (adding, filtering, sorting, editing) on an ArrayList<Application> passed in by the caller. 

This decoupling ensures that the data storage remains independent of the processing logic.

---

### Ui component

{Description of Ui component will be added here.}

---

### Storage component

**API**: [`Storage.java`](https://github.com/AY2526S2-CS2113-W10-1/tp/blob/master/src/main/java/seedu/interntrack/Storage.java)

#### Class Diagram

The following diagram illustrates the `Storage` component and its relationships:

![storage_class_diag.png](diagrams/storage_class_diag.png)

#### What the Storage Component Does

The `Storage` component handles persistence of application data through the following responsibilities:

- **Loads** application data from `./data/applications.txt` on application startup and deserializes each line into an `Application` object.
- **Saves** the full list of `Application` objects back to disk after every modifying command (add, edit, delete).
- **Depends on** the `Application` class.  It directly serializes and deserializes `Application` objects using a pipe-delimited format.
- **Handles gracefully** missing files and directories by creating them automatically on first run.
- **Skips and logs** malformed or unparseable records rather than crashing, ensuring robustness against corrupted data.
- **Uses** Java's `FileWriter` and `Scanner` classes for I/O operations and `LocalDate` for date parsing.

---

#### File Format Specification

The data file (`./data/applications.txt`) stores applications in a pipe-delimited format. Each line represents one application with exactly 5 fields:

| Field      | Index | Format              | Example                | Notes                        |
|------------|-------|---------------------|------------------------|------------------------------|
| Company    | 0     | Plain text          | `Google`               | Mandatory, non-empty         |
| Role       | 1     | Plain text          | `SWE Intern`           | Mandatory, non-empty         |
| Deadline   | 2     | ISO-8601 date       | `2025-08-01`           | Optional; `null` if not set  |
| Contact    | 3     | Plain text (email)  | `john.doe@google.com`  | Optional; `null` if not set  |
| Status     | 4     | Predefined enum     | `Applied`              | Mandatory; default is `Pending` |

**Example line:**
```
Google|SWE Intern|2025-08-01|john.doe@google.com|Applied
```

**Example with null fields:**
```
Microsoft|Azure Developer|null|contact.unknown@microsoft.com|Pending
```

---

#### Save Operation Sequence

**Key steps:**
1. User enters an add command
2. `InternTrack` creates a new `Application` and adds it to the list
3. `Storage.saveApplications()` is called immediately
4. All applications are converted to pipe-delimited format via `applicationToFileFormat()`
5. The entire list is written to `./data/applications.txt` in a single file operation

---

#### Load Operation Sequence

**Key steps:**
1. `InternTrack.main()` initializes an empty application list
2. `Storage.loadApplications()` is called
3. If the file/directory doesn't exist, they are created automatically
4. Each line is read and parsed via `parseFileString()`
5. Valid applications are added to the in-memory list
6. Malformed lines are logged and skipped

---

#### Error Handling and Corrupted Data

The `Storage` component is designed to be resilient:

| Scenario | Behavior |
|----------|----------|
| Data directory missing | Created automatically at `./data/` |
| Applications file missing | Created automatically as empty file |
| Line has wrong number of fields | Line is skipped; warning logged to console |
| Deadline field is unparseable | Line is skipped; exception logged; application not added |
| Status field is invalid/blank | Line is skipped; validation ensures consistency |
| Entire file is corrupted | Application starts with empty list; user can add data fresh |

This design prioritizes **data integrity** and **user safety** over strict parsing — the application will never crash due to a corrupted data file, but will alert the user to problems via logging.

---

#### Design Considerations

##### Aspect 1: When to Persist Data to Storage

**Alternative 1 (Current Choice):** Auto-save to `Storage` immediately after every successful command that modifies the model (add, edit, delete).

*Pros:*
- Prevents data loss if the application crashes or is forcefully terminated
- Guarantees consistency between the in-memory model and on-disk state
- Simplifies error handling: if the save fails, the entire operation can be considered incomplete and rolled back
- Users never lose work; changes are persisted immediately

*Cons:*
- Increased disk I/O operations may cause slight performance overhead
- Inefficient for batch operations (multiple adds followed by a save would write to disk multiple times)
- Disk write latency could delay user feedback

**Alternative 2:** Only save to `Storage` when the user issues an explicit `save` command or when the application exits normally.

*Pros:*
- Better performance: disk writes are minimized and can be batched
- More predictable timing—saves only happen at user-defined points
- Aligns with traditional desktop application workflows (e.g., spreadsheets require explicit saves)

*Cons:*
- **High risk of data loss** if the application is force-closed without an explicit save
- User must remember to save, placing responsibility on the user
- No guarantee of data consistency throughout a session
- Less suitable for tasks that users perform casually or repetitively

**Rationale for Current Choice:** For an internship application tracker, data loss is unacceptable. Immediate persistence ensures that every application a user enters is permanently saved. While this incurs a small performance cost, the safety guarantee is worth the trade-off given the application's domain.

---

##### Aspect 2: File Format for Persistent Storage

**Alternative 1 (Current Choice):** Store applications in a plain-text file using a pipe-delimiter (`|`) format.

*Pros:*
- Human-readable and easily debuggable—users can directly examine and understand the file contents
- No external library dependencies required
- Simple parsing and serialization logic
- Fast I/O performance for small to medium datasets
- Minimal file size overhead compared to structured formats

*Cons:*
- Not scalable if nested or complex data structures are added later
- If the delimiter character (`|`) appears in data fields, it must be escaped, complicating both serialization and deserialization
- Limited support for special characters; encoding issues may arise
- Fragile: manual edits to the file can easily corrupt the data format

**Alternative 2:** Store applications in JSON format.

*Pros:*
- Structured, widely supported format with standardized specifications
- Self-documenting: field names are included in the serialized data
- Easy to extend with new fields without breaking existing parsers
- Better handling of special characters, escape sequences, and nested objects
- Widely available libraries simplify parsing and serialization

*Cons:*
- Requires an external JSON library dependency (e.g., `gson`, `jackson`)
- Slightly slower parsing and serialization compared to plain text
- Larger file size due to formatting overhead and field name repetition
- Overkill for a simple, flat data structure like applications

**Alternative 3:** Use a database.

*Pros:*
- Supports complex queries, indexing, and relationships between entities
- Built-in data validation and type constraints
- Superior performance for large datasets (thousands of records)
- Allows for concurrent access and advanced features like transactions

*Cons:*
- Adds significant complexity and database library dependencies
- Overkill for a CLI application managing a small number of applications
- Harder to understand and debug compared to file-based approaches
- Requires knowledge of SQL and database administration
- Heavier resource footprint

**Rationale for Current Choice:** The plain-text pipe-delimited format is appropriate for an early-stage student project. It provides a good balance between simplicity, readability, and performance. If future requirements demand support for complex nested data or significantly larger datasets, migrating to JSON or a database would be straightforward.

---

### Parser component

#### Overview

The `Parser` component is responsible for interpreting user input and converting it into actionable commands. It validates user input and constructs domain objects (like `Application` instances) that represent the user's intentions.

#### Design Considerations

##### Aspect 1: Where the Application Object is Instantiated

**Alternative 1 (Current Choice):** Instantiate the complete `Application` object inside the `Parser.createApplication()` method, which is called by `ApplicationList.addApplications()`.

*Pros:*
- Parsing logic is centralized and reusable across commands
- `ApplicationList` is insulated from parsing concerns; it only knows about domain objects
- Clear separation of concerns between input parsing and model management
- Validation happens at a single point, reducing the risk of inconsistency

*Cons:*
- Parser is tightly coupled to the `Application` class structure
- Changes to the `Application` constructor signature require updating the parser
- If multiple ways to create `Application` objects are needed, code duplication may occur

**Alternative 2:** Pass raw, validated strings directly to `ApplicationList.addApplications()` and allow it to instantiate the `Application` object during the add operation.

*Pros:*
- Reduces coupling between the parser and the `Application` model
- `ApplicationList` has more control and flexibility over object instantiation
- Easier to support alternative `Application` creation paths

*Cons:*
- Violates the Single Responsibility Principle by mixing input parsing with model logic
- Duplicates validation logic if applications are created in multiple places
- Makes testing harder because the model layer must now understand input syntax
- Reduced code reusability across commands that need to create applications

**Rationale for Current Choice:** Centralizing instantiation in the parser improves testability and maintainability. Each component has a clear responsibility: the parser handles user input syntax, and the model layer handles data integrity.

---

### Application component

{Description of Application component will be added here.}

---

### Application List component

{Description of Application List component will be added here.}

---

### FilterCriteria component

{Description of FilterCriteria component will be added here.}

---

### EditDetails component

{Description of EditDetails component will be added here.}

---

## Implementation


## Application Initialization: Loading Persisted Data

Before any user interaction occurs, the application must load previously saved data from disk. This initialization step
is critical for demonstrating how the storage mechanism works bidirectionally (save and load).

When `InternTrack.main()` is invoked at startup:

1. An empty `userApplications` ArrayList is created in memory
2. Immediately, `Storage.loadApplications(userApplications)` is called
3. This method checks if the data directory (`./data/`) and file (`./data/applications.txt`) exist
4. If either is missing, they are created automatically (graceful initialization)
5. The file is read line by line; each line is passed to `Storage.parseFileString()`
6. `parseFileString()` deserializes the pipe-delimited format back into `Application` objects
7. Each deserialized `Application` is added to the in-memory `userApplications` list

By the time the user sees the welcome prompt, all previously saved applications are already in memory. This design
ensures:

* No data loss — All previous applications are restored at startup
* Consistency — The in-memory state matches the on-disk state at launch
* Error resilience — Malformed lines are logged and skipped rather than crashing the app

---

### Add feature

#### Implementation

The add command follows a 5-step pipeline:

1. Parsing — User input is tokenized and validated
2. Object Creation — A new `Application` entity is instantiated with default status
3. Model Update — The application is added to the in-memory list
4. Storage Persistence — The updated model is serialized to disk
5. User Feedback — Confirmation is displayed to the user

---

#### Detailed Walkthrough of Add Command

##### Step 1: Parsing User Input

When a user enters `add c/Google r/Software Engineer d/2024-03-31 ct/John Doe`,
the input is received by `Ui.readCommand()` and passed to `InternTrack.handleCommand()`.

This method inspects the command prefix and dispatches to `handleAddCommand()`.

The `Parser.createApplication()` method processes the raw input string:

* Uses a regex split pattern `(?=c/|r/|ct/|d/)` to tokenize by command prefixes
* Extracts mandatory fields: `c/` (Company) and `r/` (Role)
* Extracts optional fields: `d/` (Deadline in YYYY-MM-DD format) and `ct/` (Contact)
* Validates that mandatory fields are non-empty; if missing or empty, throws `InternTrackException`
* If a deadline is provided, parses it using `LocalDate.parse()`; invalid dates trigger an exception

---

##### Step 2: Object Creation and Default Initialization

Once parsing is successful, `Parser.createApplication()` instantiates a new `Application` object with the extracted
data.

Critically, the `Application` constructor automatically assigns a default status of "Pending" to all newly created
applications. This ensures every new application has a well-defined initial state.

---

##### Step 3: Model Update

The newly created `Application` object is returned to `ApplicationList.addApplications()`, which performs final
validation:

* Adds the application to the internal `userApplications` ArrayList
* Returns the newly added `Application`

---

##### Step 4: Storage Persistence

Immediately after the successful model update, `InternTrack.handleAddCommand()` calls
`Storage.saveApplications(userApplications)` to persist the updated list to disk.

This ensures in-memory and on-disk states remain synchronized.

The `Storage.saveApplications()` method:

1. Opens a `FileWriter` to `./data/applications.txt`
2. Iterates through all applications in the list
3. Converts each `Application` to a pipe-delimited string:
   `company|role|deadline|contact|status`
4. Writes all serialized applications to disk in a single operation
5. Null fields are represented as the string `"null"`

---

##### Step 5: User Feedback

Finally, `Ui.printAddApplication()` displays a confirmation message showing the added application details and the
updated total count.

---

##### Sequence Diagram illustrating the 5 steps above

![add\_sequence\_diag.png](diagrams/add_sequence_diag.png)

---


### List feature

The `list` command allows users to list every existing application.

Command format:

list

Implementation: `Ui.printAllApplications()` to format and display every existing applications to user.

##### Sequence Diagram: List Command

![list\_sequence\_diag.png](diagrams/list_sequence_diag.png)

---

### Edit feature

The `edit` command allows users to modify the status of an existing application.

Command format:

edit INDEX s/STATUS

Example:

edit 2 s/Accepted

Implementation steps:

1. `Parser.parseEditCommand()` extracts the index and new status.
2. The index is validated to ensure it exists in the application list.
3. `ApplicationList.editApplicationStatus()` retrieves the application.
4. The application's `setStatus()` method updates the status value.
5. `Storage.saveApplications()` persists the updated list.
6. `Ui.printEditSuccess()` displays confirmation to the user.

The `Application.setStatus()` method also performs validation to ensure that the status value is not null or empty.

This approach keeps validation within the model while command interpretation remains in the logic layer.

---

##### Sequence Diagram: Edit Command

![edit\_sequence\_diag.png](diagrams/edit_sequence_diag.png)

---

### Filter feature

{Description of Filter feature implementation will be added here.}

---

### Remind feature

####  Implementation

The `remind` command helps users stay on top of upcoming application deadlines by displaying all applications with deadlines within N days from today (default: 7 days).

The remind feature is facilitated by the following key components:

- **Parser** (`Parser#parseRemindDays()`) — Parses and validates the number of days from user input
- **ApplicationList** (`ApplicationList#filterApplicationsOnOrBefore()`) — Filters applications by deadline
- **Ui** (`Ui#printUpcomingDeadlines()`) — Displays the filtered applications
- **InternTrack** (`InternTrack#handleRemindCommand()`) — Orchestrates the remind command workflow

#### How the Remind Feature Works

Given below is an example usage scenario and how the remind feature behaves:

**Step 1:** User has 3 applications: Google (deadline Apr 3), Microsoft (deadline Apr 5), Amazon (deadline Apr 15). Today is Apr 1.

**Step 2:** User executes `remind 5` to see applications due within the next 5 days.

**Step 3:** `InternTrack#handleRemindCommand()` is invoked:
1. Calls `Parser#parseRemindDays("remind 5")` which returns `5`
2. Calculates cutoff date: `LocalDate.now().plusDays(5)` = Apr 6
3. Calls `ApplicationList#filterApplicationsOnOrBefore(userApplications, Apr 6)` to filter the list
4. For each application, checks: `if (deadline != null && !deadline.isAfter(Apr 6))` to include it
5. Returns filtered list: Google (Apr 3) and Microsoft (Apr 5); excludes Amazon (Apr 15)

**Step 4:** `Ui#printUpcomingDeadlines()` displays the result:
```
You have 2 applications due in the next 5 days (up to 2026-04-06):
1. Software Engineer at Google is Pending. Apply by 2026-04-03.
2. Data Analyst at Microsoft is Pending. Apply by 2026-04-05.
```

#### Error Handling

The `Parser#parseRemindDays()` method validates input and handles error cases:

| Error Case | Behavior |
|---|---|
| Days parameter ≤ 0 | `InternTrackException` thrown with message: "Number of days must be greater than 0." |
| Non-integer parameter | `InternTrackException` thrown with message: "Days must be a valid number. Use format: remind [DAYS]" |
| No parameter provided | Defaults to 7 days without exception |
| No applications match deadline criteria | `Ui#printUpcomingDeadlines()` displays: "No applications due in the next N days." |

#### Design Considerations

**Aspect: Default Behavior**

*Alternative 1 (current choice): Default to 7 days if no parameter provided*
- Pros: Convenient for the most common use case (weekly planning); reduces typing
- Cons: Users may not expect the default

*Alternative 2: Require explicit days parameter*
- Pros: Forces explicit intent; clearer semantics
- Cons: Less convenient; more typing required

**Rationale**: Weekly planning is the most natural checking interval for internship deadlines. The default of 7 days balances convenience with clarity.

---

**Aspect: Deadline Cutoff Inclusivity**

*Alternative 1 (current choice): Include deadlines "on or before" the cutoff date (inclusive)*
- Pros: Aligns with user expectation ("what do I need to do by then?"); safer approach
- Cons: May include applications that are already overdue

*Alternative 2: Exclude applications on the cutoff date (exclusive)*
- Pros: Avoids including the boundary date itself
- Cons: Users could miss deadline applications on the cutoff date if they misinterpret

**Rationale**: It is safer to over-remind than under-remind. Missing a deadline is worse than showing one extra application.

---

### Delete feature

{Description of Delete feature implementation will be added here.}

---

### Sort feature

The `sort` command allows users to sort the whole applications with some criteria.

Command format:

sort by/CRITERIA [FLAG]

Example:

sort by/ROLE DESC NONNULL

1. `Parser.parseSortCriteria()` extracts the criteria and flag for sorting.
2. The criteria and flags are validated to ensure it exists in the application list.
3. `ApplicationList.sortApplicationsByCriteria()` perform the sorting upon receiving the criteria and flags.
4. The returning value is a new list of applications after sorting.
5. `Ui.printSortedApplications()` displays the sorting to the user.

This approach keeps validation within the model while command interpretation remains in the logic layer.

##### Sequence Diagram: Sort Command

![sort\_sequence\_diag.png](diagrams/sort_sequence_diag.png)

---

### Undo feature

The `undo` command allows users to revert the most recent modification made to the application list.

Supported commands:

* add
* edit
* delete

Undo is implemented using a snapshot-based state restoration mechanism.

---

#### Snapshot Mechanism

Before executing any modifying command:

1. A deep copy of the current `userApplications` list is created.
2. The snapshot is pushed onto an undo history stack.

When the user executes `undo`:

1. The most recent snapshot is popped from the stack.
2. The application list is replaced with the snapshot.
3. The restored state is written to storage.

This guarantees the system returns to the exact state before the most recent change.

---

#### Example Workflow

add c/Google r/SWE Intern
delete 1
undo

Execution flow:

1. `add` stores a snapshot of the empty list then adds the application.
2. `delete` stores a snapshot then removes the application.
3. `undo` restores the previous snapshot.

The deleted application reappears in the list.

---

##### Sequence Diagram: Undo Command

![undo\_sequence\_diag.png](diagrams/undo_sequence_diag.png)

---

#### Design Considerations

##### Aspect 4: Undo Implementation

**Alternative 1 (Current Choice): Snapshot-based restoration**

*Pros:*
- Simple and reliable
- Independent of command logic
- Guarantees correct state restoration

*Cons:*
- Increased memory usage

**Alternative 2: Command-based reversal**

*Pros:*
- More memory efficient

*Cons:*
- Significantly more complex
- Each command requires custom undo logic

**Rationale for Current Choice:** The snapshot approach was chosen for simplicity and reliability. In a student project managing a relatively small number of internship applications, the minimal memory overhead of storing snapshots is acceptable, and the guarantee of correct state restoration outweighs the efficiency benefits of command-based reversal. This design ensures that any future modifications to commands don't inadvertently break undo functionality.

---

### Summary feature

{Description of Summary feature implementation will be added here.}

---

## Documentation, logging, testing, configuration, dev-ops

{Documentation, logging, testing, configuration, and dev-ops information will be added here.}

---

## Appendix: Requirements

### Product scope

#### Target user profile

InternTrack is designed for students who apply to multiple internships and need a simple way to track their
applications.

The target users are:

- university students applying for internships
- users comfortable with command-line interfaces
- applicants managing many applications simultaneously

#### Value proposition

InternTrack allows students to efficiently track internship applications from the command line.

Instead of manually maintaining spreadsheets or notes, users can quickly record, update, and filter applications using
simple commands.

The application provides a lightweight and fast way to manage internship applications without requiring a graphical
interface.

---

### User stories

| Version | As a ...                                        | I want to ...                                     | So that I can ...                                                                     |
|---------|-------------------------------------------------|---------------------------------------------------|---------------------------------------------------------------------------------------|
| v1.0    | Year-2 CEG student applying to many internships | add a new application entry with company and role | keep all applications in one place                                                    |
| v1.0    | Forgetful applicant                             | record an application deadline                    | avoid missing closing dates                                                           |
| v1.0    | Student mass-applying during peak season        | list all applications                             | see what I have already applied to                                                    |
| v1.0    | Student mass-applying during peak season        | delete an application                             | remove outdated applications                                                          |
| v1.0    | Applicant networking with recruiters            | add a recruiter or HR contact                     | follow up with the correct person                                                     |
| v1.0    | Applicant tracking progress                     | record outcomes per round                         | track application progress                                                            |
| v1.0    | Applicant mass-applying mduring peak season     | filter outcomes of the current applying season    | filter application progress                                                           |
| v2.0    | An organized student                            | sort internship applications                      | organize and view them based on criteria                                              |
| v2.0    | A forgetful student                             | set a reminder for an internship application      | focus on important dates such as interviews, deadlines, or follow-ups                 |
| v2.0    | A error-prone student                           | undo my most recent add, edit, or delete command  | easily recover from accidental mistakes                                               |
| v2.0    | A data-driven student                           | view a summary of my internship applications      | see an overview of my application statuses, upcoming deadlines, and overall progress. 

---

### Use cases

{Use cases will be added here.}

---

### Non-Functional Requirements

1. The application should run on any system that supports Java 17 or above.
2. The application should store application data locally in a text file.
3. The system should respond to user commands within one second for typical usage.
4. The application should provide clear error messages for invalid inputs.
5. The application should support command-line usage without requiring a graphical interface.

---

### Glossary

*Application* – A job or internship submission to a company.

*Status* – The current stage of an application (e.g., Pending, Interview, Rejected, Accepted).

*CLI* – Command Line Interface used to interact with the application.

---

## Appendix: Instructions for manual testing

### Launch and shutdown

{Instructions for launching and shutting down the application will be added here.}

---

### Deleting an application

{Instructions for deleting an application will be added here.}

---

### Saving data

**Prerequisite**: The application is running and has data loaded from previous sessions.

#### Test Case 1: Auto-save After Add Command

1. Run the application
2. Add a new application: `add c/Google r/SWE Intern d/2025-08-01 ct/john@google.com`
3. Verify the application appears in the list
4. **Without closing the app**, navigate to `./data/applications.txt`
5. **Expected**: The file contains a new line with the added application in the format: `Google|SWE Intern|2025-08-01|john@google.com|Pending`

#### Test Case 2: Auto-save After Edit Command

1. Run the application with existing data
2. Edit an application: `edit 1 s/Accepted`
3. Verify the status has changed in the display
4. **Without closing the app**, open `./data/applications.txt`
5. **Expected**: The first application line now has `Accepted` as the last field instead of `Pending`

#### Test Case 3: Auto-save After Delete Command

1. Run the application with at least 2 applications
2. Delete an application: `delete 1`
3. Verify it's removed from the list
4. **Without closing the app**, open `./data/applications.txt`
5. **Expected**: The file now has one fewer line (the deleted application is gone)

#### Test Case 4: Persistence Across Sessions

1. Run the application and add 2 applications:
   - `add c/Google r/SWE Intern`
   - `add c/Microsoft r/Azure Developer`
2. Verify both appear in the list
3. **Close the application** (via `exit` command)
4. **Reopen the application**
5. **Expected**: Both applications reappear in the list; data persisted correctly

---

#### Error Handling: Corrupted Data

**Scenario 1: Missing Data Directory**

1. Delete the `./data/` directory entirely
2. Run the application
3. **Expected**: 
   - Directory is recreated automatically
   - `applications.txt` is created as an empty file
   - Application starts with an empty list (no crash)
   - Console log shows: `INFO: Created data directory: ...`

---

**Scenario 2: Missing Applications File**

1. Delete `./data/applications.txt` (but keep the `./data/` directory)
2. Run the application
3. **Expected**: 
   - File is recreated automatically
   - Application starts with an empty list
   - Console log shows: `INFO: Created new applications file: ./data/applications.txt`

---

**Scenario 3: Malformed Line (Wrong Number of Fields)**

1. Manually edit `./data/applications.txt` and add a corrupted line: `Google|SWE Intern|2025-08-01`
   (only 3 fields instead of 5)
2. Run the application
3. **Expected**: 
   - Application starts successfully with other valid data
   - Malformed line is skipped
   - Console log shows: `WARNING: Skipping malformed application record: Google|SWE Intern|2025-08-01`

---

**Scenario 4: Unparseable Date Field**

1. Manually edit `./data/applications.txt` and add a line with an invalid date: `Google|SWE Intern|NOT-A-DATE|john@google.com|Pending`
2. Run the application
3. **Expected**: 
   - Application starts successfully
   - Line with invalid date is skipped
   - Console log shows: `WARNING: Skipping invalid application record: ...`

---

**Scenario 5: Null/Missing Optional Fields**

1. Manually add a line with optional fields as `null`: `Google|SWE Intern|null|null|Pending`
2. Run the application
3. **Expected**: 
   - Application loads successfully
   - Application is displayed correctly with no deadline and no contact information

---

**Scenario 6: Completely Corrupted File**

1. Replace the entire contents of `./data/applications.txt` with random text (e.g., "CORRUPTED DATA 12345")
2. Run the application
3. **Expected**: 
   - Application starts with an empty list (no crash)
   - All corrupted lines are skipped with warnings
   - User can continue using the application and add new data

---

