package ch.unibe.inf.seg.mergeresolution;

import ch.unibe.inf.seg.mergeresolution.analyze.AnalyzeResolutions;
import ch.unibe.inf.seg.mergeresolution.analyze.AnalyzeResults;
import ch.unibe.inf.seg.mergeresolution.clone.Clone;
import ch.unibe.inf.seg.mergeresolution.clone.CloneList;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Model.CommandSpec;

@Command(name = "mcr", subcommands = {
        Clone.class,
        CloneList.class,
        AnalyzeResolutions.class,
        AnalyzeResults.class
}, description = "Study merge conflict resolution behaviour of Git projects.")
public class Main implements Runnable {
    @Spec
    CommandSpec spec;

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Missing required subcommand");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
