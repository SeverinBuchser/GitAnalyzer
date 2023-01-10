package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.cli.config.AnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.ShowCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.CreateCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.SetCloneCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.SetAnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.SetOutCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.RunCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.CloneCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.config.ProjectListCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "config",
        description = "Command to manipulate configuration files.",
        subcommands = {
                RunCommand.class,
                AnalyzeCommand.class,
                CloneCommand.class,
                ShowCommand.class,
                CreateCommand.class,
                SetCloneCommand.class,
                SetAnalyzeCommand.class,
                SetOutCommand.class,
                ProjectListCommand.class
        },
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class ConfigCommand implements Runnable {
    @CommandLine.Spec
    public CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(this.spec.commandLine(), "Missing subcommand.");
    }
}