package ch.unibe.inf.seg.gitanalyzer.project;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingMerge;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ProjectTest {
    @Test
    void findConflictsTest() throws Exception {
        ProjectInfo projectInfo = new ProjectInfo("samplemergeconflictproject", "");
        Project project = projectInfo.toProject(Paths.get("src","test","resources").toAbsolutePath());

        Iterator<ConflictingMerge> conflictingMerges = project.iterator();

        assertEquals("16793e9c9192a5f86d4523bb9ecdd33c20ab9305", conflictingMerges.next().getCommitId());
        assertEquals("8093b82e8ee92795f8f4dade96cf90de2f31c24c", conflictingMerges.next().getCommitId());
        assertEquals("4d384506fe3d587e2465e639d21867bd7197dad4", conflictingMerges.next().getCommitId());
        assertEquals("70cf0e42600be4e71945c1102cb5a85ef6bbde82", conflictingMerges.next().getCommitId());
        assertFalse(conflictingMerges.hasNext());
        project.close();
    }

}