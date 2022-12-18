package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingChunk;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;

import java.util.ArrayList;

/**
 * Analyzer for an [iterable of conflicting chunks]{@link Iterable<ConflictingChunk>}. The analyzer simply analyzes each
 * conflicting chunk and returns an array of the results.
 */
public class ConflictingChunksAnalyzer extends Analyzer<Iterable<ConflictingChunk>, ArrayList<Boolean>> {

    private final ConflictingChunkAnalyzer subAnalyzer;

    ConflictingChunksAnalyzer(ResolutionFile resolutionFile) {
        this.subAnalyzer = new ConflictingChunkAnalyzer(resolutionFile);
    }

    /**
     * Analyzes an [iterable of conflicting chunks]{@link Iterable<ConflictingChunk>}. The analyzer simply analyzes each
     * conflicting chunk and returns an array of the results.
     * @param conflictingChunks The conflicting chunks to be analyzed.
     * @return An array of the results.
     */
    @Override
    public ArrayList<Boolean> analyze(Iterable<ConflictingChunk> conflictingChunks) {
        ArrayList<Boolean> chunks = new ArrayList<>();

        for (ConflictingChunk conflictingChunk : conflictingChunks) {
            chunks.add(this.subAnalyzer.analyze(conflictingChunk));
        }

        return chunks;
    }
}
