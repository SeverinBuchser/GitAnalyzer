package org.severin.ba.api;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.severin.ba.mergeconflict.Conflict;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Project extends Git {

    public Project(Repository repo) {
        super(repo);
    }

    public static Project cloneFromUri(String name, String uri, String cloneDestination) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI(uri)
                .setDirectory(new File(cloneDestination + "/" + name))
                .call();
        return new Project(git.getRepository());
    }

    public static Project buildFromPath(String name, String pathToProjects) throws IOException {
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(new File(pathToProjects + "/" + name + "/.git"))
                .build();
        return new Project(repo);
    }

    public ArrayList<Conflict> getConflictingMerges() throws Exception {
        // create new iterator for conflicts with iterable<revcommit> inside --> performance
        Iterable<RevCommit> merges = this.log().setRevFilter(RevFilter.ONLY_MERGES).call();
        ArrayList<Conflict> conflicts = new ArrayList<>();

        for (RevCommit merge: merges) {
            RevCommit[] parents = merge.getParents();
            RecursiveMerger merger = this.createMerger();

            boolean canMerge = merger.merge(
                    parents[0].toObjectId(),
                    parents[1].toObjectId()
            );

            if (!canMerge) {
                conflicts.add(new Conflict(merge, merger));
            }

        }

        return conflicts;
    }

    private RecursiveMerger createMerger() {
        return (RecursiveMerger) MergeStrategy.RECURSIVE.newMerger(this.getRepository(), true);
    }
}