# Yuanjin's Project Portfolio Page

### Project: InternTrack

## Overview

InternTrack streamlines the internship application chaos. It provides a fast CLI interface to log company contacts and application deadlines, ensuring students never miss a follow-up in a high-volume application season.

My main contributions focused on making application records easier to query and update reliably, especially through the `filter` workflow and the redesign of `edit` to support safe partial updates.

## Summary of Contributions

### Code Contributed

- [RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=hykzr&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=hykzr&tabRepo=AY2526S2-CS2113-W10-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)
- Main implementation PRs: [#18](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/18), [#39](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/39), [#49](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/49), [#84](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/84), [#146](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/146)

### Enhancements Implemented

- **Filter command**: implemented the original `filter` feature end-to-end in [#18](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/18), covering parser support, command handling, filtering logic, and UI output.
- **Richer filter architecture**: expanded `filter` in [#39](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/39) with a dedicated `FilterCriteria` model, support for all supported fields (`company`, `role`, `deadline`, `contact`, `status`), strict single-criterion validation, and filtered views that do not mutate persisted data.
- **Substring-based text filtering**: improved usability in [#146](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/146) by changing text filters from exact matching to case-insensitive substring matching, so commands such as `filter c/Meta` work naturally for `Meta Platforms`. This required coordinated changes to logic, JUnit tests, text-ui tests, and documentation.
- **Edit command redesign**: extended `edit` in [#39](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/39) to support partial multi-field updates through an `EditDetails` object. The parser now validates duplicate prefixes, empty values, and invalid indices before mutation, while `ApplicationList.editApplication(...)` applies only supplied changes.
- **Supporting fixes and refactoring**: fixed edge cases such as missing input and the app not terminating cleanly after `bye`, and refactored duplicated UI printing logic into a shared helper to reduce maintenance overhead.

### Contributions to the UG

- Wrote and updated the `edit` and `filter` command sections, including command format, examples, notes, and expected output.
- Updated the `filter` documentation after the substring-matching change so the UG reflects actual search behavior.
- Main UG documentation PR: [#49](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/49), with follow-up update in [#146](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/146).

### Contributions to the DG

- Added the `FilterCriteria` and `EditDetails` component sections to explain the abstractions introduced for filtering and partial edits.
- Wrote the `Edit feature` and `Filter feature` implementation sections, including command flow, validation logic, and design considerations.
- Updated and corrected the edit sequence diagram in [#84](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/84).
- Main DG documentation PR: [#49](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/49), with follow-up updates in [#84](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/84) and [#146](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/146).

### Contributions to Team-Based Tasks

- Added my team entry to `AboutUs` early in the project in [#9](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/9).
- Helped keep implementation and documentation aligned by updating both the UG and DG whenever `filter` and `edit` behavior changed.
- Addressed post-testing usability feedback by implementing the substring filter improvement in [#146](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/146).

### Review/Mentoring Contributions

- Reviewed and approved [#10](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/10) and [#36](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/36).
