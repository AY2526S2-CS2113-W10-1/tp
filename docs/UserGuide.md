I've updated your User Guide to include the `summary` command. I’ve inserted it as **Section 9** and bumped the "Exit" command to **Section 10** to keep the flow logical. I also updated the **Command Summary** table at the bottom for quick reference.

---

# User Guide

## Introduction

InternTrack is a command-line application that helps users track their internship applications efficiently. It allows users to record application details, update statuses, and organize applications using filtering and sorting commands.

InternTrack is designed for students who prefer fast keyboard-based workflows.

---

## Quick Start

1. Ensure that you have **Java 17 or above** installed.
2. Download the latest version of `InternTrack` from  
   [https://github.com/AY2526S2-CS2113-W10-1/tp/releases/tag/v1.0](https://github.com/AY2526S2-CS2113-W10-1/tp/releases/tag/v1.0)
3. Open a terminal in the folder containing the jar file.
4. Run the application using:

```
java -jar InternTrack.jar
```

5. Type commands into the terminal and press Enter to execute them.

---

## Features

### Notes about the command format

* Words in **UPPER_CASE** are parameters to be supplied by the user. Example: `add c/COMPANY` where COMPANY is replaced by the user.
* Items in **square brackets** are optional. Example: `[d/DEADLINE]`
* Parameters can be given in **any order**. Example: `add c/Google r/Intern` is the same as `add r/Intern c/Google`.

---

# Commands

---

## 1. Add a new internship application: `add`

Adds a new internship application to your tracker.

Format

```
add c/COMPANY r/ROLE [d/DEADLINE] [ct/CONTACT]
```

Parameters

- `c/COMPANY` : Name of the company
- `r/ROLE` : Role applied for
- `d/DEADLINE` : Optional application deadline
- `ct/CONTACT` : Optional recruiter or HR contact

---

## 2. List all applications: `list`

Displays all applications currently stored in the tracker.

Format

```
list
```

---

## 3. Edit an application status: `edit`

Updates the status of an existing application.

Format

```
edit INDEX s/STATUS
```

---

## 4. Delete an application: `delete`

Removes an application from the tracker.

Format

```
delete INDEX
```

---

## 5. Filter applications by status: `filter`

Shows applications that match a specific status.

Format

```
filter s/STATUS
```

---

## 6. View applications with upcoming deadlines: `remind`

Shows applications with deadlines within the next N days.

Format

```
remind [DAYS]
```

---

## 7. Sort applications: `sort`

Sorts applications based on specified criteria.

Format

```
sort by/CRITERIA [DESC] [NONNULL]
```

---

## 8. Undo the most recent change: `undo`

Reverts the most recent modifying command (`add`, `edit`, `delete`).

Format

```
undo
```

---

## 9. View a summary report: `summary`

Displays a high-level overview of your application progress. This includes the total number of applications, a breakdown of statuses (e.g., Pending, Accepted), and a list of deadlines approaching within the next 7 days.

Format

```
summary
```

Example output

```
📊 INTERNSHIP APPLICATION SUMMARY 📊
------------------------------------
Total Applications Tracked: 5

Status Overview:
 - Pending: 3
 - Accepted: 1
 - Rejected: 1

Upcoming Deadlines (Next 7 days):
 - Google (Software Engineer) : Due in 2 days.
```

---

## 10. Exit the application: `bye`

Closes InternTrack.

Format

```
bye
```

---

# FAQ

**Q: Can I undo multiple actions?** Yes. InternTrack keeps a history of previous states, so multiple undo commands can be used sequentially.

**Q: What happens if I undo after restarting the program?** Undo history is cleared when the application restarts.

---

# Command Summary

| Command | Format |
|--------|-------|
| **Add** | `add c/COMPANY r/ROLE [d/DEADLINE] [ct/CONTACT]` |
| **List** | `list` |
| **Edit** | `edit INDEX s/STATUS` |
| **Delete** | `delete INDEX` |
| **Filter** | `filter s/STATUS` |
| **Remind** | `remind [DAYS]` |
| **Sort** | `sort by/CRITERIA [DESC] [NONNULL]` |
| **Undo** | `undo` |
| **Summary**| `summary` |
| **Exit** | `bye` |
