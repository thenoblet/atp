package gtp.atp.service;

import java.io.*;
import java.time.LocalDateTime;


public class LoggerService {
    private final String logFilePath;

    public LoggerService(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void log(String message) {
        String logEntry = LocalDateTime.now() + ": " + message;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to log: " + e.getMessage());
        }
    }
}