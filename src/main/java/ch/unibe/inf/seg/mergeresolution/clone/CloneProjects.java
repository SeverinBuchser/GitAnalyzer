package ch.unibe.inf.seg.mergeresolution.clone;

import ch.unibe.inf.seg.mergeresolution.project.ProjectInfo;
import ch.unibe.inf.seg.mergeresolution.project.ProjectsInfoListReader;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "clone-projects", mixinStandardHelpOptions = true)
public class CloneProjects implements Callable<Integer> {

    @Parameters(index = "0") String projectListPath;
    @Option(
            names = {"-pd", "--project-dir"},
            description = "The directory where the projects will be cloned to."
    )
    String projectDir = ".";

    private void normalizePaths() {
        this.projectListPath = FilenameUtils.separatorsToSystem(this.projectListPath);
        this.projectDir = FilenameUtils.separatorsToSystem(this.projectDir);

        this.projectListPath = Paths.get(this.projectListPath).normalize().toAbsolutePath().toString();
        this.projectDir = Paths.get(this.projectDir).normalize().toAbsolutePath().toString();
    }

    @Override
    public Integer call() {
        this.normalizePaths();
        ProjectsInfoListReader reader = new ProjectsInfoListReader(new File(this.projectListPath));

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
