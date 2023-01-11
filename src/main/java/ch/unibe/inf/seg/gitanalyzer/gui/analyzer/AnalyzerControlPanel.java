package ch.unibe.inf.seg.gitanalyzer.gui.analyzer;

import ch.unibe.inf.seg.gitanalyzer.util.gui.ControlPanel;

import javax.swing.*;

public class AnalyzerControlPanel extends ControlPanel {
    private static final SpinnerNumberModel threadModel = new SpinnerNumberModel(
            1,
            1,
            10,
            1
    );
    private static final SpinnerNumberModel verbosityModel = new SpinnerNumberModel(
            0,
            0,
            3,
            1
    );
    private final JSpinner maxThreadSpinner = new JSpinner(threadModel);
    private final JSpinner verbosityLevelSpinner = new JSpinner(verbosityModel);

    public AnalyzerControlPanel() {
        this.addControl("Run", "run");
        this.add(new JLabel("Max. Threads:"));
        this.add(this.maxThreadSpinner);
        this.add(new JLabel("Verbosity Level:"));
        this.add(this.verbosityLevelSpinner);
    }

    public int getMaxThreadCount() {
        return (int) this.maxThreadSpinner.getValue();
    }

    public int getVerbosityLevel() {
        return (int) this.verbosityLevelSpinner.getValue();
    }

}
