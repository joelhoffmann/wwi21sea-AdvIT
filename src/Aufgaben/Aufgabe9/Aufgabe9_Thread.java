package Aufgaben.Aufgabe9;

import java.util.Arrays;

public class Aufgabe9_Thread extends Thread{

    private int id;
    private int[] mainarray;
    private int summe;
    private int time_sum;

    public Aufgabe9_Thread (int id, int[] mainarray){
        this.id = id;
        this.mainarray = mainarray;
    }

    public void run(){
        //final long timeStart = System.currentTimeMillis();
        try{
            for(int i = 0; i< mainarray.length; i ++){
                summe = summe + mainarray[i];
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        //final long timeEnd = System.currentTimeMillis();
        //time_sum += (timeEnd - timeStart);
        //System.out.println("Verlaufszeit der Schleife: " + (timeEnd - timeStart) + " Millisek.");

    }

    public static void main(String[] args) {
        int[] myArray = new int[2097152];

        for(int i = 0; i< 2097152; i++){
            myArray[i] = (int) (Math.random() * 1000);
        }

        Aufgabe9_Thread myThread = new Aufgabe9_Thread(1, myArray);
        //myThread.run();


        Aufgabe9_Thread myThread2 = new Aufgabe9_Thread(2, Arrays.copyOfRange(myArray, 0 , myArray.length / 2));
        Aufgabe9_Thread myThread3 = new Aufgabe9_Thread(3, Arrays.copyOfRange(myArray, myArray.length / 2 , myArray.length));

        final long timeStart = System.currentTimeMillis();
        myThread.run();
        //myThread2.run();
        //myThread3.run();
        final long timeEnd = System.currentTimeMillis();
        System.out.println("Verlaufszeit der Schleife: " + (timeEnd - timeStart) + " Millisek.");


    }
}
