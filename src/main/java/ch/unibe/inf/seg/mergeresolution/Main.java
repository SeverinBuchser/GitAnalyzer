package ch.unibe.inf.seg.mergeresolution;

import ch.unibe.inf.seg.mergeresolution.analyze.AnalyzeConflicts;
import ch.unibe.inf.seg.mergeresolution.clone.CloneProject;
import ch.unibe.inf.seg.mergeresolution.clone.CloneProjects;
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

@Command(
        name = "mcr",
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

    private Integer runCloneProjects(JSONObject config) {
        JSONArray projectListInfos = config.getJSONArray("projectListInfos");

        for (int i = 0 ; i < projectListInfos.length() ; i++) {
            JSONObject projectListInfo = projectListInfos.getJSONObject(i);
            String projectList = projectListInfo.getString("projectList");
            String projectDir = projectListInfo.getString("projectDir");

            projectList = relativizePathToConfigPath(projectList);
            projectDir = relativizePathToConfigPath(projectDir);

            int exitCode = new CommandLine(new CloneProjects()).execute(
                    "--project-dir=" + projectDir,
                    projectList
            );
            if (exitCode != 0) return exitCode;
        }

        return 0;
    }

    private Integer runAnalyzeConflicts(JSONObject config) {
        JSONArray projectListInfos = config.getJSONArray("projectListInfos");
        String outDir = relativizePathToConfigPath(config.getString("outDir"));

        for (int i = 0 ; i < projectListInfos.length() ; i++) {
            JSONObject projectListInfo = projectListInfos.getJSONObject(i);
            String outSuffix = projectListInfo.getString("outSuffix");
            String projectList = relativizePathToConfigPath(projectListInfo.getString("projectList"));
            String projectDir = relativizePathToConfigPath(projectListInfo.getString("projectDir"));

            int exitCode = new CommandLine(new AnalyzeConflicts()).execute(
                    "--project-dir=" + projectDir,
                    "--out-dir=" + outDir,
                    "--out-suffix=" + outSuffix,
                    projectList
            );
            if (exitCode != 0) return exitCode;
        }
        return 0;
    }

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
