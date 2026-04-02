# User Guide

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
- Text matching for company, role, contact, and status is case-insensitive.
- For `d/DEADLINE`, InternTrack shows applications with deadlines on or before the specified date.

Examples

```
filter s/Pending
```

Shows all applications whose status is `Pending`.

```
filter c/Google
```

Shows all applications whose company is `Google`.

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

Shows applications with deadlines within the next N days.

Format

```
remind [DAYS]
```

Parameters

- `[DAYS]` : Number of days to look ahead (optional, defaults to 7)

Examples

```
remind
```

Shows applications due in the next 7 days.

```
remind 3
```

Shows applications due in the next 3 days.

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

---

## 7. Sort applications: `sort`

Sorts applications based on specified criteria.

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

Reverts the most recent modifying command.

Supported commands

- `add`
- `edit`
- `delete`

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

## 9. Exit the application: `bye`

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
| Exit | `bye` |
