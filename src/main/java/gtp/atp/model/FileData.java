package gtp.atp.model;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents metadata and content of a loaded text file.
 * Holds file information including name, size, path, and contents as lines of text.
 *
 * @param fileSize in bytes
 */
public record FileData(String fileName, long fileSize, Path filePath, Stream<String> fileContent) {
    /**
     * Constructs a TextFileData object with file metadata and content.
     *
     * @param fileName    the name of the file
     * @param fileSize    the size of the file in bytes
     * @param filePath    the path to the file
     * @param fileContent the content of the file as lines of text
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public FileData(String fileName, long fileSize, Path filePath, Stream<String> fileContent) {
        this.fileName = Objects.requireNonNull(fileName, "File name cannot be null");
        this.fileSize = validateFileSize(fileSize);
        this.filePath = Objects.requireNonNull(filePath, "File path cannot be null");
        this.fileContent = Objects.requireNonNull(fileContent, "File content cannot be null");
    }

    /**
     * Validates that file size is not negative.
     *
     * @param size the file size to validate
     * @return the validated size
     * @throws IllegalArgumentException if size is negative
     */
    private long validateFileSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        return size;
    }


    /**
     * Gets the number of lines in the file.
     *
     * @return the line count
     */
    public int getLineCount() {
        return Math.toIntExact(fileContent.count());
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public Path getFilePath() {
        return filePath;
    }

    /**
     * Gets the file extension in lowercase.
     *
     * @return the file extension or empty string if none
     */
    public String getFileExtension() {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1).toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileData that = (FileData) o;
        return fileSize == that.fileSize &&
                fileName.equals(that.fileName) &&
                filePath.equals(that.filePath) &&
                fileContent.equals(that.fileContent);
    }

    @Override
    public String toString() {
        return "TextFileData{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", filePath=" + filePath +
                ", lineCount=" + getLineCount() +
                '}';
    }

    /**
     * Creates a TextFileData object from a File object and content.
     *
     * @param file    the file to represent
     * @param content the content of the file
     * @return a new TextFileData instance
     */
    public static FileData fromFile(File file, Stream<String> content) {
        return new FileData(
                file.getName(),
                file.length(),
                file.toPath(),
                content
        );
    }
}