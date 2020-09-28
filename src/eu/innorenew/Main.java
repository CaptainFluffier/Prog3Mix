
package eu.innorenew;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Main {
    public static HashMap<String,Node> peerSet = new HashMap<String, Node>();
    public static String[] keys;
    public static long starttime, endttime;
    public static String prev = null;
    public static String display_name = "3Head";
    public static int portn;
    static ExecutorService executor = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        CryptoUtil.generatePubPvtKeyPair();
        NetworkUtil.my_ip = NetworkUtil.getLocalAddress();
        executor.submit(new InputHandler());

        if(args.length != 2)
            return;


     /*   try {
            byte[] bytes = (Jsoup.connect("https://www.youtube.com/watch?v=kOTID1Z1qPc").maxBodySize(512000).get().html().getBytes());
            System.out.println(bytes.length);
            //byte[] bytes = Jsoup.connect("https://www.instagram.com/yelyahwilliams/").execute().bodyAsBytes();
            Set<Map.Entry<String, String>> test = (Jsoup.connect("https://www.instagram.com/yelyahwilliams/").execute().headers().entrySet());
            System.out.println(test);
            for (Map.Entry<String, String> s : test) {
                System.out.println(s.getValue().getBytes().length);
            }
            //System.out.println(bytes.length);
            //byte[] bytes1 = (Jsoup.connect("https://www.shutterstock.com/?kw=hi-resolution%20images&gclid=EAIaIQobChMIld7avY2v6QIVheh3Ch2JtgNWEAAYASAAEgJ7q_D_BwE&gclsrc=aw.ds#featured-collections").maxBodySize(1024*1024).execute().bodyAsBytes());
            //System.out.println(bytes1.length);
            //System.out.println(Jsoup.connect("https://www.grailed.com/").get().html());
        } catch (IOException e) {
            e.printStackTrace();
        } */

        portn = Integer.parseInt(args[0]);
        if(!args[0].equals("5000")){
            try {
                Socket trusted = new Socket(args[1],5000);
                Node trusted_node = new Node(trusted);
                trusted_node.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Discovering local ip: " + NetworkUtil.my_ip);
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







