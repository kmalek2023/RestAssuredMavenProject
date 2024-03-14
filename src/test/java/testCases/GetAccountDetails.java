package testCases;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class GetAccountDetails extends Authentication{

	String getAccountDetailsEndpointFromConfig;
	


	public GetAccountDetails() {
		getAccountDetailsEndpointFromConfig = ConfigReader.getProperty("getAccountDetailsEndpoint");
	
	}

	@Test
	public void getAccountDetails() {
//		given: all input details -> (baseURI,Headers,Authorization,Payload/Body,QueryParameters)
//		when:  submit api requests-> HttpMethod(Endpoint/Resource)
//		then:  validate response -> (status code, Headers, responseTime, Payload/Body)


		Response response = 
				given()
					.baseUri(baseURI)
					.header("Content-Type", headerContentType)
					.auth().preemptive().basic("demo1@codefios.com", "abc123") 
					.queryParam("account_id", "716")
					.log().all().
				when()
					.get(getAccountDetailsEndpointFromConfig).
				then()
					.log().all()
					.extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status code " + statusCode);
		Assert.assertEquals(statusCode, 200, "Status code is not matching");
		
		String contentType = response.getHeader("Content-Type");
		System.out.println("response Header Content Type: " + contentType);
		Assert.assertEquals(contentType, headerContentType,	"Content-Type is not matching");
		
		/*
			 {
			    "account_id": "716",
			    "account_name": "MD Techfios account 999",
			    "account_number": "9999999",
			    "description": "Test description 7",
			    "balance": "999.99",
			    "contact_person": "MD Islam"
			}
		 */
		
		String responseBody = response.getBody().asString();
		System.out.println("Response body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String accountName = jp.getString("account_name");
		System.out.println("Account name: " + accountName);
		Assert.assertEquals(accountName, "MD Techfios account 999",	"Account name is not matching");
		
		String accountNumber = jp.getString("account_number");
		System.out.println("Account Number: " + accountNumber);
		Assert.assertEquals(accountNumber, "9999999",	"Account Number is not matching");
		
		String accountDescription = jp.getString("description");
		System.out.println("Description : " + accountDescription);
		Assert.assertEquals(accountDescription, "Test description 7", "Description is not matching");
		
		String accountBalance = jp.getString("balance");
		System.out.println("Account balance: " + accountBalance);
		Assert.assertEquals(accountBalance, "999.99",	"Balance is not matching");
		
		String contactPerson = jp.getString("contact_person");
		System.out.println("Contact Person: " + contactPerson);
		Assert.assertEquals(contactPerson, "MD Islam",	"Contact Person is not matching");
		

	}

}
