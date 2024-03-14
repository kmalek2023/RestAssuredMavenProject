package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class Authentication {
	String baseURI;
	String authEndPoint;
	String headerContentType;
	String authBodyFilePath;
	static long responeTime;
	String responseBodyStauts;
	String responseBodyMessage;


	public static String bearerToken;

	public Authentication() {
		baseURI = ConfigReader.getProperty("baseURI");
		authEndPoint = ConfigReader.getProperty("authEndPoint");
		authBodyFilePath = "src\\main\\java\\data\\authBody.json";
		headerContentType = ConfigReader.getProperty("contentType");
		 
	}

	
	public  static boolean compareResponseTime(){
		boolean withinRange = false;
		if (responeTime <= 3000) {
			System.out.println("Response time is within range.");
			withinRange = true;
		} else {
			System.out.println("Response time is out of range!");
			withinRange = false;
		}		
		return withinRange;
		
	}
	

	

	public String generateBearerToken() {
//		given: all input details -> (baseURI,Headers,Authorization,Payload/Body,QueryParameters)
//		when:  submit api requests-> HttpMethod(Endpoint/Resource)
//		then:  validate response -> (status code, Headers, responseTime, Payload/Body)

//		given().baseUri(baseURI).header("Content-Type",headerContentType).body(new File(authBodyFilePath)).log().all(). 
//		when().post(authEndPoint).
//		then().statusCode(201);

		Response response =

				given()
					.baseUri(baseURI)
					.header("Content-Type", headerContentType)
					.body(new File(authBodyFilePath)).
//					.log().all().
				when()
					.post(authEndPoint).
				then()
					.log().all()
					.extract().response();

		int statusCode = response.getStatusCode();
//		System.out.println("Status code " + statusCode);
		Assert.assertEquals(statusCode, 201, "Status code is not matching");
		
		responeTime = response.getTimeIn(TimeUnit.MILLISECONDS);
//		System.out.println("Respone Time In MilliSecs: " + responeTime);
		Assert.assertEquals(compareResponseTime(), true);
		

		String contentType = response.getHeader("Content-Type");
//		System.out.println("response Header Content Type: " + contentType);
		Assert.assertEquals(contentType, headerContentType,"Content-Type is not matching");

		
		String responseBody = response.getBody().asString();
//		System.out.println("Response body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		bearerToken = jp.getString("access_token");
//		System.out.println("Bearer Token: " + bearerToken);		
		
//		responseBodyStauts = jp.getString("status");
//		System.out.println("Staus of Response Body: " + responseBodyStauts);
//		
//		responseBodyMessage = jp.getString("message");
//		System.out.println("Message of Response Body: " + responseBodyMessage);
		
		return bearerToken;
		

	}

}
