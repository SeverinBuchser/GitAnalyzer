package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingFile;
import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingMerge;
import ch.unibe.inf.seg.gitanalyzer.error.NotComparableMergesException;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionMerge;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Analyzer for a {@link ConflictingMerge}.
 * The analyzer analyzes the conflicting merge by comparing its resolutions against the actual resolution. It also
 * analyzes the conflicting files of the conflicting merge by running the {@link ConflictingFilesAnalyzer} on the
 * conflicting files of the conflicting merge.
 */

public class ConflictingMergeAnalyzer extends Analyzer<ConflictingMerge, JSONObject> {

    private static final int CONFLICT_LIMIT = 12;

    private final ConflictingFilesAnalyzer subAnalyzer = new ConflictingFilesAnalyzer();

    /**
     * Analyzes a conflicting merge.
     * Each resolution of the conflicting merge is compared to the actual merge resolution. If one of the resolutions is
     * equal to the actual merge resolution, the result will be marked as correct. If none of the resolutions matches
     * the actual one, the result will be marked as incorrect. Each conflicting file of the conflicting merge will also
     * be analyzed by running the {@link ConflictingFilesAnalyzer} on the conflicting merge. The results of the files
     * analyzer will be added to the result under the key "conflicting_files".
     * @param conflictingMerge The conflicting merge to be analyzed.
     * @return The result of the analysis.
     */
    public JSONObject analyze(ConflictingMerge conflictingMerge) {
        JSONObject result = new JSONObject();
        result.put("commit_id", conflictingMerge.getCommitId());
        result.put("all_conflicting_chunks_count", conflictingMerge.getConflictCount());
        boolean correct = false;
        printAnalyzing(conflictingMerge.getCommitIdShort(), 2);

        try {
            if (checkConflictCount(conflictingMerge)) {
                result.put("state", ResultState.SKIP);
                result.put("reason", "Conflicting Merge contains binary files.");
            } else if (checkConflictLimit(conflictingMerge)) {
                result.put("state", ResultState.SKIP);
                result.put("reason", getTooManyConflictsMessage(conflictingMerge));
            } else {
                ResolutionMerge actualResolutionMerge = conflictingMerge.getActualResolution();
                for (ResolutionMerge resolutionMerge: conflictingMerge) {
                    if (actualResolutionMerge.compareTo(resolutionMerge) == 0) {
                        correct = true;
                        break;
                    }
                }
                result.put("state", ResultState.OK);
                result.put("correct", correct);

                ArrayList<JSONObject> files = this.subAnalyzer.analyze(conflictingMerge.getConflictingFiles());
                result.put("conflicting_files", files);
                result.put("conflicting_files_count", files.size());
                putCount(result, files, "conflicting_files_correct_count");
                putSum(result, files, "conflicting_chunks_count");
                putSum(result, files, "conflicting_chunks_correct_count");
            }
        } catch (IOException e) {
            result.put("state", ResultState.FAIL);
            result.put("reason", e.getMessage());
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof NotComparableMergesException) {
                result.put("state", ResultState.FAIL);
                result.put("reason", cause.getMessage());
            } else throw e;
        }

        printComplete("Conflicting Merge", 2, result);
        return result;
    }

    private static boolean checkConflictCount(ConflictingMerge conflictingMerge) {
        if (conflictingMerge.getConflictCount() == 0) return true;
        for (ConflictingFile conflictingFile: conflictingMerge.getConflictingFiles()) {
            if (conflictingFile.getConflictCount() == 0) return true;
        }
        return  false;
    }

    private static boolean checkConflictLimit(ConflictingMerge conflictingMerge) {
        return conflictingMerge.getConflictCount() > CONFLICT_LIMIT;
    }

    private static String getTooManyConflictsMessage(ConflictingMerge conflictingMerge) {
        return String.format("Too many conflicts. Found %.0f, max is %d", conflictingMerge.getConflictCount(), CONFLICT_LIMIT);
    }
}

