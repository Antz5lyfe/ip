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

    private final Storage storage;
    private final Ui ui;
    private final ArrayList<Task> tasks;

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
     * Parses and dispatches a single user broadcast command.
     *
     * @param input the raw input string from the user.
     * @throws BraunException if the command is unrecognized or has invalid parameters.
     */
    private void processCommand(String input) throws BraunException {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return;
        }

        String lower = trimmed.toLowerCase();

        if (lower.equals("list")) {
            handleList();
        } else if (lower.equals("mark") || lower.startsWith("mark ")) {
            handleMark(trimmed);
        } else if (lower.equals("unmark") || lower.startsWith("unmark ")) {
            handleUnmark(trimmed);
        } else if (lower.equals("delete") || lower.startsWith("delete ")) {
            handleDelete(trimmed);
        } else if (lower.equals("date") || lower.startsWith("date ")) {
            handleDate(trimmed);
        } else if (lower.equals("find") || lower.startsWith("find ")) {
            handleFind(trimmed);
        } else if (lower.equals("todo") || lower.startsWith("todo ")) {
            handleTodo(trimmed);
        } else if (lower.equals("deadline") || lower.startsWith("deadline ")) {
            handleDeadline(trimmed);
        } else if (lower.equals("event") || lower.startsWith("event ")) {
            handleEvent(trimmed);
        } else {
            throw new BraunException("*static* Unknown broadcast command! "
                    + "Please use todo, deadline, event, list, mark, unmark, delete, find, date, or bye.");
        }
    }

    /**
     * Displays all tasks currently stored in the broadcast schedule.
     */
    private void handleList() {
        ui.showTaskList(tasks);
    }

    /**
     * Searches and displays all tasks occurring on a specified date.
     *
     * @param input the raw date command string.
     * @throws BraunException if the date argument is missing or invalid.
     */
    private void handleDate(String input) throws BraunException {
        String arg = input.length() > 4 ? input.substring(4).trim() : "";
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
        ui.showTasksOnDate(formattedDate, matchingTasks);
    }

    /**
     * Searches and displays tasks containing the specified keyword in their description.
     *
     * @param input the raw find command string.
     * @throws BraunException if the keyword argument is missing.
     */
    private void handleFind(String input) throws BraunException {
        String keyword = input.length() > 4 ? input.substring(4).trim() : "";
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

        ui.showMatchingTasks(keyword, matchingTasks);
    }

    /**
     * Marks a specified task as completed and persists changes to disk.
     *
     * @param input the raw mark command string.
     * @throws BraunException if the index is missing, not a number, out of bounds, or saving fails.
     */
    private void handleMark(String input) throws BraunException {
        String arg = input.length() > 4 ? input.substring(4).trim() : "";
        if (arg.isEmpty()) {
            throw new BraunException("*static* Please provide a valid task number to mark.");
        }

        int index;
        try {
            index = Integer.parseInt(arg) - 1;
        } catch (NumberFormatException e) {
            throw new BraunException("*static* Please provide a valid task number to mark.");
        }

        if (index < 0 || index >= tasks.size()) {
            throw new BraunException("*bzzzt* Invalid broadcast index! Task not found.");
        }

        Task task = tasks.get(index);
        task.markAsDone();
        storage.save(tasks);

        ui.showMarkedTask(task);
    }

    /**
     * Marks a specified task as not completed (undone) and persists changes to disk.
     *
     * @param input the raw unmark command string.
     * @throws BraunException if the index is missing, not a number, out of bounds, or saving fails.
     */
    private void handleUnmark(String input) throws BraunException {
        String arg = input.length() > 6 ? input.substring(6).trim() : "";
        if (arg.isEmpty()) {
            throw new BraunException("*static* Please provide a valid task number to unmark.");
        }

        int index;
        try {
            index = Integer.parseInt(arg) - 1;
        } catch (NumberFormatException e) {
            throw new BraunException("*static* Please provide a valid task number to unmark.");
        }

        if (index < 0 || index >= tasks.size()) {
            throw new BraunException("*bzzzt* Invalid broadcast index! Task not found.");
        }

        Task task = tasks.get(index);
        task.markAsUndone();
        storage.save(tasks);

        ui.showUnmarkedTask(task);
    }

    /**
     * Removes a specified task from the schedule and persists changes to disk.
     *
     * @param input the raw delete command string.
     * @throws BraunException if the index is missing, not a number, out of bounds, or saving fails.
     */
    private void handleDelete(String input) throws BraunException {
        String arg = input.length() > 6 ? input.substring(6).trim() : "";
        if (arg.isEmpty()) {
            throw new BraunException("*static* Please provide a valid task number to delete.");
        }

        int index;
        try {
            index = Integer.parseInt(arg) - 1;
        } catch (NumberFormatException e) {
            throw new BraunException("*static* Please provide a valid task number to delete.");
        }

        if (index < 0 || index >= tasks.size()) {
            throw new BraunException("*bzzzt* Invalid broadcast index! Task not found.");
        }

        Task removed = tasks.remove(index);
        storage.save(tasks);

        ui.showDeletedTask(removed, tasks.size());
    }

    /**
     * Validates and adds a new Todo task to the schedule and saves it to disk.
     *
     * @param input the raw todo command string.
     * @throws BraunException if the description is empty or saving fails.
     */
    private void handleTodo(String input) throws BraunException {
        String desc = input.length() > 4 ? input.substring(4).trim() : "";
        if (desc.isEmpty()) {
            throw new BraunException("*static* The description of a todo cannot be empty.");
        }
        addTask(new Todo(desc), desc);
    }

    /**
     * Validates and adds a new Deadline task to the schedule and saves it to disk.
     *
     * @param input the raw deadline command string.
     * @throws BraunException if the description, due time, or date format is invalid, or saving fails.
     */
    private void handleDeadline(String input) throws BraunException {
        String body = input.length() > 8 ? input.substring(8).trim() : "";
        int byIndex = body.toLowerCase().indexOf("/by ");
        if (byIndex == -1) {
            throw new BraunException("*static* Please specify deadline due time using /by <time>.");
        }

        String desc = body.substring(0, byIndex).trim();
        String by = body.substring(byIndex + 4).trim();
        if (desc.isEmpty() || by.isEmpty()) {
            throw new BraunException("*static* Deadline description and due time cannot be empty.");
        }

        addTask(new Deadline(desc, by), desc);
    }

    /**
     * Validates and adds a new Event task to the schedule and saves it to disk.
     *
     * @param input the raw event command string.
     * @throws BraunException if the description, intervals, or date formats are invalid, or saving fails.
     */
    private void handleEvent(String input) throws BraunException {
        String body = input.length() > 5 ? input.substring(5).trim() : "";
        int fromIndex = body.toLowerCase().indexOf("/from ");
        int toIndex = body.toLowerCase().indexOf("/to ");
        if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
            throw new BraunException("*static* Please specify event duration using /from <start> /to <end>.");
        }

        String desc = body.substring(0, fromIndex).trim();
        String from = body.substring(fromIndex + 6, toIndex).trim();
        String to = body.substring(toIndex + 4).trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new BraunException("*static* Event description, start time, and end time cannot be empty.");
        }

        addTask(new Event(desc, from, to), desc);
    }

    /**
     * Stores a validated task in the list, persists changes to disk, and displays confirmation.
     *
     * @param task the task to store.
     * @param description description used for lore remark matching.
     * @throws BraunException if saving to disk fails.
     */
    private void addTask(Task task, String description) throws BraunException {
        tasks.add(task);
        storage.save(tasks);
        ui.showAddedTask(task, tasks.size(), description);
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
