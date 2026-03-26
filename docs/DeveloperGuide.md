# Developer Guide

## Acknowledgements

This project was developed as part of the CS2113 Team Project at the National University of Singapore.

The project structure and development workflow follow the guidelines from the SE-EDU project template:
https://se-education.org/

The project focuses on learning Object-Oriented Programming (OOP) principles and good coding practices for collaborative software development.

The project was developed using Java and standard software engineering tools such as Git for version control and GitHub for project management and collaboration.

Java standard libraries such as `java.util`, `java.io`, and `java.time` were used in the implementation.

---

# Design & Implementation

## Architecture

InternTrack follows a layered architecture consisting of four main components:

- **UI (Ui)** – Handles user interaction through the command-line interface.
- **Logic (Parser, InternTrack)** – Interprets commands and coordinates system operations.
- **Model (Application, ApplicationList)** – Maintains in-memory application data.
- **Storage (Storage)** – Handles saving and loading application data from disk.

The interaction flow is:

User Input → UI → Parser → InternTrack → Model → Storage

This layered separation ensures that each component has a clearly defined responsibility.

---

## Application Initialization: Loading Persisted Data

Before user interaction begins, the application loads previously saved data from disk.

When `InternTrack.main()` is invoked:

1. An empty `userApplications` ArrayList is created.
2. `Storage.loadApplications(userApplications)` is called.
3. The system checks if the `./data/` directory and `./data/applications.txt` exist.
4. Missing files or directories are created automatically.
5. Each line from the file is parsed using `Storage.parseFileString()`.
6. Pipe-delimited data is converted back into `Application` objects.
7. Each application is added to the in-memory list.

This design ensures:

- previously saved data is restored
- in-memory state matches stored data
- corrupted entries are skipped safely

---

# Implementation of Add Feature

The `add` command allows users to record a new internship application.

The command pipeline consists of five stages:

1. Parsing user input
2. Object creation
3. Model update
4. Storage persistence
5. User feedback

---

## Step 1: Parsing User Input

Example command:

```
add c/Google r/Software Engineer d/2024-03-31 ct/John Doe
```

The input is processed as follows:

1. `Ui.readCommand()` receives the input.
2. The command is passed to `InternTrack.handleCommand()`.
3. The system identifies the `add` prefix and routes the request to `handleAddCommand()`.

`Parser.createApplication()` processes the input by:

- splitting tokens using regex `(?=c/|r/|ct/|d/)`
- extracting required fields (`c/`, `r/`)
- extracting optional fields (`d/`, `ct/`)
- validating that required fields are present
- parsing deadline values using `LocalDate.parse()`

Invalid inputs throw an `InternTrackException`.

---

## Step 2: Object Creation

Once parsing succeeds, a new `Application` object is created:

```
new Application(company, role, deadline, contact)
```

The constructor automatically assigns the default status:

```
Pending
```

This ensures every application begins with a consistent initial state.

---

## Step 3: Model Update

The new application is inserted into the system using:

```
ApplicationList.addApplications()
```

This method adds the application to the internal `userApplications` list.

---

## Step 4: Storage Persistence

After the model is updated:

```
Storage.saveApplications(userApplications)
```

This method:

1. Opens `./data/applications.txt`
2. Iterates through the application list
3. Serializes each entry into pipe-delimited format

Example format:

```
company|role|deadline|contact|status
```

---

## Step 5: User Feedback

`Ui.printAddApplication()` displays confirmation to the user including:

- application details
- updated application count

---

## Sequence Diagram: Add Command

![add_sequence_diag.png](diagrams/add_sequence_diag.png)

---

# Implementation of Edit Feature

The `edit` command allows users to modify the status of an existing application.

Command format:

```
edit INDEX s/STATUS
```

Example:

```
edit 2 s/Accepted
```

Implementation steps:

1. `Parser.parseEditCommand()` extracts the index and new status.
2. The index is validated to ensure it exists in the application list.
3. `ApplicationList.editApplicationStatus()` retrieves the application.
4. The application’s `setStatus()` method updates the status value.
5. `Storage.saveApplications()` persists the updated list.
6. `Ui.printEditSuccess()` displays confirmation to the user.

The `Application.setStatus()` method also performs validation to ensure that the status value is not null or empty.

This approach keeps validation within the model while command interpretation remains in the logic layer.

---

## Sequence Diagram: Edit Command

![edit_sequence_diag.png](diagrams/edit_sequence_diag.png)

---

# Implementation of Undo Feature

The `undo` command allows users to revert the most recent modification made to the application list.

Supported commands:

- add
- edit
- delete

