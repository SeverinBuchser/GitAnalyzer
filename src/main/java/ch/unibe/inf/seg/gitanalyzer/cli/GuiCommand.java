package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.gui.run.RunPanel;
import ch.unibe.inf.seg.gitanalyzer.gui.config.ConfigPanel;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@CommandLine.Command(
        name = "gui",
        description = "Opens the gui.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class GuiCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Option(
            names = {"-c", "--config"},
            description = "The config file to open in the gui."
    )
    public void setConfig(File config) {
        try {
            this.config = new Config(config.toPath());
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
    }

    private Config config;

    private boolean hasConfig() {
        return this.config != null;
    }

    @Override
    public void run() {
        this.logger.info("Starting GUI");
        ConfigPanel configPanel = new ConfigPanel();
        RunPanel runPanel = new RunPanel();
        configPanel.subscribe(runPanel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Config Panel", configPanel);
        tabbedPane.addTab("Run Panel", runPanel);

        if (this.hasConfig()) configPanel.next(this.config);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(tabbedPane);
        Dimension size = new Dimension(500, 500);
        frame.setMinimumSize(size);
        frame.setSize(size);
        frame.setTitle("Git Analyzer");
        frame.setVisible(true);
    }
}
