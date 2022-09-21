package org.severin.ba.project;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.severin.ba.conflict.ConflictFile;
import org.severin.ba.conflict.ConflictingFiles;
import org.severin.ba.conflict.Merge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;

public class Project extends Git {
    private final String name;

    public Project(Repository repo, String name) {
        super(repo);
        this.name = name;
    }

    public static Project cloneFromUri(String name, String uri, String cloneDestination) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI(uri)
                .setDirectory(new File(cloneDestination + "/" + name))
                .call();
        return new Project(git.getRepository(), name);

    }

    public static Project buildFromPath(String name, String pathToProjects) throws IOException {
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(new File(pathToProjects + "/" + name + "/.git"))
                .build();
        return new Project(repo, name);
    }

    public Path getRootDir() {
        return this.getRepository().getDirectory().toPath().getParent();
    }

    public ConflictingFiles getCurrentConflicts() throws GitAPIException, IOException {
        Status status = this.status().call();
        Set<String> names = status.getConflicting();

        ConflictingFiles conflictFiles = new ConflictingFiles();

        for (String name : names) {
            Path filePath = this.getRootDir().resolve(name);
            conflictFiles.add(new ConflictFile(Files.readString(filePath)));
        }

        return conflictFiles;
    }

    public Iterable<Merge> getMerges() throws GitAPIException {
        Iterable<RevCommit> commits = this.log().setRevFilter(RevFilter.ONLY_MERGES).call();
        ArrayList<Merge> merges = new ArrayList<>();

        for (RevCommit commit: commits) {
            try {
                merges.add(new Merge(commit, this.getRepository()));
            } catch (Exception e) {
            }
        }

        return merges;
    }

    public Iterable<Merge> getConflicts() throws GitAPIException, IOException {
        Iterable<Merge> merges = this.getMerges();
        ArrayList<Merge> conflicts = new ArrayList<>();

        for (Merge merge: merges) {
            RevCommit[] parents = merge.getCommit().getParents();

            RecursiveMerger merger = (RecursiveMerger) MergeStrategy.RECURSIVE.newMerger(
                    this.getRepository(),
                    true
            );
            boolean canMerge = merger.merge(
                    parents[0].toObjectId(),
                    parents[1].toObjectId()
            );
            if (!canMerge) {
                conflicts.add(merge);
            }
        }

        return conflicts;
    }
}
