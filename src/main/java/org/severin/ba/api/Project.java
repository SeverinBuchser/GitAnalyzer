package org.severin.ba.api;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.severin.ba.merge.ConflictingMerge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public ArrayList<ConflictingMerge> getConflictingMerges() throws Exception {
        Iterable<RevCommit> commits = this.log().setRevFilter(RevFilter.ONLY_MERGES).call();
        ArrayList<ConflictingMerge> merges = new ArrayList<>();

        for (RevCommit commit: commits) {
            RevCommit[] parents = commit.getParents();
            RecursiveMerger merger = this.createMerger();

            boolean canMerge = merger.merge(
                    parents[0].toObjectId(),
                    parents[1].toObjectId()
            );

            if (!canMerge) {
                merges.add(new ConflictingMerge(commit, merger));
            }

        }

        return merges;
    }

    private RecursiveMerger createMerger() {
        return (RecursiveMerger) MergeStrategy.RECURSIVE.newMerger(this.getRepository(), true);
    }
}