package Testate.Aufgabe2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Testat2_server {
    private static int DEFAULT_PORT = 7777;
    public static void main(String[] args) {
        Socket connection = null;
        try {
            ServerSocket server = new ServerSocket(DEFAULT_PORT);
            while (true) {
                try {
                    connection = server.accept();
                    PrintWriter out = new PrintWriter(connection.getOutputStream()); //output stream to client
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); //input stream of server

                    //get message
                    String message = in.readLine();
                    String[] messageArray = null;
                    if (message != null) {
                        messageArray = message.split(" ");
                    }

                    if (messageArray != null && messageArray.length > 0) {
                        //SAVE function
                        if (messageArray[0].contains("SAVE") && messageArray.length > 1) {
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
                            //send key to client
                            out.println("KEY " + key);

                            //generate and write file
                            File myFile = new File("src/Testate/Aufgabe2/Messages/" + key);
                            PrintWriter myWriter = new PrintWriter(new FileWriter(myFile));
                            myWriter.write(message.substring(5));
                            myWriter.close();

                        }
                        //GET function
                        else if (messageArray[0].contains("GET") && messageArray.length > 1) {
                            String fileUrl = "src/Testate/Aufgabe2/Messages/" + messageArray[1];
                            File myFile = new File(fileUrl);

                            if (myFile.exists()) {
                                BufferedReader myReader = new BufferedReader(new FileReader(myFile));
                                out.println("OK " + myReader.readLine());
                            } else {
                                out.println("FAILED");
                            }

                        }
                        //Catch if input is not valid
                        else if (messageArray.length == 1) {
                            out.println("command needs attributes");
                        } else {
                            out.println("command not possible -> requires SAVE ore GET");
                        }
                        out.flush();
                    }
                } finally {
                    // for Non-Persistent Server
                    if (connection != null) connection.close();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
