package Aufgaben.Aufgabe14;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Aufgabe14_server {

    public static void main(String[] args) {

        try {
            DatagramSocket myServer = new DatagramSocket(5999);

            String test = "READ myFile.txt,6";

            if(test.contains("READ")){

                test = test.substring(5);
                String[] splitString = test.split(",");
                String fileURL = "/Users/joel/IdeaProjects/wwi21sea-AdvIT-Coding/src/Aufgaben/Aufgabe14/TextFiles/" + splitString[0];
                File myFile = new File(fileURL);
                BufferedReader br  = new BufferedReader(new FileReader(myFile));

                System.out.println(br.readLine());


            }else{
                System.out.println("befehl nicht möglich -> READ required");
            }

            while (true){

                try {
                    byte[] myBuffer = new byte[65507];
                    DatagramPacket myPacket = new DatagramPacket(myBuffer, myBuffer.length);
                    myServer.receive(myPacket);




                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



        } catch (SocketException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
