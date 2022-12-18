package ch.unibe.inf.seg.mergeresolution.conflict;

import ch.unibe.inf.seg.mergeresolution.resolution.DynamicResolutionFile;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionChunk;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;
import ch.unibe.inf.seg.mergeresolution.resolution.StaticResolutionFile;
import ch.unibe.inf.seg.mergeresolution.util.path.*;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeChunk;
import org.eclipse.jgit.merge.MergeResult;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class ConflictingFile implements SizeableIterable<ResolutionFile> {
    protected final RevCommit commit;
    private final MergeResult<? extends Sequence> mergeResult;
    private final String fileName;
    private final ArrayList<ConflictingChunk> conflictingChunks;
    private final Repository repository;

    public String getFileName() {
        return fileName;
    }

    public String getFileNameShort() {
        String shortFileName = this.fileName.substring(Math.max(0, this.fileName.length() - 20));
        if (this.fileName.length() > 20) {
            return "..." + shortFileName;
        } else return String.format("%23s", shortFileName);
    }

    public int getConflictCount() {
        return this.conflictingChunks.size();
    }

    public ArrayList<ConflictingChunk> getConflictingChunks() {
        return conflictingChunks;
    }

    public ConflictingFile(
            Repository repository,
            RevCommit commit,
            MergeResult<? extends Sequence> mergeResult,
            String fileName
    ) {
        this.repository = repository;
        this.commit = commit;
        this.mergeResult = mergeResult;
        this.fileName = fileName;
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
                case FIRST_CONFLICTING_RANGE -> firstRange = resolutionChunk;
                case NEXT_CONFLICTING_RANGE -> {
                    assert firstRange != null;
                    conflictingChunks.add(new ConflictingChunk(firstRange, resolutionChunk));
                }
            }
        }
        return conflictingChunks;
    }

    private Intersections<ResolutionChunk> buildResolutions() {
        Intersections<ResolutionChunk> intersections = new Intersections<>();
        List<? extends Sequence> sequenceList = this.mergeResult.getSequences();
        Path<ResolutionChunk> path = new Path<>();
        ArrayList<ResolutionChunk> list = new ArrayList<>();

        for (MergeChunk mergeChunk: this.mergeResult) {
            Sequence sequence = sequenceList.get(mergeChunk.getSequenceIndex());

            if (!(sequence instanceof RawText)) continue;
            ResolutionChunk resolutionChunk = new ResolutionChunk(mergeChunk, (RawText) sequence);

            switch (mergeChunk.getConflictState()) {
                case NO_CONFLICT -> path.add(resolutionChunk);
                case FIRST_CONFLICTING_RANGE -> list.add(resolutionChunk);
                case NEXT_CONFLICTING_RANGE -> {
                    list.add(resolutionChunk);
                    intersections.connect(new ConnectableIntersection<>(path, list));
                    path = new Path<>();
                    list = new ArrayList<>();
                }
            }
        }

        if (path.size() > 0) {
            intersections.connect(new ConnectableIntersection<>(path, Collections.emptyList()));
        }

        return intersections;
    }

    public StaticResolutionFile getActualResolutionFile() throws IOException {
        TreeWalk treeWalk = TreeWalk.forPath(
                this.repository,
                this.fileName,
                this.commit.getTree()
        );
        if (treeWalk == null) return new StaticResolutionFile(this.fileName, new byte[0]);

        ObjectId blobId = treeWalk.getObjectId(0);
        ObjectReader objectReader = this.repository.newObjectReader();
        ObjectLoader objectLoader = objectReader.open(blobId);
        objectReader.close();
        return new StaticResolutionFile(this.fileName, objectLoader.getBytes());
    }

    @Override
    public Iterator<ResolutionFile> iterator() {
        return new Iterator<>() {
            final IntersectionsIterator<ResolutionChunk> chunkIterator = buildResolutions().iterator();

            @Override
            public boolean hasNext() {
                return chunkIterator.hasNext();
            }

            @Override
            public ResolutionFile next() {
                if (this.hasNext()) return new DynamicResolutionFile(fileName, chunkIterator.next());
                else return null;
            }
        };
    }

    @Override
    public double size() {
        return Math.pow(2, this.conflictingChunks.size());
    }
}
