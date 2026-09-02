/**
 * Represents a deadline task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {

    /** The deadline date/time string */
    protected String by;

    /**
     * Constructs a new {@code Deadline} task with the specified description and due time.
     *
     * @param description the textual description of the deadline task
     * @param by the date/time string by which the task is due
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date/time of the deadline.
     *
     * @return the due date/time string
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns the formatted string representation of the deadline task,
     * prefixed with the [D] identifier tag and appending the due time.
     *
     * @return formatted string such as "[D][ ] return book (by: June 6th)"
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns the formatted string representation of the deadline task for file storage.
     *
     * @return a pipe-delimited string prefixed with the 'D' tag and containing the due time
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by;
    }
}

