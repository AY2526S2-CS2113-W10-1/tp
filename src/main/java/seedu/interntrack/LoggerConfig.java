package seedu.interntrack;
import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig {

    public static void setup() {
        try {
            // Get the root logger
            Logger rootLogger = Logger.getLogger("");

            // Remove default console handlers
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            // Create file handler
            FileHandler fileHandler = new FileHandler("data/app.log", true); // append
            fileHandler.setLevel(Level.ALL);

            // Set format (readable logs)
            fileHandler.setFormatter(new SimpleFormatter());

            // Add to root logger
            rootLogger.addHandler(fileHandler);

            // Set global log level
            rootLogger.setLevel(Level.ALL);

        } catch (IOException e) {
            System.out.println("The logger setup is failed, it may not log properly");
        }
    }
}