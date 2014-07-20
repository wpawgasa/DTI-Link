/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.messaging;

import dti.tdl.communication.ConnectionProfile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UIResStatusMessage {
    public String msg_name;
    public String msg_params;
    public String msg_err;
    public ServerStatus serverStatus;
    
    public UIResStatusMessage() {
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

    public ServerStatus getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getMsg_err() {
        return msg_err;
    }

    public void setMsg_err(String msg_err) {
        this.msg_err = msg_err;
    }

    

    
    
    
    
    
    

   public static class ServerStatus {
       private String status_name;
       private String status_result;

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }

        public String getStatus_result() {
            return status_result;
        }

        public void setStatus_result(String status_result) {
            this.status_result = status_result;
        }
       
   }
    
  
    
}
