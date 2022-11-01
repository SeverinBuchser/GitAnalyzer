package ch.unibe.inf.seg.mergeresolution.project;

import ch.unibe.inf.seg.mergeresolution.conflict.Conflict;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ProjectTest {
    @Test
    void findConflictsTest() throws Exception {
        Project project = Project.buildFromPath("/home/severin/gitrepo/Severin/UniBe/samplemergeconflictproject");

        ArrayList<Conflict> conflicts = project.getConflictingMerges();

        assertEquals(4, conflicts.size());

        assertEquals("16793e9c9192a5f86d4523bb9ecdd33c20ab9305", conflicts.get(0).getCommitName());
        assertEquals("8093b82e8ee92795f8f4dade96cf90de2f31c24c", conflicts.get(1).getCommitName());
        assertEquals("4d384506fe3d587e2465e639d21867bd7197dad4", conflicts.get(2).getCommitName());
        assertEquals("70cf0e42600be4e71945c1102cb5a85ef6bbde82", conflicts.get(3).getCommitName());

        project.close();
    }

}