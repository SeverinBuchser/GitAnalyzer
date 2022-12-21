package ch.unibe.inf.seg.mergeresolution.util.progress;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Utility class to display progress.
 * The progress bar is text-based and runs in a separate thread. If another thread tries to print to the same output
 * stream as the progressbar while it is still running, the output will look broken.
 */
public class Progressbar extends Thread {
    /**
     * The progression indicator text.
     */
    private static final String PROGRESS_TEXT = "▉";
    /**
     * The indicator text of the progress to be made.
     */
    private static final String NO_PROGRESS_TEXT = "-";
    /**
     * The spinner text.
     */
    private static final String[] SPINNER = new String[]{
            "⠁",
            "⠂",
            "⠄",
            "⠂"
    };
    /**
     * The current step of the spinner.
     */
    private int spinnerStep = 0;

    /**
     * The output stream to write to.
     */
    private final OutputStream out;
    /**
     * The maximum progress.
     */
    private final int max;
    /**
     * The with of the progressbar in number of characters.
     */
    private final int width;
    /**
     * The rate at which the spinner is updated.
     */
    private final int updateRate;
    /**
     * The current progress.
     */
    private int progress = 0;
    /**
     * Indicator of the current step.
     */
    private String current;

    /**
     * Returns the progress in percentage (from 0 to 1).
     * @return The progress in percentage (from 0 to 1).
     */
    private float getProgressPercentage() {
        return (float) this.progress / (float) this.max;
    }

    /**
     * The with of the progress in characters.
     * @return The number of characters to display for the progress indicator.
     */
    private int getProgressWidth() {
        return Math.round(this.getProgressPercentage() * this.width);
    }

    /**
     * True if the progress is 100%
     * @return True if there is no more progress and false otherwise.
     */
    private boolean isComplete() {
        return this.max == this.progress;
    }

    /**
     * Initiates a new progressbar with an update rate of 200ms.
     * @param out The output stream to write to.
     * @param max The maximum progress.
     * @param width The number of characters of the progress bar indicator.
     */
    public Progressbar(OutputStream out, int max, int width) {
        this(out, max, width, 200);
    }

    /**
     * Initiates a new progressbar.
     * @param out The output stream to write to.
     * @param max The maximum progress.
     * @param width The number of characters of the progress bar indicator.
     * @param updateRate The update rate of the spinner.
     */
    public Progressbar(OutputStream out, int max, int width, int updateRate) {
        this.out = out;
        this.max = max;
        this.width = width;
        this.updateRate = updateRate;
        this.start();
    }

    /**
     * Sets message of the current step. The message will be displayed the next time the progressbar is updated, i.e.
     * in intervals of the specified progress rate.
     * @param current The message to display.
     */
    public void setCurrent(String current) {
        this.current = current;
    }

    /**
     * Progresses the bar by one step.
     * @throws IOException The write process to the output stream failed.
     */
    public void step() throws IOException {
        this.step(1);
    }

    /**
     * Progresses the bar by n steps.
     * @param n The number of steps.
     * @throws IOException The write process to the output stream failed.
     */
    public void step(int n) throws IOException {
        this.step(n, null);
    }

    /**
     * Progresses the bar by one step and updates the current message to a new one.
     * @param current The new message to display.
     * @throws IOException The write process to the output stream failed.
     */
    public void step(String current) throws IOException {
        this.step(1, current);
    }

    /**
     * Progresses the bar by n steps and updates the current message to a new one.
     * @param n The number of steps.
     * @param current The new message to display.
     * @throws IOException The write process to the output stream failed.
     */
    public void step(int n, String current) throws IOException {
        this.progress += n;
        this.current = current;
        this.writeExternal();
        this.current = null;
    }

    /**
     * Write operation called outside this tread.
     * This operation does not progress the spinner. The progressbar indicator is updated and the new or existing
     * message  is displayed.
     * @throws IOException The write process to the output stream failed.
     */
    private void writeExternal() throws IOException {
        String outString = this.getProgressString();
        if (this.current != null) outString += " " + this.current;
        outString += " " + SPINNER[this.spinnerStep];
        this.out.write(outString.getBytes());
    }

    /**
     * Writes only the progressbar indicator without any other information.
     * @throws IOException The write process to the output stream failed.
     */
    private void writeComplete() throws IOException {
        this.out.write(this.getProgressString().getBytes());
    }

    /**
     * Builds the progressbar indicator: First the left bracket, then the progress indicator text, then the indicators
     * for the progress to be made and then a right bracket. On the right side of the indicator is then the percentage
     * of the progress as well as the full process out of the maximum:
     * [▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉----------------------] 56.80% (284/500)
     * @return The progressbar indicator.
     */
    private String getProgressString() {
        String progressString = "\r";
        progressString += "[";
        progressString += PROGRESS_TEXT.repeat(this.getProgressWidth());
        progressString += NO_PROGRESS_TEXT.repeat(this.width - this.getProgressWidth());
        progressString += "]";
        progressString += String.format(" %3.2f%s", this.getProgressPercentage() * 100, "%");
        progressString += String.format(" (%d/%d)", this.progress, this.max);
        return progressString;
    }


    /**
     * Write operation called from within this tread.
     * This operation does progress the spinner. The progressbar indicator is updated and the new or existing
     * message  is displayed.
     * @throws IOException The write process to the output stream failed.
     */
    private void writeInternal() throws IOException, InterruptedException {
        this.spinnerStep = this.spinnerStep + 1 < SPINNER.length ? this.spinnerStep + 1 : 0;
        this.writeExternal();
    }

    /**
     * Starts this thread by calling the {@link #writeInternal()} method in a loop until the progress is complete. If
     * complete the {@link #writeComplete()} method is called and this thread is interrupted, terminating this thread.
     */
    public void run() {
        try {
            while (!isComplete()) {
                this.writeInternal();
                Thread.sleep(this.updateRate);
            }
            this.writeComplete();
            this.interrupt();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
