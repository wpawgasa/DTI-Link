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
        db = new EmbeddedDB();
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
                        UIResMessage ret = new UIResMessage();
                        switch (msg.msg_name) {
                            case "request server status":
                                
                                ret.setMsg_name("response server status");
                                UIResMessage.ServerStatus serverStatus = new UIResMessage.ServerStatus();
                                serverStatus.setStatus_name("isRunning");
                                serverStatus.setStatus_result("OK");
                                ret.setServerStatus(serverStatus);

                                this.setReturnMsg(mapper.writeValueAsString(ret));
                                break;
                            case "get profiles":
                                ret.setMsg_name("list profiles");
                                List<ConnectionProfile> profiles = db.listProfiles();
                                if(!profiles.isEmpty()) {
                                ret.setConn_profiles(db.listProfiles());
                                } else {
                                    ret.setMsg_err("Empty profile list");
                                }
                                

                                this.setReturnMsg(mapper.writeValueAsString(ret));
                                break;
                            case "get profile":
                                ret.setMsg_name("response profile");
                                ConnectionProfile req_profile = mapper.readValue(msg.msg_params, ConnectionProfile.class);
                                ConnectionProfile res_profile = db.getProfile(req_profile.getProfileId());
                                
                                ret.getConn_profiles().add(res_profile);
                                
                                String retMsg = mapper.writeValueAsString(ret);

                                this.setReturnMsg(retMsg);
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
        } catch (Exception e) {

        }
    }

}
