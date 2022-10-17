package Testate.Aufgabe1;

import java.util.concurrent.Semaphore;

public class Aufgabe1_b extends Thread{

    public static Semaphore[] sem_zug = new Semaphore[2];
    public static Semaphore mutex = new Semaphore(1, true);
    public static int[] zustand_zug = new int[2];
    // 0 -> nicht wartend
    // 1 -> zug wartend
    // 2 -> zug auf gemeinsamen Gleis
    public static int ctr = 0; //für Fall, dass lock0 zuerst fahren darf

    public int id;
    public int id_andererZug;
    public int speed;

    public Aufgabe1_b(int id, int speed){
        this.id = id;
        sem_zug[id] = new Semaphore(0, true);
        if(id == 0) this.id_andererZug = 1;
        else this.id_andererZug = 0;
        this.speed = speed;
    }

    public static void main(String[] args) {
        new Aufgabe1_b(0, 2000).start();
        new Aufgabe1_b(1,5000).start();
    }

    @Override
    public void run() {

            try {
                while (true){
                //Lock fährt auf eigenem Gleis
                Thread.sleep((long)(this.speed * 0.7)); // 0.7 -> länge des eigenen Gleises

                this.enterLok();

                //KA
                Thread.sleep((long)(this.speed * 0.3));// 0.3 -> länge des gemeinsamen Gleises
                System.out.println("Lok: " +  this.id + " befährt das gleis");
                //KA end

                this.exitLok();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void enterLok(){
        try {
            mutex.acquire();

            //wenn noch kein Zug über Gleis gefahren
            if(ctr == 0){
                ctr++;
                zustand_zug[0] = 2;
                sem_zug[0].release();
                if(this.id == 1) {
                    zustand_zug[1] = 1;
                }
            }

            mutex.release();
            sem_zug[this.id].acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void exitLok(){
        try {
            mutex.acquire();
            zustand_zug[this.id] = 0;
            System.out.println("Lok: " +  this.id + " verlässt das gleis");

            //wenn anderer Zug wartet
            //if(zustand_zug[this.id_andererZug] == 1){
                sem_zug[this.id_andererZug].release();
            //}
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
