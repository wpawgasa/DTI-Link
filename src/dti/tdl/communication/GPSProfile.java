/*
 * This class handle the GPS position object
 * Created by Wichai Pawgasame (ODC3)
 * Date created: 20/06/2014
 * Last modified: 20/05/2015
 */

package dti.tdl.communication;

/**
 *
 * @author wichai.p
 */
public class GPSProfile {
    private int profileId;
    private int gpsmode;
    private int gpsupdate;
    private int gpsreport;
    private boolean gpsenabled;

    public GPSProfile() {
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    
    public int getGpsmode() {
        return gpsmode;
    }

    public void setGpsmode(int gpsmode) {
        this.gpsmode = gpsmode;
    }

    public int getGpsupdate() {
        return gpsupdate;
    }

    public void setGpsupdate(int gpsupdate) {
        this.gpsupdate = gpsupdate;
    }

    public int getGpsreport() {
        return gpsreport;
    }

    public void setGpsreport(int gpsreport) {
        this.gpsreport = gpsreport;
    }

    public boolean isGpsenabled() {
        return gpsenabled;
    }

    public void setGpsenabled(boolean gpsenabled) {
        this.gpsenabled = gpsenabled;
    }
    
    
}
