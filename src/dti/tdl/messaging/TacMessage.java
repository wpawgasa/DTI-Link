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
public class TacMessage {
    public int tac_message_type;
    public String tac_message;
    public String senderId;
    public String senderName;

    public TacMessage() {
    }

    public int getTac_message_type() {
        return tac_message_type;
    }

    public void setTac_message_type(int tac_message_type) {
        this.tac_message_type = tac_message_type;
    }

    public String getTac_message() {
        return tac_message;
    }

    public void setTac_message(String tac_message) {
        this.tac_message = tac_message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    
    
}
