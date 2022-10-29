package Testate.Aufgabe2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Testat2_client {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 7777);

            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

            BufferedReader networkIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter networkOut = new PrintWriter(s.getOutputStream());

            try {

                while (true){
                    String theLine = userIn.readLine();
                    if(theLine.equals(".")) break;
                    networkOut.println(theLine);
                    networkOut.flush();
                    System.out.println(networkIn.readLine());
                    break;
                }

            }catch (Exception e){
                System.out.println(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
