package Aufgaben.Aufgabe12;

public class Aufgabe12 extends Thread{

    public int id;
    public static Aufgabe12_monitor monitor = new Aufgabe12_monitor();


    public Aufgabe12(int id){
        this.id = id;
    }

    public static void main(String[] args) {
        for(int i = 0; i < 5; i++){
            new Aufgabe12(i).start();
        }
    }

    @Override
    public void run() {

        monitor.eat(this.id);
        //KA
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //KA end
        monitor.finishedEat(this.id);

    }

}
