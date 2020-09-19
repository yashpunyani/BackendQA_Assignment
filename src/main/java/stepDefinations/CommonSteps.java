package stepDefinations;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import utilities.BuildJsonPayload;
import utilities.ReusableMethods;

import org.apache.commons.io.output.WriterOutputStream;
import org.junit.Assert;

import static io.restassured.RestAssured.given;

public class CommonSteps {

	public static FileInputStream fis;
	public static Properties propEnv = new Properties();
	public static Properties propresource = new Properties();
	public static String resourceURL;
	public static RequestSpecification requestSpecs;
	public static Response res;
	public static StringWriter writer;
	public static PrintStream printer;
	public static String jsonPathFieldValue;

	JsonPath jsonPath;
	static String singleRecordFromJsonResponse;

	@SuppressWarnings("rawtypes")
	ResponseBody body;

	String envPropertyFilePath = System.getProperty("user.dir") + "\\src\\test\\java\\resources\\config.properties";
	String resourceURLPropertyFilePath = System.getProperty("user.dir")
			+ "\\src\\test\\java\\resources\\resourceURL.properties";
	ReusableMethods reusableMethods = new ReusableMethods();

	// Will be executed before every scenario
	@Before
	public void beforeEveryScenario(Scenario s) throws IOException {

		writer = new StringWriter();
		printer = new PrintStream(new WriterOutputStream(writer), true);

		fis = new FileInputStream(envPropertyFilePath);
		propEnv.load(fis);
		fis = new FileInputStream(resourceURLPropertyFilePath);
		propresource.load(fis);

		System.out
				.println("-------------------------------------------------------------------------------------------");
		System.out.println(s.getName());
		System.out
				.println("-------------------------------------------------------------------------------------------");
	}

	// Method to fetch BaseURI from config.properties file based on mentioned
	// testing environment(DEV/SIT/UAT)
	@Given("^Testing Environment$")
	public void getBaseURI() throws Throwable {
		RestAssured.baseURI = propEnv.getProperty("SEERA_" + propEnv.getProperty("environment"));

		requestSpecs = given().filter(new RequestLoggingFilter(printer)).filter(new ResponseLoggingFilter(printer))
				.contentType(ContentType.JSON);
	}

	// Method to set headers in the Request
	@When("I pass Headers")
	public void setHeaders(Map<String, String> headers) {

		Iterator<Entry<String, String>> it = headers.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			requestSpecs = requestSpecs.header(pair.getKey(), pair.getValue());
		}
	}

	// Method to set query parameter in the Request
	@And("^I pass queryparameter$")
	public void setQueryParameter(Map<String, String> pathParam) throws Throwable {

		Iterator<Entry<String, String>> it = pathParam.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			requestSpecs = requestSpecs.queryParam(pair.getKey(), pair.getValue());
		}
	}

	// Method to set request payload in body.
	// It will take name of the payload from feature file, and then for the request
	// body using POJO
	@And("^I pass body \"([^\"]*)\"$")
	public void setBodyInRequest(String payloadName, Map<String, String> payloadData) throws Throwable {

		switch (payloadName) {

		case "postHotelSearch":
			requestSpecs = requestSpecs.body(BuildJsonPayload.postHotelSearch(payloadData));
			break;

		default:
			System.err.println("Payload method " + payloadName + " not found");
			break;
		}
	}

	// Method to execute HTTP operation based on input from feature file
	@And("^I perform \"([^\"]*)\" operation \"([^\"]*)\"$")
	public void executeOperation_GET(String httpMethod, String resourceName) throws Throwable {
		resourceURL = propresource.getProperty(resourceName);

		if (httpMethod.equalsIgnoreCase("GET"))
			res = requestSpecs.when().get(resourceURL);
		else if (httpMethod.equalsIgnoreCase("POST"))
			res = requestSpecs.when().post(resourceURL);
		else if (httpMethod.equalsIgnoreCase("PUT"))
			res = requestSpecs.when().put(resourceURL);
		else
			res = requestSpecs.when().delete(resourceURL);
	}

	// Method to verify HTTP status code of response
	@Then("^I should get http status \"([^\"]*)\" in response$")
	public void verifyHttpStatus(String expectedHttpStatus) throws Throwable {
		res = res.then().assertThat().statusCode(Integer.parseInt(expectedHttpStatus)).extract().response();
	}

	// Method to verify if response is in JSON format
	@And("^I verify response content type is JSON$")
	public void verifyResponseContentType() throws Throwable {
		res.then().assertThat().contentType(ContentType.JSON);
	}

	// Method to verify if the values passes from feature file are present in
	// response
	@And("^I verify response body contains$")
	public void verifyResponseBodyContains(DataTable table) throws Throwable {
		String expectedValue;
		String bodyasString;

		@SuppressWarnings("rawtypes")
		ResponseBody body = res.getBody();
		bodyasString = body.asString();

		List<String> list = table.asList(String.class);
		Iterator<String> it = list.iterator();

		while (it.hasNext()) {
			expectedValue = it.next();
			if (!bodyasString.contains(expectedValue)) {
				System.err.println("Scenario Failed : " + expectedValue + " not found in response");
			}
			Assert.assertTrue(bodyasString.contains(expectedValue));

		}
	}

	// Method to fetch single node form a list of nodes in response.
	// It will take required json path and its value from feature file and fetch
	// associated node from list of response
	@And("^I fetch a single record using jsonPath \"([^\"]*)\" and value \"([^\"]*)\" from response$")
	public void fetchRecordFromJsonArray(String jsonPath, String value) throws Throwable {
		String[] splitJsonPath = jsonPath.split("\\.");
		singleRecordFromJsonResponse = ReusableMethods
				.getChildNodeFromJsonArray(splitJsonPath[0], splitJsonPath[1], value).toString();
		System.out.println("Single Record From Response : " + singleRecordFromJsonResponse);
	}

	// Method to validate value of json paths in response
	@And("^I validate below jsonpaths and values in response$")
	public void validateResponseUsingJsonPath(Map<String, String> data) throws Throwable {

		if (singleRecordFromJsonResponse != null) {
			jsonPath = ReusableMethods.stringToJson(singleRecordFromJsonResponse);
			singleRecordFromJsonResponse = null;
		} else
			jsonPath = ReusableMethods.stringToJson(res.asString());

		Iterator<Entry<String, String>> it = data.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			jsonPathFieldValue = jsonPath.getString(pair.getKey()).replaceAll("[\\[\\]]", "");
			Assert.assertEquals(pair.getValue(), jsonPathFieldValue);
		}
	}

	// Will return the number of child nodes in the response, based on parent node
	// received from feature file
	@And("^I verify number of records in body for jsonpath \"([^\"]*)\" is \"([^\"]*)\"$")
	public void verifyNumberOfRecordsInBody(String path, String expectedNumberOfRecords) throws Throwable {
		jsonPath = ReusableMethods.stringToJson(res.asString());
		String numberOfRecords = jsonPath.get(path + ".size()").toString();
		Assert.assertEquals(expectedNumberOfRecords, numberOfRecords);
	}

	// Will execute after every scenario
	@After
	public static void afterEveryScenario() {
		System.out.println(writer.toString());
	}

}
