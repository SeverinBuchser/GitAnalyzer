package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingChunk;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;

import java.util.ArrayList;

public class ConflictingChunksAnalyzer extends Analyzer<Iterable<ConflictingChunk>, ArrayList<Boolean>> {

    private final ConflictingChunkAnalyzer subAnalyzer;

    ConflictingChunksAnalyzer(ResolutionFile resolutionFile) {
        this.subAnalyzer = new ConflictingChunkAnalyzer(resolutionFile);
    }

    @Override
    public ArrayList<Boolean> analyze(Iterable<ConflictingChunk> conflictingChunks) {
        ArrayList<Boolean> chunks = new ArrayList<>();

        for (ConflictingChunk conflictingChunk : conflictingChunks) {
            chunks.add(this.subAnalyzer.analyze(conflictingChunk));
        }

        return chunks;
    }
}
