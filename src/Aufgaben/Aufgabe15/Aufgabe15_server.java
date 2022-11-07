package Aufgaben.Aufgabe15;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Aufgabe15_server extends Thread {
    private DatagramPacket myPacket;
    private DatagramSocket myServer;

    public Aufgabe15_server(DatagramSocket myServer, DatagramPacket myPacket) {
        this.myPacket = myPacket;
        this.myServer = myServer;
    }

    @Override
    public void run() {
        try {
            //Thread.sleep((long) (Math.random() * 10000)); // waiting random time

            String line_to_send = "";
            ArrayList<String> list;
            String[] splitString;
            BufferedReader br;
            String input = new String(myPacket.getData(), 0, myPacket.getLength());

            if (input.startsWith("WRITE") | input.startsWith("READ")) {

                if (input.startsWith("READ")) splitString = input.substring(5).split(",");
                else splitString = input.substring(6).split(",");

                String fileURL = "src/Aufgaben/Aufgabe14/TextFiles/" + splitString[0];
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
                    int lineCounter;
                    lineCounter = (int) Files.lines(Path.of(fileURL)).count();
                    String line;

                    if (Integer.parseInt(splitString[1]) > lineCounter) {
                        line_to_send = "line not available";
                        myServer.send(new DatagramPacket(line_to_send.getBytes(StandardCharsets.UTF_8), line_to_send.getBytes().length, InetAddress.getLocalHost(), myPacket.getPort()));
                    } else {

                        while ((line = br.readLine()) != null) {
                            list.add(line);
                        }

                        list.set((Integer.parseInt(splitString[1]) - 1), splitString[2]); //change line

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
            try {
                myServer.send(new DatagramPacket("file not available".getBytes(StandardCharsets.UTF_8), "file not available".getBytes(StandardCharsets.UTF_8).length, InetAddress.getLocalHost(), myPacket.getPort()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Thread finished request and will be closed now");
    }

    public static void main(String[] args) {
        byte[] myBuffer;
        DatagramPacket myPacket;
        try {
            DatagramSocket myServer = new DatagramSocket(5999);
            while (true) {
                myBuffer = new byte[65507];
                myPacket = new DatagramPacket(myBuffer, myBuffer.length);
                myServer.receive(myPacket);
                new Aufgabe15_server(myServer, myPacket).start(); //start thread

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
