package ch.unibe.inf.seg.mergeresolution.resolution;

import org.eclipse.jgit.diff.*;

public abstract class FileResolution implements Comparable<FileResolution> {

    private final String fileName;

    public String getFileName() {
        return fileName;
    }

    public FileResolution(String fileName) {
        this.fileName = fileName;
    }

    protected abstract RawText buildRawText();

    public static EditList diff(FileResolution resolution1, FileResolution resolution2) {
        DiffAlgorithm da = DiffAlgorithm.getAlgorithm(DiffAlgorithm.SupportedAlgorithm.MYERS);
        SequenceComparator<RawText> sc = RawTextComparator.DEFAULT;
        return da.diff(sc, resolution1.buildRawText(), resolution2.buildRawText());
    }

    @Override
    public int compareTo(FileResolution otherResolution) {
        return FileResolution.diff(this, otherResolution).size();
    }
}
