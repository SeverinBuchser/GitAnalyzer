package org.severin.ba.conflict;

import org.severin.ba.resolution.ResolutionFile;

import java.util.ArrayList;

public class ConflictingFiles extends ArrayList<ConflictFile> {

    public ArrayList<ArrayList<ResolutionFile>> buildAllResolutions() {
        ArrayList<ArrayList<ResolutionFile>> allResolutions = new ArrayList<>();

        for (ConflictFile conflictFile: this) {
            allResolutions.add(conflictFile.buildResolutions());
        }

        return allResolutions;
    }
}
