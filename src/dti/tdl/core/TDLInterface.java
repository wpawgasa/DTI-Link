/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.core;

import dti.tdl.communication.ConnectionProfile;
import dti.tdl.communication.TDLConnection;
import dti.tdl.db.EmbeddedDB;
import java.util.List;

/**
 *
 * @author wichai.p
 */
public class TDLInterface {
    
    public EmbeddedDB db;
    public TDLConnection conn;
    public TDLInterface() {
        db = new EmbeddedDB();
        conn = new TDLConnection();
        
        setupServer();
    }
    
    public static void main(String[] args) {
        TDLInterface inf = new TDLInterface();
                
    }
    
    public void setupServer() {
        try {
        new TDLServer() {
            @Override
            public void processRequest(String reqMsg) {
                String req = reqMsg;
                switch(req) {
                    case "test":
                       this.setReturnMsg("Test message from server");
                    break;
                }
            }
        };
        } catch(Exception e) {
            
        }
    }
    
    
    
    
    
    
}
