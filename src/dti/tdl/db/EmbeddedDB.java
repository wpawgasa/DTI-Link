/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.db;

import dti.tdl.communication.ConnectionProfile;
import dti.tdl.communication.TDLConnection;
import dti.tdl.communication.UserProfile;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
            rs = dbmd.getTables(null, "APP", "PROFILES", null);
            if (!rs.next()) {
                createProfileTable();
            }
            rs = dbmd.getTables(null, "APP", "SERIALCONFIG", null);
            if (!rs.next()) {
                createSerialConfigTable();
            }
            rs = dbmd.getTables(null, "APP", "GPSCONFIG", null);
            if (!rs.next()) {
                createSerialConfigTable();
            }
            rs = dbmd.getTables(null, "APP", "RADIOCONFIG", null);
            if (!rs.next()) {
                createSerialConfigTable();
            }
            
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createErrorTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE ERRORS "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",ERROR_TYPE CHAR(1),ERROR_DESC LONG VARCHAR,LOG_TIME TIMESTAMP)");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createActionTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE ACTIONS "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",ACTION_DESC LONG VARCHAR,LOG_TIME TIMESTAMP)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createSerialConfigTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE SERIALCONFIG "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",PROFILE_ID INTEGER NOT NULL,COMMPORT VARCHAR(10), BITRATE INTEGER NOT NULL"
                    + ", DATABITS INTEGER NOT NULL, STOPBITS VARCHAR(5), PARITY VARCHAR(20), FLOWCONTROL VARCHAR(20))");
            insertSerialConfig(1, "COM1", 38400, 8, "1", "None", "None");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createGPSConfigTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE GPSCONFIG "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",PROFILE_ID INTEGER NOT NULL,GPSMODE INTEGER NOT NULL, GPSUPDATE INTEGER NOT NULL"
                    + ", GPSREPORT INTEGER NOT NULL)");
            insertSerialConfig(1, "COM1", 38400, 8, "1", "None", "None");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createProfileTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE PROFILES "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",PROFILE_NAME VARCHAR(50),TIME_CREATED TIMESTAMP)");
            
            insertProfile("Default");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertProfile(String profileName) {
        try {
            String sql = "INSERT INTO PROFILES (PROFILE_NAME,TIME_CREATED) VALUES (?,?)";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            java.util.Date date= new java.util.Date();
	 
            stm.setString(1, profileName);
            stm.setTimestamp(2, new Timestamp(date.getTime()));
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertSerialConfig(int profileId, String commport, int bitrate, int databits, String stopbits, String parity, String flowControl) {
        try {
            String sql = "INSERT INTO SERIALCONFIG (PROFILE_ID,COMMPORT,BITRATE,DATABITS,STOPBITS,PARITY,FLOWCONTROL) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            
            stm.setInt(1, profileId);
            stm.setString(2, commport);
            stm.setInt(3, bitrate);
            stm.setInt(4, databits);
            stm.setString(5, stopbits);
            stm.setString(6, parity);
            stm.setString(7, flowControl);
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateSerialConfig(int profileId, String commport, int bitrate, int databits, String stopbits, String parity, String flowControl) {
        try {
            String sql = "UPDATE SERIALCONFIG SET COMMPORT=?,BITRATE=?,DATABITS=?,STOPBITS=?,"
                    + "PARITY=?,FLOWCONTROL=? WHERE PROFILE_ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            
            stm.setInt(7, profileId);
            stm.setString(1, commport);
            stm.setInt(2, bitrate);
            stm.setInt(3, databits);
            stm.setString(4, stopbits);
            stm.setString(5, parity);
            stm.setString(6, flowControl);
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertGPSConfig(int profileId, int gpsmode, int gpsupdate, int gpsreport, boolean gpsenabled) {
        try {
            String sql = "INSERT INTO GPSCONFIG (PROFILE_ID,GPSMODE,GPSUPDATE,GPSREPORT,GPSENABLED) VALUES (?,?,?,?,?)";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            
            stm.setInt(1, profileId);
            stm.setInt(2, gpsmode);
            stm.setInt(3, gpsupdate);
            stm.setInt(4, gpsreport);
            stm.setBoolean(5, gpsenabled);
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateGPSConfig(int profileId, int gpsmode, int gpsupdate, int gpsreport, boolean gpsenabled) {
        try {
            String sql = "UPDATE GPSCONFIG SET GPSMODE=?,GPSUPDATE=?,GPSREPORT=?,GPSENABLED=? "
                    + "WHERE PROFILE_ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            
            stm.setInt(7, profileId);
            stm.setInt(1, gpsmode);
            stm.setInt(2, gpsupdate);
            stm.setInt(3, gpsreport);
            stm.setBoolean(4, gpsenabled);
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertRadioConfig(int profileId, int otabaud, int slottime, int frametime, double frequency,int power) {
        try {
            String sql = "INSERT INTO RADIOCONFIG (PROFILE_ID,OTABAUD,SLOTTIME,FRAMETIME,FREQUENCY,POWER) VALUES (?,?,?,?,?,?)";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            
            stm.setInt(1, profileId);
            stm.setInt(2, otabaud);
            stm.setInt(3, slottime);
            stm.setInt(4, frametime);
            stm.setDouble(5, frequency);
            stm.setInt(6, power);
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateRadioConfig(int profileId, int otabaud, int slottime, int frametime, double frequency,int power) {
        try {
            String sql = "UPDATE GPSCONFIG SET GPSMODE=?,GPSUPDATE=?,GPSREPORT=?,GPSENABLED=? "
                    + "WHERE PROFILE_ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            
            stm.setInt(7, profileId);
            stm.setInt(1, gpsmode);
            stm.setInt(2, gpsupdate);
            stm.setInt(3, gpsreport);
            stm.setBoolean(4, gpsenabled);
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
    
    public ConnectionProfile getProfile(int profileId) {
        ConnectionProfile profile = new ConnectionProfile();
        try {
            String sql = "SELECT SERIALCONFIG.*, PROFILES.* FROM SERIALCONFIG INNER JOIN PROFILES ON "
                    + "SERIALCONFIG.PROFILE_ID=PROFILES.ID WHERE SERIALCONFIG.PROFILE_ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            stm.setInt(1, profileId);
            ResultSet rs = stm.executeQuery();
            
            while(rs.next()) {
                
                String profileName = rs.getString("PROFILE_NAME");
                //String commport = rs.getString("COMMPORT");
                profile.setProfileName(profileName);
                profile.setProfileId(rs.getInt("PROFILE_ID"));
                profile.setComm_port(rs.getString("COMMPORT"));
                profile.setBit_rates(rs.getInt("BITRATE"));
                profile.setData_bits(rs.getInt("DATABITS"));
                profile.setParity(rs.getString("PARITY"));
                profile.setStop_bits(rs.getString("STOPBITS"));
                profile.setFlowcontrol(rs.getString("FLOWCONTROL"));
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return profile;
    }
    
    public int getMaxProfileId() {
        int maxId = 0;
        try {
            String sql = "SELECT MAX(ID) AS MAX_ID FROM PROFILES";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while(rs.next()) {
                
                maxId = rs.getInt("MAX_ID");
                
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return maxId;
    }
    
    public UserProfile getProfileByName(String profileName) {
        UserProfile profile = new UserProfile();
        try {
            String sql = "SELECT SERIALCONFIG.*, PROFILES.* FROM SERIALCONFIG INNER JOIN PROFILES ON "
                    + "SERIALCONFIG.PROFILE_ID=PROFILES.ID WHERE SERIALCONFIG.PROFILE_ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            stm.setString(1, profileName);
            ResultSet rs = stm.executeQuery();
            
            while(rs.next()) {
                
                profile.setProfileName(profileName);
                profile.setProfileId(rs.getInt("PROFILE_ID"));
                profile.setComm_port(rs.getString("COMMPORT"));
                profile.setBit_rates(rs.getInt("BITRATE"));
                profile.setData_bits(rs.getInt("DATABITS"));
                profile.setParity(rs.getString("PARITY"));
                profile.setStop_bits(rs.getString("STOPBITS"));
                profile.setFlowcontrol(rs.getString("FLOWCONTROL"));
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return profile;
    }
    
    public List<UserProfile> listProfiles() {
        List<UserProfile> profiles = new ArrayList<UserProfile>();
        try {
            String sql = "SELECT * FROM PROFILES ";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while(rs.next()) {
                int profileId = rs.getInt("PROFILE_ID");
                String profileName = rs.getString("PROFILE_NAME");
                if(profileId==1) {
                    profileName = InetAddress.getLocalHost().getHostName().substring(0, 8);
                }
                
                //String commport = rs.getString("COMMPORT");
                UserProfile profile = new UserProfile();
                profile.setProfileName(profileName);
                profile.setProfileId(profileId);
                profiles.add(profile);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return profiles;
    }
    
    
    
    
}
