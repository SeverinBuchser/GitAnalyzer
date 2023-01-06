package ch.unibe.inf.seg.gitanalyzer.gui.config;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.gui.SimpleDocumentListener;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updatable;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscription;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscriptionManager;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updater;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class ProjectListInfoEditor extends JPanel implements Updater {

    private final UpdateSubscriptionManager updateSubscriptionManager = new UpdateSubscriptionManager();

    private final ProjectList projectList;
    private final JTextField projectListText;
    private final JTextField projectDir;
    private final JTextField outSuffix;
    private final JCheckBox skip;
    private final JButton removeButton;

    private final EventListenerList actionEventListeners = new EventListenerList();

    public ProjectListInfoEditor(ProjectList projectList) {
        this.projectList = projectList;

        this.setLayout(new GridBagLayout());
        this.projectListText = this.addTextFiled("Project List:", projectList.getListPath().toString(), 0);
        this.projectDir = this.addTextFiled("Project Dir:", projectList.getDirPath().toString(), 1);
        this.outSuffix = this.addTextFiled("Out Suffix:", projectList.getSuffix(), 2);
        this.skip = this.addSkipCheckbox();
        this.removeButton = this.addRemoveButton();

        this.initListeners();
    }

    private JTextField addTextFiled(String label, String text, int row) {
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = row;
        this.add(new Label(label), c);

        JTextField textField = new JTextField(text);
        textField.setColumns(30);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(textField, c);
        return textField;
    }

    private JCheckBox addSkipCheckbox() {
        JCheckBox skip = new JCheckBox("Skip");
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 3;
        this.add(skip, c);
        return skip;
    }

    private JButton addRemoveButton() {
        JButton removeButton = new JButton("Remove");
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 1;
        c.gridy = 3;
        this.add(removeButton, c);
        return removeButton;
    }

    private void initListeners() {
        this.projectListText.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            this.projectList.setList(this.projectListText.getText());
            this.updateSubscriptionManager.updateAll();
        });
        this.projectDir.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            this.projectList.setDir(this.projectDir.getText());
            this.updateSubscriptionManager.updateAll();
        });
        this.outSuffix.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            this.projectList.setSuffix(this.outSuffix.getText());
            this.updateSubscriptionManager.updateAll();
        });
        this.skip.addItemListener(e -> {
            this.projectList.setSkip(e.getStateChange() == ItemEvent.SELECTED);
            this.updateSubscriptionManager.updateAll();
        });
        this.removeButton.addActionListener(a -> {
            for (ActionListener listener: this.actionEventListeners.getListeners(ActionListener.class)) {
                listener.actionPerformed(a);
            }
        });
    }

    @Override
    public UpdateSubscription getUpdates(Updatable updatable) {
        return this.updateSubscriptionManager.create(updatable);
    }

    public void addActionListener(ActionListener listener) {
        this.actionEventListeners.add(ActionListener.class, listener);
    }
}
