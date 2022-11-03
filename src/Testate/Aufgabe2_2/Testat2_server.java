package Testate.Aufgabe2_2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Testat2_server {

    public static void main(String[] args) {
        int DEFAULT_PORT = 7777;
        Socket connection = null;

        try {
            ServerSocket server = new ServerSocket(DEFAULT_PORT);

            while (true) {
                try {
                    connection = server.accept();
                    PrintWriter out = new PrintWriter(connection.getOutputStream());// for sending data to client
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); // for reciving data form client

                    String message = in.readLine();
                    String[] messageArray = message.split(" ");

                    if (messageArray.length > 0) {

                        if (messageArray[0].contains("SAVE")) {

                            //generate Key
                            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                            StringBuilder sb = new StringBuilder();
                            Random random = new Random();
                            int length = 16;
                            for (int i = 0; i < length; i++) {
                                int index = random.nextInt(alphabet.length());
                                char randomChar = alphabet.charAt(index);
                                sb.append(randomChar);
                            }
                            String key = sb.toString();

                            //generate File and write file

                            File myFile = new File("/Users/joel/IdeaProjects/wwi21sea-AdvIT-Coding/src/Testate/Aufgabe2_2/Messages/" + key);
                            if(myFile.createNewFile()) System.out.println("test");
                            PrintWriter myWrite = new PrintWriter(myFile); // wirte to generated file above
                            myWrite.println(message.substring(5));
                            myWrite.close();
                            //send key to client
                            out.println("KEY " + key);

                        } else if (messageArray[0].contains("GET")) {

                            String fileUrl = "/Users/joel/IdeaProjects/wwi21sea-AdvIT-Coding/src/Testate/Aufgabe2_2/Messages/" + messageArray[1];
                            File myFile = new File(fileUrl);

                            if (myFile.exists()) {
                                BufferedReader myReader = new BufferedReader(new FileReader(myFile));
                                out.println("OK " + myReader.readLine());
                            } else {
                                out.println("FAILED");
                            }

                        } else {
                            out.println("command not possible -> requiers SAVE ore GET");
                        }
                        out.flush();
                    }

                } finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
