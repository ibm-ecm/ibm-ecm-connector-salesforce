package com.filenet.salesforce.action;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.filenet.salesforce.config.Environment;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetAll {

	public static JsonObject get(String authToken, String recordType) throws Exception {
		JsonObject jo = null;
		String queryPath = Environment.getQueryPath(recordType);
		String url = Environment.getSalesforceBaseUrl() + queryPath;

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).uri(new URI(url))
				.header("Content-Type", "application/json").header("Accept", "application/json")
				.header("Authorization", "Bearer " + authToken).GET().build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		JsonElement jsonElement = JsonParser.parseString(response.body());
		jo = jsonElement.getAsJsonObject();

		return jo;

	}

}
