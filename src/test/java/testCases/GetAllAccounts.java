package testCases;



import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class GetAllAccounts extends Authentication{

	String getAllAccountsEndpointFromConfig;
	String firstAccountId;


	public GetAllAccounts() {
		getAllAccountsEndpointFromConfig = ConfigReader.getProperty("getAllAccountsEndpoint");
	
	}

	@Test
	public void getAllAccounts() {
//		given: all input details -> (baseURI,Headers,Authorization,Payload/Body,QueryParameters)
//		when:  submit api requests-> HttpMethod(Endpoint/Resource)
//		then:  validate response -> (status code, Headers, responseTime, Payload/Body)
//		Bearer {{bearer_token_api}}

		Response response = 

		given()
			.baseUri(baseURI)
			.header("Content-Type", headerContentType) // Content-Type in the header in postman
			.header("Authorization", "Bearer " + generateBearerToken()) // Authorization in the header in postman
			.log().all().
		when()
			.get(getAllAccountsEndpointFromConfig).
		then()
			.log().all()
			.extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status code " + statusCode);
		Assert.assertEquals(statusCode, 200, "Status code is not matching");
		
		String contentType = response.getHeader("Content-Type");
		System.out.println("response Header Content Type: " + contentType);
		Assert.assertEquals(contentType, headerContentType,	"Content-Type is not matching");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		firstAccountId = jp.getString("records[0].account_id");
		System.out.println("First Account ID: " + firstAccountId);
		
		if (firstAccountId != null) {
			System.out.println("First account ID is NOT null!");
		}else {
			System.out.println("First account ID is NULL!");
		}
				
		responeTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Respone Time In MilliSecs: " + responeTime);
		Assert.assertEquals(compareResponseTime(), true);

		

	}

}
