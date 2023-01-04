package ch.unibe.inf.seg.gitanalyzer.util.json;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class JSONValidator {

    private static final String SCHEMA_ROOT = "schemas/";
    private static final String RESOLUTION_SCOPE = "classpath://" + SCHEMA_ROOT;
    private static final SchemaLoader.SchemaLoaderBuilder loader = SchemaLoader.builder()
            .useDefaults(true)
            .schemaClient(SchemaClient.classPathAwareClient())
            .resolutionScope(RESOLUTION_SCOPE);

    private static JSONObject getRawSchema(String schema) {
        InputStream in = JSONValidator.class.getClassLoader()
                .getResourceAsStream(SCHEMA_ROOT + schema);
        assert in != null;
        return new JSONObject(new JSONTokener(in));
    }

    private static Schema buildSchema(JSONObject rawSchema) {
        return JSONValidator.loader.schemaJson(rawSchema).build().load().build();
    }

    public static void validate(Object subject, String schema) {
        buildSchema(getRawSchema(schema)).validate(subject);
    }
}
