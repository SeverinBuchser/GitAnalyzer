package org.severin.ba.mergeconflict;

import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.merge.MergeResult;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.severin.ba.mergeconflict.resolution.FileResolution;
import org.severin.ba.mergeconflict.resolution.Resolution;
import org.severin.ba.mergeconflict.resolution.StaticFileResolution;
import org.severin.ba.util.path.PathBuilder;

import java.io.IOException;
import java.util.ArrayList;
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
        System.out.println(this.merger.getBaseCommitId());
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

    public Resolution getActualResolution() throws IOException {
        ArrayList<FileResolution> fileResolutions = new ArrayList<>();

        for (String fileName: this.getConflictingFileNames()) {
            byte[] fileContent = this.getFileContent(fileName);
            fileResolutions.add(new StaticFileResolution(fileName, fileContent));
        }

        return new Resolution(fileResolutions);
    }

    private byte[] getFileContent(String fileName) throws IOException {
        try (TreeWalk treeWalk = TreeWalk.forPath(
                this.merger.getRepository(),
                fileName,
                this.commit.getTree()
            )
        ) {
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
