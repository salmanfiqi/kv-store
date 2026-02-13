package com.kvstore;

import java.io.*;
import java.net.*;
import java.util.*;

public class ReplicatedClient {
    private List<Integer> serverPorts = Arrays.asList(8080, 9001, 9002);
    private int W = 2; // Write quorum, only 2 ports need to accept

    public boolean put(String key, String value) {
        int acks = 0;

        for (int port : serverPorts) {
            try {
                Socket socket = new Socket("localhost", port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("PUT " + key + " " + value);
                String response = in.readLine();

                if ("OK".equals(response)) {
                    acks++;
                    System.out.println("ACK from port " + port);
                }

                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to reach server on port " + port);
            }
        }

        System.out.println("Total ACKs: " + acks + ", needed: " + W);
        return acks >= W;
    }

    public static void main(String[] args) {
        ReplicatedClient client = new ReplicatedClient();
        boolean success = client.put("name", "koko");
        System.out.println("PUT success (W = 2): " + success);
    }
    
}
