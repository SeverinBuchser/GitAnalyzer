package org.severin.ba.project;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.severin.ba.conflict.Merge;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    private Project project;

    private static final ObjectId[] merges = {
            ObjectId.fromString("16793e9c9192a5f86d4523bb9ecdd33c20ab9305"),
            ObjectId.fromString("bac74d2e675e24592eaf3d3bea73b27c6b00b727"),
            ObjectId.fromString("0861e4bf09be1fc5b62ab0e181fb194617dddd09"),
            ObjectId.fromString("8093b82e8ee92795f8f4dade96cf90de2f31c24c"),
            ObjectId.fromString("4d384506fe3d587e2465e639d21867bd7197dad4"),
            ObjectId.fromString("70cf0e42600be4e71945c1102cb5a85ef6bbde82")
    };

    private static final ObjectId[] conflicts = {
            ObjectId.fromString("16793e9c9192a5f86d4523bb9ecdd33c20ab9305"),
            ObjectId.fromString("8093b82e8ee92795f8f4dade96cf90de2f31c24c"),
            ObjectId.fromString("4d384506fe3d587e2465e639d21867bd7197dad4"),
            ObjectId.fromString("70cf0e42600be4e71945c1102cb5a85ef6bbde82")
    };

    @BeforeEach
    void generateProject() throws IOException {
        this.project = Project.buildFromPath(
                "samplemergeconflictproject",
                "/home/severin/gitrepo/Severin/UniBe"
        );
    }

    @Test
    void findMerges() throws GitAPIException {
        Iterable<Merge> merges = this.project.getMerges();
        int mergeCount = 0;
        for (Merge merge: merges) {
            assertTrue(merge.getCommit().toObjectId().equals(ProjectTest.merges[mergeCount]));
            mergeCount++;
        }
        assertEquals(ProjectTest.merges.length, mergeCount);
    }

    @Test
    void findConflicts() throws GitAPIException, IOException {
        Iterable<Merge> conflicts = this.project.getConflicts();
        int conflictCount = 0;
        for (Merge conflict: conflicts) {
            assertTrue(conflict.getCommit().toObjectId().equals(ProjectTest.conflicts[conflictCount]));
            conflictCount++;
        }
        assertEquals(ProjectTest.conflicts.length, conflictCount);
    }
}