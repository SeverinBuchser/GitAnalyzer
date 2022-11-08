package ch.unibe.inf.seg.mergeresolution.resolution;

import org.eclipse.jgit.diff.EditList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Resolution extends ArrayList<ResolutionFile> implements Comparable<Resolution> {

    public Resolution(List<ResolutionFile> mergeResolution) {
        super(mergeResolution);
    }

    private Optional<ResolutionFile> findOptionalByFileName(String fileName) {
        return this.stream().filter(fileResolution -> fileResolution.getFileName().equals(fileName)).findAny();
    }

    private ResolutionFile getByFileName(String fileName) {
        Optional<ResolutionFile> optionalFileResolution = this.findOptionalByFileName(fileName);
        return optionalFileResolution.orElse(null);
    }

    public static ArrayList<EditList> diff(
            Resolution resolution1,
            Resolution resolution2
    ) throws Exception {
        if (resolution1.doesNotHaveSameFiles(resolution2)) {
            throw new Exception("The two resolutions are not comparable. They are not for the same merge!");
        }
        ArrayList<EditList> edits = new ArrayList<>();

        for (ResolutionFile resolutionFile1 : resolution1) {
            ResolutionFile resolutionFile2 = resolution2.getByFileName(resolutionFile1.getFileName());
            edits.add(ResolutionFile.diff(resolutionFile1, resolutionFile2));
        }

        return edits;
    }

    private boolean doesNotHaveSameFiles(Resolution resolution) {
        if (this.size() != resolution.size()) return true;
        for (ResolutionFile resolutionFile : this) {
            if (resolution.findOptionalByFileName(resolutionFile.getFileName()).isEmpty()) return true;
        }
        return false;
    }

    @Override
    public int compareTo(Resolution otherResolution) {
        try {
            ArrayList<EditList> edits = Resolution.diff(this, otherResolution);
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
