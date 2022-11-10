package ch.unibe.inf.seg.mergeresolution.conflict;

import ch.unibe.inf.seg.mergeresolution.resolution.DynamicResolutionFile;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionChunk;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;
import ch.unibe.inf.seg.mergeresolution.resolution.StaticResolutionFile;
import ch.unibe.inf.seg.mergeresolution.util.path.Path;
import ch.unibe.inf.seg.mergeresolution.util.path.PathBuilder;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.merge.MergeChunk;
import org.eclipse.jgit.merge.MergeResult;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ConflictingFile {
    protected final RecursiveMerger merger;
    protected final RevCommit commit;
    private final MergeResult<? extends Sequence> mergeResult;
    private final String fileName;
    private final ArrayList<ConflictingChunk> conflictingChunks;

    private int conflictCount = 0;

    public String getFileName() {
        return fileName;
    }

    public int getConflictCount() {
        return conflictCount;
    }

    public ArrayList<ConflictingChunk> getConflictingChunks() {
        return conflictingChunks;
    }

    public ConflictingFile(
            RevCommit commit,
            RecursiveMerger merger,
            String fileName,
            MergeResult<? extends Sequence> mergeResult
    ) {
        this.commit = commit;
        this.merger = merger;
        this.fileName = fileName;
        this.mergeResult = mergeResult;
        this.conflictingChunks = this.findConflictingChunks();
    }

    private ArrayList<ConflictingChunk> findConflictingChunks() {
        ArrayList<ConflictingChunk> conflictingChunks = new ArrayList<>();
        List<? extends Sequence> sequenceList = this.mergeResult.getSequences();

        ResolutionChunk firstRange = null;

        for (MergeChunk mergeChunk: this.mergeResult) {
            Sequence sequence = sequenceList.get(mergeChunk.getSequenceIndex());

            if (!(sequence instanceof RawText)) continue;
            ResolutionChunk resolutionChunk = new ResolutionChunk(mergeChunk, (RawText) sequence);

            switch (mergeChunk.getConflictState()) {
                case FIRST_CONFLICTING_RANGE -> {
                    this.conflictCount++;
                    firstRange = resolutionChunk;
                }
                case NEXT_CONFLICTING_RANGE -> {
                    assert firstRange != null;
                    conflictingChunks.add(new ConflictingChunk(firstRange, resolutionChunk));
                }
            }
        }
        return conflictingChunks;
    }

    private PathBuilder<ResolutionChunk> buildResolutions() {
        PathBuilder<ResolutionChunk> pathBuilder = new PathBuilder<>();
        List<? extends Sequence> sequenceList = this.mergeResult.getSequences();

        ResolutionChunk firstRange = null;

        for (MergeChunk mergeChunk: this.mergeResult) {
            Sequence sequence = sequenceList.get(mergeChunk.getSequenceIndex());

            if (!(sequence instanceof RawText)) continue;
            ResolutionChunk resolutionChunk = new ResolutionChunk(mergeChunk, (RawText) sequence);

            switch (mergeChunk.getConflictState()) {
                case NO_CONFLICT -> pathBuilder.addItemLayer(resolutionChunk);
                case FIRST_CONFLICTING_RANGE -> {
                    this.conflictCount++;
                    firstRange = resolutionChunk;
                }
                case NEXT_CONFLICTING_RANGE -> {
                    assert firstRange != null;
                    pathBuilder.addItemLayer(firstRange, resolutionChunk);
                }
            }
        }

        return pathBuilder;
    }


    public ArrayList<ResolutionFile> getResolutionFiles() {
        ArrayList<ResolutionFile> resolutionFiles = new ArrayList<>();
        PathBuilder<ResolutionChunk> paths = this.buildResolutions();
        if (paths.getPathCount() > 256) return null;
        for (int i = 0 ; i < paths.getPathCount() ; i++) {
            resolutionFiles.add(new DynamicResolutionFile(this.fileName, new Path<>(i, paths)));
        }
        return resolutionFiles;
    }

    public StaticResolutionFile getActualResolutionFile() throws IOException {
        TreeWalk treeWalk = TreeWalk.forPath(
                this.merger.getRepository(),
                fileName,
                this.commit.getTree()
        );
        if (treeWalk == null) return new StaticResolutionFile(this.fileName, new byte[0]);

        ObjectId blobId = treeWalk.getObjectId(0);
        ObjectReader objectReader = this.merger.getRepository().newObjectReader();
        ObjectLoader objectLoader = objectReader.open(blobId);
        objectReader.close();
        return new StaticResolutionFile(this.fileName, objectLoader.getBytes());
    }
}
