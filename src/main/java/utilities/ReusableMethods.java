package utilities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.path.json.JsonPath;

import stepDefinations.CommonSteps;

public class ReusableMethods {

	// Returns JsonPath variable used to traverse the response
	public static JsonPath stringToJson(String res) {
		JsonPath jsonPath = new JsonPath(res);
		return jsonPath;
	}
	
	//Returns complete json node from a list response for the unique jsonPath given in input
	public static List<String> getChildNodeFromJsonArray(String parentNode, String childNode, String value) throws JsonMappingException, JsonProcessingException{
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(CommonSteps.res.asString()).get(parentNode);
		
		List<String> requiredRecord = new ArrayList<String>();
		
		if(jsonNode.isArray()) {
			for(JsonNode temp : jsonNode) {
				if((temp.get(childNode) != null) && (temp.get(childNode).asText().equals(value))) {
					requiredRecord.add(temp.toString());
				}
			}
		}
		return requiredRecord;
		
	}
}
