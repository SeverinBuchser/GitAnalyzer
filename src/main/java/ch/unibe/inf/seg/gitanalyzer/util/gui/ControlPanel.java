package ch.unibe.inf.seg.gitanalyzer.util.gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ControlPanel extends JPanel {
    private final HashMap<String, JButton> controls = new HashMap<>();

    public void addControl(String controlText, String id) {
        JButton control = new JButton(controlText);
        this.add(control);
        this.controls.put(id, control);
    }

    public void addActionListener(String id, ActionListener listener) {
        if (listener != null && this.controls.containsKey(id)) {
            this.controls.get(id).addActionListener(listener);
        }
    }
}
