package ch.unibe.inf.seg.gitanalyzer.util.logger;

public class GuiLogger extends LoggerDuplicator {
    private final SubscribableByteArrayOutputStream out = new SubscribableByteArrayOutputStream();

    public SubscribableByteArrayOutputStream getOut() {
        return out;
    }

    public GuiLogger() {
        this.setOutOne(new PrintStreamLogger(this.out));
        this.setOutTwo(LoggerProvider.getLogger());
    }
}
