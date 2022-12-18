package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingFile;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ConflictingFileAnalyzer extends Analyzer<ConflictingFile, JSONObject> {
    @Override
    public JSONObject analyze(ConflictingFile conflictingFile) {
        JSONObject result = new JSONObject();
        result.put("file_name", conflictingFile.getFileName());
        result.put("conflict_count", conflictingFile.getConflictCount());
        boolean correct = false;
        printAnalyzing(conflictingFile);

        try {
            ResolutionFile actualResolutionFile = conflictingFile.getActualResolutionFile();
            for (ResolutionFile resolutionFile: conflictingFile) {
                if (actualResolutionFile.compareTo(resolutionFile) == 0) {
                    correct = true;
                    break;
                }
            }
            result.put("state", ResultState.OK);
            result.put("correct", correct);

            ConflictingChunksAnalyzer conflictingChunksAnalyzer = new ConflictingChunksAnalyzer(actualResolutionFile);
            ArrayList<Boolean> chunks = conflictingChunksAnalyzer.analyze(conflictingFile.getConflictingChunks());
            result.put("conflicting_chunks", chunks);
            result.put("conflicting_chunks_count", chunks.size());
            result.put("conflicting_chunks_correct_count", chunks.stream().filter(c -> c!=null && c).count());
        } catch (IOException e) {
            result.put("state", ResultState.FAIL);
            result.put("reason", e.getMessage());
        }
        System.out.format("\t\tConflicting File: %5s: %b\n", result.get("state"), correct);
        return result;
    }

    private static void printAnalyzing(ConflictingFile conflictingFile) {
        System.out.format("\t\tAnalyzing %s:\n", conflictingFile.getFileNameShort());
    }
}
