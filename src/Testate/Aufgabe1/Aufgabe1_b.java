package Testate.Aufgabe1;

import java.util.concurrent.Semaphore;

public class Aufgabe1_b extends Thread {

    // static variablen
    public static Semaphore[] sem_zug = new Semaphore[2];
    public static Semaphore mutex = new Semaphore(1, true);
    public static int[] zustand_zug = new int[2];
    public static int ctr = 0;
    // 0 -> nicht wartend
    // 1 -> wartend
    // 2 -> fahrend

    //object variablen
    public int id;
    public int id_andererZug;
    public float speed;

    public static void main(String[] args) {
        new Aufgabe1_b(0, 6000).start();
        new Aufgabe1_b(1, 1000).start();
    }

    public Aufgabe1_b(int id, float speed) {
        this.id = id;
        this.speed = speed;
        sem_zug[id] = new Semaphore(0, true);
        zustand_zug[id] = 0;

        if (id == 0) this.id_andererZug = 1;
        else this.id_andererZug = 0;

        System.out.println("Zug " + this.id + " fährt los");
    }

    @Override
    public void run() {

        try {
            while (true) {
                //Fahren auf eigener Strecke
                Thread.sleep((long) (this.speed * 0.6));

                this.enterLok();
                //Lock im mittleren Teilstück

                //Kritischer Abschnitt -> gemeinsames Gleis
                Thread.sleep((long) (this.speed * 0.3));
                System.out.println("Zug " + this.id + " befährt gemeinsames Gleis");
                //Kritischer Abschnitt Ende

                this.exitLok();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void enterLok() {
        try {
            //Prüfen, ob das gemeinsame gleis befahren werden darf
            mutex.acquire();

            //wenn noch kein Zug über das gemeinsame Gleis gefahren ist -> Zug 0 darf zu erst
            if (ctr == 0) {
                zustand_zug[0] = 2;
                sem_zug[0].release();
                ctr++;
                if (this.id == 1) zustand_zug[this.id] = 1;
            } else {
                //wenn gemeinsames Gleis frei ist
                if (zustand_zug[this.id_andererZug] != 2) {
                    zustand_zug[this.id] = 2;
                    sem_zug[this.id].release();
                }
                //wenn gemeinsames Gleis nicht frei ist
                else {
                    zustand_zug[this.id] = 1;
                }
            }
            mutex.release();
            sem_zug[this.id].acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exitLok() {
        try {
            mutex.acquire();
            zustand_zug[this.id] = 0;
            System.out.println("Zug " + this.id + " verlässt gemeinsames Gleis \n");

            //wenn anderer Zug wartet -> freischalten
            if (zustand_zug[this.id_andererZug] == 1) {
                sem_zug[this.id_andererZug].release();
            }
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void drive(){
        while (true){
            try {
                //Prüfen, ob das gemeinsame gleis befahren werden darf
                mutex.acquire();

                //wenn noch kein Zug über das gemeinsame Gleis gefahren ist -> Zug 0 darf zu erst
                if (ctr == 0) {
                    zustand_zug[0] = 2;
                    sem_zug[0].release();
                    ctr++;
                    if (this.id == 1) zustand_zug[this.id] = 1;
                } else {
                    //wenn gemeinsames Gleis frei ist
                    if (zustand_zug[this.id_andererZug] != 2) {
                        zustand_zug[this.id] = 2;
                        sem_zug[this.id].release();
                    }
                    //wenn gemeinsames Gleis nicht frei ist
                    else {
                        zustand_zug[this.id] = 1;
                    }
                }
                mutex.release();
                sem_zug[this.id].acquire();

                //Kritischer Abschnitt -> gemeinsames Gleis
                Thread.sleep((long) (this.speed * 0.3));
                System.out.println("Zug " + this.id + " befährt gemeinsames Gleis");
                //Kritischer Abschnitt Ende

                mutex.acquire();
                zustand_zug[this.id] = 0;
                System.out.println("Zug " + this.id + " verlässt gemeinsames Gleis \n");

                //wenn anderer Zug wartet -> freischalten
                if (zustand_zug[this.id_andererZug] == 1) {
                    sem_zug[this.id_andererZug].release();
                }
                mutex.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
