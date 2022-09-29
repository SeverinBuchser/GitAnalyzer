package org.severin.ba.util.log;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

class FormattingTest {

    @Test
    void getBytes() {
        assertEquals(
                "---------------------------------------------------------\n",
                FormattingTest.bytesToString(Formatting.DELIMITER.getBytes())
        );

        assertEquals(
                ";",
                FormattingTest.bytesToString(Formatting.SEMICOLON.getBytes())
        );

        assertEquals(
                "\n",
                FormattingTest.bytesToString(Formatting.NEW_LINE.getBytes())
        );
    }

    private static String bytesToString(byte[] bytes) {
        return new String(bytes, Charset.defaultCharset());
    }

    @Test
    void values() {
        Formatting[] formattings = Formatting.values();
        assertEquals(3, formattings.length);
        assertEquals(Formatting.DELIMITER, formattings[0]);
        assertEquals(Formatting.SEMICOLON, formattings[1]);
        assertEquals(Formatting.NEW_LINE, formattings[2]);
    }

    @Test
    void valueOf() {
        Formatting delimiter = Formatting.valueOf("DELIMITER");
        assertEquals(Formatting.DELIMITER, delimiter);

        Formatting semicolon = Formatting.valueOf("SEMICOLON");
        assertEquals(Formatting.SEMICOLON, semicolon);

        Formatting new_line = Formatting.valueOf("NEW_LINE");
        assertEquals(Formatting.NEW_LINE, new_line);
    }
}