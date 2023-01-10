package ch.unibe.inf.seg.gitanalyzer.util.logger;

import picocli.CommandLine;

public class GlobalLogger extends PrintStreamLogger {
    @CommandLine.Option(
            names = {"-v", "--verbose"},
            description = "Increase verbosity. Specify multiple times to increase (-vvv)."
    )
    @Override
    public void setVerbosityLevel(boolean[] verbosity) {
        this.verbosityLevel = verbosity.length;
        LoggerProvider.setLogger(this);
    }

    public GlobalLogger() {
        super(System.out);
    }
}
