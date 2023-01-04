package ch.unibe.inf.seg.gitanalyzer.util.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class used to read and write CSV files. You can read from a {@link CSVParser}, write to a {@link Appendable} or
 * directly to a {@link File}. There is also the possibility to create an empty CSV file. Adding records to the CSV file
 * is also a possibility.
 */
public class CSVFile implements Iterable<JSONObject> {
    private final JSONArray jsonRecords = new JSONArray();
    private final CSVFormat writeFormat;

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

    @Override
    public Iterator<JSONObject> iterator() {
        return new Iterator<>() {
            private final Iterator<Object> iterator = jsonRecords.iterator();
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public JSONObject next() {
                return (JSONObject) iterator.next();
            }
        };
    }
}
