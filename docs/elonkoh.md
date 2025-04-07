# Koh Elon - Project Portfolio Page

## Project: FlashCLI
## Overview
FlashCLI is a command-line flashcard application designed specifically for CS2113 Software Engineering students. It helps students create, manage, and review flashcards efficiently, making it easier to retain important concepts in Java, software design, and coding best practices.

Given below are my contributions to the project
### Summary of Contributions
- **New Feature**: Added the ability to edit a specific flashcard.
  - **What it does**: Allows the user to edit a specific flashcard's question, answer, or both using a single command.
    The command will edit only the question or only the answer based on the specified user input (/q or /a or both)
  - **Justification**: This is an essential feature, allowing the user to modify existing flashcards keeps the flashcard in its
original order and index in the deck. But also makes it more convenient, without requiring the user to delete and re-create 
a new flashcard in the situation that he made a mistake or wishes to make changes. This provides a quick and easy way to make
edits.
  - **Highlights**: Users are able to edit either question or answer separately, but exceptions will be thrown for putting the 
arguments in the wrong order (/a then /q). 
  - **Credits**: Method to parsing string arguments learnt in the IP 
<br><br>
- **New Feature**: Added the ability to insert code snippets into flashcards
  - **What it does**: Allows the user to add snippets of code into the flashcards as an input, and it will be automatically parsed
and formatted with indents and line breaks to properly display the code following standards. The formatted code snipppets are visible when using the view commands.
  - **Justification**: This is a feature that tailors the flashcards to the target user - they will be using a CLI and as a 
CS student, will benefit greatly from having the flashcards store code snippets that can act as a visual aid for certain code related questions.
  - **Highlights**: Code snippets are formatted based on the curly braces found in the input, and indents are appropriately inserted
to accurately display the code. The algorithm was implemented to find the next open or close curly braces and append the correct number
of indents as well as line break to the desired output then stored and printed.
  - **Credits**:
    <br><br>
- **New Feature**: Added the ability to time the user during the quiz mode.
  - **What it does**: Tracks the time elapsed from when the user starts the quiz mode to display how long the user took for the quiz.
  - **Justification**: Users of flashcards and quizzes are typically timed to ensure that they are able to answer the questions in a timely manner.
They are able to see where they are slower or lacking in and can also view their progress after multiple runs.
  - **Highlights**: Timer class acts essentially as a stopwatch, and is able to store and return durations from multiple laps as well, works well with the quiz mode.
  - **Credits**:
<hr>
  
- Code contributed: [RepoSense link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=ElonKoh&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)
- **Documentation:**
  - User Guide:
    - Added documentation for the features `edit question and answer`, `edit question`, `edit answer` and `insert_code` [#130](https://github.com/AY2425S2-CS2113-F11-4/tp/pull/130) [#131](https://github.com/AY2425S2-CS2113-F11-4/tp/pull/131/files)
  - Developer's Guide:
    - Added diagrams and details for design and implementation of `edit` and `insert_code`. [#63](https://github.com/AY2425S2-CS2113-F11-4/tp/pull/63)
    - Added Non-Functional Requirements in the appendix. [#63](https://github.com/AY2425S2-CS2113-F11-4/tp/pull/63)
- **Community:**
  - Set up GitHub repo together as a team.
  - Reviewed PRs for merge. [#20](https://github.com/AY2425S2-CS2113-F11-4/tp/pull/20) [#45](https://github.com/AY2425S2-CS2113-F11-4/tp/pull/45) [#49](https://github.com/AY2425S2-CS2113-F11-4/tp/pull/49) [#64](https://github.com/AY2425S2-CS2113-F11-4/tp/pull/64)
  - Reviewed other group. [#T13-3 LeBook](https://github.com/nus-cs2113-AY2425S2/tp/pulls?q=is%3Aopen+is%3Apr+CS2113-T13-3+)