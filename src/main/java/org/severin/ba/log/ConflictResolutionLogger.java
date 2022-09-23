package org.severin.ba.log;

import org.severin.ba.api.Project;
import org.severin.ba.merge.ConflictingMerge;
import org.severin.ba.merge.ConflictingMergeResolution;
import org.severin.ba.util.log.Formatting;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ConflictResolutionLogger {
    private final OutputStream out;

    public ConflictResolutionLogger(OutputStream out) {
        this.out = out;
    }

    public void logConflict(ConflictingMerge conflict) throws Exception {
        ArrayList<ConflictingMergeResolution> mergeResolutions = conflict.getResolutions();
        ConflictingMergeResolution actualResolution = conflict.getActualResolution();

        int resolutionCount = 1;
        for (ConflictingMergeResolution mergeResolution: mergeResolutions) {

            this.out.write(Formatting.DELIMITER.getBytes());
            this.out.write(
                    String.format("Resolution: %d/%d\n", resolutionCount, mergeResolutions.size())
                        .getBytes(Charset.defaultCharset())
            );
            this.out.write(
                    String.format("Differences: %d\n", mergeResolution.compareTo(actualResolution))
                            .getBytes(Charset.defaultCharset())
            );
            this.out.write(Formatting.DELIMITER.getBytes());
            ConflictingMergeResolution.diffFormat(
                    this.out,
                    mergeResolution,
                    actualResolution
            );
            resolutionCount++;
        }
    }

    public void logConflicts(ArrayList<ConflictingMerge> conflicts) throws Exception {
        int conflictCount = 1;
        for (ConflictingMerge conflict: conflicts) {
            this.out.write(Formatting.DELIMITER.getBytes());
            this.out.write(
                    String.format("Conflict: %d/%d\n", conflictCount, conflicts.size())
                            .getBytes(Charset.defaultCharset())
            );
            this.out.write(
                    String.format("Commit: %s\n", conflict.getCommitName())
                            .getBytes(Charset.defaultCharset())
            );
            this.out.write(Formatting.DELIMITER.getBytes());
            this.logConflict(conflict);
            conflictCount++;
        }
    }

    public void logConflictsOfProject(Project project) throws Exception {
        this.logConflicts(project.getConflictingMerges());
    }
}
