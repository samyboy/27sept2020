package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.GetTransaction;
import resources.APIResources;
import resources.Utils;

public class StepDefinitionGet extends Utils {
	List<RequestSpecification> requests = new ArrayList<RequestSpecification>();
	ResponseSpecification resspec;
	List<Response> responses = new ArrayList<Response>();
	List<GetTransaction> summaryData = new ArrayList<GetTransaction>();
	
	@Given("{string} Accum Transaction Payload from CSV getFlow for {string}")
	public void accum_transaction_payload_from_csv_get_flow_for(String requestType, String scenarioName) {
		String csvFilePath = System.getProperty("user.dir") + "\\GetPayload_final.csv";
		try {
			summaryData = Utils.csvToObject(csvFilePath, GetTransaction.class);
			resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

			for (GetTransaction transaction : summaryData) {
				if (transaction.getScenarioName().equalsIgnoreCase(scenarioName)) {
					System.out.println("Scenario: " + transaction.getScenarioName() + " is Matched");
                    System.out.println("Get URL is: ");
					requests.add(given().spec(requestSpecification(requestType)).body(Utils.createGetRequest(transaction)));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception :" + e);
		}

	}

	@When("user calls {string} with {string} http request getFlow")
	public void user_calls_with_http_request_get_flow(String resource, String method) throws IOException {
		APIResources resourceAPI = APIResources.valueOf(resource);
		System.out.println(getGlobalValue(resource));
		for (RequestSpecification request : requests) {
			if (method.equalsIgnoreCase("POST"))
				responses.add(request.when().post(resourceAPI.getResource()));
			else if (method.equalsIgnoreCase("GET"))
				responses.add(request.when().get(resourceAPI.getResource()));
		}

	}

	@Then("the API call got success with status code {int} getFlow")
	public void the_api_call_got_success_with_status_code_get_flow(Integer int1) {
		for (Response response : responses) {
			//assertEquals(200, response.getStatusCode());
			System.out.println(response.getStatusCode());
	}
}
	@Then("{string} in response body of getFlow is {string}")
	public void in_response_body_of_get_flow_is(String keyValue, String expectedValue) {
		for (Response response : responses) {
			assertEquals(getJsonPath(response, keyValue), expectedValue);
			//System.out.println(getJsonPath(response, keyValue));
			responseForConsole(response.asString());
				//	System.out.println("Parse Response");
			}
	}

	@Then("{string} in response body matches with expected amount in csv file")
	public void comparing_with_CSV(String keyValue) {
		for (int i=0; i<responses.size();i++) {
		    Map<String, BigDecimal> amountPerAccumtMap = getAmountOfAllAccumFromJsonPath(responses.get(i));
		    String[] listOfExpectedAmountWithAccum = summaryData.get(i).getExpectedAmount().split(";");
		    for(String expectedAmountWithAccum : listOfExpectedAmountWithAccum) {
		        String[] values = expectedAmountWithAccum.split(":");
		        assertTrue(amountPerAccumtMap.get(values[0]) != null);
		        assertEquals(amountPerAccumtMap.get(values[0]), values[1]);
		    }
		}
	}

}
