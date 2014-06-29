/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import dti.tdl.communication.ConnectionProfile;
import dti.tdl.communication.TDLConnection;
import dti.tdl.db.EmbeddedDB;
import dti.tdl.messaging.UIReqMessage;
import dti.tdl.messaging.UIResMessage;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wichai.p
 */
public class TDLInterface {
    
    public EmbeddedDB db;
    public TDLConnection conn;
    public TDLInterface() {
        //db = new EmbeddedDB();
        //conn = new TDLConnection();
        
        setupServer();
    }
    
    public static void main(String[] args) {
        TDLInterface inf = new TDLInterface();
                
    }
    
    public void setupServer() {
        System.out.println("....");
        try {
        TDLServer tdlserver = (new TDLServer() {
            @Override
            public void processRequest(String reqMsg) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    UIReqMessage msg = mapper.readValue(reqMsg, UIReqMessage.class);
                    
                    switch(msg.msg_name) {
                        case "request server status":
                            UIResMessage ret = new UIResMessage();
                            ret.setMsg_name("response server status");
                            UIResMessage.ServerStatus serverStatus = new UIResMessage.ServerStatus();
                            serverStatus.setStatus_name("isRunning");
                            serverStatus.setStatus_result("OK");
                            ret.setServerStatus(serverStatus);
                            
                            this.setReturnMsg(mapper.writeValueAsString(ret));
                            break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TDLInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
           //TDLServer tdlserver = new TDLServer();
           //TDlSocketIOServer tdlserver = new TDlSocketIOServer();
           tdlserver.start();
        } catch(Exception e) {
            
        }
    }
    
    
    
    
    
    
}
