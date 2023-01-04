package ch.unibe.inf.seg.gitanalyzer.cli;

import picocli.CommandLine;

/**
 * The root-command and main executable.
 * This command does nothing if called without any subcommand:
 *  - gui
 *  - analyze
 *  - clone
 *  - config
 *  One can either open a GUI, run an analysis of one list of projects, clone a list of projects or create, edit, or
 *  run a config.
 */
@CommandLine.Command(
        name = "git-analyzer",
        subcommands = {
                GuiCommand.class,
                AnalyzeCommand.class,
                CloneCommand.class,
                ConfigCommand.class
        },
        description = "Study merge conflict resolution behaviour of Git projects.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class)
public class GitAnalyzer implements Runnable {
    @CommandLine.Spec
    public CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(this.spec.commandLine(), "Missing subcommand.");
    }

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new GitAnalyzer());
        CommandLine.ParseResult parseResult = cmd.parseArgs(args);
        String guiName = new CommandLine(new GuiCommand()).getCommandName();
        int exitCode = new CommandLine(new GitAnalyzer()).execute(args);
        if (!parseResult.subcommand().commandSpec().name().equals(guiName)) {
            System.exit(exitCode);
        }
    }
}
