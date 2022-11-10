package ch.unibe.inf.seg.mergeresolution.conflict;

import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionChunk;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;

public class ConflictingChunk {

    private final ResolutionChunk firstConflictingRange;
    private final ResolutionChunk nextConflictingRange;
    ConflictingChunk(ResolutionChunk firstConflicingRange, ResolutionChunk nextConflictingRange) {
        this.firstConflictingRange = firstConflicingRange;
        this.nextConflictingRange = nextConflictingRange;
    }

    public boolean isFirstConflictingRangeInResolutionFile(ResolutionFile resolutionFile) {
        return this.isConflictingRangeInResolutionFile(this.firstConflictingRange, resolutionFile);
    }

    public boolean isNextConflictingRangeInResolutionFile(ResolutionFile resolutionFile) {
        return this.isConflictingRangeInResolutionFile(this.nextConflictingRange, resolutionFile);
    }

    private boolean isConflictingRangeInResolutionFile(ResolutionChunk conflictingRange, ResolutionFile resolutionFile) {
        String resolutionFileString = new String(resolutionFile.buildRawText().getRawContent());
        int rangeIndex = resolutionFileString.indexOf(conflictingRange.getString());
        return rangeIndex > -1;
    }
}
