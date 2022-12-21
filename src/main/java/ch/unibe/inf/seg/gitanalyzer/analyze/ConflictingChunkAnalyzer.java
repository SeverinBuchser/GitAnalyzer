package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingChunk;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;

/**
 * Analyzer for a {@link ConflictingChunk}.
 * The analyzer compares the chunks to a {@link ResolutionFile} by searching the chunk in the {@link #resolutionFile}.
 * If one onf the conflicting chunks is found in the file, the result will be true and false otherwise.
 */
public class ConflictingChunkAnalyzer extends Analyzer<ConflictingChunk, Boolean> {
    private final ResolutionFile resolutionFile;

    public ConflictingChunkAnalyzer(ResolutionFile resolutionFile) {
        this.resolutionFile = resolutionFile;
    }

    /**
     * Analyzes a conflicting chunk by searching the two chunks in the {@link #resolutionFile}. If one of the chunks is
     * found, the result will be true and false otherwise.
     * @param conflictingChunk The conflicting chunk to be analyzed.
     * @return True if one of the chunks is found and false otherwise.
     */
    @Override
    public Boolean analyze(ConflictingChunk conflictingChunk) {
        printAnalyzing("chunk", 4);
        boolean correct = false;

        if (conflictingChunk.isFirstConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        } else if (conflictingChunk.isNextConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        }

        printComplete("Conflicting Chunk", 4, ResultState.OK, correct);
        return correct;
    }
}
