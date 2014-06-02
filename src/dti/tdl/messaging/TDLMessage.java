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
    public String fromId;
    public String toId;
    public String msgId;
    public String msgType;
    public String msg;

    public TDLMessage(String fromId, String toId, String msgId, String msgType, String msg) {
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

    public String getMsgType() {
        return msgType;
    }

    public String getMsg() {
        return msg;
    }
    
    
}
