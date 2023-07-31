package com.filenet.salesforce.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.filenet.salesforce.config.Environment;

public class SalesforceRecord {

	private static Connection connection;
	private static String sql;

	static {
		sql = "merge SalesforceRecord as t using (SELECT ? as Type, ? as Id, ? as Name, ? as Url) as s on t.Type=s.Type and t.id=s.id when matched then update set t.Name=s.Name, t.Url=s.Url when not matched then insert values (s.Type, s.Id, s.Name, s.Url );";
	}


	public static void getConnection() {
		try {
			connection = DriverManager.getConnection(Environment.getConnectionString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connection= null;
	}

	public static void insert(String type, String recordId, String name) throws Exception {

		String url = Environment.getSalesforceBaseUrl() + "lightning/r/" + type + "/" + recordId + "/view";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, type);
			pstmt.setString(2, recordId);
			pstmt.setString(3, name);
			pstmt.setString(4, url);
			pstmt.executeUpdate();
		}

	}

}
