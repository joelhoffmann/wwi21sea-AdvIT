package Testate.Aufgabe3;

import Aufgaben.Aufgabe15.Aufgabe15_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

public class Testat3_Server extends Thread{

    private DatagramSocket serverSocket;
    private int id;

    public Testat3_Server (DatagramSocket serverSocket, int id){
        this.serverSocket = serverSocket;
        this.id = id;
    }

    @Override
    public void run() {

        System.out.println("Thread " + this.id + " started");

        try{

            while (true){

                DatagramPacket myPacket = buffer.takeElementFromBuffer();

                String line_to_send = "";
                ArrayList<String> list;
                String[] splitString;
                BufferedReader br;
                String input = new String(myPacket.getData(), 0, myPacket.getLength());

                if (input.startsWith("WRITE") | input.startsWith("READ")) {

                    if (input.startsWith("READ")) splitString = input.substring(5).split(",");
                    else splitString = input.substring(6).split(",");

                    if(input.startsWith("WRITE")){

                        monitor.startWrite(this.id);

                        String fileURL = "src/Aufgaben/Aufgabe14/TextFiles/" + splitString[0];
                        File myFile = new File(fileURL);
                        br = new BufferedReader(new FileReader(myFile));

                        list = new ArrayList<>();
                        int lineCounter;
                        lineCounter = (int) Files.lines(Path.of(fileURL)).count();
                        String line;

                        if (Integer.parseInt(splitString[1]) > lineCounter) {
                            line_to_send = "line not available";
                            serverSocket.send(new DatagramPacket(line_to_send.getBytes(StandardCharsets.UTF_8), line_to_send.getBytes().length, InetAddress.getLocalHost(), myPacket.getPort()));
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
                            Thread.sleep(10000);
                            serverSocket.send(new DatagramPacket(line_to_send.getBytes(StandardCharsets.UTF_8), line_to_send.getBytes().length, InetAddress.getLocalHost(), myPacket.getPort()));
                        }

                        monitor.endWrite(this.id);

                    }else{

                        monitor.startRead(this.id);

                        String fileURL = "src/Aufgaben/Aufgabe14/TextFiles/" + splitString[0];
                        File myFile = new File(fileURL);
                        br = new BufferedReader(new FileReader(myFile));

                        for (int i = 0; i < Integer.parseInt(splitString[1]); i++) {
                            line_to_send = br.readLine();
                        }

                        Thread.sleep(3000);

                        if (line_to_send != null) {
                            serverSocket.send(new DatagramPacket(line_to_send.getBytes(StandardCharsets.UTF_8), line_to_send.getBytes().length, InetAddress.getLocalHost(), myPacket.getPort()));
                        } else {
                            line_to_send = "line not available";
                            serverSocket.send(new DatagramPacket(line_to_send.getBytes(StandardCharsets.UTF_8), line_to_send.getBytes().length, InetAddress.getLocalHost(), myPacket.getPort()));
                        }

                        monitor.endRead(this.id);

                    }

                }else {
                    System.out.println("befehl nicht möglich -> READ required");
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static DatagrammPaketBuffer buffer = new DatagrammPaketBuffer();
    private static Testat3_Monitor monitor = new Testat3_Monitor();

    public static void main(String[] args) {
        byte[] myBuffer;
        DatagramPacket myPacket;
        try {
            DatagramSocket myServer = new DatagramSocket(5999);

            for(int i = 0; i< 5 ; i++){

                new Testat3_Server(myServer, i).start();

            }

            while (true) {
                myBuffer = new byte[65507];
                myPacket = new DatagramPacket(myBuffer, myBuffer.length);
                myServer.receive(myPacket);

                buffer.addElementToBuffer(myPacket);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class DatagrammPaketBuffer {
        int max = 5;
        int nextfree = 0;
        int nextfull = 0;
        int ctr = 0;
        DatagramPacket[] buffer = new DatagramPacket[max];

        public synchronized void addElementToBuffer(DatagramPacket inputPacket) {

            try {
                while (ctr == max) {
                    wait();
                }

                buffer[nextfree] = inputPacket;
                if (++nextfree == max) {
                    nextfree = 0;
                }
                ++ctr;
                System.out.println("Element added to buffer");
                notifyAll();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public synchronized DatagramPacket takeElementFromBuffer() {

            try {

                while (ctr == 0) {
                    wait();
                }
                DatagramPacket outputpacket = buffer[nextfull];
                if (++nextfull == max) {
                    nextfull = 0;
                }
                --ctr;
                notifyAll();
                System.out.println("Element removed from buffer");
                return outputpacket;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }
}
