package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import picocli.CommandLine;

import java.io.IOException;

public class ConfigMixin extends Config {

    private IOException loadException;

    public boolean hasLoadException() {
        return this.loadException != null;
    }

    public IOException getLoadException() {
        return this.loadException;
    }

    @CommandLine.Parameters(
            paramLabel = "<config>",
            description = "The path of the config.",
            index = "0"
    )
    @Override
    public void setAndLoad(String configPath) {
        try {
            super.setAndLoad(configPath);
        } catch (IOException e) {
            this.loadException = e;
        }
    }

}
