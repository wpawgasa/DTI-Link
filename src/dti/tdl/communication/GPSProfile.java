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
public class GPSProfile {
    private int gpsmode;
    private int gpsupdate;
    private int gpsreport;
    private boolean gpsenabled;

    public GPSProfile() {
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
