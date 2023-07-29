package com.filenet.salesforce.cli.tasks;

import java.util.Arrays;
import java.util.List;

import com.filenet.salesforce.action.GetAll;
import com.filenet.salesforce.action.SalesforceLogin;
import com.filenet.salesforce.config.Environment;
import com.filenet.salesforce.db.SalesforceRecord;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ImportSalesforceRecords{

    public static void execute() throws Exception {
    	
		String authToken = SalesforceLogin.login();
		List<String> recordTypes = Arrays.asList(Environment.getRecordTypes().split(",", -1));
		System.out.println("Importing Salesforce Records:");
		
		for(String s : recordTypes) {
			
			JsonObject jo = GetAll.get(authToken, s);
			System.out.println(" " + s + " " + jo.get("totalSize").getAsInt() + " records");
			JsonArray records = jo.getAsJsonArray("records");
			
			for(JsonElement je : records) {
			
			    JsonObject jeo = je.getAsJsonObject();
			    String id = jeo.get("Id").getAsString();
			    String name = null;
			    if(jeo.get("Name") != null) {
			    	name = jeo.get("Name").getAsString();
			    }
			    SalesforceRecord.insert(s, id, name);
			}
			
		}
		
		SalesforceRecord.close();	    	
    	
    }

}
