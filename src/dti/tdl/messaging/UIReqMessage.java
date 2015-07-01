/*
 * This class handle the request message from UI application
 * Created by Wichai Pawgasame (ODC3)
 * Date created: 20/06/2014
 * Last modified: 20/05/2015
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
