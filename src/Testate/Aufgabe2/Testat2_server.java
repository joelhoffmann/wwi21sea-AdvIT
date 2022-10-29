package Testate.Aufgabe2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.Charset;
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
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); //input stream from client
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
                            //send key to client
                            out.println("KEY " + key);

                            //generate and write file
                            File myFile = new File("/Users/joel/IdeaProjects/wwi21sea-AdvIT-Coding/src/Testate/Aufgabe2/Messages/" + key);
                            FileWriter myWriter = new FileWriter(myFile);
                            myWriter.write( message.substring(5));
                            myWriter.close();

                        } else if (messageArray[0].contains("GET")) {
                            String fileUrl = "/Users/joel/IdeaProjects/wwi21sea-AdvIT-Coding/src/Testate/Aufgabe2/Messages/" + messageArray[1];
                            File myFile = new File(fileUrl);

                            if(myFile.exists()){
                                BufferedReader myReader = new BufferedReader(new FileReader(myFile));
                                out.println("OK " + myReader.readLine());
                            }else{
                                out.println("FAILED");
                            }

                        } else {
                            out.println("command not possible -> requiers SAVE ore GET");
                        }
                        out.flush();
                    }
                }finally {
                    if(connection != null) connection.close();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
