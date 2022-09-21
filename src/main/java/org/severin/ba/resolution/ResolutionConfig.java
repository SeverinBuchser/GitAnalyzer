package org.severin.ba.resolution;

import java.util.ArrayList;

public class ResolutionConfig extends ArrayList<Boolean> {

    public boolean isYoursUsed(int conflictIndex) {
        return this.get(conflictIndex);
    }

}
