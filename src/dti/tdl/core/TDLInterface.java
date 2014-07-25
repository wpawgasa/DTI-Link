/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import dti.tdl.communication.ConnectionProfile;
import dti.tdl.communication.GPSProfile;
import dti.tdl.communication.RadioProfile;
import dti.tdl.communication.SetupProfile;
import dti.tdl.communication.TDLConnection;
import dti.tdl.communication.UserProfile;
import dti.tdl.db.EmbeddedDB;
import dti.tdl.messaging.PPLI;
import dti.tdl.messaging.TDLMessage;
import dti.tdl.messaging.TDLMessageHandler;
import dti.tdl.messaging.UIReqMessage;
import dti.tdl.messaging.UIResProfileMessage;
import dti.tdl.messaging.UIResSetupMessage;
import dti.tdl.messaging.UIResStatusMessage;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wichai.p
 */
public class TDLInterface {

    public EmbeddedDB db;
    public TDLConnection conn;
    private String timestamp;
    private InputStream inputStream;
    private OutputStream outputStream;
    public PPLI ownPos;
    public String ownRadioId;
    public String ownprofileId;
    public TransmitThread txT;
    public ReceiveThread rxT;
    
    public TDLInterface() {
        db = new EmbeddedDB();
        conn = new TDLConnection();

        setupServer();
    }

    public static void main(String[] args) {
        TDLInterface inf = new TDLInterface();

    }

