package ch.unibe.inf.seg.gitanalyzer.gui.run;

import ch.unibe.inf.seg.gitanalyzer.util.gui.ControlPanel;

import javax.swing.*;

public class RunControlPanel extends ControlPanel {

    private static final int runningModelMin = 1;
    private static final int runningModelMax = 10;
    private static final SpinnerNumberModel runningModel = new SpinnerNumberModel(
            1,
            runningModelMin,
            runningModelMax,
            1
    );

    private static final int verbosityModelMin = 0;
    private static final int verbosityModelMax = 3;
    private static final SpinnerNumberModel verbosityModel = new SpinnerNumberModel(
            0,
            verbosityModelMin,
            verbosityModelMax,
            1
    );
    private final JSpinner maxRunningSpinner = new JSpinner(runningModel);
    private final JSpinner verbosityLevelSpinner = new JSpinner(verbosityModel);
    private final JCheckBox autoSaveCheckBox = new JCheckBox("Auto Save", true);

    public RunControlPanel() {
        this.addControl("Run", "run");
        this.add(new JLabel("Max. Running:"));
        this.add(this.maxRunningSpinner);
        this.add(new JLabel("Verbosity Level:"));
        this.add(this.verbosityLevelSpinner);
        this.add(this.autoSaveCheckBox);
    }

    public int getMaxRunningCount() {
        return (int) this.maxRunningSpinner.getValue();
    }

    public void setMaxRunningCount(int maxRunningCount) {
        if (maxRunningCount > runningModelMax) maxRunningCount = runningModelMax;
        if (maxRunningCount < runningModelMin) maxRunningCount = runningModelMin;
        this.maxRunningSpinner.setValue(maxRunningCount);
    }

    public int getVerbosityLevel() {
        return (int) this.verbosityLevelSpinner.getValue();
    }

    public void setVerbosityLevel(int verbosityLevel) {
        if (verbosityLevel > verbosityModelMax) verbosityLevel = verbosityModelMax;
        if (verbosityLevel < verbosityModelMin) verbosityLevel = verbosityModelMin;
        this.verbosityLevelSpinner.setValue(verbosityLevel);
    }

    public boolean getAutoSave() {
        return this.autoSaveCheckBox.isSelected();
    }

}
