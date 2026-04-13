# User Guide

## Table of Contents

1. [Introduction](#introduction)
2. [Quick Start](#quick-start)
3. [Features](#features)
4. [Commands](#commands)
   - [1. Add a new internship application: `add`](#1-add-a-new-internship-application-add)
   - [2. List all applications: `list`](#2-list-all-applications-list)
   - [3. Edit an application: `edit`](#3-edit-an-application-edit)
   - [4. Delete an application: `delete`](#4-delete-an-application-delete)
   - [5. Filter applications: `filter`](#5-filter-applications-filter)
   - [6. View applications with upcoming deadlines: `remind`](#6-view-applications-with-upcoming-deadlines-remind)
   - [7. Sort applications: `sort`](#7-sort-applications-sort)
   - [8. Undo the most recent change: `undo`](#8-undo-the-most-recent-change-undo)
   - [9. Get summary: `summary`](#9-get-summary-summary)
   - [10. Exit the application: `bye`](#10-exit-the-application-bye)
5. [FAQ](#faq)
6. [Command Summary](#command-summary)

---

## Introduction

InternTrack is a command-line application that helps users track their internship applications efficiently. It allows users to record application details, update statuses, and organize applications using filtering and sorting commands.

InternTrack is designed for students who prefer fast keyboard-based workflows.

---

## Quick Start

1. Ensure that you have **Java 17 or above** installed.
2. Download the latest version of `InternTrack` from  
   https://github.com/AY2526S2-CS2113-W10-1/tp/releases/tag/v1.0
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

### Important: Restricted Characters

* The pipe character `|` is **not allowed** in any text input fields (company, role, contact, deadline). Using this character will result in an error message. Please use alternative characters such as `/`, `&`, or `-` instead.

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

Deadline format

```
YYYY-MM-DD
```

Examples

```
add c/Google r/Software Engineer
```

Adds an application with status **Pending**.

```
add c/Shopee r/Backend Intern d/2023-11-30 ct/John
```

Adds an application with a deadline and contact.

### Duplicate Detection

InternTrack automatically detects and prevents duplicate applications from being added. Two applications are considered duplicates based on the following criteria:

**Mandatory Comparison:**
- **Company** and **Role** must match exactly (comparison is case-insensitive and ignores extra spaces)

**Optional Comparison (only if both applications have values):**
- **Deadline**: If both applications have non-null deadlines, they must be identical. If one has a deadline and the other doesn't, they are NOT duplicates since different deadlines indicate different hiring cycles or seasons.

**Not Compared:**
- **Contact**: Contact person is treated as a metadata attribute of an application, not a duplicate criterion. Different recruiters at the same company for the same role represent the same application position, not separate opportunities.
- **Status**: Status is never checked for duplicates since all new applications automatically start as "Pending"

**Examples of Duplicate Detection:**

| Scenario | First Application | Second Application | Result    |
|----------|-------------------|-------------------|-----------|
| **Same company & role, both no deadline** | Google, SWE, no deadline | Google, SWE, no deadline | DUPLICATE |
| **Same company & role, different deadline** | Google, SWE, 2025-05-01 | Google, SWE, 2025-06-01 | SEPARATE   |
| **One has deadline, one doesn't** | Google, SWE, 2025-05-01 | Google, SWE, no deadline | SEPARATE  |
| **Same company & role, different contact** | Google, SWE, contact: John | Google, SWE, contact: Jane | DUPLICATE |
| **Case and spacing variations** | Google, SWE | GOOGLE, S W E | DUPLICATE |

**Why this logic?**

- Different deadlines represent different hiring cycles, seasons, or batches. Applications to the same company for the same role but with different deadlines are genuinely distinct opportunities. If you wanted to add a deadline to an existing application, use the `edit` command.
- Contact person variations (different recruiters handling different submissions) do not make them separate applications from the company's perspective. Applicant tracking systems typically treat multiple submissions for the same role as one application record. Thus, contact is stored as a tracked field but not used as a duplicate criterion.

---

## 2. List all applications: `list`

Displays all applications currently stored in the tracker.

Format

```
list
```

Example

```
list
```

Example output

```
You have applied for 3 roles
1. Backend Intern at Shopee is Accepted. Apply by 2023-11-30. Contact with John.
2. SWE Intern at Google is Pending.
3. ML Intern at Meta is Pending.
```

---

## 3. Edit an application: `edit`

Updates one or more fields of an existing application.

Format

```
edit INDEX [c/COMPANY] [r/ROLE] [d/DEADLINE] [ct/CONTACT] [s/STATUS]
```

Parameters

- `INDEX` : Index of the application shown in the list
- `c/COMPANY` : Updated company name
- `r/ROLE` : Updated role name
- `d/DEADLINE` : Updated deadline in `YYYY-MM-DD` format
- `ct/CONTACT` : Updated recruiter or HR contact
- `s/STATUS` : Updated status value

Notes

- You must provide at least one field to update.
- Each field can only be supplied once in the same command.
- The application index must be greater than 0.
- For `s/STATUS`, some default statuses you can think of are `Applied`, `Pending`, `Accepted`, `Rejected` or you custom statuses you set.
Example

```
edit 2 s/Accepted
```

Updates application 2 to `Accepted`.

```
edit 1 c/Google Singapore ct/Jane Tan
```

Updates both the company and contact fields for application 1.

Example output

```
Nice! I've updated application 2:
  Google - Software Engineer (Deadline: 2026-04-15, Contact: Jane Tan, Status: Accepted)
```

---

## 4. Delete an application: `delete`

Removes an application from the tracker.

Format

```
delete INDEX
```

Parameters

- `INDEX` : Index of the application in the list

Example

```
delete 2
```

Result

```
Noted. I've removed this application.
```

---

## 5. Filter applications: `filter`

Shows applications that match one filter criterion.

Format

```
filter c/COMPANY
filter r/ROLE
filter d/DEADLINE
filter ct/CONTACT
filter s/STATUS
```

Notes

- The command accepts exactly one field per use.
- Text matching for company, role, contact, and status is case-insensitive and matches substrings.
- For `d/DEADLINE`, InternTrack shows applications with deadlines on or before the specified date.
- For `s/STATUS`, some default statuses you can think of are `Applied`, `Pending`, `Accepted`, `Rejected` or you custom statuses you set.

Examples

```
filter s/Pending
```

Shows all applications whose status is `Pending`.

```
filter c/Meta
```

Shows all applications whose company contains `Meta`, such as `Meta Platforms`.

```
filter d/2026-04-10
```

Shows all applications with deadlines on or before `2026-04-10`.

Example output

```
You have 2 applications matching status Pending.
1. Backend Intern at Shopee is Pending. Apply by 2026-04-03.
2. SWE Intern at Google is Pending.
```

---

## 6. View applications with upcoming deadlines: `remind`

Shows applications with deadlines within the next N days, starting from today onwards.

**Important:** Only applications with deadlines on or after today are shown. Applications with past deadlines (even if recently expired) are **not** displayed, regardless of the N value. This ensures you focus on active opportunities rather than expired applications.

It will throw error if the day specified is invalid(negative, not integer) or too big(more than 32 bits).

Format

```
remind [DAYS]
```

Parameters

- `[DAYS]` : Number of days to look ahead from today (optional, defaults to 7)

Examples

```
remind
```

Shows applications due in the next 7 days (from today onwards).

```
remind 3
```

Shows applications due in the next 3 days (from today onwards).

Example output

```
You have 2 applications due in the next 3 days (up to 2026-04-04).
1. Software Engineer at Google is Pending. Apply by 2026-04-03.
2. Data Analyst at Microsoft is Pending. Apply by 2026-04-02.
```

If there are no applications with upcoming deadlines:

```
No applications due in the next 3 days.
```

**Note on Past Deadlines:** Applications with deadlines before today are excluded from the reminder. For example, if you have an application with deadline 2025-04-12, and it is 2026-04-13 today, it will not appear in any `remind` command regardless of whether you use `remind 1`, `remind 365`, or any other N value.

---

## 7. Sort applications: `sort`

Sorts applications based on specified criteria.

If the criteria for sorting is non-existent, it will be put at the end of the list unless `NONNULL` flag is used.

Format

```
sort by/CRITERIA [DESC] [NONNULL]
```

Parameters

- `by/CRITERIA` : Sorting field

Supported values

```
ROLE
COMPANY
DEADLINE
CONTACT
STATUS
```

Optional flags

- `DESC` : Sort in descending order
- `NONNULL` : Exclude entries where the chosen field is null

Examples

```
sort by/COMPANY
```

Sort applications alphabetically by company.

```
sort by/DEADLINE DESC
```

Sort applications by deadline in descending order.

---

## 8. Undo the most recent change: `undo`

Reverts the most recent modifying command. If previous there is no supported commands for undo, it will do nothing.

Supported commands

- `add`
- `edit`
- `delete`
- `archive`
- `unarchive`

Format

```
undo
```

Example

```
undo
```

Result

```
Done. I've undone the most recent change.
```

If there is nothing to undo, InternTrack will show an error message.

---

## 9. Get summary: `summary`

Provides a basic summary regarding the status of all internship applications.

Format

```
summary
```

Example

```
summary
```

Result

```
____________________________________________________________
   INTERNSHIP APPLICATION SUMMARY   
------------------------------------
Total Applications Tracked: 3

Status Overview:
 - Pending: 3

Upcoming Deadlines (Next 7 days):
 - Amazon (Fullstack Developer) : Due in 4 days.
____________________________________________________________
```
## 10. Archive an application: `archive`

Marks an application as archived without deleting it from the tracker.

Archived applications remain stored, but can be separated from active applications for better organisation.

Format

```
archive INDEX
```

Parameters

- `INDEX` : Index of the application in the list

Example

```
archive 2
```

Result

Archives application 2.

**Example output:**

```
Nice! I've archived application 2:
[Archived] Meta - Data Analyst (Deadline: 2026-05-25, Contact: Bob, Status: Pending)
```

### Notes

- The application index must be greater than 0.
- The application must exist in the list.
- An already archived application cannot be archived again.

---

## 11. Restore an archived application: `unarchive`

Restores an archived application back to its active state.

 Format

```
unarchive INDEX
```
 
Parameters

- `INDEX` : Index of the application in the list

 Example

```
unarchive 2
```

 Result

Restores application 2.

**Example output:**

```
Nice! I've restored application 2:
Meta - Data Analyst (Deadline: 2026-05-25, Contact: Bob, Status: Pending)
```

 Notes

- The application index must be greater than 0.
- The application must exist in the list.
- The application must already be archived.

---

## 12. View archived applications: `listarchived`

Displays all archived applications currently stored in the tracker.

 Format

```
listarchived
```

 Example

```
listarchived
```

 Result

Shows all archived applications.

**Example output:**

```
You have 1 archived application
1. [Archived] Data Analyst at Meta is Pending. Apply by 2026-05-25. Contact with Bob.
```

If there are no archived applications:

```
You have no archived applications.
```
## 13. Exit the application: `bye`

Closes InternTrack.

Format

```
bye
```

Example

```
bye
```

Result

```
Bye. Hope to see you again soon!
```

---

# FAQ

**Q: Can I undo multiple actions?**  
Yes. InternTrack keeps a history of previous states, so multiple undo commands can be used sequentially.

**Q: What happens if I undo after restarting the program?**  
Undo history is cleared when the application restarts.

---

# Command Summary

| Command | Format |
|--------|-------|
| Add | `add c/COMPANY r/ROLE [d/DEADLINE] [ct/CONTACT]` |
| List | `list` |
| Edit | `edit INDEX [c/COMPANY] [r/ROLE] [d/DEADLINE] [ct/CONTACT] [s/STATUS]` |
| Delete | `delete INDEX` |
| Filter | `filter c/COMPANY`, `filter r/ROLE`, `filter d/DEADLINE`, `filter ct/CONTACT`, or `filter s/STATUS` |
| Remind | `remind [DAYS]` |
| Sort | `sort by/CRITERIA [DESC] [NONNULL]` |
| Undo | `undo` |
| Summary | `summary` |
| Archive | `archive INDEX` |
| Unarchive | `unarchive INDEX` |
| List archived | `listarchived` |
| Exit | `bye` |

* Archived applications are marked with `[Archived]` when displayed.
* Text-based `filter` commands use case-insensitive substring matching.
