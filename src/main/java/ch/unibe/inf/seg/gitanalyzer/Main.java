package ch.unibe.inf.seg.gitanalyzer;

import ch.unibe.inf.seg.gitanalyzer.analyze.AnalyzeConflicts;
import ch.unibe.inf.seg.gitanalyzer.clone.CloneProject;
import ch.unibe.inf.seg.gitanalyzer.clone.CloneProjects;
import org.apache.commons.io.FilenameUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * The main command.
 * Can run multiple subcommands:
 *  - CloneProject
 *  - CloneProjects
 *  - AnalyzeConflicts
 * The subcommands are explained in their respective classes.
 * This command can also be called with a configuration JSON file, which is explained in the json-schema for the config.
 * The subcommands and the config file are mutually exclusive, meaning that only one of these options can be run at
 * once. Regardless, the command can parse the config and run the subcommands in the config file with the appropriate
 * normalization of the paths provided by the config.
 */
@Command(
        name = "git-analyzer",
        version = "1.0.0",
        subcommands = {
                CloneProject.class,
                CloneProjects.class,
                AnalyzeConflicts.class
        },
        description = "Study merge conflict resolution behaviour of Git projects.",
        mixinStandardHelpOptions = true
)
public class Main implements Callable<Integer> {
    @Spec
    CommandSpec spec;

    @Option(
            names = {"-c", "--config"},
            description = "Use either this option or use a sub-command. Path to the config file."
    )
    String configPath;

    /**
     * Runs the command.
     * @return The exit code of the command.
     */
    @Override
    public Integer call() throws IOException {
        if (this.configPath != null) {

            this.configPath = Paths.get(FilenameUtils.separatorsToSystem(this.configPath))
                    .normalize()
                    .toAbsolutePath()
                    .toString();
            if (new File(this.configPath).isFile()) {
                String jsonString = Files.readString(Paths.get(this.configPath));
                JSONObject config = new JSONObject(jsonString);
                if (this.validate(config)) {
                    return this.runConfig(config);
                }
            } else {
                System.out.format("Could not find config at location '%s'", this.configPath);
            }

        } else {
            throw new ParameterException(spec.commandLine(), "Missing required subcommand or config file.");
        }
        return 0;
    }

    /**
     * Validates the config.
     * @param config The configuration provided.
     * @return True if the config is valid and false otherwise.
     */
    private boolean validate(JSONObject config) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.schema.json")){
            assert inputStream != null;
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.builder()
                    .useDefaults(true)
                    .schemaJson(rawSchema)
                    .build()
                    .load()
                    .build();
            schema.validate(config);
            return true;
        } catch (ValidationException e) {
            System.err.println(e.getMessage());
            e.getCausingExceptions()
                    .stream()
                    .map(ValidationException::getMessage)
                    .forEach(System.err::println);
            return false;
        }
    }

    /**
     * Runs the subcommands provided by the configuration. If one of the subcommands fails, this method will abort.
     * @param config The configuration provided.
     * @return The exit code of the subcommands.
     */
    private Integer runConfig(JSONObject config) {
        if (config.getBoolean("cloneProjects")) {
            int exitCode = this.runCloneProjects(config);
            if (exitCode != 0) return exitCode;
        }

        if (config.getBoolean("analyzeConflicts")) {
            int exitCode = this.runAnalyzeConflicts(config);
            if (exitCode != 0) return exitCode;
        }

        return 0;
    }

    /**
     * Runs the clone-projects subcommand for every project list provided in the config file. If the subcommand fails
     * once, this method aborts.
     * @param config The configuration provided.
     * @return The exit code of the subcommands.
     */
    private Integer runCloneProjects(JSONObject config) {
        JSONArray projectListInfos = config.getJSONArray("projectListInfos");

        for (int i = 0 ; i < projectListInfos.length() ; i++) {
            JSONObject projectListInfo = projectListInfos.getJSONObject(i);
            String projectList = projectListInfo.getString("projectList");
            String projectDir = projectListInfo.getString("projectDir");

            projectList = relativizePathToConfigPath(projectList);
            projectDir = relativizePathToConfigPath(projectDir);

            boolean skip = projectListInfo.optBoolean("skip");
            if (skip) continue;

            int exitCode = new CommandLine(new CloneProjects()).execute(
                    "--project-dir=" + projectDir,
                    projectList
            );
            if (exitCode != 0) return exitCode;
        }

        return 0;
    }

    /**
     * Runs the analyze-conflicts subcommand for every project list provided in the config file. If the subcommand fails
     * once, this method aborts.
     * @param config The configuration provided.
     * @return The exit code of the subcommands.
     */
    private Integer runAnalyzeConflicts(JSONObject config) {
        JSONArray projectListInfos = config.getJSONArray("projectListInfos");
        String outDir = relativizePathToConfigPath(config.getString("outDir"));
        boolean verbose = config.getBoolean("verbose");

        for (int i = 0 ; i < projectListInfos.length() ; i++) {
            JSONObject projectListInfo = projectListInfos.getJSONObject(i);
            String outSuffix = projectListInfo.getString("outSuffix");
            String projectList = relativizePathToConfigPath(projectListInfo.getString("projectList"));
            String projectDir = relativizePathToConfigPath(projectListInfo.getString("projectDir"));
            boolean skip = projectListInfo.optBoolean("skip");

            if (skip) continue;

            int exitCode = new CommandLine(new AnalyzeConflicts()).execute(
                    "--project-dir=" + projectDir,
                    "--out-dir=" + outDir,
                    "--out-suffix=" + outSuffix,
                    "--verbose=" + verbose,
                    projectList
            );
            if (exitCode != 0) return exitCode;
        }
        return 0;
    }

    /**
     * Relativizes a path to be relative to the path of the config file path.
     * @param path The path to relativize.
     * @return The relativized path.
     */
    private String relativizePathToConfigPath(String path) {
        if (!(new File(path).isAbsolute())) {
            return Paths.get(
                    "/",
                    FilenameUtils.getPath(this.configPath),
                    path
            ).toString();
        }
        return path;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
