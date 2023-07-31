package com.filenet.salesforce.config;

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

	private static String dbUrl = System.getenv("db.url");
	private static String dbName = "database=" + System.getenv("db.name") + ";";
	private static String dbUserName = "user=" + System.getenv("db.username") + ";";
	private static String dbPassword = "password=" + System.getenv("db.password") + ";";
	private static String dbEncrypt = "encrypt=" + System.getenv("db.encrypt") + ";";
	private static String dbTSC = "trustServerCertificate=" + System.getenv("db.trustServerCertificate") + ";";
	private static String dbHIC = "hostNameInCertificate=" + System.getenv("db.hostNameInCertificate") + ";";
	private static String dbLoginTimeout = "loginTimeout=" + System.getenv("db.loginTimeout") + ";";

	static {
		try {

			salesforceOrgId = System.getenv("salesforce.org.id");
			salesforceUserName = System.getenv("salesforce.username");
			salesforcePassword = System.getenv("salesforce.password");
			salesforceBaseUrl = System.getenv("salesforce.base.url");
			salesforceGrantService = System.getenv("salesforce.grant.service");
			clientId = System.getenv("salesforce.client.id");
			clientSecret = System.getenv("salesforce.client.secret");
			queryPath = System.getenv("salesforce.query.path");
			recordTypes = System.getenv("salesforce.record.types");

			sqliteDBFile = System.getenv("sqlite.db.file");

			cpeUserId = System.getenv("cpe.user.id");
			cpeUserPassword = System.getenv("cpe.user.password");
			cpeObjecStoreName = System.getenv("cpe.object.store.name");
			cpeUri = System.getenv("cpe.uri");

			dbUrl = System.getenv("db.url");
			dbName = "database=" + System.getenv("db.name") + ";";
			dbUserName = "user=" + System.getenv("db.username") + ";";
			dbPassword = "password=" + System.getenv("db.password") + ";";
			dbEncrypt = "encrypt=" + System.getenv("db.encrypt") + ";";
			dbTSC = "trustServerCertificate=" + System.getenv("db.trustServerCertificate") + ";";
			dbHIC = "hostNameInCertificate=" + System.getenv("db.hostNameInCertificate") + ";";
			dbLoginTimeout = "loginTimeout=" + System.getenv("db.loginTimeout") + ";";

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

	public static String getDbUrl() {
		return dbUrl;
	}

	public static String getDbName() {
		return dbName;
	}

	public static String getDbUserName() {
		return dbUserName;
	}

	public static String getDbPassword() {
		return dbPassword;
	}

	public static String getDbEncrypt() {
		return dbEncrypt;
	}

	public static String getDbTSC() {
		return dbTSC;
	}

	public static String getDbHIC() {
		return dbHIC;
	}

	public static String getDbLoginTimeout() {
		return dbLoginTimeout;
	}

	public static String getConnectionString() {

		String connectionUrl = dbUrl
				+ dbName
				+ dbUserName
				+ dbPassword
				+ dbEncrypt
				+ dbTSC
				+ dbHIC
				+ dbLoginTimeout;

		return connectionUrl;
	}
}
