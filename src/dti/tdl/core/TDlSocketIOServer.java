/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dti.tdl.core;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

/**
 *
 * @author wpawgasa
 */
public class TDlSocketIOServer {

    public TDlSocketIOServer() {
        Configuration config = new Configuration();
    config.setHostname("localhost");
    config.setPort(81);

    SocketIOServer server = new SocketIOServer(config);
    }
    
}
