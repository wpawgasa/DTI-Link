/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author wichai.p
 */
public class EmbeddedDB {

    public static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String db_url = "jdbc:derby:tdldb;create=true";
    public Connection conn;
    public EmbeddedDB() {
        try {
            Class.forName(driver);
            this.conn = DriverManager.getConnection(db_url);
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "ERRORS", null);
            if (!rs.next()) {
                createErrorTable();
            }
            rs = dbmd.getTables(null, "APP", "ACTIONS", null);
            if (!rs.next()) {
                createActionTable();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createErrorTable() {
        try {
            this.conn.createStatement().executeQuery("CREATE TABLE ERRORS"
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",ERROR_TYPE CHAR(1),ERROR_DESC LONGVARCHAR,LOG_TIME TIMESTAMP)");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createActionTable() {
        try {
            this.conn.createStatement().executeQuery("CREATE TABLE ACTIONS"
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",ACTION_DESC LONGVARCHAR,LOG_TIME TIMESTAMP)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createProfileTable() {
        
    }
    
    public void insertError(String errCode,String errDesc) {
        try {
            String sql = "INSERT INTO ERRORS (ERROR_TYPE,ERROR_DESC,LOG_TIME) VALUES (?,?,?)";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            java.util.Date date= new java.util.Date();
	 
            stm.setString(1, errCode);
            stm.setString(2, errDesc);
            stm.setTimestamp(3, new Timestamp(date.getTime()));
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertAction(String actionDesc) {
        try {
            String sql = "INSERT INTO ACTIONS (ACTION_DESC,LOG_TIME) VALUES (?,?)";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            java.util.Date date= new java.util.Date();
	 
            stm.setString(1, actionDesc);
            stm.setTimestamp(2, new Timestamp(date.getTime()));
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertProfile() {
    }
    
    public void updateProfile() {
        
    }
    
    public void deleteProfile() {
        
    }
    
    public void clearErrorLog() {
        
    }
    
    public void clearActionLog() {
        
    }

}
