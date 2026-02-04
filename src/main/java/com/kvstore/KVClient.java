package com.kvstore;

import java.io.*;
import java.net.*;

public class KVClient {
    private String host;
    private int port;

    public KVClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String send(String command) throws IOException {
        // connect to server
        Socket socket = new Socket(host, port);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        // send command
        out.write(command);

        // read response & close
        String response = in.readLine();
        socket.close();

        return response;
    }
    public static void main(String[] args) throws IOException {
        KVClient client = new KVClient("localhost", 8080);
        
        System.out.println(client.send("PUT user:123 Koko"));
        System.out.println(client.send("GET user:123"));
        System.out.println(client.send("DELETE user:123"));
        System.out.println(client.send("GET user:123"));
    }
}
