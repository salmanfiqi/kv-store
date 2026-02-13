package com.kvstore;

import java.io.*;
import java.net.*;

public class KVServer {
    private KVStore store;
    private int port;

    public KVServer(int port) {
        this.port = port;
        this.store = new KVStore();
    }

    public void start() throws IOException{
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                Socket client = server.accept();

                // read from client
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream())
                );

                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                String line = in.readLine();

                String[] cmd = line.split(" ");

                if (cmd[0].equals("PUT")) {
                    store.put(cmd[1], cmd[2]);
                    out.println("OK");
                }

                if (cmd[0].equals("GET")) {
                    String value = store.get(cmd[1]);
                    out.println(value);
                }

                if (cmd[0].equals("DELETE")) {
                    store.delete(cmd[1]);
                    out.println("OK");
                }

                client.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        KVServer server = new KVServer(port);
        System.out.println("Server starting on port " + port);
        server.start();
    }
}
