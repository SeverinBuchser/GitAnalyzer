package ch.unibe.inf.seg.gitanalyzer.gui.analyzer;

import ch.unibe.inf.seg.gitanalyzer.util.logger.GuiLogger;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;

import javax.swing.*;

public class ProjectListAnalyzerPanel extends JScrollPane implements Subscriber<String> {
    private final GuiLogger logger = new GuiLogger();

    private final JTextArea textArea = new JTextArea(20, 50);

    public GuiLogger getLogger() {
        return logger;
    }

    public ProjectListAnalyzerPanel() {
        this.logger.getOutputStream().subscribe(this);
        this.textArea.setEditable(false);
        this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(this.textArea);
        this.setViewportView(this.textArea);
    }

    @Override
    public void next(String text) {
        this.textArea.setText(text);
        JScrollBar vertical = this.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }
}
