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

/**
 * Class representing a conflicting file.
 * The conflicting chunks of the conflicting file as well as every resolution file and the actual resolution file can be
 * retrieved from this class. Important to mention is that the resolution files can only be obtained iteratively to
 * reduce memory usage.
 */
public class ConflictingFile implements SizeableIterable<ResolutionFile> {
    /**
     * The repository this conflicting file belongs to.
     */
    private final Repository repository;
    /**
     * The commit in which the conflicting file was found to have conflicts.
     */
    protected final RevCommit commit;
    /**
     * The merge result for this conflicting file.
     */
    private final MergeResult<? extends Sequence> mergeResult;
    /**
     * The filename of this conflicting file.
     */
    private final String fileName;
    /**
     * The conflicting chunks of this conflicting file.
     * The conflicting chunks are generated on construction.
     */
    private final ArrayList<ConflictingChunk> conflictingChunks;

    /**
     * Getter for {@link #fileName}.
     * @return The filename of this conflicting file.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Formats the filename to a specific length. If the filename is too short empty spaces will be added. If it is too
     * long, the file name will be shortened.
     * @return A shortened filename.
     */
    public String getFileNameShort() {
        String shortFileName = this.fileName.substring(Math.max(0, this.fileName.length() - 20));
        if (this.fileName.length() > 20) {
            return "..." + shortFileName;
        } else return String.format("%23s", shortFileName);
    }

    /**
     * Size of the {@link #conflictingChunks}.
     * @return The number of conflicting chunks.
     */
    public int getConflictCount() {
        return this.conflictingChunks.size();
    }

    /**
     * Getter for {@link #conflictingChunks}.
     * @return The conflicting chunks of this conflicting file.
     */
    public ArrayList<ConflictingChunk> getConflictingChunks() {
        return conflictingChunks;
    }

    /**
     * Instantiates a new conflicting file.
     * @param repository The repository this conflicting file belongs to.
     * @param commit The commit in which the conflicting file was found to have conflicts.
     * @param mergeResult The merge result for this conflicting file.
     * @param fileName The filename of this conflicting file.
     */
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

    /**
     * Searches and returns the conflicting chunks of this conflicting file.
     * The {@link MergeResult} for this file, provided externally, are searched for conflicting chunks. The merge result
     * contains multiple {@link MergeChunk}s which are iterated over and checked if they contain conflicts. A
     * {@link MergeChunk} contains a conflict if the conflict state of this chunk is either
     * {@link MergeChunk.ConflictState#FIRST_CONFLICTING_RANGE} or
     * {@link MergeChunk.ConflictState#NEXT_CONFLICTING_RANGE}. For each conflict a first conflicting range and a next
     * conflicting range must be found. They will both be placed within a {@link ConflictingChunk} object and added to
     * a list which is then returned.
     * @return A list of all conflicting chunks within this conflicting file.
     */
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

    /**
     * Builds an intersections object of resolution chunks.
     * The method iterates over every merge chunk in the merge result and adds it either to a path of resolution chunks
     * or to a list of resolution chunks. The merge chunks will first be transformed into a resolution chunk. If a
     * merge chunk does not contain conflicts, the corresponding resolution chunk is added to the path. If it does
     * contain a conflict, it is added to the list. If the list contains two resolution chunks (i.e. the two conflicting
     * chunks of the conflict), a connectable intersection, containing the path as the path and the list as the
     * collection, is connected to the intersections and the path and the list are both emptied. The process then
     * continues with the remaining merge chunks. If at the end some merge chunks are left over in the path, this path
     * is also passed to a connectable intersection, but with an empty collection. This connectable intersection is then
     * added to the intersections as well.
     * @return Intersections object containing all merge chunks of the merge result.
     */
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

    /**
     * Searches and retrieves the actual resolution file.
     * @return The actual resolution file corresponding to this conflicting file.
     * @throws IOException Thrown if the conflicting file cannot be found in the actual resolution.
     */
    public StaticResolutionFile getActualResolutionFile() throws IOException {
        TreeWalk treeWalk = TreeWalk.forPath(
                this.repository,
                this.fileName,
                this.commit.getTree()
        );
        if (treeWalk == null) return new StaticResolutionFile(new byte[0]);

        ObjectId blobId = treeWalk.getObjectId(0);
        ObjectReader objectReader = this.repository.newObjectReader();
        ObjectLoader objectLoader = objectReader.open(blobId);
        objectReader.close();
        return new StaticResolutionFile(objectLoader.getBytes());
    }

    /**
     * Iterator, iterating over every resolution file.
     * The iterator wraps the intersections iterator obtained from the {@link #buildResolutions()} method. The paths of
     * the intersections iterator can then be transformed into a dynamic resolution file.
     * @return Iterator of all resolution files for this conflicting file.
     */
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
                if (this.hasNext()) return new DynamicResolutionFile(chunkIterator.next());
                else return null;
            }
        };
    }

    /**
     * Calculates the size of the conflicting files.
     * @return The number of paths, which is two to the power of the number of conflicting chunks.
     */
    @Override
    public double size() {
        return Math.pow(2, this.conflictingChunks.size());
    }
}
