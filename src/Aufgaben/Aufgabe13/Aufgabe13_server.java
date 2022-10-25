package Aufgaben.Aufgabe13;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Aufgabe13_server {

    public static void main(String[] args) {

        try {
            DatagramSocket mySocket = new DatagramSocket(4999);

            while (true) {
                try {

                    byte[] myBuffer = new byte[65507];
                    DatagramPacket myPacket = new DatagramPacket(myBuffer, myBuffer.length);
                    mySocket.receive(myPacket);
                    System.out.println(new String(myBuffer, 0, myPacket.getLength()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }


    }

}
