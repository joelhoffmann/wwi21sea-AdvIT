package Aufgaben.Aufgabe14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Aufgabe14_client {

    public static void main(String[] args) {
        String message = "";
        Scanner myScanner = new Scanner(System.in);
        DatagramSocket mySocket = null;
        try {
            mySocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                message= myScanner.nextLine();
                byte[] myByte = message.getBytes(StandardCharsets.UTF_8);
                mySocket.send(new DatagramPacket(myByte, myByte.length, InetAddress.getLocalHost(), 5999));

                byte[] myBuffer = new byte[65507];
                DatagramPacket myPacket = new DatagramPacket(myBuffer, myBuffer.length);

                mySocket.receive(myPacket);

                System.out.println(new String(myBuffer,0, myPacket.getLength()));

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
