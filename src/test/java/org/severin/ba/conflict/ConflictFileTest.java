package org.severin.ba.conflict;

import org.junit.jupiter.api.Test;
import org.severin.ba.resolution.ResolutionFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ConflictFileTest {

    @Test
    public void shouldFindOneConflict() throws IOException {
        assertEquals(1, this.readFileOne().size());
    }

    @Test
    public void shouldFindTwoConflicts() throws IOException {
        assertEquals(2, this.readFileTwo().size());
    }

    @Test
    public void shouldBuildTwoResolutions() throws IOException {
        ConflictFile conflictFile = this.readFileOne();
        ArrayList<ResolutionFile> resolutions = conflictFile.buildResolutions();
        assertEquals(2, resolutions.size());
    }

    @Test
    public void shouldBuildFourResolutions() throws IOException {
        ConflictFile conflictFile = this.readFileTwo();
        ArrayList<ResolutionFile> resolutions = conflictFile.buildResolutions();
        assertEquals(4, resolutions.size());
        System.out.println(resolutions.get(3).getCharContent(true));
        assertEquals(
                this.readResource("/resolutions/file2.resolution-0.txt"),
                resolutions.get(0).getCharContent(true)
        );
        assertEquals(
                this.readResource("/resolutions/file2.resolution-1.txt"),
                resolutions.get(1).getCharContent(true)
        );
        assertEquals(
                this.readResource("/resolutions/file2.resolution-2.txt"),
                resolutions.get(2).getCharContent(true)
        );
        assertEquals(
                this.readResource("/resolutions/file2.resolution-3.txt"),
                resolutions.get(3).getCharContent(true)
        );
    }

    private ConflictFile readFileOne() throws IOException {
        return new ConflictFile(this.readResource("/conflicting-files/file1.txt"));
    }

    private ConflictFile readFileTwo() throws IOException {
        return new ConflictFile(this.readResource("/conflicting-files/file2.txt"));
    }

    private String readResource(String filePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(filePath);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
