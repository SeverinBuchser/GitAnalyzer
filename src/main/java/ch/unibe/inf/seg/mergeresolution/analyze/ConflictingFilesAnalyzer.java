package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingFile;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Analyzer for an [iterable of conflicting files]{@link Iterable<ConflictingFile>}. The analyzer simply analyzes each
 * conflicting file and returns an array of the results.
 */
public class ConflictingFilesAnalyzer extends Analyzer<Iterable<ConflictingFile>, ArrayList<JSONObject>> {

    private final ConflictingFileAnalyzer subAnalyzer = new ConflictingFileAnalyzer();

    /**
     * Analyzes an [iterable of conflicting files]{@link Iterable<ConflictingFile>}. The analyzer simply analyzes each
     * conflicting file and returns an array of the results.
     * @param conflictingFiles The conflicting files to be analyzed.
     * @return An array of the results.
     */
    @Override
    public ArrayList<JSONObject> analyze(Iterable<ConflictingFile> conflictingFiles) {
        ArrayList<JSONObject> results = new ArrayList<>();

        for (ConflictingFile conflictingFile: conflictingFiles) {
            results.add(this.subAnalyzer.analyze(conflictingFile));
        }

        return results;
    }
}
