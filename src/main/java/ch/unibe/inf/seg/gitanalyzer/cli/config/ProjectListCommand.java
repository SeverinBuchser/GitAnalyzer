package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.RunCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.ShowCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.AddCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.SetListCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.SetDirCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.SetSuffixCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.SetSkipCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.AnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.CloneCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.RemoveCommand;
import picocli.CommandLine;

@CommandLine.Command(
        name = "project-list",
        description = "Command to manipulate specific project lists within a config file.",
        subcommands = {
                RunCommand.class,
                CloneCommand.class,
                AnalyzeCommand.class,
                ShowCommand.class,
                AddCommand.class,
                RemoveCommand.class,
                SetListCommand.class,
                SetDirCommand.class,
                SetSuffixCommand.class,
                SetSkipCommand.class
        },
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class ProjectListCommand implements Runnable {
    @CommandLine.Spec
    public CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(this.spec.commandLine(), "Missing subcommand.");
    }
}