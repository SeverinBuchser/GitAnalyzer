package org.severin.ba.resolution;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class ResolutionFile extends SimpleJavaFileObject {
    private final String code;

    public ResolutionFile(String name, String code) {
        super(
                URI.create("string:///" + name),
                Kind.SOURCE
        );
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return this.code;
    }
}