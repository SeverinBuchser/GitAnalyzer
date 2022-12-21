package ch.unibe.inf.seg.gitanalyzer.util.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to read and write CSV files. You can read from a {@link CSVParser}, write to a {@link Appendable} or
 * directly to a {@link File}. There is also the possibility to create an empty CSV file. Adding records to the CSV file
 * is also a possibility.
 */
public class CSVFile {
    private final JSONArray jsonRecords = new JSONArray();
    private final CSVFormat writeFormat;

    /**
     * The CSV records are stored in {@link JSONObject} format. These records can be obtained with this method.
     * @return The CSV records in form of a {@link JSONArray}.
     */
    protected JSONArray getJsonRecords() {
        return jsonRecords;
    }

    /**
     * Creates a CSV file from a {@link CSVParser}. The parser can be obtained from an existing CSV file.
     * @param parser The parser to read from.
     * @throws IOException Thrown if the parser cannot be closed.
     */
    public CSVFile(CSVParser parser) throws IOException {
        this.writeFormat = this.getWriteFormat(parser.getHeaderNames().toArray(new String[0]));

        for (CSVRecord csvRecord: parser) {
            JSONObject jsonRecord = new JSONObject(csvRecord.toMap());
            this.jsonRecords.put(jsonRecord);
        }

        parser.close();
    }

    /**
     * Creates an empty CSV file with the specified headers.
     * @param headers The headers of the CSV file.
     */
    public CSVFile(String[] headers) {
        this.writeFormat = this.getWriteFormat(headers);

    }

    private CSVFormat getWriteFormat(String[] headers) {
        return CSVFormat.DEFAULT
                .builder()
                .setHeader(headers)
                .build();
    }

    /**
     * Writes the CSV file to a {@link Appendable}.
     * @param appendable The {@link Appendable} to write to.
     * @throws IOException If the writing does not work.
     */
    public void write(Appendable appendable) throws IOException {
        CSVPrinter printer = new CSVPrinter(appendable, this.writeFormat);
        for (int i = 0 ; i < this.jsonRecords.length() ; i++) {
            JSONObject jsonRecord = this.jsonRecords.getJSONObject(i);
            List<String> values = new ArrayList<>();
            for (String header: this.writeFormat.getHeader()) {
                values.add(jsonRecord.get(header).toString());
            }
            printer.printRecord(values);
        }
        printer.close();
    }

    /**
     * Writes the CSV file directly to a {@link File}.
     * @param file The {@link File} to write to.
     * @throws IOException If the writing does not work.
     */
    public void write(File file) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        this.write(writer);
        writer.close();
    }

    /**
     * Appends a CSV record.
     * @param values The values of the CSV record.
     */
    public void appendRecord(Object... values) {
        JSONObject jsonRecord = new JSONObject();
        for (int i = 0 ; i < this.writeFormat.getHeader().length ; i++) {
            jsonRecord.put(this.writeFormat.getHeader()[i], values[i]);
        }
        this.jsonRecords.put(jsonRecord);
    }

}
