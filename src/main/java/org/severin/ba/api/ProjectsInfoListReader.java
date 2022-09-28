package org.severin.ba.api;

import java.io.*;
import java.util.ArrayList;

public class ProjectsInfoListReader extends BufferedReader {

    public ProjectsInfoListReader(Reader in) {
        super(in);
    }

    public ArrayList<ProjectInfo> readProjectInfoList() throws IOException {
        this.readLine();
        ArrayList<ProjectInfo> projectInfoList = new ArrayList<>();
        String line;
        while ((line = this.readLine()) != null) {
            String[] projectInfo = line.split(",");
            projectInfoList.add(new ProjectInfo(projectInfo[0], projectInfo[1]));
        }
        return projectInfoList;
    }

    public static ArrayList<ProjectInfo> readListFile(String pathToInfoList) throws Exception {
        if (pathToInfoList == null) {
            pathToInfoList = "projects.csv";
        }

        InputStream is = ProjectsInfoListReader.class.getClassLoader().getResourceAsStream(pathToInfoList);

        if (is == null) {
            throw new Exception("File " + pathToInfoList + " does not exist!");
        }

        InputStreamReader isr = new InputStreamReader(is);
        ProjectsInfoListReader reader = new ProjectsInfoListReader(isr);
        ArrayList<ProjectInfo> projectInfoList = reader.readProjectInfoList();
        is.close();

        return projectInfoList;
    }
}
