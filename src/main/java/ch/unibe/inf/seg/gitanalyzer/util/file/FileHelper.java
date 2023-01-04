package ch.unibe.inf.seg.gitanalyzer.util.file;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helper class for file operations.
 */
public class FileHelper {

    /**
     * Normalizes a path.
     * @param path The path to normalize.
     * @return A normalized path.
     */
    public static Path normalize(String path) {
        return normalize(Paths.get(path));
    }

    /**
     * Normalizes a path.
     * @param path The path to normalize.
     * @return A normalized path.
     */
    public static Path normalize(Path path) {
        return Paths.get(FilenameUtils.separatorsToSystem(path.toString())).normalize();
    }

    /**
     * Creates an absolute path, from the supplied one.
     * @param path The path to make absolute.
     * @return An absolute path.
     * @see #toAbsolutePath(Path, Path)
     */
    public static Path toAbsolutePath(String path) {
        return toAbsolutePath(path, Paths.get("").toAbsolutePath().toString());
    }

    /**
     * Creates an absolute path, from the supplied one.
     * @param path The path to make absolute.
     * @return An absolute path.
     * @see #toAbsolutePath(Path, Path)
     */
    public static Path toAbsolutePath(Path path) {
        return toAbsolutePath(path, Paths.get("").toAbsolutePath());
    }

    /**
     * Creates an absolute path, from the supplied one.
     * @param path The path to make absolute.
     * @param relativeTo The path which the 'path' is relative to.
     * @return An absolute path.
     * @see #toAbsolutePath(Path, Path)
     */
    public static Path toAbsolutePath(String path, String relativeTo) {
        return toAbsolutePath(Paths.get(path), Paths.get(relativeTo));
    }

    /**
     * Creates an absolute path, from the supplied one.
     * @param path The path to make absolute.
     * @param relativeTo The path which the 'path' is relative to.
     * @return An absolute path.
     * @see #toAbsolutePath(Path, Path)
     */
    public static Path toAbsolutePath(String path, Path relativeTo) {
        return toAbsolutePath(Paths.get(path), relativeTo);
    }

    /**
     * Creates an absolute path, from the supplied one.
     * @param path The path to make absolute.
     * @param relativeTo The path which the 'path' is relative to.
     * @return An absolute path.
     * @see #toAbsolutePath(Path, Path)
     */
    public static Path toAbsolutePath(Path path, String relativeTo) {
        return toAbsolutePath(path, Paths.get(relativeTo));
    }

    /**
     * Creates an absolute path, from the supplied one.
     * If the 'path' is already absolute, the 'path' will be normalized and returned. If not, the 'path' is relative to
     * the 'relativeTo' path. So the 'path' needs to be an absolute path, relative to the 'relativeTo' path. Therefore,
     * the 'relativeTo' path is first tested if it is absolute. If not, it will be made absolute with a recursive call.
     * If the 'relativeTo' path is absolute, a potential file will be removed, such that there is only a directory left
     * for the 'relativeTo' path. Then the relative path 'path' and the absolute path 'relativeTo' will be resolved and
     * normalized, in order to create a fully absolute path.
     * Example:
     * 'cwd' (current working dir): '/A/B/'
     * 'relativeTo': "./C/D/E.txt" (relative)
     *      -> first to abs. with cwd: '/A/B/C/D/E.txt' (absolute)
     *      -> remove file: '/A/B/C/D/' (absolute)
     * 'path': "../F/G.txt (relative)
     * toAbsolutePath(path, relativeTo): '/A/B/C/F/G.txt'
     * @param path The path to make absolute.
     * @param relativeTo The path which the 'path' is relative to.
     * @return An absolute path.
     */
    public static Path toAbsolutePath(Path path, Path relativeTo) {
        path = normalize(path);
        if (path.isAbsolute()) {
            return Paths.get(FilenameUtils.normalize(path.toString()));
        }
        relativeTo = normalize(relativeTo);

        if (!relativeTo.isAbsolute()) {
            relativeTo = toAbsolutePath(relativeTo);
        }
        if (!FilenameUtils.getExtension(relativeTo.toString()).equals("")) {
            relativeTo = Paths.get(FilenameUtils.getPath(relativeTo.toString()));
        }
        return Paths.get("/", FilenameUtils.normalize(relativeTo.resolve(path).toString()));
    }
}
