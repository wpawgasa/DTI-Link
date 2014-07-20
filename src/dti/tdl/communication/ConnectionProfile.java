/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.communication;

/**
 *
 * @author wichai.p
 */
public class ConnectionProfile {

    private int profileId;
    private String comm_port;
    private int bit_rates;
    private int data_bits;
    private String stop_bits;
    private String parity;
    private String flowcontrol;

    public ConnectionProfile() {
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    
    
    public String getComm_port() {
        return comm_port;
    }

    public void setComm_port(String comm_port) {
        this.comm_port = comm_port;
    }

    public int getBit_rates() {
        return bit_rates;
    }

    public void setBit_rates(int bit_rates) {
        this.bit_rates = bit_rates;
    }

    public int getData_bits() {
        return data_bits;
    }

    public void setData_bits(int data_bits) {
        this.data_bits = data_bits;
    }

    public String getStop_bits() {
        return stop_bits;
    }

    public void setStop_bits(String stop_bits) {
        this.stop_bits = stop_bits;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getFlowcontrol() {
        return flowcontrol;
    }

    public void setFlowcontrol(String flowcontrol) {
        this.flowcontrol = flowcontrol;
    }

}
