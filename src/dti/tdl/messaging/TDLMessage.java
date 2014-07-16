/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.messaging;

/**
 *
 * @author Administrator
 */
public class TDLMessage {
    public String profileId;
    public String fromId;
    public String toId;
    public String msgId;
    public byte msgType;
    public String msg;

    public TDLMessage(String fromId, String toId, String msgId, byte msgType, String msg) {
        this.fromId = fromId;
        this.toId = toId;
        this.msgId = msgId;
        this.msgType = msgType;
        this.msg = msg;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getMsgId() {
        return msgId;
    }

    public byte getMsgType() {
        return msgType;
    }

    public String getMsg() {
        return msg;
    }

    public String getProfileId() {
        return profileId;
    }
    
    
    
}
