package com.filenet.salesforce.action;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.filenet.salesforce.config.Environment;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SalesforceLogin {

	public static String login() throws Exception {
		
		String accessToken = null;
		
		String userName = Environment.getSalesforceUserName();
		String password = Environment.getSalesforcePassword();
		String url = Environment.getSalesforceBaseUrl();
		String grantService = Environment.getSalesforceGrantService();
		String clientID = Environment.getClientId();
		String clientSecret = Environment.getClientSecret();

		String loginUrl = url + grantService + "&client_id=" + clientID + "&client_secret=" + clientSecret
				+ "&username=" + userName + "&password=" + password;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = null;
		request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).uri(new URI(loginUrl))
				.POST(HttpRequest.BodyPublishers.ofString(String.join("", ""))).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		int statusCode = response.statusCode();
		
		if(statusCode == 200) {
			JsonElement jsonElement = JsonParser.parseString(response.body());
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			accessToken = jsonObject.get("access_token").getAsString();
		}
		else {
			throw new RuntimeException("Received the HTTP status code from Salesforce: " + statusCode);
		}
		
		return accessToken;

	}

}
