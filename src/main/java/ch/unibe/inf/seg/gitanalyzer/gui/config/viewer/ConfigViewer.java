package ch.unibe.inf.seg.gitanalyzer.gui.config.viewer;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updatable;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigViewer extends JScrollPane implements Updatable, Subscriber<Config> {

    private final DefaultStyledDocument document = new DefaultStyledDocument();
    private static final BoldBlueSas BOLD_BLUE = new BoldBlueSas();
    private static final BoldSas BOLD = new BoldSas();
    private Config config;
    private final JTextPane textPane = new JTextPane();

    public ConfigViewer() {
        this.textPane.setDocument(this.document);
        this.textPane.setEditable(false);
        this.add(this.textPane);
        this.setViewportView(this.textPane);
        this.updateUI();
    }

    private void formatKeys() {
        Pattern pattern = Pattern.compile("\".*\":", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(this.textPane.getText());

        while (matcher.find()) {
            int start = matcher.start();
            int length = matcher.end() - start - 1;
            this.document.setCharacterAttributes(start, length, BOLD_BLUE, true);
        }
    }

    private void formatValues() {
        Pattern pattern = Pattern.compile("(:\\s)(?!\\[).*?(,?)\\n", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(this.textPane.getText());

        while (matcher.find()) {
            int start = matcher.start() + matcher.group(1).length() - 1;
            int length = matcher.end() - start - matcher.group(2).length() - 1;
            this.document.setCharacterAttributes(start, length, BOLD, true);
        }
    }

    @Override
    public void update() {
        this.textPane.setText(this.config.toString());
        this.formatKeys();
        this.formatValues();
    }

    @Override
    public void next(Config config) {
        this.config = config;
        this.update();
    }

    private static final class BoldBlueSas extends SimpleAttributeSet {
        public BoldBlueSas() {
            StyleConstants.setBold(this, true);
            StyleConstants.setForeground(this, new Color(95, 59, 255));
        }
    }

    private static final class BoldSas extends SimpleAttributeSet {
        public BoldSas() {
            StyleConstants.setBold(this, true);
        }
    }
}