package eu.innorenew;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class InputHandler implements Runnable {
    Scanner input;

    @Override
    public void run() {
        input = new Scanner(System.in);
        String line;
        while ((line = input.next()) != null) {
            // System.out.println("Reading line: " + line);
            if (line.startsWith("|")) {
                StringTokenizer st = new StringTokenizer(line);
                String command = st.nextToken();
                if(command.equalsIgnoreCase("|list_nodes")){
                    //list nodes
                    for(Node n : Main.peerSet.values()){
                        System.out.println("Node: " + n.getPub_key());
                        System.out.println("Address: " + n.getAddress());
                        System.out.println();
                    }
                }
            }else{ //normal message
                Node[] path = ChatProtocol.randomPath();
                Message message = null;
                String temp="";
                String tempkey="";
                Main.keys = new String[path.length];
                for(int i=path.length-1;i>=0;i--){
                    String tmp = CryptoUtil.getAlphaNumericString(8);
                    Main.keys[i] = tmp;
                        if(message!=null){
                            temp = message.getBody();
                            tempkey = message.getSecret();
                        }
                        if(i==path.length-1){
                            line += "ČČČ" + tmp + "ČČČ" + "zadnji";
                            line = CryptoUtil.encryptAES(line,tmp);
                        }else {
                            temp += "ČČČ" + tempkey + "ČČČ" + path[i+1].getPub_key();
                            line = CryptoUtil.encryptAES(temp,tmp);
                        }
                        try{
                            tmp = CryptoUtil.encryptText(tmp,path[i].getPubKey());
                        }catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException |
                                BadPaddingException | InvalidKeyException e) {
                            e.printStackTrace();
                            System.out.println("Error with keys");
                        }
                    // System.out.println("msg: " + line + " :: " + tmp);
                     message = new Message(System.currentTimeMillis(),
                            "chat_protocol",
                            "text",
                            line,
                            tmp,CryptoUtil.pub);
                }
             //   System.out.println("Sending to: " + path[0].getPort() + " " + Main.keys[Main.keys.length-1]);
                ChatProtocol.broadcast(CryptoUtil.signMessage(message),path[0]); // null
            }
        }
    }
}
