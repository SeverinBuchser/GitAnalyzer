package org.severin.ba.util.log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CSVFile {

    private final String path;
    private FileWriter out;

    private CSVPrinter printer;

    private Iterable<CSVRecord> records;

    private final CSVFormat writeFormat;
    private final CSVFormat readFormat;

    public CSVFile(String path, String[] headers, boolean eraseOld) throws IOException {
        this(path, headers);
        if (eraseOld) {
            this.open();
            this.out.write("");
            this.close();
        }
    }

    public CSVFile(String path, String[] headers) {
        this.path = path;
        this.writeFormat = CSVFormat.DEFAULT
                .builder()
                .setHeader(headers)
                .build();
        this.readFormat = CSVFormat.DEFAULT
                .builder()
                .setHeader(headers)
                .setSkipHeaderRecord(true)
                .build();
    }

    private void parse() {
        try {
            FileReader in = new FileReader(this.path);
            this.records = new ArrayList<>(this.readFormat.parse(in).stream().toList());
            in.close();
        } catch (IOException e) {
            this.records = new ArrayList<>();
        }
    }

    private void open() throws IOException {
        this.out = new FileWriter(this.path);
        this.printer = new CSVPrinter(this.out, this.writeFormat);
    }

    private void close() throws IOException {
        assert this.out != null;
        this.out.close();
        this.printer.close();
    }

    private void writeExistingRecords() throws IOException {
        this.printer.printRecords(this.records);
    }

    public void appendRecord(Object... values) throws IOException {
        this.parse();
        this.open();
        this.writeExistingRecords();
        this.printer.printRecord(values);
        this.close();
    }

    public Iterable<CSVRecord> getRecords() {
        this.parse();
        return this.records;
    }

    public Stream<CSVRecord> getStream() {
        return StreamSupport.stream(this.getRecords().spliterator(), false);
    }

}
