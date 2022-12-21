package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.project.ProjectsInfoListReader;
import ch.unibe.inf.seg.mergeresolution.util.file.FileHelper;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * Subcommand used to analyze a project list and the conflict of the projects in that project list.
 * The subcommand takes one parameter, which is the {@link #projectListPath}. This path describes the path to the
 * project list to analyze. The {@link #projectDir} option is the path to the directory where the projects of the
 * project list are located. The second option is the {@link #outDir}, which is the path to the directory where the
 * output file will be saved to. The last option is the {@link #outSuffix}, which is an optional suffix. If supplied, it
 * will be attached to the end of the output file with a "-" as a separator.
 */
@Command(name = "analyze-conflicts")
public class AnalyzeConflicts implements Callable<Integer> {

    @Parameters(
            index = "0",
            description = "The path to the project list CSV file."
    )
    String projectListPath;
    @Option(
            names = {"-pd", "--project-dir"},
            description = "The directory where the cloned projects are located. Defaults to working directory."
    )
    String projectDir = ".";
    @Option(
            names = {"-od", "--out-dir"},
            description = "The output directory, where the output JSON file goes. Defaults to working directory."
    )
    String outDir = ".";
    @Option(
            names = {"-os", "--out-suffix"},
            description = "The suffix for the output JSON file. Default is no suffix."
    )
    String outSuffix = "";
    @Option(
            names = {"-vb", "--verbose"},
            description = "Verbose mode. Additional information will be displayed."
    )
    boolean verbose = false;

    private File resultFile;

    private void normalizePaths() {
        this.projectListPath = FileHelper.normalizePath(this.projectListPath);
        this.projectDir = FileHelper.normalizePath(this.projectDir);
        this.outDir = FileHelper.normalizePath(this.outDir);
    }

    private void createResultFile() {
        String projectListFileName = FilenameUtils.getBaseName(this.projectListPath);
        this.resultFile = Paths.get(this.outDir,
                projectListFileName
                        + (this.outSuffix.equals("") ? "" : "-" + this.outSuffix)
                        + ".json"
        ).toFile();
    }

    /**
     * Analyzes the project list supplied by the command line.
     * @return 0, the exit code if successful.
     * @throws Exception Thrown if the project list does not exist, the output file cannot be written or the result
     * cannot be converted to a string.
     */
    @Override
    public Integer call() throws Exception {
        this.normalizePaths();
        this.createResultFile();

        Analyzer.verbose = this.verbose;

        ProjectsInfoListReader reader = ProjectsInfoListReader.read(new File(this.projectListPath));
        ProjectInfoListAnalyzer subAnalyzer = new ProjectInfoListAnalyzer(this.projectDir);
        JSONObject result = subAnalyzer.analyze(reader);

        FileWriter writer = new FileWriter(this.resultFile);
        writer.write(result.toString(4));
        System.out.println("\n");
        writer.close();
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new AnalyzeConflicts()).execute(args);
        System.exit(exitCode);
    }
}