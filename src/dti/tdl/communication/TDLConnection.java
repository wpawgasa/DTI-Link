/*
 * This class handle the connection to Data Terminal
 * Created by Wichai Pawgasame (ODC3)
 * Date created: 20/06/2014
 * Last modified: 20/05/2015
 */
package dti.tdl.communication;

import gnu.io.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wichai.p
 */
public class TDLConnection  {

    private CommPortIdentifier portId;

    private InputStream inputStream;
    private OutputStream outputStream;
    private SerialPort serialPort;
    Thread readThread;
    private String TimeStamp;

    private String commPort;
    private int bitRate;
    private int dataBits;
    private String stopBits;
    private String flowControl;
    private String parity;

    public boolean isError;
    public String errMsg;

    public TDLConnection() {

    }

    public boolean connect() {
        
        try {
            errMsg = "";
            this.TimeStamp = new java.util.Date().toString();
            this.serialPort = 
                    (SerialPort) this.portId.open("TDLConnection", 2000);
            System.out.println(TimeStamp + ": " + this.portId.getName() + 
                    " opened for communicate with Radio");

        } catch (PortInUseException e) {
            errMsg = e.getMessage();
            return false;
        }
        
        
        serialPort.notifyOnDataAvailable(true);
        try {
            int databits = getSerialPortDataBits(this.dataBits);
            int stopbits = getSerialPortStopBits(this.stopBits);
            int parityVal = getSerialPortParity(this.parity);
            serialPort.setSerialPortParams(this.bitRate,
                    databits,
                    stopbits,
                    parityVal);

            serialPort.setDTR(false);
            serialPort.setRTS(false);

        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    public void disconnect() {
        this.serialPort.removeEventListener();
        this.serialPort.close();
        
    }

    public InputStream getSerialInputStream() {
        InputStream is = null;
        try {
            is = serialPort.getInputStream();
            
        } catch (IOException e) {
        }
        return is;
    }
    
    public OutputStream getSerialOutputStream() {
        OutputStream os = null;
        try {
            os = serialPort.getOutputStream();
            
        } catch (IOException e) {
            Logger.getLogger(TDLConnection.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return os;
    }
    
    public void setPortListener(SerialPortEventListener portListener) {
        try {
            serialPort.addEventListener(portListener);
        } catch (TooManyListenersException ex) {
            Logger.getLogger(TDLConnection.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    public String getCommPort() {
        return commPort;
    }

    public void setCommPort(String commPort) {
        this.commPort = commPort;

    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public String getStopBits() {
        return stopBits;
    }

    public void setStopBits(String stopBits) {
        this.stopBits = stopBits;
    }

    public String getFlowControl() {
        return flowControl;
    }

    public void setFlowControl(String flowControl) {
        this.flowControl = flowControl;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public final void setPortId() {
        try {
            this.portId = CommPortIdentifier.getPortIdentifier(this.commPort);
        } catch (NoSuchPortException ex) {
            Logger.getLogger(TDLConnection.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }


    public int getSerialPortDataBits(int bits) {
        int value = 8;
        switch (bits) {
            case 5:
                value = SerialPort.DATABITS_5;
                break;
            case 6:
                value = SerialPort.DATABITS_6;
                break;
            case 7:
                value = SerialPort.DATABITS_7;
                break;
            case 8:
                value = SerialPort.DATABITS_8;
                break;
        }
        return value;
    }

    public int getSerialPortStopBits(String bits) {
        int value = 1;
        if (bits.equalsIgnoreCase("1")) {
            value = SerialPort.STOPBITS_1;
        }
        if (bits.equalsIgnoreCase("1.5")) {
            value = SerialPort.STOPBITS_1_5;
        }
        if (bits.equalsIgnoreCase("2")) {
            value = SerialPort.STOPBITS_2;

        }
        return value;
    }

    public int getSerialPortParity(String parity) {
        int value = 0;
        if (parity.equalsIgnoreCase("even")) {
            value = SerialPort.PARITY_EVEN;
        }
        if (parity.equalsIgnoreCase("odd")) {
            value = SerialPort.PARITY_ODD;
        }
        if (parity.equalsIgnoreCase("mark")) {
            value = SerialPort.PARITY_MARK;
        }
        if (parity.equalsIgnoreCase("space")) {
            value = SerialPort.PARITY_SPACE;
        }
        return value;
    }


}
