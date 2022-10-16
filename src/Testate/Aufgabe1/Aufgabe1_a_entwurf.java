package Testate.Aufgabe1;

import java.util.concurrent.Semaphore;

public class Aufgabe1_a_entwurf extends Thread{
    public static Semaphore lock0 = new Semaphore(1,true);
    public static Semaphore lock1 = new Semaphore(0,true);
    public static Semaphore gleis = new Semaphore(1, true);
    public static int ctr = 0;
    public int id;
    public int speed;

    public static void main(String[] args) {
        new Aufgabe1_a_entwurf(0, 5000).start();
        new Aufgabe1_a_entwurf(1, 2000).start();
    }

    public Aufgabe1_a_entwurf(int id, int speed){
        this.id = id;
        this.speed = speed;
    }

    public void run(){
        while(true){
        try {
            System.out.println("Lok " + this.id + " fährt los");
            Thread.sleep(speed * (2/3));
            System.out.println("Lok " + this.id + " will über gemeinsames gleis");
            if (ctr == 0) {
                if (this.id == 0) {
                    lock0.acquire();
                    System.out.println("Lok " + this.id + " fährt über gemeinsames gleis");
                    Thread.sleep(speed * (1/3));
                    System.out.println("Lok " + this.id + " verlässt das gemeinsames gleis");
                    lock1.release();
                    lock0.release();
                    ctr++;
                }
                if (this.id == 1) {
                    lock1.acquire();
                    System.out.println("Lok " + this.id + " fährt über gemeinsames gleis");
                    Thread.sleep(speed * (1/3));
                    System.out.println("Lok " + this.id + " verlässt das gemeinsames gleis");
                    lock1.release();
                    ctr++;
                }
            } else {
                gleis.acquire();
                System.out.println("Lok " + this.id + " fährt über gemeinsames gleis");
                Thread.sleep(1000);
                System.out.println("Lok " + this.id + " verlässt das gemeinsames gleis");
                ctr++;
                gleis.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        }
    }
}
