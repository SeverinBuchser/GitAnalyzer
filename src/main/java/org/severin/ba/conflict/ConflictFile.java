package org.severin.ba.conflict;

import org.severin.ba.resolution.ResolutionConfig;
import org.severin.ba.resolution.ResolutionFile;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConflictFile extends ArrayList<Conflict> {
    private static final Pattern conflictsRegExp = Pattern.compile("(?:<{7} .+\n)(?:.*\n)*?(?:>{7} .+)");
    private static final String placeholderStart = "<<<<<<<";
    private static final String placeholderEnd = ">>>>>>>";
    private static final Pattern placeholderRegExp = Pattern.compile("(?:<{7})(\\d*)(?:>{7})");
    private static final int idGroup = 1;

    private final ArrayList<ResolutionConfig> configs;

    private String name;
    private final String originalCode;
    private String gapCode;

    public ConflictFile(String code) {
        this.originalCode = code;
        this.parseConflicts();
        this.configs = this.buildResolutionConfigs();
    }


    private void parseConflicts() {
        Matcher matcher = ConflictFile.conflictsRegExp.matcher(this.originalCode);
        while(matcher.find()) {
            this.gapCode = matcher.replaceFirst((MatchResult result) -> {
                int start = result.start();
                int end = result.end();
                Conflict conflict = new Conflict(result.group(0), start, end);
                this.add(conflict);
                return ConflictFile.getPlaceholder(this.size() - 1);
            });
            matcher.reset(this.gapCode);
        }
    }


    private ArrayList<ResolutionConfig> buildResolutionConfigs() {
        ArrayList<ResolutionConfig> configs = new ArrayList<>();
        for (int versionIndex = 0 ; versionIndex < Math.pow(2, this.size()) ; versionIndex++) {
            configs.add(this.buildResolutionConfig(versionIndex));
        }
        return configs;
    }


    private ResolutionConfig buildResolutionConfig(int versionIndex) {
        ResolutionConfig config = new ResolutionConfig();
        for (int conflictIndex = 0 ; conflictIndex < this.size() ; conflictIndex++) {
            config.add(((versionIndex >> conflictIndex) & 1) == 1);
        }
        return config;
    }


    public ArrayList<ResolutionFile> buildResolutions() {
        ArrayList<ResolutionFile> resolutions = new ArrayList<>();
        for (ResolutionConfig config: this.configs) {
            resolutions.add(this.buildResolution(config));
        }
        return resolutions;
    }


    private ResolutionFile buildResolution(ResolutionConfig config) {
        String resolution = this.gapCode;
        Matcher matcher = ConflictFile.placeholderRegExp.matcher(resolution);

        while(matcher.find()) {
            resolution = matcher.replaceFirst((MatchResult result) -> {
                int conflictIndex = Integer.parseInt(result.group(ConflictFile.idGroup));
                if (config.isYoursUsed(conflictIndex)) {
                    return this.get(conflictIndex).getYours();
                } else {
                    return this.get(conflictIndex).getTheirs();
                }
            });
            matcher.reset(resolution);
        }
        return new ResolutionFile(this.name, resolution);
    }


    private static String getPlaceholder(int conflictIndex) {
        return ConflictFile.placeholderStart + conflictIndex
                + ConflictFile.placeholderEnd;
    }
}