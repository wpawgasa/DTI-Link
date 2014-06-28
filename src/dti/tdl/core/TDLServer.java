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
import dti.tdl.messaging.UIControlMessage;
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
    private SocketIOServer server;
    private boolean isServerRunning;

    public TDLServer() throws IOException {
//        this.serverSocket = new ServerSocket(9880);
//        this.serverSocket.setSoTimeout(100);
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9889);
        config.setAllowCustomRequests(true);
        

        server = new SocketIOServer(config);
        setupListeners();
        
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

    public void setupListeners() {
        //final SocketIONamespace controlnamespace = server.addNamespace("/controlMessages");
        server.addJsonObjectListener(UIControlMessage.class, new DataListener<UIControlMessage>() {
            @Override
            public void onData(SocketIOClient client, UIControlMessage data, AckRequest ackRequest) {
                // broadcast messages to all clients
                server.getBroadcastOperations().sendJsonObject(data);
            }
        });

        
    }
    
    @Override
    public void run() {
        server.start();
        isServerRunning = true;
        System.out.println("Server is running");
        while (true) {
            
            try {
                System.out.println("Waiting...");
                Thread.sleep(1000);
                if(!isServerRunning) {
                    server.stop();
                    System.out.println("Server intentionally stopped");
                }
            } catch (InterruptedException ex) {
                server.stop();
                System.out.println("Server stopped due to thread exception");
                System.out.println("Error: "+ex.getMessage());;
            }
                
        }
    }
    
    public void stopServer() {
        isServerRunning = false;
    }

//    @Override
//    public void run() {
//        while (true) {
//            try {
//                System.out.println("Waiting...");
//                this.clientSocket = this.serverSocket.accept();
//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
//                        String reqMsg = "";
//                        try {
//                            Socket socket = TDLServer.this.clientSocket;
//                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//                            reqMsg = reader.readLine();
//                            TDLServer.this.processRequest(reqMsg);
//
//                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                            out.writeUTF(returnMsg);
//                            out.close();
//                        } catch (IOException e) {
//
//                        }
//                    }
//                };
//            } catch (Exception e) {
//
//            }
//        }
//    }

    public void processRequest(String request) {

    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

}
