package Aufgaben.Aufgabe9;

import java.util.Arrays;

public class Aufgabe9_thread_v2 extends  Thread{
    private int id;
    private int[] array;
    public int summe;

    public Aufgabe9_thread_v2 (int id, int[] array){
        this.id = id;
        this.array = array;
    }
    @Override
    public void run() {
        for(int i = 0; i < array.length; i++){
            summe += array[i];
        }
    }

    public static void main(String[] args) {
        int[] array = new int[2097152];
        int[] runs = {1,2,4,8,16,32,64,128};

        for(int i = 0; i < 2097152 ; i++){
            array[i] = (int) ( Math.random() * 10);
        }

        for(int i = 0; i < runs.length ; i++){
            calcSum(runs[i], array);
        }

    }

    public static void calcSum (int anzahl_threads, int[] array){

        Aufgabe9_thread_v2[] threads = new Aufgabe9_thread_v2[anzahl_threads];

        long timestampstart = System.currentTimeMillis();
        for(int i = 0; i < anzahl_threads ; i++){
            threads[i] = new Aufgabe9_thread_v2(i, Arrays.copyOfRange(array,(array.length/anzahl_threads * i)  , (array.length / anzahl_threads * (i + 1))));
            threads[i].start();
        }
        //wait for all threads to finish
        try {
            for(int i = 0; i < anzahl_threads ; i++){
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long timestampend = System.currentTimeMillis();

        int summe = 0;
        for(int i = 0; i < anzahl_threads ; i++){
            summe += threads[i].summe;
        }
        System.out.println("-----------------------");
        System.out.println("run: " + anzahl_threads);
        System.out.println("Summe threads: " + summe);
        System.out.println("zeit: " + (timestampend - timestampstart));
    }
}
