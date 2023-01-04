package ch.unibe.inf.seg.gitanalyzer.cli;

import picocli.CommandLine;

public class VersionProvider implements CommandLine.IVersionProvider {

    @Override
    public String[] getVersion() {
        return new String[] {
                "Git-Analyzer version 1.0.0",
                "Picocli " + CommandLine.VERSION,
                "JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
                "OS: ${os.name} ${os.version} ${os.arch}"
        };
    }
}
