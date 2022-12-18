package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingFile;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConflictingFilesAnalyzer extends Analyzer<Iterable<ConflictingFile>, ArrayList<JSONObject>> {

    private final ConflictingFileAnalyzer subAnalyzer;

    ConflictingFilesAnalyzer() {
        this.subAnalyzer = new ConflictingFileAnalyzer();
    }

    @Override
    public ArrayList<JSONObject> analyze(Iterable<ConflictingFile> conflictingFiles) {
        ArrayList<JSONObject> files = new ArrayList<>();

        for (ConflictingFile conflictingFile: conflictingFiles) {
            files.add(this.subAnalyzer.analyze(conflictingFile));
        }

        return files;
    }
}
