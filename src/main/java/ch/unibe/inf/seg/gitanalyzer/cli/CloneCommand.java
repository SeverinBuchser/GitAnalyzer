package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
import picocli.CommandLine;

@CommandLine.Command(
        name = "clone",
        description = "Clones a list of projects to one directory. Each project will have its own sub-directory " +
                "which is named after the projects name, which is stored inside the list of projects.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class CloneCommand extends AbstractCloneCommand {

    @CommandLine.Mixin
    public GlobalLogger logger;

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
        this.logger.info("Executing Clone Command...");
        this.cloneProjectList(projectList);
        this.logger.success("Clone Command Complete.");
    }
}
