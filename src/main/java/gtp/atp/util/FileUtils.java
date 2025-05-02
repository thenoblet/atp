package gtp.atp.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public final class FileUtils {
    /**
     * Reads all lines from a file.
     * Suitable for small to medium files.
     */
    public List<String> readFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.notExists(filePath)) {
            throw new NoSuchFileException("File does not exist: " + path);
        }
        return Files.readAllLines(filePath);
    }

    /**
     * Reads a file using a Stream (better for large files).
     * Caller must close the stream (or use try-with-resources).
     */
    public Stream<String> streamFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.notExists(filePath)) {
            throw new NoSuchFileException("File does not exist: " + path);
        }
        return Files.lines(filePath);
    }

    /**
     * Writes lines to a file. Overwrites existing content.
     * Creates file if it doesn't exist.
     */
    public void writeFile(String path, List<String> lines) throws IOException {
        Path filePath = Paths.get(path);
        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Appends a line to the file. Creates file if it doesn't exist.
     */
    public void appendToFile(String path, String content) throws IOException {
        Path filePath = Paths.get(path);
        Files.write(
                filePath,
                Collections.singletonList(content),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    /**
     * Deletes a file if it exists.
     */
    public void deleteFile(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

    /**
     * Checks whether the file exists.
     */
    public boolean fileExists(String path) {
        return Files.exists(Paths.get(path));
    }
}
