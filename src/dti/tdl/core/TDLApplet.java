/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.core;

import java.applet.Applet;
import java.awt.HeadlessException;
import dti.tdl.communication.TDLConnection;
import dti.tdl.db.EmbeddedDB;
/**
 *
 * @author wichai.p
 */
public class TDLApplet extends Applet {
    public TDLConnection conn; 
    public EmbeddedDB db;
    @Override
    public void init() {
        //setup embedded db connection
        db = new EmbeddedDB();
    }
    public Boolean connectPort(String commPort, int bitRate, int dataBits, 
            int stopBits, String flowControl, String parity) {
        
        conn = new TDLConnection();
        conn.setCommPort(commPort);
        conn.setBitRate(bitRate);
        conn.setDataBits(dataBits);
        conn.setStopBits(stopBits);
        conn.setFlowControl(flowControl);
        conn.setParity(parity);
        
        conn.connect();
        
        if(conn.isError) {
            db.insertError("A", "Cannot connect to "+commPort);
            //return to javascript
            return false;
        } 
        
        return true;
        
    }
    
    public void loadDefaultProfile() {
        
    }
    
    public void getProfile(int profileId) {
        
    }
    
    public void createNewProfile() {
        
    }
    
    public void updateProfile() {
        
    }
    
    public void deleteProfile() {
        
    }
    
    
}
