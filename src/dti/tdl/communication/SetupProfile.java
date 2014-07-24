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
public class SetupProfile {
    private int profileId;
    private String radioId;
    private String missionkey;
    private RadioProfile radioprofile;
    private GPSProfile gpsprofile;

    public SetupProfile() {
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getRadioId() {
        return radioId;
    }

    public void setRadioId(String radioId) {
        this.radioId = radioId;
    }

    public String getMissionkey() {
        return missionkey;
    }

    public void setMissionkey(String missionkey) {
        this.missionkey = missionkey;
    }

    public RadioProfile getRadioprofile() {
        return radioprofile;
    }

    public void setRadioprofile(RadioProfile radioprofile) {
        this.radioprofile = radioprofile;
    }

    public GPSProfile getGpsprofile() {
        return gpsprofile;
    }

    public void setGpsprofile(GPSProfile gpsprofile) {
        this.gpsprofile = gpsprofile;
    }
    
    
}
