package ch.unibe.inf.seg.mergeresolution.project;

import ch.unibe.inf.seg.mergeresolution.util.csv.CSVFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Reads a project info list and provides list of {@link ProjectInfo}.
 * The reader needs a {@link CSVParser} and a list name to work but it is also possible to initiate the reader via a
 * file name. The name the reader will have when initiating with a file, is the basename of the file without. The
 * csv parser can be setup in any way, but it is necessary for the header to contain "name" and "url" columns. The
 * parser should also not read the header as a row, otherwise there will be a row with the header as the data.
 */
public class ProjectsInfoListReader extends CSVFile implements Iterable<ProjectInfo> {
    public final String name;

    /**
     * Initates a new project info list reader.
     * @param parser The {@link CSVParser} to read from.
     * @param name The name of the reader.
     * @throws IOException thrown when the closing of the parser is unsuccessful.
     */
    public ProjectsInfoListReader(CSVParser parser, String name) throws IOException {
        super(parser);
        this.name = name;
    }

    /**
     * Creates a reader from a {@link File}.
     * @param file The file to read from.
     * @return A new project info list reader from the {@link File}.
     * @throws IOException If the file is not found or the reader could not be initiated.
     */
    public static ProjectsInfoListReader read(File file) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT
                .builder()
                .setHeader("name", "url")
                .setSkipHeaderRecord(true)
                .build();
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileReader reader = new FileReader(file);
        CSVParser parser = format.parse(reader);
        String projectListBaseName = FilenameUtils.getBaseName(file.getAbsolutePath());
        ProjectsInfoListReader projectsInfoListReader = new ProjectsInfoListReader(parser, projectListBaseName);
        reader.close();
        return projectsInfoListReader;
    }

    /**
     * Transforms the gathered information to a list of {@link ProjectInfo} objects.
     * @return A list of {@link ProjectInfo}
     */
    public List<ProjectInfo> toList() {
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        JSONArray jsonRecords = this.getJsonRecords();

        for (int i = 0 ; i < jsonRecords.length() ; i++) {
            JSONObject jsonRecord = jsonRecords.getJSONObject(i);
            projectInfoList.add(new ProjectInfo(
                    jsonRecord.getString("name"),
                    jsonRecord.getString("url")
            ));
        }

        return projectInfoList;
    }

    /**
     * Iterator over every {@link ProjectInfo} object.
     * @return Iterator over every {@link ProjectInfo} object.
     */
    @Override
    public Iterator<ProjectInfo> iterator() {
        return this.toList().iterator();
    }

    /**
     * Transforms the project info into projects by reading projects based on the project info and a directory in which
     * all the projects of the project info list must be located.
     * The name of the projects are then used as a subdirectory in which the project/project info with the corresponding
     * name is located.
     * @param projectDir The parent directory of all projects.
     * @return A list of projects.
     * @throws IOException one or more projects could not be loaded.
     */
    public List<Project> toProjectList(String projectDir) throws IOException {
        List<Project> projectList = new ArrayList<>();
        JSONArray jsonRecords = this.getJsonRecords();

        for (int i = 0 ; i < jsonRecords.length() ; i++) {
            JSONObject jsonRecord = jsonRecords.getJSONObject(i);
            projectList.add(Project.buildFromPath(
                Paths.get(projectDir, FilenameUtils.separatorsToSystem(jsonRecord.getString("name"))),
                jsonRecord.getString("name")
            ));
        }

        return projectList;
    }
}
