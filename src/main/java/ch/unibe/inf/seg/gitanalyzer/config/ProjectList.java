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
        if (!this.hasProjectLists()) throw new IllegalStateException("The project list does not belong to a " +
                "ProjectLists object.");
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
        return FileHelper.normalize(this.getList());
    }
    public Path getListPathAbsolute() {
        return this.toAbsolutePath(this.getList());
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
        return this.toAbsolutePath(this.getDir());
    }

    public void setSuffix(String suffix) {
        this.jsonObject.put("suffix", suffix);
    }
    public String getSuffix() {
        return this.jsonObject.getString("suffix");
    }

    public void setSkip(boolean skip) {
        this.jsonObject.put("skip", skip);
    }
    public boolean getSkip() {
        return this.jsonObject.getBoolean("skip");
    }

    public Path getOutFilename() {
        String listBaseName = FilenameUtils.getBaseName(this.getList().toString());
        return Path.of(listBaseName + (this.getSuffix().equals("") ? "" : "-") + this.getSuffix() + ".json");
    }

    public ProjectList(String list, ProjectLists projectLists) {
        this(list);
        this.projectLists = projectLists;
    }

    public ProjectList(String list) {
        this.setList(list);
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
            String list,
            String dir,
            String suffix,
            boolean skip,
            ProjectLists projectLists
    ) {
        this(list, dir, suffix, skip);
        this.projectLists = projectLists;
    }

    public ProjectList(String list, String dir, String suffix, boolean skip) {
        this.setList(list);
        this.setDir(dir);
        this.setSuffix(suffix);
        this.setSkip(skip);
        this.preconditionSchema();
    }

    /**
     * Validates the project list info against the config JSON-schema.
     * If the validation fails, a {@link ValidationException} is thrown.
     */
    private void preconditionSchema() {
        JSONValidator.validate(this.jsonObject, "projectList.json");
    }

    public boolean hasProjectLists() {
        return this.projectLists != null;
    }

    private Path toAbsolutePath(Path path) {
        Path normalized = FileHelper.normalize(path);
        if (normalized.isAbsolute()) return normalized;
        if (this.hasProjectLists() && this.projectLists.belongsToConfig()) {
            Config config = this.projectLists.getConfig();
            if (config.hasConfigPath()) return FileHelper.toAbsolutePath(normalized, config.getConfigPathAbsolute());
        }
        return FileHelper.toAbsolutePath(normalized);
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
        return this.jsonObject.toString(4);
    }
}
