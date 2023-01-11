package ch.unibe.inf.seg.gitanalyzer.gui.config.editor;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectLists;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updatable;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscription;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscriptionManager;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updater;

import javax.swing.*;
import java.awt.*;

public class ProjectListInfosEditor extends JScrollPane implements Updater, Updatable, Subscriber<Config> {

    private final UpdateSubscriptionManager updateSubscriptionManager = new UpdateSubscriptionManager();

    private final JPanel panel = new JPanel();
    private ProjectLists projectLists;

    public ProjectListInfosEditor() {
        this.panel.setLayout(new GridBagLayout());
        this.add(this.panel);
    }

    @Override
    public void update() {
        this.panel.removeAll();
        this.projectLists.forEach(this::addProjectListInfoEditor);
        this.setViewportView(this.panel);
        this.updateUI();
    }

    private void addProjectListInfoEditor(ProjectList projectList) {
        ProjectListInfoEditor projectListInfoEditor = new ProjectListInfoEditor(projectList);
        projectListInfoEditor.getUpdates(this.updateSubscriptionManager::updateAll);
        projectListInfoEditor.addActionListener(a -> {
            this.projectLists.remove(projectList);
            this.updateSubscriptionManager.updateAll();
            this.panel.remove(projectListInfoEditor);
            this.updateUI();
        });
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        if (this.panel.getComponentCount() >= 1) {
            c.insets = new Insets(10,0,0,0);
        }
        this.panel.add(projectListInfoEditor, c);
    }

    @Override
    public UpdateSubscription getUpdates(Updatable updatable) {
        return this.updateSubscriptionManager.create(updatable);
    }

    @Override
    public void next(Config config) {
        this.projectLists = config.getProjectLists();
        this.update();
    }
}
