package ch.unibe.inf.seg.mergeresolution.conflict;

import ch.unibe.inf.seg.mergeresolution.resolution.Resolution;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;
import ch.unibe.inf.seg.mergeresolution.util.path.PathBuilder;
import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.merge.MergeResult;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConflictingMerge {
    protected final RecursiveMerger merger;
    protected final RevCommit commit;

    private final Map<String, ConflictingFile> conflictingFiles;

    public Map<String, ConflictingFile> getConflictingFiles() {
        return this.conflictingFiles;
    }

    public ConflictingMerge(RevCommit commit, RecursiveMerger merger) {
        this.commit = commit;
        this.merger = merger;
        this.conflictingFiles = this.findConflictingFiles();
    }

    public Map<String, ConflictingFile> findConflictingFiles() {
        Map<String, ConflictingFile> conflictingFiles = new HashMap<>();
        Map<String, MergeResult<? extends  Sequence>> mergeResults = this.merger.getMergeResults();

        if (mergeResults.keySet().size() > 5) return new HashMap<>();
        for (String fileName: mergeResults.keySet()) {
            ConflictingFile conflictingFile = new ConflictingFile(
                    this.commit,
                    this.merger,
                    fileName,
                    mergeResults.get(fileName)
            );

            conflictingFiles.put(fileName, conflictingFile);
        }
        return conflictingFiles;
    }

    public PathBuilder<ResolutionFile> buildResolutions() {
        PathBuilder<ResolutionFile> paths = new PathBuilder<>();
        Map<String, MergeResult<? extends  Sequence>> mergeResults = this.merger.getMergeResults();
        if (mergeResults.keySet().size() > 5) return null;
        for (String fileName: mergeResults.keySet()) {
            ConflictingFile conflictingFile = new ConflictingFile(
                    this.commit,
                    this.merger,
                    fileName,
                    mergeResults.get(fileName)
            );
            ArrayList<ResolutionFile> conflictingFiles = conflictingFile.getResolutionFiles();
            if (conflictingFiles == null) return null;
            paths.addItemLayer(conflictingFiles);
        }
        return paths;
    }

    public Map<String, ArrayList<ResolutionFile>> buildFileResolutions() {
        Map<String, ArrayList<ResolutionFile>> allFileResolutions = new HashMap<>();

        for (String fileName: this.conflictingFiles.keySet()) {
            ArrayList<ResolutionFile> resolutionFiles = this.conflictingFiles.get(fileName).getResolutionFiles();
            if (resolutionFiles == null) return null;
            allFileResolutions.put(fileName, resolutionFiles);
        }
        return allFileResolutions;
    }

    public Resolution getActualResolution() throws IOException {
        ArrayList<ResolutionFile> resolutionFiles = new ArrayList<>();

        for (ConflictingFile conflictingFile: this.conflictingFiles.values()) {
            resolutionFiles.add(conflictingFile.getActualResolutionFile());
        }

        return new Resolution(resolutionFiles);
    }

    public Map<String, ResolutionFile> getActualFileResolutions() throws IOException {
        Map<String, ResolutionFile> fileResolutions = new HashMap<>();

        for (ConflictingFile conflictingFile: this.conflictingFiles.values()) {
            fileResolutions.put(conflictingFile.getFileName(), conflictingFile.getActualResolutionFile());
        }

        return fileResolutions;
    }

    public String getCommitName() {
        return this.commit.getName();
    }
}
