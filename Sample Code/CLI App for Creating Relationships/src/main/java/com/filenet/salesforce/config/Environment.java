package com.filenet.salesforce.config;

import java.io.FileInputStream;
import java.util.Properties;

import com.filenet.salesforce.query.Query;

public class Environment {

	private static String salesforceUserName;
	private static String salesforcePassword;
	private static String salesforceBaseUrl;
	private static String salesforceGrantService;
	private static String salesforceOrgId;
	private static String clientId;
	private static String clientSecret;
	private static String queryPath;
	private static String recordTypes;
	private static String sqliteDBFile;
	private static String cpeUserId;
	private static String cpeUserPassword;
	private static String cpeObjecStoreName;
	private static String cpeUri;
	
	static {
		try {

			Properties prop = new Properties();
			prop.load(new FileInputStream("./config/config.properties"));

			salesforceOrgId = prop.getProperty("salesforce.org.id");
			salesforceUserName = prop.getProperty("salesforce.username");
			salesforcePassword = prop.getProperty("salesforce.password");
			salesforceBaseUrl = prop.getProperty("salesforce.base.url");
			salesforceGrantService = prop.getProperty("salesforce.grant.service");
			clientId = prop.getProperty("salesforce.client.id");
			clientSecret = prop.getProperty("salesforce.client.secret");
			queryPath = prop.getProperty("salesforce.query.path");
			recordTypes = prop.getProperty("salesforce.record.types");
			sqliteDBFile = prop.getProperty("sqlite.db.file");
			cpeUserId = prop.getProperty("cpe.user.id");
			cpeUserPassword = prop.getProperty("cpe.user.password");
			cpeObjecStoreName = prop.getProperty("cpe.object.store.name");
			cpeUri = prop.getProperty("cpe.uri");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getQueryPath(String type) {

		return queryPath + Query.get(type);
	}

	public static String getSalesforceUserName() {
		return salesforceUserName;
	}

	public static String getSalesforcePassword() {
		return salesforcePassword;
	}

	public static String getSalesforceBaseUrl() {
		return salesforceBaseUrl;
	}

	public static String getSalesforceGrantService() {
		return salesforceGrantService;
	}

	public static String getClientId() {
		return clientId;
	}

	public static String getClientSecret() {
		return clientSecret;
	}

	public static String getRecordTypes() {
		return recordTypes;
	}

	public static void setRecordTypes(String recordTypes) {
		Environment.recordTypes = recordTypes;
	}

	public static String getSalesforceOrgId() {
		return salesforceOrgId;
	}

	public static void setSalesforceOrgId(String salesforceOrgId) {
		Environment.salesforceOrgId = salesforceOrgId;
	}

	public static String getQueryPath() {
		return queryPath;
	}

	public static String getSqliteDBFile() {
		return sqliteDBFile;
	}

	public static String getCpeUserId() {
		return cpeUserId;
	}

	public static String getCpeUserPassword() {
		return cpeUserPassword;
	}

	public static String getCpeObjecStoreName() {
		return cpeObjecStoreName;
	}

	public static String getCpeUri() {
		return cpeUri;
	}

}
