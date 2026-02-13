package com.kvstore;

import java.io.*;
import java.net.*;
import java.util.*;

public class ReplicatedClient {
    private List<Integer> serverPorts = Arrays.asList(8080, 9001, 9002);
    private int W = 2; // Write quorum, only 2 ports need to accept

    // PUT system, data is replicated across 3 servers
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

    public String get(String key) {
        int R = 2; // Read quorum
        List<String> responses = new ArrayList<>();
    
        for (int port : serverPorts) {
            try {
                Socket socket = new Socket("localhost", port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
                );
    
                out.println("GET " + key);
                String value = in.readLine();
    
                if (value != null) {
                    responses.add(value);
                    System.out.println("Response from port " + port + ": " + value);
                }
    
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to reach server on port " + port);
            }
        }
    
        if (responses.size() >= R) {
            return responses.get(0);
        }
        return null;
    }
    public static void main(String[] args) {
        ReplicatedClient client = new ReplicatedClient();
        
        boolean success = client.put("name", "koko");
        System.out.println("PUT success (W=2): " + success);
        
        String value = client.get("name");
        System.out.println("GET result (R=2): " + value);
    }
    
}
