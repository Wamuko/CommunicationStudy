package jikkenD;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// HTTPで送信されてくる
public abstract class AbstractHttpMessage {
    protected Map<String, String> headers;
    protected byte[] body;

    public AbstractHttpMessage() {
        this.headers = new HashMap<>();
        this.body = new byte[0];
    }

    public void addHeaderField(String name, String value) {
        this.headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return this.body;
    }

    protected abstract String getStartLine();

    @Override
    public String toString() {
        return getStartLine() + "headers: " + headers + " body: " + new String(body, StandardCharsets.UTF_8);
    }
}
