package braun;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import braun.exception.BraunException;
import braun.storage.Storage;
import braun.task.Deadline;
import braun.task.Event;
import braun.task.Task;
import braun.task.Todo;
import braun.ui.Ui;
import braun.util.DateTimeUtil;

/**
 * Main entry point for the Braun chatbot application.
 * Braun is a CRT TV-headed supernatural talk show host from 'Got Dropped into a Ghost Story, Still Gotta Work'.
 * Coordinates user interactions through {@link Ui}, loads and saves tasks via {@link Storage},
 * and executes broadcast commands across stored {@link Task} objects.
 */
public class Braun {

    private static final String DEFAULT_STORAGE_PATH = Paths.get("data", "braun.txt").toString();

    private static final String CMD_BYE = "bye";
    private static final String CMD_LIST = "list";
    private static final String CMD_MARK = "mark";
    private static final String CMD_UNMARK = "unmark";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_DATE = "date";
    private static final String CMD_FIND = "find";
    private static final String CMD_TODO = "todo";
    private static final String CMD_DEADLINE = "deadline";
    private static final String CMD_EVENT = "event";

    private static final String DELIMITER_BY = "/by ";
    private static final String DELIMITER_FROM = "/from ";
    private static final String DELIMITER_TO = "/to ";

    private final Storage storage;
    private final Ui ui;
    private final ArrayList<Task> tasks;

    /**
     * Constructs a new {@code Braun} application instance with the default storage path.
     */
    public Braun() {
        this(DEFAULT_STORAGE_PATH);
    }

    /**
     * Constructs a new {@code Braun} application instance with the specified file storage path.
     *
     * @param filePath path to the local task persistence file.
     */
    public Braun(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = storage.load();
    }

    /**
     * Returns Braun's opening broadcast greeting for display in the GUI.
     *
     * @return opening welcome greeting string.
     */
    public String getWelcomeMessage() {
        return ui.getWelcomeMessage();
    }

    /**
     * Generates a response for the user's chat input in the GUI.
     *
     * @param input raw command string entered by the user.
     * @return Braun's theatrical broadcast response string.
     */
    public String getResponse(String input) {
        try {
            return executeCommand(input);
        } catch (BraunException e) {
            return e.getMessage();
        }
    }

    /**
     * Starts the main application loop, greeting the user and dispatching broadcast commands.
     */
    public void run() {
        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();

            if (input.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }

            try {
                processCommand(input);
            } catch (BraunException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    /**
     * Parses and dispatches a single user broadcast command for console output.
     *
     * @param input the raw input string from the user.
     * @throws BraunException if the command is unrecognized or has invalid parameters.
     */
    private void processCommand(String input) throws BraunException {
        String response = executeCommand(input);
        if (!response.isEmpty()) {
            ui.showResponse(response);
        }
    }

    /**
     * Parses and executes a user broadcast command, returning Braun's response string.
     *
     * @param input the raw input string from the user.
     * @return the response string resulting from command execution.
     * @throws BraunException if the command is unrecognized or has invalid parameters.
     */
    public String executeCommand(String input) throws BraunException {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        String lower = trimmed.toLowerCase();

        if (lower.equals(CMD_BYE)) {
            return ui.getGoodbyeMessage();
        } else if (lower.equals(CMD_LIST)) {
            return handleList();
        } else if (lower.equals(CMD_MARK) || lower.startsWith(CMD_MARK + " ")) {
            return handleMark(trimmed);
        } else if (lower.equals(CMD_UNMARK) || lower.startsWith(CMD_UNMARK + " ")) {
            return handleUnmark(trimmed);
        } else if (lower.equals(CMD_DELETE) || lower.startsWith(CMD_DELETE + " ")) {
            return handleDelete(trimmed);
        } else if (lower.equals(CMD_DATE) || lower.startsWith(CMD_DATE + " ")) {
            return handleDate(trimmed);
        } else if (lower.equals(CMD_FIND) || lower.startsWith(CMD_FIND + " ")) {
            return handleFind(trimmed);
        } else if (lower.equals(CMD_TODO) || lower.startsWith(CMD_TODO + " ")) {
            return handleTodo(trimmed);
        } else if (lower.equals(CMD_DEADLINE) || lower.startsWith(CMD_DEADLINE + " ")) {
            return handleDeadline(trimmed);
        } else if (lower.equals(CMD_EVENT) || lower.startsWith(CMD_EVENT + " ")) {
            return handleEvent(trimmed);
        } else {
            throw new BraunException("*static* Unknown broadcast command! "
                    + "Please use todo, deadline, event, list, mark, unmark, delete, find, date, or bye.");
        }
    }

    /**
     * Displays all tasks currently stored in the broadcast schedule.
     *
     * @return formatted task list response string.
     */
    private String handleList() {
        return ui.formatTaskList(tasks);
    }

    /**
     * Searches and displays all tasks occurring on a specified date.
     *
     * @param input the raw date command string.
     * @return formatted tasks on date response string.
     * @throws BraunException if the date argument is missing or invalid.
     */
    /**
     * Searches and displays all tasks occurring on a specified date.
     *
     * @param input the raw date command string.
     * @return formatted tasks on date response string.
     * @throws BraunException if the date argument is missing or invalid.
     */
    private String handleDate(String input) throws BraunException {
        String arg = input.length() > CMD_DATE.length() ? input.substring(CMD_DATE.length()).trim() : "";
        if (arg.isEmpty()) {
            throw new BraunException("*static* Please specify a date to search for (e.g. date 2026-08-30).");
        }

        LocalDate queryDate = DateTimeUtil.parseDate(arg);
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isOnDate(queryDate)) {
                matchingTasks.add(task);
            }
        }

        String formattedDate = DateTimeUtil.formatDate(queryDate);
        return ui.formatTasksOnDate(formattedDate, matchingTasks);
    }

