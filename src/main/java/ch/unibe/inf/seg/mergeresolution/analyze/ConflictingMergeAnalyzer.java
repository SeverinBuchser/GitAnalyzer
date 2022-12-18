package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingMerge;
import ch.unibe.inf.seg.mergeresolution.error.NotComparableMergesException;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionMerge;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ConflictingMergeAnalyzer extends Analyzer<ConflictingMerge, JSONObject> {

    private static final int CONFLICT_LIMIT = 12;

    public JSONObject analyze(ConflictingMerge conflictingMerge) {
        JSONObject result = new JSONObject();
        result.put("commit_id", conflictingMerge.getCommitId());
        result.put("conflict_count", conflictingMerge.getConflictCount());
        result.put("parent_count", conflictingMerge.getParentCount());
        boolean correct = false;
        printAnalyzing(conflictingMerge);

        try {
            if (skipConflictingMerge(conflictingMerge)) {
                result.put("state", ResultState.SKIP);
                result.put("reason", getTooManyConflictsMessage(conflictingMerge));
            } else if (conflictingMerge.getParentCount() > 2) {
                result.put("state", ResultState.SKIP);
                result.put("reason", String.format(
                        "Octopus merges will be skipped, found %d parents.",
                        conflictingMerge.getParentCount()
                ));
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

                ConflictingFilesAnalyzer conflictingFilesAnalyzer = new ConflictingFilesAnalyzer();
                ArrayList<JSONObject> files = conflictingFilesAnalyzer.analyze(conflictingMerge.getConflictingFiles());
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

        System.out.format("\tConflicting Merge: %5s: %b\n", result.get("state"), correct);
        return result;
    }


    private static boolean skipConflictingMerge(ConflictingMerge conflictingMerge) {
        return conflictingMerge.getConflictCount() > CONFLICT_LIMIT;
    }

    private static String getTooManyConflictsMessage(ConflictingMerge conflictingMerge) {
        return String.format("Too many conflicts. Found %.0f, max is %d", conflictingMerge.getConflictCount(), CONFLICT_LIMIT);
    }

    private static void printAnalyzing(ConflictingMerge conflictingMerge) {
        System.out.format("\tAnalyzing %s:\n", conflictingMerge.getCommitIdShort());
    }
}
