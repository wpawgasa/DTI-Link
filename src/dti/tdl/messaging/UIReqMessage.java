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
public class UIReqMessage {
    public String msg_name;
    public String msg_params;
    

    public UIReqMessage() {
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

    
    
    
}
