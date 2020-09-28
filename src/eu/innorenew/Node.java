


package eu.innorenew;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;

public class Node extends Thread {
    private Socket connection;
    BufferedWriter out;
    BufferedReader in;
    Gson gson;
    int port;
    public PublicKey pub;
    String pub_key;
    String address;
    String nickname;
    boolean is_running= true;

    public Node(Socket client) {
        this.pub = CryptoUtil.getPubKey();
        this.connection = client;
        gson = new Gson();
        try {
            out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HandShakeProtocol.sendHandShake(this);
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = in.readLine()) != null && is_running) {
                Message m = gson.fromJson(line, Message.class);
                if (CryptoUtil.verifyMessage(m)) {
                    switch (m.getHeader()) {
                        case "handshake":
                            HandShakeProtocol.digestHandshake(m, this);
                            PeerDiscoveryProtocol.requestPeers(this);
                            Main.peerSet.put(this.pub_key, this);
                            System.out.println("Added to list: " + this.getPort());
                            if(this.getPub_key()==null){
                                HandShakeProtocol.sendHandShake(this);
                            }
                            break;
                        case "peer_discovery":
                            PeerDiscoveryProtocol.digest(m, this);
                            break;
                        case "chat_protocol":
                            ChatProtocol.digest(m, this);
                            break;
                        case "return":
                            ChatProtocol.digestReturn(m);
                            break;
                        default:
                            System.out.println("Received message from: " + m.getPub_key());
                            System.out.println("Signature: " + m.getSignature());
                            break;
                    }
                } else {
                    System.out.println("MITM attack: Forged message!");
                }
            }
        } catch (IOException e) {
            HandShakeProtocol.sendHandShake(this);
            System.out.println("Socket broke");
        }
    }

    public void send(Message m) {
        try {
            out.write(gson.toJson(m));
            out.newLine();
            //System.out.println("Sending: " +gson.toJson(m));
            out.flush();
        } catch (IOException e) {
            try {
                out.close();
                in.close();
                connection.close();
                Main.peerSet.remove(this.getPub_key());
                this.is_running = false;
                System.out.println("Peer removed due to broken connection");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickname() {
        return nickname;
    }

    public void setPort(int portn) { this.port = portn; }

    public int getPort(){ return port; }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public PublicKey getPubKey(){ return pub; }

    public  void setPubKey(PublicKey key) {this.pub = key; }
}






