package Aufgaben.Aufgabe14;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Aufgabe14_server {

    public static void main(String[] args) {

        BufferedReader br;
        byte[] myBuffer;
        DatagramPacket myPacket;
        String line_to_send = "";
        ArrayList<String> list;

        try {
            DatagramSocket myServer = new DatagramSocket(5999);
            while (true) {
                myBuffer = new byte[65507];
                myPacket = new DatagramPacket(myBuffer, myBuffer.length);
                try {
                    myServer.receive(myPacket);
                    String input = new String(myBuffer, 0, myPacket.getLength());
                    if (input.contains("WRITE") | input.contains("READ")) {
                        String working_string = "";
                        if (input.contains("READ")) working_string = input.substring(5);
                        if (input.contains("WRITE")) working_string = input.substring(6);
                        String[] splitString = working_string.split(",");

                        String fileURL = "/Users/joel/IdeaProjects/wwi21sea-AdvIT-Coding/src/Aufgaben/Aufgabe14/TextFiles/" + splitString[0];
                        File myFile = new File(fileURL);
                        br = new BufferedReader(new FileReader(myFile));

                        if (input.contains("READ")) {

                            for (int i = 0; i < Integer.parseInt(splitString[1]); i++) {
                                line_to_send = br.readLine();
                            }

                            if (line_to_send != null) {
                                myServer.send(new DatagramPacket(line_to_send.getBytes(StandardCharsets.UTF_8), line_to_send.getBytes().length, InetAddress.getLocalHost(), myPacket.getPort()));
                            } else {
                                line_to_send = "line not available";
                                myServer.send(new DatagramPacket(line_to_send.getBytes(StandardCharsets.UTF_8), line_to_send.getBytes().length, InetAddress.getLocalHost(), myPacket.getPort()));
                            }
                        } else if (input.contains("WRITE")) {
                            list = new ArrayList<>();
                            boolean breaker = true;
                            while (breaker) {
                                list.add(br.readLine());
                                if (list.get(list.size() - 1) == null) {
                                    breaker = false;
                                    list.remove(list.size() - 1);
                                }
                            }
                            if (Integer.parseInt(splitString[1]) > list.size()) {
                                line_to_send = "line not available";
                                myServer.send(new DatagramPacket(line_to_send.getBytes(StandardCharsets.UTF_8), line_to_send.getBytes().length, InetAddress.getLocalHost(), myPacket.getPort()));
                            } else {
                                list.set((Integer.parseInt(splitString[1]) - 1), splitString[2]);
                                PrintWriter printWriter = new PrintWriter(myFile);
                                for (int i = 0; i < list.size(); i++) {
                                    printWriter.println(list.get(i));
                                }
                                printWriter.close();
                                line_to_send = "ok";
                                myServer.send(new DatagramPacket(line_to_send.getBytes(StandardCharsets.UTF_8), line_to_send.getBytes().length, InetAddress.getLocalHost(), myPacket.getPort()));
                            }
                        }
                    } else {
                        System.out.println("befehl nicht möglich -> READ required");
                    }
                } catch (FileNotFoundException e) {
                    myServer.send(new DatagramPacket("file not available".getBytes(StandardCharsets.UTF_8), "file not available".getBytes(StandardCharsets.UTF_8).length, InetAddress.getLocalHost(), myPacket.getPort()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
