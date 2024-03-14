package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class UpdateAccount extends Authentication {

	String updateAccountEndpointFromConfig;
	String getAccountDetailsEndpointFromConfig;
	String  updateAccountBodyFilePath;
	SoftAssert softAssert;

	public UpdateAccount() {		
		updateAccountEndpointFromConfig = ConfigReader.getProperty("updateAccountEndpoint");
		getAccountDetailsEndpointFromConfig = ConfigReader.getProperty("getAccountDetailsEndpoint");
		updateAccountBodyFilePath = "src\\main\\java\\data\\updateAccountBody.json";
		softAssert = new SoftAssert();
		
		
	}

	/*
	 * step 1: createAccount (statusCode, message)
	 * Step 2: getAllAccounts( get firstAccountID)
	 * Step 3: getAccountDetails (account_id, firstAccountId)
	*/
	@Test (priority=1)
	public void updateAccount() {
		Response response = 
				given()
					.baseUri(baseURI)
					.header("Content-Type", ConfigReader.getProperty("contentType"))
					.header("Authorization", "Bearer " + generateBearerToken())
					.body(new File (updateAccountBodyFilePath)).
				when()
					.put(updateAccountEndpointFromConfig).
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
		Assert.assertEquals(successMessage, "Account updated successfully.", "Success Messages are NOT matching");

	}
	
	@Test (priority=2)
	public void getAccountDetails() {
		
		
		File expectedRequestBody = new File (updateAccountBodyFilePath);
		JsonPath jp2 = new JsonPath(expectedRequestBody);
		
		String updateAccountId = jp2.getString("account_id");
				
		String expectedAccountName = jp2.getString("account_name");
		System.out.println("Expected Account name: " + expectedAccountName);
		
		
		String expectedAccountNumber = jp2.getString("account_number");
		System.out.println("Expected Account Number: " + expectedAccountNumber);

		
		String expectedAccountDescription = jp2.getString("description");
		System.out.println("Expected Description : " + expectedAccountDescription);

		
		String expectedAccountBalance = jp2.getString("balance");
		System.out.println("expected Account balance: " + expectedAccountBalance);

		
		String expectedAccountContactPerson = jp2.getString("contact_person");
		System.out.println("Expected Contact Person: " + expectedAccountContactPerson);

		Response response = 
				given()
					.baseUri(baseURI)
					.header("Content-Type", headerContentType)
					.auth().preemptive().basic("demo1@codefios.com", "abc123") 
					.queryParam("account_id", updateAccountId).
//					.log().all().
				when()
					.get(getAccountDetailsEndpointFromConfig).
				then()
//					.log().all()
					.extract().response();
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
		
		JsonPath jp1 = new JsonPath(responseBody);
		String actualAccountName = jp1.getString("account_name");
		System.out.println("Actual Account name: " + actualAccountName);

		
		String actualAccountNumber = jp1.getString("account_number");
		System.out.println("Actual Account Number: " + actualAccountNumber);

		
		String actualAccountDescription = jp1.getString("description");
		System.out.println("Actual Description : " + actualAccountDescription);

		
		String actualAccountBalance = jp1.getString("balance");
		System.out.println("Actual Account balance: " + actualAccountBalance);

		
		String actualAccountContactPerson = jp1.getString("contact_person");
		System.out.println("Actual Contact Person: " + actualAccountContactPerson);

	
		softAssert.assertEquals(actualAccountName, expectedAccountName, "line 138 ---> Account names are NOT matching");
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber,"Account Numbers are NOT matching");
		Assert.assertEquals(actualAccountDescription, expectedAccountDescription, "Descriptions are NOT matching");
		Assert.assertEquals(actualAccountBalance,expectedAccountBalance, "Balances are NOT matching");
		Assert.assertEquals(actualAccountContactPerson, expectedAccountContactPerson, "Contact Persons are NOT matching");
	}
}
