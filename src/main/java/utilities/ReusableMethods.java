package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import stepDefinations.CommonSteps;

public class ReusableMethods {

	// Returns JsonPath variable used to traverse the response
	public static JsonPath stringToJson(String res) {
		JsonPath jsonPath = new JsonPath(res);
		return jsonPath;
	}

	// Returns complete json node from a list response for the unique jsonPath given
	// in input
	public static List<String> getChildNodeFromJsonArray(String parentNode, String childNode, String value)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(CommonSteps.res.asString()).get(parentNode);

		List<String> requiredRecord = new ArrayList<String>();

		if (jsonNode.isArray()) {
			for (JsonNode temp : jsonNode) {
				if ((temp.get(childNode) != null) && (temp.get(childNode).asText().equals(value))) {
					requiredRecord.add(temp.toString());
				}
			}
		}
		return requiredRecord;
	}

	public static String ReadPayloadFromJsonFile(String path) throws IOException {
		return new String (Files.readAllBytes(Paths.get(path)));
	}
//	Below method will print the curl of the request that can be imported in potman
	public static String genrateCurlFromRequestWriter(RequestSpecification reqSpec) {
		String reqMethod = "";
		String reqURI = "";
		String curlVal = "";
		String headerValue = "";

		QueryableRequestSpecification queryable = SpecificationQuerier.query(reqSpec);
		reqMethod = queryable.getMethod();
		reqURI = queryable.getURI();

		curlVal = "curl -X " + reqMethod + " '" + reqURI + "'" + System.lineSeparator();

		Headers reqHeaders = queryable.getHeaders();

		for (Header headerVal : reqHeaders) {

			headerValue = headerVal.toString().replace("=", ":");
			if (headerValue.contains("Authorization")) {
				headerValue = headerValue.replaceAll("Bearer\\s.*", "Bearer ****");
			}
			curlVal = curlVal.concat(" -H '").concat(headerValue).concat("'").concat(System.lineSeparator());
		}

		String reqBody = queryable.getBody();
		if (reqBody != null) {
			if (!reqBody.isEmpty()) {
				curlVal = curlVal.concat(" -d '").concat(reqBody.toString()).concat("'");
			}
		}
		return curlVal;
	}
}
