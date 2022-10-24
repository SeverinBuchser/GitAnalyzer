package org.severin.ba.api;

import org.severin.ba.util.log.CSVFile;

import java.io.File;
import java.util.Iterator;
import java.util.stream.Stream;

public class ProjectsInfoListReader extends CSVFile implements Iterable<ProjectInfo> {

    public ProjectsInfoListReader(File file) {
        super(file, new String[]{"name", "url"});
    }

    private Stream<ProjectInfo> getInfoStream() {
        return this.getStream()
                .map(record -> new ProjectInfo(record.get("name"), record.get("url")));
    }

    @Override
    public Iterator<ProjectInfo> iterator() {
        return this.getInfoStream().iterator();
    }
}
