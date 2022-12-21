package ch.unibe.inf.seg.gitanalyzer.project;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingMerge;
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

/**
 * A project representing a git project.
 * The project inherits all functionality of the {@link Git} class and adds some new functionality. A project can be
 * created from a repository and a name, cloned from an url to a path and loaded from a path. The project is also able
 * to gather information about itself like commit count etc. The most important functionality is that it can find every
 * conflicting merge the project has.
 */
public class Project extends Git implements Iterable<ConflictingMerge> {
    /**
     * The name of the project.
     */
    public final String name;

    /**
     * Initiates a new project.
     * @param repo The repository of the project.
     * @param name The name of the project.
     */
    public Project(Repository repo, String name) {
        super(repo);
        this.name = name;
    }

    /**
     * Clones a project from an url to a path.
     * @param url The url to the remote repository.
     * @param path The path to clone the project to.
     * @param name The name of the project.
     * @return The project which was cloned to the path.
     * @throws GitAPIException If the clone command is unsuccessful.
     */
    public static Project cloneFromUri(String url, String path, String name) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(new File(path))
                .call();
        return new Project(git.getRepository(), name);
    }

    /**
     * Loads a project from a path pointing to the Git project.
     * @param path The path to the local git project without the ".git" extension.
     * @param name The name of the project.
     * @return The project located at the path.
     * @throws IOException If the path does not point to a valid location or the directory is not a git directory.
     */
    public static Project buildFromPath(Path path, String name) throws IOException {
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(Project.buildGitPath(path).toFile())
                .build();
        return new Project(repo, name);
    }

    /**
     * Appends the ".git" extension to a path.
     * @param path The path to extend.
     * @return The extended path.
     */
    private static Path buildGitPath(Path path) {
        return Paths.get(path.toString(), ".git");
    }

    /**
     * Creates an in-core recursive merger for the repository of this project.
     * @return A new in-core recursive merger for the repository of this project.
     */
    private RecursiveMerger createMerger() {
        return (RecursiveMerger) MergeStrategy.RECURSIVE.newMerger(this.getRepository(), true);
    }

    /**
     * Retrieves the number of commits, merges and octopus merges of this project.
     * @return An array containing the commit count at index 0, the merge count at index 1 and the octopus merge count
     * at index 2.
     * @throws GitAPIException If the log command fails.
     */
    public Integer[] getCommitsMergesOctopusMergesCount() throws GitAPIException {
        Iterable<RevCommit> commits = this.log().call();
        int commitsCount = 0;
        int mergeCount = 0;
        int octopusMergeCount = 0;
        for( RevCommit commit : commits ) {
            commitsCount++;
            if (commit.getParentCount() >= 2) mergeCount++;
            if (commit.getParentCount() > 2) octopusMergeCount++;
        }
        return new Integer[]{commitsCount, mergeCount, octopusMergeCount};
    }

    /**
     * Retrieves the tag count for this project.
     * @return The tag count for this project.
     * @throws GitAPIException If the tagList command fails.
     */
    public Integer getTagsCount() throws GitAPIException {
        List<Ref> tags = this.tagList().call();
        int tagCount = 0;
        for( Ref ignored : tags ) {
            tagCount++;
        }
        return tagCount;
    }

    /**
     * Retrieves the contributor count for this project.
     * Warning: The method counts contributors using multiple accounts multiple times.
     * @return The contributor count for this project.
     */
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

    /**
     * Iterator over every conflicting merge of this project.
     * The iteration is memory friendly and only generates the conflicting merges when called upon. The generated
     * iterator wraps the iterator from the log command of git. The log command is set to only log merges, so the merges
     * have to be re-merged in order to find the conflicting merges. The re-merge is done using a three-way recursive
     * merge, so only merges with exactly two parents are allowed while the octopus merges are ignored.
     * @return Iterator over every conflicting merge of this project.
     */
    @Override
    public Iterator<ConflictingMerge> iterator() {
        return new Iterator<>() {
            /**
             * The merges to check for conflicts.
             */
            private final Iterator<RevCommit> merges;
            /**
             * The current conflicting merge.
             */
            private ConflictingMerge currentConflictingMerge;
            /**
             * Flag to indicate whether the current conflicting merge was already retrieved.
             */
            private boolean retrievedCurrentConflictingMerge = true;

            {
                try {
                    merges = log().setRevFilter(RevFilter.ONLY_MERGES).call().iterator();
                } catch (GitAPIException e) {
                    throw new RuntimeException(e);
                }
            }

            /**
             * Finds the next conflicting merge.
             * Since the merges are stored in an iterator, the progress does not get lost. So the method continues at
             * the next merge in the iterator and checks if the merge is a three-way merge by checking the parent count.
             * If it is a three-way merge, the recursive merger tries to re-merge the parents of the merge. If
             * successful, the next merge is checked. If the merge fails, the merge contains conflicts and a new
             * conflicting merge is created and stored in the {@link #currentConflictingMerge} field. If there are no
             * more merges left, the {@link #currentConflictingMerge} is set to {@code null}.
             * @throws IOException some sources could not be read or outputs could not be written to the repository.
             */
            private void findNextConflictingMerge() throws IOException {
                RevCommit merge = null;
                RecursiveMerger merger = null;
                RevCommit[] parents;
                boolean canMerge = true;

                while (canMerge && this.merges.hasNext()) {
                    merge = this.merges.next();
                    merger = createMerger();
                    parents = merge.getParents();
                    // limit to three-way merges
                    if (parents.length != 2) continue;

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

            /**
             * Checks if there is another conflicting merge.
             * Since all merges need to be checked at runtime, this method will try to find the next conflicting merge
             * by calling {@link #findNextConflictingMerge()} but only if the last conflicting merge is already
             * retrieved. If there is another conflicting merge, the {@link #retrievedCurrentConflictingMerge} flag
             * is set to false and the method returns true.
             * @return True if there is another conflicting merge and false otherwise.
             */
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

            /**
             * Returns the next conflicting merge.
             * If there is another conflicting merge, the next one will be returned. Otherwise, {@code null} will be
             * returned in order to ease the handling of the case where no further element exists.
             * @return The next conflicting merge or {@code null}.
             */
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