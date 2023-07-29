package com.filenet.salesforce.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import java.sql.Statement;

import com.filenet.salesforce.config.Environment;
import com.filenet.salesforce.model.SQLDocument;

public class FileNetDocument {

	private static String dbFileName;
	private static Connection connection;
	private static String insertSql;
	private static String selectSql;
	private static String updateSql;

	static {

		dbFileName = Environment.getSqliteDBFile();
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		insertSql = "INSERT INTO FileNetDocument (ObjectStoreName, Id, Name, Creator,isRelationshipCreated) values(?,?,?,?,0) ON CONFLICT(ObjectStoreName, Id) DO UPDATE SET Name = ?";
		selectSql = "select b.type, a.id as id, recordid, url from FileNetDocument a, SalesforceRecord b where recordid = b.id and RecordId is not null and isRelationshipCreated = 0";
		updateSql = "Update FileNetDocument set isRelationshipCreated = 1 where Id = ?";

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

	public static void insert(String osName, String id, String name, String creator) throws Exception {

		try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
			pstmt.setString(1, osName);
			pstmt.setString(2, id);
			pstmt.setString(3, name);
			pstmt.setString(4, creator);
			pstmt.setString(5, name);
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}	
	
	
	public static List<SQLDocument> select() throws Exception {
		
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(selectSql);
        List<SQLDocument> sdl = new ArrayList<SQLDocument>();
        while(rs.next())
        {
          SQLDocument sd = new SQLDocument();
          sd.setID(rs.getString("id"));
          sd.setRecordId(rs.getString("recordid"));
          sd.setType(rs.getString("type"));
          sd.setUrl(rs.getString("url"));
          sdl.add(sd);
        }
        
		return sdl;
		
	}
	
	public static void update(String id) {
		try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
