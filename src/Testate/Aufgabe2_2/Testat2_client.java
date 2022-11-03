package Testate.Aufgabe2_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Testat2_client {

    public static void main(String[] args) {

            Socket s;
            BufferedReader userIn;
            BufferedReader networkIn;
            PrintWriter networtOut;

            try{
                while (true){

                    s = new Socket("localhost", 7777);
                    userIn = new BufferedReader(new InputStreamReader(System.in));
                    networkIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    networtOut = new PrintWriter(s.getOutputStream());

                    String line = userIn.readLine();
                    if(line.equals(".")) break;
                    networtOut.println(line);
                    networtOut.flush();
                    System.out.println(networkIn.readLine());
                    s.close();
                }

            }catch (Exception e){
                System.out.println(e);
            }





    }

}
