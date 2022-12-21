package ch.unibe.inf.seg.gitanalyzer.clone;

import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfo;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectsInfoListReader;
import ch.unibe.inf.seg.gitanalyzer.util.file.FileHelper;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Subcommand to clone multiple projects.
 * The projects are provided by a project list, which is specified by the parameter {@link #projectListPath}. The
 * projects will be cloned to the specified option {@link #projectDir} into the directory specified by the name in the
 * project list. The url of the project must also be specified in the project list.
 */
@Command(name = "clone-projects")
public class CloneProjects implements Callable<Integer> {

    @Parameters(index = "0") String projectListPath;
    @Option(
            names = {"-pd", "--project-dir"},
            description = "The directory where the projects will be cloned to. Defaults to working directory."
    )
    String projectDir = ".";

    /**
     * Normalize paths to current OS and relativize paths to current working directory.
     */
    private void normalizePaths() {
        this.projectListPath = FileHelper.normalizePath(this.projectListPath);
        this.projectDir = FileHelper.normalizePath(this.projectDir);
    }

    /**
     * Clones a list of projects into the {@link #projectDir}.
     * @return 0, if successful.
     * @throws IOException Thrown if the project list does not exist.
     */
    @Override
    public Integer call() throws IOException {
        this.normalizePaths();
        ProjectsInfoListReader reader = ProjectsInfoListReader.read(new File(this.projectListPath));

        for (ProjectInfo projectInfo: reader) {
            int exitCode = new CommandLine(new CloneProject()).execute(
                    projectInfo.name,
                    projectInfo.url,
                    "--project-dir=" + this.projectDir
            );
            if (exitCode != 0) {
                return exitCode;
            }
            System.out.println();
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new CloneProjects()).execute(args);
        System.exit(exitCode);
    }
}
