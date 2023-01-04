package ch.unibe.inf.seg.gitanalyzer.project;

import ch.unibe.inf.seg.gitanalyzer.util.csv.CSVFile;
import org.eclipse.jgit.api.CloneCommand;
import org.json.JSONObject;

import java.nio.file.Path;
import java.util.ArrayList;

public class ProjectInfos extends ArrayList<ProjectInfo> {

    /**
     * Constructor.
     * @param csvFile The {@link CSVFile} containing the project info records.
     */
    public ProjectInfos(CSVFile csvFile) {
        for (JSONObject record: csvFile) {
            this.add(new ProjectInfo(
                    record.getString("name"),
                    record.getString("remoteURL")
            ));
        }
    }

    /**
     * Creates an {@link ArrayList<CloneCommand>}. 
     * For each of the {@link ProjectInfo} objects a {@link CloneCommand} will be created.
     *
     * @param dir The parent directory of the projects.
     * @return An {@link ArrayList<CloneCommand>} to clone the projects.
     * @see ProjectInfo#toCloneCommand(Path)
     */
    public ArrayList<CloneCommand> toCloneCommands(Path dir) {
        ArrayList<CloneCommand> cloneCommands = new ArrayList<>();
        for (ProjectInfo projectInfo : this) {
            cloneCommands.add(projectInfo.toCloneCommand(dir));
        }
        return cloneCommands;
    }

    /**
     * Creates a {@link ProjectIterator} object.
     *
     * @param dir The parent directory of the projects.
     * @return The {@link ProjectIterator}.
     */
    public ProjectIterator projectsIterator(Path dir) {
        return new ProjectIterator(this, dir, this.size());
    }
}