    public void setupServer() {
        System.out.println("....");
        try {
            TDLServer tdlserver = (new TDLServer() {
                @Override
                public void processRequest(String reqMsg) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        UIReqMessage msg = mapper.readValue(reqMsg, UIReqMessage.class);

                        switch (msg.msg_name) {
                            case "request server status":
                                UIResStatusMessage ret = new UIResStatusMessage();
                                ret.setMsg_name("response server status");
                                UIResStatusMessage.ServerStatus serverStatus = new UIResStatusMessage.ServerStatus();
                                serverStatus.setStatus_name("isRunning");
                                serverStatus.setStatus_result("OK");
                                ret.setServerStatus(serverStatus);

                                this.setReturnMsg(mapper.writeValueAsString(ret));
                                break;
                            case "get profiles":
                                UIResProfileMessage retProf = new UIResProfileMessage();
                                retProf.setMsg_name("list profiles");
                                List<UserProfile> profiles = db.listProfiles();
                                if (!profiles.isEmpty()) {
                                    retProf.setUserprofiles(profiles);
                                } else {
                                    retProf.setMsg_err("Empty profile list");
                                }

                                retProf.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retProf));
                                break;
                            case "get profile":
                                UIResProfileMessage retProfSingle = new UIResProfileMessage();
                                retProfSingle.setMsg_name("response profile");
                                UserProfile req_profile = mapper.readValue(msg.msg_params, UserProfile.class);
                                UserProfile res_profile = db.getProfile(req_profile.getProfileId());
                                if (!db.isDBError) {
                                    retProfSingle.getUserprofiles().add(res_profile);
                                } else {
                                    retProfSingle.setMsg_err(db.DBError);
                                }
                                retProfSingle.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retProfSingle));
                                break;
                            case "add new profile":
                                UIResProfileMessage retAddProf = new UIResProfileMessage();
                                retAddProf.setMsg_name("response add profile");
                                UserProfile new_profile = mapper.readValue(msg.msg_params, UserProfile.class);
                                retAddProf.getUserprofiles().add(new_profile);
                                if (db.getProfileByName(new_profile.getProfileName()).getProfileId() != 0) {
                                    retAddProf.setMsg_err(new_profile.getProfileName() + " already exists");
                                } else {
                                    db.insertProfile(new_profile.getProfileName());
                                    int new_profile_id = db.getMaxProfileId();
                                    retAddProf.getUserprofiles().get(0).setProfileId(new_profile_id);
                                    if (!db.isDBError) {
                                        db.insertSerialConfig(new_profile_id, "COM1", 38400, 8, "1", "None", "None");
                                        db.insertGPSConfig(new_profile_id, 12, 2, 5, true);
                                        db.insertRadioConfig(new_profile_id, 5, 100, 1, 145.125, 80);

                                        if (db.isDBError) {
                                            retAddProf.setMsg_err(db.DBError);
                                        }
                                    } else {
                                        retAddProf.setMsg_err(db.DBError);
                                    }
                                }
                                retAddProf.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retAddProf));
                                break;
                            case "update profile":
                                UIResProfileMessage retUpdateProf = new UIResProfileMessage();
                                retUpdateProf.setMsg_name("response update profile");
                                UserProfile update_profile = mapper.readValue(msg.msg_params, UserProfile.class);
                                retUpdateProf.getUserprofiles().add(update_profile);
                                db.updateProfile(update_profile.getProfileId(), update_profile.getProfileName());
                                if (db.isDBError) {
                                    retUpdateProf.setMsg_err(db.DBError);
                                }
                                retUpdateProf.getUserprofiles().get(0).setProfileId(update_profile.getProfileId());
                                retUpdateProf.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retUpdateProf));
                                break;
                            case "delete profile":
                                UIResProfileMessage retDeleteProf = new UIResProfileMessage();
                                retDeleteProf.setMsg_name("response delete profile");
                                UserProfile delete_profile = mapper.readValue(msg.msg_params, UserProfile.class);
                                retDeleteProf.getUserprofiles().add(delete_profile);
                                db.deleteProfile(delete_profile.getProfileId());
                                if (db.isDBError) {
                                    retDeleteProf.setMsg_err(db.DBError);
                                }
                                retDeleteProf.getUserprofiles().get(0).setProfileId(delete_profile.getProfileId());
                                retDeleteProf.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retDeleteProf));
                                break;
                            case "update serial profile":
                                UIResProfileMessage retUpdateSerial = new UIResProfileMessage();
                                retUpdateSerial.setMsg_name("response update serial profile");
                                ConnectionProfile serial_profile = mapper.readValue(msg.msg_params, ConnectionProfile.class);
                                UserProfile res_update_serial_profile = new UserProfile();
                                res_update_serial_profile.setConnProfile(serial_profile);
                                retUpdateSerial.getUserprofiles().add(res_update_serial_profile);
                                db.updateSerialConfig(serial_profile.getProfileId(), serial_profile.getComm_port(), serial_profile.getBit_rates(), serial_profile.getData_bits(), serial_profile.getStop_bits(), serial_profile.getParity(), serial_profile.getFlowcontrol());
                                if (db.isDBError) {
                                    retUpdateSerial.setMsg_err(db.DBError);
                                }
                                retUpdateSerial.getUserprofiles().get(0).setProfileId(serial_profile.getProfileId());
                                retUpdateSerial.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retUpdateSerial));
                                break;
                            case "update gps profile":
                                UIResProfileMessage retUpdateGPS = new UIResProfileMessage();
                                retUpdateGPS.setMsg_name("response update gps profile");
                                GPSProfile gps_profile = mapper.readValue(msg.msg_params, GPSProfile.class);
                                UserProfile res_update_gps_profile = new UserProfile();
                                res_update_gps_profile.setGpsProfile(gps_profile);
                                retUpdateGPS.getUserprofiles().add(res_update_gps_profile);
                                db.updateGPSConfig(gps_profile.getProfileId(), gps_profile.getGpsmode(), gps_profile.getGpsupdate(), gps_profile.getGpsreport(), gps_profile.isGpsenabled());
                                if (db.isDBError) {
                                    retUpdateGPS.setMsg_err(db.DBError);
                                }
                                retUpdateGPS.getUserprofiles().get(0).setProfileId(gps_profile.getProfileId());
                                retUpdateGPS.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retUpdateGPS));
                                break;
                            case "update radio profile":
                                UIResProfileMessage retUpdateRadio = new UIResProfileMessage();
                                retUpdateRadio.setMsg_name("response update radio profile");
                                RadioProfile radio_profile = mapper.readValue(msg.msg_params, RadioProfile.class);
                                UserProfile res_update_radio_profile = new UserProfile();
                                res_update_radio_profile.setRadioProfile(radio_profile);
                                retUpdateRadio.getUserprofiles().add(res_update_radio_profile);
                                db.updateRadioConfig(radio_profile.getProfileId(), radio_profile.getOtabaud(), radio_profile.getSlottime(),
                                        radio_profile.getFrametime(), radio_profile.getFrequency(), radio_profile.getPower());
                                if (db.isDBError) {
                                    retUpdateRadio.setMsg_err(db.DBError);
                                }
                                retUpdateRadio.getUserprofiles().get(0).setProfileId(radio_profile.getProfileId());
                                retUpdateRadio.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retUpdateRadio));
                                break;
                            case "connect":
                                UIResProfileMessage retConn = new UIResProfileMessage();
                                retConn.setMsg_name("response connect");
                                ConnectionProfile conn_profile = mapper.readValue(msg.msg_params, ConnectionProfile.class);
                                conn.setCommPort(conn_profile.getComm_port());
                                conn.setBitRate(conn_profile.getBit_rates());
                                conn.setDataBits(conn_profile.getData_bits());
                                conn.setParity(conn_profile.getParity());
                                conn.setStopBits(conn_profile.getStop_bits());
                                conn.setFlowControl(conn_profile.getFlowcontrol());
                                conn.setPortId();

                                if (!conn.connect()) {
                                    retConn.setMsg_err(conn.errMsg);
                                } else {
                                    inputStream = conn.getSerialInputStream();
                                    outputStream = conn.getSerialOutputStream();
                                    conn.setPortListener(new TDLInterface.portListener());
                                    txT = new TDLInterface.TransmitThread();

                                    txT.start();

                                    rxT = new TDLInterface.ReceiveThread();

                                    rxT.start();
                                }
                                retConn.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retConn));
                                break;
                            case "setup radio":
                                UIResSetupMessage retSetup = new UIResSetupMessage();
                                retSetup.setMsg_name("response setup");
                                SetupProfile setup_profile = mapper.readValue(msg.msg_params, SetupProfile.class);
                                retSetup.setSetupProfile(setup_profile);
                                //check if radio in cmd mode
                                //loop until radio is free from cmd mode
                                while(TDLMessageHandler.isCmdMode) {
                                    Thread.sleep(200);
                                }
                                //Thread.sleep(10000);
                                //Put radio into cmd mode after radio is free
                                startCmdMode();
                                
                                Thread.sleep(1000);
                                //check radio status
                                
                                if(checkRadioStatus()) {
                                    retSetup.getSetupProfile().setRadioId(ownRadioId);
                                    
                                    //Select channel
                                    Thread.sleep(1000);
                                    
                                } else {
                                    retSetup.setMsg_err("Radio Offline");
                                }
                                
                                Thread.sleep(1000);
                                quitCmdMode();
                                
                                retSetup.setMsg_params(msg.msg_params);
                                this.setReturnMsg(mapper.writeValueAsString(retSetup));
                                break;
                            case "request radio status":
                                break;
                            case "request gps status":
                                break;
                            case "request own position":
                                break;
                            case "request own track":
                                break;
                            case "request members stat":
                                break;
                            case "request member stat":
                                break;
                            case "request member track":
                                break;
                            case "send broadcast text":
                                break;
                            case "send individual text":
                                break;
                            case "get broadcast text":
                                break;
                            case "get individual text":
                                break;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(TDLInterface.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TDLInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            //TDLServer tdlserver = new TDLServer();
            //TDlSocketIOServer tdlserver = new TDlSocketIOServer();
            tdlserver.start();
        } catch (Exception e) {

        }
    }

    private void startCmdMode() {                                            
        // TODO add your handling code here:
        String msg = "+++";
        System.out.println("start cmd");
        try {
            
            outputStream.write(msg.getBytes());
            outputStream.flush();
            TDLMessageHandler.isCmdMode = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }      
    private void quitCmdMode() {                                            
        // TODO add your handling code here:
        String msg = "ATCN";
        System.out.println("start cmd");
        try {
            
            outputStream.write(msg.getBytes());
            outputStream.write((byte)13);
            outputStream.flush();
            TDLMessageHandler.isCmdMode = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }      
    public boolean checkRadioStatus() {
        String msg = "ATMY";
        System.out.println("check radio");
        int maxWaitingCount = 5;
        
        try {
            TDLMessageHandler.cmdReqStack.add(msg);
            outputStream.write(msg.getBytes());
            outputStream.write((byte)13);
            outputStream.flush();
            int waitingCount = 0;
            while(TDLMessageHandler.cmdResStack.size()<=0&&waitingCount<maxWaitingCount) {
            Thread.sleep(500);
            waitingCount++;
            }
            
            if(TDLMessageHandler.cmdResStack.size()>0) {
                String cmdRes = null;
                String endRes = "";
                String cmdReq = TDLMessageHandler.cmdReqStack.removeFirst();
                while(TDLMessageHandler.cmdResStack.size()>0) {
                    //cmdRes.append(TDLMessageHandler.cmdResStack.removeFirst());
                    //endRes = TDLMessageHandler.cmdResStack.removeFirst();
                    //System.out.println(TDLMessageHandler.cmdResStack.removeFirst());
                    cmdRes = TDLMessageHandler.cmdResStack.removeFirst();
                    if(cmdRes.equals(cmdReq)) {
                        cmdRes = TDLMessageHandler.cmdResStack.removeFirst();
                        ownRadioId = cmdRes;
                    }
                    cmdRes = TDLMessageHandler.cmdResStack.removeFirst();
                    if(cmdRes.equals("OK")) {
                        return true;
                    }
                    
                    
                }
                
                //System.out.println(cmdRes.toString());
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(TDLInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean selectRadioChannel(int channel) {
        String msg = "ATHP "+channel;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setRadioFreq(double frequency) {
        String msg = "ATFX "+frequency;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setRadioPwr(int power) {
        String msg = "ATPO "+power;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setGPSMode(int mode) {
        String msg = "GPS "+mode;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setNMEAOUT(int isEnabled) {
        String msg = "NMEAOUT "+isEnabled;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setNMEAMASK(int mask) {
        String msg = "NMEAMASK "+mask;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setKEY(String key) {
        String msg = "KEYPHRASE "+key;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setOTABaud(int rate) {
        String msg = "ATR2 "+rate;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setSlottime(int time) {
        String msg = "SLOTTIME "+time;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setTDMAtime(int time) {
        String msg = "TDMATIME "+time;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setWMX(int isEnabled) {
        String msg = "WMX "+isEnabled;
        
        
        return writeSingleLnCmd(msg);
    }
    
    public boolean setGPSUpdate(int frequency) {
        String msg = "NMEARATE "+frequency;
        
        
        return writeSingleLnCmd(msg);
    }
    
    
    
    
    public boolean writeSingleLnCmd(String cmd) {
        String msg = cmd;
        System.out.println("write cmd");
        int maxWaitingCount = 5;
        
        try {
            TDLMessageHandler.cmdReqStack.add(msg);
            outputStream.write(msg.getBytes());
            outputStream.write((byte)13);
            outputStream.flush();
            int waitingCount = 0;
            while(TDLMessageHandler.cmdResStack.size()<=0&&waitingCount<maxWaitingCount) {
            Thread.sleep(500);
            waitingCount++;
            }
            
            if(TDLMessageHandler.cmdResStack.size()>0) {
                String cmdRes = null;
                String endRes = "";
                String cmdReq = TDLMessageHandler.cmdReqStack.removeFirst();
                while(TDLMessageHandler.cmdResStack.size()>0) {
                    //cmdRes.append(TDLMessageHandler.cmdResStack.removeFirst());
                    //endRes = TDLMessageHandler.cmdResStack.removeFirst();
                    //System.out.println(TDLMessageHandler.cmdResStack.removeFirst());
                    
                    cmdRes = TDLMessageHandler.cmdResStack.removeFirst();
                    if(cmdRes.equals("OK")) {
                        return true;
                    }
                    
                    
                }
                
                //System.out.println(cmdRes.toString());
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(TDLInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public class TransmitThread extends Thread {

        private volatile boolean isThreadAlive = true;

        @Override
        public void run() {
            try {
                while (isThreadAlive) {
                    if (TDLMessageHandler.txStack.size() > 0) {
                        //String txFrame = TDLMessageHandler.txStack.removeFirst();
                        byte[] txFrame = TDLMessageHandler.getBytesFromQueue();

                        try {
                            outputStream.write(txFrame);
                            outputStream.flush();

                        } catch (Exception ex) {
                            Logger.getLogger(TestTDL.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        public void kill() {
            isThreadAlive = false;
        }
    }

    public class ReceiveThread extends Thread {

        private volatile boolean isThreadAlive = true;

        @Override
        public void run() {
            try {
                while (isThreadAlive) {
                    if (TDLMessageHandler.rxStack.size() > 0) {
                        TDLMessage rxMsg = TDLMessageHandler.rxStack.removeFirst();
                        //userMsgTxtArea.append("Received message: "+rxMsg.getMsg()+"\n");
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        public void kill() {
            isThreadAlive = false;
        }
    }

    class PositionReportThread extends Thread {

        private volatile boolean isThreadAlive = true;

        @Override
        public void run() {

        }
    }

    public class portListener implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            switch (event.getEventType()) {
                case SerialPortEvent.BI:
                case SerialPortEvent.OE:
                case SerialPortEvent.FE:
                case SerialPortEvent.PE:
                case SerialPortEvent.CD:
                case SerialPortEvent.CTS:
                case SerialPortEvent.DSR:
                case SerialPortEvent.RI:
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                    break;
                case SerialPortEvent.DATA_AVAILABLE:
                    StringBuilder readBuffer = new StringBuilder();
                    int c;
                    byte[] b = {(byte) 1};
                    try {
                        while ((b[0] = (byte) inputStream.read()) != (byte) 10) {
                            if (b[0] != (byte) 13) {
                                readBuffer.append(new String(b));
                            }
                        }
                        String scannedInput = readBuffer.toString();
                        timestamp = new java.util.Date().toString();
                        System.out.println(timestamp + ": input received:" + scannedInput);
                        //displayArea.append(timestamp + ": input received:" + scannedInput + "\n");
                        System.out.println(TDLMessageHandler.isCmdMode);
                        if (TDLMessageHandler.isCmdMode) {
                            
                            if(TDLMessageHandler.cmdReqStack.size()>0) {
                                TDLMessageHandler.cmdResStack.add(scannedInput);
                            }
                        } else {
                            if (scannedInput.substring(0, 6).equalsIgnoreCase("$GPRMC")) {
                                System.out.println(scannedInput.substring(0, 6));
                                PPLI ppli = TDLMessageHandler.decodeOwnPosition(scannedInput);
                                System.out.println("own position: " + ppli.getPosLat() + ", " + ppli.getPosLon());
                                //currentPosition.setText(ppli.getPosLat()+", "+ppli.getPosLon());
                            }
                            if (scannedInput.charAt(0) == (char) 1) {
                                TDLMessageHandler.deformatMessage(scannedInput.getBytes());
                            }
                        }
                    } catch (IOException e) {
                    }

                    break;
            }
        }
    }

}
