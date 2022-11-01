package ch.unibe.inf.seg.mergeresolution.project;

import ch.unibe.inf.seg.mergeresolution.util.csv.CSVFile;

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
