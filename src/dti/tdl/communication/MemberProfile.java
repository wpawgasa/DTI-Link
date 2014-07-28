/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.communication;

import dti.tdl.messaging.PPLI;
import java.util.Date;


/**
 *
 * @author Administrator
 */
public class MemberProfile {
    private String radioId;
    private String profileName;
    private PPLI currPos;
    private boolean status;
    private Date updateTime;

    public MemberProfile() {
    }

    public String getRadioId() {
        return radioId;
    }

    public void setRadioId(String radioId) {
        this.radioId = radioId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public PPLI getCurrPos() {
        return currPos;
    }

    public void setCurrPos(PPLI currPos) {
        this.currPos = currPos;
    }
    
    
    
    
}
