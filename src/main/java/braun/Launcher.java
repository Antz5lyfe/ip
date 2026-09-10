package braun;

import javafx.application.Application;

/**
 * Entry point for the Braun chatbot application.
 * Acts as a workaround to prevent JavaFX classpath/modulepath issues when packaged as a fat JAR.
 */
public class Launcher {

    /**
     * Constructs a new {@code Launcher} instance.
     */
    public Launcher() {
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args optional command line arguments.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
