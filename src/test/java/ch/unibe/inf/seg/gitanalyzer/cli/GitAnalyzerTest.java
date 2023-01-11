package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.helper.EmptyParameterExceptionHandler;
import ch.unibe.inf.seg.gitanalyzer.helper.TestingPrintStream;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GitAnalyzerTest {
    @Test
    void noSubcommandTest() {
        System.setOut(new TestingPrintStream());
        int exitCode = this.getCmd().execute();
        assertEquals(2, exitCode);
    }

    @Test
    void additionalOptionTest() {
        System.setOut(new TestingPrintStream());
        int exitCode = this.getCmd().execute("--option=optionValue");
        assertEquals(2, exitCode);
    }

    @Test
    void additionalParameterTest() {
        System.setOut(new TestingPrintStream());
        int exitCode = this.getCmd().execute("parameter");
        assertEquals(2, exitCode);
    }

    public CommandLine getCmd() {
        return new CommandLine(new GitAnalyzer())
                .setParameterExceptionHandler(new EmptyParameterExceptionHandler());
    }
}