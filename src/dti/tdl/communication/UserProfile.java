/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.communication;

/**
 *
 * @author wichai.p
 */
public class UserProfile {
    private int profileId;
    private String profileName;
    private ConnectionProfile connProfile;
    private GPSProfile gpsProfile;
    private RadioProfile radioProfile;

    public UserProfile() {
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public ConnectionProfile getConnProfile() {
        return connProfile;
    }

    public void setConnProfile(ConnectionProfile connProfile) {
        this.connProfile = connProfile;
    }

    public GPSProfile getGpsProfile() {
        return gpsProfile;
    }

    public void setGpsProfile(GPSProfile gpsProfile) {
        this.gpsProfile = gpsProfile;
    }

    public RadioProfile getRadioProfile() {
        return radioProfile;
    }

    public void setRadioProfile(RadioProfile radioProfile) {
        this.radioProfile = radioProfile;
    }
    
    
}
