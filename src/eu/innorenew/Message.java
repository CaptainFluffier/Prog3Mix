

package eu.innorenew;

import java.security.PublicKey;
import java.util.Base64;

public class Message {
    private long timestamp;
    private String header;
    private String contentType;
    private String body;
    private String pub_key;
    private String signature;
    private byte[] pubkey;
    private String secret;

    public Message(long timestamp, String header, String contentType, String body, String pub_key, String signature) {
        this.timestamp = timestamp;
        this.header = header;
        this.contentType = contentType;
        this.body = body;
        this.pub_key = pub_key;
        this.signature = signature;
    }

    public Message(long timestamp, String header, String contentType, String body, byte[] key) {
        this.timestamp = timestamp;
        this.header = header;
        this.contentType = contentType;
        this.body = body;
        this.pubkey = key;
    }
    public Message(long timestamp, String header, String contentType, String body) {
        this.timestamp = timestamp;
        this.header = header;
        this.contentType = contentType;
        this.body = body;
    }

    public Message(long timestamp, String header, String contentType, String body, String key, PublicKey pub) {
        this.timestamp = timestamp;
        this.header = header;
        this.contentType = contentType;
        this.body = body;
        this.secret = key;
        this.pub_key = Base64.getEncoder().encodeToString(pub.getEncoded());
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getPubkey() { return pubkey; }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }

    public String getSignature() {
        return signature;
    }

    public String getSecret() {return secret;}
    public void setSignature(String signature) {
        this.signature = signature;
    }
}
