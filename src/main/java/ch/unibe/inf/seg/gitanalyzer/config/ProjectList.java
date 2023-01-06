package ch.unibe.inf.seg.gitanalyzer.config;

import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfos;
import ch.unibe.inf.seg.gitanalyzer.util.csv.CSVFile;
import ch.unibe.inf.seg.gitanalyzer.util.file.FileHelper;
import ch.unibe.inf.seg.gitanalyzer.util.json.JSONValidator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.FilenameUtils;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class ProjectList {

    public static final CSVFormat READ_FORMAT = CSVFormat.DEFAULT
            .builder()
            .setHeader("name", "remoteURL")
            .setSkipHeaderRecord(true)
            .build();

    private ProjectLists projectLists;

    public void setProjectLists(ProjectLists projectLists) {
        this.projectLists = projectLists;
    }

    public ProjectLists getProjectLists() {
        this.preconditionBelongsToProjectLists();
        return this.projectLists;
    }

    private JSONObject jsonObject = new JSONObject();

    public JSONObject getJsonObject() {
        return this.jsonObject;
    }

    public void setList(String list) {
        this.jsonObject.put("list", list);
        this.preconditionSchema();
    }
    public void setList(Path list) {
        this.setList(list.toString());
    }
    private Path getList() {
        return Path.of(this.jsonObject.getString("list"));
    }
    public Path getListPath() {
        this.preconditionHasList();
        return FileHelper.normalize(this.getList());
    }
    public Path getListPathAbsolute() {
        this.preconditionHasList();
        if (this.belongsToProjectLists() && this.projectLists.belongsToConfig()) {
            return this.getProjectLists().getConfig().toRelativePath(this.getList());
        } else {
            return FileHelper.toAbsolutePath(this.getList());
        }
    }

    public void setDir(String dir) {
        this.jsonObject.put("dir", dir);
    }
    public void setDir(Path dir) {
        this.jsonObject.put("dir", dir.toString());
    }
    private Path getDir() {
        return Path.of(this.jsonObject.getString("dir"));
    }

    /**
     * Creates a relative path representing the project directory.
     * The actual value set to the project lists dir, will be returned here, this may be a relative or absolute path.
     * The path will be normalized.
     * @return An absolute path, representing the project directory.
     */
    public Path getDirPath() {
        this.preconditionHasList();
        return FileHelper.normalize(this.getDir());
    }

    /**
     * Creates an absolute path representing the project directory.
     * If the directory is already absolute, the absolute path will be returned. If the directory is relative, one of
     * two things can happen: If the project list belongs to a {@link Config}, the directory will be relative to the
     * directory of the config, but only if the config has a [configPath]{@link Config#getConfigPathAbsolute()}. If the
     * latter is not the case or this project list does not belong to a config, the returned path will be relative to
     * the current working directory.
     * @return An absolute path, representing the project directory.
     */
    public Path getDirPathAbsolute() {
        this.preconditionHasList();
        if (this.belongsToProjectLists() && this.projectLists.belongsToConfig()) {
            return this.getProjectLists().getConfig().toRelativePath(this.getDir());
        } else {
            return FileHelper.toAbsolutePath(this.getDir());
        }
    }

    public void setSuffix(String suffix) {
        this.jsonObject.put("suffix", suffix);
    }
    public String getSuffix() {
        this.preconditionHasList();
        return this.jsonObject.getString("suffix");
    }

    public void setSkip(boolean skip) {
        this.jsonObject.put("skip", skip);
    }
    public boolean getSkip() {
        this.preconditionHasList();
        return this.jsonObject.getBoolean("skip");
    }

    public Path getOutFilename() {
        this.preconditionHasList();
        String listBaseName = FilenameUtils.getBaseName(this.getList().toString());
        return Path.of(listBaseName + (this.getSuffix().equals("") ? "" : "-") + this.getSuffix() + ".json");
    }

    public ProjectList() {
        this.preconditionSchema();
    }

    public ProjectList(ProjectLists projectLists) {
        this();
        this.projectLists = projectLists;
    }

    public ProjectList(String projectList, ProjectLists projectLists) {
        this(projectList);
        this.projectLists = projectLists;
    }

    public ProjectList(String projectList) {
        this.setList(projectList);
        this.preconditionSchema();
    }

    public ProjectList(JSONObject jsonObject, ProjectLists projectLists) {
        this(jsonObject);
        this.projectLists = projectLists;
    }

    public ProjectList(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        this.preconditionSchema();
    }

    public ProjectList(
            String projectList,
            String projectDir,
            String outSuffix,
            boolean skip,
            ProjectLists projectLists
    ) {
        this(projectList, projectDir, outSuffix, skip);
        this.projectLists = projectLists;
    }

    public ProjectList(String projectList, String projectDir, String outSuffix, boolean skip) {
        this.setList(projectList);
        this.setDir(projectDir);
        this.setSuffix(outSuffix);
        this.setSkip(skip);
        this.preconditionSchema();
    }

    /**
     * Validates the project list info against the config JSON-schema.
     * If the validation fails, a {@link ValidationException} is thrown.
     */
    private void preconditionSchema() {
        if (this.hasList()) {
            JSONValidator.validate(this.jsonObject, "projectList.json");
        }
    }

    /**
     * Precondition method, checking if the list has been set yet.
     * @throws IllegalStateException the list has not been set yet.
     */
    private void preconditionHasList() {
        if (!this.hasList()) throw new IllegalStateException("ProjectListInfo does not have a list.");
    }

    /**
     * Precondition method, checking if the project list belong to a {@link ProjectLists} object.
     * @throws IllegalStateException the project list does not belong to a {@link ProjectLists} object.
     */
    private void preconditionBelongsToProjectLists() {
        if (!this.belongsToProjectLists()) throw new IllegalStateException("The project list does not belong to a " +
                "ProjectLists object.");
    }

    public boolean hasList() {
        return this.jsonObject.has("list");
    }

    public boolean belongsToProjectLists() {
        return this.projectLists != null;
    }

    public ProjectInfos toProjectInfos() throws IOException {
        FileReader reader = new FileReader(this.getListPathAbsolute().toFile());
        CSVParser csvParser = new CSVParser(reader, READ_FORMAT);
        CSVFile csvFile = new CSVFile(csvParser);
        ProjectInfos projectInfos = new ProjectInfos(csvFile);
        csvParser.close();
        reader.close();
        return projectInfos;
    }

    @Override
    public String toString() {
        this.preconditionHasList();
        return this.jsonObject.toString(4);
    }
}
