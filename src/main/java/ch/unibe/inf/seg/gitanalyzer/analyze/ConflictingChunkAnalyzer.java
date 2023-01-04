package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingChunk;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;

public class ConflictingChunkAnalyzer implements Analyzer<ConflictingChunk, Boolean> {
    private final ResolutionFile resolutionFile;

    public ConflictingChunkAnalyzer(ResolutionFile resolutionFile) {
        this.resolutionFile = resolutionFile;
    }

    @Override
    public Boolean analyze(ConflictingChunk conflictingChunk) {
        System.out.format("\t\t\t\tIn Progress: %s\n", "cc");
        boolean correct = false;

        if (conflictingChunk.isFirstConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        } else if (conflictingChunk.isNextConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        }

        System.out.format("\t\t\t\t%s: %b\n", "cc", correct);
        return correct;
    }
}
