package ch.unibe.inf.seg.gitanalyzer.error;

public class NotUniqueProjectListException extends RuntimeException {

    public NotUniqueProjectListException(String projectList) {
        super(String.format("projectList '%s' is not unique", projectList));
    }

}