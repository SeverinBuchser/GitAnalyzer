package ch.unibe.inf.seg.gitanalyzer.conflict;

import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionChunk;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;

/**
 * A datastructure representing a conflicting chunk.
 * The conflicting chunk consists out of the first and second conflicting chunks. The datastructure provides some
 * functionality to search the chunks in a resolution file.
 */
public class ConflictingChunk {

    private final ResolutionChunk firstConflictingRange;
    private final ResolutionChunk nextConflictingRange;
    ConflictingChunk(ResolutionChunk firstConflictingRange, ResolutionChunk nextConflictingRange) {
        this.firstConflictingRange = firstConflictingRange;
        this.nextConflictingRange = nextConflictingRange;
    }

    /**
     * Checks if the first conflicting chunk is present in a resolution file.
     * @param resolutionFile The resolution file to search in.
     * @return True if the chunk is found in the resolution file and false otherwise.
     */
    public boolean isFirstConflictingRangeInResolutionFile(ResolutionFile resolutionFile) {
        return this.isConflictingRangeInResolutionFile(this.firstConflictingRange, resolutionFile);
    }

    /**
     * Checks if the second conflicting chunk is present in a resolution file.
     * @param resolutionFile The resolution file to search in.
     * @return True if the chunk is found in the resolution file and false otherwise.
     */
    public boolean isNextConflictingRangeInResolutionFile(ResolutionFile resolutionFile) {
        return this.isConflictingRangeInResolutionFile(this.nextConflictingRange, resolutionFile);
    }

    /**
     * Searches for a conflicting chunk in a file resolution.
     * If the text of the conflicting chunk is found within the file resolution, true will be returned and false
     * otherwise.
     * @param conflictingChunk The conflicting chunk to search.
     * @param resolutionFile The resolution file in which to search.
     * @return True if the chunk is found and false otherwise.
     */
    private boolean isConflictingRangeInResolutionFile(ResolutionChunk conflictingChunk, ResolutionFile resolutionFile) {
        String resolutionFileString = new String(resolutionFile.buildRawText().getRawContent());
        int rangeIndex = resolutionFileString.indexOf(conflictingChunk.getString());
        return rangeIndex > -1;
    }
}
