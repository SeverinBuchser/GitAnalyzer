package ch.unibe.inf.seg.gitanalyzer.helper;

import picocli.CommandLine;
import picocli.CommandLine.IParameterExceptionHandler;

public class EmptyParameterExceptionHandler implements IParameterExceptionHandler {
    @Override
    public int handleParseException(CommandLine.ParameterException ex, String[] args) {
        CommandLine cmd = ex.getCommandLine();
        CommandLine.Model.CommandSpec spec = cmd.getCommandSpec();

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                : spec.exitCodeOnInvalidInput();
    }
}
