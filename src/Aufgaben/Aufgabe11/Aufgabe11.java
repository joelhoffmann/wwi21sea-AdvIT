package Aufgaben.Aufgabe11;

import java.util.concurrent.Semaphore;

public class Aufgabe11 extends Thread{
    public int philosophNumber;
    public static Semaphore[] gabeln = new Semaphore[5];

    public static void main(String[] args) {
        for(int i = 0; i < 5; i++){
            gabeln[i] = new Semaphore(1, true);
        }
        for(int i = 0; i < 5; i++){
            new Aufgabe11(i).start();
        }
    }

    public Aufgabe11(int philosophNumber){
        this.philosophNumber = philosophNumber;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 1000));
            eat(this.philosophNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void eat (int philosophNumber){
        if(philosophNumber == 4){
            try {
                gabeln[0].acquire();
                gabeln[philosophNumber].acquire();
                System.out.println("Philosoph " + this.philosophNumber + " isst jetzt");
                Thread.sleep(3000);
                System.out.println("Philosoph " + this.philosophNumber + " isst nicht mehr");
                gabeln[0].release();
                gabeln[philosophNumber].release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                gabeln[philosophNumber].acquire();
                gabeln[philosophNumber + 1].acquire();
                System.out.println("Philosoph " + this.philosophNumber + " isst jetzt");
                Thread.sleep(3000);
                System.out.println("Philosoph " + this.philosophNumber + " isst nicht mehr");
                gabeln[philosophNumber].release();
                gabeln[philosophNumber + 1].release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
