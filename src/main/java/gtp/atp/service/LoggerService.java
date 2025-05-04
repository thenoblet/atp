package gtp.atp.service;

import gtp.atp.ui.TextProcessingApp;
import gtp.atp.util.ColorConsoleFormatter;

import java.io.*;
import java.util.logging.*;


public class LoggerService {
    public static final Logger LOGGER = Logger.getLogger(TextProcessingApp.class.getName());

    /**
     * Configures logging format, console coloring, and file logging.
     */
    public static void configureLogging() {
        try {
            System.setProperty("java.util.logging.SimpleFormatter.format\n",
                    "%1$tY-%m-%d %1$tH:%1$tM:%1$tS.%1$tL %4$s [%2$s] %5$s%6$s%n");

            Logger appLogger = Logger.getLogger("gtp.ems");
            appLogger.setUseParentHandlers(false); // Don't inherit root handlers

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new ColorConsoleFormatter());
            appLogger.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("ems-app.log", false);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            appLogger.addHandler(fileHandler);

            appLogger.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println("Failed to configure logging: " + e.getMessage());
        }
    }
}