package ch.unibe.inf.seg.mergeresolution.project;

import ch.unibe.inf.seg.mergeresolution.util.csv.CSVFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectsInfoListReader extends CSVFile implements Iterable<ProjectInfo> {
    public ProjectsInfoListReader(CSVParser parser) throws IOException {
        super(parser);
    }

    public static ProjectsInfoListReader read(File file) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT
                .builder()
                .setHeader("name", "url")
                .setSkipHeaderRecord(true)
                .build();
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileReader reader = new FileReader(file);
        CSVParser parser = format.parse(reader);
        ProjectsInfoListReader projectsInfoListReader = new ProjectsInfoListReader(parser);
        reader.close();
        return projectsInfoListReader;
    }

    @Override
    public Iterator<ProjectInfo> iterator() {
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        JSONArray jsonRecords = this.getJsonRecords();

        for (int i = 0 ; i < jsonRecords.length() ; i++) {
            JSONObject jsonRecord = jsonRecords.getJSONObject(i);
            projectInfoList.add(new ProjectInfo(
                    jsonRecord.getString("name"),
                    jsonRecord.getString("url")
            ));
        }

        return projectInfoList.iterator();
    }
}
