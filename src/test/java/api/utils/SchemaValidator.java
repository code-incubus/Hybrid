package api.utils;

import api.exceptions.ApiException;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.io.InputStream;

public class SchemaValidator {

    /**
     * Validates response body against a JSON Schema file.
     * <p>
     * Schema files are located in src/test/resources/schemas/
     *
     * @param response   - REST Assured response to validate
     * @param schemaFile - Schema filename (e.g., "user-response-schema.json")
     */
    public static void validate(Response response, String schemaFile) {

        // Load schema from resources/schemas/ folder
        InputStream schema = SchemaValidator.class
                .getClassLoader()
                .getResourceAsStream("schemas/" + schemaFile);

        if (schema == null) {
            throw new ApiException(
                    schemaFile, 0,
                    "Schema file not found: schemas/" + schemaFile
            );
        }

        // RestAssured built-in schema validation
        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schema));
    }
}