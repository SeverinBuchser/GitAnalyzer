package org.severin.ba.util.log;

import java.nio.charset.Charset;

public enum Formatting {
    DELIMITER("---------------------------------------------------------\n"),
    SEMICOLON(";"),
    NEW_LINE("\n");

    private final byte[] bytes;

    Formatting(String string) {
        this.bytes = string.getBytes(Charset.defaultCharset());
    }

    public byte[] getBytes() {
        return bytes;
    }
}
