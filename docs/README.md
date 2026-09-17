# Braun — User Guide

Braun is a CRT TV-headed supernatural talk-show host chatbot from *Got Dropped into a Ghost Story, Still Gotta Work*, assisting field explorers in managing broadcast tasks, investigations, deadlines, and anomaly schedules.

---

## Features

### 1. Adding Tasks
- **Todo**: Adds a simple task without time constraints.
  - `todo <description>`
  - Example: `todo explore anomaly`
- **Deadline**: Adds a task with a due date/time.
  - `deadline <description> /by <date/time>`
  - Example: `deadline submit monthly report /by 2026-08-30 1700`
- **Event**: Adds a scheduled event with start and end times.
  - `event <description> /from <start> /to <end>`
  - Example: `event search pink rabbit doll /from 2026-08-24 1400 /to 2026-08-24 1600`

### 2. Managing Tasks
- **List**: Displays all broadcast tasks.
  - `list`
- **Mark**: Marks a task as completed.
  - `mark <task_number>`
- **Unmark**: Marks a task as not completed.
  - `unmark <task_number>`
- **Delete**: Removes a task from the schedule.
  - `delete <task_number>`

### 3. Finding Tasks (`C-BetterSearch`)
Searches for broadcast tasks using flexible keyword, phrase, and schedule matching.

- **Single Keyword**: Searches for tasks containing the keyword (case-insensitive).
  - `find rabbit`
- **Multiple Keywords (OR Search)**: Finds tasks matching any of the space-separated words.
  - `find rabbit ghost`
- **Quoted Exact Phrases**: Finds tasks containing the exact phrase enclosed in quotes.
  - `find "pink rabbit"`
- **Date & Time Matching**: Matches tasks scheduled for specific dates or times.
  - `find Aug 24`
  - `find 2026-08-30`

### 4. Querying by Date
- `date <yyyy-MM-dd>`
- Example: `date 2026-08-24`

### 5. Exiting Application
- `bye`