/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.messaging;

import dti.tdl.communication.SetupProfile;
import dti.tdl.communication.UserProfile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UIResPPLIMessage {
    public String msg_name;
    public String msg_params;
    public String msg_err;
    public List<PPLI> tracks = new ArrayList<PPLI>();
    
    public UIResPPLIMessage() {
    }

    public String getMsg_name() {
        return msg_name;
    }

    public void setMsg_name(String msg_name) {
        this.msg_name = msg_name;
    }

    public String getMsg_params() {
        return msg_params;
    }

    public void setMsg_params(String msg_params) {
        this.msg_params = msg_params;
    }

    

    public String getMsg_err() {
        return msg_err;
    }

    public void setMsg_err(String msg_err) {
        this.msg_err = msg_err;
    }

    public List<PPLI> getTracks() {
        return tracks;
    }

    public void setTracks(List<PPLI> tracks) {
        this.tracks = tracks;
    }

    
    
    

    
    
    
    
    
    

   
    
  
    
}