Undo is implemented using a **snapshot-based state restoration mechanism**.

---

## Snapshot Mechanism

Before executing any modifying command:

1. A deep copy of the current `userApplications` list is created.
2. The snapshot is pushed onto an undo history stack.

When the user executes `undo`:

1. The most recent snapshot is popped from the stack.
2. The application list is replaced with the snapshot.
3. The restored state is written to storage.

This guarantees the system returns to the exact state before the most recent change.

---

## Example Workflow

Command sequence:

```
add c/Google r/SWE Intern
delete 1
undo
```

Execution flow:

1. `add` stores a snapshot of the empty list then adds the application.
2. `delete` stores a snapshot then removes the application.
3. `undo` restores the previous snapshot.

The deleted application reappears in the list.

---

## Sequence Diagram: Undo Command

![undo_sequence_diag.png](diagrams/undo_sequence_diag.png)

---

# Design Considerations

## Aspect 1: Application Instantiation

**Current Approach**

Application objects are created inside the parser.

Pros:

- clear separation between parsing and model logic
- centralized validation
- improved maintainability

Cons:

- parser depends on the application structure

Alternative approach: create objects inside the model layer.

This was rejected because it mixes parsing logic with model responsibilities.

---

## Aspect 2: Storage Persistence

**Current Approach**

Automatically save after every modifying command.

Pros:

- prevents data loss
- ensures consistent state

Cons:

- slightly increased disk I/O

Alternative approach: manual save command.

Rejected due to high risk of losing data.

---

## Aspect 3: Storage Format

**Current Approach**

Pipe-delimited text format.

Pros:

- human readable
- no external dependencies
- simple implementation

Cons:

- limited scalability

Alternative options considered:

- JSON
- database storage

These options introduce unnecessary complexity for a small CLI application.

---

## Aspect 4: Undo Implementation

Two approaches were evaluated.

### Snapshot-based restoration (current approach)

Pros:

- simple and reliable
- independent of specific command logic
- guarantees correct state restoration

Cons:

- increased memory usage due to storing copies

### Command-based reversal

Pros:

- more memory efficient

Cons:

- significantly more complex
- each command requires custom undo logic

Given the relatively small number of applications expected, the snapshot approach was chosen for simplicity and reliability.

---

# Product Scope

## Target User Profile

InternTrack targets students applying to multiple internships who want a simple command-line tool to track applications.

Typical users:

- university students
- CLI-comfortable users
- applicants managing multiple internship submissions

---

## Value Proposition

InternTrack provides a lightweight alternative to spreadsheets or notes.

Users can:

- record applications quickly
- track progress
- filter and organize entries

All through fast CLI commands.

---

# User Stories

| Version | As a... | I want to... | So that I can... |
|--------|--------|--------|--------|
| v1.0 | Year-2 student applying for internships | add a new application | keep all applications in one place |
| v1.0 | Forgetful applicant | record deadlines | avoid missing closing dates |
| v1.0 | Student mass-applying during peak season | list applications | see what I applied for |
| v1.0 | Student mass-applying during peak season | delete applications | remove outdated entries |
| v1.0 | Applicant networking with recruiters | add recruiter contact | follow up with the correct person |
| v1.0 | Applicant tracking progress | update application status | track interview progress |
| v1.0 | Applicant mass-applying during peak season | filter applications | view applications by status |
| v2.0 | Organized student | sort applications | organize applications based on criteria |
| v2.0 | Error-prone student | undo commands | recover from accidental mistakes |

---

# Non-Functional Requirements

1. The application must run on systems supporting Java 17 or above.
2. Application data should be stored locally in a text file.
3. Commands should execute within one second under typical usage.
4. The system must display clear error messages for invalid input.
5. The application should support full command-line operation without a graphical interface.

---

# Glossary

**Application** – An internship submission to a company.

**Status** – The stage of an application (e.g., Pending, Interview, Accepted).

**CLI** – Command Line Interface.

---

# Instructions for Manual Testing

## Launching the application

Run:

```
java -jar InternTrack.jar
```

---

## Add an application

```
add c/Google r/SWE Intern
```

Expected result:  
Application is added with status `Pending`.

---

## Edit an application

```
edit 1 s/Interview
```

Expected result:  
Application 1 status becomes `Interview`.

---

## Delete an application

```
delete 1
```

Expected result:  
Application 1 is removed.

---

## Undo command

Command sequence:

```
add c/Meta r/ML Intern
delete 1
undo
```

Expected result:  
The deleted application is restored.

---

## Undo when no history exists

```
undo
```

Expected result:  
The system displays a message indicating that there is no command to undo.