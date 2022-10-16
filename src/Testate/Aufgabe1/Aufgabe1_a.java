package Testate.Aufgabe1;

import java.sql.SQLOutput;
import java.util.concurrent.Semaphore;

public class Aufgabe1_a extends Thread {

    public static Semaphore empty = new Semaphore(1, true);
    public static Semaphore full = new Semaphore(0, true);

    public int id;

    public Aufgabe1_a(int id) {
        this.id = id;
    }

    public static void main(String[] args) {
        new Aufgabe1_a(0).start();
        new Aufgabe1_a(1).start();
    }

    @Override
    public void run() {
        while (true) {

            try {
                if (this.id == 0) {
                    Thread.sleep(200);
                    this.enterLok1();
                } else {
                    Thread.sleep(2000);
                    this.enterLok2();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void enterLok1() {

        try {
            empty.acquire();

            System.out.println("0 fährt");
            Thread.sleep(200);
            System.out.println("0 raus");

            full.release();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterLok2() {
        try {
            full.acquire();
            System.out.println("1 fährt");
            Thread.sleep(500);
            System.out.println("1 raus");
            empty.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
