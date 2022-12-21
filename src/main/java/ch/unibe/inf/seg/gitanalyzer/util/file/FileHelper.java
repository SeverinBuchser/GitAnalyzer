package ch.unibe.inf.seg.gitanalyzer.util.file;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Paths;

/**
 * Helper class for file operations.
 */
public class FileHelper {
    /**
     * Normalizes paths to current OS and transforms them to absolute path, relative to current working directory.
     * @param path The path to normalize.
     * @return The normalized path.
     */
    public static String normalizePath(String path) {
        return Paths.get(FilenameUtils.separatorsToSystem(path)).normalize().toAbsolutePath().toString();
    }
}
