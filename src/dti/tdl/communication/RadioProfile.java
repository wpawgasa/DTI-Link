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
public class RadioProfile {
    private int otabaud;
    private int slottime;
    private int frametime;
    private double frequency;
    private int power;

    public RadioProfile() {
    }

    public int getOtabaud() {
        return otabaud;
    }

    public void setOtabaud(int otabaud) {
        this.otabaud = otabaud;
    }

    public int getSlottime() {
        return slottime;
    }

    public void setSlottime(int slottime) {
        this.slottime = slottime;
    }

    public int getFrametime() {
        return frametime;
    }

    public void setFrametime(int frametime) {
        this.frametime = frametime;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
    
    
}
