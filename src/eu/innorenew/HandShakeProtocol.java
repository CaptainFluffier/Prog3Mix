package eu.innorenew;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class HandShakeProtocol {

    public static void sendHandShake(Node node){
       // System.out.println("Sending handshake");
        Message message = new Message(
                System.currentTimeMillis(),
                "handshake",
                "text",
                NetworkUtil.getLocalAddress() + " " + Main.display_name + " " + Main.portn,
                CryptoUtil.getPubRSA().getEncoded()
                );
        node.send(CryptoUtil.signMessage(message));
    }
    public static String digestHandshake(Message message, Node node){
        String [] body = message.getBody().split( " ");
        node.setAddress(body[0]);
        node.setNickname(body[1]);
        try {
            node.setPubKey(KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(message.getPubkey())));
           // System.out.println(node.getPubKey());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        node.setPort(Integer.parseInt(body[2]));
        node.setPub_key(message.getPub_key());
       //  System.out.println("Received handsake from " +node.getPub_key() +  " " + body[2] );
        return message.getPub_key();
    }
}
