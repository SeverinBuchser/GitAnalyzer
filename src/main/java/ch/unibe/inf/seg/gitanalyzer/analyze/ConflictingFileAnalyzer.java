package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingFile;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Analyzer for a {@link ConflictingFile}.
 * The analyzer analyzes the conflicting file by comparing its resolutions against the actual resolution. It also
 * analyzes the conflicting chunks of the conflicting file by running the {@link ConflictingChunksAnalyzer} on the
 * conflicting chunks of the conflicting file.
 */
public class ConflictingFileAnalyzer extends Analyzer<ConflictingFile, JSONObject> {
    /**
     * Analyzes a conflicting file.
     * Each resolution of the conflicting file is compared to the actual resolution file. If one of the resolutions is
     * equal to the actual resolution file, the result will be marked as correct. If none of the resolutions matches the
     * actual one, the result will be marked as incorrect. Each conflicting chunk of the conflicting file will also be
     * analyzed by running the {@link ConflictingChunksAnalyzer} on the conflicting file. The results of the chunks
     * analyzer will be added to the result under the key "conflicting_chunks".
     * @param conflictingFile The conflicting file to be analyzed.
     * @return The result of the analysis.
     */
    @Override
    public JSONObject analyze(ConflictingFile conflictingFile) {
        JSONObject result = new JSONObject();
        result.put("file_name", conflictingFile.getFileName());
        result.put("conflict_count", conflictingFile.getConflictCount());
        boolean correct = false;
        printAnalyzing(conflictingFile.getFileNameShort(), 3);

        try {

            if (checkConflictCount(conflictingFile)) {
                result.put("state", ResultState.SKIP);
                result.put("reason", "Conflicting File may be a binary file.");
            } else {
                ResolutionFile actualResolutionFile = conflictingFile.getActualResolutionFile();
                for (ResolutionFile resolutionFile : conflictingFile) {
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
                result.put("conflicting_chunks_correct_count", chunks.stream().filter(c -> c != null && c).count());
            }
        } catch (IOException e) {
            result.put("state", ResultState.FAIL);
            result.put("reason", e.getMessage());
        }
        printComplete("Conflicting File", 3, result);
        return result;
    }

    private static boolean checkConflictCount(ConflictingFile conflictingFile) {
        return conflictingFile.getConflictCount() == 0;
    }
}