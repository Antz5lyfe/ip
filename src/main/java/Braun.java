import java.util.Random;
import java.util.Scanner;

/**
 * Main entry point for the Braun chatbot application.
 * Braun is a TV-headed supernatural talk show host from 'Got Dropped into a Ghost Story, Still Gotta Work'.
 * Stores user-entered items (up to 100), lists them on demand with completion status ([X] / [ ]),
 * marks/unmarks tasks as done, and delivers contextual lore remarks or general broadcast catchphrases.
 */
public class Braun {

    private static final String DIVIDER = "    ____________________________________________________________";
    private static final String INDENT = "     ";
    private static final int MAX_ITEMS = 100;

    /** Random generator and pool of general broadcast catchphrases used when no keyword matches */
    private static final Random RANDOM = new Random();
    private static final String[] GENERAL_REMARKS = {
        "*bzzzt* Added to the broadcast schedule! The audience is already whispering...",
        "*kzzzt* Splendid choice, dear viewer! Another intriguing entry for tonight.",
        "*static hiss* Recorded on tape! Stay tuned, folks!",
        "*chime* An exciting development! Let's see how this unfolds on air.",
        "*applause track* The studio audience is leaning in with bated breath!",
        "*screen flickers* Ooh, this smells like a juicy scenario for the show!",
        "*bzzzt* Logging this entry into tonight's special feature!"
    };

    public static void main(String[] args) {
        String banner = ""
                + "    ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n"
                + "    ⠀⠀⠀⠀⠀⠀⠀⢠⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡄⠀⠀⠀⠀⠀⠀⠀\n"
                + "    ⠀⠀⠀⠀⠀⠀⠀⠀⠉⠻⣦⣄⠀⠀⠀⠀⠀⠀⣠⣴⠟⠉⠀⠀⠀⠀⠀⠀⠀⠀\n"
                + "    ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⣠⣴⣶⣶⣦⣄⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n"
                + "    ⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣉⣉⣉⣉⣉⣉⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀\n"
                + "    ⠀⠀⢸⣿⠟⠛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⠛⠛⠛⠛⢿⡿⠛⠿⣿⡇⠀⠀\n"
                + "    ⠀⠀⢸⡏⢠⣾⣿⣿⣿⣿⣿⠿⠛⠋⠉⠁⠀⠀⠀⠀⠀⠀⢸⣇⠀⠀⣽⡇⠀⠀\n"
                + "    ⠀⠀⢸⡇⢸⣿⣿⣿⠟⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⣿⡇⠀⠀\n"
                + "    ⠀⠀⢸⡇⢸⣿⡿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⣿⡇⠀⠀\n"
                + "    ⠀⠀⢸⡇⢸⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡿⠿⠿⣿⡇⠀⠀\n"
                + "    ⠀⠀⢸⡇⠸⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡷⠶⠶⣾⡇⠀⠀\n"
                + "    ⠀⠀⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡷⠶⠶⢾⡇⠀⠀\n"
                + "    ⠀⠀⢸⣿⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⣷⣶⣶⣿⡇⠀⠀\n"
                + "    ⠀⠀⠘⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠃⠀⠀\n"
                + "    ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n";

        // Greet the user with Braun's talk-show host persona
        System.out.println(DIVIDER);
        System.out.print(banner);
        System.out.println(INDENT + "*kzzzt... bzzzt!*");
        System.out.println(INDENT + "Good evening, dear guest! I'm Braun, host of the Late-Night Show.");
        System.out.println(INDENT + "What can I do for you?");
        System.out.println(DIVIDER);

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_ITEMS];
        int taskCount = 0;

