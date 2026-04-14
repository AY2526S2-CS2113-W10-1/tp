# Chua Yong Liang's Project Portfolio Page

### Project: InternTrack

## Overview

InternTrack streamlines the internship application chaos. It provides a fast CLI interface to log company contacts and application deadlines, ensuring students never miss a follow-up in a high-volume application season.

## Summary of Contributions

### Code Contributed

- [RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=chuayongliang6&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

### Enhancements Implemented

**New Features:**

- **Add Command**: Engineered the logic for adding applications with format `add c/COMPANY r/ROLE [d/DEADLINE] [ct/CONTACT]`. Implemented comprehensive input validation and field parsing to ensure data integrity.
- **Duplicate Detection**: Integrated a **whitespace-insensitive duplicate detection system** using regex-based normalization to notify users of duplicate applications and prevent redundant entries from being saved.
- **Remind Command**: Designed a deadline reminder system that utilizes `LocalDate` arithmetic to filter and display applications due within a specified number of days. Developed logic to explicitly exclude past-due applications to maintain user focus on active tasks.
- **Persistent Storage System**: Architected the entire file-based persistence backbone. Implemented bi-directional data conversion using a pipe-delimited text format and built a resilient loading pipeline that handles malformed or corrupted data files without crashing the application.

**Other Enhancements:**

- **Initial Application Model**: Established the project's core by designing the foundational `Application` class, defining its attributes, and implementing internal invariants using Java assertions to ensure state integrity.
- **Unit Testing**: Developed a comprehensive JUnit test suite covering critical logic paths, including **leap-year date parsing**, boundary conditions for reminders, and complex duplicate detection scenarios.
- **Logging & Error Handling**: Integrated professional Java logging and authored a custom `InternTrackException` hierarchy to provide descriptive, user-friendly error feedback.
- **Command Dispatch System**: Implemented the central routing and dispatcher logic for core commands including `add`, `remind`, `summary`, and `bye`.

### Contributions to the UG

- **Add & Remind Documentation**: Authored the technical guide for the `add` and `remind` commands, including detailed tables for duplicate detection logic and examples of expected output.

### Contributions to the DG

- **Storage Component**: Documented the complete load/save architecture, file format specification, and sequence diagrams for data persistence.
- **Add & Remind Implementation**: Penned the technical deep-dives for these features, utilising sequence diagrams and walkthroughs to explain the workflows.
- **Testing Guide**: Provided the project’s comprehensive manual testing guide, focusing on data saving verification and cross-session persistence.

### Contributions to Team-Based Tasks

- **Project Management**: Managed the team’s issue trackers by initializing and organizing task milestones for the v1.0 and v2.0 releases.
- **Meetings**: Initiate meetings with team members to align on project matters.

### Review/Mentoring Contributions

- **Major Code Reviews:** [#57](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/57), [#56](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/56), [#39](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/39), [#31](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/31)
- **Additional Reviews:** [#9](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/9), [#18](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/18), [#20](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/20), [#25](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/25), [#34](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/34), [#38](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/38), [#40](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/40), [#53](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/53), [#59](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/59), [#79](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/79), [#138](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/138), [#149](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/149), [#150](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/150), [#154](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/154)

### Contributions Beyond the Project Team

**Evidence of Helping Others:**
Gave comments for other group's sequence diagrams for their tP: [Link to PR](https://github.com/NUS-CS2113-AY2526-S2/tp/pull/6)
