package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingChunk;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;

public class ConflictingChunkAnalyzer extends Analyzer<ConflictingChunk, Boolean> {
    private final ResolutionFile resolutionFile;

    public ConflictingChunkAnalyzer(ResolutionFile resolutionFile) {
        this.resolutionFile = resolutionFile;
    }
    
    @Override
    public Boolean analyze(ConflictingChunk conflictingChunk) {
        printAnalyzing();
        boolean correct = false;

        if (conflictingChunk.isFirstConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        } else if (conflictingChunk.isNextConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        }

        System.out.format("\t\t\tConflicting Chunk: %5s: %b\n", "OK", correct);
        return correct;
    }

    private static void printAnalyzing() {
        System.out.println("\t\t\tAnalyzing chunk");
    }
}
