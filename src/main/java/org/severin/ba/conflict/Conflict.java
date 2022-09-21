package org.severin.ba.conflict;

import java.util.regex.*;

public class Conflict {
    private static final Pattern conflictRegExp = Pattern.compile("(?:<{7} (.+)\n)((?:.*\n)*?)(?:={7}\n)((?:.*\n)*?)(?:>{7} (.+)\n?)");
    private static final int yoursNameGroup = 1;
    private static final int yoursGroup = 2;
    private static final int theirsNameGroup = 4;
    private static final int theirsGroup = 3;

    private String yoursName;
    private String yours;
    private String theirsName;
    private String theirs;

    private final int start;
    private final int end;

    public Conflict(String conflict, int start, int end) throws IllegalStateException {
        this.parse(conflict);
        this.start = start;
        this.end = end;
    }

    private void parse(String conflict) throws IllegalStateException {
        Matcher matcher = Conflict.conflictRegExp.matcher(conflict);
        if (matcher.matches()) {
            this.yoursName = matcher.group(Conflict.yoursNameGroup);
            this.yours = matcher.group(Conflict.yoursGroup);
            this.theirsName = matcher.group(Conflict.theirsNameGroup);
            this.theirs = matcher.group(Conflict.theirsGroup);
        } else {
            throw new IllegalStateException("No match for a conflict has been found!");
        }
    }

    public String getYoursName() {
        return this.yoursName;
    }

    public String getYours() {
        return this.yours;
    }

    public String getTheirsName() {
        return this.theirsName;
    }

    public String getTheirs() {
        return this.theirs;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public String toString() {
        return "<<<<<<< " + this.yoursName + "\n"
                + this.yours
                + "=======\n"
                + this.theirs
                + ">>>>>>> " + this.theirsName;
    }
}
