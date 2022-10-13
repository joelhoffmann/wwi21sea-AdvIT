package Aufgaben.Aufgabe11;

import java.util.concurrent.Semaphore;

public class Aufgabe11_privateSem extends Thread {

    public int id;
    public static int[] p_state = new int[5];
    // 0 -> denkend
    // 1 -> hungrig
    // 2 -> essend
    public static Semaphore[] p_sem = new Semaphore[5];
    public static Semaphore mutex = new Semaphore(1, true);
    private int links1, links2, rechts1, rechts2 ;

    public static void main(String[] args) {
        for (int i = 0; i < p_state.length; i++) {
            p_state[i] = 0;
            p_sem[i] = new Semaphore(0, true);
        }
        for (int i = 0; i < 5; i++) {
            new Aufgabe11_privateSem(i).start();
        }
    }

    public Aufgabe11_privateSem(int id) {
        this.id = id;
        this.links1 = (id - 1 + 5) % 5;
        this.links2 = (id - 2 + 5) % 5;
        this.rechts1 = (id + 1 + 5) % 5;
        this.rechts2 = (id + 2 + 5) % 5;
    }

    @Override
    public void run() {
        try {
            mutex.acquire();
            //darf ich essen?
            if (p_state[this.links1] != 2 && p_state[this.rechts1] != 2) { // essen meine Nachbarn?
                //ja ich darf essen da meine Nachbarn nicht essen
                p_state[this.id] = 2;
                p_sem[this.id].release();
            } else {
                //nein ich darf nicht essen
                p_state[this.id] = 1;
                System.out.println("Philosoph " + this.id + " wartet");
            }
            mutex.release();
            p_sem[this.id].acquire();

            //kritischer Abschnitt -> essen
            System.out.println("Philosoph " + this.id + " isst jetzt");
            Thread.sleep(3000);

            //Ende Kritischer Abschnitt -> fertig mit essen
            mutex.acquire();
            System.out.println("Philosoph " + this.id + " hat fertig gegessen");
            p_state[this.id] = 0;

            //wenn links nicht isst und dessen linker partner auch nicht
            if (p_state[this.links1] == 1 && p_state[this.links2] != 2) {
                p_state[this.links1] = 2;
                p_sem[this.links1].release();
            }
            //wenn rechts nicht isst und dessen rechter partner auch nicht
            if (p_state[this.rechts1] == 1 && p_state[this.rechts2] != 2) {
                p_state[this.rechts1] = 2;
                p_sem[this.rechts1].release();
            }
            mutex.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
