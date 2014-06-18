/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dti.tdl.messaging;

import com.google.common.primitives.Longs;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 *
 * @author wichai.p
 */
public class TDLMessageHandler {

    public static boolean hasUnfinishedMsg = false;
    public final static LinkedList<String> txStack = new LinkedList<String>();
    public final static LinkedList<TDLMessage> rxStack = new LinkedList<TDLMessage>();

    public static void formatTextMessage(TDLMessage message) {
        //use 9600 bps slot time = 100 ms
        //max frame size = 100 bytes
        //header+tailer = 11 bytes
        //max message size = 89 bytes

        byte[] start = {(byte) 1};
        byte[] startMsg = {(byte) 2};
        byte[] endMsg = {(byte) 3};
        byte[] end = {(byte) 4};

        byte[] from = hexStringToByteArray(message.getFromId());
        byte[] to = hexStringToByteArray(message.getToId());
        byte[] msg = null;
//        try {
            msg = message.getMsg().getBytes();
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
        int numBlk = 1;

        if (msg.length > 89) {
            numBlk = msg.length / 89;
            if (msg.length % 89 > 0) {
                numBlk++;
            }

        }
        //String utf8msg = new String()
        long checksum = CRC32Checksum(message.getMsg());
        //byte[] checksumBytes = longToBytes(checksum);
        byte[] checksumBytes = Longs.toByteArray(checksum);
        //System.out.println(checksumBytes.length);
        //System.out.println("Start msg byte : " + (int) startMsg[0]);

//        try {
//            String test = new String(checksumBytes, "UTF-8");
//            byte[] testBytes = test.getBytes("UTF-8");
//            long testCS = bytesToLong(testBytes);
//            System.out.println(checksum);
//            System.out.println(testCS);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
        byte[] frame = null;
        int msgLength = msg.length;
        int msgIdx = 0;
        for (int i = 0; i < numBlk; i++) {
            if (msgLength > 89) {
                msgLength = msgLength - 89;
            }
            int msgBlkLength = 89;
            if (i == numBlk - 1) {
                msgBlkLength = msgLength;
            }
            msgIdx = 89 * i;
            frame = new byte[start.length + from.length + to.length + checksumBytes.length + startMsg.length + msgBlkLength + endMsg.length + end.length];
            System.arraycopy(start, 0, frame, 0, start.length);
//            System.out.println(start.length);
            System.arraycopy(from, 0, frame, start.length, from.length);
//            System.out.println(start.length + from.length);
            
            System.arraycopy(to, 0, frame, start.length + from.length, to.length);
//            for (int k = 0; k < frame.length; k++) {
//                System.out.println("Partial Tx frame Byte " + k + ": " + (int) frame[k]);
//            }
//            System.out.println(to.length);
//            System.out.println(start.length + from.length + to.length);
            System.arraycopy(checksumBytes, 0, frame, start.length + from.length + to.length, checksumBytes.length);
//            System.out.println(checksumBytes.length);
//            for (int k = 0; k < checksumBytes.length; k++) {
//                System.out.println("Long Byte " + k + ": " + (int) checksumBytes[k]);
//            }
//            for (int k = 0; k < frame.length; k++) {
//                System.out.println("Partial Tx frame Byte " + k + ": " + (int) frame[k]);
//            }
            System.arraycopy(startMsg, 0, frame, start.length + from.length + to.length + checksumBytes.length, startMsg.length);
            System.arraycopy(Arrays.copyOfRange(msg, msgIdx, msgIdx + msgBlkLength), 0, frame, start.length + from.length + to.length + checksumBytes.length + startMsg.length, msgBlkLength);
            System.arraycopy(endMsg, 0, frame, start.length + from.length + to.length + checksumBytes.length + startMsg.length + msgBlkLength, endMsg.length);
            System.arraycopy(end, 0, frame, start.length + from.length + to.length + checksumBytes.length + startMsg.length + msgBlkLength + endMsg.length, end.length);
//            for (int k = 0; k < frame.length; k++) {
//                System.out.println("Partial Tx frame Byte " + k + ": " + (int) frame[k]);
//            }
            String txMsg = null;
            StringBuilder builder = new StringBuilder();
//            try {
                for (int j = 0; j < frame.length; j++) {
                    if(j<frame.length-1) {
                    builder.append((int)frame[j]+",");
                    } else {
                        builder.append((int)frame[j]);
                    }
                }
                txMsg = builder.toString();
                //txMsg = new String(frame);
//            } catch (Exception ex) {
//                Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
//            }
            txStack.add(txMsg);
            System.out.println(txMsg);
//            try {
//                System.out.println(txMsg.getBytes().length);
//            } catch (UnsupportedEncodingException ex) {
//                Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            System.out.println(frame.length);
            for (int k = 0; k < frame.length; k++) {
                System.out.println("Tx frame Byte " + k + ": " + (int) frame[k]);
            }
        }

    }

