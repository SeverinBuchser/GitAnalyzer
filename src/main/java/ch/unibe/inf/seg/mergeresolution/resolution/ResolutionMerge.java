package ch.unibe.inf.seg.mergeresolution.resolution;

import ch.unibe.inf.seg.mergeresolution.error.NotComparableMergesException;
import ch.unibe.inf.seg.mergeresolution.util.path.Path;
import org.eclipse.jgit.diff.EditList;

import java.util.ArrayList;
import java.util.Iterator;

public class ResolutionMerge extends Path<ResolutionFile> implements Comparable<ResolutionMerge> {

    public ResolutionMerge() {
        super();
    }

    public ResolutionMerge(Path<ResolutionFile> path) {
        super(path);
    }

    public static ArrayList<EditList> diff(
            ResolutionMerge resolutionMerge1,
            ResolutionMerge resolutionMerge2
    ) throws NotComparableMergesException {
        ArrayList<EditList> edits = new ArrayList<>();

        Iterator<ResolutionFile> iterator1 = resolutionMerge1.iterator();
        Iterator<ResolutionFile> iterator2 = resolutionMerge2.iterator();

        while (iterator1.hasNext() && iterator2.hasNext()) {
            edits.add(ResolutionFile.diff(iterator1.next(), iterator2.next()));
        }

        if (iterator1.hasNext() || iterator2.hasNext()) {
            throw new NotComparableMergesException();
        }

        return edits;
    }

    @Override
    public int compareTo(ResolutionMerge otherResolutionMerge) {
        try {
            ArrayList<EditList> edits = ResolutionMerge.diff(this, otherResolutionMerge);
            int editCount = 0;

            for (EditList edit: edits) {
                editCount += edit.size();
            }

            return editCount;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
