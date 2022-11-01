package ch.unibe.inf.seg.mergeresolution.conflict;

import ch.unibe.inf.seg.mergeresolution.resolution.FileResolution;
import ch.unibe.inf.seg.mergeresolution.resolution.Resolution;
import ch.unibe.inf.seg.mergeresolution.resolution.StaticFileResolution;
import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.merge.MergeResult;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import ch.unibe.inf.seg.mergeresolution.util.path.PathBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conflict {
    protected final RecursiveMerger merger;

    protected final RevCommit commit;

    public Conflict(RevCommit commit, RecursiveMerger merger) {
        this.commit = commit;
        this.merger = merger;
    }

    public PathBuilder<FileResolution> buildResolutions() {
        PathBuilder<FileResolution> paths = new PathBuilder<>();
        Map<String, MergeResult<? extends  Sequence>> mergeResults = this.merger.getMergeResults();
        if (mergeResults.keySet().size() > 5) return null;
        for (String fileName: mergeResults.keySet()) {
            ConflictFile conflictingFile = new ConflictFile(
                    fileName,
                    mergeResults.get(fileName)
            );
            ArrayList<FileResolution> conflictingFiles = conflictingFile.getResolutions();
            if (conflictingFiles == null) return null;
            paths.addItemLayer(conflictingFiles);
        }
        return paths;
    }

    public Map<String, ArrayList<FileResolution>> buildFileResolutions() {
        Map<String, ArrayList<FileResolution>> fileResolutions = new HashMap<>();
        Map<String, MergeResult<? extends  Sequence>> mergeResults = this.merger.getMergeResults();
        if (mergeResults.keySet().size() > 5) return null;
        for (String fileName: mergeResults.keySet()) {
            ConflictFile conflictingFile = new ConflictFile(
                    fileName,
                    mergeResults.get(fileName)
            );
            ArrayList<FileResolution> conflictingFiles = conflictingFile.getResolutions();
            if (conflictingFiles == null) return null;
            fileResolutions.put(fileName, conflictingFiles);
        }
        return fileResolutions;
    }

    public Resolution getActualResolution() throws IOException {
        ArrayList<FileResolution> fileResolutions = new ArrayList<>();

        for (String fileName: this.getConflictingFileNames()) {
            byte[] fileContent = this.getFileContent(fileName);
            fileResolutions.add(new StaticFileResolution(fileName, fileContent));
        }

        return new Resolution(fileResolutions);
    }

    public Map<String, FileResolution> getActualFileResolutions() throws IOException {
        Map<String, FileResolution> fileResolutions = new HashMap<>();

        for (String fileName: this.getConflictingFileNames()) {
            byte[] fileContent = this.getFileContent(fileName);
            fileResolutions.put(fileName, new StaticFileResolution(fileName, fileContent));
        }

        return fileResolutions;
    }

    private byte[] getFileContent(String fileName) throws IOException {
        try (TreeWalk treeWalk = TreeWalk.forPath(
                this.merger.getRepository(),
                fileName,
                this.commit.getTree()
            )
        ) {
            if (treeWalk == null) return new byte[0];
            ObjectId blobId = treeWalk.getObjectId(0);
            try (ObjectReader objectReader = this.merger.getRepository().newObjectReader()) {
                ObjectLoader objectLoader = objectReader.open(blobId);
                return objectLoader.getBytes();
            }
        }
    }

    private List<String> getConflictingFileNames() {
        return this.merger.getMergeResults().keySet().stream().toList();
    }

    public String getCommitName() {
        return this.commit.getName();
    }
}
