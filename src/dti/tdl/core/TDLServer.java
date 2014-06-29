/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.core;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import dti.tdl.messaging.UIReqMessage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    public TDLServer() throws IOException {
        this.serverSocket = new ServerSocket(9889);
        //this.serverSocket.setSoTimeout(100);

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
                            //System.out.println("++++");
                            Socket socket = TDLServer.this.clientSocket;
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                            reqMsg = reader.readLine();
                            System.out.println(reqMsg);
                            TDLServer.this.processRequest(reqMsg);
                            System.out.println(TDLServer.this.getReturnMsg());
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF(TDLServer.this.getReturnMsg());
                            out.close();
                        } catch (IOException e) {

                        }
                    }
                };
                thread.setName("TDL Data Service");
                thread.start();
            } catch (Exception e) {

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

}
