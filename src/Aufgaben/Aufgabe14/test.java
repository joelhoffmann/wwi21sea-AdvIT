package Aufgaben.Aufgabe14;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws IOException {

        try {
            String test = "WRITE myFile.txt,0,lol";
            test = test.substring(6);
            String[] splitString = test.split(",");
            String fileURL = "/Users/joel/IdeaProjects/wwi21sea-AdvIT-Coding/src/Aufgaben/Aufgabe14/TextFiles/" + splitString[0];
            File myFile = new File(fileURL);
            BufferedReader br  = new BufferedReader(new FileReader(myFile));

            //TODO what if file not exists
            ArrayList<String> list = new ArrayList<>();
            boolean breaker = true;
            while (breaker){
                list.add(br.readLine());
                if(list.get(list.size() - 1) == null) {
                    breaker = false;
                    list.remove(list.size() - 1);
                }
            }
            list.set(Integer.parseInt(splitString[1]), splitString[2]);
            PrintWriter printWriter = new PrintWriter (myFile);
            for(int i = 0; i< list.size(); i++){
                printWriter.println(list.get(i));
            }
            printWriter.close();


    } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
