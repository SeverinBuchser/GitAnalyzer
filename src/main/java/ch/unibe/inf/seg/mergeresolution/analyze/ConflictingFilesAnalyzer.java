package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingFile;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConflictingFilesAnalyzer extends Analyzer<Iterable<ConflictingFile>, ArrayList<JSONObject>> {

    private final ConflictingFileAnalyzer subAnalyzer = new ConflictingFileAnalyzer();

    @Override
    public ArrayList<JSONObject> analyze(Iterable<ConflictingFile> conflictingFiles) {
        ArrayList<JSONObject> results = new ArrayList<>();

        for (ConflictingFile conflictingFile: conflictingFiles) {
            results.add(this.subAnalyzer.analyze(conflictingFile));
        }

        return results;
    }
}
