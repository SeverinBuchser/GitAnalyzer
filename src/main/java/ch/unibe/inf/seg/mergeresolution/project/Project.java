package ch.unibe.inf.seg.mergeresolution.project;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingMerge;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Project extends Git {

    public Project(Repository repo) {
        super(repo);
    }

    public static Project cloneFromUri(String url, String path) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(new File(path))
                .call();
        return new Project(git.getRepository());
    }

    public static Project buildFromPath(Path path) throws IOException {
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(Project.buildGitPath(path).toFile())
                .build();
        return new Project(repo);
    }

    private static Path buildGitPath(Path path) {
        return Paths.get(path.toString(), ".git");
    }

    public ArrayList<ConflictingMerge> getConflictingMerges() throws Exception {
        Iterable<RevCommit> merges = this.log().setRevFilter(RevFilter.ONLY_MERGES).call();
        ArrayList<ConflictingMerge> conflictingMerges = new ArrayList<>();

        for (RevCommit merge: merges) {
            RevCommit[] parents = merge.getParents();
            RecursiveMerger merger = this.createMerger();

            boolean canMerge = merger.merge(
                    parents[0].toObjectId(),
                    parents[1].toObjectId()
            );

            if (!canMerge) {
                conflictingMerges.add(new ConflictingMerge(merge, merger));
            }

        }

        return conflictingMerges;
    }

    private RecursiveMerger createMerger() {
        return (RecursiveMerger) MergeStrategy.RECURSIVE.newMerger(this.getRepository(), true);
    }
}