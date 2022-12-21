package ch.unibe.inf.seg.mergeresolution.resolution;

import org.eclipse.jgit.diff.*;

/**
 * A class representing a file.
 * The resolution file can be compared against other resolution files using the git diff algorithm by passing the
 * {@link RawText}s of the resolution files to compare. This class therefore provides a method which builds the
 * {@link RawText} object.
 */
public abstract class ResolutionFile implements Comparable<ResolutionFile> {

    /**
     * Builds the {@link RawText} for this resolution file.
     * @return The {@link RawText} of this resolution file.
     */
    public abstract RawText buildRawText();

    /**
     * Creates an {@link EditList} by calling the git diff algorithm on two resolution files.
     * @param resolution1 The first resolution file.
     * @param resolution2 The second resolution file.
     * @return An {@link EditList} of the git diff algorithm.
     */
    public static EditList diff(ResolutionFile resolution1, ResolutionFile resolution2) {
        DiffAlgorithm da = DiffAlgorithm.getAlgorithm(DiffAlgorithm.SupportedAlgorithm.MYERS);
        SequenceComparator<RawText> sc = RawTextComparator.DEFAULT;
        return da.diff(sc, resolution1.buildRawText(), resolution2.buildRawText());
    }

    /**
     * Compares two resolution files.
     * This method returns the amount of differences between the two files.
     * @param otherResolution The resolution to be compared.
     * @return The number of differences between the two files.
     */
    @Override
    public int compareTo(ResolutionFile otherResolution) {
        return ResolutionFile.diff(this, otherResolution).size();
    }
}
