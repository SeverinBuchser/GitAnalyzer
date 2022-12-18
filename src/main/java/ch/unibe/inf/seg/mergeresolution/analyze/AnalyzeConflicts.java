package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingMerge;
import ch.unibe.inf.seg.mergeresolution.project.Project;
import ch.unibe.inf.seg.mergeresolution.project.ProjectInfo;
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
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@Command(name = "analyze-conflicts")
public class AnalyzeConflicts extends Analyzer<Iterable<ConflictingMerge>, ArrayList<JSONObject>> implements Callable<Integer> {

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
    private final ConflictingMergeAnalyzer subAnalyzer;


    public AnalyzeConflicts() {
        this.subAnalyzer = new ConflictingMergeAnalyzer();
    }

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

    private Project getProject(String projectName) throws IOException {
        return Project.buildFromPath(Paths.get(this.projectDir, FilenameUtils.separatorsToSystem(projectName)));
    }

    @Override
    public Integer call() throws Exception {
        this.normalizePaths();
        this.createResultFile();

        ProjectsInfoListReader reader = ProjectsInfoListReader.read(new File(this.projectListPath));
        JSONObject result = new JSONObject();
        result.put("project_list", FilenameUtils.getBaseName(this.projectListPath));

        ArrayList<JSONObject> projects = new ArrayList<>();

        for (ProjectInfo projectInfo: reader) {
            Project project = this.getProject(projectInfo.name);
            JSONObject project_result = new JSONObject();
            project_result.put("project_name", projectInfo.name);
            printAnalyzing(projectInfo);
            boolean correct = false;

            try {
                ArrayList<JSONObject> merges = this.analyze(project);
                project_result.put("state", ResultState.OK);
                project_result.put("conflicting_merges", merges);
                project_result.put("conflicting_merges_count", merges.size());
                putCount(project_result, merges, "conflicting_merges_correct_count");
                putSum(project_result, merges, "conflicting_files_count");
                putSum(project_result, merges, "conflicting_files_correct_count");
                putSum(project_result, merges, "conflicting_chunks_count");
                putSum(project_result, merges, "conflicting_chunks_correct_count");

                correct = project_result.getInt("conflicting_merges_correct_count") == project_result.getInt("conflicting_merges_count")
                        && project_result.getInt("conflicting_merges_correct_count") != 0;
                project_result.put("correct", correct);
                project_result.put("conflict_count", merges.stream().map(x -> x.getInt("conflict_count")).reduce(0, Integer::sum));

                project_result.put("commits_count", project.getCommitsCount());
                project_result.put("merges_count", project.getMergesCount());
                project_result.put("tags_count", project.getTagsCount());
                project_result.put("contributors_count", project.getCommitsCount() > 0 ? project.getContributorsCount() : 0);

            } catch (Exception e) {
                project_result.put("state", ResultState.FAIL);
                project_result.put("reason", e.getMessage());
            }
            projects.add(project_result);
            System.out.format("Project: %5s: %b\n", project_result.get("state"), correct);
            project.close();
        }
        result.put("state", ResultState.OK);
        result.put("projects", projects);
        result.put("projects_count", projects.size());
        putCount(result, projects, "projects_correct_count");
        putSum(result, projects, "conflicting_merges_count");
        putSum(result, projects, "conflicting_merges_correct_count");
        putSum(result, projects, "conflicting_files_count");
        putSum(result, projects, "conflicting_files_correct_count");
        putSum(result, projects, "conflicting_chunks_count");
        putSum(result, projects, "conflicting_chunks_correct_count");
        putSum(result, projects, "conflict_count");
        putSum(result, projects, "commits_count");
        putSum(result, projects, "merges_count");
        putSum(result, projects, "tags_count");
        putSum(result, projects, "contributors_count");


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

    @Override
    public ArrayList<JSONObject> analyze(Iterable<ConflictingMerge> conflictingMerges) {
        ArrayList<JSONObject> merges = new ArrayList<>();

        for (ConflictingMerge conflictingMerge : conflictingMerges) {
            if (conflictingMerge.getConflictCount() == 0) continue;
            merges.add(this.subAnalyzer.analyze(conflictingMerge));
        }

        return merges;
    }

    private static void printAnalyzing(ProjectInfo projectInfo) {
        System.out.format("Analyzing %s:\n", projectInfo.name);
    }
}
