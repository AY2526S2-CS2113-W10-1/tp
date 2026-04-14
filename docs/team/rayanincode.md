# Rayan Rahman's Project Portfolio Page

### Project: InternTrack

## Overview

InternTrack streamlines the internship application chaos. It provides a fast CLI interface to log company contacts and application deadlines, ensuring students never miss a follow-up in a high-volume application season.

## Summary of Contributions

### Code Contributed

- [RepoSense link]([https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=baolam&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=BaoLam194&tabRepo=AY2526S2-CS2113-W10-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=RayanIncode&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=BaoLam194&tabRepo=AY2526S2-CS2113-W10-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancements Implemented

#### New Features:

- **Delete command**: Integrated delete support into the existing command handling flow so users can remove an active application safely from the tracked list.
    
    - **What it does:** Users can delete an active application using the appropriate delete command format. The system parses the target index, removes the selected active application, updates storage, and shows a confirmation message to the user.
        
    - **Justification:** This feature is essential for maintaining an accurate and up-to-date application tracker. Users may enter duplicate applications, add incorrect entries, or no longer wish to keep certain active applications, so deletion is necessary for proper list management.
        
    - **Highlights:** The implementation saves the current state before deletion to support undo functionality, deletes only from the active application list, logs the deleted company and role for traceability, updates the UI with a clear confirmation, and persists the updated list to storage immediately.
        
    
- **Summary command**: Implemented a dedicated SummaryCommand to generate an overview of the user’s internship applications in a concise and informative report.
    
    - **What it does:** Users can generate a summary of their tracked applications, including the total number of applications, the number of active and archived applications, a breakdown of active applications by status, and upcoming deadlines within the next 7 days.
        
    - **Justification:** This feature improves usability by giving users a quick snapshot of their internship search progress without requiring them to inspect each application manually. It helps users monitor their pipeline, identify status trends, and stay aware of urgent deadlines.
        
    - **Highlights:** The summary distinguishes between active and archived applications, handles empty application lists gracefully, groups active applications by status using a map-based count, and identifies approaching deadlines using LocalDate and ChronoUnit. It also provides reassuring feedback when no deadlines are near, making the output more user-friendly.

#### **Other Enhancements:**

- **Undo support integration for deletion**: Ensured that deletion works together with the undo history mechanism by saving the current state before removing an application.
    
- **User feedback and display clarity**: Improved UI messaging so that both deletion results and summary outputs are clearly presented to the user in a readable format.
    
- **Edge case handling**: Covered important scenarios such as empty application lists, missing or blank statuses, and applications without relevant upcoming deadlines, so the commands behave robustly without crashing.
    
### Contributions to the UG

- Added documentation for the `summary `and `delete` command, including format, parameters, and usage examples with expected output

### Contributions to the DG

- **UI Component**: Documented the design and responsibilities of the Ui component, including:
    
    - Its role as the interface layer between the user and the applicatio
    - The class diagram illustrating how Ui interacts with other components
    - Key design considerations such as using static methods for CLI simplicity and centralizing formatting logic within the Ui class
    - Implementation details covering standardized output, context-aware feedback, and consistent display formatting across commands
        
- **Delete Feature Implementation**: Documented the delete command workflow and its supporting rationale, including:

    - How the command is handled through InternTrack.handleDeleteCommand()
    - Parsing and validating the target index before deletion
    - Preserving the previous state for undo support
    - Removing the selected application, updating storage immediately, and displaying confirmation feedback
    - Error handling for invalid indices and non-numeric input
    - Design considerations comparing index-based deletion with name-based deletion
        
- **Summary Feature Implementation**: Documented the summary command and its internal reporting logic, including:
    
    - The purpose of the feature in giving users a high-level overview of their internship application progress
    - Real-time generation of total application counts, active versus archived breakdown, status distribution, and upcoming deadlines
    - Helper method responsibilities such as dynamic status aggregation and deadline tracking
    - Error handling for empty lists and missing data fields
    - Design considerations comparing real-time calculation against a cached summary approach
    - Sequence diagram illustrating the end-to-end workflow of the summary command
        
### Contributions to Team-Based Tasks

- Review and approve other team members pull request to ensure good standard.

### Contributions to peer reviewing issue

- Squash multiple bugs related to own feature and team implementation.

### Review/Mentoring Contributions

- Pre-Issue PRs reviewed:[#87](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/87), [#48](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/48), [#22](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/22), [#6](https://github.com/AY2526S2-CS2113-W10-1/tp/pull/6)
