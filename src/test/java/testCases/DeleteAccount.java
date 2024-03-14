package testCases;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class DeleteAccount extends Authentication {

	String deleteAccountEndpointFromConfig;
	String getAccountDetailsEndpointFromConfig;
	String  updateAccountBodyFilePath;
	String deleteAccountId;

	public DeleteAccount() {		
		deleteAccountEndpointFromConfig = ConfigReader.getProperty("deleteAccountEndpoint");
		getAccountDetailsEndpointFromConfig = ConfigReader.getProperty("getAccountDetailsEndpoint");
		deleteAccountId = "758";
		
	}

	/*
	 * step 1: createAccount (statusCode, message)
	 * Step 2: getAllAccounts( get firstAccountID)
	 * Step 3: getAccountDetails (account_id, firstAccountId)
	*/
	@Test (priority=1)
	public void deleteAccount() {
		Response response = 
				given()
					.baseUri(baseURI)
					.header("Content-Type", ConfigReader.getProperty("contentType"))
					.header("Authorization", "Bearer " + generateBearerToken())
					.queryParam("account_id", deleteAccountId).
				when()
					.delete(deleteAccountEndpointFromConfig).
				then()
					.extract().response();
					
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		Assert.assertEquals(statusCode, 200, "Status codes are NOT matching");	
		
		String responseBody = response.getBody().asString();
//		System.out.println("Response Body: " + responseBody);
		JsonPath jp = new JsonPath(responseBody);
		
		String successMessage = jp.get ("message");
		System.out.println("Success message: " + successMessage);
		Assert.assertEquals(successMessage, "Account deleted successfully.", "Success Messages are NOT matching");

	}
	
	@Test (priority=2)
	public void getAccountDetails() {
		
	
		Response response = 
				given()
					.baseUri(baseURI)
					.header("Content-Type", headerContentType)
					.auth().preemptive().basic("demo1@codefios.com", "abc123") 
					.queryParam("account_id", deleteAccountId).
//					.log().all().
				when()
					.get(getAccountDetailsEndpointFromConfig).
				then()
//					.log().all()
					.extract().response();
		
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		Assert.assertEquals(statusCode, 404, "Status codes are NOT matching");	
		
		

	}
}
