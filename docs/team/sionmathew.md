# Saayuj Ion's Project Portfolio Page

## Project: InternTrack

## Overview
InternTrack is a command-line application designed to help users efficiently manage internship applications. It supports adding, editing, filtering, and organising applications through a structured CLI workflow, with a focus on clarity, robustness, and ease of use.

---

## Summary of contributions

### Code contributed
[View my code contribution (RepoSense)](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=W10&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=SIONMATHEW&tabRepo=AY2526S2-CS2113-W10-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=functional-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

My contributions span multiple core components including command handling, parsing, model updates, storage integration, and UI output. I focused primarily on implementing complex features such as `edit`, `undo`, and archive-related functionality, ensuring correctness across the entire system.

---

### Enhancements implemented

#### 1. Edit Command
- Implemented a flexible `edit` command supporting partial updates via prefix-based inputs
- Introduced `EditDetails` to encapsulate optional fields and maintain clean separation between parsing and model logic
- Added validation for duplicate prefixes, invalid inputs, and empty fields before applying updates
- Integrated across Parser, ApplicationList, Storage, and UI with defensive checks (logging, assertions) and unit tests

**PRs:**
- [#14](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/14)
- [#22](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/22)
  
---

#### 2. Undo Command
- Implemented snapshot-based undo using deep copies to preserve previous states
- Integrated undo across all modifying commands (`add`, `edit`, `delete`, `archive`, `unarchive`)
- Ensured consistency between in-memory data and persisted storage

**PR:**
- [#34](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/34)
  
---

#### 3. Archive / Unarchive Feature
- Implemented soft-delete using an `isArchived` flag to retain data while excluding archived entries from active workflows
- Extended archive handling across multiple components including listing, filtering, sorting, and undo
- Modified related behaviours (e.g., summary and list outputs) to correctly account for archived entries
- Ensured compatibility with all existing commands without introducing duplicated logic or inconsistencies
- Ensured archived entries are consistently handled across all user-facing commands

**PR:**
- [#87](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/87)
  
---

### Contributions to the User Guide

I contributed extensively to the User Guide by designing clear, structured, and user-friendly documentation for both core and extended features.

**Commands Documented**
- `edit`
- `undo`
- `archive`
- `unarchive`
- `listarchived`

**Key Contributions**
- Defined precise command formats using consistent prefix-based syntax
- Provided realistic examples and expected outputs for each command
- Documented edge cases (e.g., invalid indices, empty undo history, duplicate fields)
- Ensured consistency in tone, formatting, and structure across all command sections

**Impact**
- Improved usability by making command behaviour explicit and predictable
- Enabled users to confidently use advanced features such as partial edits and undo operations

---

### Contributions to the Developer Guide

I led the design and refinement of key sections of the Developer Guide, focusing on both architectural clarity and correctness of implementation details.

**Component Design**
- Documented the Parser component, including its responsibilities, method structure, and relationships with domain classes.
- Designed and added a class diagram to illustrate object creation responsibilities (e.g., `Application`, `EditDetails`, `FilterCriteria`) and exception handling.

**Feature Implementation Documentation**
- Authored detailed implementation sections for:
    - `edit` command (partial update design using `EditDetails`)
    - `undo` command (snapshot-based state restoration using deep copies)
    - archive/unarchive features (soft-delete design and system integration)
- Explained internal workflows, design decisions, and trade-offs (e.g., snapshot vs command-based undo).

**UML Diagrams**
- Designed and corrected multiple sequence diagrams (`edit`, `undo`, archive) to strictly follow SE-EDU UML conventions:
    - Method-level calls instead of descriptive text
    - Correct activation bars and return arrows
    - Appropriate abstraction level (focusing on inter-component interactions)
- Ensured diagrams were both technically accurate and easy to interpret.

**Impact**
- Improved the clarity and correctness of the DG, making it easier for future developers to understand system behavior and design decisions.
- Ensured all diagrams and explanations align with SE-EDU standards, avoiding common UML notation errors.

**PRs:**
- [#24](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/24)
- [#40](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/40)
- [#51](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/51)
- [#58](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/58)
- [#150](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/150)
  
---

### Contributions to team-based tasks
- Set up and enforced GitHub fork-based workflow for the team.
- Guided team members on branching, PR workflow, and resolving merge issues.
- Managed releases (v1.0, v2.0, v2.1), including:
    - Generating JAR files
    - Uploading release assets
    - Ensuring documentation consistency

---

### Review / mentoring contributions
- Reviewed multiple PRs across features (summary, delete, DG updates, testing, UG updates).
- Provided feedback on:
    - Code correctness and edge cases
    - UML diagram accuracy
    - Documentation clarity and consistency
- Helped teammates debug issues and align with project architecture.

---

### Contributions beyond the project team
- Reported bugs and evaluated other teams’ products during the Practical Exam Dry Run.
- Applied SE-EDU bug classification guidelines (functionality bug, feature flaw, UG bug).
