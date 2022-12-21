package ch.unibe.inf.seg.gitanalyzer.project;

/**
 * Datastructure holing information about a project: name and remote url.
 */
public class ProjectInfo {
    /**
     * The name of the project.
     */
    public final String name;
    /**
     * The remote url of the project.
     */
    public final String url;

    /**
     * Initiates a new project info object with name and remote url.
     * @param name The name of the project.
     * @param url The remote url of the project.
     */
    public ProjectInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Prints the information.
     * @return A string containing the name and remote url of the project.
     */
    @Override
    public String toString() {
        return String.format("Project: %s, URL: %s", this.name, this.url);
    }
}
