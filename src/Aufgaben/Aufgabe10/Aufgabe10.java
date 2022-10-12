package Aufgaben.Aufgabe10;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Aufgabe10{

    public static Queue myStaticQueue = new LinkedList<String>();
    public static Semaphore mutex = new Semaphore(1, true);
    static Runnable r = new Runnable() {
        @Override
        public void run() {
            while (true){
                try {
                    mutex.acquire();
                    //kritischer Abschnitt Start

                    myStaticQueue.remove();
                    myStaticQueue.add("fourth");

                    //kritischer Abschnitt Ende
                    mutex.release();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static void main(String[] args) {

        ArrayList<Thread> myThreads = new ArrayList<>();

        myStaticQueue.add("hallo");

        for(int i = 0; i < 400; i++){
            myThreads.add(new Thread(r));
            myThreads.get(i).start();
        }

        try {
            for (Thread thread : myThreads){
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(myStaticQueue.size());
    }

}
