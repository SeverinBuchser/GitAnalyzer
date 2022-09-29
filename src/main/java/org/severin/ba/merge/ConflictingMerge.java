package org.severin.ba.merge;

import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.merge.MergeResult;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.severin.ba.util.tree.Node;
import org.severin.ba.util.tree.Tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ConflictingMerge extends Tree<ConflictingMergeFileResolution> {
    protected final RecursiveMerger merger;

    protected final RevCommit commit;

    public ConflictingMerge(RevCommit commit, RecursiveMerger merger) {
        this.commit = commit;
        this.merger = merger;
        this.buildResolutionTree();
    }

    private void buildResolutionTree() {
        for (Map.Entry<String, MergeResult<? extends Sequence>> entry: this.merger.getMergeResults().entrySet()) {

            ConflictingMergeFile conflictingFile = new ConflictingMergeFile(
                    entry.getKey(),
                    entry.getValue()
            );
            ArrayList<ConflictingMergeFileResolution> fileResolutions = conflictingFile.getResolutions();
            ArrayList<Node<ConflictingMergeFileResolution>> fileResolutionsNodes = new ArrayList<>();

            for (ConflictingMergeFileResolution fileResolution: fileResolutions) {
                fileResolutionsNodes.add(new Node<>(fileResolution));
            }

            this.addList(fileResolutionsNodes);
        }
    }

    public ArrayList<ConflictingMergeResolution> getResolutions() {
        ArrayList<ConflictingMergeResolution> mergeResolutions = new ArrayList<>();
        for (Stack<Node<ConflictingMergeFileResolution>> path : this.getPaths()) {
            mergeResolutions.add(ConflictingMergeResolution.fromTreePath(path));
        }
        return mergeResolutions;
    }

    public ConflictingMergeResolution getActualResolution() throws IOException {
        ArrayList<ConflictingMergeFileResolution> fileResolutions = new ArrayList<>();

        for (String fileName: this.getConflictingFileNames()) {
            byte[] fileContent = this.getFileContent(fileName);
            fileResolutions.add(new ConflictingMergeFileResolution(fileName, fileContent));
        }

        return new ConflictingMergeResolution(fileResolutions);
    }

    private byte[] getFileContent(String fileName) throws IOException {
        try (TreeWalk treeWalk = TreeWalk.forPath(this.merger.getRepository(), fileName, this.commit.getTree())) {
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