        // Command processing loop
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println(DIVIDER);
                System.out.println(INDENT + "*bzzzt* That's a wrap for today's broadcast!");
                System.out.println(INDENT + "Bye. Hope to see you again soon!");
                System.out.println(DIVIDER);
                break;
            } else if (input.equalsIgnoreCase("list")) {
                System.out.println(DIVIDER);
                System.out.println(INDENT + "Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(INDENT + (i + 1) + "." + tasks[i]);
                }
                System.out.println(DIVIDER);
            } else if (input.toLowerCase().startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5).trim()) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsDone();
                        System.out.println(DIVIDER);
                        System.out.println(INDENT + "Nice! I've marked this task as done:");
                        System.out.println(INDENT + "  " + tasks[index]);
                        System.out.println(DIVIDER);
                    } else {
                        System.out.println(DIVIDER);
                        System.out.println(INDENT + "*bzzzt* Invalid broadcast index! Task not found.");
                        System.out.println(DIVIDER);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(DIVIDER);
                    System.out.println(INDENT + "*static* Please provide a valid task number to mark.");
                    System.out.println(DIVIDER);
                }
            } else if (input.toLowerCase().startsWith("unmark ")) {
                try {
                    int index = Integer.parseInt(input.substring(7).trim()) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsUndone();
                        System.out.println(DIVIDER);
                        System.out.println(INDENT + "OK, I've marked this task as not done yet:");
                        System.out.println(INDENT + "  " + tasks[index]);
                        System.out.println(DIVIDER);
                    } else {
                        System.out.println(DIVIDER);
                        System.out.println(INDENT + "*bzzzt* Invalid broadcast index! Task not found.");
                        System.out.println(DIVIDER);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(DIVIDER);
                    System.out.println(INDENT + "*static* Please provide a valid task number to unmark.");
                    System.out.println(DIVIDER);
                }
            } else if (input.toLowerCase().startsWith("todo")) {
                String desc = input.length() > 4 ? input.substring(4).trim() : "";
                if (desc.isEmpty()) {
                    System.out.println(DIVIDER);
                    System.out.println(INDENT + "*static* The description of a todo cannot be empty.");
                    System.out.println(DIVIDER);
                } else {
                    taskCount = addTask(new Todo(desc), tasks, taskCount, desc);
                }
            } else if (input.toLowerCase().startsWith("deadline")) {
                String body = input.length() > 8 ? input.substring(8).trim() : "";
                int byIndex = body.toLowerCase().indexOf("/by ");
                if (byIndex == -1) {
                    System.out.println(DIVIDER);
                    System.out.println(INDENT + "*static* Please specify deadline due time using /by <time>.");
                    System.out.println(DIVIDER);
                } else {
                    String desc = body.substring(0, byIndex).trim();
                    String by = body.substring(byIndex + 4).trim();
                    if (desc.isEmpty() || by.isEmpty()) {
                        System.out.println(DIVIDER);
                        System.out.println(INDENT + "*static* Deadline description and due time cannot be empty.");
                        System.out.println(DIVIDER);
                    } else {
                        taskCount = addTask(new Deadline(desc, by), tasks, taskCount, desc);
                    }
                }
            } else if (input.toLowerCase().startsWith("event")) {
                String body = input.length() > 5 ? input.substring(5).trim() : "";
                int fromIndex = body.toLowerCase().indexOf("/from ");
                int toIndex = body.toLowerCase().indexOf("/to ");
                if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
                    System.out.println(DIVIDER);
                    System.out.println(INDENT + "*static* Please specify event duration using /from <start> /to <end>.");
                    System.out.println(DIVIDER);
                } else {
                    String desc = body.substring(0, fromIndex).trim();
                    String from = body.substring(fromIndex + 6, toIndex).trim();
                    String to = body.substring(toIndex + 4).trim();
                    if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        System.out.println(DIVIDER);
                        System.out.println(INDENT + "*static* Event description, start time, and end time cannot be empty.");
                        System.out.println(DIVIDER);
                    } else {
                        taskCount = addTask(new Event(desc, from, to), tasks, taskCount, desc);
                    }
                }
            } else {
                System.out.println(DIVIDER);
                System.out.println(INDENT + "*static* Unknown broadcast command! Please use todo, deadline, event, list, mark, unmark, or bye.");
                System.out.println(DIVIDER);
            }
        }

        scanner.close();
    }

    /**
     * Helper method to store a task and print standard addition confirmation.
     *
     * @param task the task to store
     * @param tasks the storage array
     * @param taskCount current count of stored tasks
     * @param description description used for lore remark matching
     * @return updated task count
     */
    private static int addTask(Task task, Task[] tasks, int taskCount, String description) {
        if (taskCount < MAX_ITEMS) {
            tasks[taskCount] = task;
            taskCount++;
            System.out.println(DIVIDER);
            System.out.println(INDENT + "Got it. I've added this task:");
            System.out.println(INDENT + "  " + task);
            System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
            System.out.println(INDENT + getRemarkForTask(description));
            System.out.println(DIVIDER);
            return taskCount;
        } else {
            System.out.println(DIVIDER);
            System.out.println(INDENT + "*static* The broadcast log is full! Cannot store more than " + MAX_ITEMS + " entries.");
            System.out.println(DIVIDER);
            return taskCount;
        }
    }

    /**
     * Determines the appropriate remark for an entered task.
     * Scans for thematic keywords to provide lore-specific dialogue,
     * or falls back to a random general broadcast remark.
     *
     * @param task the text entered by the user
     * @return a thematic or randomized talk-show host remark
     */
    private static String getRemarkForTask(String task) {
        String lower = task.toLowerCase();

        if (lower.contains("ghost") || lower.contains("anomaly") || lower.contains("monster") || lower.contains("entity")) {
            return "*screen flickers* A new anomaly! Daydream Inc. will want this documented.";
        } else if (lower.contains("work") || lower.contains("deadline") || lower.contains("report") || lower.contains("salary")) {
            return "*chime* Even trapped in a ghost story, we still gotta work, don't we?";
        } else if (lower.contains("explore") || lower.contains("investigate") || lower.contains("record")) {
            return "*static hiss* Watch your step, Field Explorer! Duly noted in the Dark Exploration Records.";
        } else if (lower.contains("rabbit") || lower.contains("doll") || lower.contains("plush")) {
            return "*bzzzt* Reminds me of a certain charming pink rabbit doll, doesn't it?";
        } else if (lower.contains("ticket") || lower.contains("wish") || lower.contains("survive")) {
            return "*applause track* Keep racking up those points for your Wish Ticket home!";
        } else if (lower.contains("soleum") || lower.contains("roe deer") || lower.contains("kim")) {
            return "*screen flashes bright* Ah, our favorite explorer! Keep your wits sharp!";
        }

        return getRandomGeneralRemark();
    }

    /**
     * Selects a random catchphrase from Braun's general broadcast remark pool.
     *
     * @return a random talk-show host remark string
     */
    private static String getRandomGeneralRemark() {
        return GENERAL_REMARKS[RANDOM.nextInt(GENERAL_REMARKS.length)];
    }
}
