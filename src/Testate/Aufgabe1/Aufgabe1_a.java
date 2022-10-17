package Testate.Aufgabe1;

import java.util.concurrent.Semaphore;

public class Aufgabe1_a extends Thread {

    public static Semaphore full = new Semaphore(1, true);//gemeinsames Gleis
    public static Semaphore empty = new Semaphore(0, true);//Verbaucher queue
    public int id;
    public int speed;//Geschwindigkeit des Zuges

    public static void main(String[] args) {
        //Erstellung beider Züge
        new Aufgabe1_a(0, 2000).start();
        new Aufgabe1_a(1, 5000).start();
    }

    public Aufgabe1_a(int id, int speed) {
        this.id = id;
        this.speed = speed;
    }

    public void run() {
        while (true) {
            try {
                //Lock fährt auf eigenem Gleis
                Thread.sleep((long) (this.speed * 0.7)); // 0.7 -> länge des eigenen Gleises

                //enter lok0 auf gemeinsames Gleis
                if (this.id == 0) {
                    full.acquire();

                    //KA
                    System.out.println("Lok: " + this.id + " befährt das gleis");
                    Thread.sleep((long) (this.speed * 0.3));// 0.3 -> länge des gemeinsamen Gleises
                    System.out.println("Lok: " + this.id + " verlässt das gleis");
                    //KA end

                    //exit lok0 vom gemeinsamen Gleis
                    empty.release();

                }
                //enter lok1 auf gemeinsames Gleis
                else {
                    empty.acquire();

                    //KA
                    System.out.println("Lok: " + this.id + " befährt das gleis");
                    Thread.sleep((long) (this.speed * 0.3));// 0.3 -> länge des gemeinsamen Gleises
                    System.out.println("Lok: " + this.id + " verlässt das gleis");
                    //KA end

                    //exit lok1 vom gemeinsamen Gleis
                    full.release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
