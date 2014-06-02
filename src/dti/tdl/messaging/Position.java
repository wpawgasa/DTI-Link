/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.messaging;

/**
 *
 * @author wichai.p
 */
public class Position {
    
    public String posId;
    public double posLat;
    public double posLon;
    public double speed;
    public double trueCourse;
    public double magVariation;
    
    public Position() {
        
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public double getPosLat() {
        return posLat;
    }

    public void setPosLat(double posLat) {
        this.posLat = posLat;
    }

    public double getPosLon() {
        return posLon;
    }

    public void setPosLon(double posLon) {
        this.posLon = posLon;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getTrueCourse() {
        return trueCourse;
    }

    public void setTrueCourse(double trueCourse) {
        this.trueCourse = trueCourse;
    }

    public double getMagVariation() {
        return magVariation;
    }

    public void setMagVariation(double magVariation) {
        this.magVariation = magVariation;
    }
    
    
    
}
