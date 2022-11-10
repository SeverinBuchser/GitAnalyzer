package ch.unibe.inf.seg.mergeresolution.resolution;

import org.eclipse.jgit.diff.*;

public abstract class ResolutionFile implements Comparable<ResolutionFile> {

    private final String fileName;

    public String getFileName() {
        return fileName;
    }

    public ResolutionFile(String fileName) {
        this.fileName = fileName;
    }

    public abstract RawText buildRawText();

    public static EditList diff(ResolutionFile resolution1, ResolutionFile resolution2) {
        DiffAlgorithm da = DiffAlgorithm.getAlgorithm(DiffAlgorithm.SupportedAlgorithm.MYERS);
        SequenceComparator<RawText> sc = RawTextComparator.DEFAULT;
        return da.diff(sc, resolution1.buildRawText(), resolution2.buildRawText());
    }

    @Override
    public int compareTo(ResolutionFile otherResolution) {
        return ResolutionFile.diff(this, otherResolution).size();
    }
}
