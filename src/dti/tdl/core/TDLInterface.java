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
        conn = new TDLConnection();

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
                                
                                this.setReturnMsg(mapper.writeValueAsString(ret));
                                break;
                            case "add new profile":
                                ret.setMsg_name("response update profile");
                                ConnectionProfile new_profile = mapper.readValue(msg.msg_params, ConnectionProfile.class);
                                if(db.getProfileByName(new_profile.getProfileName()).getProfileId()!=0) {
                                    ret.setMsg_err(new_profile.getProfileName()+" already exists");
                                } else {
                                    db.insertProfile(new_profile.getProfileName());
                                    int new_profile_id = db.getMaxProfileId();
                                    db.insertSerialConfig(new_profile_id, new_profile.getComm_port(), new_profile.getBit_rates()
                                            , new_profile.getData_bits(), new_profile.getStop_bits(), new_profile.getParity(), new_profile.getFlowcontrol());
                                    
                                }
                                this.setReturnMsg(mapper.writeValueAsString(ret));
                                break;
                            case "update profile":
                                ret.setMsg_name("response update profile");
                                ConnectionProfile update_profile = mapper.readValue(msg.msg_params, ConnectionProfile.class);
                                db.updateSerialConfig(update_profile.getProfileId(), update_profile.getComm_port(), update_profile.getBit_rates()
                                            , update_profile.getData_bits(), update_profile.getStop_bits(), update_profile.getParity(), update_profile.getFlowcontrol());
                                
                                this.setReturnMsg(mapper.writeValueAsString(ret));
                                break;
                            case "connect":
                                ret.setMsg_name("response connect");
                                ConnectionProfile conn_profile = mapper.readValue(msg.msg_params, ConnectionProfile.class);
                                conn.setCommPort(conn_profile.getComm_port());
                                conn.setBitRate(conn_profile.getBit_rates());
                                conn.setDataBits(conn_profile.getData_bits());
                                conn.setParity(conn_profile.getParity());
                                conn.setStopBits(conn_profile.getStop_bits());
                                conn.setFlowControl(conn_profile.getFlowcontrol());
                                conn.setPortId();
                                
                                if(!conn.connect()) {
                                    ret.setMsg_err("Cannot connect to serial interface");
                                }
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
        } catch (Exception e) {

        }
    }

}
