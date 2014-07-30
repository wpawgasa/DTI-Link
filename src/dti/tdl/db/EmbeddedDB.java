/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.db;

import dti.tdl.communication.ConnectionProfile;
import dti.tdl.communication.GPSProfile;
import dti.tdl.communication.RadioProfile;
import dti.tdl.communication.TDLConnection;
import dti.tdl.communication.UserProfile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wichai.p
 */
public class EmbeddedDB {

    public static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String db_url = "jdbc:derby:tdldb;create=true";
    public Connection conn;
    public boolean isDBError = false;
    public String DBError = "";
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
                createGPSConfigTable();
            }
            rs = dbmd.getTables(null, "APP", "RADIOCONFIG", null);
            if (!rs.next()) {
                createRadioConfigTable();
            }
            isDBError = false;
            DBError = "";
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }

    public void createErrorTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE ERRORS "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",ERROR_TYPE CHAR(1),ERROR_DESC LONG VARCHAR,LOG_TIME TIMESTAMP)");
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public void createActionTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE ACTIONS "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",ACTION_DESC LONG VARCHAR,LOG_TIME TIMESTAMP)");
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public void createSerialConfigTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE SERIALCONFIG "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",PROFILE_ID INTEGER NOT NULL,COMMPORT VARCHAR(10), BITRATE INTEGER NOT NULL"
                    + ", DATABITS INTEGER NOT NULL, STOPBITS VARCHAR(5), PARITY VARCHAR(20), FLOWCONTROL VARCHAR(20))");
            isDBError = false;
            DBError = "";
            insertSerialConfig(1, "COM1", 38400, 8, "1", "None", "None");
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public void createGPSConfigTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE GPSCONFIG "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",PROFILE_ID INTEGER NOT NULL,GPSMODE INTEGER NOT NULL, GPSUPDATE INTEGER NOT NULL"
                    + ", GPSENABLED BOOLEAN NOT NULL"
                    + ", GPSREPORT INTEGER NOT NULL)");
            isDBError = false;
            DBError = "";
            insertGPSConfig(1, 12, 2, 5, true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public void createRadioConfigTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE RADIOCONFIG "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",PROFILE_ID INTEGER NOT NULL,OTABAUD INTEGER NOT NULL, SLOTTIME INTEGER NOT NULL"
                    + ",FRAMETIME INTEGER NOT NULL, FREQUENCY DOUBLE NOT NULL"
                    + ", POWER INTEGER NOT NULL)");
            isDBError = false;
            DBError = "";
            insertRadioConfig(1, 5, 100, 1, 145.125, 80);
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public void createProfileTable() {
        try {
            this.conn.createStatement().executeUpdate("CREATE TABLE PROFILES "
                    + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",PROFILE_NAME VARCHAR(50),TIME_CREATED TIMESTAMP)");
            isDBError = false;
            DBError = "";
            insertProfile("Default");
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
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
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public void updateProfile(int profileId, String profileName) {
        try {
            String sql = "UPDATE PROFILES SET PROFILE_NAME=? WHERE ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            java.util.Date date= new java.util.Date();
	 
            stm.setString(1, profileName);
            stm.setInt(2, profileId);
            stm.executeUpdate();
            isDBError = false;
            DBError = "";
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public void deleteProfile(int profileId) {
        try {
            String sql = "DELETE FROM PROFILES WHERE ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            java.util.Date date= new java.util.Date();
	 
            stm.setInt(1, profileId);
            stm.executeUpdate();
            
            sql = "DELETE FROM SERIALCONFIG WHERE PROFILE_ID=?";
            stm = this.conn.prepareStatement(sql);
            
            stm.setInt(1, profileId);
            stm.executeUpdate();
            
            sql = "DELETE FROM GPSCONFIG WHERE PROFILE_ID=?";
            stm = this.conn.prepareStatement(sql);
            
            stm.setInt(1, profileId);
            stm.executeUpdate();
            
            sql = "DELETE FROM RADIOCONFIG WHERE PROFILE_ID=?";
            stm = this.conn.prepareStatement(sql);
            
            stm.setInt(1, profileId);
            stm.executeUpdate();
            
            isDBError = false;
            DBError = "";
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
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
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
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
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
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
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public void updateGPSConfig(int profileId, int gpsmode, int gpsupdate, int gpsreport, boolean gpsenabled) {
        try {
            String sql = "UPDATE GPSCONFIG SET GPSMODE=?,GPSUPDATE=?,GPSREPORT=?,GPSENABLED=? "
                    + "WHERE PROFILE_ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            
            stm.setInt(5, profileId);
            stm.setInt(1, gpsmode);
            stm.setInt(2, gpsupdate);
            stm.setInt(3, gpsreport);
            stm.setBoolean(4, gpsenabled);
            stm.executeUpdate();
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
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
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public void updateRadioConfig(int profileId, int otabaud, int slottime, int frametime, double frequency,int power) {
        try {
            String sql = "UPDATE RADIOCONFIG SET OTABAUD=?,SLOTTIME=?,FRAMETIME=?,FREQUENCY=?,POWER=? "
                    + "WHERE PROFILE_ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            
            stm.setInt(6, profileId);
            stm.setInt(1, otabaud);
            stm.setInt(2, slottime);
            stm.setInt(3, frametime);
            stm.setDouble(4, frequency );
            stm.setInt(5, power);
            stm.executeUpdate();
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
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
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
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
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
    }
    
    public UserProfile getProfile(int profileId) {
        UserProfile profile = new UserProfile();
        ConnectionProfile connProfile = new ConnectionProfile();
        GPSProfile gPSProfile = new GPSProfile();
        RadioProfile radioProfile = new RadioProfile();
        try {
            String sql = "SELECT SERIALCONFIG.*,GPSCONFIG.*,RADIOCONFIG.*, PROFILES.PROFILE_NAME AS PROFILE_NAME, PROFILES.ID AS PROFID FROM PROFILES INNER JOIN SERIALCONFIG ON "
                    + "SERIALCONFIG.PROFILE_ID=PROFILES.ID INNER JOIN GPSCONFIG ON "
                    + "GPSCONFIG.PROFILE_ID=PROFILES.ID INNER JOIN RADIOCONFIG ON "
                    + "RADIOCONFIG.PROFILE_ID=PROFILES.ID "
                    + "WHERE PROFILES.ID=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            stm.setInt(1, profileId);
            ResultSet rs = stm.executeQuery();
            
            while(rs.next()) {
                
                String profileName = rs.getString("PROFILE_NAME");
                //String commport = rs.getString("COMMPORT");
                if(profileId==1) {
                    profileName = InetAddress.getLocalHost().getHostName().substring(0, 8);
                }
                profile.setProfileName(profileName);
                profile.setProfileId(rs.getInt("PROFID"));
                connProfile.setComm_port(rs.getString("COMMPORT"));
                connProfile.setBit_rates(rs.getInt("BITRATE"));
                connProfile.setData_bits(rs.getInt("DATABITS"));
                connProfile.setParity(rs.getString("PARITY"));
                connProfile.setStop_bits(rs.getString("STOPBITS"));
                connProfile.setFlowcontrol(rs.getString("FLOWCONTROL"));
                profile.setConnProfile(connProfile);
                gPSProfile.setGpsmode(rs.getInt("GPSMODE"));
                gPSProfile.setGpsenabled(rs.getBoolean("GPSENABLED"));
                gPSProfile.setGpsupdate(rs.getInt("GPSUPDATE"));
                gPSProfile.setGpsreport(rs.getInt("GPSREPORT"));
                profile.setGpsProfile(gPSProfile);
                radioProfile.setOtabaud(rs.getInt("OTABAUD"));
                radioProfile.setSlottime(rs.getInt("SLOTTIME"));
                radioProfile.setFrametime(rs.getInt("FRAMETIME"));
                radioProfile.setFrequency(rs.getDouble("FREQUENCY"));
                radioProfile.setPower(rs.getInt("POWER"));
                profile.setRadioProfile(radioProfile);
            }
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        } catch (UnknownHostException ex) {
            Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, null, ex);
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
           isDBError = false;
            DBError = ""; 
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
        }
        return maxId;
    }
    
    public UserProfile getProfileByName(String profileName) {
        UserProfile profile = new UserProfile();
        ConnectionProfile connProfile = new ConnectionProfile();
        GPSProfile gPSProfile = new GPSProfile();
        RadioProfile radioProfile = new RadioProfile();
        try {
            String sql = "SELECT SERIALCONFIG.*,GPSCONFIG.*,RADIOCONFIG.*, PROFILES.*, PROFILES.ID AS PROFID FROM PROFILES INNER JOIN SERIALCONFIG ON "
                    + "SERIALCONFIG.PROFILE_ID=PROFILES.ID INNER JOIN GPSCONFIG ON "
                    + "GPSCONFIG.PROFILE_ID=PROFILES.ID INNER JOIN RADIOCONFIG ON "
                    + "RADIOCONFIG.PROFILE_ID=PROFILES.ID WHERE PROFILES.PROFILE_NAME=?";
            PreparedStatement stm = this.conn.prepareStatement(sql);
            stm.setString(1, profileName);
            ResultSet rs = stm.executeQuery();
            
            while(rs.next()) {
                
                profile.setProfileName(profileName);
                profile.setProfileId(rs.getInt("PROFILE_ID"));
                connProfile.setComm_port(rs.getString("COMMPORT"));
                connProfile.setBit_rates(rs.getInt("BITRATE"));
                connProfile.setData_bits(rs.getInt("DATABITS"));
                connProfile.setParity(rs.getString("PARITY"));
                connProfile.setStop_bits(rs.getString("STOPBITS"));
                connProfile.setFlowcontrol(rs.getString("FLOWCONTROL"));
                profile.setConnProfile(connProfile);
                gPSProfile.setGpsmode(rs.getInt("GPSMODE"));
                gPSProfile.setGpsenabled(rs.getBoolean("GPSENABLED"));
                gPSProfile.setGpsupdate(rs.getInt("GPSUPDATE"));
                gPSProfile.setGpsreport(rs.getInt("GPSREPORT"));
                profile.setGpsProfile(gPSProfile);
                radioProfile.setOtabaud(rs.getInt("OTABAUD"));
                radioProfile.setSlottime(rs.getInt("SLOTTIME"));
                radioProfile.setFrametime(rs.getInt("FRAMETIME"));
                radioProfile.setFrequency(rs.getDouble("FREQUENCY"));
                radioProfile.setPower(rs.getInt("POWER"));
                profile.setRadioProfile(radioProfile);
            }
            isDBError = false;
            DBError = "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
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
                int profileId = rs.getInt("ID");
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
            isDBError = false;
            DBError = "";
        } catch (Exception ex) {
            ex.printStackTrace();
            isDBError = true;
            DBError = ex.getMessage();
            return null;
        }
        return profiles;
    }
    
    
    
    
}
