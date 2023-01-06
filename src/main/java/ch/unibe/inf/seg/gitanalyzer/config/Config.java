package ch.unibe.inf.seg.gitanalyzer.config;

import ch.unibe.inf.seg.gitanalyzer.error.NotUniqueProjectListException;
import ch.unibe.inf.seg.gitanalyzer.util.file.FileHelper;
import ch.unibe.inf.seg.gitanalyzer.util.json.JSONValidator;
import org.everit.json.schema.ValidationException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private Path configPath;

    /**
     * The {@link JSONObject} object, representing the config.
     */
    private JSONObject jsonObject;
    private ProjectLists projectLists;
    public ProjectLists getProjectLists() {
        return this.projectLists;
    }

    public void setConfigPath(String configPath) {
        this.setConfigPath(Path.of(configPath));
    }
    public void setConfigPath(Path configPath) {
        this.configPath = configPath;
    }

    public Path getConfigPath() {
        this.preconditionHasConfigPath();
        return FileHelper.normalize(this.configPath);
    }

    public Path getConfigPathAbsolute() {
        this.preconditionHasConfigPath();
        return FileHelper.toAbsolutePath(this.configPath);
    }

    public boolean getClone() {
        return this.jsonObject.getBoolean("clone");
    }
    public void setClone(boolean clone) {
        this.jsonObject.put("clone", clone);
    }
    public boolean getAnalyze() {
        return this.jsonObject.getBoolean("analyze");
    }
    public void setAnalyze(boolean analyze) {
        this.jsonObject.put("analyze", analyze);
    }
    private Path getOut() {
        return Path.of(this.jsonObject.getString("out"));
    }

    /**
     * Returns a path, created from the out directory. The path will not be transformed to be absolute, which does not
     * mean, that it cannot be absolute. The value of the path will be the normalized path, which was set to the out
     * directory.
     * @return The normalized out path.
     */
    public Path getOutPath() {
        return FileHelper.normalize(this.getOut());
    }

    /**
     * Returns a path, created from the out directory. If the out directory is set to be relative, the following
     * transformations will happen:
     * - If out is absolute ({@code out = /other/absolute}):
     *      -> returns {@code /other/absolute}
     * - Else If the {@link #configPath} is set:
     *      - {@code out = ../out}
     *      - {@code configPath = /absolute/path/config.json}
     *      -> returns {@code /absolute/out}
     * - Else:
     *      - {@code out = ../out}
     *      - {@code cwd = /current/working/dir}
     *      -> returns {@code /current/working/out}
     * @return The normalized absolute out path.
     */
    public Path getOutPathAbsolute() {
        Path normalized = FileHelper.normalize(this.getOut());
        if (normalized.isAbsolute()) return normalized;
        if (this.hasConfigPath()) return FileHelper.toAbsolutePath(normalized, this.getConfigPathAbsolute());
        return FileHelper.toAbsolutePath(normalized);
    }

    public void setOut(String out) {
        this.setOut(Path.of(out));
    }
    public void setOut(Path out) {
        this.jsonObject.put("out", out.toString());
    }
    public boolean getVerbose() {
        return this.jsonObject.getBoolean("verbose");
    }
    public void setVerbose(boolean verbose) {
        this.jsonObject.put("verbose", verbose);
    }

    public Config() {
        this(new JSONObject());
    }

    public Config(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        this.preconditionSchema();
    }

    public Config(Path configPath) throws IOException {
        this.setAndLoad(configPath);
    }

    /**
     * Validates the config against the config JSON-schema.
     * If the validation fails, either a {@link ValidationException} or a {@link NotUniqueProjectListException} is
     * thrown.
     */
    private void preconditionSchema() {
        JSONValidator.validate(this.jsonObject, "config.json");
        if (!this.jsonObject.has("projectLists")) {
            this.jsonObject.put("projectLists", new JSONArray());
        }
        this.projectLists = new ProjectLists(this.jsonObject.getJSONArray("projectLists"), this);
    }

    /**
     * Precondition method, checking if the config path has been set yet.
     * @throws IllegalStateException the config path has not been set yet.
     */
    private void preconditionHasConfigPath() {
        if (!this.hasConfigPath()) throw new IllegalStateException("The config path has not been set yet.");
    }

    public boolean hasConfigPath() {
        return this.configPath != null;
    }

    /**
     * Loads a new config from a path pointing to a JSON file.
     * The path is set with {@link #setConfigPath(String)}. If the config path has not been set, an exception will be
     * thrown.
     * @throws IOException JSON file at the location of the path could not be loaded or does not exist.
     * @throws IllegalStateException the config has not been set yet.
     * @see #setConfigPath(String)
     */
    public void load() throws IOException {
        this.preconditionHasConfigPath();
        String jsonString = Files.readString(this.getConfigPathAbsolute());
        this.jsonObject = new JSONObject(jsonString);
        this.preconditionSchema();
    }

    /**
     * Loads a new config from a path pointing to a JSON file.
     * @see #load()
     */
    public void setAndLoad(Path configPath) throws IOException {
        this.setConfigPath(configPath);
        this.load();
    }

    /**
     * Creates a JSON file from the current {@link Config}.
     * The path is set with {@link #setConfigPath(String)}. If the config path has not been set, an exception will be
     * thrown.
     * @throws IOException JSON file at the location of the path could not be created or written to.
     * @throws IllegalStateException the config has not been set yet.
     * @see #setConfigPath(String)
     */
    public void save() throws IOException {
        this.preconditionHasConfigPath();
        FileWriter writer = new FileWriter(this.getConfigPathAbsolute().toFile());
        writer.write(this.toString());
        writer.close();
    }

    /**
     * Creates a JSON file from the current {@link Config}.
     * @see #save()
     */
    public void setAndSave(Path configPath) throws IOException {
        this.setConfigPath(configPath);
        this.save();
    }

    @Override
    public String toString() {
        return this.jsonObject.toString(4);
    }

    public static List<String> extractValidationMessages(ValidationException e) {
        List<String> validationMessages = new ArrayList<>();
        if (e.getCausingExceptions().size() == 0) {
            validationMessages.add(e.getMessage());
        } else {
            for (ValidationException eNext: e.getCausingExceptions()) {
                validationMessages.addAll(extractValidationMessages(eNext));
            }
        }
        return validationMessages;
    }
}
