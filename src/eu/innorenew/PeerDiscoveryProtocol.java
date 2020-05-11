package eu.innorenew;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class PeerDiscoveryProtocol {

    public static void requestPeers(Node node) {
        //System.out.println("Sending peer reqeust");
        Message message = new Message(
                System.currentTimeMillis(),
                "peer_discovery",
                "text",
                "request"
        );
        node.send(CryptoUtil.signMessage(message));
    }

    public static void digest(Message message, Node node) {
        Gson g = new Gson();
        if (message.getBody().equals("request")) {
           // System.out.println("Received request for peers");
            //prepare and send peer list
            //String body = g.toJson(Main.peerSet.values().stream().map(n -> n.getAddress()).collect(Collectors.toList()));
            String peers[] = new String[Main.peerSet.size()];
            int cnt=0;
            for (Node n: Main.peerSet.values()) {
                //System.out.println("loop to nodes");
                peers[cnt] = n.getAddress() + " " + n.getPub_key() + " " + n.getPort();
                cnt++;
            }
            String body = g.toJson(peers);
            //System.out.println(body);
            Message reply = new Message(System.currentTimeMillis(),"peer_discovery","text/json",body);
            node.send(CryptoUtil.signMessage(reply));
        } else {
            String[] newPeers = g.fromJson(message.getBody(),String[].class);
             //System.out.println("Received peer list");
            for (int i = 0; i < newPeers.length; i++) {
                String[] info = newPeers[i].split(" ");
               // System.out.println(newPeers[i]);
                if (!Main.peerSet.containsKey(info[1]) && Main.portn != Integer.parseInt(info[2])) {
                    try {
                     //   System.out.println("Testing: " + Main.portn + " : " + Integer.parseInt(info[2]));
                        Socket newpeer = new Socket(info[0], Integer.parseInt(info[2]));
                        Node newNode = new Node(newpeer);
                        Main.addNode(newNode);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}



