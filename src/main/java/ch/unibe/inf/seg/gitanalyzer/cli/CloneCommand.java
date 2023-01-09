package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.CliLogger;
import picocli.CommandLine;

@CommandLine.Command(
        name = "clone",
        description = "Clones a list of projects to one directory. Each project will have its own sub-directory " +
                "which are named after the projects name, which is stored inside the list of projects.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class CloneCommand implements Runnable {

    @CommandLine.Mixin
    public CliLogger logger;

    private final ProjectList projectList = new ProjectList("");

    @CommandLine.Option(
            names = {"-d", "--dir"},
            description = "The directory the projects will be cloned to.",
            defaultValue = ""
    )
    public void setDir(String dir) {
        this.projectList.setDir(dir);
    }

    @CommandLine.Parameters(
            description = "The list of projects to clone."
    )
    public void setList(String list) {
        this.projectList.setList(list);
    }

    @Override
    public void run() {
        this.logger.info("Running Clone Command");
        ProjectListCloner cloner = new ProjectListCloner(this.logger);
        cloner.call(this.projectList);
    }
}
