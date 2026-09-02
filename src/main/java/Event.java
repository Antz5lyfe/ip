/**
 * Represents an event task that starts at a specific date/time and ends at a specific date/time.
 */
public class Event extends Task {

    /** The start date/time string of the event */
    protected String from;

    /** The end date/time string of the event */
    protected String to;

    /**
     * Constructs a new {@code Event} task with the specified description, start time, and end time.
     *
     * @param description the textual description of the event
     * @param from the start date/time of the event
     * @param to the end date/time of the event
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date/time of the event.
     *
     * @return the start date/time string
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the end date/time of the event.
     *
     * @return the end date/time string
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the formatted string representation of the event task,
     * prefixed with the [E] identifier tag and appending start and end times.
     *
     * @return formatted string such as "[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)"
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns the formatted string representation of the event task for file storage.
     *
     * @return a pipe-delimited string prefixed with the 'E' tag and containing start and end times
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from + " | " + to;
    }
}

