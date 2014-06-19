/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author wichai.p
 */
public class TDLServer extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String returnMsg;

    public TDLServer() throws IOException {
        this.serverSocket = new ServerSocket(9880);
        this.serverSocket.setSoTimeout(100);
        System.out.println("Server is running");
//        try {    
//            BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
//
//            log.write("Server is started");
//            log.flush();
//          }
//          catch (Exception e) {
//            e.printStackTrace();
//          }
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                System.out.println("Waiting...");
                this.clientSocket = this.serverSocket.accept();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        String reqMsg = "";
                        try {
                        Socket socket = TDLServer.this.clientSocket;
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                        reqMsg = reader.readLine();
                        TDLServer.this.processRequest(reqMsg);
                        
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF(returnMsg);
                            out.close();
                        } catch(IOException e) {
                            
                        }
                    }
                };
            } catch(Exception e) {
                
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
