package org.severin.ba;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.severin.ba.conflict.ConflictingFiles;
import org.severin.ba.project.Project;
import org.severin.ba.resolution.ResolutionFile;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, GitAPIException {
        Project project = Project.buildFromPath(
                "4pr0n/ripme",
                "/home/severin/ba_projects"
        );
        ConflictingFiles conflicts = project.getCurrentConflicts();
        ArrayList<ArrayList<ResolutionFile>> allResolutions = conflicts.buildAllResolutions();

        // ArrayList<ResolutionFile> versions = this.get(0).buildVersions();
        // JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        // ArrayList<ResolutionFile> vs = new ArrayList<ResolutionFile>();
        // vs.add(versions.get(0));
        // System.out.println(compiler.getTask(null, fileManager, null, null, null, vs).call());

        Iterable<RevCommit> merges = project.log().setRevFilter(RevFilter.ONLY_MERGES).call();
        for (RevCommit merge: merges) {
            RevCommit[] parents = merge.getParents(); // the two commits which are merged
        }

        project.close();
    }
}