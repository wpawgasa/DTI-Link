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
    private int tac_message_type;
    private String tac_message;

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
    
    
    
}
