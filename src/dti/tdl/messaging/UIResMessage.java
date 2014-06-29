/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.messaging;

/**
 *
 * @author Administrator
 */
public class UIResMessage {
    public String msg_name;
    public String msg_params;
    public ServerStatus serverStatus;

    public UIResMessage() {
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
