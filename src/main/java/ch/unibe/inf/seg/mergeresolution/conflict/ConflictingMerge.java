package ch.unibe.inf.seg.mergeresolution.conflict;

import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionMerge;
import ch.unibe.inf.seg.mergeresolution.util.path.ConnectableIntersection;
import ch.unibe.inf.seg.mergeresolution.util.path.Intersections;
import ch.unibe.inf.seg.mergeresolution.util.path.IntersectionsIterator;
import ch.unibe.inf.seg.mergeresolution.util.path.SizeableIterable;
import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeResult;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ConflictingMerge implements SizeableIterable<ResolutionMerge> {
    private final Repository repository;
    protected final RevCommit commit;
    private final Map<String, MergeResult<? extends Sequence>> mergeResults;

    private final ArrayList<ConflictingFile> conflictingFiles;

    public ArrayList<ConflictingFile> getConflictingFiles() {
        return this.conflictingFiles;
    }

    public ConflictingMerge(
            Repository repository,
            RevCommit commit,
            Map<String, MergeResult<? extends  Sequence>> mergeResults
    ) {
        this.repository = repository;
        this.commit = commit;
        this.mergeResults = mergeResults;
        this.conflictingFiles = this.findConflictingFiles();
    }

    private ArrayList<ConflictingFile> findConflictingFiles() {
        ArrayList<ConflictingFile> conflictingFiles = new ArrayList<>();

        for (String fileName: this.mergeResults.keySet()) {

            if (this.mergeResults.get(fileName).getSequences().size() == 0) continue;

            ConflictingFile conflictingFile = new ConflictingFile(
                    this.repository,
                    this.commit,
                    this.mergeResults.get(fileName),
                    fileName
            );

            if (conflictingFile.getConflictCount() > 0) {
                conflictingFiles.add(conflictingFile);
            }
        }
        return conflictingFiles;
    }

    private Intersections<ResolutionFile> buildResolutions() {
        Intersections<ResolutionFile> intersections = new Intersections<>();
        for (ConflictingFile conflictingFile: this.conflictingFiles) {
            intersections.connect(new ConnectableIntersection<>(conflictingFile));
        }
        return intersections;
    }

    public ResolutionMerge getActualResolution() throws IOException {
        ResolutionMerge resolutionMerge = new ResolutionMerge();
        for (ConflictingFile conflictingFile: this.conflictingFiles) {
            resolutionMerge.add(conflictingFile.getActualResolutionFile());
        }
        return resolutionMerge;
    }

    public String getCommitId() {
        return this.commit.getName();
    }

    public String getCommitIdShort() {
        return this.getCommitId().substring(0, 7);
    }

    @Override
    public Iterator<ResolutionMerge> iterator() {
        return new Iterator<>() {
            private final IntersectionsIterator<ResolutionFile> iterator = buildResolutions().iterator();
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ResolutionMerge next() {
                if (this.hasNext()) return new ResolutionMerge(iterator.next());
                else return null;
            }
        };
    }

    @Override
    public double size() {
        if (this.conflictingFiles.size() == 0) return 0;
        double size = 1;

        for (ConflictingFile conflictingFile : this.conflictingFiles) {
            size *= conflictingFile.size();
        }
        return size;
    }

    public double getConflictCount() {
        return this.conflictingFiles.stream().reduce((double) 0, (conflictCount, conflictingFile) -> {
            return conflictCount + conflictingFile.getConflictCount();
        }, Double::sum);
    }

    public int getParentCount() {
        return this.commit.getParentCount();
    }
}
