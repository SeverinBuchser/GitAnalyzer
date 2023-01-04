package ch.unibe.inf.seg.gitanalyzer.project;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.csv.CSVFile;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectInfosTest {
    @Test
    void infoListReaderTest() throws IOException {
        File file = new File(Objects.requireNonNull(ProjectInfos.class.getClassLoader()
                .getResource("sample-project-list.csv")).getFile());

        FileReader reader = new FileReader(file);
        CSVParser csvParser = new CSVParser(reader, ProjectList.READ_FORMAT);
        CSVFile csvFile = new CSVFile(csvParser);
        ProjectInfos projectInfos = new ProjectInfos(csvFile);
        csvParser.close();
        reader.close();

        assertEquals(6, projectInfos.size());

        assertEquals("project0", projectInfos.get(0).getName());
        assertEquals("project1", projectInfos.get(1).getName());
        assertEquals("project2", projectInfos.get(2).getName());
        assertEquals("project3", projectInfos.get(3).getName());
        assertEquals("project4", projectInfos.get(4).getName());
        assertEquals("project5", projectInfos.get(5).getName());

        assertEquals("linktoproject0", projectInfos.get(0).getRemoteURL());
        assertEquals("linktoproject1", projectInfos.get(1).getRemoteURL());
        assertEquals("linktoproject2", projectInfos.get(2).getRemoteURL());
        assertEquals("linktoproject3", projectInfos.get(3).getRemoteURL());
        assertEquals("linktoproject4", projectInfos.get(4).getRemoteURL());
        assertEquals("linktoproject5", projectInfos.get(5).getRemoteURL());
    }
}