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
        try {
            msg = message.getMsg().getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            System.out.println(start.length);
            System.arraycopy(from, 0, frame, start.length, from.length);
            System.out.println(start.length + from.length);
            
            System.arraycopy(to, 0, frame, start.length + from.length, to.length);
            for (int k = 0; k < frame.length; k++) {
                System.out.println("Partial Tx frame Byte " + k + ": " + (int) frame[k]);
            }
            System.out.println(to.length);
            System.out.println(start.length + from.length + to.length);
            System.arraycopy(checksumBytes, 0, frame, start.length + from.length + to.length, checksumBytes.length);
            System.out.println(checksumBytes.length);
            for (int k = 0; k < checksumBytes.length; k++) {
                System.out.println("Long Byte " + k + ": " + (int) checksumBytes[k]);
            }
            for (int k = 0; k < frame.length; k++) {
                System.out.println("Partial Tx frame Byte " + k + ": " + (int) frame[k]);
            }
            System.arraycopy(startMsg, 0, frame, start.length + from.length + to.length + checksumBytes.length, startMsg.length);
            System.arraycopy(Arrays.copyOfRange(msg, msgIdx, msgIdx + msgBlkLength), 0, frame, start.length + from.length + to.length + checksumBytes.length + startMsg.length, msgBlkLength);
            System.arraycopy(endMsg, 0, frame, start.length + from.length + to.length + checksumBytes.length + startMsg.length + msgBlkLength, endMsg.length);
            System.arraycopy(end, 0, frame, start.length + from.length + to.length + checksumBytes.length + startMsg.length + msgBlkLength + endMsg.length, end.length);
            for (int k = 0; k < frame.length; k++) {
                System.out.println("Partial Tx frame Byte " + k + ": " + (int) frame[k]);
            }
            String txMsg = null;
            StringBuilder builder = new StringBuilder();
            try {
//                for (int j = 0; j < frame.length; j++) {
//                    builder.append(new String(to))
//                }
                txMsg = new String(frame, "ISO-8859-1");
            } catch (Exception ex) {
                Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            txStack.add(txMsg);
            System.out.println(txMsg);
            try {
                System.out.println(txMsg.getBytes("ISO-8859-1").length);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(frame.length);
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
        try {
            String rxMsgT = new String(bytes, "ISO-8859-1");
            System.out.println(rxMsgT);
            System.out.println(size);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            String rxMsg = new String(msg, "ISO-8859-1");
            System.out.println(rxMsg);
            System.out.println(CRC32Checksum("Hello"));

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
        } catch (UnsupportedEncodingException ex) {
        }

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
        try {
            bytes = input.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TDLMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Checksum check = new CRC32();
        // update the current checksum with the specified array of bytes
        check.update(bytes, 0, bytes.length);
        // get the current checksum value
        checksum = check.getValue();
        return checksum;

    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes);
        buffer.flip();//need flip 
        return buffer.getLong();
    }

}
