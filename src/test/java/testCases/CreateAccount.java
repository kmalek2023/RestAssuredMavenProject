package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class CreateAccount extends Authentication {

	String createAccountEndpointFromConfig;
	String getAllAccountsEndpointFromConfig;
	String getAccountDetailsEndpointFromConfig;
	String  createAccountBodyFilePath;
	static int  statusCode;
	String firstAccountId;

	public CreateAccount() {		
		createAccountEndpointFromConfig = ConfigReader.getProperty("createAccountEndpoint");
		getAllAccountsEndpointFromConfig = ConfigReader.getProperty("getAllAccountsEndpoint");
		getAccountDetailsEndpointFromConfig = ConfigReader.getProperty("getAccountDetailsEndpoint");
		createAccountBodyFilePath = "src\\main\\java\\data\\createAccountBody.json";
		
	}
	public  static boolean compare201CreatedStatusCode(){
		boolean isStatusCode201 = false;
		if (statusCode == 201) {
			System.out.println("Status code " + statusCode );
			isStatusCode201 = true;
		} else {
			System.out.println("Status code is NOT matched: " + statusCode);
			isStatusCode201 = false;
		}		
		return isStatusCode201;
		
	}
	
	

	/*
	 * step 1: createAccount (statusCode, message)
	 * Step 2: getAllAccounts( get firstAccountID)
	 * Step 3: getAccountDetails (account_id, firstAccountId)
	*/
	@Test (priority=1)
	public void createAccount() {
		Response response = 
				given()
					.baseUri(baseURI)
					.header("Content-Type", ConfigReader.getProperty("contentType"))
					.header("Authorization", "Bearer " + generateBearerToken())
					.body(new File (createAccountBodyFilePath)).
				when()
					.post(createAccountEndpointFromConfig).
				then()
					.extract().response();
					
		statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
//		Assert.assertEquals(compare201CreatedStatusCode(), true);
		Assert.assertEquals(statusCode, 201, "Status codes are NOT matching");	
		
		String responseBody = response.getBody().asString();
//		System.out.println("Response Body: " + responseBody);
		JsonPath jp = new JsonPath(responseBody);
		
		String successMessage = jp.get ("message");
		System.out.println("Success message: " + successMessage);
		Assert.assertEquals(successMessage, "Account created successfully.", "Success Messages are NOT matching");
		
	
	}
	
	
	@Test (priority=2)
	public void getAllAccounts() {
//		given: all input details -> (baseURI,Headers,Authorization,Payload/Body,QueryParameters)
//		when:  submit api requests-> HttpMethod(Endpoint/Resource)
//		then:  validate response -> (status code, Headers, responseTime, Payload/Body)
//		Bearer {{bearer_token_api}}

		Response response = 

		given()
			.baseUri(baseURI)
			.header("Content-Type", headerContentType) // Content-Type in the header in postman
			.header("Authorization", "Bearer " + generateBearerToken()). // Authorization in the header in postman
//			.log().all().
		when()
			.get(getAllAccountsEndpointFromConfig).
		then()
//			.log().all()
			.extract().response();
	
		String responseBody = response.getBody().asString();
//		System.out.println("Response body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		firstAccountId = jp.getString("records[0].account_id");
		System.out.println("First Account ID: " + firstAccountId);

	}
	
	
	@Test (priority=3)
	public void getAccountDetails() {

		Response response = 
				given()
					.baseUri(baseURI)
					.header("Content-Type", headerContentType)
					.auth().preemptive().basic("demo1@codefios.com", "abc123") 
					.queryParam("account_id", firstAccountId).
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

		
		
		File expectedRequestBody = new File (createAccountBodyFilePath);
		JsonPath jp2 = new JsonPath(expectedRequestBody);
		
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
		
		
		Assert.assertEquals(actualAccountName, expectedAccountName, "Account names are NOT matching");
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber,"Account Numbers are NOT matching");
		Assert.assertEquals(actualAccountDescription, expectedAccountDescription, "Descriptions are NOT matching");
		Assert.assertEquals(actualAccountBalance,expectedAccountBalance, "Balances are NOT matching");
		Assert.assertEquals(actualAccountContactPerson, expectedAccountContactPerson, "Contact Persons are NOT matching");
	}
}
