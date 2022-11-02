package Testate.Aufgabe2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Testat2_client {
    public static void main(String[] args) {
            Socket s;
            BufferedReader userIn ;
            BufferedReader networkIn;
            PrintWriter networkOut;

            try {

                while (true){
                    s = new Socket("localhost", 7777);
                    userIn = new BufferedReader(new InputStreamReader(System.in));
                    networkIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    networkOut = new PrintWriter(s.getOutputStream());
                    String line = userIn.readLine();
                    networkOut.println(line);
                    networkOut.flush();
                    System.out.println(networkIn.readLine());
                    s.close();
                }

            }catch (Exception e){
                System.out.println(e);
            }

    }
}
