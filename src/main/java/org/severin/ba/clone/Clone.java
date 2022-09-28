package org.severin.ba.clone;

import org.severin.ba.api.Project;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "clone", mixinStandardHelpOptions = true)
class Clone implements Callable<Integer> {

    @Parameters(index = "0") String name;
    @Parameters(index = "1") String uri;
    @Parameters(index = "2") String destination;

    @Override
    public Integer call() {
        try {
            Project.cloneFromUri(this.name, this.uri, this.destination).close();
            System.out.format("Project %s cloned from %s to '%s'", this.name, this.uri, this.destination);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Clone()).execute(args);
        System.exit(exitCode);
    }
}