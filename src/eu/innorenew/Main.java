
package eu.innorenew;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Main {
    public static HashMap<String,Node> peerSet = new HashMap<String, Node>();
    public static String[] keys;
    public static String prev = null;
    public static String display_name = "3Head";
    public static int portn;
    static ExecutorService executor = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        CryptoUtil.generatePubPvtKeyPair();
        NetworkUtil.my_ip = NetworkUtil.getLocalAddress();
        executor.submit(new InputHandler());

        if(args.length != 1)
            return;
        portn = Integer.parseInt(args[0]);
        if(!args[0].equals("5000")){
            try {
                Socket trusted = new Socket("fe80:0:0:0:ec42:c93a:9d4b:740f%eth5",5000);
                Node trusted_node = new Node(trusted);
                trusted_node.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // System.out.println("Discovering local ip: " + NetworkUtil.my_ip);
        ServerSocket server = null;
        try {
            server = new ServerSocket(Integer.parseInt(args[0]));
            while (true) {
                Socket client = server.accept();
                System.out.println("New connection accepted...");
                Node new_node = new Node(client);
                addNode(new_node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void addNode(Node node){
        executor.submit(node);
    }
}







