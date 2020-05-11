package eu.innorenew;

import org.jsoup.Jsoup;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.text.Utilities;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ChatProtocol {
    static HashMap<String, Message> message_log = new HashMap<>();

    public static synchronized void digest(Message message, Node node){
     //  if(!message_log.containsKey(message.getSignature())){
            Node origin = Main.peerSet.get(message.getPub_key());
            Main.prev = message.getPub_key();
        System.out.println("Prev je: " + Main.prev);
        try {
            System.out.println("Digesting msg: ");
            String key = CryptoUtil.decryptText(message.getSecret(),CryptoUtil.pvtRSA);
            String[] arrOfStr = CryptoUtil.decryptAES(message.getBody(),key).split("ČČČ", 0);
            //System.out.println(arrOfStr[0] + " " + arrOfStr[1] + " " + arrOfStr[2] + " " + key);
            if(arrOfStr[arrOfStr.length-1].equals("zadnji")){
                if(Main.prev==null) return;
                System.out.println("Enoded with" + arrOfStr[arrOfStr.length-2]);
                Message m = new Message(System.currentTimeMillis(),
                        "return",
                        "text",
                        CryptoUtil.encryptAES(getHtml(arrOfStr[0]),arrOfStr[arrOfStr.length-2]));
                ChatProtocol.broadcast(CryptoUtil.signMessage(m),Main.peerSet.get(Main.prev));
                Main.prev = null;
            }else {
                Message msg = new Message(System.currentTimeMillis(),
                        "chat_protocol",
                        "text",
                        arrOfStr[0],
                        arrOfStr[arrOfStr.length-2],CryptoUtil.pub);
                Main.peerSet.get(arrOfStr[arrOfStr.length-1]);
                ChatProtocol.broadcast(CryptoUtil.signMessage(msg),Main.peerSet.get(arrOfStr[arrOfStr.length-1]));
            }
        } catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException |
                BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Error decrypting");
        }}
         /*   System.out.println("\033[42m" + "   "+ "\033[0m" + "\033[1;92m " +
                    origin.getNickname() +
                    " : "+
                    "\033[1;35m" +
                    message.getBody() +
                    "\033[0m" + message.getSecret() );
            System.out.println("BROADCASTING: " + message.getBody());
            broadcast(message, node); */



    public static synchronized void digestReturn(Message message){
        System.out.println("Returning: ");
        if(Main.prev==null)
            System.out.println(CryptoUtil.decryptAES(message.getBody(),Main.keys[Main.keys.length-1]));
        else{
            message.setPub_key(Main.prev);
            ChatProtocol.broadcast(CryptoUtil.signMessage(message),Main.peerSet.get(Main.prev));
            Main.prev = null;
        }
    }

    public static void broadcast(Message message, Node node){
       // message_log.put(message.getSignature(),message);
        for(Node n: Main.peerSet.values()){
            if(n.equals(node)) {
               // System.out.println("Sending: ");
                n.send(message);
                break;
            }
        }
    }

    public static String getHtml(String url) { // String url
        try {
            String html = Jsoup.connect(url).get().html();
            return html;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading html";
        }
    }

    public static Node[] randomPath(){
    //    int mix = ThreadLocalRandom.current().nextInt(2, Main.peerSet.size()+1);
     //   Node[] path = new Node[mix];
      //  System.out.println("Nodes in mix: " + mix);
        boolean turn = false;
        Node[] path = new Node[Main.peerSet.size()];
        int i = 0;
     //   while(i!=mix){
            for(Node n: Main.peerSet.values()){
              //  for(int j=0;j<mix;j++){
                   // if(path[].getPort() != n.getPort()){
                path[i] = n;
                i++;
                    //}
             //   }
            }
       // }
        for(int j =0; j<path.length;j++){
            System.out.println(path[j].getPort());
        }
    return path;}
}
