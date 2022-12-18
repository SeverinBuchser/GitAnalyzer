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

    @Override
    public Integer call() throws Exception {
        this.normalizePaths();
        this.createResultFile();

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
