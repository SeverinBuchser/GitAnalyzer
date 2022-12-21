package ch.unibe.inf.seg.gitanalyzer.resolution;

import ch.unibe.inf.seg.gitanalyzer.error.NotComparableMergesException;
import ch.unibe.inf.seg.gitanalyzer.util.path.Path;
import org.eclipse.jgit.diff.EditList;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A resolution merge represents a resolved conflicting merge.
 * The resolution merge contains a path of resolution files or can be built by gradually adding new resolution files.
 * The resolution merge can be compared to other resolution merges by calling the compare method on each individual file.
 * If the two resolutions to not have the same files, the resolutions are considered to be incomparable.
 */
public class ResolutionMerge extends Path<ResolutionFile> implements Comparable<ResolutionMerge> {

    /**
     * Initiates an empty resolution merge.
     */
    public ResolutionMerge() {
        super();
    }

    /**
     * Initiates a resolution merge from a path of resolution files.
     * @param path The path of resolution files.
     */
    public ResolutionMerge(Path<ResolutionFile> path) {
        super(path);
    }

    /**
     * Creates a list of edit lists. Each edit list belongs to one resolution file.
     * @param resolutionMerge1 The first resolution merge.
     * @param resolutionMerge2 The second resolution merge.
     * @return A list of edit lists.
     * @throws NotComparableMergesException The resolution merges do not contain the same resolution files.
     */
    public static ArrayList<EditList> diff(
            ResolutionMerge resolutionMerge1,
            ResolutionMerge resolutionMerge2
    ) throws NotComparableMergesException {
        ArrayList<EditList> edits = new ArrayList<>();

        Iterator<ResolutionFile> iterator1 = resolutionMerge1.iterator();
        Iterator<ResolutionFile> iterator2 = resolutionMerge2.iterator();

        while (iterator1.hasNext() && iterator2.hasNext()) {
            edits.add(ResolutionFile.diff(iterator1.next(), iterator2.next()));
        }

        if (iterator1.hasNext() || iterator2.hasNext()) {
            throw new NotComparableMergesException();
        }

        return edits;
    }

    /**
     * Compares two resolution merges.
     * If the merges do not contain the same resolution files a runtime exception will be thrown.
     * @param otherResolutionMerge The resolution merge to compare.
     * @return The resolutions do not contain the same resolution files.
     */
    @Override
    public int compareTo(ResolutionMerge otherResolutionMerge) {
        try {
            ArrayList<EditList> edits = ResolutionMerge.diff(this, otherResolutionMerge);
            int editCount = 0;

            for (EditList edit: edits) {
                editCount += edit.size();
            }

            return editCount;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
