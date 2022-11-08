package ch.unibe.inf.seg.mergeresolution.clone;

import ch.unibe.inf.seg.mergeresolution.project.Project;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "clone-project", mixinStandardHelpOptions = true)
public class Clone implements Callable<Integer> {

    @Parameters(index = "0") String name;
    @Parameters(index = "1") String uri;
    @Parameters(index = "2") String destination;

    @Override
    public Integer call() {
        try {
            System.out.format("Cloning project %s into %s\n", this.uri, this.destination);
            Project.cloneFromUri(this.uri, this.destination + "/" + this.name).close();
            System.out.format("Project %s cloned\n", this.name);
        } catch (Exception e) {
            System.out.println("Project " + this.name + " could not be cloned! " + e.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Clone()).execute(args);
        System.exit(exitCode);
    }

    public static void withArgs(String name, String uri, String destination) {
        new CommandLine(new Clone()).execute(name, uri, destination);
    }
}