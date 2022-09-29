package org.severin.ba.merge;

import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.merge.MergeChunk;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResolutionChunkTest {

    @Test
    void getText() {
        String text = "line1\nline2\nline3\nline4";
        RawText rawText = new RawText(text.getBytes(Charset.defaultCharset()));
        MergeChunk mergeChunk = mock(MergeChunk.class);

        when(mergeChunk.getBegin()).thenReturn(1);
        when(mergeChunk.getEnd()).thenReturn(3);

        RawResolutionChunk resolutionChunk = new RawResolutionChunk(rawText, mergeChunk);

        assertEquals("line2\nline3\n", resolutionChunk.getText());
    }
}