/*
 * This class handle the radio object
 * Created by Wichai Pawgasame (ODC3)
 * Date created: 20/06/2014
 * Last modified: 20/05/2015
 */

package dti.tdl.communication;

/**
 *
 * @author wichai.p
 */
public class RadioProfile {
    private int otabaud;
    private int slottime;
    private int frametime;
    private double frequency;
    private int power;
    private int profileId;
    private String missionKey;
    public RadioProfile() {
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    
    public int getOtabaud() {
        return otabaud;
    }

    public void setOtabaud(int otabaud) {
        this.otabaud = otabaud;
    }

    public int getSlottime() {
        return slottime;
    }

    public void setSlottime(int slottime) {
        this.slottime = slottime;
    }

    public int getFrametime() {
        return frametime;
    }

    public void setFrametime(int frametime) {
        this.frametime = frametime;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getMissionKey() {
        return missionKey;
    }

    public void setMissionKey(String missionKey) {
        this.missionKey = missionKey;
    }
    
    
}