    /**
     * Searches and displays tasks containing the specified keyword in their description.
     *
     * @param input the raw find command string.
     * @return formatted matching tasks response string.
     * @throws BraunException if the keyword argument is missing.
     */
    private String handleFind(String input) throws BraunException {
        String keyword = input.length() > CMD_FIND.length() ? input.substring(CMD_FIND.length()).trim() : "";
        if (keyword.isEmpty()) {
            throw new BraunException("*static* Please specify a keyword to search for (e.g. find book).");
        }

        String searchLower = keyword.toLowerCase();
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(searchLower)) {
                matchingTasks.add(task);
            }
        }

        return ui.formatMatchingTasks(keyword, matchingTasks);
    }

    /**
     * Extracts and validates a zero-based task index from user input.
     *
     * @param input the raw command string.
     * @param prefixLength the character length of the command prefix.
     * @param action the action name for error messages (e.g. "mark", "unmark", "delete").
     * @return the zero-based task index within {@link #tasks}.
     * @throws BraunException if the index argument is missing, not an integer, or out of bounds.
     */
    private int parseTaskIndex(String input, int prefixLength, String action) throws BraunException {
        String arg = input.length() > prefixLength ? input.substring(prefixLength).trim() : "";
        if (arg.isEmpty()) {
            throw new BraunException("*static* Please provide a valid task number to " + action + ".");
        }

        int index;
        try {
            index = Integer.parseInt(arg) - 1;
        } catch (NumberFormatException e) {
            throw new BraunException("*static* Please provide a valid task number to " + action + ".");
        }

        if (index < 0 || index >= tasks.size()) {
            throw new BraunException("*bzzzt* Invalid broadcast index! Task not found.");
        }

        return index;
    }

    /**
     * Marks a specified task as completed and persists changes to disk.
     *
     * @param input the raw mark command string.
     * @return formatted marked task response string.
     * @throws BraunException if the index is missing, not a number, out of bounds, or saving fails.
     */
    private String handleMark(String input) throws BraunException {
        int index = parseTaskIndex(input, CMD_MARK.length(), "mark");
        Task task = tasks.get(index);
        task.markAsDone();
        storage.save(tasks);

        return ui.formatMarkedTask(task);
    }

    /**
     * Marks a specified task as not completed (undone) and persists changes to disk.
     *
     * @param input the raw unmark command string.
     * @return formatted unmarked task response string.
     * @throws BraunException if the index is missing, not a number, out of bounds, or saving fails.
     */
    private String handleUnmark(String input) throws BraunException {
        int index = parseTaskIndex(input, CMD_UNMARK.length(), "unmark");
        Task task = tasks.get(index);
        task.markAsUndone();
        storage.save(tasks);

        return ui.formatUnmarkedTask(task);
    }

    /**
     * Removes a specified task from the schedule and persists changes to disk.
     *
     * @param input the raw delete command string.
     * @return formatted deleted task response string.
     * @throws BraunException if the index is missing, not a number, out of bounds, or saving fails.
     */
    private String handleDelete(String input) throws BraunException {
        int index = parseTaskIndex(input, CMD_DELETE.length(), "delete");
        Task removed = tasks.remove(index);
        storage.save(tasks);

        return ui.formatDeletedTask(removed, tasks.size());
    }

    /**
     * Validates and adds a new Todo task to the schedule and saves it to disk.
     *
     * @param input the raw todo command string.
     * @return formatted added todo response string.
     * @throws BraunException if the description is empty or saving fails.
     */
    private String handleTodo(String input) throws BraunException {
        String desc = input.length() > CMD_TODO.length() ? input.substring(CMD_TODO.length()).trim() : "";
        if (desc.isEmpty()) {
            throw new BraunException("*static* The description of a todo cannot be empty.");
        }
        return addTask(new Todo(desc), desc);
    }

    /**
     * Validates and adds a new Deadline task to the schedule and saves it to disk.
     *
     * @param input the raw deadline command string.
     * @return formatted added deadline response string.
     * @throws BraunException if the description, due time, or date format is invalid, or saving fails.
     */
    private String handleDeadline(String input) throws BraunException {
        String body = input.length() > CMD_DEADLINE.length() ? input.substring(CMD_DEADLINE.length()).trim() : "";
        int byIndex = body.toLowerCase().indexOf(DELIMITER_BY);
        if (byIndex == -1) {
            throw new BraunException("*static* Please specify deadline due time using /by <time>.");
        }

        String desc = body.substring(0, byIndex).trim();
        String by = body.substring(byIndex + DELIMITER_BY.length()).trim();
        if (desc.isEmpty() || by.isEmpty()) {
            throw new BraunException("*static* Deadline description and due time cannot be empty.");
        }

        return addTask(new Deadline(desc, by), desc);
    }

    /**
     * Validates and adds a new Event task to the schedule and saves it to disk.
     *
     * @param input the raw event command string.
     * @return formatted added event response string.
     * @throws BraunException if the description, intervals, or date formats are invalid, or saving fails.
     */
    private String handleEvent(String input) throws BraunException {
        String body = input.length() > CMD_EVENT.length() ? input.substring(CMD_EVENT.length()).trim() : "";
        int fromIndex = body.toLowerCase().indexOf(DELIMITER_FROM);
        int toIndex = body.toLowerCase().indexOf(DELIMITER_TO);
        if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
            throw new BraunException("*static* Please specify event duration using /from <start> /to <end>.");
        }

        String desc = body.substring(0, fromIndex).trim();
        String from = body.substring(fromIndex + DELIMITER_FROM.length(), toIndex).trim();
        String to = body.substring(toIndex + DELIMITER_TO.length()).trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new BraunException("*static* Event description, start time, and end time cannot be empty.");
        }

        return addTask(new Event(desc, from, to), desc);
    }

    /**
     * Stores a validated task in the list, persists changes to disk, and displays confirmation.
     *
     * @param task the task to store.
     * @param description description used for lore remark matching.
     * @return formatted task addition response string.
     * @throws BraunException if saving to disk fails.
     */
    private String addTask(Task task, String description) throws BraunException {
        tasks.add(task);
        storage.save(tasks);
        return ui.formatAddedTask(task, tasks.size(), description);
    }

    /**
     * Main program entry point.
     *
     * @param args optional command line arguments where args[0] is the storage path.
     */
    public static void main(String[] args) {
        String storagePath = (args.length > 0) ? args[0] : DEFAULT_STORAGE_PATH;
        new Braun(storagePath).run();
    }
}
