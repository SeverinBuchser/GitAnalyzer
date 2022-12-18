package ch.unibe.inf.seg.mergeresolution.clone;

import ch.unibe.inf.seg.mergeresolution.project.Project;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "clone-project")
public class CloneProject implements Callable<Integer> {

    @Parameters(
            index = "0",
            description = "The name of the project. The project will be cloned into a directory with this name."
    )
    String name;
    @Parameters(
            index = "1",
            description = "The url from which the project can be cloned from."
    )
    String url;
    @Option(
            names = {"-pd", "--project-dir"},
            description = "The directory where the project will be cloned to. Defaults to working directory."
    )
    String projectDir = ".";

    @Override
    public Integer call() {
        try {
            System.out.format("Cloning project %s into %s\n", this.url, this.projectDir);
            Project.cloneFromUri(this.url, this.projectDir + "/" + this.name, this.name).close();
            System.out.format("Project %s cloned\n", this.name);
        } catch (Exception e) {
            System.out.println("Project " + this.name + " could not be cloned! " + e.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CloneProject()).execute(args);
        System.exit(exitCode);
    }
}