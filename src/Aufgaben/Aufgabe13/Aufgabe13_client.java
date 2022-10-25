package Aufgaben.Aufgabe13;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Aufgabe13_client {


    public static String message = "Hello World";

    public static void main(String[] args) {
        while(true) {
            try {
                Scanner myScanner = new Scanner(System.in);
                message= myScanner.nextLine();
                DatagramSocket mySocket = new DatagramSocket();
                byte[] myByte = message.getBytes(StandardCharsets.UTF_8);

                mySocket.send(new DatagramPacket(myByte, myByte.length, InetAddress.getLocalHost(), 4999));

                mySocket.close();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
