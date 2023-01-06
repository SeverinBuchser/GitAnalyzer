package ch.unibe.inf.seg.gitanalyzer.config;

import ch.unibe.inf.seg.gitanalyzer.error.NotUniqueProjectListException;
import ch.unibe.inf.seg.gitanalyzer.util.json.JSONValidator;
import org.everit.json.schema.ValidationException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ProjectLists implements Iterable<ProjectList> {

    private Config config;

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        this.preconditionBelongsToConfig();
        return this.config;
    }

    private final JSONArray jsonArray;

    private final ArrayList<ProjectList> projectLists = new ArrayList<>();

    public ProjectLists(JSONArray jsonArray, Config config) {
        this(jsonArray);
        this.config = config;
    }

    public ProjectLists(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.jsonArray.forEach(o -> this.projectLists.add(new ProjectList((JSONObject) o, this)));
        this.preconditionSchema();
    }

    /**
     * Precondition method, checking if the supplied JSON array is valid based on the 'projectLists.json' schema as
     * well as checking if the list property of each project list is unique.
     * @throws ValidationException the JSON array is not valid against the schema.
     * @throws NotUniqueProjectListException one or more list properties have duplicates.
     */
    private void preconditionSchema() {
        JSONValidator.validate(this.jsonArray, "projectLists.json");

        ArrayList<String> keys = new ArrayList<>();
        for (ProjectList projectList : this.projectLists) {
            String key = projectList.getListPath().toString();
            if (keys.contains(key)) {
                throw new NotUniqueProjectListException(key);
            } else keys.add(key);
        }
    }
    /**
     * Precondition method, checking if the project lists belong to a config.
     * @throws IllegalStateException the project lists do not belong to a config.
     */
    private void preconditionBelongsToConfig() {
        if (!this.belongsToConfig()) throw new IllegalStateException("The project lists do not belong to a config.");
    }

    public boolean belongsToConfig() {
        return this.config != null;
    }


    public boolean isEmpty() {
        return this.projectLists.size() == 0;
    }

    public int indexOf(ProjectList projectList) {
        return this.indexOf(projectList.getListPath().toString());
    }

    public int indexOf(String list) {
        int index = 0;
        for (ProjectList projectList : this.projectLists) {
            if (projectList.getListPath().toString().equals(list)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public ProjectList get(ProjectList projectList) {
        return this.get(projectList.getListPath().toString());
    }

    public ProjectList get(String list) {
        int index = this.indexOf(list);
        if (index < 0 || index >= this.size()) return null;
        return this.get(index);
    }

    public ProjectList get(int index) {
        return this.projectLists.get(index);
    }

    public boolean has(ProjectList projectList) {
        return this.has(projectList.getListPath().toString());
    }

    public boolean has(String projectList) {
        for (ProjectList projectListInfo: this.projectLists) {
            if (projectListInfo.getListPath().toString().equals(projectList)) {
                return true;
            }
        }
        return false;
    }

    public void add(ProjectList projectList) {
        if (!this.has(projectList)) {
            projectList.setProjectLists(this);
            this.projectLists.add(projectList);
            this.jsonArray.put(projectList.getJsonObject());
        }
    }

    public ProjectList remove(ProjectList projectList) {
        return this.remove(projectList.getListPath().toString());
    }

    public ProjectList remove(String projectList) {
        int index = this.indexOf(projectList);
        if (index >= 0) {
            ProjectList projectListInfo = this.projectLists.get(index);
            this.projectLists.remove(index);
            this.jsonArray.remove(index);
            projectListInfo.setProjectLists(null);
            return projectListInfo;
        } else return null;
    }

    @Override
    public Iterator<ProjectList> iterator() {
        return this.projectLists.iterator();
    }

    public int size() {
        return this.projectLists.size();
    }

    @Override
    public String toString() {
        return this.jsonArray.toString(4);
    }
}
