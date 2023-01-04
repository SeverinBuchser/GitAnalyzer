package ch.unibe.inf.seg.gitanalyzer.util.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface SimpleDocumentListener extends DocumentListener {
    void change(DocumentEvent e);

    @Override
    default void insertUpdate(DocumentEvent e) {
        this.change(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        this.change(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        this.change(e);
    }
}
