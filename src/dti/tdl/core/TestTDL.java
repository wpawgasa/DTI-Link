/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.core;

import dti.tdl.communication.TDLConnection;
import dti.tdl.db.EmbeddedDB;
import dti.tdl.messaging.PPLI;
import dti.tdl.messaging.TDLMessage;
import dti.tdl.messaging.TDLMessageHandler;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wichai.p
 */
public class TestTDL extends javax.swing.JFrame {

    /**
     * Creates new form TestTDL
     */
    public TestTDL() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        connectBtn = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        disconnectBtn = new javax.swing.JButton();
        msgText = new javax.swing.JTextField();
        sendMsgBtn = new javax.swing.JButton();
        startCmdBtn = new javax.swing.JButton();
        stopCmdBtn = new javax.swing.JButton();
        commandText = new javax.swing.JTextField();
        sendCmdBtn = new javax.swing.JButton();
        currentPosition = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        userMsgTxtArea = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        connectBtn.setText("Connect");
        connectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectBtnActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COM1", "COM2", "COM3", "COM4" }));

        jLabel1.setText("Comm Port :");

        jLabel2.setText("Bit rate :");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "4800", "9600", "19200", "38400", "57600", "115200" }));

        jLabel3.setText("Data Bits :");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "6", "7", "8" }));
        jComboBox3.setSelectedIndex(3);

        jLabel4.setText("Parity :");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Even", "Odd", "Mark", "Space" }));

        jLabel5.setText("Flow Control :");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Xon/Xoff", "Hardware" }));

        jLabel6.setText("Stop Bits :");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "1.5", "2" }));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        disconnectBtn.setText("Disconnect");
        disconnectBtn.setEnabled(false);
        disconnectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectBtnActionPerformed(evt);
            }
        });

        sendMsgBtn.setText("Send Message");
        sendMsgBtn.setEnabled(false);
        sendMsgBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMsgBtnActionPerformed(evt);
            }
        });

        startCmdBtn.setText("Start Command Mode");
        startCmdBtn.setEnabled(false);
        startCmdBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startCmdBtnActionPerformed(evt);
            }
        });

        stopCmdBtn.setText("Stop Command Mode");
        stopCmdBtn.setEnabled(false);
        stopCmdBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopCmdBtnActionPerformed(evt);
            }
        });

        sendCmdBtn.setText("Send Command");
        sendCmdBtn.setEnabled(false);
        sendCmdBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendCmdBtnActionPerformed(evt);
            }
        });

        jLabel7.setText("Current Position :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(connectBtn)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(disconnectBtn)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(46, 46, 46)
                                    .addComponent(jLabel5))
                                .addComponent(jLabel2)))))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(msgText, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendMsgBtn))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(startCmdBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopCmdBtn))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(commandText, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sendCmdBtn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currentPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(sendMsgBtn)
                    .addComponent(msgText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(startCmdBtn)
                            .addComponent(stopCmdBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(commandText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendCmdBtn)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(connectBtn)
                            .addComponent(disconnectBtn))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        displayArea.setColumns(20);
        displayArea.setRows(5);
        jScrollPane1.setViewportView(displayArea);

        jTabbedPane1.addTab("Messages", jScrollPane1);

        userMsgTxtArea.setColumns(20);
        userMsgTxtArea.setRows(5);
        jScrollPane2.setViewportView(userMsgTxtArea);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("User Input Messages", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Map", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void connectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectBtnActionPerformed
        // TODO add your handling code here:
        conn.setCommPort(jComboBox1.getSelectedItem().toString());
        conn.setBitRate(Integer.parseInt(jComboBox2.getSelectedItem().toString()));
        conn.setDataBits(Integer.parseInt(jComboBox3.getSelectedItem().toString()));
        conn.setParity(jComboBox4.getSelectedItem().toString());
        conn.setStopBits(jComboBox5.getSelectedItem().toString());
        conn.setFlowControl(jComboBox6.getSelectedItem().toString());
        conn.setPortId();

        if (conn.connect()) {
            connectBtn.setEnabled(false);
            disconnectBtn.setEnabled(true);
            sendMsgBtn.setEnabled(true);
            startCmdBtn.setEnabled(true);
            
        }
        inputStream = conn.getSerialInputStream();
        outputStream = conn.getSerialOutputStream();
        conn.setPortListener(new portListener());
        txT = new TransmitThread();
        
        txT.start();
        
        rxT = new ReceiveThread();
        
        rxT.start();
    }//GEN-LAST:event_connectBtnActionPerformed

    private void disconnectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectBtnActionPerformed
        // TODO add your handling code here:
        conn.disconnect();
        connectBtn.setEnabled(true);
        disconnectBtn.setEnabled(false);
        sendMsgBtn.setEnabled(false);
        startCmdBtn.setEnabled(false);
        stopCmdBtn.setEnabled(false);
        sendCmdBtn.setEnabled(false);
        txT.kill();
        rxT.kill();
        
    }//GEN-LAST:event_disconnectBtnActionPerformed

    private void sendMsgBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMsgBtnActionPerformed
        // TODO add your handling code here:
        
        TDLMessage msg = new TDLMessage(null, null, null, "text", msgText.getText());
        TDLMessageHandler.formatTextMessage(msg);
//        try {
//            outputStream.write(msg.getBytes());
//            outputStream.flush();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }//GEN-LAST:event_sendMsgBtnActionPerformed

    private void startCmdBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startCmdBtnActionPerformed
        // TODO add your handling code here:
        String msg = "+++";
        try {
            outputStream.write(msg.getBytes());
            outputStream.flush();
            //sendMsgBtn.setEnabled(false);
            startCmdBtn.setEnabled(false);
            stopCmdBtn.setEnabled(true);
            sendCmdBtn.setEnabled(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_startCmdBtnActionPerformed

    private void stopCmdBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopCmdBtnActionPerformed
        // TODO add your handling code here:
        String msg = "ATCN";
        try {
            
            outputStream.write(msg.getBytes());
            outputStream.write((byte)13);
            outputStream.flush();
            //sendMsgBtn.setEnabled(false);
            startCmdBtn.setEnabled(true);
            stopCmdBtn.setEnabled(false);
            sendCmdBtn.setEnabled(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_stopCmdBtnActionPerformed

    private void sendCmdBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendCmdBtnActionPerformed
        // TODO add your handling code here:
        String msg = commandText.getText();
        try {
            outputStream.write(msg.getBytes());
            outputStream.write((byte)13);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_sendCmdBtnActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
////            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
////                if ("Nimbus".equals(info.getName())) {
////                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
////                    break;
////                }
////            }
//            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(TestTDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(TestTDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(TestTDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(TestTDL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        //EmbeddedDB db = new EmbeddedDB();
//        final TestTDL test = new TestTDL();
//
//        test.conn = new TDLConnection();
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                test.setVisible(true);
//            }
//        });
//    }
    private TDLConnection conn;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField commandText;
    private javax.swing.JButton connectBtn;
    private javax.swing.JTextField currentPosition;
    private javax.swing.JButton disconnectBtn;
    private static javax.swing.JTextArea displayArea;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField msgText;
    private javax.swing.JButton sendCmdBtn;
    private javax.swing.JButton sendMsgBtn;
    private javax.swing.JButton startCmdBtn;
    private javax.swing.JButton stopCmdBtn;
    private javax.swing.JTextArea userMsgTxtArea;
    // End of variables declaration//GEN-END:variables
    private String timestamp;
    private InputStream inputStream;
    private OutputStream outputStream;
    //private SerialPortEventListener portListener;
    Thread t;
    TransmitThread txT;
    ReceiveThread rxT;
    

    public class connThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(100);
                //displayArea.append(timestamp + "\n");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public class TransmitThread extends Thread {
        private volatile boolean isThreadAlive = true;
        @Override
        public void run() {
            try {
                while(isThreadAlive) {
                    if(TDLMessageHandler.txStack.size()>0) {
                        //String txFrame = TDLMessageHandler.txStack.removeFirst();
                        byte[] txFrame = TDLMessageHandler.getBytesFromQueue();
//                        try {
                            //String txMsg = new String(txFrame.getBytes("UTF-8"), "UTF-8");
                            //System.out.println(txMsg);
                            TDLMessageHandler.deformatMessage(txFrame);
    //                        try { 
                           // outputStream.write(txFrame.getBytes());
                           // outputStream.flush();
                                //outputStream.flush();
//                        } catch (Exception ex) {
//                            Logger.getLogger(TestTDL.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                            
                            
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
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
                while(isThreadAlive) {
                    if(TDLMessageHandler.rxStack.size()>0) {
                        TDLMessage rxMsg = TDLMessageHandler.rxStack.removeFirst();
                        userMsgTxtArea.append("Received message: "+rxMsg.getMsg()+"\n");
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
                        while ((b[0] = (byte) inputStream.read()) != (byte)10) {
                            if (b[0] != (byte)13) {
                                readBuffer.append(new String(b));
                            }
                        }
                        String scannedInput = readBuffer.toString();
                        timestamp = new java.util.Date().toString();
                        System.out.println(timestamp + ": input received:" + scannedInput);
                        displayArea.append(timestamp + ": input received:" + scannedInput + "\n");
                        if(scannedInput.substring(0, 6).equalsIgnoreCase("$GPRMC")) {
                            PPLI ppli = TDLMessageHandler.decodeOwnPosition(scannedInput);
                            currentPosition.setText(ppli.getPosLat()+", "+ppli.getPosLon());
                        }
                        if(scannedInput.charAt(0)== (char)1) {
                            TDLMessageHandler.deformatMessage(scannedInput.getBytes());
                        }
                    } catch (IOException e) {
                    }

                    break;
            }
        }
    }

}
