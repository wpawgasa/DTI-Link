/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import dti.tdl.communication.ConnectionProfile;
import dti.tdl.communication.GPSProfile;
import dti.tdl.communication.MemberProfile;
import dti.tdl.communication.RadioProfile;
import dti.tdl.communication.SetupProfile;
import dti.tdl.communication.TDLConnection;
import dti.tdl.communication.UserProfile;
import dti.tdl.db.EmbeddedDB;
import dti.tdl.messaging.PPLI;
import dti.tdl.messaging.TDLMessage;
import dti.tdl.messaging.TDLMessageHandler;
import dti.tdl.messaging.UIReqMessage;
import dti.tdl.messaging.UIResMembersMessage;
import dti.tdl.messaging.UIResPPLIMessage;
import dti.tdl.messaging.UIResProfileMessage;
import dti.tdl.messaging.UIResSetupMessage;
import dti.tdl.messaging.UIResStatusMessage;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.client.am.DateTime;

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
    public LinkedList<PPLI> ownTrack = new LinkedList<PPLI>();
    public LinkedList<PPLI> memberTracks = new LinkedList<PPLI>();
    public List<MemberProfile> members = new ArrayList<MemberProfile>();

    public String ownRadioId;
    public String ownprofileId;
    public TransmitThread txT;
    public ReceiveThread rxT;
    public PositionReportThread reportT;
    public SimulateRadioThread simT;
    public SimulateRadioThread simT2;
    public SimulateRadioThread simT3;
    public CheckingMemberStatusThread checkStatT;
    public String radioErr;
    public int posreportRate;
    public boolean simNoRadio = false;
    public boolean isSetting = false;

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
                                    ownprofileId = res_profile.getProfileName();
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
                                if (!simNoRadio) {
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
                                } else {

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
                                this.setIsSetting(true);
                                if (!simNoRadio) {
                                //check if radio in cmd mode
                                    //loop until radio is free from cmd mode
                                    while (TDLMessageHandler.isCmdMode) {
                                        Thread.sleep(200);
                                    }
                                //Thread.sleep(10000);
                                    //Put radio into cmd mode after radio is free
                                    startCmdMode();

                                    Thread.sleep(1000);
                                    //check radio status

                                    if (checkRadioStatus()) {
                                        retSetup.getSetupProfile().setRadioId(ownRadioId);
                                        //Enable GPS

                                        if (setup_profile.getGpsprofile().isGpsenabled()) {

                                            //set gps mode
                                            Thread.sleep(1000);
                                            if (!setGPSMode(setup_profile.getGpsprofile().getGpsmode())) {
                                                break;
                                            }
                                            //enable gps report message
                                            Thread.sleep(1000);
                                            if (!setNMEAOUT(1)) {
                                                break;
                                            }
                                            //set gps update rate
                                            Thread.sleep(1000);
                                            if (!setGPSUpdate(setup_profile.getGpsprofile().getGpsreport())) {
                                                break;
                                            }
                                            //set gps report rate
                                            posreportRate = setup_profile.getGpsprofile().getGpsreport();
                                            //start position report

                                        } else {
                                            Thread.sleep(1000);
                                            if (!setGPSMode(0)) {
                                                break;
                                            }

                                            Thread.sleep(1000);
                                            if (!setNMEAOUT(0)) {
                                                break;
                                            }

                                        }

                                        //Select channel
                                        Thread.sleep(1000);
                                        if (!selectRadioChannel(1)) {
                                            break;
                                        }

                                        //Set frequency
                                        Thread.sleep(1000);
                                        if (!setRadioFreq(setup_profile.getRadioprofile().getFrequency())) {
                                            break;
                                        }

                                        //Set power output
                                        Thread.sleep(1000);
                                        if (!setRadioPwr(setup_profile.getRadioprofile().getPower())) {
                                            break;
                                        }

                                        //Set ota baud
                                        Thread.sleep(1000);
                                        if (!setOTABaud(setup_profile.getRadioprofile().getOtabaud())) {
                                            break;
                                        }

                                        //Set slot time
                                        Thread.sleep(1000);
                                        if (!setSlottime(setup_profile.getRadioprofile().getSlottime())) {
                                            break;
                                        }

                                        //Set frame time
                                        Thread.sleep(1000);
                                        if (!setTDMAtime(setup_profile.getRadioprofile().getFrametime())) {
                                            break;
                                        }

                                        //set nmeamask
                                        Thread.sleep(1000);
                                        if (!setNMEAMASK(256)) {
                                            break;
                                        }

                                        //disable WMX
                                        Thread.sleep(1000);
                                        if (!setWMX(0)) {
                                            break;
                                        }

                                        //Set key
                                        Thread.sleep(1000);
                                        if (!setup_profile.getMissionkey().equals("")) {

                                            if (!setKEY(setup_profile.getMissionkey())) {
                                                break;
                                            }
                                        } else {
                                            if (!setKEY("0")) {
                                                break;
                                            }
                                        }

                                        //Calculate max message bytes
                                        double slottime = (double) setup_profile.getRadioprofile().getSlottime();
                                        int ota = setup_profile.getRadioprofile().getOtabaud();
                                        double bitrate = 0;
                                        switch (ota) {
                                            case 3:
                                                bitrate = 4800;
                                                break;
                                            case 5:
                                                bitrate = 9600;
                                                break;
                                            case 6:
                                                bitrate = 19200;
                                                break;
                                            default:
                                                bitrate = 9600;
                                                break;
                                        }

                                        TDLMessageHandler.messageMaxBytes = Math.floor(slottime * bitrate * 0.001 / 8) - TDLMessageHandler.messageOverheadBytes;

                                    } else {
                                        radioErr = "Radio Offline";
                                    }

                                    retSetup.setMsg_err(radioErr);

                                    Thread.sleep(1000);
                                    quitCmdMode();
                                    //Start position report thread
                                    reportT = new TDLInterface.PositionReportThread();
                                    reportT.start();
                                } else {
                                    PPLI ownPPLI = new PPLI();
                                    ownPPLI.setPosId("0001");
                                    ownPPLI.setPosName("This");
                                    ownPPLI.setPosLat(13.910016);
                                    ownPPLI.setPosLon(100.550662);
                                    ownPPLI.setSpeed(0.0);
                                    ownPPLI.setTrueCourse(0.0);
                                    ownPPLI.setMagVariation(0.0);

                                    ownTrack.add(ownPPLI);
                                    posreportRate = 5;

                                    PPLI simRadio1 = new PPLI();
                                    simRadio1.setPosId("0099");
                                    simRadio1.setPosName("MemT");
                                    simRadio1.setPosLat(13.910016);
                                    simRadio1.setPosLon(100.550662);
                                    simRadio1.setSpeed(0.0);
                                    simRadio1.setTrueCourse(0.0);
                                    simRadio1.setMagVariation(0.0);
                                    simT = new TDLInterface.SimulateRadioThread(simRadio1);
                                    simT.start();

                                    PPLI simRadio2 = new PPLI();
                                    simRadio2.setPosId("0088");
                                    simRadio2.setPosName("MemS");
                                    simRadio2.setPosLat(13.920016);
                                    simRadio2.setPosLon(100.450662);
                                    simRadio2.setSpeed(0.0);
                                    simRadio2.setTrueCourse(0.0);
                                    simRadio2.setMagVariation(0.0);
                                    simT2 = new TDLInterface.SimulateRadioThread(simRadio2);
                                    simT2.start();

                                    PPLI simRadio3 = new PPLI();
                                    simRadio3.setPosId("0077");
                                    simRadio3.setPosName("MemR");
                                    simRadio3.setPosLat(13.940016);
                                    simRadio3.setPosLon(100.480662);
                                    simRadio3.setSpeed(0.0);
                                    simRadio3.setTrueCourse(0.0);
                                    simRadio3.setMagVariation(0.0);
                                    simT3 = new TDLInterface.SimulateRadioThread(simRadio3);
                                    simT3.start();
                                }

                                checkStatT = new TDLInterface.CheckingMemberStatusThread();
                                checkStatT.start();
                                
                                retSetup.setMsg_params(msg.msg_params);
                                this.setIsSetting(false);
                                this.setReturnMsg(mapper.writeValueAsString(retSetup));
                                break;
                            case "request radio status":
                                break;
                            case "request gps status":
                                break;
                            case "request own position":
                                UIResPPLIMessage retOwnPPLI = new UIResPPLIMessage();
                                retOwnPPLI.setMsg_name("response own position");
                                if (ownTrack.size() > 0) {
                                    PPLI curr_own_pos = ownTrack.getLast();
                                    retOwnPPLI.getTracks().add(curr_own_pos);
                                }
                                this.setReturnMsg(mapper.writeValueAsString(retOwnPPLI));
                                break;
                            case "request own track":
                                break;
                            case "request members":
                                UIResMembersMessage retMembers = new UIResMembersMessage();
                                retMembers.setMsg_name("response members");
                                if (members.size() > 0) {
                                    retMembers.setMembers(members);
                                }
                                this.setReturnMsg(mapper.writeValueAsString(retMembers));

                                break;
                            case "request member record":
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
        System.out.println("exit cmd");
        try {

            outputStream.write(msg.getBytes());
            outputStream.write((byte) 13);
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
            outputStream.write((byte) 13);
            outputStream.flush();
            int waitingCount = 0;
            while (TDLMessageHandler.cmdResStack.size() <= 0 && waitingCount < maxWaitingCount) {
                Thread.sleep(500);
                waitingCount++;
            }

            if (TDLMessageHandler.cmdResStack.size() > 0) {
                String cmdRes = null;
                String endRes = "";
                //String cmdReq = TDLMessageHandler.cmdReqStack.removeFirst();
                while (TDLMessageHandler.cmdResStack.size() > 0) {
                    //cmdRes.append(TDLMessageHandler.cmdResStack.removeFirst());
                    //endRes = TDLMessageHandler.cmdResStack.removeFirst();
                    //System.out.println(TDLMessageHandler.cmdResStack.removeFirst());
                    cmdRes = TDLMessageHandler.cmdResStack.removeFirst();
                    System.out.println(cmdRes);
                    String[] resStr = cmdRes.split("\r\n");

                    ownRadioId = resStr[1];
                    if (resStr[2].equals("OK")) {
                        System.out.println("ID OK");
                        return true;
                    }
//                    if (cmdRes.equals(cmdReq)) {
//                        cmdRes = TDLMessageHandler.cmdResStack.removeFirst();
//                        System.out.println(cmdRes);
//                        ownRadioId = cmdRes;
//
//                    }
//                    cmdRes = TDLMessageHandler.cmdResStack.removeFirst();
//                    if (cmdRes.equals("OK")) {
//                        System.out.println(cmdRes);
//                        return true;
//                    }

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
        String msg = "ATHP " + channel;

        return writeSingleLnCmd(msg);
    }

    public boolean setRadioFreq(double frequency) {
        String msg = "ATFX " + frequency;

        return writeSingleLnCmd(msg);
    }

    public boolean setRadioPwr(int power) {
        String msg = "ATPO " + power;

        return writeSingleLnCmd(msg);
    }

    public boolean setGPSMode(int mode) {
        String msg = "GPS " + mode;

        return writeSingleLnCmd(msg);
    }

    public boolean setNMEAOUT(int isEnabled) {
        String msg = "NMEAOUT " + isEnabled;

        return writeSingleLnCmd(msg);
    }

    public boolean setNMEAMASK(int mask) {
        String msg = "NMEAMASK " + mask;

        return writeSingleLnCmd(msg);
    }

    public boolean setKEY(String key) {
        String msg = "KEYPHRASE " + key;

        return writeSingleLnCmd(msg);
    }

    public boolean setOTABaud(int rate) {
        String msg = "ATR2 " + rate;

        return writeSingleLnCmd(msg);
    }

    public boolean setSlottime(int time) {
        String msg = "SLOTTIME " + time;

        return writeSingleLnCmd(msg);
    }

    public boolean setTDMAtime(int time) {
        String msg = "TDMATIME " + time;

        return writeSingleLnCmd(msg);
    }

    public boolean setWMX(int isEnabled) {
        String msg = "WMX " + isEnabled;

        return writeSingleLnCmd(msg);
    }

    public boolean setGPSUpdate(int frequency) {
        String msg = "NMEARATE " + frequency;

        return writeSingleLnCmd(msg);
    }

    public boolean checkExistMember(String radioId, String profileName) {
        for (int i = 0; i < members.size(); i++) {
            MemberProfile member = members.get(i);
            if (member.getRadioId().equals(radioId) && member.getProfileName().equals(profileName)) {
                return true;
            }
        }
        return false;
    }

    public MemberProfile findMember(String radioId, String profileName) {
        for (int i = 0; i < members.size(); i++) {
            MemberProfile member = members.get(i);
            if (member.getRadioId().equals(radioId) && member.getProfileName().equals(profileName)) {
                return member;
            }
        }
        return null;
    }

    public boolean writeSingleLnCmd(String cmd) {
        String msg = cmd;
        System.out.println("write cmd");
        int maxWaitingCount = 5;

        try {
            TDLMessageHandler.cmdReqStack.add(msg);
            outputStream.write(msg.getBytes());
            outputStream.write((byte) 13);
            outputStream.flush();
            int waitingCount = 0;
            while (TDLMessageHandler.cmdResStack.size() <= 0 && waitingCount < maxWaitingCount) {
                Thread.sleep(500);
                waitingCount++;
            }

            if (TDLMessageHandler.cmdResStack.size() > 0) {
                String cmdRes = null;
                String endRes = "";
                //String cmdReq = TDLMessageHandler.cmdReqStack.removeFirst();
                while (TDLMessageHandler.cmdResStack.size() > 0) {
                    //cmdRes.append(TDLMessageHandler.cmdResStack.removeFirst());
                    //endRes = TDLMessageHandler.cmdResStack.removeFirst();
                    //System.out.println(TDLMessageHandler.cmdResStack.removeFirst());

//                    cmdRes = TDLMessageHandler.cmdResStack.removeFirst();
//                    if (cmdRes.equals("OK")) {
//                        return true;
//                    }
                    cmdRes = TDLMessageHandler.cmdResStack.removeFirst();
//                    System.out.println(cmdRes);
                    String[] resStr = cmdRes.split("\r\n");
                    for (int i = 0; i < resStr.length; i++) {
                        if (resStr[i].equals("OK")) {
                            return true;
                        }
                    }

                }

                //System.out.println(cmdRes.toString());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(TDLInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        radioErr = "Radio Cmd Error: " + msg;
        return false;
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
                            System.out.println("Transmit message: " + txFrame.toString());
                            //TDLMessageHandler.deFraming(txFrame);
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
                        System.out.println("Received message from: " + rxMsg.getProfileId());
                        byte msgType = rxMsg.getMsgType();
                        byte[] data = rxMsg.getMsg();

                        if (msgType == (byte) 49) {
                            //receive message is position report
                            System.out.println("Received position report from " + rxMsg.getProfileId());
                            PPLI rcvPPLI = TDLMessageHandler.bytestoPPLI(data);
                            rcvPPLI.setPosId(rxMsg.getFromId());
                            rcvPPLI.setPosName(rxMsg.getProfileId());
                            memberTracks.add(rcvPPLI);
                            System.out.println("Lat: " + rcvPPLI.getPosLat() + ",Lon: " + rcvPPLI.getPosLon());
                            if (!checkExistMember(rcvPPLI.getPosId(), rcvPPLI.getPosName())) {
                                MemberProfile newMember = new MemberProfile();
                                newMember.setRadioId(rcvPPLI.getPosId());
                                newMember.setProfileName(rcvPPLI.getPosName());
                                newMember.setStatus(true);
                                newMember.setCurrPos(rcvPPLI);
                                newMember.setUpdateTime(new Date());
                                members.add(newMember);
                            } else {
                                MemberProfile foundMember = findMember(rcvPPLI.getPosId(), rcvPPLI.getPosName());
                                foundMember.setCurrPos(rcvPPLI);
                                foundMember.setStatus(true);
                                foundMember.setUpdateTime(new Date());
                            }
                        } else if (msgType == (byte) 48) {
                            System.out.println("Received corrupted message from " + rxMsg.getProfileId());
                        }
                    }
                    Thread.sleep(100);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                this.kill();
            }
        }

        public void kill() {
            isThreadAlive = false;
        }
    }

    public class SimulateRadioThread extends Thread {

        private volatile boolean isThreadAlive = true;
        private PPLI simPosition;

        public SimulateRadioThread(PPLI simPosition) {
            this.simPosition = simPosition;

        }

        @Override
        public void run() {
            try {
                while (isThreadAlive) {

                    double newLat = this.simPosition.getPosLat() + Math.random() * 0.5 - 0.25;
                    double newLon = this.simPosition.getPosLon() + Math.random() * 0.5 - 0.25;
                    DateFormat df = new SimpleDateFormat("MMddyy");
                    DateFormat tf = new SimpleDateFormat("HHmmss");

                    Date today = Calendar.getInstance().getTime();

                    String reportDate = df.format(today);
                    String reportTime = tf.format(today);
                    this.simPosition.setPosLat(newLat);
                    this.simPosition.setPosLon(newLon);
                    this.simPosition.setPosDate(reportDate);
                    this.simPosition.setPosTime(reportTime);

                    byte[] ppliBytes = TDLMessageHandler.pplitobytes(this.simPosition);

                    TDLMessage msg = new TDLMessage(this.simPosition.getPosName(), this.simPosition.getPosId(), null, null, (byte) 49, ppliBytes);
                    TDLMessageHandler.SimFraming(msg);
//                    if (ownTrack.size() > 0) {
//                        PPLI txPPLI = ownTrack.getLast();
//                        byte[] ppliBytes = TDLMessageHandler.pplitobytes(txPPLI);
//                        TDLMessage msg = new TDLMessage(ownprofileId, ownRadioId, null, null, (byte) 49, ppliBytes);
//                        TDLMessageHandler.SimFraming(msg);
//                    }

                    Thread.sleep(posreportRate * 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class CheckingMemberStatusThread extends Thread {

        private volatile boolean isThreadAlive = true;

        @Override
        public void run() {
            try {
                while (isThreadAlive) {
                    if (members.size() > 0) {
                        Date now = new Date();
                        for (int i = 0; i < members.size(); i++) {
                            MemberProfile member = members.get(i);
                            if (now.getTime() - member.getUpdateTime().getTime() > 10000) {
                                member.setStatus(false);
                            }
                        }
                    }
                    Thread.sleep(60000);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        public void kill() {
            isThreadAlive = false;
        }

        public boolean isRunning() {
            return isThreadAlive;
        }
    }

    class PositionReportThread extends Thread {

        private volatile boolean isThreadAlive = true;

        @Override
        public void run() {
            try {
                while (isThreadAlive) {
                    if (ownTrack.size() > 0) {
                        PPLI txPPLI = ownTrack.getLast();
                        byte[] ppliBytes = TDLMessageHandler.pplitobytes(txPPLI);
                        TDLMessage msg = new TDLMessage(ownprofileId, ownRadioId, null, null, (byte) 49, ppliBytes);
                        TDLMessageHandler.constructFrame(msg);
                    }
                    Thread.sleep(posreportRate * 1000);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        public void kill() {
            isThreadAlive = false;
        }

        public boolean isRunning() {
            return isThreadAlive;
        }
    }
    public String tmpStr;

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
                    //if (readBuffer.length() <= 0) {
                    StringBuilder readBuffer = new StringBuilder();
                    List<Integer> inputInt = new ArrayList<Integer>();
                    //}
                    int c;
                    int b;
                    int b_idx=0;
                    
                    boolean msgEnd = false;
                    try {
                        //while ((b = (byte) inputStream.read()) != (byte) 10) {
                        //    if (b != (byte) 13) {
                        do {
                            b = (int) inputStream.read();
                            if(b!=-1) {
                            //if (b != 13) {  
                            //System.out.println(b);
                            inputInt.add(b);
                            b_idx++;
                            //readBuffer.append(b + ",");
                            } else {
                               if(b_idx>=2) {
                                   if((inputInt.get(b_idx-1)==0&&inputInt.get(b_idx-2)==10)||(inputInt.get(b_idx-1)==4&&inputInt.get(b_idx-2)==3)) {
                                       msgEnd = true;
                                   }
                               } 
                            }
                            //}

                        } while (!msgEnd);
                        //System.out.println(readBuffer.charAt(readBuffer.length() - 2));
                        //if (readBuffer.charAt(readBuffer.length() - 2) == (char) 10) {
                        //readBuffer.substring(0, readBuffer.length());
                        //String scannedInput = readBuffer.toString();
                        //readBuffer = null;
                        //String[] rxBytesStrArray = scannedInput.split(",");
                        //byte[] rxBytes = new byte[rxBytesStrArray.length];
                        byte[] rxBytes = new byte[inputInt.size()];
                        String rxMsg = "";
//                        for (int j = 0; j < rxBytesStrArray.length; j++) {
//                            int byteInt = Integer.parseInt(rxBytesStrArray[j]);
//                            //frameMsg = frameMsg+" "+byteInt;
//                            rxBytes[j] = (byte) byteInt;
//                            rxMsg = rxMsg + (char) byteInt;
//                        }
                        for (int j = 0; j < inputInt.size(); j++) {
                            int byteInt = inputInt.get(j);
                            //frameMsg = frameMsg+" "+byteInt;
                            rxBytes[j] = (byte) byteInt;
                            rxMsg = rxMsg + (char) byteInt;
                        }
                        timestamp = new java.util.Date().toString();
                        System.out.println(timestamp + ": input received:" + rxMsg);
                        //displayArea.append(timestamp + ": input received:" + scannedInput + "\n");
                        System.out.println(TDLMessageHandler.isCmdMode);
                        if (TDLMessageHandler.isCmdMode) {

                            if (TDLMessageHandler.cmdReqStack.size() > 0) {
                                TDLMessageHandler.cmdResStack.add(rxMsg);
                            }
                        } else {
                            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + rxBytes[rxBytes.length - 1]);
                            if ((int) rxBytes[rxBytes.length - 1] == 10) {
//                                if(!tmpStr.equals("")) {
//                                    rxMsg = tmpStr+rxMsg;
//                                }
                                if (rxMsg.substring(0, 6).equalsIgnoreCase("$GPRMC")) {
                                    PPLI ppli = TDLMessageHandler.decodeOwnPosition(rxMsg);
                                    System.out.println("own position: " + ppli.getPosLat() + ", " + ppli.getPosLon());
                                    ppli.setPosId(ownRadioId);
                                    ppli.setPosName(ownprofileId);

                                    ownTrack.add(ppli);
                                    //currentPosition.setText(ppli.getPosLat()+", "+ppli.getPosLon());
                                }
                                tmpStr = "";
                            } //else {
                            // tmpStr = tmpStr+rxMsg;
                            //}
                            if ((int) rxBytes[rxBytes.length - 1] == 4) {
                                if (rxMsg.charAt(0) == (char) 1) {

                                    TDLMessageHandler.deFraming(rxBytes);
                                }
                            }
                        }
                        //}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

}
