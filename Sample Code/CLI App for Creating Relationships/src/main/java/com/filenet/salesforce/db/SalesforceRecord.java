package com.filenet.salesforce.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.filenet.salesforce.config.Environment;

public class SalesforceRecord {

	private static String dbFileName;
	private static Connection connection;
	private static String sql;

	static {

		dbFileName = Environment.getSqliteDBFile();
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql = "INSERT INTO SalesforceRecord (Type, Id, Name, Url) values(?,?,?,?) ON CONFLICT(Type, Id) DO UPDATE SET Url = ?";

	}

	public static void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void insert(String type, String recordId, String name) throws Exception {

		String url = Environment.getSalesforceBaseUrl() + "lightning/r/" + type + "/" + recordId + "/view";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, type);
			pstmt.setString(2, recordId);
			pstmt.setString(3, name);
			pstmt.setString(4, url);
			pstmt.setString(5, url);
			pstmt.executeUpdate();
		}

	}

}
