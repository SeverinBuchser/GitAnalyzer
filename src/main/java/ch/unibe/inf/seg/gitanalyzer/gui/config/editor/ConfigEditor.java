package ch.unibe.inf.seg.gitanalyzer.gui.config.editor;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.gui.SimpleDocumentListener;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscribable;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscription;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.SubscriptionManager;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updatable;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscription;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscriptionManager;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class ConfigEditor extends JPanel implements Updatable, Updater, Subscriber<Config>, Subscribable<Config> {

    private final UpdateSubscriptionManager updateSubscriptionManager = new UpdateSubscriptionManager();

    private final SubscriptionManager<Config> subscriptionManager = new SubscriptionManager<>();

    private final JCheckBox cloneProjectsToggle = new JCheckBox("Clone Projects", false);
    private final JCheckBox analyzeConflictsToggle = new JCheckBox("Analyze Conflicts", false);

    private final JTextField outDirTextField = new JTextField();
    private final ProjectListInfosEditor projectListInfosEditor;
    private Config config;

    public ConfigEditor() {
        this.setLayout(new GridBagLayout());

        // groups
        JPanel togglePanel = new JPanel();
        JPanel outDirPanel = new JPanel();

        // toggle panel
        togglePanel.setLayout(new GridLayout(2, 1));
        togglePanel.add(this.cloneProjectsToggle);
        togglePanel.add(this.analyzeConflictsToggle);

        // out dir
        this.outDirTextField.setColumns(20);
        outDirPanel.add(new JLabel("Out Directory: "));
        outDirPanel.add(this.outDirTextField);

        // project list infos
        this.projectListInfosEditor = new ProjectListInfosEditor();
        JButton addProjectListInfoButton = new JButton("Add Project List Info");

        // this
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_START;

        c.gridy = 0;
        this.add(togglePanel, c);
        c.gridy = 1;
        c.insets = new Insets(5,0,0,0);
        this.add(outDirPanel, c);
        c.gridy = 2;
        c.insets = new Insets(5,5,0,0);
        this.add(addProjectListInfoButton, c);
        c.gridy = 3;
        c.insets = new Insets(5,0,0,0);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.ipadx = 0;
        this.add(this.projectListInfosEditor, c);

        this.cloneProjectsToggle.addItemListener(evt -> {
            this.config.setClone(evt.getStateChange() == ItemEvent.SELECTED);
            this.updateSubscriptionManager.updateAll();
        });
        this.analyzeConflictsToggle.addItemListener(evt -> {
            this.config.setAnalyze(evt.getStateChange() == ItemEvent.SELECTED);
            this.updateSubscriptionManager.updateAll();
        });
        this.outDirTextField.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            this.config.setOut(this.outDirTextField.getText());
            this.updateSubscriptionManager.updateAll();
        });

        addProjectListInfoButton.addActionListener(e -> {
            JFrame frame = new JFrame();
            String projectList = JOptionPane.showInputDialog(frame, "Project List Path:",
                    "Project List Path Input", JOptionPane.PLAIN_MESSAGE);
            if (projectList != null && !projectList.equals("")) {
                ProjectList projectListInfo = new ProjectList(projectList);
                if (!this.config.getProjectLists().has(projectListInfo)) {
                    this.config.getProjectLists().add(projectListInfo);
                    this.updateSubscriptionManager.updateAll();
                    this.projectListInfosEditor.update();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            String.format("Project list '%s' already exists.", projectListInfo.getListPath().toString()));
                }
            }
            frame.dispose();
        });

        this.projectListInfosEditor.getUpdates(this.updateSubscriptionManager::updateAll);
        this.subscribe(this.projectListInfosEditor);
    }

    @Override
    public void update() {
        this.projectListInfosEditor.update();
    }

    @Override
    public UpdateSubscription getUpdates(Updatable updatable) {
        return this.updateSubscriptionManager.create(updatable);
    }

    @Override
    public void next(Config config) {
        this.config = config;
        this.subscriptionManager.nextAll(config);
        this.cloneProjectsToggle.setSelected(config.getClone());
        this.analyzeConflictsToggle.setSelected(config.getAnalyze());
        this.outDirTextField.setText(config.getOutPath().toString());
    }

    @Override
    public Subscription<Config> subscribe(Subscriber<Config> subscriber) {
        return this.subscriptionManager.create(subscriber);
    }
}
