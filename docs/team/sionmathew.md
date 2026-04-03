# Saayuj Ion's Project Portfolio Page

## Project: InternTrack

## Overview
InternTrack is a command-line application that helps users efficiently track and manage internship applications. It allows users to add, edit, filter, and organize applications through a streamlined CLI interface.

---

## Summary of contributions

### Code contributed
[View my code contribution (RepoSense)](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=W10&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=SIONMATHEW&tabRepo=AY2526S2-CS2113-W10-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=functional-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

My contributions span multiple components including command handling, parsing, model updates, storage handling, and UI output, primarily supporting the implementation of the `edit` and `undo` features.

---

### Enhancements implemented

**Edit Command**
- Implemented the `edit` command to update application status.
- Integrated across Parser, Logic, Model, and UI components.
- Added validation, assertions, logging, and improved storage robustness.
- Included unit tests for correctness.

**Highlights:**
- Required cross-component integration and defensive programming.

**PRs:** [#14](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/14), [#22](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/22)

---

**Undo Command**
- Implemented `undo` to revert the most recent modifying command (`add`, `edit`, `delete`).
- Designed using snapshot-based state restoration with deep copying.
- Integrated with all modifying commands and handled edge cases.

**Highlights:**
- Required stack-based state management and ensured data consistency.

**PR:** [#34](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/34)

---

### Documentation

**Developer Guide**
- Wrote the Introduction and contributed to overall DG structure.
- Authored documentation for Parser, Application, and ApplicationList components.
- Documented implementation of `edit` and `undo` features.
- Added UML and sequence diagrams for edit and undo workflows.

**PRs:** [#24](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/24), [#40](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/40), [#51](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/51)

---

### Contributions to team-based tasks
- Set up team GitHub workflow using forks.
- Guided team members on branching and collaboration.
- Generated and uploaded JAR files for v1.0 and v2.0 releases.
- Managed release assets including JAR and documentation files.

---

### Review/mentoring contributions
- Reviewed and approved multiple team PRs (e.g., summary, DG updates, testing, UG updates, delete feature).
- Provided feedback to ensure code quality, correctness, and consistency across the project.
