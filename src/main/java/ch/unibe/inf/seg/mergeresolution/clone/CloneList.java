package ch.unibe.inf.seg.mergeresolution.clone;

import ch.unibe.inf.seg.mergeresolution.project.ProjectInfo;
import ch.unibe.inf.seg.mergeresolution.project.ProjectsInfoListReader;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "clone-projects", mixinStandardHelpOptions = true)
public class CloneList implements Callable<Integer> {

    @Parameters(index = "0") String listPath;
    @Option(names = {"-d", "--destination"}) String destination = "./";

    @Override
    public Integer call() {
        ProjectsInfoListReader reader = new ProjectsInfoListReader(new File(this.listPath));

        for (ProjectInfo projectInfo: reader) {
            Clone.withArgs(projectInfo.name, projectInfo.uri, this.destination);
            System.out.println();
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new CloneList()).execute(args);
        System.exit(exitCode);
    }
}
