/**
 * Represents a task with a description and a completion status.
 * Provides methods to retrieve status details and toggle completion.
 */
public class Task {

    /** The description or title of the task */
    protected String description;

    /** The completion status of the task */
    protected boolean isDone;

    /**
     * Constructs a new {@code Task} with the specified description.
     * The task is initially marked as not done.
     *
     * @param description the textual description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon indicating whether the task is completed.
     *
     * @return "X" if the task is done, otherwise a single space " "
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not completed (undone).
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns the description of the task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the task is completed.
     *
     * @return {@code true} if done, {@code false} otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the formatted string representation of the task including its status icon.
     *
     * @return a formatted string such as "[X] read book" or "[ ] return book"
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
