package ch.unibe.inf.seg.mergeresolution.project;

public class ProjectInfo {
    public final String name;
    public final String url;

    public ProjectInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format("Project: %s, URL: %s", this.name, this.url);
    }
}
