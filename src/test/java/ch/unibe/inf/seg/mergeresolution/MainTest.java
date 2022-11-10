package ch.unibe.inf.seg.mergeresolution;

import ch.unibe.inf.seg.mergeresolution.helper.EmptyParameterExceptionHandler;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    public void withConfigTest() {
        CommandLine cmd = MainTest.getCmd();

        Path resourceDirectory = Paths.get("src","test","resources", "main");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        //cmd.execute("--config=" + absolutePath + "/test.config.json");
    }

    private static CommandLine getCmd() {
        Main main = new Main();
        return new CommandLine(main);//.setParameterExceptionHandler(new EmptyParameterExceptionHandler());
    }
}