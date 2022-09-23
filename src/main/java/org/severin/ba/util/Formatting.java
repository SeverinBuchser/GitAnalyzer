package org.severin.ba.util;

import java.nio.charset.Charset;

public enum Formatting {
    DELIMITER("---------------------------------------------------------\n"),
    SEMICOLON(";"),
    NEW_LINE("\n");

    private final byte[] bytes;

    private Formatting(String string) {
        this.bytes = string.getBytes(Charset.defaultCharset());
    }

    public byte[] getBytes() {
        return bytes;
    }
}
