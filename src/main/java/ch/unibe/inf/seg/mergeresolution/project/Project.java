package ch.unibe.inf.seg.mergeresolution.project;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingMerge;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class Project extends Git implements Iterable<ConflictingMerge> {

    public final String name;

    public Project(Repository repo, String name) {
        super(repo);
        this.name = name;
    }

    public static Project cloneFromUri(String url, String path, String name) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(new File(path))
                .call();
        return new Project(git.getRepository(), name);
    }

    public static Project buildFromPath(Path path, String name) throws IOException {
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(Project.buildGitPath(path).toFile())
                .build();
        return new Project(repo, name);
    }

    private static Path buildGitPath(Path path) {
        return Paths.get(path.toString(), ".git");
    }

    private RecursiveMerger createMerger() {
        return (RecursiveMerger) MergeStrategy.RECURSIVE.newMerger(this.getRepository(), true);
    }

    public Integer getCommitsCount() throws GitAPIException {
        Iterable<RevCommit> commits = this.log().call();
        int commitCount = 0;
        for( RevCommit ignored : commits ) {
            commitCount++;
        }
        return commitCount;
    }

    public Integer getMergesCount() throws GitAPIException {
        Iterable<RevCommit> merges = this.log().setRevFilter(RevFilter.ONLY_MERGES).call();
        int mergeCount = 0;
        for( RevCommit ignored : merges ) {
            mergeCount++;
        }
        return mergeCount;
    }

    public Integer getTagsCount() throws GitAPIException {
        List<Ref> tags = this.tagList().call();
        int tagCount = 0;
        for( Ref ignored : tags ) {
            tagCount++;
        }
        return tagCount;
    }

    public Integer getContributorsCount() {
        try {
            ProcessBuilder builder = new ProcessBuilder("git", "shortlog", "-sne", "--all");
            builder.directory(this.getRepository().getDirectory().getParentFile());
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            int contributorCount = 0;
            while ( reader.readLine() != null) {
                contributorCount++;
            }
            return contributorCount;
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public Iterator<ConflictingMerge> iterator() {
        return new Iterator<>() {
            private final Iterator<RevCommit> merges;
            private ConflictingMerge currentConflictingMerge;
            private boolean retrievedCurrentConflictingMerge = true;

            {
                try {
                    merges = log().setRevFilter(RevFilter.ONLY_MERGES).call().iterator();
                } catch (GitAPIException e) {
                    throw new RuntimeException(e);
                }
            }

            private void findNextConflictingMerge() throws IOException {
                RevCommit merge = null;
                RecursiveMerger merger = null;
                RevCommit[] parents;
                boolean canMerge = true;

                while (canMerge && this.merges.hasNext()) {
                    merge = this.merges.next();
                    merger = createMerger();
                    parents = merge.getParents();

                    canMerge = merger.merge(parents);
                }

                if (!canMerge) {
                    this.currentConflictingMerge = new ConflictingMerge(
                            getRepository(),
                            merge,
                            merger.getMergeResults()
                    );
                } else {
                    this.currentConflictingMerge = null;
                }
            }

            @Override
            public boolean hasNext() {
                if (this.retrievedCurrentConflictingMerge) {
                    try {
                        this.findNextConflictingMerge();
                        this.retrievedCurrentConflictingMerge = false;
                    } catch (IOException e) {
                        return false;
                    }
                }
                return this.currentConflictingMerge != null;
            }

            @Override
            public ConflictingMerge next() {
                if (this.hasNext()) {
                    this.retrievedCurrentConflictingMerge = true;
                    return this.currentConflictingMerge;
                }
                return null;
            }
        };
    }
}