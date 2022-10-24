package org.severin.ba.mergeconflict.resolution;

import org.eclipse.jgit.diff.EditList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Resolution extends ArrayList<FileResolution> implements Comparable<Resolution> {

    public Resolution(List<FileResolution> mergeResolution) {
        super(mergeResolution);
    }

    private Optional<FileResolution> findOptionalByFileName(String fileName) {
        return this.stream().filter(fileResolution -> fileResolution.getFileName().equals(fileName)).findAny();
    }

    private FileResolution getByFileName(String fileName) {
        Optional<FileResolution> optionalFileResolution = this.findOptionalByFileName(fileName);
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

        for (FileResolution fileResolution1: resolution1) {
            FileResolution fileResolution2 = resolution2.getByFileName(fileResolution1.getFileName());
            edits.add(FileResolution.diff(fileResolution1, fileResolution2));
        }

        return edits;
    }

    private boolean doesNotHaveSameFiles(Resolution resolution) {
        if (this.size() != resolution.size()) return true;
        for (FileResolution fileResolution: this) {
            if (resolution.findOptionalByFileName(fileResolution.getFileName()).isEmpty()) return true;
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
