package gtp.atp.service;

import gtp.atp.model.FileData;
import gtp.atp.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Service class for handling file operations including reading file metadata and content.
 * Provides methods to retrieve file information as a stream for efficient memory usage.
 */
public class FileService {
    private final FileUtils fileUtils;

    /**
     * Constructs a new FileService with default FileUtils instance.
     */
    public FileService() {
        this.fileUtils = new FileUtils();
    }

    /**
     * Retrieves file data including metadata and content stream for the specified file path.
     * The content is provided as a Stream<String> for memory-efficient processing of large files.
     *
     * @param pathString the path to the file as a string (cannot be null or empty)
     * @return FileData object containing:
     *         - File name
     *         - File size in bytes
     *         - File path
     *         - Content as Stream<String>
     * @throws NoSuchFileException if the specified file does not exist
     * @throws IOException if an I/O error occurs reading the file
     * @throws SecurityException if access to the file is denied
     * @throws IllegalArgumentException if pathString is null or empty
     *
     * @example
     * // Get data for existing file
     * FileData data = fileService.getFileData("data/largefile.txt");
     * try (Stream<String> lines = data.getContent()) {
     *     lines.forEach(System.out::println);
     * }
     */
    public FileData getFileData(String pathString) throws IOException {
        if (pathString == null || pathString.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }

        Path path = Paths.get(pathString);

        if (!fileUtils.fileExists(pathString)) {
            throw new NoSuchFileException("File does not exist: " + pathString);
        }

        String fileName = path.getFileName().toString();
        long fileSize = Files.size(path);
        Stream<String> fileContent = fileUtils.streamFile(pathString);

        return new FileData(fileName, fileSize, path, fileContent);
    }
}