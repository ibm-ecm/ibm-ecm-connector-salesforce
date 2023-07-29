package com.filenet.salesforce.query;

public class Query {

	public static String get(String recordType) {
		
		if(recordType.equalsIgnoreCase("contract")) {
			return "select+id+from+contract";
		}
		else if(recordType.equalsIgnoreCase("case")) {
			return "select+id+from+case";
		}
		return "select+id,name+from+" + recordType;
	}
}