    public static void deformatMessage(byte[] bytes) {
        int size = bytes.length;
        int startIdx = -1;
        int endIdx = -1;
        int startMsgIdx = -1;
        int endMsgIdx = -1;
//        try {
            String rxMsgT = new String(bytes);
//            System.out.println(rxMsgT);
//            System.out.println(size);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
        for (int k = 0; k < bytes.length; k++) {
            System.out.println("Rx frame Byte " + k + ": " + (int) bytes[k]);
        }
        for (int i = 0; i < size; i++) {
            if (bytes[i] == (byte) 1) {
                startIdx = i;

            }
            if (bytes[i] == (byte) 2) {
                startMsgIdx = i;

            }
            if (bytes[i] == (byte) 3) {
                endMsgIdx = i;

            }
            if (bytes[i] == (byte) 4) {
                endIdx = i;

            }
        }

        if (startIdx == -1 || endIdx == -1) {
            TDLMessage rxMsg = new TDLMessage(null, null, null, null, "Corrupted Message: invalid frame");
            rxStack.add(rxMsg);
            return;
        }

        byte[] formId = {bytes[startIdx + 1], bytes[startIdx + 2]};
        byte[] toId = {bytes[startIdx + 3], bytes[startIdx + 4]};
        //int checksumSize = startMsgIdx - startIdx+9;
        byte[] checksum = Arrays.copyOfRange(bytes, startIdx + 5, startMsgIdx);
        byte[] msg = Arrays.copyOfRange(bytes, startMsgIdx + 1, endMsgIdx);
        //try {
            String rxMsg = new String(msg);
            System.out.println(rxMsg);
            
            long newChecksum = CRC32Checksum(rxMsg);
            System.out.println(startIdx);
            System.out.println(startMsgIdx);
            System.out.println(endIdx);
            System.out.println(checksum.length);
            //long receiveChecksum = ByteUtil.bytesToLong(checksum);
            long receiveChecksum = Longs.fromByteArray(checksum);
            //System.out.println(checksum.toString());
            System.out.println(newChecksum);
            System.out.println(receiveChecksum);
            if (newChecksum != receiveChecksum) {
                TDLMessage rxMsgObj = new TDLMessage(null, null, null, null, "Corrupted Message: checksum not matched");
                rxStack.add(rxMsgObj);
                return;
            }

            TDLMessage rxMsgObj = new TDLMessage(null, null, null, null, rxMsg);
            rxStack.add(rxMsgObj);
//        } catch (UnsupportedEncodingException ex) {
//        }

    }

    public static byte[] getBytesFromQueue() {
        String txBytesStr = txStack.removeFirst();
        String[] txBytesStrArray = txBytesStr.split(",");
        byte[] txBytes = new byte[txBytesStrArray.length];
        for (int i = 0; i < txBytesStrArray.length; i++) {
            int byteInt = Integer.parseInt(txBytesStrArray[i]);
            txBytes[i] = (byte) byteInt;
        }
        return txBytes;
    }
    public static byte[] hexStringToByteArray(String s) {
        if (s == null || s == "") {
            s = "0000";
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static long CRC32Checksum(String input) {
        long checksum = 0;
        // get bytes from string
        byte[] bytes = null;
        //try {
            bytes = input.getBytes();
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
        Checksum check = new CRC32();
        // update the current checksum with the specified array of bytes
        check.update(bytes, 0, bytes.length);
        // get the current checksum value
        checksum = check.getValue();
        return checksum;

    }

    public static PPLI decodeOwnPosition(String posStr) {
        PPLI pos = new PPLI();
        //pos.posId = "0000";
        String[] posValues = posStr.split(",");
        String posTime = posValues[1];
        String posDate = posValues[9];
        
//        double posLat = degreeToDecimal(posValues[3]);
//        double posLon = degreeToDecimal(posValues[5]);
        double posLat = Double.parseDouble(posValues[3]);
        double posLon = Double.parseDouble(posValues[5]);
        double posSpeed = Double.parseDouble(posValues[7]);
        double posTC = Double.parseDouble(posValues[8]);
        double posMV = Double.parseDouble(posValues[9]);
        
        pos.setPosId("0000");
        pos.setPosDate(posDate);
        pos.setPosTime(posTime);
        pos.setPosLat(posLat);
        pos.setPosLon(posLon);
        pos.setTrueCourse(posTC);
        pos.setSpeed(posSpeed);
        pos.setMagVariation(posMV);
        return pos;
    }

    public static double degreeToDecimal(String degreeStr) {
        String posMinStr = degreeStr.substring(degreeStr.length()-5,2);
        String posSecondStr = degreeStr.substring(degreeStr.length()-2,2);
        String posDegreeStr = degreeStr.substring(0,degreeStr.length()-5);
        double posMin = Double.parseDouble(posMinStr);
        double posSecond = Double.parseDouble(posSecondStr);
        posSecond = posMin*60 + posSecond;
        posSecondStr = String.valueOf(posSecond);
        int decimal = posDegreeStr.length();
        posSecond = posSecond/decimal;
        double posDegree = Double.parseDouble(posDegreeStr);
        return posDegree+posSecond;
        
    }
}
