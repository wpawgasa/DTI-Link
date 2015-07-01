/*
 * This class is TDL server handling message queing
 * Created by Wichai Pawgasame (ODC3)
 * Date created: 20/06/2014
 * Last modified: 20/05/2015
 */
package dti.tdl.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wichai.p
 */
public class TDLServer extends Thread {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String returnMsg;
    //private SocketIOServer server;
    private boolean isServerRunning;
    private boolean isSetting = false;

    public TDLServer() throws IOException {
        this.serverSocket = new ServerSocket(9889);
        

    }

    
    
    
    
    public void stopServer() {
        isServerRunning = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Waiting...");
                this.clientSocket = this.serverSocket.accept();
                
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        String reqMsg = "";
                        try {
                            Socket socket = TDLServer.this.clientSocket;
                            BufferedReader reader = 
                                    new BufferedReader(
                                            new InputStreamReader(
                                                    socket.getInputStream(), 
                                                    "UTF-8"));
                            reqMsg = reader.readLine();
                            System.out.println(reqMsg);
                            TDLServer.this.processRequest(reqMsg);
                            System.out.println(TDLServer.this.getReturnMsg());
                            if(!TDLServer.this.isIsSetting()) {
                            DataOutputStream out = 
                                new DataOutputStream(socket.getOutputStream());
                            out.writeUTF(TDLServer.this.getReturnMsg());
                            out.close();
                            }
                        } catch (IOException e) {

                        }
                    }
                };
                thread.setName("TDL Data Service");
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger(TDLServer.class.getName())
                        .log(Level.SEVERE, null, e);
            }
        }
    }

    public void processRequest(String request) {

    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public boolean isIsSetting() {
        return isSetting;
    }

    public void setIsSetting(boolean isSetting) {
        this.isSetting = isSetting;
    }
    
    

}
